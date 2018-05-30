package com.tv.health;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.iwgang.uptime.Config;
import cn.iwgang.uptime.TimeLimit;
import cn.iwgang.uptime.UpTimeView;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.tv_alert)
    TextView tv_alert;

    @BindView(R.id.iv_disable)
    ImageView iv_disable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
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
        iv_disable.setVisibility(View.GONE);
        tv_alert.setVisibility(View.GONE);
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
        iv_disable.setVisibility(View.VISIBLE);
        tv_alert.setVisibility(View.VISIBLE);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.w("keyCode", "keyCode is " + keyCode);
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        boolean isHandleToSys = true;
        switch (keyCode) {
            //模拟器测试时键盘中的的Enter键，模拟ok键（推荐TV开发中使用蓝叠模拟器）
            case KeyEvent.KEYCODE_ENTER:
                break;
            case KeyEvent.KEYCODE_DPAD_CENTER:
                Toast("你按下中间键");
                break;

            case KeyEvent.KEYCODE_DPAD_DOWN:
                Toast("你按下下方向键");
                break;

            case KeyEvent.KEYCODE_DPAD_LEFT:
                Toast("你按下左方向键");
                break;

            case KeyEvent.KEYCODE_DPAD_RIGHT:
                Toast("你按下右方向键");
                break;

            case KeyEvent.KEYCODE_DPAD_UP:
                Toast("你按下上方向键");
                break;
            case KeyEvent.KEYCODE_HOME:
                Toast("你按下HOME键");
                isHandleToSys = false;
                break;
            case KeyEvent.KEYCODE_BACK:
                Toast("你按下返回键");
                isHandleToSys = false;
                break;
            case KeyEvent.KEYCODE_MENU:
                Toast("你按下菜单键");
                isHandleToSys = false;
                break;
        }
        if (isHandleToSys) {
            return super.onKeyDown(keyCode, event);
        } else {
            return true;
        }
    }

    private void Toast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

}
