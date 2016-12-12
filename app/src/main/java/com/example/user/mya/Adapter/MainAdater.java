package com.example.user.mya.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.user.mya.R;
import com.example.user.mya.entity.one_entity;

import java.util.List;


/**
 * Created by dxc on 2016/7/18.
 */
public class MainAdater extends RecyclerView.Adapter<MainAdater.viewHolder> {
    private Context context;

    public MainAdater() {

    }

    public MainAdater(Context context) {
        this.context = context;
    }

    /**
     * checkbox选中
     */
    private onItemCheckedChageListenter onCheckedChangeListener;
    private OnItemClickListener onItemClickListener;
    private List<one_entity> userInfoList = null;
    /**
     * 最右边的图片被触摸的时候
     */
    private ItemTouchHelper itemTouchHelper;

    public void setOnCheckedChangeListener(
            onItemCheckedChageListenter onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
    }

    public void setItemTouchHelper(ItemTouchHelper itemTouchHelper) {
        this.itemTouchHelper = itemTouchHelper;
    }

    public void setUserInfoList(List<one_entity> userInfoList) {
        this.userInfoList = userInfoList;
        super.notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * item点击事件
     */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    /**
     * checked改变
     */
    public interface onItemCheckedChageListenter {
        void onItemCheckchange(View view, int position, boolean checked);
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new viewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_main, parent, false));
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        holder.setDate();
    }

    @Override
    public int getItemCount() {
        return userInfoList == null ? 0 : userInfoList.size();
    }

    public one_entity getUser(int postion) {
        return userInfoList.get(postion);
    }

    class viewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener, View.OnTouchListener,
            CompoundButton.OnCheckedChangeListener {
        /**
         * 名字和性别
         */
        private TextView tv_title;
        /**
         * 触摸就可以拖拽
         */
        private ImageView img_fengmian;

        public viewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            img_fengmian = (ImageView) itemView.findViewById(R.id.img_fengmian);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            img_fengmian.setOnTouchListener(this);
        }

        public void setDate() {
            one_entity userInfo = getUser(getAdapterPosition());
            Glide.with(context).load(userInfo.getSrc()).error(R.drawable.ic_menu_camera).into(img_fengmian);
            tv_title.setText(userInfo.getTitle());
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (itemTouchHelper != null) {
                itemTouchHelper.startDrag(this);
            }
            return false;
        }

        /**
         * 选中状态改变的时候
         *
         * @param buttonView
         * @param isChecked
         */
        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            if (onCheckedChangeListener != null) {
                onCheckedChangeListener.onItemCheckchange(buttonView,
                        getAdapterPosition(), isChecked);
            }
        }
    }
}
