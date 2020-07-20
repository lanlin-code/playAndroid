package com.example.playandroid.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class FlowLayout extends ViewGroup {

    private List<List<View>> mAllViews = new ArrayList<>();
    private List<Integer> mLineHeights = new ArrayList<>();

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 获取父布局的尺寸和模式
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int widthModel = MeasureSpec.getMode(widthMeasureSpec);
        int heightModel = MeasureSpec.getMode(heightMeasureSpec);

        int finalWidth;
        int finalHeight;

        // 最大测量宽度
        int width = 0;
        // 测量高度
        int height = 0;
        // 每一行的宽度
        int lineWidth = 0;
        int lineHeight = 0;

        int childCount = getChildCount();
        // 测量子布局占有父布局的最大宽度和高度
        for (int i = 0; i < childCount; i ++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
            int childHeight = child.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
            if (childWidth + lineWidth <= getWidth() + getPaddingStart() + getPaddingEnd()) {
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, childHeight);
            } else {
                width = Math.max(lineWidth, child.getWidth());
                lineWidth = child.getWidth();
                height += lineHeight;
                lineHeight = childHeight;
            }
        }
        if (widthModel == MeasureSpec.EXACTLY) finalWidth = sizeWidth;
        else finalWidth = width + getPaddingStart() + getPaddingEnd();
        if (heightModel == MeasureSpec.EXACTLY) finalHeight = sizeHeight;
        else finalHeight = height + getPaddingTop() + getPaddingBottom();

        setMeasuredDimension(finalWidth, finalHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mAllViews.clear();
        mLineHeights.clear();

        int childCount = getChildCount();
        int lineHeight = 0;
        int lineWidth = 0;
        List<View> lineViews = new ArrayList<>();

        // 获取每行的子布局和高度
        for (int i = 0; i < childCount; i ++) {
            View child = getChildAt(i);
            MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
            int childHeight = child.getMeasuredHeight() + layoutParams.topMargin + layoutParams.rightMargin;

            if (lineWidth + childWidth <= getWidth() - getPaddingStart() - getPaddingEnd()) {
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, childHeight);

                lineViews.add(child);
            } else {
                lineWidth = childWidth;
                mLineHeights.add(lineHeight);
                mAllViews.add(lineViews);
                lineViews = new ArrayList<>();
                lineViews.add(child);
            }


        }
        mAllViews.add(lineViews);
        mLineHeights.add(lineHeight);


        int line = mAllViews.size();
        int top = getPaddingTop();
        int left = getPaddingLeft();
        // 放置子布局
        for (int i = 0; i < line; i ++) {
            List<View> lineView = mAllViews.get(i);
            int lineSize = lineView.size();

            for (int j = 0; j < lineSize; j ++) {

                View child = lineView.get(j);
                MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();
                int lc = left + layoutParams.leftMargin;
                int tc = top + getPaddingTop();
                int rc = lc + child.getMeasuredWidth();
                int bc = tc + child.getMeasuredHeight();

                child.layout(lc, tc, rc, bc);

                left += child.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
            }
            top += mLineHeights.get(i);
            left = getPaddingLeft();
        }


    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }


}
