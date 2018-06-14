package com.tv.health;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by allen on 2018/6/14 10:52.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        // Normal app init code...
    }
}
