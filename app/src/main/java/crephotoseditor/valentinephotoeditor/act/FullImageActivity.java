package crephotoseditor.valentinephotoeditor.act;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.ads.NativeAd;

import java.io.File;

import crephotoseditor.valentinephotoeditor.BuildConfig;
import crephotoseditor.valentinephotoeditor.R;


public class FullImageActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout insta_ll, fb_ll, whatsapp_ll, more_ll;
    ImageView img_share, back;
    private int pos;
    private NativeAd nativeAd;
    private LinearLayout adView;
    private LinearLayout nativeAdContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);
        bindView();

    }

    private void bindView() {
        insta_ll = (LinearLayout) findViewById(R.id.insta_ll);
        fb_ll = (LinearLayout) findViewById(R.id.fb_ll);
        whatsapp_ll = (LinearLayout) findViewById(R.id.whatsapp_ll);
        more_ll = (LinearLayout) findViewById(R.id.more_ll);
        img_share = (ImageView) findViewById(R.id.img_share);
        back = (ImageView) findViewById(R.id.back);

        pos = getIntent().getIntExtra("position", 0);

        img_share.setImageURI(Uri.parse(MyWorkActivity.file_path.get(pos)));

        insta_ll.setOnClickListener(this);
        fb_ll.setOnClickListener(this);
        whatsapp_ll.setOnClickListener(this);
        more_ll.setOnClickListener(this);
        img_share.setOnClickListener(this);
        back.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        final Intent shareIntent1 = new Intent(Intent.ACTION_SEND);
        Uri photoURI = FileProvider.getUriForFile(FullImageActivity.this,
                BuildConfig.APPLICATION_ID + ".provider",
                new File(MyWorkActivity.file_path.get(pos)));
        shareIntent1.setType("image/*");
        shareIntent1.putExtra(Intent.EXTRA_TEXT, "Try out this unique " + getResources().getString(R.string.app_name) + " https://play.google.com/store/apps/details?id=" + getPackageName());
        shareIntent1.putExtra(Intent.EXTRA_STREAM, photoURI);
        switch (v.getId()) {

            case R.id.back:
                finish();
                overridePendingTransition(R.anim.leave_transition, 0);
                break;

            case R.id.home_share:
                finish();
                overridePendingTransition(R.anim.leave_transition, 0);
                break;

            case R.id.insta_ll:
                try {
                    if (isPackageInstalled("com.instagram.android", getPackageManager())) {
                        shareIntent1.setPackage("com.instagram.android");
                        startActivity(shareIntent1);
                    } else {
                        Toast.makeText(FullImageActivity.this, "Instagram doesn't installed", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(FullImageActivity.this, "Instagram doesn't installed", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.fb_ll:
                try {
                    if (isPackageInstalled("com.facebook.katana", getPackageManager())) {
                        shareIntent1.setPackage("com.facebook.katana");
                        startActivity(shareIntent1);
                    } else {
                        Toast.makeText(FullImageActivity.this, "Facebook doesn't installed", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(FullImageActivity.this, "Facebook doesn't installed", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.whatsapp_ll:
                try {
                    if (isPackageInstalled("com.whatsapp", getPackageManager())) {
                        shareIntent1.setPackage("com.whatsapp");
                        startActivity(shareIntent1);
                    } else {
                        Toast.makeText(FullImageActivity.this, "WhatsApp doesn't installed", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(FullImageActivity.this, "WhatsApp doesn't installed", Toast.LENGTH_LONG).show();
                }
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.leave_transition, 0);
    }
}
