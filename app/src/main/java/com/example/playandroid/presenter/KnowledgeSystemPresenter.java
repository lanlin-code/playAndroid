package com.example.playandroid.presenter;

import com.example.playandroid.entity.KnowledgeSystem;
import com.example.playandroid.model.KnowledgeSystemModel;

import java.util.List;

public class KnowledgeSystemPresenter {

    public static List<KnowledgeSystem> getKnowledgeSystems() {
        return KnowledgeSystemModel.getKnowledgeSystem();
    }

}
