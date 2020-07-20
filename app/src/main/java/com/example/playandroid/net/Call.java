package com.example.playandroid.net;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Call {

    private String mPostData;
    private String mUrl;
    private boolean mPost;

    void setPost(boolean mPost) {
        this.mPost = mPost;
    }

    void setPostData(String mPostData) {
        this.mPostData = mPostData;
    }

    void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public Response execute() {
        StringBuilder builder;
        if (mPost) builder = post();
        else builder = get();
        return new Response(builder);
    }

    /**
     * 发送post请求并读取返回数据
     * @return 返回一个携带数据的StringBuilder
     */
    private StringBuilder post() {
        HttpURLConnection connection = null;
        BufferedWriter outputStream = null;
        BufferedReader reader = null;
        StringBuilder stringBuilder = new StringBuilder();;
        try {
            connection = (HttpURLConnection) new URL(mUrl).openConnection();
            connection.setRequestMethod("POST");
            outputStream = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            Log.d("TAG", "post: " + mPostData);
            outputStream.write(mPostData);
            outputStream.close();
            int requestCode = connection.getResponseCode();
            if (requestCode == HttpURLConnection.HTTP_OK) {
                String line;
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = reader.readLine()) != null) stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (connection != null) connection.disconnect();
        }
        return stringBuilder;
    }

    /**
     * 发送get请求并读取返回数据
     * @return 返回携带数据的StringBuilder
     */
    private StringBuilder get() {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try {
            connection = (HttpURLConnection) new URL(mUrl).openConnection();
            connection.setRequestMethod("GET");
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK)
                while ((line = reader.readLine()) != null) builder.append(line);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) connection.disconnect();
        }
        return builder;
    }
}
