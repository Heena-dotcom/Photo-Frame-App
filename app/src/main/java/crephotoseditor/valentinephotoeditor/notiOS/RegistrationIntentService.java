package crephotoseditor.valentinephotoeditor.notiOS;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;


public class RegistrationIntentService extends IntentService implements TokenJSONParser.TokenListener {

    private static final String TAG = "RegistrationIntentService";

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        try {
            OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
            String pushToken = status.getSubscriptionStatus().getPushToken();
            sendRegistrationToServer(pushToken);
            Log.e("onHandleIntent", "onHandleIntent: " + pushToken);
        } catch (Exception e) {
            sharedPreferences.edit().putBoolean(GCMUtils.SENT_TOKEN_TO_SERVER, false).apply();
        }
    }

    private void sendRegistrationToServer(String token) {
        TokenJSONParser objTokenJSONParser = new TokenJSONParser();
        objTokenJSONParser.InsertOrderMaster(RegistrationIntentService.this, token);
    }

    @Override
    public void OnTokenReceived(boolean isExit) {
        stopSelf();
    }
}
