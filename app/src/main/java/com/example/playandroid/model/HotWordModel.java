package com.example.playandroid.model;

import android.util.Log;

import com.example.playandroid.net.MyService;
import com.example.playandroid.net.OkHttpClient;
import com.example.playandroid.net.Request;
import com.example.playandroid.net.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HotWordModel {

    public static List<String> getHotWord() {
        String url = MyService.getHotWordLink();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        return parseData(response.body().toString());
    }

    private static List<String> parseData(String data) {
        List<String> hotWord = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray array = jsonObject.getJSONArray("data");
            for (int i = 0; i < array.length(); i ++) {
                JSONObject object = array.getJSONObject(i);
                String name = object.getString("name");
                Log.d("TAG", "parseData: " + name);
                hotWord.add(name);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return hotWord;
    }

}
