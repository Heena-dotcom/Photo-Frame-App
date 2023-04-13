package crephotoseditor.valentinephotoeditor.act;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.facebook.ads.AdView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;

import java.io.File;
import java.util.ArrayList;

import crephotoseditor.valentinephotoeditor.Glob.Glob;
import crephotoseditor.valentinephotoeditor.Glob.ImageUtil;
import crephotoseditor.valentinephotoeditor.R;
import crephotoseditor.valentinephotoeditor.adapter.MyCreationAdapter;


public class MyWorkActivity extends AppCompatActivity implements View.OnClickListener, MyCreationAdapter.RecyclerViewClickListener, MyCreationAdapter.checkSize {

    ImageView back_my_creation;
    public static ArrayList<String> file_path = new ArrayList<String>();
    RecyclerView my_creation_recycler;
    MyCreationAdapter myCreationAdapter;
    LinearLayout llnovideo;
    private com.google.android.gms.ads.InterstitialAd mInterstitialAdMob;
    private AdView adView;
    private com.google.android.gms.ads.AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mywork);
        bindView();
        setRecyclerView();
        if (Glob.abanner != null && !Glob.abanner.equals("")) {
            showAdmobBanner();
        }
        if (Glob.ainterstitial1 != null && !Glob.ainterstitial1.equals("")) {
            mInterstitialAdMob = showAdmobFullAd();
            loadAdmobAd();
        }

    }

    private void bindView() {
        back_my_creation = (ImageView) findViewById(R.id.back_my_creation);
        my_creation_recycler = (RecyclerView) findViewById(R.id.my_creation_recycler);
        llnovideo = (LinearLayout) findViewById(R.id.llnovideo);
        back_my_creation.setOnClickListener(this);

    }

    private void setRecyclerView() {
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        my_creation_recycler.setLayoutManager(mLayoutManager);
//        my_creation_recycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        file_path.clear();
        file_path = ImageUtil.getfile(new File(ImageUtil.RootPath + "/" + getResources().getString(R.string.app_name) + "/"), "image");

        if (file_path.size() == 0) {
            llnovideo.setVisibility(View.VISIBLE);
        } else {
            myCreationAdapter = new MyCreationAdapter(file_path, MyWorkActivity.this, this, this);
            my_creation_recycler.setAdapter(myCreationAdapter);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_my_creation:
                finish();
                overridePendingTransition(R.anim.leave_transition, 0);
                break;
        }
    }


    @Override
    public void recyclerclicked(View v, int position) {
        Intent i = new Intent(MyWorkActivity.this, FullImageActivity.class);
        i.putExtra("position", position);
        startActivity(i);
        showAdmobInterstitial();
        overridePendingTransition(R.anim.enter_transition, 0);
    }


    @Override
    public void sendSize(int size) {
        if (size == 0) {
            llnovideo.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.leave_transition, 0);
    }

    @Override
    public void onPause() {

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

    private void showAdmobBanner() {
        mAdView = new com.google.android.gms.ads.AdView(MyWorkActivity.this);
        mAdView.setAdSize(AdSize.BANNER);
        mAdView.setAdUnitId(Glob.abanner);
        final RelativeLayout layout = findViewById(R.id.loutadMobView);
        layout.addView(mAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                layout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                layout.setVisibility(View.GONE);
            }
        });
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
    //AdMob InterstitialAds End

}
