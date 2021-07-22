package com.bytedance.practice5.recycler;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bytedance.practice5.R;

import java.text.SimpleDateFormat;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.VideoViewHolder>{
    private List<VideoData> data;
    private IOnItemClickListener mItemClickListener;
    private static final String TAG = "11111";
    public void setData(List<VideoData> videoList){
        Log.d(TAG, "setData: 1");
        data = videoList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video,parent,false);
        return new VideoViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, final int position) {
        holder.bind(data.get(position));
        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemCLick(position, data.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data==null?0:data.size();
    }

    public interface IOnItemClickListener {

        void onItemCLick(int position, VideoData data);

//        void onItemLongCLick(int position, VideoData data);
    }

    public void setOnItemClickListener(IOnItemClickListener listener) {
        mItemClickListener = listener;
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder{
        private TextView userName;
        private TextView updateDate;
        private ImageView imageView;
        private View view;
        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            userName = view.findViewById(R.id.user_name);
            updateDate = view.findViewById(R.id.update_date);
            imageView = view.findViewById(R.id.image);
        }
        public void bind(VideoData videodata){
            userName.setText("上传者："+videodata.getUserName());
            SimpleDateFormat ft = new SimpleDateFormat("yyyy.MM.dd 'at' hh:mm:ss");
            updateDate.setText("上传时间："+ft.format(videodata.getUpdatedAt()));
            Glide
                    .with(imageView.getContext())
                    .load(videodata.getImageUrl())
                    .placeholder(R.mipmap.jiazai)
                    .into(imageView);

        }

        public void setOnClickListener(View.OnClickListener listener) {
            if (listener != null) {
                view.setOnClickListener(listener);
            }
        }
    }

}


