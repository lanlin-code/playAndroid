package com.example.playandroid.net;

public class Response {

    private StringBuilder mBuilder;

    Response(StringBuilder builder) {
        mBuilder = builder;
    }

    public StringBuilder body() {
        return mBuilder;
    }
}
