package com.example.playandroid.model;


import com.example.playandroid.entity.Item;
import com.example.playandroid.manager.ItemManager;
import com.example.playandroid.net.MyService;
import com.example.playandroid.net.OkHttpClient;
import com.example.playandroid.net.Request;
import com.example.playandroid.net.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class ItemModel {

    static List<Item> getItems(int page, int cid) {
        String url = MyService.getItemsLink(page, cid);
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
            String code = jsonObject.getString(ItemManager.ERROR_CODE);
            if (MyService.isSuccess(code)) {
                JSONObject object = jsonObject.getJSONObject(ItemManager.DATA);
                JSONArray array = object.getJSONArray(ItemManager.DATAS);
                for (int i = 0; i < array.length(); i ++) {
                    JSONObject itemData = array.getJSONObject(i);
                    Item item = new Item();
                    String author = itemData.getString(ItemManager.AUTHOR);
                    item.setAuthor(author);
                    String description = itemData.getString(ItemManager.DESC);
                    item.setDescription(description);
                    String picUrl = itemData.getString(ItemManager.ENVELOP_PIC);
                    item.setPictureLink(picUrl);
                    String link = itemData.getString(ItemManager.LINK);
                    item.setLink(link);
                    String niceDate = itemData.getString(ItemManager.NICE_DATE);
                    item.setNiceDate(niceDate);
                    String title = itemData.getString(ItemManager.TITLE);
                    item.setTitle(title);
                    int id = itemData.getInt(ItemManager.ID);
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
