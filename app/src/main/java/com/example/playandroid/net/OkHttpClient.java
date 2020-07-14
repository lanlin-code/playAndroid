package com.example.playandroid.net;

public class OkHttpClient {

    // 将数据从request对象拷贝到新创建的call对象中，返回这个call对象
    public Call newCall(Request request) {
        Call call = new Call();
        call.setPost(request.isPost());
        call.setPostData(request.getPostData());
        call.setUrl(request.getUrl());
        return call;
    }

}
