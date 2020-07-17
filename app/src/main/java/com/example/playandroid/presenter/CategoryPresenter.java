package com.example.playandroid.presenter;

import com.example.playandroid.entity.Category;
import com.example.playandroid.entity.Item;
import com.example.playandroid.model.CategoryModel;

import java.util.List;

public class CategoryPresenter {
    public static List<Category> getCategoryList() {
        return CategoryModel.getCategory();
    }

    public static List<Item> loadCategory(Category category) {
        return CategoryModel.loadCategory(category, category.getCurrentPage());
    }

    public static List<Item> freshCategory(Category category) {
        return CategoryModel.loadCategory(category, 1);
    }
}
