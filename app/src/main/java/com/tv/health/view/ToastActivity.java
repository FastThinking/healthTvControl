package com.tv.health.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class ToastActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        final TextView textView = new TextView(this);
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundColor(Color.BLACK);
        textView.setText("点击移除悬浮窗");
        textView.setTextSize(10);
        textView.setTextColor(Color.RED);

        //类型是TYPE_TOAST，像一个普通的Android Toast一样。这样就不需要申请悬浮窗权限了。
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_TOAST);

        //初始化后不首先获得窗口焦点。不妨碍设备上其他部件的点击、触摸事件。
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = 300;
//        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        //params.gravity=Gravity.BOTTOM;
        final WindowManager windowManager = (WindowManager) getApplication().getSystemService(getApplication().WINDOW_SERVICE);
        windowManager.addView(textView, params);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplication(), "不需要权限的悬浮窗实现", Toast.LENGTH_LONG).show();
                windowManager.removeView(textView);
            }
        });


    }
}