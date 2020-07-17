package com.example.playandroid.entity;

import androidx.annotation.NonNull;

public class Item {
    private String mAuthor;
    private String mDescription;
    private String mLink;
    private String mNiceDate;
    private String mPictureLink;
    private String mTitle;
    private int mId;

    public void setAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public void setLink(String mLink) {
        this.mLink = mLink;
    }

    public void setNiceDate(String mNiceDate) {
        this.mNiceDate = mNiceDate;
    }

    public void setPictureLink(String mPictureLink) {
        this.mPictureLink = mPictureLink;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getLink() {
        return mLink;
    }

    public String getNiceDate() {
        return mNiceDate;
    }

    public String getPictureLink() {
        return mPictureLink;
    }

    public String getTitle() {
        return mTitle;
    }

    public int getId() {
        return mId;
    }

    @NonNull
    @Override
    public String toString() {
        return "title is " + mTitle + ", id is " + mId;
    }
}
