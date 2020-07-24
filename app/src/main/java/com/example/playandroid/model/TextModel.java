package com.example.playandroid.model;

import com.example.playandroid.entity.Text;
import com.example.playandroid.manager.TextKeyManager;
import com.example.playandroid.net.MyService;
import com.example.playandroid.net.OkHttpClient;
import com.example.playandroid.net.Request;
import com.example.playandroid.net.RequestBody;
import com.example.playandroid.net.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TextModel {

    // 首页加载数据时调用
    public static synchronized List<Text> getTexts(int page) {
        String link = MyService.getHomeTextLink() + page + MyService.getDataType();
        Request request = new Request.Builder().url(link).build();
        Response response = new OkHttpClient().newCall(request).execute();
        String data = response.body().toString();
        return parseData(data);
    }

    // 搜索得到的文章数据
    public static List<Text> getSearchText(int page, String keyword) {
        String url = MyService.getSearchLink(page);
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new RequestBody.Builder().add("k", keyword).build();
        Request request = new Request.Builder().url(url).post(requestBody).build();
        Response response = client.newCall(request).execute();
        String data = response.body().toString();
        return parseData(data);
    }


    public static Text getRecommendText() {
        String url = MyService.getRecommendUrl();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        return parseRecommend(response.body().toString());
    }

    private static Text parseRecommend(String data) {
        Text text = null;
        try {
            JSONObject jsonObject = new JSONObject(data);
            String errorCode = jsonObject.getString("errorCode");
            if (!MyService.isSuccess(errorCode)) return text;
            JSONObject object = jsonObject.getJSONObject("data");
            JSONArray array = object.getJSONArray("datas");
            int position = (int) (array.length() * Math.random());
            JSONObject recommend = array.getJSONObject(position);
            text = new Text();
            String title = recommend.getString(TextKeyManager.TITLE);
            text.setTitle(title);
            String link = recommend.getString(TextKeyManager.LINK);
            text.setLink(link);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return text;
    }


    /**
     * 解析网络请求的数据
     * @param data 发送网络请求得到的数据
     * @return 如果网络请求返回的errorCode=1，返回一个空的List，否则返回一个含有多个text的List
     */

     private static List<Text> parseData(String data) {
        List<Text> texts = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(data);
            String code = object.getString("errorCode");
            if (!MyService.isSuccess(code)) return texts;
            JSONObject dataObject = object.getJSONObject("data");
            JSONArray array = dataObject.getJSONArray("datas");
            for (int i = 0; i < array.length(); i ++) {
                Text text = new Text();
                JSONObject jsonObject = array.getJSONObject(i);
                String author = jsonObject.getString(TextKeyManager.AUTHOR);
                text.setAuthor(author);
                String chapterName = jsonObject.getString(TextKeyManager.CHAPTER_NAME);
                text.setChapterName(chapterName);
                String link = jsonObject.getString(TextKeyManager.LINK);
                text.setLink(link);
                String niceDate = jsonObject.getString(TextKeyManager.NICE_DATE);
                text.setNiceDate(niceDate);
                String superChapterName = jsonObject.getString(TextKeyManager.SUPER_CHAPTER_NAME);
                text.setSuperChapterName(superChapterName);
                String title = jsonObject.getString(TextKeyManager.TITLE);
                text.setTitle(title);
                int id = jsonObject.getInt(TextKeyManager.ID);
                text.setId(id);
                texts.add(text);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return texts;
    }
}
