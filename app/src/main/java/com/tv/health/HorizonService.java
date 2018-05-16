package com.tv.health;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Messenger;
import android.os.SystemClock;
import android.util.Log;

import com.tv.health.bean.Note;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by allen on 2018/5/16 16:57.
 */
public class HorizonService extends Service {
    Messenger mMessenger;

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
//                if (mMessenger == null) {
//                    mMessenger = (Messenger) intent.getExtras().get("messenger");
//                }
                long startTime = SystemClock.elapsedRealtime();
                long s = startTime / 1000;
                long h = s / (60 * 60);
                long m = s % (60 * 60) / 60;
                long second = s % 60;
                StringBuffer time = new StringBuffer().append(h).append(":").append(m).append(":").append(second);
//                Message message = Message.obtain();
//                message.what = 1;
//                message.obj = time.toString();
                Log.e("HorizonService", "time.toString():" + time.toString());
                EventBus.getDefault().post(new Note(time.toString()));
//        try {
//            mMessenger.send(message);
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
            }
        }.start();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int five = 1000; // 这是1s
        long triggerAtTime = SystemClock.elapsedRealtime() + five;
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }
}
