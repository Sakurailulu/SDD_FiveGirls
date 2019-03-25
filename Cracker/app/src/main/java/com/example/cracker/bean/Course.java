package com.example.cracker.bean;

import cn.bmob.v3.BmobObject;

public class Course extends BmobObject {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
