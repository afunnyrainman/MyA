package com.example.user.mya.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by user on 2016/11/23.
 */
public class Comment  extends BmobObject {
    private String username;//用户姓名和头像路径
    private String content;
    private String title;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
