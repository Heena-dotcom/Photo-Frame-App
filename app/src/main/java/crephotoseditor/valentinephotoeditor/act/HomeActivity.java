package crephotoseditor.valentinephotoeditor.act;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdError;
import com.facebook.ads.AdIconView;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.MediaViewListener;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdBase.NativeComponentTag;
import com.facebook.ads.NativeAdListener;
import com.google.android.gms.ads.AdRequest;
import com.u.securekeys.SecureEnvironment;
import com.u.securekeys.annotation.SecureKey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import crephotoseditor.valentinephotoeditor.Glob.ConnectivityChangeReceiver;
import crephotoseditor.valentinephotoeditor.Glob.Glob;
import crephotoseditor.valentinephotoeditor.Glob.ImageUtil;
import crephotoseditor.valentinephotoeditor.Glob.ReqAPIforResponse;
import crephotoseditor.valentinephotoeditor.R;
import crephotoseditor.valentinephotoeditor.adapter.AppAdsAdapter;
import crephotoseditor.valentinephotoeditor.notiOS.GCMUtils;
import crephotoseditor.valentinephotoeditor.notiOS.RegistrationIntentService;
import pl.aprilapps.easyphotopicker.EasyImage;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, NativeAdListener {

    private static final int REQ_EXIT = 256;
    private static final int REQ_FRAME = 257;

    private static final int MY_REQUEST_CODE_MY_CREATION = 99;
    private static final int MY_REQUEST_CODE_EDIT = 100;
    private static final int MY_REQUEST_CODE_SHARE = 101;
    private static final String TAG = "fblog";
    RecyclerView hvApplist;
    LinearLayout loutEdit, loutCreation;
    AppAdsAdapter appListAdapter;
    private BroadcastReceiver mNetworkBroadcastReceiver, mRegistrationBroadcastReceiver;
    private com.google.android.gms.ads.InterstitialAd mInterstitialAdMob;

    private InterstitialAd interstitialAd;
    ImageView ivShare, ivRate, ivPolicy;

    private LinearLayout adChoicesContainer;
    private LinearLayout nativeAdContainer;
    private LinearLayout adView;
    private NativeAd nativeAd;
    private AdChoicesView adChoicesView;

    @Override
    @SecureKey(key = "client-secret", value = "http://securetechnoweb.com/rr/service/app_link/crephotos_splash/" + GCMUtils.APP_ID)
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//        AdSettings.addTestDevice("9a56736f-4c71-4839-b1b4-4fc958518120");
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_bg));

        ActionBar ab = getSupportActionBar();
        TextView tv = new TextView(getApplicationContext());

        // Create a LayoutParams for TextView
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, // Width of TextView
                RelativeLayout.LayoutParams.WRAP_CONTENT); // Height of TextView

        // Apply the layout parameters to TextView widget
        tv.setLayoutParams(lp);

        // Set text to display in TextView
        tv.setText(ab.getTitle()); // ActionBar title text

        tv.setTextColor(Color.WHITE);

        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);

        // Set the ActionBar display option
        ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        // Finally, set the newly created TextView as ActionBar custom view
        ab.setCustomView(tv);

        hvApplist = (RecyclerView) findViewById(R.id.hvApplist);
        loutEdit = (LinearLayout) findViewById(R.id.loutEdit);
        loutCreation = (LinearLayout) findViewById(R.id.loutCreation);
        ivShare = findViewById(R.id.ivShare);
        ivRate = findViewById(R.id.ivRate);
        ivPolicy = findViewById(R.id.ivPolicy);

        ivShare.setOnClickListener(this);
        ivRate.setOnClickListener(this);
        ivPolicy.setOnClickListener(this);
        loutEdit.setOnClickListener(this);
        loutCreation.setOnClickListener(this);

        Glob.listAppIcon.clear();
        Glob.listAppName.clear();
        Glob.listAppLink.clear();
        if (Glob.listAppName.size() > 0) {
            showAppsAd();
        } else {
            receiveResponse();
        }

        if (Glob.ainterstitial1 != null && !Glob.ainterstitial1.equals("")) {
            mInterstitialAdMob = showAdmobFullAd();
            loadAdmobAd();
        }
        if (Glob.fnative != null && !Glob.fnative.equals("")) {
            showNativeAd();
        }
        if (Glob.finterstitial1 != null && !Glob.finterstitial1.equals("")) {
            loadFBInterstitial();
        }

        hvApplist.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(HomeActivity.this, 3, LinearLayoutManager.VERTICAL, false);
        hvApplist.setLayoutManager(layoutManager);

        if (isNetAvailable()) {
            findViewById(R.id.txtInfo).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.txtInfo).setVisibility(View.GONE);
        }
    }

    private void gotoMyEdit() {
        Intent intent = new Intent(this, FrameActivity.class);
        startActivityForResult(intent, REQ_FRAME);
        showAdmobInterstitial();
    }

    private void gotoMyCreation() {
        ImageUtil.createDirIfNotExists(getResources().getString(R.string.app_name));
        Intent i = new Intent(HomeActivity.this, MyWorkActivity.class);
        startActivity(i);
        showFBInterstitial();
    }

    public void setNetworkdetail() {
        requestToken();
    }

    private void requestToken() {
        if (!GCMUtils.getPrefBoolean(HomeActivity.this, GCMUtils.TOKEN)) {
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNetworkBroadcastReceiver = new ConnectivityChangeReceiver(HomeActivity.this);
        registerReceiver(mNetworkBroadcastReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(GCMUtils.REGISTRATION_COMPLETE));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mNetworkBroadcastReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }


    private void showAppsAd() {
        appListAdapter = new AppAdsAdapter(this, Glob.listAppLink, Glob.listAppIcon, Glob.listAppName);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hvApplist.setAdapter(appListAdapter);
            }
        });
    }

    private void share() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/*");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "You should also try " + getResources().getString(R.string.app_name) + " https://play.google.com/store/apps/details?id=" + getPackageName());
        startActivity(Intent.createChooser(shareIntent, "Share Image using"));
    }

    public void rateUs() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);

        try {
            startActivity(myAppLinkToMarket);

        } catch (ActivityNotFoundException e) {

            //the device hasn't installed Google Play
            Toast.makeText(HomeActivity.this, "You don't have Google Play installed", Toast.LENGTH_LONG).show();
        }
    }

    private void moreapps() {
        Uri uri = Uri.parse(Glob.acc_link);
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);

        try {
            startActivity(myAppLinkToMarket);

        } catch (ActivityNotFoundException e) {

            //the device hasn't installed Google Play
            Toast.makeText(HomeActivity.this, "You don't have Google Play installed", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_drawer_drawer, menu);//Menu Resource, Menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_share:
                share();
                return true;
            case R.id.nav_rate_us:
                rateUs();
                return true;
            case R.id.nav_more_apps:
                moreapps();
                return true;
            case R.id.nav_privacy_policy:
                showPoilcy();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showPoilcy() {
        if (isNetAvailable() && Glob.privacy_policy != null) {
            Intent i = new Intent(HomeActivity.this, ShowPolicyActivity.class);
            startActivity(i);
        } else
            Toast.makeText(this, "Check your Internet connection..", Toast.LENGTH_SHORT).show();
    }

    private void receiveResponse() {
        Glob.listAppIcon.clear();
        Glob.listAppName.clear();
        Glob.listAppLink.clear();

        new Thread(new Runnable() {
            @Override
            public void run() {
                ReqAPIforResponse.callGet("", "", false, new ReqAPIforResponse.ResultCallBack() {
                    @Override
                    public void onSuccess(int responseCode, String strResponse) {
                        JSONObject json = null;
                        try {
                            json = new JSONObject(strResponse);
                            JSONArray jArray = json.getJSONArray("data");
                            Log.d(TAG, "onSuccess: data0" +jArray);
                            Glob.acc_link = json.getString("ac_link");
                            Glob.privacy_policy = json.getString("privacy_link");

                            if (json.getBoolean("adsstatus")) {
                                Log.d("splash if", "onSuccess: in if");

                                Glob.fnativebanner = json.getString("fbanner");
                                Glob.fnative = json.getString("fnative");
                                Glob.finterstitial1 = json.getString("finterstitial1");
                                Glob.finterstitial2 = json.getString("finterstitial2");
                                Glob.finterstitial3 = json.getString("finterstitial3");

                                Glob.anative = json.getString("gnative");
                                Glob.abanner = json.getString("gbanner");
                                Glob.ainterstitial1 = json.getString("ginterstitial1");
                                Glob.ainterstitial2 = json.getString("ginterstitial2");
                                Log.d(TAG, "onSuccess: idid" +Glob.fnative);
                            } else {
                                Log.d("splash else", "onSuccess: in if");

                                Glob.fnativebanner = "";
                                Glob.fnative = "";
                                Glob.finterstitial1 = "";
                                Glob.finterstitial2 = "";
                                Glob.finterstitial3 = "";

                                Glob.anative = "";
                                Glob.abanner = "";
                                Glob.ainterstitial1 = "";
                                Glob.ainterstitial2 = "";
                            }

                            for (int i = 0; i < jArray.length(); i++) {
                                JSONObject objJson = jArray.getJSONObject(i);
                                String app_name = objJson.getString("application_name");
                                String app_links = objJson.getString("application_link");
                                String app_icon = objJson.getString("icon_link");

                                Glob.listAppIcon.add(app_icon);
                                Glob.listAppName.add(app_name);
                                Glob.listAppLink.add(app_links);
                            }
                            showAppsAd();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled() {

                    }

                    @Override
                    public void onFailure(int responseCode, String strResponse) {

                    }

                }, SecureEnvironment.getString("client-secret"));

            }
        }).start();
    }

    public boolean isNetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if ((mobile != null && mobile.isConnectedOrConnecting())
                    || (wifi != null && wifi.isConnectedOrConnecting()))
                return true;
            else
                return false;
        } else
            return false;
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (isNetAvailable()) {
            Intent i = new Intent(HomeActivity.this, LeaveActivity.class);
            startActivityForResult(i, REQ_EXIT);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQ_EXIT:
                    finish();
                    break;
                case REQ_FRAME:
                    Intent intent = new Intent(this, SaveandShareActivity.class);
                    startActivity(intent);
                    showFBInterstitial();
                    break;
            }

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_REQUEST_CODE_MY_CREATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    gotoMyCreation();
                } else {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_REQUEST_CODE_MY_CREATION);
                    }
                }
                break;
            case MY_REQUEST_CODE_EDIT:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    gotoMyEdit();
                } else {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_REQUEST_CODE_EDIT);
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                return;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivShare:
                share();
                break;
            case R.id.ivRate:
                rateUs();
                break;
            case R.id.ivPolicy:
                showPoilcy();
                break;
            case R.id.loutEdit:
                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        gotoMyEdit();
                    } else if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_REQUEST_CODE_EDIT);
                    }
                } else {
                    gotoMyEdit();
                }
                break;
            case R.id.loutCreation:
                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        gotoMyCreation();
                    } else if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_REQUEST_CODE_MY_CREATION);
                    }
                } else {
                    gotoMyCreation();
                }
                break;
        }

    }

    public void loadFBInterstitial() {
        interstitialAd = new InterstitialAd(getApplicationContext(), Glob.finterstitial1);
        interstitialAd.loadAd();

        interstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                loadFBInterstitial();
            }

            @Override
            public void onError(Ad ad, AdError adError) {
            }

            @Override
            public void onAdLoaded(Ad ad) {
            }

            @Override
            public void onAdClicked(Ad ad) {
            }

            @Override
            public void onLoggingImpression(Ad ad) {
            }
        });


    }

    public void showFBInterstitial() {
        if (interstitialAd != null && interstitialAd.isAdLoaded())
            interstitialAd.show();
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

    public void showNativeAd() {
        nativeAdContainer = (LinearLayout) findViewById(R.id.nativeAdContainer);
        if (!ImageUtil.CheckNet(this)) {
            nativeAdContainer.setVisibility(View.GONE);
            return;
        }

        LayoutInflater inflater = LayoutInflater.from(HomeActivity.this);

        adView = (LinearLayout) inflater.inflate(R.layout.native_ad_unit, nativeAdContainer, false);
        nativeAdContainer.addView(adView);

        adChoicesContainer = (LinearLayout) findViewById(R.id.ad_choices_container);
        nativeAd = new NativeAd(HomeActivity.this, Glob.fnative);
        // Set a listener to get notified when the ad was loaded.
        nativeAd.setAdListener(HomeActivity.this);
        nativeAd.loadAd();

    }

    @Override
    public void onError(Ad ad, AdError error) {
        nativeAdContainer.setVisibility(View.GONE);
        Log.d(TAG, "ad error " + error.getErrorMessage() + error.getErrorCode());

    }

    @Override
    public void onAdClicked(Ad ad) {
        Log.d(TAG, "onAdClicked: adclicked");
    }

    @Override
    public void onLoggingImpression(Ad ad) {
        Log.d(TAG, "onLoggingImpression");
    }

    @Override
    public void onMediaDownloaded(Ad ad) {
        if (nativeAd == ad) {
            Log.d(TAG, "onMediaDownloaded");
        }
    }

    @Override
    public void onAdLoaded(Ad ad) {
        Log.d(TAG, "onAdLoaded: ");
        if (nativeAd == null || nativeAd != ad) {
            // Race condition, load() called again before last ad was displayed
            return;
        }

        if (adView == null) {
            return;
        }

        // Unregister last ad
        nativeAd.unregisterView();

        if (adChoicesView == null && adChoicesContainer != null) {
            adChoicesView = new AdChoicesView(HomeActivity.this, nativeAd, true);
            adChoicesContainer.addView(adChoicesView, 0);
        }

        inflateAd(nativeAd, adView, HomeActivity.this);
        nativeAd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    int i = view.getId();
                    if (i == R.id.native_ad_call_to_action) {
                        Log.d(TAG, "Call to action button clicked");
                    } else if (i == R.id.native_ad_media) {
                        Log.d(TAG, "Main image clicked");
                    } else {
                        Log.d(TAG, "Other ad component clicked");
                    }
                }
                return false;
            }
        });
    }

    public static void inflateAd(NativeAd nativeAd, View adView, final Context context) {
        // Create native UI using the ad metadata.
        AdIconView nativeAdIcon = (AdIconView) adView.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = (TextView) adView.findViewById(R.id.native_ad_title);
        TextView nativeAdBody = (TextView) adView.findViewById(R.id.native_ad_body);
        MediaView nativeAdMedia = (MediaView) adView.findViewById(R.id.native_ad_media);
        TextView sponsoredLabel = (TextView) adView.findViewById(R.id.native_ad_sponsored_label);

        nativeAdMedia.setListener(new MediaViewListener() {
            @Override
            public void onVolumeChange(MediaView mediaView, float volume) {
                Log.i(HomeActivity.class.toString(),
                        "MediaViewEvent: Volume " + volume);
            }

            @Override
            public void onPause(MediaView mediaView) {
                Log.i(HomeActivity.class.toString(),
                        "MediaViewEvent: Paused");
            }

            @Override
            public void onPlay(MediaView mediaView) {
                Log.i(HomeActivity.class.toString(), "MediaViewEvent: Play");
            }

            @Override
            public void onFullscreenBackground(MediaView mediaView) {
                Log.i(HomeActivity.class.toString(),
                        "MediaViewEvent: FullscreenBackground");
            }

            @Override
            public void onFullscreenForeground(MediaView mediaView) {
                Log.i(HomeActivity.class.toString(),
                        "MediaViewEvent: FullscreenForeground");
            }

            @Override
            public void onExitFullscreen(MediaView mediaView) {
                Log.i(HomeActivity.class.toString(),
                        "MediaViewEvent: ExitFullscreen");
            }

            @Override
            public void onEnterFullscreen(MediaView mediaView) {
                Log.i(HomeActivity.class.toString(),
                        "MediaViewEvent: EnterFullscreen");
            }

            @Override
            public void onComplete(MediaView mediaView) {
                Log.i(HomeActivity.class.toString(), "MediaViewEvent: Completed");
            }
        });
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

        // Setting the Text
        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdTitle.setText(nativeAd.getAdvertiserName());
        nativeAdBody.setText(nativeAd.getAdBodyText());
        sponsoredLabel.setText(R.string.sponsored);

        // You can use the following to specify the clickable areas.
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdIcon);
        clickableViews.add(nativeAdMedia);
        clickableViews.add(nativeAdCallToAction);
        nativeAd.registerViewForInteraction(
                adView,
                nativeAdMedia,
                nativeAdIcon,
                clickableViews);

        // Optional: tag views
        NativeComponentTag.tagView(nativeAdIcon, NativeComponentTag.AD_ICON);
        NativeComponentTag.tagView(nativeAdTitle, NativeComponentTag.AD_TITLE);
        NativeComponentTag.tagView(nativeAdBody, NativeComponentTag.AD_BODY);
        NativeComponentTag.tagView(nativeAdSocialContext, NativeComponentTag.AD_SOCIAL_CONTEXT);
        NativeComponentTag.tagView(nativeAdCallToAction, NativeComponentTag.AD_CALL_TO_ACTION);

    }

    @Override
    public void onDestroy() {
        EasyImage.clearConfiguration(this);

        if (nativeAd != null) {
            nativeAd.unregisterView();
            nativeAd.destroy();
        }

        super.onDestroy();
    }
}



