package com.d.lib.common.view.dialog;

import android.content.Context;
import android.support.v4.content.ContextCompat;
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
 * BottomDialog
 * Created by D on 2017/7/27.
 * <p>
 * Modify by whb:
 * To display the title, call the constructor
 * BottomDialog(Context context, String title, List<Bean> datas) with the title parameter.
 * Call the BottomDialog(Context context, List<Bean> datas) constructor when no display is required,
 * or pass the title=null or "" method above.
 */
public class BottomVerSheetDialog extends AbsSheetDialog<BottomVerSheetDialog.Bean> {
    private boolean mIsChecked;

    public BottomVerSheetDialog(Context context, String title, List<Bean> datas) {
        super(context);
        this.mTitle = title;
        this.mDatas = datas;
        initView(mRootView);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.lib_pub_dialog_bottom_style_ver;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        int adapterLayoutId = R.layout.lib_pub_adapter_dlg_bottom_ver;
        if (mDatas != null && mDatas.size() > 0) {
            int size = mDatas.size();
            for (int i = 0; i < size; i++) {
                if (mDatas.get(i).isChecked) {
                    adapterLayoutId = R.layout.lib_pub_adapter_dlg_bottom_ver_check;
                    mIsChecked = true;
                    break;
                }
            }
        }
        return new SheetAdapter(mContext, mDatas, adapterLayoutId);
    }

    @Override
    protected void initView(View rootView) {
        initRecyclerList(rootView, R.id.rv_list, LinearLayoutManager.VERTICAL);

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
            holder.setTextColor(R.id.tv_item, ContextCompat.getColor(mContext, item.color));
            holder.setViewVisibility(R.id.iv_check, item.isChecked ? View.VISIBLE : View.GONE);
            holder.setViewVisibility(R.id.v_bttom_line, position < getItemCount() - 1 ? View.VISIBLE : View.GONE);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mIsChecked) {
                        item.isChecked = true;
                        notifyDataSetChanged();
                    }
                    onItemClick(position, item);
                }
            });
        }
    }

    public static class Bean {
        public String item;
        public int color;
        public boolean isChecked;

        public Bean(String item, int color, boolean isChecked) {
            this.item = item;
            this.color = color;
            this.isChecked = isChecked;
        }
    }
}
