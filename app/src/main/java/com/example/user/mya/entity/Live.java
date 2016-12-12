package com.example.user.mya.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by user on 2016/11/16.
 */
public class Live  extends BmobObject {
    private String username;//用户名+用户头像路径(没登录不可收藏)
    private String url;
    private String title;
    private String insertdate;

    public String getInsertdate() {
        return insertdate;
    }

    public void setInsertdate(String insertdate) {
        this.insertdate = insertdate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
