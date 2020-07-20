package com.example.playandroid.net;

import android.text.TextUtils;

public class RequestBody {

    private String data;



    public String getData() {
        return data;
    }

    public static class Builder {
        private StringBuilder builder = new StringBuilder();

        // 添加要发送的数据
        public Builder add(String key, String value) {
            if (TextUtils.isEmpty(builder.toString())) {
                builder.append(key).append("=").append(value);
            } else {
                builder.append("&").append(key).append("=").append(value);
            }
            return this;
        }

        // 将Builder的数据拷贝到RequestBody对象中，返回这个对象
        public RequestBody build() {
            RequestBody requestBody = new RequestBody();
            requestBody.data = builder.toString();
            return requestBody;
        }

    }

}
