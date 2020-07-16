package com.example.playandroid.net;

public class MyService {
    private static String host = "https://wanandroid.com/";

    public static String getHomeTextLink() {
        return host + "article/list/";
    }

    // 返回码为0则成功
    public static boolean isSuccess(String code) {
        return "0".equals(code);
    }

    public static String getDataType() {
        return "/json";
    }

    public static String getKnowledgeSystemLink() {
        return host + "tree" + getDataType();
    }
}
