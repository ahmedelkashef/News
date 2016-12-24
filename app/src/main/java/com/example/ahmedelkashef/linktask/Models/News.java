package com.example.ahmedelkashef.linktask.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ahmedelkashef on 12/20/2016.
 */

public class News implements Parcelable{
    private String NewsTitle;
    private String Nid;
    private String PostDate;
    private String ImageUrl;
    private String NewsType;
    private String NumofViews;
    private String Likes;
    private String ItemDescription;
    private String ShareURL;

    public News() {
    }

    private String VideoURL;

    protected News(Parcel in) {
        NewsTitle = in.readString();
        Nid = in.readString();
        PostDate = in.readString();
        ImageUrl = in.readString();
        NewsType = in.readString();
        NumofViews = in.readString();
        Likes = in.readString();
        ItemDescription = in.readString();
        ShareURL = in.readString();
        VideoURL = in.readString();
    }

    public static final Creator<News> CREATOR = new Creator<News>() {
        @Override
        public News createFromParcel(Parcel in) {
            return new News(in);
        }

        @Override
        public News[] newArray(int size) {
            return new News[size];
        }
    };

    public String getVideoURL() {
        return VideoURL;
    }

    public void setVideoURL(String videoURL) {
        VideoURL = videoURL;
    }

    public String getShareURL() {
        return ShareURL;
    }

    public void setShareURL(String shareURL) {
        ShareURL = shareURL;
    }

    public String getNewsTitle() {
        return NewsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        NewsTitle = newsTitle;
    }

    public String getItemDescription() {
        return ItemDescription;
    }

    public void setItemDescription(String itemDescription) {
        ItemDescription = itemDescription;
    }

    public String getNumofViews() {
        return NumofViews;
    }

    public void setNumofViews(String numofViews) {
        NumofViews = numofViews;
    }

    public String getLikes() {
        return Likes;
    }

    public void setLikes(String likes) {
        Likes = likes;
    }

    public String getNewsType() {
        return NewsType;
    }

    public void setNewsType(String newsType) {
        NewsType = newsType;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getNid() {
        return Nid;
    }

    public void setNid(String nid) {
        Nid = nid;
    }

    public String getPostDate() {
        return PostDate;
    }

    public void setPostDate(String postDate) {
        PostDate = postDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(NewsTitle);
        parcel.writeString(Nid);
        parcel.writeString(PostDate);
        parcel.writeString(ImageUrl);
        parcel.writeString(NewsType);
        parcel.writeString(NumofViews);
        parcel.writeString(Likes);
        parcel.writeString(ItemDescription);
        parcel.writeString(ShareURL);
        parcel.writeString(VideoURL);
    }
}
