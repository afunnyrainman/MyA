package com.example.user.mya.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.mya.Adapter.LiveAdater;
import com.example.user.mya.R;
import com.example.user.mya.Utils.Utils;
import com.example.user.mya.entity.Live;
import com.example.user.mya.itemtouch.ItemTouchHlepCallback;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by user on 2016/11/16.
 */
public class Activity_LiveMain extends BaseActivity implements View.OnClickListener, SwipyRefreshLayout.OnRefreshListener {

    @Bind(R.id.rv_livemian)
    RecyclerView recyclerView;

    private LiveAdater LiveAdater;
    private List<Live> data = new ArrayList<Live>();
    private SwipyRefreshLayout swipeRefreshLayout;
    private int beginNum = 0;
    private ImageView top_tv_left;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livemain);
        ButterKnife.bind(this);
        initList();
        addShuaxin();

        swipeRefreshLayout.post(new Runnable() {//手动显示加载
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
        getDate();
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
            getDate();
        } else {
            getDateLoad();
        }
    }

    /**
     * 去BMOB中查询收藏
     */
    public void getDate() {
        if (Utils.isOnline(Activity_LiveMain.this)) {
            beginNum = 0;
            data.clear();
            BmobQuery<Live> bmobQuery = new BmobQuery<Live>();
            bmobQuery.setLimit(10);//10个
            bmobQuery.order("-createdAt");//降序
            addSubscription(
                    bmobQuery.findObjects(new FindListener<Live>() {
                        @Override
                        public void done(List<Live> object, BmobException e) {
                            if (e == null) {
                                for (Live gameScore : object) {
                                    //获得数据的objectId信息
                                    gameScore.getObjectId();
                                    //获得createdAt数据创建时间（注意是：createdAt，不是createAt）
                                    gameScore.getCreatedAt();
                                    data.add(gameScore);
                                }
                                LiveAdater.setUserInfoList(data);
                                LiveAdater.notifyDataSetChanged();
                            } else {
                                Log.e("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                            }
                            swipeRefreshLayout.setRefreshing(false);//加载按钮还原
                        }
                    })
            );
        } else {
            Utils.showOnlinError(Activity_LiveMain.this);
        }
    }

    private ItemTouchHlepCallback itemTouchCallback;
    private  TextView RecordTopTiele;
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
        RecordTopTiele.setText("收藏列表");

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Activity_LiveMain.this);
        //linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);// 竖着
        // 默认
        // linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);//横着

        // GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 5);
        //  StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(
        //       1, StaggeredGridLayoutManager.VERTICAL);// 瀑布流


        recyclerView.setLayoutManager(linearLayoutManager);

        LiveAdater = new LiveAdater(Activity_LiveMain.this);
        LiveAdater.setOnItemClickListener(onItemClickListener);
        recyclerView.setAdapter(LiveAdater);
        LiveAdater.setUserInfoList(data);
        LiveAdater.notifyDataSetChanged();
       /* recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState ==RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 ==LiveAdater.getItemCount()) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getDateLoad();
                        }
                    },0);
                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView,dx, dy);
                lastVisibleItem =linearLayoutManager.findLastVisibleItemPosition();
            }
        });*/

        itemTouchCallback = new ItemTouchHlepCallback(
                onItemTouchCallbackListenter);
        itemTouchCallback.setCanDrag(true);
        itemTouchCallback.setCanSwipe(true);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchCallback);
        LiveAdater.setItemTouchHelper(itemTouchHelper);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    /**
     * 滑动或者策侧滑
     */
    ItemTouchHlepCallback.onItemTouchCallbackListenter onItemTouchCallbackListenter = new ItemTouchHlepCallback.onItemTouchCallbackListenter() {
        @Override
        public void onSiwipet(int adapterPosition) {
            if (data != null) {
                data.remove(adapterPosition);
                LiveAdater.notifyItemRemoved(adapterPosition);
            }
        }

        @Override
        public boolean onMove(int srcPosition, int targetPosition) {
            if (data != null) {
                // 更换数据源
                Collections.swap(data, srcPosition, targetPosition);
                // 更新位置
                LiveAdater.notifyItemMoved(srcPosition, targetPosition);
                return true;
            }
            return false;
        }
    };

    public void getDateLoad() {
        if (Utils.isOnline(Activity_LiveMain.this)) {
            beginNum = beginNum + 10;
            BmobQuery<Live> bmobQuery = new BmobQuery<Live>();
            bmobQuery.setLimit(10);//10个
            bmobQuery.setSkip(beginNum);
            bmobQuery.order("-createdAt");//降序
            addSubscription(
                    bmobQuery.findObjects(new FindListener<Live>() {
                        @Override
                        public void done(List<Live> object, BmobException e) {
                            if (e == null) {
                                if (object.size() == 0) {
                                    beginNum = beginNum - 10;
                                    Utils.showToast(Activity_LiveMain.this, "沒有更多了");
                                } else {
                                    for (Live gameScore : object) {
                                        //获得数据的objectId信息
                                        gameScore.getObjectId();
                                        //获得createdAt数据创建时间（注意是：createdAt，不是createAt）
                                        gameScore.getCreatedAt();
                                        data.add(gameScore);
                                    }
                                    LiveAdater.setUserInfoList(data);
                                    LiveAdater.notifyDataSetChanged();
                                }
                            } else {
                                beginNum = beginNum - 10;
                                Log.e("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                            }
                            swipeRefreshLayout.setRefreshing(false);//加载按钮还原
                        }
                    })
            );
        } else {
            Utils.showOnlinError(Activity_LiveMain.this);
        }
    }

    /**
     * item点击监听
     */
    private LiveAdater.OnItemClickListener onItemClickListener = new LiveAdater.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Intent intent = new Intent();
            intent.setClass(Activity_LiveMain.this, Activity_Xiangxi.class);
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
