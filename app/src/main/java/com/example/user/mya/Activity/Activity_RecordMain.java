package com.example.user.mya.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.mya.Adapter.RecordAdater;
import com.example.user.mya.R;
import com.example.user.mya.Utils.LiteOrmDBUtil;
import com.example.user.mya.Utils.Utils;
import com.example.user.mya.entity.Record;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by user on 2016/11/16.
 */
public class Activity_RecordMain extends BaseActivity implements View.OnClickListener, SwipyRefreshLayout.OnRefreshListener {

    @Bind(R.id.rv_livemian)
    RecyclerView recyclerView;

    private RecordAdater LiveAdater;
    private List<Record> data = new ArrayList<Record>();
    private SwipyRefreshLayout swipeRefreshLayout;
    private ImageView top_tv_left;

    private int beginIndex = 0;

    private TextView RecordTopTiele;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livemain);
        ButterKnife.bind(this);
        initList();
        addShuaxin();

        /*swipeRefreshLayout.post(new Runnable() {//手动显示加载
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });*/

        getdate();
    }

    //刷新
    private void getdate() {
        data.clear();
        //5、查询Conversation,并且只取10条
        List<Record> list =
                LiteOrmDBUtil.getQueryLimit(Record.class, 0, 10);
        data.addAll(list);
        LiveAdater.setUserInfoList(data);
        LiveAdater.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);//加载按钮还原
    }

    //上拉加载
    private void LoadDate() {
        beginIndex += 10;
        //5、查询Conversation,并且只取10条
        List<Record> list =
                LiteOrmDBUtil.getQueryLimit(Record.class, beginIndex, 10);
        data.addAll(list);
        if (list.size() == 0) {
            Utils.showToast(Activity_RecordMain.this, "没有更多了!");
            beginIndex -= 10;
        } else {
            LiveAdater.setUserInfoList(data);
            LiveAdater.notifyDataSetChanged();
        }
        swipeRefreshLayout.setRefreshing(false);//加载按钮还原
        Log.e("dxq","false");
    }

    //加载刷新控件
    private void addShuaxin() {
        swipeRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        // 设置下拉多少距离之后开始刷新数据
        swipeRefreshLayout.setDistanceToTriggerSync(100);

        swipeRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
        // 设置刷新动画的颜色，可以设置1或者更多.
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_green_light));
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        if (direction == SwipyRefreshLayoutDirection.TOP) {
            getdate();
        } else {
            LoadDate();
        }
    }

    /**
     * 初始化reclearlist
     */
    private void initList() {
        //透明状态栏
        //  getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        //  getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        RecordTopTiele= (TextView) findViewById(R.id.RecordTopTiele);
        top_tv_left = (ImageView) findViewById(R.id.top_tv_left);
        top_tv_left.setOnClickListener(this);
        RecordTopTiele.setText("浏览记录");

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Activity_RecordMain.this);
        //linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);// 竖着
        // 默认
        // linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);//横着

        // GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 5);
        //  StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(
        //       1, StaggeredGridLayoutManager.VERTICAL);// 瀑布流

        recyclerView.setLayoutManager(linearLayoutManager);

        LiveAdater = new RecordAdater(Activity_RecordMain.this);
        LiveAdater.setOnItemClickListener(onItemClickListener);
        recyclerView.setAdapter(LiveAdater);
        LiveAdater.setUserInfoList(data);
        LiveAdater.notifyDataSetChanged();
    }

    /**
     * item点击监听
     */
    private RecordAdater.OnItemClickListener onItemClickListener = new RecordAdater.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Intent intent = new Intent();
            intent.setClass(Activity_RecordMain.this, Activity_Xiangxi.class);
            intent.putExtra("url", data.get(position).getUrl());
            startActivity(intent);
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_tv_left:
                finish();
                break;
        }
    }

}
