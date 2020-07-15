package com.example.playandroid.presenter;


import com.example.playandroid.entity.Text;
import com.example.playandroid.model.TextModel;

import java.util.List;

public class TextPresenter {

    public static List<Text> getTexts(int page) {
        return TextModel.getTexts(page);
    }


}
