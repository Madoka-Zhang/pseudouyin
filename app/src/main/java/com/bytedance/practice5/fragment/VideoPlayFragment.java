package com.bytedance.practice5.fragment;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bytedance.practice5.Love;
import com.bytedance.practice5.R;
import com.bytedance.practice5.VideoDetailActivity;

import java.io.IOException;

import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.Inflater;

public class VideoPlayFragment extends Fragment {

    private static final String TAG = "11111";
    String mockUrl;
    int PLAY_TO;
    private View view;
    private MediaPlayer mediaPlayer;
    //    private SeekBar seekBar;
    private SurfaceView sv_main_surface;
    private SurfaceHolder surfaceHolder;

    private int mVideoWidth, mVideoHeight;
    private int showVideoHeight, showVideoWidth;
    private float scale;
//    private LinearLayout control;
    private boolean isrun;
//    private boolean isSeekbarChaning;//互斥变量，防止进度条和定时器冲突。
//
//    public SeekBar seekBar;
//    public ImageButton pause;
//    public TextView textView;
//    public TextView tv_end;
//    public ImageButton full;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_video_play, container, false);
        mockUrl = getActivity().getIntent().getStringExtra("1");
        Log.d(TAG, mockUrl);


        sv_main_surface = view.findViewById(R.id.sv_surface);
//        control = view.findViewById(R.id.m_control);
        showVideoWidth = sv_main_surface.getLayoutParams().height;
        showVideoHeight = sv_main_surface.getLayoutParams().width;
        surfaceHolder = sv_main_surface.getHolder();
//        control = view.findViewById(R.id.m_control);
//        seekBar = view.findViewById(R.id.seekbar);
//        pause = view.findViewById(R.id.btn_rp);
//        textView = view.findViewById(R.id.txt);
//        tv_end = view.findViewById(R.id.tv_en);
//        full = view.findViewById(R.id.btn_full);

        surfaceHolder.addCallback(new VideoPlayFragment.PlayerCallBack());
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(view.getContext(), Uri.parse(mockUrl));
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        isrun = true;
//        int duration2 = mediaPlayer.getDuration() / 1000;
//        int position = mediaPlayer.getCurrentPosition();
//        textView.setText("34".toCharArray(), 0, "34".toCharArray().length);
//        textView.setText(calculateTime(position / 1000).toCharArray(), 0, calculateTime(position / 1000).toCharArray().length);
//        tv_end.setText(("/"+calculateTime(duration2)).toCharArray(), 0, ("/"+calculateTime(duration2)).toCharArray().length);

        if (savedInstanceState != null) {
            // 从已保存状态恢复成员的值
            PLAY_TO = savedInstanceState.getInt("playto");
        } else {
            PLAY_TO = 0;
            // 可能初始化一个新实例的默认值的成员
        }

        sv_main_surface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isrun) {
                    isrun = false;
                    mediaPlayer.pause();
                } else {
                    isrun = true;
                    mediaPlayer.start();
                }
            }
        });

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
                int newheight = (int) (showVideoWidth * scale);
                sv_main_surface.setLayoutParams(new RelativeLayout.LayoutParams(showVideoWidth, newheight));

//                VideoView videoView = view.findViewById(R.id.vv_detail);
//                videoView.setVideoURI(Uri.parse(mockUrl));
//                videoView.setMediaController(new MediaController(getActivity()));
//                videoView.start();
            }
        });

//        pause.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (isrun) {
//                    mediaPlayer.pause();
//                } else {
//                    mediaPlayer.start();
//                }
//            }
//        });
//
//        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
////                int duration2 = mediaPlayer.getDuration() / 1000;//获取音乐总时长
////                int position = mediaPlayer.getCurrentPosition();//获取当前播放的位置
////                textView.setText(calculateTime(position / 1000));//开始时间
////                tv_end.setText("/"+calculateTime(duration2));//总时长
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//                isSeekbarChaning = true;
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                isSeekbarChaning = false;
//                mediaPlayer.seekTo(seekBar.getProgress());//在当前位置播放
//                textView.setText(calculateTime(mediaPlayer.getCurrentPosition() / 1000));
//            }
//        });

        return view;
    }

    //计算播放时间
    public String calculateTime(int time){
        int minute;
        int second;
        if(time > 60){
            minute = time / 60;
            second = time % 60;
            //分钟再0~9
            if(minute >= 0 && minute < 10){
                //判断秒
                if(second >= 0 && second < 10){
                    return "0"+minute+":"+"0"+second;
                }else {
                    return "0"+minute+":"+second;
                }
            }else {
                //分钟大于10再判断秒
                if(second >= 0 && second < 10){
                    return minute+":"+"0"+second;
                }else {
                    return minute+":"+second;
                }
            }
        }else if(time < 60){
            second = time;
            if(second >= 0 && second < 10){
                return "00:"+"0"+second;
            }else {
                return "00:"+ second;
            }
        }
        return null;
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
            mediaPlayer.stop();
        }
    }
}
