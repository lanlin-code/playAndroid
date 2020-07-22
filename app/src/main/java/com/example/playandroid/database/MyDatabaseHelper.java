package com.example.playandroid.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String CREATE_TABLE = "create table text(" +
            "id integer, author text, chapterName text, link text, niceDate text" +
            ", superChapterName text, title text, del integer)";

    private static final String CREATE_ANOTHER_TABLE = "create table history(id integer " +
            "primary key autoincrement, word text, del integer)";


    public static final String DATABASE_NAME = "MyApplication.db";
    public static final int CURRENT_VERSION = 8;

    public MyDatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_ANOTHER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists text");
        db.execSQL("drop table if exists history");
        onCreate(db);
    }
}
