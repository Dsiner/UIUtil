package com.d.lib.common.view.dialog;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.d.lib.common.R;
import com.d.lib.xrv.adapter.CommonAdapter;
import com.d.lib.xrv.adapter.CommonHolder;

import java.util.List;

/**
 * BottomHorSheetDialog
 * Created by D on 2017/7/27.
 */
public class BottomHorSheetDialog extends AbsSheetDialog<BottomHorSheetDialog.Bean> {

    public BottomHorSheetDialog(Context context, String title, List<Bean> datas) {
        super(context);
        this.mTitle = title;
        this.mDatas = datas;
        initView(mRootView);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.lib_pub_dialog_bottom_style_hor;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new SheetAdapter(mContext, mDatas, R.layout.lib_pub_adapter_dlg_bottom_hor);
    }

    @Override
    protected void initView(View rootView) {
        initRecyclerList(rootView, R.id.rv_list, LinearLayoutManager.HORIZONTAL);

        TextView tvCancel = (TextView) rootView.findViewById(R.id.tv_cancel);
        TextView tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
        if (!TextUtils.isEmpty(mTitle)) {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(mTitle);
        } else {
            tvTitle.setVisibility(View.GONE);
        }

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(-1, null);
            }
        });
    }

    public class SheetAdapter extends CommonAdapter<Bean> {
        SheetAdapter(Context context, List<Bean> datas, int layoutId) {
            super(context, datas, layoutId);
        }

        @Override
        public void convert(final int position, CommonHolder holder, final Bean item) {
            holder.setText(R.id.tv_item, item.item);
            holder.setImageResource(R.id.iv_item, item.drawble);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(position, item);
                }
            });
        }
    }

    public static class Bean {
        public String item;
        public int drawble;

        public Bean(String item, int drawble) {
            this.item = item;
            this.drawble = drawble;
        }
    }
}