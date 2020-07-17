package com.example.playandroid.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.example.playandroid.entity.Category;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

public class CategoryAdapter extends PagerAdapter {

    private List<Category> mCategories;
    private List<View> mViews;

    public CategoryAdapter(List<Category> categories, List<View> views) {
        mCategories = categories;
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

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view = mViews.get(position);
        container.removeView(view);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = mViews.get(position);
        container.addView(view);
        return view;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mCategories.get(position).getName();
    }
}
