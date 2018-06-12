package com.tv.health;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.tv.health.view.ToastActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.iwgang.uptime.Config;
import cn.iwgang.uptime.TimeLimit;
import cn.iwgang.uptime.UpTimeView;

public class MainActivity extends AppCompatActivity {
    public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000; //需要自己定义标志
    TextView tv_alert;
    WindowManager windowManager;
    View rootview;
    WindowManager.LayoutParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        setContentView(R.layout.activity_main);
        initLogger();
        Logger.d("onCreate");
        initWindowParams();
        EventBus.getDefault().register(this);
        //启动后台服务
        Intent intent = new Intent(this, CalulateTimeOutService.class);
//        intent.putExtra("messenger", new Messenger(mHandler));
        startService(intent);
    }

    @Override
    protected void onResume() {
        initView();
        addWindowView2Window();
        super.onResume();
        Logger.d("onResume");
    }


    private void initLogger() {
        //        Logger.addLogAdapter(new AndroidLogAdapter());
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
//                .methodCount(0)         // (Optional) How many method line to show. Default 2
                .methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
//                .logStrategy(new LogStrategy() {
//                    @Override
//                    public void log(int priority, @Nullable String tag, @NonNull String message) {
//
//                    }
//                }) // (Optional) Changes the log strategy to print out. Default LogCat
                .tag("MyCustomTag")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build();
//        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return BuildConfig.DEBUG;
            }
        });
    }

    private void initView() {
        rootview = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        long time1 = (long) (Config.allowMinute * 60 * 1000);
        UpTimeView upTimeView = (UpTimeView) rootview.findViewById(R.id.utv);
        upTimeView.setTag("test1");
        upTimeView.start(time1);

        rootview.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                windowManager.removeView(rootview);
                moveTaskToBack(true);
            }
        });
        rootview.findViewById(R.id.btn_open).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ToastActivity.class));
            }
        });
        tv_alert = (TextView) rootview.findViewById(R.id.tv_alert);
    }

    private void addWindowView2Window() {
        windowManager.addView(rootview, params);
    }

    private void initWindowParams() {
        windowManager = (WindowManager) getApplication().getSystemService(getApplication().WINDOW_SERVICE);
        //类型是TYPE_TOAST，像一个普通的Android Toast一样。这样就不需要申请悬浮窗权限了。
        params = new WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_TOAST);
        //初始化后不首先获得窗口焦点。不妨碍设备上其他部件的点击、触摸事件。
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
//        params.height = 300;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        //params.gravity=Gravity.BOTTOM;
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
        Logger.d("onDestroy");
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

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Toast("dispatchKeyEvent(), action=" + event.getAction() + " keycode=" + event.getKeyCode());
        boolean isHandleToSys = true;
        switch (event.getKeyCode()) {
            //模拟器测试时键盘中的的Enter键，模拟ok键（推荐TV开发中使用蓝叠模拟器）
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
            return super.dispatchKeyEvent(event);
        } else {
            return true;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Logger.d("onRestart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Logger.d("onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Logger.d("onStop");
    }
}
