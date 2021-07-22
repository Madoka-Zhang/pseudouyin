package com.bytedance.practice5;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bytedance.practice5.model.UploadResponse;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UploadActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_COVER_IMAGE = 101;
    private static final String COVER_IMAGE_TYPE = "image/*";
    private static final int REQUEST_CODE_VIDEO = 102;
    private static final String COVER_VIDEO_TYPE = "video/*";
    private static final String TAG = "D";

    private mVideoView mVideoView;
    private MediaMetadataRetriever mMetadataRetriever;
    private Button mRecordButton;
    private boolean isRecording = false;
    private int mVideoWidth, mVideoHeight;
    private int showVideoHeight, showVideoWidth;
    private SimpleDraweeView coverSD;
    private float scale;
    private IApi api;

    private Uri videouri, coverImageUri;

    private static String mp4Path = "";

    public static void startUI(Context context, String Path) {
        Intent intent = new Intent(context, UploadActivity.class);
        context.startActivity(intent);
        mp4Path = Path;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initNetwork();
        setContentView(R.layout.activity_upload);

        mVideoView = findViewById(R.id.vv_detail);
        if (mp4Path != null && mp4Path == "") {
            mVideoView.setVideoPath(mp4Path);
            mVideoView.start();
        }
        showVideoHeight = mVideoView.getMeasuredHeight();
        showVideoWidth = mVideoView.getMeasuredWidth();

        coverSD = findViewById(R.id.sd_cover);
//        draweeView.setImageURI();

        Button btn_upload = findViewById(R.id.bt_upload);
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        mVideoWidth = mp.getVideoWidth();
                        mVideoHeight = mp.getVideoHeight();

                        scale = (float) mVideoWidth / (float) mVideoHeight;
                        if (scale < 1) {
                            mVideoView.setMeasure((int)(scale*showVideoHeight), showVideoHeight);
                        } else {

                        }
                    }
                });
                mp.start();
                mp.setLooping(true);
            }
        });

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

        Button btn_change = findViewById(R.id.bt_change);
        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFile(REQUEST_CODE_COVER_IMAGE, COVER_IMAGE_TYPE, "选择图片");
            }
        });

        Button btn_choose = findViewById(R.id.bt_choose);
        btn_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFile(REQUEST_CODE_VIDEO, COVER_VIDEO_TYPE, "选择视频");
            }
        });
    }

    private String getOutputMediaPath() {
//        File mediaStorageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File mediaStorageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir, "IMG_" + timeStamp + ".png");
        if (!mediaFile.exists()) {
            mediaFile.getParentFile().mkdirs();
        }
        return mediaFile.getAbsolutePath();
    }

    private void initNetwork() {
        Retrofit retrofit = new Retrofit.Builder()
               .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(IApi.class);
    }

    private void submit() {
        byte[] videoData = null;
        Uri uri = videouri;
        try {
            uri = PathUtils.getUriForFile(UploadActivity.this, mp4Path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        videoData = readDataFromUri(uri);
        byte[] coverImageData = readDataFromUri(coverImageUri);
        if (coverImageData == null || coverImageData.length == 0) {
            Toast.makeText(this, "封面不存在", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            MultipartBody.Part coverImagePart = MultipartBody.Part.createFormData("cover_image", "cover.png",
                    RequestBody.create(MediaType.parse("multipart/from-data"), coverImageData));
            MultipartBody.Part videoPart = MultipartBody.Part.createFormData("video", "AA.mp4",
                    RequestBody.create(MediaType.parse("multipart/from-data"), videoData));
            Call<UploadResponse> response = api.submitMessage(Constants.STUDENT_ID, Constants.USER_NAME, "", coverImagePart, videoPart, Constants.token);
            Log.d("Submit", response.toString());
            Toast.makeText(this, "开始提交", Toast.LENGTH_SHORT).show();
            response.enqueue(new Callback<UploadResponse>() {
                @Override
                public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {
                    Log.d("Submit", "response" + response.toString());
                    if (!response.isSuccessful()) {
                        Toast.makeText(getApplication(), "提交失败", Toast.LENGTH_SHORT).show();
                        Log.d("Submit", "提交失败");
                        return;
                    } else {
                        Toast.makeText(getApplication(), "提交成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<UploadResponse> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "wtf!!!", Toast.LENGTH_SHORT).show();
        }
    }

    private byte[] readDataFromUri(Uri uri) {
        byte[] data = null;
        try {
            InputStream is = getContentResolver().openInputStream(uri);
            data = Util.inputStream2bytes(is);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    private void getFile(int requestCode, String type, String title) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(type);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        intent.putExtra(Intent.EXTRA_TITLE, title);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE_COVER_IMAGE == requestCode) {
            if (resultCode == Activity.RESULT_OK) {
                coverImageUri = data.getData();
                coverSD.setImageURI(coverImageUri);

                if (coverImageUri != null) {
                    Log.d(TAG, "pick cover image " + coverImageUri.toString());
                } else {
                    Log.d(TAG, "uri2File fail " + data.getData());
                }

            } else {
                Log.d(TAG, "file pick fail");
            }
        }
        if (REQUEST_CODE_VIDEO == requestCode) {
            if (resultCode == Activity.RESULT_OK) {
                videouri = data.getData();
                mp4Path = videouri.getPath();
                mVideoView.setVideoURI(videouri);

                if (videouri != null) {
                    Log.d(TAG, "pick video " + videouri.toString());
                } else {
                    Log.d(TAG, "uri2File fail " + data.getData());
                }

            } else {
                Log.d(TAG, "file pick fail");
            }
        }
    }
}