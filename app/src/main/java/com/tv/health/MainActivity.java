package com.tv.health;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.tv.health.bean.Note;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {
    TextView tv_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_time = findViewById(R.id.tv_time);
        EventBus.getDefault().register(this);
        Intent intent = new Intent(this, HorizonService.class);
        intent.putExtra("messenger", new Messenger(mHandler));
        startService(intent);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceive(Note note) {
        String time = note.msg;
        tv_time.setText(time);
    }

    private Handler mHandler = new Handler() {
        // 接收结果，刷新界面
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    String time = (String) msg.obj;
                    tv_time.setText(time);
                    break;

                default:
                    break;
            }
        }

        ;
    };
}
