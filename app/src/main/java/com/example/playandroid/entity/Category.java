package com.example.playandroid.entity;

import java.util.List;

public class Category {
    private String mName;
    private int mId;
    private List<Item> mItems;
    private int mCurrentPage = 1;

    public void setId(int mId) {
        this.mId = mId;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public void setCurrentPage(int mCurrentPage) {
        this.mCurrentPage = mCurrentPage;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public void setItems(List<Item> mItems) {
        this.mItems = mItems;
    }

    public List<Item> getItems() {
        return mItems;
    }

    public int getCurrentPage() {
        return mCurrentPage;
    }
}
