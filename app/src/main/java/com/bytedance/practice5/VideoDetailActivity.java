package com.bytedance.practice5;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class VideoDetailActivity extends AppCompatActivity {

    private static final String TAG = "11111";
    String mockUrl;

    int PLAY_TO;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("playto", PLAY_TO);
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);
        mockUrl= getIntent().getStringExtra("1");
        Log.d(TAG, mockUrl);


        final VideoView videoView = findViewById(R.id.vv_detail);
        videoView.setVideoURI(Uri.parse(mockUrl));
        videoView.setMediaController(new MediaController(this));

        videoView.start();

        if (savedInstanceState != null) {
            // 从已保存状态恢复成员的值
            PLAY_TO = savedInstanceState.getInt("playto");
        } else {
            PLAY_TO = 0;
            // 可能初始化一个新实例的默认值的成员
        }


        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.seekTo(PLAY_TO);
            }
        });



        Button btnrte = findViewById(R.id.btn_rotate);
        btnrte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PLAY_TO= videoView.getCurrentPosition();

                if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                }
                if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                }

                videoView.seekTo(PLAY_TO);

            }
        });
    }

}
