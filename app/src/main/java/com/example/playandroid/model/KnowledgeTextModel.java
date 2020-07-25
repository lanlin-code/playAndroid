package com.example.playandroid.model;

import com.example.playandroid.entity.Knowledge;
import com.example.playandroid.entity.KnowledgeText;
import com.example.playandroid.manager.KnowledgeTextManager;
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
        return knowledgeTexts;
    }

    private static List<KnowledgeText> parseData(String data) {
        List<KnowledgeText> knowledgeTexts = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(data);
            String errorCode = jsonObject.getString(KnowledgeTextManager.ERROR_CODE);
            if (MyService.isSuccess(errorCode)) {
                JSONObject object = jsonObject.getJSONObject(KnowledgeTextManager.DATA);
                JSONArray array = object.getJSONArray(KnowledgeTextManager.DATAS);
                for (int i = 0; i < array.length(); i ++) {
                    JSONObject item = array.getJSONObject(i);
                    KnowledgeText knowledgeText = new KnowledgeText();
                    String author = item.getString(KnowledgeTextManager.AUTHOR);
                    knowledgeText.setAuthor(author);
                    String chapterName = item.getString(KnowledgeTextManager.CHAPTER_NAME);
                    knowledgeText.setChapterName(chapterName);
                    int id = item.getInt(KnowledgeTextManager.ID);
                    knowledgeText.setId(id);
                    String link = item.getString(KnowledgeTextManager.LINK);
                    knowledgeText.setLink(link);
                    String niceDate = item.getString(KnowledgeTextManager.NICE_DATE);
                    knowledgeText.setNiceDate(niceDate);
                    String superChapterName = item.getString(KnowledgeTextManager.SUPER_CHAPTER_NAME);
                    knowledgeText.setSuperChapterName(superChapterName);
                    String title = item.getString(KnowledgeTextManager.TITLE);
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
