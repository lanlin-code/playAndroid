package com.example.playandroid.net;

public class Request {

    private String mUrl;
    private boolean mIsPost;
    private String mPostData;

    public String getPostData() {
        return mPostData;
    }

    public String getUrl() {
        return mUrl;
    }

    public boolean isPost() {
        return mIsPost;
    }

    public static class Builder {
        private String mUrl;
        private boolean post = false;
        private String postData = null;

        public Builder url(String url) {
            mUrl = url;
            return this;
        }

        public Builder post(RequestBody body) {
            post = true;
            postData = body.getData();
            return this;
        }

        public Request build() {
            Request request = new Request();
            request.mIsPost = post;
            request.mUrl = mUrl;
            request.mPostData = postData;
            return request;
        }



    }

}
