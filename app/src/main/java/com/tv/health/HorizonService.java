package com.tv.health;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Messenger;
import android.os.SystemClock;

import cn.iwgang.uptime.Config;

/**
 * Created by allen on 2018/5/16 16:57.
 */
public class HorizonService extends Service {
    Messenger mMessenger;
    private long lastAlertTime;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                boolean expireLimited = false;
                while (!expireLimited) {
                    long startTime = SystemClock.elapsedRealtime();
                    long m = startTime / (1000 * 60);
                    if (m > Config.allowMinute) {
                        long alertInternal = SystemClock.elapsedRealtime() - lastAlertTime;
                        int minute = (int) (alertInternal / (1000 * 60));
                        if (minute >= Config.alertInternalMinute) {
                            Intent it = new Intent(getApplicationContext(), MainActivity.class);
                            it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(it);
                            lastAlertTime = SystemClock.elapsedRealtime();
                        }
                    }
                }
            }
        }.start();
        return super.onStartCommand(intent, flags, startId);
    }
}
