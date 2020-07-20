package com.example.playandroid.model;

import android.util.Log;

import com.example.playandroid.entity.Knowledge;
import com.example.playandroid.entity.KnowledgeText;
import com.example.playandroid.net.MyService;
import com.example.playandroid.net.OkHttpClient;
import com.example.playandroid.net.Request;
import com.example.playandroid.net.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class KnowledgeTextModel {

    public static synchronized List<KnowledgeText> load(Knowledge knowledge, int page, int cid) {
        String url = MyService.getKnowledgeLink(page, cid);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        List<KnowledgeText> knowledgeTexts = parseData(response.body().toString());
        List<KnowledgeText> texts = knowledge.getTexts();
        Iterator<KnowledgeText> iterator = knowledgeTexts.iterator();
        while (iterator.hasNext()) {
            KnowledgeText text = iterator.next();
            for (KnowledgeText knowledgeText : texts) {
                if (text.getId() == knowledgeText.getId()) {
                    iterator.remove();
                    break;
                }
            }
        }
        Log.d("TAG", "load: ");
        return knowledgeTexts;
    }

    private static List<KnowledgeText> parseData(String data) {
        List<KnowledgeText> knowledgeTexts = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(data);
            String errorCode = jsonObject.getString("errorCode");
            if (MyService.isSuccess(errorCode)) {
                JSONObject object = jsonObject.getJSONObject("data");
                JSONArray array = object.getJSONArray("datas");
                for (int i = 0; i < array.length(); i ++) {
                    JSONObject item = array.getJSONObject(i);
                    KnowledgeText knowledgeText = new KnowledgeText();
                    String author = item.getString("author");
                    knowledgeText.setAuthor(author);
                    String chapterName = item.getString("chapterName");
                    knowledgeText.setChapterName(chapterName);
                    int id = item.getInt("id");
                    knowledgeText.setId(id);
                    String link = item.getString("link");
                    knowledgeText.setLink(link);
                    String niceDate = item.getString("niceDate");
                    knowledgeText.setNiceDate(niceDate);
                    String superChapterName = item.getString("superChapterName");
                    knowledgeText.setSuperChapterName(superChapterName);
                    String title = item.getString("title");
                    knowledgeText.setTitle(title);
                    knowledgeTexts.add(knowledgeText);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return knowledgeTexts;
    }

}
