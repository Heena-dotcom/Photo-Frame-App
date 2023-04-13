package crephotoseditor.valentinephotoeditor.act;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.u.securekeys.SecureEnvironment;
import com.u.securekeys.annotation.SecureKey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import crephotoseditor.valentinephotoeditor.Glob.Glob;
import crephotoseditor.valentinephotoeditor.Glob.ReqAPIforResponse;
import crephotoseditor.valentinephotoeditor.R;
import crephotoseditor.valentinephotoeditor.notiOS.GCMUtils;


public class SplashActivity extends AppCompatActivity {

    @Override
//    @SecureKey(key = "client-secretss", value = "http://securetechnoweb.com/rr/service/app_link/crephotos_splash/" + GCMUtils.APP_ID)
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        receiveResponse();

        getWindow().getDecorView().getRootView().postDelayed(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(i);
                finish();
            }
        }, 7000);

        ImageView imageView2 = (ImageView) findViewById(R.id.ivSplash);
//        WaveDrawable chromeWave = new WaveDrawable(this, R.mipmap.appicon);
//        imageView2.setImageDrawable(chromeWave);
//        chromeWave.setIndeterminate(true);
//        chromeWave.setWaveSpeed(5);
    }

    private void receiveResponse() {

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
                            } else {
                                Log.d("splash else", "onSuccess: in else");

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
}
