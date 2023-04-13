package crephotoseditor.valentinephotoeditor.act;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.facebook.ads.NativeAdBase;
import com.facebook.ads.NativeAdListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import crephotoseditor.valentinephotoeditor.BuildConfig;
import crephotoseditor.valentinephotoeditor.Glob.Glob;
import crephotoseditor.valentinephotoeditor.Glob.ImageUtil;
import crephotoseditor.valentinephotoeditor.R;

public class SaveandShareActivity extends AppCompatActivity implements View.OnClickListener, NativeAdListener {
    private static final String TAG = "loglogh";
    LinearLayout save_ll, insta_ll, fb_ll, twitter_ll, whatsapp_ll, email_ll, more_ll;
    ImageView img_share, back, home_share;
    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;


    private NativeAd nativeAd;
    private LinearLayout adView;
    private LinearLayout nativeAdContainer;
    private InterstitialAd interstitialAd;
    private AdChoicesView adChoicesView;
    private LinearLayout adChoicesContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_and_share);

        bindView();
        if (Glob.fnative != null && !Glob.fnative.equals("")) {
            showNativeAd();        }
        if (Glob.finterstitial1 != null && !Glob.finterstitial1.equals("")) {
            loadFBInterstitial();
        }

        mShortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);
    }


    private void bindView() {
        save_ll = (LinearLayout) findViewById(R.id.save_ll);
        insta_ll = (LinearLayout) findViewById(R.id.insta_ll);
        fb_ll = (LinearLayout) findViewById(R.id.fb_ll);
        twitter_ll = (LinearLayout) findViewById(R.id.twitter_ll);
        whatsapp_ll = (LinearLayout) findViewById(R.id.whatsapp_ll);
        email_ll = (LinearLayout) findViewById(R.id.email_ll);
        more_ll = (LinearLayout) findViewById(R.id.more_ll);
        img_share = (ImageView) findViewById(R.id.img_share);
        back = (ImageView) findViewById(R.id.back);
        home_share = (ImageView) findViewById(R.id.home_share);


        img_share.setImageURI(Uri.parse(EditImageActivity.imagePath));

        save_ll.setOnClickListener(this);
        insta_ll.setOnClickListener(this);
        fb_ll.setOnClickListener(this);
        twitter_ll.setOnClickListener(this);
        whatsapp_ll.setOnClickListener(this);
        email_ll.setOnClickListener(this);
        more_ll.setOnClickListener(this);
        img_share.setOnClickListener(this);
        back.setOnClickListener(this);
        home_share.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        final Intent shareIntent1 = new Intent(Intent.ACTION_SEND);
        Uri photoURI = FileProvider.getUriForFile(SaveandShareActivity.this,
                BuildConfig.APPLICATION_ID + ".provider",
                new File(EditImageActivity.imagePath));
        shareIntent1.setType("image/*");
        shareIntent1.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.app_name) + " https://play.google.com/store/apps/details?id=" + getPackageName());

        shareIntent1.putExtra(Intent.EXTRA_STREAM, photoURI);
        switch (v.getId()) {

            case R.id.back:
                finish();
                overridePendingTransition(R.anim.leave_transition, 0);
                showFBInterstitial();
                break;

            case R.id.home_share:
                finish();
                overridePendingTransition(R.anim.leave_transition, 0);
                showFBInterstitial();
                break;

            case R.id.img_share:
                zoomImageFromThumb(img_share, EditImageActivity.imagePath);
                break;

            case R.id.save_ll:
                Toast.makeText(this, "Image saved at:" + EditImageActivity.imagePath, Toast.LENGTH_SHORT).show();
                break;

            case R.id.insta_ll:
                try {
                    if (isPackageInstalled("com.instagram.android", getPackageManager())) {
                        shareIntent1.setPackage("com.instagram.android");
                        startActivity(shareIntent1);
                    } else {
                        Toast.makeText(SaveandShareActivity.this, "Instagram doesn't installed", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(SaveandShareActivity.this, "Instagram doesn't installed", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.fb_ll:
                try {
                    if (isPackageInstalled("com.facebook.katana", getPackageManager())) {
                        shareIntent1.setPackage("com.facebook.katana");
                        startActivity(shareIntent1);
                    } else {
                        Toast.makeText(SaveandShareActivity.this, "Facebook doesn't installed", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(SaveandShareActivity.this, "Facebook doesn't installed", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.twitter_ll:
                try {
                    if (isPackageInstalled("com.twitter.android", getPackageManager())) {
                        shareIntent1.setPackage("com.twitter.android");
                        startActivity(shareIntent1);
                    } else {
                        Toast.makeText(SaveandShareActivity.this, "Twitter doesn't installed", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(SaveandShareActivity.this, "Twitter doesn't installed", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.whatsapp_ll:
                try {
                    if (isPackageInstalled("com.whatsapp", getPackageManager())) {
                        shareIntent1.setPackage("com.whatsapp");
                        startActivity(shareIntent1);
                    } else {
                        Toast.makeText(SaveandShareActivity.this, "WhatsApp doesn't installed", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(SaveandShareActivity.this, "WhatsApp doesn't installed", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.email_ll:
                shareIntent1.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
                startActivity(Intent.createChooser(shareIntent1, "Share this using"));
                break;


            case R.id.more_ll:
                startActivity(Intent.createChooser(shareIntent1, "Share this using"));
                break;
        }
    }

    private boolean isPackageInstalled(String packagename, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packagename, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private void zoomImageFromThumb(final View thumbView, String imageResId) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        final ImageView expandedImageView = (ImageView) findViewById(
                R.id.expanded_image);
        expandedImageView.setImageURI(Uri.parse(imageResId));

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.container)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageView,
                View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y, startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.leave_transition, 0);
        showFBInterstitial();
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

    public void showNativeAd() {
        nativeAdContainer = (LinearLayout) findViewById(R.id.nativeAdContainer);
        if (!ImageUtil.CheckNet(this)) {
            nativeAdContainer.setVisibility(View.GONE);
            return;
        }

        LayoutInflater inflater = LayoutInflater.from(SaveandShareActivity.this);

        adView = (LinearLayout) inflater.inflate(R.layout.native_ad_unit_small, nativeAdContainer, false);
        nativeAdContainer.addView(adView);

        adChoicesContainer = (LinearLayout) findViewById(R.id.ad_choices_container);
        nativeAd = new NativeAd(SaveandShareActivity.this, Glob.fnative);
        // Set a listener to get notified when the ad was loaded.
        nativeAd.setAdListener(SaveandShareActivity.this);
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
            adChoicesView = new AdChoicesView(SaveandShareActivity.this, nativeAd, true);
            adChoicesContainer.addView(adChoicesView, 0);
        }

        inflateAd(nativeAd, adView, SaveandShareActivity.this);
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
        Button nativeAdCallToAction =  adView.findViewById(R.id.native_ad_call_to_action);

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
        NativeAdBase.NativeComponentTag.tagView(nativeAdIcon, NativeAdBase.NativeComponentTag.AD_ICON);
        NativeAdBase.NativeComponentTag.tagView(nativeAdTitle, NativeAdBase.NativeComponentTag.AD_TITLE);
        NativeAdBase.NativeComponentTag.tagView(nativeAdBody, NativeAdBase.NativeComponentTag.AD_BODY);
        NativeAdBase.NativeComponentTag.tagView(nativeAdSocialContext, NativeAdBase.NativeComponentTag.AD_SOCIAL_CONTEXT);
        NativeAdBase.NativeComponentTag.tagView(nativeAdCallToAction, NativeAdBase.NativeComponentTag.AD_CALL_TO_ACTION);

    }

    @Override
    public void onDestroy() {

        if (nativeAd != null) {
            nativeAd.unregisterView();
            nativeAd.destroy();
        }

        super.onDestroy();
    }
}
