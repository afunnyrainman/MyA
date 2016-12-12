package com.example.user.mya.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.mya.Adapter.CommentAdater;
import com.example.user.mya.MyApli.MyAplication;
import com.example.user.mya.R;
import com.example.user.mya.Utils.SharedPreferencesUtils;
import com.example.user.mya.Utils.Utils;
import com.example.user.mya.entity.Comment;
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
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by user on 2016/11/16.
 */
public class Activity_CommentMain extends BaseActivity implements View.OnClickListener, SwipyRefreshLayout.OnRefreshListener {

    @Bind(R.id.rv_livemian)
    RecyclerView recyclerView;

    private CommentAdater commentAdater;
    private List<Comment> data = new ArrayList<Comment>();
    private SwipyRefreshLayout swipeRefreshLayout;
    private int beginNum = 0;
    private ImageView top_tv_left;

    private TextView tv_send;

    private EditText EdComment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commentmain);
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
        if (Utils.isOnline(Activity_CommentMain.this)) {
            beginNum = 0;
            data.clear();
            BmobQuery<Comment> bmobQuery = new BmobQuery<Comment>();
            bmobQuery.addWhereEqualTo("username", MyAplication.username);
            bmobQuery.setLimit(10);//10个
            bmobQuery.order("-createdAt");//降序
            addSubscription(
                    bmobQuery.findObjects(new FindListener<Comment>() {
                        @Override
                        public void done(List<Comment> object, BmobException e) {
                            if (e == null) {
                                for (Comment gameScore : object) {
                                    //获得数据的objectId信息
                                    //gameScore.getObjectId();
                                    //获得createdAt数据创建时间（注意是：createdAt，不是createAt）
                                   // gameScore.getCreatedAt();
                                    Log.e("dxq","gameScore+"+gameScore.getUsername());
                                    data.add(gameScore);
                                }
                                commentAdater.setUserInfoList(data);
                                commentAdater.notifyDataSetChanged();
                            } else {
                                Log.e("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                            }
                            swipeRefreshLayout.setRefreshing(false);//加载按钮还原
                        }
                    })
            );
        } else {
            Utils.showOnlinError(Activity_CommentMain.this);
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
        tv_send= (TextView) findViewById(R.id.tv_send);
        EdComment= (EditText) findViewById(R.id.EdComment);

        tv_send.setOnClickListener(this);

        RecordTopTiele= (TextView) findViewById(R.id.RecordTopTiele);
        top_tv_left = (ImageView) findViewById(R.id.top_tv_left);
        top_tv_left.setOnClickListener(this);
        RecordTopTiele.setText("评论列表");

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Activity_CommentMain.this);
        //linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);// 竖着
        // 默认
        // linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);//横着

        // GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 5);
        //  StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(
        //       1, StaggeredGridLayoutManager.VERTICAL);// 瀑布流


        recyclerView.setLayoutManager(linearLayoutManager);

        commentAdater = new CommentAdater(Activity_CommentMain.this);
        commentAdater.setOnItemClickListener(onItemClickListener);
        recyclerView.setAdapter(commentAdater);
        commentAdater.setUserInfoList(data);
        commentAdater.notifyDataSetChanged();
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
        commentAdater.setItemTouchHelper(itemTouchHelper);
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
                commentAdater.notifyItemRemoved(adapterPosition);
            }
        }

        @Override
        public boolean onMove(int srcPosition, int targetPosition) {
            if (data != null) {
                // 更换数据源
                Collections.swap(data, srcPosition, targetPosition);
                // 更新位置
                commentAdater.notifyItemMoved(srcPosition, targetPosition);
                return true;
            }
            return false;
        }
    };

    public void getDateLoad() {
        if (Utils.isOnline(Activity_CommentMain.this)) {
            beginNum = beginNum + 10;
            BmobQuery<Comment> bmobQuery = new BmobQuery<Comment>();
            bmobQuery.addWhereEqualTo("username", MyAplication.username);
            bmobQuery.setLimit(10);//10个
            bmobQuery.setSkip(beginNum);
            bmobQuery.order("-createdAt");//降序
            addSubscription(
                    bmobQuery.findObjects(new FindListener<Comment>() {
                        @Override
                        public void done(List<Comment> object, BmobException e) {
                            if (e == null) {
                                if (object.size() == 0) {
                                    beginNum = beginNum - 10;
                                    Utils.showToast(Activity_CommentMain.this, "沒有更多了");
                                } else {
                                    for (Comment gameScore : object) {
                                        //获得数据的objectId信息
                                        gameScore.getObjectId();
                                        //获得createdAt数据创建时间（注意是：createdAt，不是createAt）
                                        gameScore.getCreatedAt();
                                        data.add(gameScore);
                                    }
                                    commentAdater.setUserInfoList(data);
                                    commentAdater.notifyDataSetChanged();
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
            Utils.showOnlinError(Activity_CommentMain.this);
        }
    }

    /**
     * item点击监听
     */
    private CommentAdater.OnItemClickListener onItemClickListener = new CommentAdater.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
           /* Intent intent = new Intent();
            intent.setClass(Activity_CommentMain.this, Activity_Xiangxi.class);
            intent.putExtra("url", data.get(position).getUrl());
            startActivity(intent);*/
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_tv_left:
                finish();
                break;
            case R.id.tv_send:
                insertComment();
                break;
        }
    }

    /**
     * 插入评论
     */
    private void insertComment(){
        if(Utils.isOnline(Activity_CommentMain.this)){
        String title= SharedPreferencesUtils.getSharedPreferences(Activity_CommentMain.this,
                SharedPreferencesUtils.LOGINVALUE,SharedPreferencesUtils.TITLEJILU);
            Comment comment=new Comment();
            comment.setContent(EdComment.getText().toString());
            comment.setTitle(title);
            if(MyAplication.username.equals("")){
                comment.setUsername("游客");
            }else{
                comment.setUsername(MyAplication.username);
            }
            comment.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if(e==null){
                        Utils.showToast(Activity_CommentMain.this,"评论成功!");
                    }else{

                    }
                }
            });
        }else{
            Utils.showOnlinError(Activity_CommentMain.this);
        }

    }

}
