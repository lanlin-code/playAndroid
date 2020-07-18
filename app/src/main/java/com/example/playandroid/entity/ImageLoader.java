package com.example.playandroid.entity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import com.example.playandroid.executor.MyThreadPool;
import com.example.playandroid.net.OkHttpClient;
import com.example.playandroid.net.Response;
import com.example.playandroid.util.MD5Util;
import com.example.playandroid.util.ThreadAdjustUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

public class ImageLoader {

    private LruCache<String, Bitmap> mCache;
    // 存标记，防止错位
    private Map<ImageView, String> mTags = new LinkedHashMap<>();
    public ImageLoader(int maxSize) {
        mCache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes()*value.getHeight();
            }
        };
    }

    public void display(String url, ImageView imageView) {
        Bitmap bitmap = mCache.get(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            return;
        }
        bitmap = loadFromLocal(url, imageView.getContext());
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            return;
        }
        loadFromNet(url, imageView);

    }

    private Bitmap loadFromLocal(String url, Context context) {
        File file = getCacheFile(url, context);
        // 如果本地有则加载进内存
        if (file.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            mCache.put(url, bitmap);
            return bitmap;
        }
        return null;
    }

    private File getCacheFile(String url, Context context) {
        String name = MD5Util.encode(url);
        // 获取当前状态
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // 挂载状态，sd卡存在
            File dir = new File(context.getCacheDir(), "Image");
            // 如果文件不存在则创建
            if (!dir.exists()) dir.mkdirs();
            return new File(dir, name);
        } else {
            File dir = new File(context.getCacheDir(), "Image");
            if (!dir.exists()) dir.mkdirs();
            return new File(dir, name);
        }
    }

    private void loadFromNet(String url, ImageView imageView) {
        mTags.put(imageView, url);
        MyThreadPool.execute(new ImgLoader(url, imageView));
    }

    class ImgLoader implements Runnable {

        String url;
        ImageView imageView;

        ImgLoader(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        public void run() {
            HttpURLConnection connection;
            InputStream is;
            try {
                URL link = new URL(url);
                connection = (HttpURLConnection) link.openConnection();
                is = connection.getInputStream();
                final Bitmap bitmap = BitmapFactory.decodeStream(is);
                saveToLocal(bitmap, url, imageView.getContext());
                mCache.put(url, bitmap);
                String current = mTags.get(imageView);
                if (url.equals(current)) {
                    ThreadAdjustUtil.post(new Runnable() {
                        @Override
                        public void run() {
                            display(url, imageView);
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void saveToLocal(Bitmap bitmap, String url, Context context) throws Exception {
        File file = getCacheFile(url, context);
        FileOutputStream outputStream = new FileOutputStream(file);

        /*
         *压缩图片，第一个参数如果是Bitmap.CompressFormat.PNG,不过第二个参数是何值，
         * 图片大小都不会改变，不支持PNG图片压缩。使用这个方法压缩图片，图片的大小不会
         * 改变，只是图片存储在磁盘上的大小会变化。第二个值（0-100）越小，存储在磁盘的图片文件
         * 越小。
         *
         */
        if (bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)) {
            outputStream.flush();
            outputStream.close();
        }
    }

}
