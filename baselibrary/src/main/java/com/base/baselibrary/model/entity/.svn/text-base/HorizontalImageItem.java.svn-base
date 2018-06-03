package com.base.baselibrary.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by 15600 on 2017/9/26.
 */

public class HorizontalImageItem implements Serializable, Parcelable {
    private String url;
    private String path;
    private String imgName;
    private String localImgName;

    public HorizontalImageItem() {
    }

    public HorizontalImageItem(String url, String path, String imgName, String localImgName) {
        this.url = url;
        this.path = path;
        this.imgName = imgName;
        this.localImgName = localImgName;
    }

    protected HorizontalImageItem(Parcel in) {
        url = in.readString();
        path = in.readString();
        imgName = in.readString();
        localImgName = in.readString();
    }

    public static final Creator<HorizontalImageItem> CREATOR = new Creator<HorizontalImageItem>() {
        @Override
        public HorizontalImageItem createFromParcel(Parcel in) {
            return new HorizontalImageItem(in);
        }

        @Override
        public HorizontalImageItem[] newArray(int size) {
            return new HorizontalImageItem[size];
        }
    };

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public String getLocalImgName() {
        return localImgName;
    }

    public void setLocalImgName(String localImgName) {
        this.localImgName = localImgName;
    }

    @Override
    public String toString() {
        return "HorizontalImageItem{" +
                "url='" + url + '\'' +
                ", path='" + path + '\'' +
                ", imgName='" + imgName + '\'' +
                ", localImgName='" + localImgName + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(url);
        parcel.writeString(path);
        parcel.writeString(imgName);
        parcel.writeString(localImgName);
    }
}
