package com.d.uiutil.sort;

import android.content.Context;
import android.view.View;

import com.d.lib.ui.sort.SortBean;
import com.d.lib.xrv.adapter.CommonAdapter;
import com.d.lib.xrv.adapter.CommonHolder;
import com.d.uiutil.R;

import java.util.List;

/**
 * SortAdapter
 * Created by D on 2017/6/7.
 */
public class SortAdapter extends CommonAdapter<SortBean> {
    public SortAdapter(Context context, List<SortBean> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(int position, CommonHolder holder, SortBean item) {
        if (item.isLetter) {
            holder.setViewVisibility(R.id.llyt_sort, View.VISIBLE);
            holder.setText(R.id.tv_letter, item.letter);
        } else {
            holder.setViewVisibility(R.id.llyt_sort, View.GONE);
        }
        holder.setText(R.id.tv_content, item.content);
    }
}
