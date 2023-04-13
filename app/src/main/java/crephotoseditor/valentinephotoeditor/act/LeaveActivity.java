package crephotoseditor.valentinephotoeditor.act;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.u.securekeys.SecureEnvironment;
import com.u.securekeys.annotation.SecureKey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import crephotoseditor.valentinephotoeditor.Glob.ReqAPIforResponse;
import crephotoseditor.valentinephotoeditor.Glob.Glob;
import crephotoseditor.valentinephotoeditor.R;
import crephotoseditor.valentinephotoeditor.adapter.ExitAppListAdapter;
import crephotoseditor.valentinephotoeditor.notiOS.GCMUtils;

public class LeaveActivity extends AppCompatActivity {

    private GridView gvAppAdList;
    private TextView txtExit;
    private TextView txtCancel;

    @Override
    @SecureKey(key = "client-secret_x", value = "http://securetechnoweb.com/rr/service/app_link/crephotos_exit/"+ GCMUtils.APP_ID)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave);
        gvAppAdList = (GridView) findViewById(R.id.gvAppAdList);
        if (Glob.listAppNameExit.size() > 0)
            showAppsAd();
        else
            receiveResponse();

        bindConfirmExit();
    }

    private void bindConfirmExit() {
        txtExit = (TextView) findViewById(R.id.txtExit);
        txtCancel = (TextView) findViewById(R.id.txtCancel);

        txtExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
            }
        });
        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
    }

    private void receiveResponse() {
        Glob.listAppIconExit.clear();
        Glob.listAppLinkExit.clear();
        Glob.listAppNameExit.clear();


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

                            for (int i = 0; i < jArray.length(); i++) {
                                JSONObject objJson = jArray.getJSONObject(i);
                                String app_name = objJson.getString("application_name");
                                String app_links = objJson.getString("application_link");
                                String app_icon = objJson.getString("icon_link");

                                Glob.listAppIconExit.add(app_icon);
                                Glob.listAppNameExit.add(app_name);
                                Glob.listAppLinkExit.add(app_links);
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
                }, SecureEnvironment.getString("client-secret_x"));

            }
        }).start();
    }

    private void showAppsAd() {
        final ExitAppListAdapter adapterApp = new ExitAppListAdapter(this, Glob.listAppLinkExit, Glob.listAppIconExit, Glob.listAppNameExit);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                gvAppAdList.setAdapter(adapterApp);
            }
        });
        gvAppAdList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Uri uri = Uri.parse(Glob.listAppLinkExit.get(position));
                Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(myAppLinkToMarket);
                } catch (ActivityNotFoundException e) {

                    Toast.makeText(LeaveActivity.this, "You don't have Google Play installed", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
    }
}
