package com.example.cracker.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {

    // 数据库名
    private static final String DATABASE_NAME = "note.db";
    public static final String CREATE_ARTICLE = "create table notes ("
            + "id integer primary key autoincrement, "
            + "title VARCHAR, "
            + "content VARCHAR, "
            + "type VARCHAR, "
            + "img VARCHAR, "
            + "time VARCHAR)";
    public static final String plan = "create table plans ("
            + "id integer primary key autoincrement, "
            + "des VARCHAR, "
            + "type VARCHAR, "
            + "status VARCHAR, "
            + "ke VARCHAR, "
            + "time VARCHAR)";
    private static final int DATABASE_VERSION = 1;


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL(plan);
        db.execSQL(CREATE_ARTICLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
