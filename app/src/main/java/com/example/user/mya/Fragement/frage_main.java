package com.example.user.mya.Fragement;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.mya.Activity.Activity_Xiangxi;
import com.example.user.mya.Adapter.MainAdater;
import com.example.user.mya.MyApli.MyAplication;
import com.example.user.mya.R;
import com.example.user.mya.Utils.DateUtils;
import com.example.user.mya.Utils.SharedPreferencesUtils;
import com.example.user.mya.Utils.SimpleHUD;
import com.example.user.mya.Utils.Utils;
import com.example.user.mya.entity.one_entity;
import com.example.user.mya.itemtouch.ItemTouchHlepCallback;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by user on 2016/11/9.
 */
public class frage_main extends Fragment {
    RecyclerView recyclerView;
    private List<one_entity> data = new ArrayList<one_entity>();
    private MainAdater mainAdater;
    private ItemTouchHlepCallback itemTouchCallback;

    /**
     * 默认全部数据
     */
    //http://www.sbkk8.cn/mingzhu/gudaicn/
    private String url = "";

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDate(List<one_entity> data) {
        this.data = data;
        mainAdater.setUserInfoList(data);
        mainAdater.notifyDataSetChanged();

    }

    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.content_main, null);
        initList(v);
        if (MyAplication.getMyAplication().getDijige().equals("1")) {
            getOneValue(url);
            MyAplication.getMyAplication().setUrlMain(url);
        }
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        MyAplication.isMain = true;
    }

    /**
     * 初始化reclearlist
     */
    private void initList(View v) {
        recyclerView = (RecyclerView) v.findViewById(R.id.rv_main);
        // LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        //linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);// 竖着
        // 默认
        // linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);//横着

        // GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 5);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(
                2, StaggeredGridLayoutManager.VERTICAL);// 瀑布流

        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        mainAdater = new MainAdater(getActivity());
        mainAdater.setOnItemClickListener(onItemClickListener);
        recyclerView.setAdapter(mainAdater);


        initTouch();
    }

    /**
     * 滑动或者策侧滑
     */
    ItemTouchHlepCallback.onItemTouchCallbackListenter onItemTouchCallbackListenter = new ItemTouchHlepCallback.onItemTouchCallbackListenter() {
        @Override
        public void onSiwipet(int adapterPosition) {
            if (data != null) {
                data.remove(adapterPosition);
                mainAdater.notifyItemRemoved(adapterPosition);
            }
        }

        @Override
        public boolean onMove(int srcPosition, int targetPosition) {
            if (data != null) {
                // 更换数据源
                Collections.swap(data, srcPosition, targetPosition);
                // 更新位置
                mainAdater.notifyItemMoved(srcPosition, targetPosition);
                return true;
            }
            return false;
        }
    };
    /**
     * item点击监听
     */
    private MainAdater.OnItemClickListener onItemClickListener = new MainAdater.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            one_entity item = data.get(position);
            if (item.getType().equals(one_entity.LIEBIAO)) {
                getOneValue(item.getHref());

            } else {
                Intent intent = new Intent();
                intent.setClass(getActivity(), Activity_Xiangxi.class);
                intent.putExtra("url", item.getHref());
                startActivity(intent);
            }
        }
    };

    /**
     * 加载Touch
     */
    private void initTouch() {

         /* 自定义ItemTouchHelper*/

        // DefaultItemTouchHelper itemTouchHelper = new
        // DefaultItemTouchHelper(onItemTouchCallbackListenter);
        // itemTouchHelper.setCanSwipe(true);
        // itemTouchHelper.setCanDrag(true);

        itemTouchCallback = new ItemTouchHlepCallback(
                onItemTouchCallbackListenter);
        itemTouchCallback.setCanDrag(true);
        itemTouchCallback.setCanSwipe(true);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchCallback);
        mainAdater.setItemTouchHelper(itemTouchHelper);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void getOneValue(final String url) {
        setDiji(url);

        data.clear();
        String oneValue = SharedPreferencesUtils.getSharedPreferences(getActivity(), SharedPreferencesUtils.VALUEONENAME,
                SharedPreferencesUtils.ONENAME + url);
        if (oneValue == null) {
            // data.addAll(DateUtils.getDateList(this,url,name,handler));//网络获取
            if (Utils.isOnline(getActivity())) {
                SimpleHUD
                        .showLoadingMessage(getActivity(), "正在查询...", true);
                AsyncTask<String, Long, String> task = new AsyncTask<String, Long, String>() {
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
                            SharedPreferencesUtils.SavaSharedPreferences(getActivity(), SharedPreferencesUtils.VALUEONENAME,
                                    SharedPreferencesUtils.ONENAME + url, result);
                            data.addAll(DateUtils.getDate(result, url));
                            mainAdater.setUserInfoList(data);
                            mainAdater.notifyDataSetChanged();

                        } else {
                            Utils.showOnlinError(getActivity());
                        }
                    }
                };
                task.execute();
            } else {
                Utils.showOnlinError(getActivity());
            }
        } else {
            data.addAll(DateUtils.getDate(oneValue, url));//解析
            mainAdater.setUserInfoList(data);
            mainAdater.notifyDataSetChanged();

        }
    }

    /**
     * 修改第几个
     */
    private void setDiji(String url) {
        if (MyAplication.getMyAplication().getDijige().equals("1")) {
            MyAplication.getMyAplication().setDijige("2");

            MyAplication.getMyAplication().setUrlTwo(url);
        } else if (MyAplication.getMyAplication().getDijige().equals("2")) {
            MyAplication.getMyAplication().setDijige("3");
            MyAplication.getMyAplication().setUrlthere(url);

        } else if (MyAplication.getMyAplication().getDijige().equals("3")) {
            MyAplication.getMyAplication().setDijige("4");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
