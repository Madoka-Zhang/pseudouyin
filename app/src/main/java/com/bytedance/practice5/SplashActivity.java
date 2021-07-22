package com.bytedance.practice5;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener{

    private Handler handler = new Handler();
    private TextView tvJump;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        tvJump = findViewById(R.id.tvJump);
        tvJump.setOnClickListener(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.startMainActvity(SplashActivity.this);
                    }
                }, 3000);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tvJump:
                handler.removeCallbacksAndMessages(null);
                MainActivity.startMainActvity(SplashActivity.this);
                break;
            default:
                break;

        }
    }
}
