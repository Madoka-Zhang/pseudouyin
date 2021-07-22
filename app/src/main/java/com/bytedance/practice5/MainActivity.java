package com.bytedance.practice5;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bytedance.practice5.fragment.AllVideosFragment;
import com.bytedance.practice5.fragment.MyVideosFragment;
import com.bytedance.practice5.fragment.PersonalDetailsFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {
    private static final int PAGE_COUNT = 3;
    private final static int PERMISSION_REQUEST_CODE = 1001;



    public static void startMainActvity(AppCompatActivity activity){
        Intent intent = new Intent(activity , MainActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        ViewPager pager = findViewById(R.id.view_pager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                if(i==0){
                    return new AllVideosFragment();
                }else if (i==1){
                    return new MyVideosFragment();//之后改成我的视频
                }
                return new PersonalDetailsFragment();//之后改成个人信息
            }


            @Override
            public int getCount() {
                return PAGE_COUNT;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                if(position==0){
                    return "全部视频";
                }else if (position==1){
                    return "我的视频";//之后改成我的视频
                }
                return "个人信息";//之后改成个人信息

            }
        });
        Log.d("11111","test");
        tabLayout.setupWithViewPager(pager);

        ImageButton btnSearch = findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(this, SearchActivity.class));
            }
        });

        ImageButton btnUpload = findViewById(R.id.btn_upload);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, CameraActivity.class));
//                requestPermission();

                AlertDialog alertDialog2 = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("选择上传形式")
                        .setIcon(R.mipmap.ic_launcher)
                        .setPositiveButton("拍摄", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                requestPermission();
                            }
                        })

                        .setNegativeButton("上传", new DialogInterface.OnClickListener() {//添加取消
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
//                                startActivity(new Intent(MainActivity.this, UploadActivity.class));
                                UploadActivity.startUI(MainActivity.this, "");
                            }
                        })
                        .create();
                alertDialog2.show();
            }
        });

    }

    private void requestPermission() {
        boolean hasCameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean hasAudioPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
        boolean hasWritePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        boolean hasReadPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        if (hasCameraPermission && hasAudioPermission && hasWritePermission && hasReadPermission) {
            recordVideo();
        } else {
            List<String> permission = new ArrayList<String>();
            if (!hasCameraPermission) {
                permission.add(Manifest.permission.CAMERA);
            }
            if (!hasAudioPermission) {
                permission.add(Manifest.permission.RECORD_AUDIO);
            }
            if (!hasWritePermission) {
                permission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (!hasReadPermission) {
                permission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            ActivityCompat.requestPermissions(this, permission.toArray(new String[permission.size()]), PERMISSION_REQUEST_CODE);
        }
    }

    private void recordVideo() {
        CameraActivity.startUI(this);
    }

}
