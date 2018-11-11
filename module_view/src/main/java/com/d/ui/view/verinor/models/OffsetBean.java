package com.d.ui.view.verinor.models;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * RecylcerView滚动位置
 * Created by D on 2018/2/12.
 */
public class OffsetBean {
    private int lastPosition;
    private int lastOffset;

    public static void setPositionAndOffset(RecyclerView rv) {
        if (rv == null || rv.getTag() == null || !(rv.getTag() instanceof OffsetBean)) {
            return;
        }
        OffsetBean tag = (OffsetBean) rv.getTag();

        LinearLayoutManager layoutManager = (LinearLayoutManager) rv.getLayoutManager();
        // 获取可视的第一个view
        View topView = layoutManager.getChildAt(0);
        if (topView != null) {
            // 得到该View的数组位置
            tag.lastPosition = layoutManager.getPosition(topView);
            // 获取与该view的左部的偏移量
            tag.lastOffset = topView.getLeft();
        }
    }

    public static void scrollToPositionWithOffset(RecyclerView rv) {
        if (rv == null || rv.getTag() == null || !(rv.getTag() instanceof OffsetBean)) {
            return;
        }
        OffsetBean tag = (OffsetBean) rv.getTag();
        LinearLayoutManager llManager = (LinearLayoutManager) rv.getLayoutManager();
        llManager.scrollToPositionWithOffset(tag.lastPosition, tag.lastOffset);
    }
}
