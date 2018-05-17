package com.tv.health;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.iwgang.countdownview.CountdownView;
import cn.iwgang.uptime.Config;
import cn.iwgang.uptime.TimeLimit;
import cn.iwgang.uptime.UpTimeView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        CountdownView mCvCountdownViewTest1 = (CountdownView) findViewById(R.id.cv_countdownViewTest1);
        mCvCountdownViewTest1.setTag("test1");
        long time1 = (long) (Config.allowHour * 60 * 60 * 1000);
        mCvCountdownViewTest1.start(time1);

        UpTimeView upTimeView = (UpTimeView) findViewById(R.id.utv);
        upTimeView.setTag("test1");
        upTimeView.start(time1);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    AlertDialog dialog;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainTimeLimit(TimeLimit timeLimit) {
        if (dialog == null) {
            dialog = new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("已超过1.5小时")
                    .setPositiveButton("确定", null)
                    .create();
        }
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }
}
