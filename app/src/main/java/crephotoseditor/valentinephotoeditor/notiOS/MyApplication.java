package crephotoseditor.valentinephotoeditor.notiOS;

import android.app.Application;

import com.onesignal.OneSignal;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        OneSignal.startInit(this)
                .autoPromptLocation(false) // default call promptLocation later
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .filterOtherGCMReceivers(true)
                .init();
    }
}
