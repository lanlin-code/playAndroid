package com.example.playandroid.presenter;

import com.example.playandroid.model.HotWordModel;

import java.util.List;

public class HotWordPresenter {

    public static List<String> getHotWord() {
        return HotWordModel.getHotWord();
    }

}
