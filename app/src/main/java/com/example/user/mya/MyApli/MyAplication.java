package com.example.user.mya.MyApli;

import android.app.Application;
import android.content.Context;

import com.example.user.mya.Utils.LiteOrmDBUtil;
import com.litesuits.orm.LiteOrm;
import com.tencent.bugly.crashreport.CrashReport;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;

/**
 * Created by user on 2016/11/9.
 */
public class MyAplication extends Application {
    public static boolean isLogin = false;
    public static String username = "";
    private static MyAplication MyAplication = null;
    public static  final  String BMOBAPPLICTIONID="02547c7db96fb8c464f148d3d245b1cb";

    public static com.example.user.mya.MyApli.MyAplication getMyAplication() {
        if (MyAplication == null) {
            MyAplication = new MyAplication();
        }
        return MyAplication;
    }

    private String dijige = "1";
    private String urlMain = "http://www.sbkk8.cn/mingzhu/gudaicn/";
    private String urlTwo = "";
    private String urlXiangxi = "";
    private String urlthere = "";

    public String getUrlthere() {
        return urlthere;
    }

    public void setUrlthere(String urlthere) {
        this.urlthere = urlthere;
    }

    public String getDijige() {
        return dijige;
    }

    public void setDijige(String dijige) {
        this.dijige = dijige;
    }

    public String getUrlMain() {
        return urlMain;
    }

    public void setUrlMain(String urlMain) {
        this.urlMain = urlMain;
    }

    public String getUrlTwo() {
        return urlTwo;
    }

    public void setUrlTwo(String urlTwo) {
        this.urlTwo = urlTwo;
    }

    public String getUrlXiangxi() {
        return urlXiangxi;
    }

    public void setUrlXiangxi(String urlXiangxi) {
        this.urlXiangxi = urlXiangxi;
    }


    public static boolean isMain = true;

    private static final String DB_NAME = "gank.db";
    public static Context sContext;
    public static LiteOrm sDb;

    @Override
    public void onCreate() {
        super.onCreate();
        //第一：默认初始化
        Bmob.initialize(this, BMOBAPPLICTIONID);
        //初始化本地数据库
        sContext = this;
        // sDb = LiteOrm.newSingleInstance(this, DB_NAME);
        LiteOrmDBUtil.createDb(this, DB_NAME);

        // 使用推送服务时的初始化操作
        BmobInstallation.getCurrentInstallation().save();
        // 启动推送服务
        BmobPush.startWork(this);

         /* Bugly SDK初始化
        * 参数1：上下文对象
        * 参数2：APPID，平台注册时得到,注意替换成你的appId
        * 参数3：是否开启调试模式，调试模式下会输出'CrashReport'tag的日志
        */
        CrashReport.initCrashReport(getApplicationContext(), "d331c0a845", true);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
