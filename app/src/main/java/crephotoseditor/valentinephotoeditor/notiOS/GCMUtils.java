package crephotoseditor.valentinephotoeditor.notiOS;

import android.content.Context;
import android.content.SharedPreferences;

import crephotoseditor.valentinephotoeditor.R;

/**
 * Created by 26 on 13-Oct-17.
 */

public class GCMUtils {

    public static final String APP_ID="386";

    public static final String TOKEN = "token";
    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static boolean getPrefBoolean(Context context, String key) {
        if (context != null) {
            String PREF_NAME = context.getString(R.string.app_name);
            return context.getSharedPreferences(PREF_NAME, 0).getBoolean(key, false);
        } else {
            return false;
        }
    }

    public static void setPrefBoolean(Context context, String key, boolean value) {
        String PREF_NAME = context.getString(R.string.app_name);
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, 0).edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
}
