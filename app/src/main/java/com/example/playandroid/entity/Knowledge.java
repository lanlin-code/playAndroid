package com.example.playandroid.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class Knowledge implements Parcelable {
    private int mId;
    private String mName;
    private List<KnowledgeText> texts = new ArrayList<>();
    private int page = 0;

    public Knowledge() {

    }

    public List<KnowledgeText> getTexts() {
        return texts;
    }

    protected Knowledge(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
        texts = in.createTypedArrayList(KnowledgeText.CREATOR);
        page = in.readInt();
    }

    public static final Creator<Knowledge> CREATOR = new Creator<Knowledge>() {
        @Override
        public Knowledge createFromParcel(Parcel in) {
            return new Knowledge(in);
        }

        @Override
        public Knowledge[] newArray(int size) {
            return new Knowledge[size];
        }
    };

    public void setPage(int page) {
        this.page = page;
    }


    public void setId(int mId) {
        this.mId = mId;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public int getPage() {
        return page;
    }

    @NonNull
    @Override
    public String toString() {
        return "name is " + mName + "\n";
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mName);
        dest.writeTypedList(texts);
        dest.writeInt(page);
    }


}
