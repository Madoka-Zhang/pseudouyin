package com.bytedance.practice5.fragment;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.view.LayoutInflater;

import com.bytedance.practice5.R;

public class mControl extends View {
    public SeekBar seekBar;
    public ImageButton pause;
    public TextView textView;
    public ImageButton full;
    private View view;


    public mControl(Context context) {
        super(context);
    }

    public mControl(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        view = LayoutInflater.from(context).inflate(R.layout.mcontrol, null);
        seekBar = view.findViewById(R.id.seekbar);
        pause = view.findViewById(R.id.btn_rp);
        full = view.findViewById(R.id.btn_full);
        textView = view.findViewById(R.id.txt);
        view.setVisibility(View.GONE);
        view.setAlpha((float) 0.5);
    }

    public void showControl() {
        view.setVisibility(View.VISIBLE);
    }

    public void hideControl() {
        view.setVisibility(View.GONE);
    }
}
