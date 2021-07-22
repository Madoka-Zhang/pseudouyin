package com.bytedance.practice5;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener{

    private Handler handler = new Handler();
    private TextView tvJump;
    private Runnable runnableToLogin = new Runnable() {
        @Override
        public void run() {
            handler.removeCallbacks(runnableToLogin);
            MainActivity.startMainActvity(SplashActivity.this);
            finish();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        tvJump = findViewById(R.id.tvJump);
        tvJump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacks(runnableToLogin);
                MainActivity.startMainActvity(SplashActivity.this);
                finish();
            }
        });
        handler.postDelayed(runnableToLogin, 4000);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //防止内存泄漏
        handler.removeCallbacks(runnableToLogin);
    }

    @Override
    public void onClick(View view) {

    }
}
