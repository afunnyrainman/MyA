package com.example.user.mya.entity;


import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.Table;

/**
 * Created by user on 2016/11/15.
 */
@Table("Record")
public class Record extends Soul{
    @Column("username")
    private String username;

    @Column("url")
    private String url;

    @Column("title")
    private String title;

    @Column("insertdate")
    private String insertdate;

    @Column("content")
    private String content;

    public String getInsertdate() {
        return insertdate;
    }

    public void setInsertdate(String insertdate) {
        this.insertdate = insertdate;
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

}
