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
 * InfoDialog
 * Created by D on 2018/6/15.
 */
public class InfoDialog extends AbsSheetDialog<InfoDialog.Bean> {

    public InfoDialog(Context context, String title, List<Bean> datas) {
        super(context, R.style.lib_pub_dialog_style, false, 0, 0, 0);
        this.mTitle = title;
        this.mDatas = datas;
        initView(mRootView);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.lib_pub_dialog_info;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new SheetAdapter(mContext, mDatas, R.layout.lib_pub_adapter_dlg_info);
    }

    @Override
    protected void initView(View rootView) {
        initRecyclerList(rootView, R.id.rv_list, LinearLayoutManager.VERTICAL);

        TextView tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
        if (!TextUtils.isEmpty(mTitle)) {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(mTitle);
        } else {
            tvTitle.setVisibility(View.GONE);
        }
    }

    public class SheetAdapter extends CommonAdapter<Bean> {
        SheetAdapter(Context context, List<Bean> datas, int layoutId) {
            super(context, datas, layoutId);
        }

        @Override
        public void convert(final int position, CommonHolder holder, final Bean item) {
            holder.setText(R.id.tv_title, item.title);
            holder.setText(R.id.tv_content, item.content);
        }
    }

    public static class Bean {
        public String title;
        public String content;

        public Bean(String title, String content) {
            this.title = title;
            this.content = content;
        }
    }
}
