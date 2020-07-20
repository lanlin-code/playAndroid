package com.example.playandroid.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class KnowledgeText implements Parcelable {
    private int mId;
    private String mChapterName;
    private String mLink;
    private String mNiceDate;
    private String mSuperChapterName;
    private String mTitle;
    private String mAuthor;

    protected KnowledgeText(Parcel in) {
        mId = in.readInt();
        mChapterName = in.readString();
        mLink = in.readString();
        mNiceDate = in.readString();
        mSuperChapterName = in.readString();
        mTitle = in.readString();
        mAuthor = in.readString();
    }

    public KnowledgeText() {

    }

    public static final Creator<KnowledgeText> CREATOR = new Creator<KnowledgeText>() {
        @Override
        public KnowledgeText createFromParcel(Parcel in) {
            return new KnowledgeText(in);
        }

        @Override
        public KnowledgeText[] newArray(int size) {
            return new KnowledgeText[size];
        }
    };

    public void setChapterName(String mChapterName) {
        this.mChapterName = mChapterName;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public void setLink(String mLink) {
        this.mLink = mLink;
    }

    public void setNiceDate(String mNiceDate) {
        this.mNiceDate = mNiceDate;
    }

    public void setSuperChapterName(String mSuperChapterName) {
        this.mSuperChapterName = mSuperChapterName;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }

    public int getId() {
        return mId;
    }

    public String getChapterName() {
        return mChapterName;
    }

    public String getLink() {
        return mLink;
    }

    public String getNiceDate() {
        return mNiceDate;
    }

    public String getSuperChapterName() {
        return mSuperChapterName;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthor() {
        return mAuthor;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mChapterName);
        dest.writeString(mLink);
        dest.writeString(mNiceDate);
        dest.writeString(mSuperChapterName);
        dest.writeString(mTitle);
        dest.writeString(mAuthor);
    }

    @NonNull
    @Override
    public String toString() {
        return "title: " + mTitle + ", id: " + mId;
    }
}
