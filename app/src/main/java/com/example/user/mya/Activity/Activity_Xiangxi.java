package com.example.user.mya.Activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.mya.MyApli.MyAplication;
import com.example.user.mya.R;
import com.example.user.mya.Utils.DateUtils;
import com.example.user.mya.Utils.LiteOrmDBUtil;
import com.example.user.mya.Utils.SharedPreferencesUtils;
import com.example.user.mya.Utils.SimpleHUD;
import com.example.user.mya.Utils.Utils;
import com.example.user.mya.entity.Live;
import com.example.user.mya.entity.Record;
import com.example.user.mya.entity.Xiangxi_Entity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by user on 2016/11/9.
 */
public class Activity_Xiangxi extends BaseActivity implements View.OnClickListener {
    private FrameLayout content_main;
    private ImageView iv4;
    private ImageView iv3;
    private ImageView iv2;
    private ImageView iv1;


    private Xiangxi_Entity xiangxi_entity;

    private String url = "";

    private BmobBatch Mybmo;

    private TextView tv_title, tv_content;

    private String Thisresult;

    private String re;
    private String title;
    private final int radius1 = 500;
    private List<ImageView> imageViews = new ArrayList<ImageView>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.xiaoshuo_iangxi);
        MyAplication.getMyAplication().setDijige("4");
        init();
        addOnclick();
    }

    /**
     * 缓存最后一次查看的文章的标题
     * @param title
     */
    private void insertJllu(String title){
        SharedPreferencesUtils.SavaSharedPreferences(Activity_Xiangxi.this,
                SharedPreferencesUtils.LOGINVALUE,SharedPreferencesUtils.TITLEJILU,title);
    }

    private void initBar(String title) {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //使用CollapsingToolbarLayout必须把title设置到CollapsingToolbarLayout上，设置到Toolbar上则不会显示
        CollapsingToolbarLayout mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        mCollapsingToolbarLayout.setTitle(title);
        //通过CollapsingToolbarLayout修改字体颜色
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);//设置还没收缩时状态下字体颜色
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.GREEN);//设置收缩后Toolbar上字体的颜色
    }

    /**
     * 添加点击事件
     */
    private void addOnclick() {
        iv2.setOnClickListener(this);
    }
    /**
     * 像bmob云服务器插入数据
     */
   /* private void addBmob(String content){

        Record record=new Record();
        record.setUsername("测试");
        record.setUrl(url);
        record.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if(e==null){
                    toast("添加数据成功，返回objectId为："+objectId);
                    toast("创建数据失败：" + e.getMessage());
                }
            }
        });
    }*/

    /**
     * 吐司方法
     *
     * @param msg
     */
    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 加载布局
     */
    private void init() {
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        content_main = (FrameLayout) findViewById(R.id.content_main);
        iv4 = (ImageView) findViewById(R.id.iv4);
        imageViews.add(iv4);
        iv3 = (ImageView) findViewById(R.id.iv3);
        imageViews.add(iv3);
        iv2 = (ImageView) findViewById(R.id.iv2);
        imageViews.add(iv2);
        iv1 = (ImageView) findViewById(R.id.iv1);

        Mybmo = new BmobBatch();
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_content = (TextView) findViewById(R.id.tv_content);
        url = getIntent().getStringExtra("url").toString();
        getDatess(url);
    }

    public void onClick(View v) {
        if (v.getId() == iv1.getId()) {
            Boolean isShowing = (Boolean) iv1.getTag();
            if (null == isShowing || isShowing == false) {
                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(iv1, "rotation", 0, 45);
                objectAnimator.setDuration(500);
                objectAnimator.start();
                iv1.setTag(true);
                showSectorMenu();
            } else {
                iv1.setTag(false);
                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(iv1, "rotation", 45, 0);
                objectAnimator.setDuration(500);
                objectAnimator.start();
                closeSectorMenu();
            }
        } else if (v.getId() == iv2.getId()) {

            isLive(MyAplication.username, url);

        }else if(v.getId()==iv4.getId()){
            Intent In=new Intent();
            In.setClass(Activity_Xiangxi.this,Activity_CommentMain.class);
            startActivity(In);
        }
    }

    /**
     * 添加收藏
     *
     * @param name 用户名+头像路径
     * @param url  文章路径
     *             标题  在我的收藏中显示用
     */
    private void addLive(String name, String url, String title) {
        if (MyAplication.isLogin) {
            Live live = new Live();
            live.setUrl(url);
            live.setUsername(name);
            live.setTitle(title);
            live.setInsertdate(Utils.getDate());
            live.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        toast("收藏成功");
                    } else {
                        toast("收藏失败");
                    }
                }
            });
        } else {
            toast("请您QQ登录后再进行此操作,谢谢。");
        }
    }

    /**
     * 是否收藏了
     *
     * @param username
     * @param url
     */
    private void isLive(String username, final String url) {
        //联网
        if (Utils.isOnline(Activity_Xiangxi.this)) {
            BmobQuery<Live> bmobQuery = new BmobQuery<Live>();
            bmobQuery.addWhereEqualTo("url", url);
            bmobQuery.addWhereEqualTo("username", username);
            addSubscription(
                    bmobQuery.findObjects(new FindListener<Live>() {
                        @Override
                        public void done(List<Live> object, BmobException e) {
                            if (e == null) {
                                if (object.size() >= 1) {
                                    toast("您已经收藏了这篇文章了!");
                                } else {
                                    addLive(MyAplication.username, url, title);
                                }
                            } else {
                                toast("收藏失败!");
                            }
                        }
                    })
            );
        } else {
            Utils.showOnlinError(Activity_Xiangxi.this);
        }

    }

    /**
     * 显示扇形菜单
     */
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private void showSectorMenu() {
        /***第一步，遍历所要展示的菜单ImageView*/
        for (int i = 0; i < imageViews.size(); i++) {
            PointF point = new PointF();
            /***第二步，根据菜单个数计算每个菜单之间的间隔角度*/
            int avgAngle = (90 / (imageViews.size() - 1));
            /**第三步，根据间隔角度计算出每个菜单相对于水平线起始位置的真实角度**/
            int angle = avgAngle * i;
            Log.d("dxq", "angle=" + angle);
            /**
             * ﻿﻿
             * 圆点坐标：(x0,y0)
             * 半径：r
             * 角度：a0
             * 则圆上任一点为：（x1,y1）
             * x1   =   x0   +   r   *   cos(ao   *   3.14   /180   )
             * y1   =   y0   +   r   *   sin(ao   *   3.14   /180   )
             */
            /**第四步，根据每个菜单真实角度计算其坐标值**/
            point.x = (float) Math.cos(angle * (Math.PI / 180)) * radius1;
            point.y = (float) -Math.sin(angle * (Math.PI / 180)) * radius1-20;
            Log.d("dxq", point.toString());

            /**第五步，根据坐标执行位移动画**/
            /**
             * 第一个参数代表要操作的对象
             * 第二个参数代表要操作的对象的属性
             * 第三个参数代表要操作的对象的属性的起始值
             * 第四个参数代表要操作的对象的属性的终止值
             */
            ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(imageViews.get(i), "translationX", 0, point.x);
            ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(imageViews.get(i), "translationY", 0, point.y);
            /**动画集合，用来编排动画**/
            AnimatorSet animatorSet = new AnimatorSet();
            /**设置动画时长**/
            animatorSet.setDuration(500);
            /**设置同时播放x方向的位移动画和y方向的位移动画**/
            animatorSet.play(objectAnimatorX).with(objectAnimatorY);
            /**开始播放动画**/
            animatorSet.start();
        }
    }


    /**
     * 关闭扇形菜单
     */
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private void closeSectorMenu() {
        for (int i = 0; i < imageViews.size(); i++) {
            PointF point = new PointF();
            int avgAngle = (90 / (imageViews.size() - 1));
            int angle = avgAngle * i;
            Log.d("dxq", "angle=" + angle);
            point.x = (float) Math.cos(angle * (Math.PI / 180)) * radius1;
            point.y = (float) -Math.sin(angle * (Math.PI / 180)) * radius1;
            Log.d("dxq", point.toString());

            ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(imageViews.get(i), "translationX", point.x, 0);
            ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(imageViews.get(i), "translationY", point.y, 0);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.setDuration(500);
            animatorSet.play(objectAnimatorX).with(objectAnimatorY);
            animatorSet.start();
        }
    }

    /**
     * 抓取信息
     *
     * @param url
     */
    private void getDatess(final String url) {
        String oneValue = SharedPreferencesUtils.getSharedPreferences(Activity_Xiangxi.this, SharedPreferencesUtils.VALUEONENAME,
                SharedPreferencesUtils.ONENAME + url);
        if (oneValue == null) {
            if (Utils.isOnline(Activity_Xiangxi.this)) {
                SimpleHUD
                        .showLoadingMessage(Activity_Xiangxi.this, "正在查询...", true);
                AsyncTask<String, Long, String> task = new AsyncTask<String, Long, String>() {
                    /**
                     * 查询
                     */
                    @Override
                    protected String doInBackground(String... s) {
                        // TODO Auto-generated method stub

                        Document doc = null;
                        try {
                            doc = Jsoup.connect(url).get();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return doc.toString();
                    }

                    // 查询成功
                    @Override
                    protected void onPostExecute(String result) {
                        // TODO Auto-generated method stub
                        super.onPostExecute(result);
                        SimpleHUD.dismiss();
                        if (Utils.Nonull(result)) {
                            Thisresult = result;
                            xiangxi_entity = new Xiangxi_Entity();

                            SharedPreferencesUtils.SavaSharedPreferences(Activity_Xiangxi.this, SharedPreferencesUtils.VALUEONENAME,
                                    SharedPreferencesUtils.ONENAME + url, result);
                            Xiangxi_Entity xiangxi_entity = DateUtils.getXiangxiDate(result);
                            setView(xiangxi_entity);
                            re = result;
                            //addBmob(result);
                            addRecord();
                        } else {
                            Utils.showOnlinError(Activity_Xiangxi.this);
                        }
                    }
                };
                task.execute();
            } else {
                Utils.showOnlinError(Activity_Xiangxi.this);
            }
        } else {
            xiangxi_entity = DateUtils.getXiangxiDate(oneValue);
            setView(xiangxi_entity);
            //addBmob(Thisresult);
            addRecord();
        }
    }

    /**
     * 添加数据库
     */
    private void addRecord() {
        LiteOrmDBUtil.deleteWhere(Record.class, "title", new String[]{title});//删除重复的
        Record record = new Record();
        record.setUrl(url);
        record.setTitle(title);
        record.setUsername(MyAplication.username);
        record.setContent(re);
        record.setInsertdate(Utils.getDate());
        LiteOrmDBUtil.insert(record);//插入
    }

    private void setView(Xiangxi_Entity view) {
        //tv_title.setText(view.getTitle());
        tv_content.setText(view.getContent());
        initBar(view.getTitle());
        title = view.getTitle();

        insertJllu(title);
    }

    @Override
    public void onStart() {
        super.onStart();
        MyAplication.isMain = false;
    }
}
