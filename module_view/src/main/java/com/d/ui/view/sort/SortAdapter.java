package com.d.ui.view.sort;

import android.content.Context;
import android.view.View;

import com.d.lib.pulllayout.rv.adapter.CommonAdapter;
import com.d.lib.pulllayout.rv.adapter.CommonHolder;
import com.d.lib.ui.view.sort.SortBean;
import com.d.ui.view.R;

import java.util.List;

/**
 * SortAdapter
 * Created by D on 2017/6/7.
 */
class SortAdapter extends CommonAdapter<SortBean> {
    SortAdapter(Context context, List<SortBean> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(int position, CommonHolder holder, SortBean item) {
        if (item.isLetter) {
            holder.setVisibility(R.id.llyt_sort, View.VISIBLE);
            holder.setText(R.id.tv_letter, item.letter);
        } else {
            holder.setVisibility(R.id.llyt_sort, View.GONE);
        }
        holder.setText(R.id.tv_content, item.content);
    }
}
