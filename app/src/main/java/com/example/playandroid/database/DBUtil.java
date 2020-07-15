package com.example.playandroid.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.playandroid.entity.Text;
import com.example.playandroid.manager.TextKeyManager;

import java.util.ArrayList;
import java.util.List;

public class DBUtil {

    // 导入本地数据
    public static List<Text> loadTextFromLocal(Context context) {
        List<Text> textList = new ArrayList<>();
        MyDatabaseHelper helper = new MyDatabaseHelper(context, MyDatabaseHelper.DATABASE_NAME,
                null, MyDatabaseHelper.CURRENT_VERSION);
        SQLiteDatabase database = helper.getReadableDatabase();
        String sql = "select * from text where del = ? order by id desc";
        Cursor cursor = database.rawQuery(sql, new String[]{"0"});
        if (cursor.moveToFirst()) {
            do {
                Text text = new Text();
                String author = cursor.getString(cursor.getColumnIndex(TextKeyManager.AUTHOR));
                text.setAuthor(author);
                String chapterName = cursor.getString(cursor.getColumnIndex(TextKeyManager.CHAPTER_NAME));
                text.setChapterName(chapterName);
                String link = cursor.getString(cursor.getColumnIndex(TextKeyManager.LINK));
                text.setLink(link);
                String niceDate = cursor.getString(cursor.getColumnIndex(TextKeyManager.NICE_DATE));
                text.setNiceDate(niceDate);
                String superChapterName = cursor.getString(cursor.getColumnIndex(TextKeyManager.SUPER_CHAPTER_NAME));
                text.setSuperChapterName(superChapterName);
                String title = cursor.getString(cursor.getColumnIndex(TextKeyManager.TITLE));
                text.setTitle(title);
                int id = cursor.getInt(cursor.getColumnIndex(TextKeyManager.ID));
                text.setId(id);
                textList.add(text);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return textList;
    }

    // 将数据写入数据库
    public static void writeToLocal(List<Text> texts, Context context) {
        MyDatabaseHelper helper = new MyDatabaseHelper(context, MyDatabaseHelper.DATABASE_NAME,
                null, MyDatabaseHelper.CURRENT_VERSION);
        SQLiteDatabase database = helper.getWritableDatabase();
        String sql = "insert into text (author, chapterName, link, niceDate," +
                " superChapterName, title, del) values(?, ?, ?, ?, ?, ?, ?)";
        for (Text text : texts) {

            database.execSQL(sql, new Object[]{text.getAuthor(), text.getChapterName(),
            text.getLink(), text.getNiceDate(), text.getSuperChapterName(), text.getTitle(), 0});
        }
        database.close();
        Log.d("TAG", "writeToLocal: --------write--------");
    }

}
