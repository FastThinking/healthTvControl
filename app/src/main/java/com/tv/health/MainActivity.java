package com.tv.health;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.iwgang.uptime.Config;
import cn.iwgang.uptime.TimeLimit;
import cn.iwgang.uptime.UpTimeView;

public class MainActivity extends AppCompatActivity {
    TextView tv_alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        long time1 = (long) (Config.allowMinute * 60 * 1000);
        UpTimeView upTimeView = (UpTimeView) findViewById(R.id.utv);
        upTimeView.setTag("test1");
        upTimeView.start(time1);

        findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveTaskToBack(true);
            }
        });

        tv_alert = (TextView) findViewById(R.id.tv_alert);
        //启动后台服务
        Intent intent = new Intent(this, HorizonService.class);
//        intent.putExtra("messenger", new Messenger(mHandler));
        startService(intent);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    AlertDialog dialog;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainTimeLimit(TimeLimit timeLimit) {
        tv_alert.setText("开机已超过" + Config.allowMinute + "分钟，请注意休息");
    }

    private void alert() {
        if (dialog == null) {
            dialog = new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("已超过" + Config.allowMinute + "分钟")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            moveTaskToBack(true);
                        }
                    })
                    .create();
        }
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }
}
