package com.tv.health;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by allen on 2018/5/17 15:22.
 */
public class ContentReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        Intent it = new Intent(context, MainActivity.class);
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(it);
        Toast.makeText(context, "我自启动成功了哈", Toast.LENGTH_LONG).show();
    }
}
