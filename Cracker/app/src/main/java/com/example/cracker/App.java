package com.example.cracker;

import android.app.Application;
import android.content.Context;

import cn.bmob.v3.Bmob;



public class App extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        Bmob.initialize(this, "9a2a1c3bd3f4a0cac01abaf4da1164f7");
    }
    public static Context getContext(){
        return mContext;
    }
}
