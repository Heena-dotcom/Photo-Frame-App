package crephotoseditor.valentinephotoeditor.notiOS;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.u.securekeys.SecureEnvironment;
import com.u.securekeys.annotation.SecureKey;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class TokenJSONParser {

    TokenListener objTokenListener;
    private String requestBody;

    @SecureKey(key = "api_main", value = "http://securetechnoweb.com/rr/service/store/crephotos")

    public void InsertOrderMaster(final Context context, final String token) {

        try {
            RequestQueue queue = Volley.newRequestQueue(context);
            String url = SecureEnvironment.getString("api_main");

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {

                        Log.e("Token", "  " + response);
                        if (response != null) {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject != null) {
                                if (jsonObject.getBoolean("status")) {
                                    GCMUtils.setPrefBoolean(context, GCMUtils.TOKEN, true);
                                    objTokenListener = (TokenListener) context;
                                    objTokenListener.OnTokenReceived(true);
                                }
                            } else {
                                objTokenListener = (TokenListener) context;
                                objTokenListener.OnTokenReceived(false);
                            }
                        } else {
                            objTokenListener = (TokenListener) context;
                            objTokenListener.OnTokenReceived(false);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        objTokenListener = (TokenListener) context;
                        objTokenListener.OnTokenReceived(false);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    objTokenListener = (TokenListener) context;
                    objTokenListener.OnTokenReceived(false);
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("app_id", GCMUtils.APP_ID);
                    params.put("device_token", token);
                    return params;
                }
            };

            queue.add(stringRequest);

        } catch (Exception e) {
            e.printStackTrace();
            objTokenListener = (TokenListener) context;
            objTokenListener.OnTokenReceived(false);
        }
    }


    public interface TokenListener {
        void OnTokenReceived(boolean isExit);
    }
}
