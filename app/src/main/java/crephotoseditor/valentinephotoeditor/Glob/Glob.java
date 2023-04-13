package crephotoseditor.valentinephotoeditor.Glob;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.ads.AdIconView;
import com.facebook.ads.NativeBannerAd;

import java.util.ArrayList;
import java.util.List;

import crephotoseditor.valentinephotoeditor.R;


public class Glob {
    public static String acc_link = "https://play.google.com/store/apps/developer?id=Crephotos";
    public static String privacy_policy = "https://helpofcrephotos.blogspot.com/";
    public static ArrayList<String> listAppIcon = new ArrayList<String>();
    public static ArrayList<String> listAppName = new ArrayList<String>();
    public static ArrayList<String> listAppLink = new ArrayList<String>();

    public static ArrayList<String> listAppIconExit = new ArrayList<String>();
    public static ArrayList<String> listAppNameExit = new ArrayList<String>();
    public static ArrayList<String> listAppLinkExit = new ArrayList<String>();

    public static String fnativebanner = "";
    public static String fnative = "";
    public static String finterstitial1 = "";
    public static String finterstitial2 = "";
    public static String finterstitial3 = "";

    public static String anative = "";
    public static String abanner = "";
    public static String ainterstitial1 = "";
    public static String ainterstitial2 = "";

//    public static void inflateAd(NativeBannerAd nativeBannerAd, View adView, final Context context) {
//        // Create native UI using the ad metadata.
//        TextView nativeAdTitle = (TextView) adView.findViewById(R.id.native_ad_title);
//        TextView nativeAdSocialContext =
//                (TextView) adView.findViewById(R.id.native_ad_social_context);
//        TextView sponsoredLabel = (TextView) adView.findViewById(R.id.native_ad_sponsored_label);
//        AdIconView nativeAdIconView = (AdIconView) adView.findViewById(R.id.native_icon_view);
//        Button nativeAdCallToAction = (Button) adView.findViewById(R.id.native_ad_call_to_action);
//
//        // Setting the Text
//        nativeAdCallToAction.setText(nativeBannerAd.getAdCallToAction());
//        nativeAdCallToAction.setVisibility(
//                nativeBannerAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
//        nativeAdTitle.setText(nativeBannerAd.getAdvertiserName());
//        nativeAdSocialContext.setText(nativeBannerAd.getAdSocialContext());
//
//        // You can use the following to specify the clickable areas.
//        List<View> clickableViews = new ArrayList<>();
//        clickableViews.add(nativeAdCallToAction);
//        nativeBannerAd.registerViewForInteraction(adView, nativeAdIconView, clickableViews);
//
//        sponsoredLabel.setText(R.string.sponsored);
//    }

}
