package crephotoseditor.valentinephotoeditor.Glob;


import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import crephotoseditor.valentinephotoeditor.act.HomeActivity;


public class ConnectivityChangeReceiver extends WakefulBroadcastReceiver {

    Context context;

    public ConnectivityChangeReceiver() {

    }

    public ConnectivityChangeReceiver(Context context) {
        this.context = context;
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (ImageUtil.CheckNet(context)) {
            if (this.context instanceof HomeActivity) {
                ((HomeActivity) this.context).setNetworkdetail();
            }

        }


    }
}
