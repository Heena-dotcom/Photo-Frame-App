package crephotoseditor.valentinephotoeditor.act;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import crephotoseditor.valentinephotoeditor.Glob.Glob;
import crephotoseditor.valentinephotoeditor.R;


public class ShowPolicyActivity extends Activity {

    WebView webPrivacyPolicy;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_policy);
        webPrivacyPolicy = (WebView) findViewById(R.id.wvPrivacyPolicy);
        back = (ImageView) findViewById(R.id.backWeb);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.leave_transition, 0);

            }
        });
        WebSettings webSettings = webPrivacyPolicy.getSettings();
        webSettings.setJavaScriptEnabled(true);// enable javascript
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webPrivacyPolicy.setInitialScale(1);
        webPrivacyPolicy.getSettings().setLoadWithOverviewMode(true);
        webPrivacyPolicy.getSettings().setUseWideViewPort(true);
        webPrivacyPolicy.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webPrivacyPolicy.setScrollbarFadingEnabled(true);
        webPrivacyPolicy.getSettings().setBuiltInZoomControls(true);
        webPrivacyPolicy.getSettings().setDisplayZoomControls(false);

        webPrivacyPolicy.setWebViewClient(new WebViewClient() {


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http:") || url.startsWith("https:")) {
                    return false;
                }
                // Otherwise allow the OS to handle things like tel, mailto, etc.
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(ShowPolicyActivity.this, description, Toast.LENGTH_SHORT).show();
            }
        });

        webPrivacyPolicy.loadUrl(Glob.privacy_policy);

    }


    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.leave_transition, 0);
    }
}
