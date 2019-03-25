package com.example.cracker.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.cracker.bean.Note;

import java.util.ArrayList;
import java.util.List;


public class DBDao {

    private static DBDao userDB;

    private SQLiteDatabase db;

    private DBDao(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
    }
    public static DBDao getInstance(Context context) {
        if (userDB == null) {
            userDB = new DBDao(context);
        }
        return userDB;
    }

    public ContentValues getContentValues(Note order){
        ContentValues cv = new ContentValues();
        cv.put("title", order.getTitle());
        cv.put("content", order.getContent());
        cv.put("time", order.getTime());
        return cv;

    }


    public Boolean insertNote(Note order) {
        if (order != null) {
            long result = db.insert("notes", null, getContentValues(order));
            if (result != -1) {
                return true;
            } else {
                return false;
            }
        }
        return  false;
    }

    public Boolean del(String id) {
        db.delete("notes", "id" + "=?" , new String[]{id});
        return  false;
    }


    public List<Note> loadNoteByTime(String Notetime){
        ArrayList<Note> orderList = new ArrayList<Note>();
        Cursor cursor = db.query("notes", null, "time=?", new String[]{Notetime}, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                Note order = new Note(id,title, content, time);
                orderList.add(order);
            }

            cursor.close();
        }
        return orderList;
    }





}
