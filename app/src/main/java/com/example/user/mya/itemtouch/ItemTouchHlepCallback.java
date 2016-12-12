package com.example.user.mya.itemtouch;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by dxc on 2016/7/19.
 */
public class ItemTouchHlepCallback extends ItemTouchHelper.Callback {
    private onItemTouchCallbackListenter onItemTouchCallbackListenter;
    private boolean isCanDrag = false;// 是否可以被拖拽
    private boolean isCanSwipe = false;// 是否可以被滑动

    public ItemTouchHlepCallback(
            onItemTouchCallbackListenter onItemTouchCallbackListenter) {
        this.onItemTouchCallbackListenter = onItemTouchCallbackListenter;
    }

    /**
     * 是否可以被拖拽
     *
     * @param canDrag
     */
    public void setCanDrag(boolean canDrag) {
        isCanDrag = canDrag;
    }

    /**
     * 是否可以被滑动
     *
     * @param canSwipe
     */
    public void setCanSwipe(boolean canSwipe) {
        isCanSwipe = canSwipe;
    }

    /**
     * 是否可以被滑动 横着就左右 竖着就上下滑动
     *
     * @return
     */
    @Override
    public boolean isItemViewSwipeEnabled() {
        return isCanSwipe;
    }

    /**
     * 长按式是否可以拖拽
     *
     * @return
     */
    @Override
    public boolean isLongPressDragEnabled() {
        return isCanDrag;
    }

    /**
     * 拖拽或者滑动 需要告诉系统拖拽或者滑动的方向
     *
     * @param recyclerView
     * @param viewHolder
     * @return
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView,
                                RecyclerView.ViewHolder viewHolder) {
        RecyclerView.LayoutManager layoutManager = recyclerView
                .getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {// GridLayoutManager
            int dragFlag = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
                    | ItemTouchHelper.UP | ItemTouchHelper.DOWN;// 长按
            int swipeFlag = 0;// 滑动
            return makeMovementFlags(dragFlag, swipeFlag);
        } else if (layoutManager instanceof LinearLayoutManager) {// LinearLayoutManager
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            int dragFlag = 0;
            int swipeFlag = 0;
            int orientation = linearLayoutManager.getOrientation();
            if (orientation == LinearLayoutManager.HORIZONTAL) {// 横着
                swipeFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                dragFlag = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            } else if (orientation == LinearLayoutManager.VERTICAL) {// 竖着
                dragFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                swipeFlag = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            }
            return makeMovementFlags(dragFlag, swipeFlag);
        }
        return 0;
    }

    /**
     * 拖拽的回调
     *
     * @param recyclerView
     * @param viewHolder   拖拽的item
     * @param target       拖拽到的位置
     * @return
     */
    @Override
    public boolean onMove(RecyclerView recyclerView,
                          RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        if (onItemTouchCallbackListenter != null) {
            return onItemTouchCallbackListenter.onMove(
                    viewHolder.getAdapterPosition(),
                    target.getAdapterPosition());
        }
        return false;
    }

    /**
     * 左右滑动
     *
     * @param viewHolder
     * @param direction
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (onItemTouchCallbackListenter != null) {
            onItemTouchCallbackListenter.onSiwipet(viewHolder
                    .getAdapterPosition());// 滑动删除的时候回调
        }
    }

    /**
     * 拖拽滑动回调
     */
    public interface onItemTouchCallbackListenter {
        /**
         * 左右滑动的时候
         *
         * @param adapterPosition
         */
        void onSiwipet(int adapterPosition);

        /**
         * 位置转换的时候
         *
         * @param srcPosition    拖拽的ITEM
         * @param targetPosition 被拖拽的位置 目的地
         * @return
         */
        boolean onMove(int srcPosition, int targetPosition);
    }

}
