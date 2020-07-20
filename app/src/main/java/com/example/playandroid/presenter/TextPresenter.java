package com.example.playandroid.presenter;


import android.content.Context;

import com.example.playandroid.entity.Text;
import com.example.playandroid.model.TextModel;

import java.util.List;

public class TextPresenter {

    public static List<Text> getTexts(int page) {
        return TextModel.getTexts(page);
    }

    public static List<Text> getSearchText(int page, String keyword) {
        return TextModel.getSearchText(page, keyword);
    }

}
