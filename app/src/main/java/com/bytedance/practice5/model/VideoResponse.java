package com.bytedance.practice5.model;

import com.bytedance.practice5.recycler.VideoData;
import com.bytedance.practice5.recycler.VideoData;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideoResponse {
    @SerializedName("feeds")
    public List<VideoData> feeds;
    @SerializedName("success")
    public boolean success;
}
