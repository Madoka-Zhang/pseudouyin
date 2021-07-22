package com.bytedance.practice5.fragment;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bytedance.practice5.R;

import java.util.zip.Inflater;

public class VideoPlayFragment extends Fragment {

    private static final String TAG = "11111";
    String mockUrl;
    private View view;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_video_play, container, false);
        mockUrl= getActivity().getIntent().getStringExtra("1");
        Log.d(TAG, mockUrl);
        VideoView videoView = view.findViewById(R.id.vv_detail);
        videoView.setVideoURI(Uri.parse(mockUrl));
        videoView.setMediaController(new MediaController(getActivity()));
        videoView.start();
        return  view;


    };
}
