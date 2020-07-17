package com.example.playandroid.model;

import com.example.playandroid.entity.Category;
import com.example.playandroid.entity.Item;
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

public class CategoryModel {

    public static List<Item> freshCategory(Category category) {
        List<Item> itemList = ItemModel.getItems(category.getCurrentPage(), category.getId());
        List<Item> currentList = category.getItems();
        Iterator<Item> iterator = itemList.iterator();
        while (iterator.hasNext()) {
            Item item = iterator.next();
            boolean del = false;
            for (Item existItem : currentList) {
                if (item.getId() == existItem.getId()) {
                    del = true;
                    break;
                }
            }
            if (del) iterator.remove();
        }
        return itemList;
    }

    public static List<Category> getCategory() {
        String url = MyService.getCategoryLink();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        String data = response.body().toString();
        return parseData(data);
    }

    private static List<Category> parseData(String data) {
        List<Category> categories = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(data);
            String errorCode = jsonObject.getString("errorCode");
            if (MyService.isSuccess(errorCode)) {
                JSONArray array = jsonObject.getJSONArray("data");
                for (int i = 0; i < array.length(); i ++) {
                    Category category = new Category();
                    JSONObject object = array.getJSONObject(i);
                    int id = object.getInt("id");
                    category.setId(id);
                    String name = object.getString("name");
                    category.setName(name);
                    categories.add(category);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return categories;
    }



}
