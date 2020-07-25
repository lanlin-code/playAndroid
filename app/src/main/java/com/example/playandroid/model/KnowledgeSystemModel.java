package com.example.playandroid.model;


import com.example.playandroid.entity.Knowledge;
import com.example.playandroid.entity.KnowledgeSystem;
import com.example.playandroid.manager.KnowledgeSystemManager;
import com.example.playandroid.net.MyService;
import com.example.playandroid.net.OkHttpClient;
import com.example.playandroid.net.Request;
import com.example.playandroid.net.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class KnowledgeSystemModel {

    public static List<KnowledgeSystem> getKnowledgeSystem() {
        String url = MyService.getKnowledgeSystemLink();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        String data = response.body().toString();
        return parseData(data);
    }

    private static List<KnowledgeSystem> parseData(String data) {
        List<KnowledgeSystem> knowledgeSystemList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(data);
            String errorCode = jsonObject.getString(KnowledgeSystemManager.ERROR_CODE);
            if (MyService.isSuccess(errorCode)) {
                JSONArray array = jsonObject.getJSONArray(KnowledgeSystemManager.DATA);
                for (int i = 0; i < array.length(); i ++) {
                    JSONObject object = array.getJSONObject(i);
                    KnowledgeSystem knowledgeSystem = new KnowledgeSystem();
                    String systemName = object.getString(KnowledgeSystemManager.NAME);
                    knowledgeSystem.setName(systemName);
                    JSONArray jsonArray = object.getJSONArray(KnowledgeSystemManager.CHILDREN);
                    List<Knowledge> knowledgeList = new ArrayList<>();
                    for (int j = 0; j < jsonArray.length(); j ++) {
                        JSONObject child = jsonArray.getJSONObject(j);
                        Knowledge knowledge = new Knowledge();
                        int id = child.getInt(KnowledgeSystemManager.ID);
                        knowledge.setId(id);
                        String name = child.getString(KnowledgeSystemManager.NAME);
                        knowledge.setName(name);
                        knowledgeList.add(knowledge);
                    }
                    knowledgeSystem.setKnowledgeList(knowledgeList);
                    knowledgeSystemList.add(knowledgeSystem);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return knowledgeSystemList;
    }

}
