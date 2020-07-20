package com.example.playandroid.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.example.playandroid.entity.Knowledge;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

public class KnowledgeAdapter extends PagerAdapter {

    private List<Knowledge> mKnowledgeList;
    private List<View> mViews;

    public KnowledgeAdapter(List<Knowledge> knowledgeList, List<View> views) {
        mKnowledgeList = knowledgeList;
        mViews = views;
    }

    @Override
    public int getCount() {
        return mViews.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = mViews.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(mViews.get(position));
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mKnowledgeList.get(position).getName();
    }
}
