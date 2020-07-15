package com.example.playandroid.entity;

public class Text {
    private String mAuthor; // 作者
    private String mChapterName; // 小类别
    private String mLink; // 文章的链接
    private String mNiceDate; // 时间
    private String mSuperChapterName; // 大类别
    private String mTitle; // 标题


    public void setAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }


    public void setChapterName(String mChapterName) {
        this.mChapterName = mChapterName;
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


    public String getAuthor() {
        return mAuthor;
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
}
