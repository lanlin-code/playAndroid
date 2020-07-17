package com.example.playandroid.model;

import android.util.Log;

import com.example.playandroid.entity.Item;
import com.example.playandroid.net.MyService;
import com.example.playandroid.net.OkHttpClient;
import com.example.playandroid.net.Request;
import com.example.playandroid.net.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ItemModel {

    public static List<Item> getItems(int page, int cid) {
        String url = MyService.getItemsLink(page, cid);
        Log.d("TAG", "getItems: " + url);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        String data = response.body().toString();
        return parseData(data);
    }

    private static List<Item> parseData(String data) {
        List<Item> itemList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(data);
            String code = jsonObject.getString("errorCode");
            if (MyService.isSuccess(code)) {
                JSONObject object = jsonObject.getJSONObject("data");
                JSONArray array = object.getJSONArray("datas");
                for (int i = 0; i < array.length(); i ++) {
                    JSONObject itemData = array.getJSONObject(i);
                    Item item = new Item();
                    String author = itemData.getString("author");
                    item.setAuthor(author);
                    String description = itemData.getString("desc");
                    item.setDescription(description);
                    String picUrl = itemData.getString("envelopePic");
                    item.setPictureLink(picUrl);
                    String link = itemData.getString("link");
                    item.setLink(link);
                    String niceDate = itemData.getString("niceDate");
                    item.setNiceDate(niceDate);
                    String title = itemData.getString("title");
                    item.setTitle(title);
                    int id = itemData.getInt("id");
                    item.setId(id);
                    itemList.add(item);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return itemList;
    }

}
