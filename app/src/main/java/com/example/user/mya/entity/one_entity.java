package com.example.user.mya.entity;

/**
 * Created by user on 2016/11/8.
 */
public class one_entity {
    public final static String LIEBIAO="1";
    public final static String XIANGXI="2";
    private String title;
    private String src;
    private String href;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getTitle() {
        return title;
    }

    public String getSrc() {
        return src;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}
