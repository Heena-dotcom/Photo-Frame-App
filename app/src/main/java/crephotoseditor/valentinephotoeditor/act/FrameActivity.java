package crephotoseditor.valentinephotoeditor.act;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.facebook.ads.Ad;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdError;
import com.facebook.ads.NativeAdBase;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeBannerAd;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;

import crephotoseditor.valentinephotoeditor.Glob.Glob;
import crephotoseditor.valentinephotoeditor.Glob.ImageUtil;
import crephotoseditor.valentinephotoeditor.R;
import crephotoseditor.valentinephotoeditor.adapter.FrameAdapter;
import crephotoseditor.valentinephotoeditor.model.FrameListModel;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

//import static crephotoseditor.valentinephotoeditor.Glob.Glob.inflateAd;

public class FrameActivity extends AppCompatActivity implements View.OnClickListener, FrameAdapter.clickFrame {
    private static final String TAG = "logog";
    public ArrayList<FrameListModel> frameList;
    public static final int REQ_EDITIMAGE = 23;

    private RecyclerView rvFrameList;
    FrameAdapter frameAdapter;
    ImageView back;
    private com.facebook.ads.AdView adView;
    private int pos;
    private InterstitialAd mInterstitialAdMob;

    private LinearLayout mAdView;
    private RelativeLayout mAdChoicesContainer;
    private RelativeLayout mNativeBannerAdContainer;
    private @Nullable
    NativeBannerAd mNativeBannerAd;
    private @Nullable
    AdChoicesView mAdChoicesView;
    private boolean isAdViewAdded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame);

//        if (Glob.fnativebanner != null && !Glob.fnativebanner.equals("")) {
//            showNativeBanner();
//        }
        if (Glob.ainterstitial1 != null && !Glob.ainterstitial1.equals("")) {
            mInterstitialAdMob = showAdmobFullAd();
            loadAdmobAd();
        }

        frameList = ImageUtil.setListForFrame();
        rvFrameList = (RecyclerView) findViewById(R.id.rvFrameList);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);

        rvFrameList.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(FrameActivity.this, 2, GridLayoutManager.VERTICAL, false);
        rvFrameList.setLayoutManager(layoutManager);

        frameAdapter = new FrameAdapter(FrameActivity.this, frameList, this);
        rvFrameList.setAdapter(frameAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                overridePendingTransition(R.anim.leave_transition, 0);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.leave_transition, 0);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 69) {
                handleCropResult(data);
            } else {
                EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
                    public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                        onPhotosReturned(imageFile);
                    }


                    public void onCanceled(EasyImage.ImageSource source, int type) {
                        if (source == EasyImage.ImageSource.CAMERA) {
                            File photoFile = EasyImage.lastlyTakenButCanceledPhoto(FrameActivity.this);
                            if (photoFile != null) {
                                photoFile.delete();
                            }
                        }
                    }
                });
            }
        }
        if (resultCode == 96) {
            handleCropError(data);
        }

        if (requestCode==REQ_EDITIMAGE){
            setResult(RESULT_OK);
            finish();
        }

    }


    private void onPhotosReturned(File returnedPhotos) {
        advancedConfig(basisConfig(UCrop.of(Uri.fromFile(returnedPhotos), Uri.fromFile(new File(getCacheDir(), "CropImage.jpg")))))
                .withAspectRatio(1, 1)
                .start(this);
    }

    private UCrop basisConfig(@NonNull UCrop uCrop) {
        return uCrop.useSourceImageAspectRatio();
    }

    private UCrop advancedConfig(@NonNull UCrop uCrop) {
        UCrop.Options options = new UCrop.Options();
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        return uCrop.withOptions(options);
    }

    private void handleCropResult(@NonNull Intent result) {
        Uri resultUri = UCrop.getOutput(result);
        if (resultUri != null) {
            Intent intent = new Intent(this, EditImageActivity.class);
            intent.setData(resultUri);
            intent.putExtra("frame", pos);
            startActivityForResult(intent, REQ_EDITIMAGE);
            showAdmobInterstitial();

        }
    }

    private void handleCropError(@NonNull Intent result) {
        Log.i("log", UCrop.getError(result).getMessage());
    }

    @Override
    public void clickedFrame(int pos) {
        this.pos=pos;
        openGallery();
    }

    private void openGallery() {
        ImageUtil.createDirIfNotExists(getResources().getString(R.string.app_name));
        try {
            EasyImage.openGallery(FrameActivity.this, 0);
        } catch (Exception e) {
            Log.i("log", e.getMessage());
        }
    }

    //AdMob InterstitialAds start
    private com.google.android.gms.ads.InterstitialAd showAdmobFullAd() {
        com.google.android.gms.ads.InterstitialAd interstitialAd = new com.google.android.gms.ads.InterstitialAd(getApplicationContext());
        interstitialAd.setAdUnitId(Glob.ainterstitial1);
        interstitialAd.setAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdClosed() {
                loadAdmobAd();
            }

            @Override
            public void onAdLoaded() {

            }

            @Override
            public void onAdOpened() {
                //   super.onAdOpened();
            }
        });
        return interstitialAd;
    }

    private void loadAdmobAd() {
        this.mInterstitialAdMob.loadAd(new AdRequest.Builder().build());
    }

    private void showAdmobInterstitial() {
        if (this.mInterstitialAdMob != null && this.mInterstitialAdMob.isLoaded()) {
            this.mInterstitialAdMob.show();
        }
    }
// Admob end

//    public void showNativeBanner() {
//        LayoutInflater inflater = LayoutInflater.from(FrameActivity.this);
//
//        mNativeBannerAdContainer = (RelativeLayout) findViewById(R.id.native_banner_ad_container);
//
//        mAdView = (LinearLayout) inflater.inflate(R.layout.native_banner_ad_layout, mNativeBannerAdContainer, false);
//
//        mAdChoicesContainer = (RelativeLayout) mAdView.findViewById(R.id.ad_choices_container);
//        mNativeBannerAd = new NativeBannerAd(FrameActivity.this, Glob.fnativebanner);
//
//        mNativeBannerAd.setAdListener(FrameActivity.this);
//
//        mNativeBannerAd.loadAd(NativeAdBase.MediaCacheFlag.ALL);
//
//    }


}
