package com.bytedance.practice5;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

public class mVideoView extends VideoView {
    private int width;
    private int height;

    public mVideoView(Context context) {
        super(context);
    }

    public mVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setMeasure(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getDefaultSize(getWidth(), widthMeasureSpec);
        int height = getDefaultSize(getHeight(), heightMeasureSpec);
        if (this.width > 0 && this.height > 0) {
            width = this.width;
            height = this.height;
        }
        setMeasuredDimension(width, height);
    }
}