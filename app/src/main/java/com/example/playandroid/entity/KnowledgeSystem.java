package com.example.playandroid.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class KnowledgeSystem implements Parcelable {
    private String mName;
    private List<Knowledge> mKnowledgeList;

    public KnowledgeSystem() {

    }


    protected KnowledgeSystem(Parcel in) {
        mName = in.readString();
        mKnowledgeList = in.createTypedArrayList(Knowledge.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeTypedList(mKnowledgeList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<KnowledgeSystem> CREATOR = new Creator<KnowledgeSystem>() {
        @Override
        public KnowledgeSystem createFromParcel(Parcel in) {
            return new KnowledgeSystem(in);
        }

        @Override
        public KnowledgeSystem[] newArray(int size) {
            return new KnowledgeSystem[size];
        }
    };

    public void setKnowledgeList(List<Knowledge> mKnowledgeList) {
        this.mKnowledgeList = mKnowledgeList;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public List<Knowledge> getKnowledgeList() {
        return mKnowledgeList;
    }

    public String getName() {
        return mName;
    }


}
