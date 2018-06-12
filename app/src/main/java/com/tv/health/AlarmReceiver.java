package com.tv.health;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by allen on 2018/5/16 16:58.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, CalulateTimeOutService.class);
        context.startService(i);
    }
}
