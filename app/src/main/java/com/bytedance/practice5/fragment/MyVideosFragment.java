package com.bytedance.practice5.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bytedance.practice5.Constants;
import com.bytedance.practice5.R;
import com.bytedance.practice5.VideoDetailActivity;
import com.bytedance.practice5.model.VideoResponse;
import com.bytedance.practice5.recycler.MyAdapter;
import com.bytedance.practice5.recycler.VideoData;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static android.os.Looper.getMainLooper;

public class MyVideosFragment extends Fragment implements MyAdapter.IOnItemClickListener {
    private RecyclerView recyclerView;
    private MyAdapter myAdapter= new MyAdapter();
    private RecyclerView.LayoutManager layoutManager;
    private GridLayoutManager gridLayoutManager;
    private View view;
    private static final String TAG = "11111";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_all_videos, container, false);
        Fresco.initialize(getActivity());
        initview();
        return  view;

    }

    private void initview(){
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_all);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        myAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(myAdapter);

        getData(Constants.STUDENT_ID);
    }


    private void getData(final String studentId){
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<VideoData> messages = baseGetReposFromRemote(
                        studentId, Constants.token);
                Log.d(TAG, "111"+messages.toString());
                if (messages!= null && !messages.isEmpty()) {
                    new Handler(getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            myAdapter.setData(messages);
                        }
                    });

                }
            }
        }).start();
    }

    public List<VideoData> baseGetReposFromRemote(String studentId, String accept) {

        String urlStr =
                String.format((Constants.BASE_URL+"/video?%s#"),studentId);
        List<VideoData> result = null;
        VideoResponse temp = null;

        Log.d(TAG,urlStr);
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(600);
            Log.d(TAG,""+conn.getResponseCode());
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == 200) {

                InputStream in = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
                temp = new Gson().fromJson(reader, new TypeToken<VideoResponse>() {
                }.getType());
                result = temp.feeds;
                reader.close();
                in.close();

            } else {
                Log.d(TAG,"error");
            }
            conn.disconnect();

        } catch (final Exception e) {
            e.printStackTrace();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), "网络异常" + e.toString(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG,"网络异常" + e.toString());
                }
            });
        }
        return result;
    }

    public void onItemCLick(int position, VideoData data) {
        Log.d(TAG,data.getVideoUrl() );
        Intent intent = new Intent(getActivity(), VideoDetailActivity.class);
        intent.putExtra("1",data.getVideoUrl());
        startActivity(intent);

    }
}
