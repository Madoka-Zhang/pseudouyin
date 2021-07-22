package com.bytedance.practice5;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bytedance.practice5.fragment.mControl;

import java.io.IOException;

public class VideoDetailActivity extends AppCompatActivity {

    private static final String TAG = "11111";
    String mockUrl;

    int PLAY_TO;
    private MediaPlayer mediaPlayer;
    //    private SeekBar seekBar;
    private SurfaceView sv_main_surface;
    private SurfaceHolder surfaceHolder;

    private int mVideoWidth, mVideoHeight;
    private int showVideoHeight, showVideoWidth;
    private float scale;
    private mControl control;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("playto", PLAY_TO);
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_detail);
        mockUrl= getIntent().getStringExtra("1");
        Log.d(TAG, mockUrl);


//        final VideoView videoView = findViewById(R.id.vv_detail);
//        videoView.setVideoURI(Uri.parse(mockUrl));
//        videoView.setMediaController(new MediaController(this));

//        videoView.start();
        sv_main_surface = findViewById(R.id.sv_surface);
        control = findViewById(R.id.m_control);
        showVideoWidth = sv_main_surface.getLayoutParams().height;
        showVideoHeight = sv_main_surface.getLayoutParams().width;
        surfaceHolder = sv_main_surface.getHolder();
        surfaceHolder.addCallback(new VideoDetailActivity.PlayerCallBack());
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(this, Uri.parse(mockUrl));
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (savedInstanceState != null) {
            // 从已保存状态恢复成员的值
            PLAY_TO = savedInstanceState.getInt("playto");
        } else {
            PLAY_TO = 0;
            // 可能初始化一个新实例的默认值的成员
        }

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
                mediaPlayer.setLooping(true);
            }
        });

        mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i1) {
                mVideoWidth = mediaPlayer.getVideoWidth();
                mVideoHeight = mediaPlayer.getVideoHeight();

                scale = (float) mVideoHeight / (float) mVideoWidth;
//                int newwidth, newheight; = (int)(showVideoHeight*scale);
                int newheight = (int)(showVideoWidth*scale);
                sv_main_surface.setLayoutParams(new RelativeLayout.LayoutParams(showVideoWidth, newheight));

            }
        });


//        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                videoView.seekTo(PLAY_TO);
//            }
//        });



        Button btnrte = findViewById(R.id.btn_rotate);
        btnrte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                PLAY_TO= sv_main_surface.getCurrentPosition();

                if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                }
                if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                }

//                videoView.seekTo(PLAY_TO);

            }
        });
    }

    private class PlayerCallBack implements SurfaceHolder.Callback {

        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            mediaPlayer.setDisplay(surfaceHolder);
//            mediaPlayer.setSurface(surfaceHolder.getSurface());
//            mediaPlayer.prepareAsync();
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

        }
    }

}
