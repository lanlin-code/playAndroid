package com.example.playandroid.presenter;

import com.example.playandroid.entity.Knowledge;
import com.example.playandroid.entity.KnowledgeText;
import com.example.playandroid.model.KnowledgeTextModel;

import java.util.List;

public class KnowledgePresenter {
    public static List<KnowledgeText> freshData(Knowledge knowledge) {
        return KnowledgeTextModel.load(knowledge, 0, knowledge.getId());
    }

    public static List<KnowledgeText> loadMore(Knowledge knowledge) {
        return KnowledgeTextModel.load(knowledge, knowledge.getPage(), knowledge.getId());
    }
}
