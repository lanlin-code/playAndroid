package com.example.playandroid.model;

import android.util.Log;

import com.example.playandroid.entity.Knowledge;
import com.example.playandroid.entity.KnowledgeSystem;
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
        List<KnowledgeSystem> knowledgeSystemList = parseData(data);
        if (knowledgeSystemList.isEmpty()) Log.d("TAG", "getKnowledgeSystem: -------list is empty---------");
        else Log.d("TAG", "getKnowledgeSystem: ---------list is not empty");
        return knowledgeSystemList;
    }

    private static List<KnowledgeSystem> parseData(String data) {
        List<KnowledgeSystem> knowledgeSystemList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(data);
            String errorCode = jsonObject.getString("errorCode");
            if (MyService.isSuccess(errorCode)) {
                JSONArray array = jsonObject.getJSONArray("data");
                for (int i = 0; i < array.length(); i ++) {
                    JSONObject object = array.getJSONObject(i);
                    KnowledgeSystem knowledgeSystem = new KnowledgeSystem();
                    String systemName = object.getString("name");
                    knowledgeSystem.setName(systemName);
                    JSONArray jsonArray = object.getJSONArray("children");
                    List<Knowledge> knowledgeList = new ArrayList<>();
                    for (int j = 0; j < jsonArray.length(); j ++) {
                        JSONObject child = jsonArray.getJSONObject(j);
                        Knowledge knowledge = new Knowledge();
                        int id = child.getInt("id");
                        knowledge.setId(id);
                        String name = child.getString("name");
                        knowledge.setName(name);
//                        Log.d("TAG", "parseData: " + knowledge.toString());
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
