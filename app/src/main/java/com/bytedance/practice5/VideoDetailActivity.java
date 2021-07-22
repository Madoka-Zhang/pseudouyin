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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bytedance.practice5.fragment.AllVideosFragment;
import com.bytedance.practice5.fragment.MyVideosFragment;
import com.bytedance.practice5.fragment.VideoPlayFragment;
import com.google.android.material.tabs.TabLayout;

public class VideoDetailActivity extends AppCompatActivity {
    private static final int PAGE_COUNT = 2;
    private static final String TAG = "11111";
    String mockUrl;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);
        mockUrl= getIntent().getStringExtra("1");
        Log.d(TAG, mockUrl);
        setContentView(R.layout.activity_video_detail);
        ViewPager pager = findViewById(R.id.view_pager1);
        TabLayout tabLayout = findViewById(R.id.tab_layout1);
        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                if(i==0){
                    return new VideoPlayFragment();
                }
                    return new AllVideosFragment();
            }


            @Override
            public int getCount() {
                return PAGE_COUNT;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                if(position==0){
                    return "当前视频";
                }
                    return "推荐";
            }
        });
        Log.d("11111","test");
        tabLayout.setupWithViewPager(pager);

    }

}
