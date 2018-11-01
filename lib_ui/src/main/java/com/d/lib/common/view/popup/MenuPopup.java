package com.d.lib.common.view.popup;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.PopupWindow;

import com.d.lib.common.R;
import com.d.lib.common.utils.Util;
import com.d.lib.xrv.adapter.CommonAdapter;
import com.d.lib.xrv.adapter.CommonHolder;

import java.util.ArrayList;
import java.util.List;

public class MenuPopup extends AbstractPopup {
    protected List<Bean> datas;
    protected OnMenuListener listener;

    public MenuPopup(Context context, List<Bean> datas) {
        super(context, R.layout.lib_pub_popup_menu,
                Util.dip2px(context, 135),
                datas != null ? datas.size() * Util.dip2px(context, 40) + Util.dip2px(context, 6) : 0, true, -1);
        this.datas = datas != null ? datas : new ArrayList<Bean>();
        initView(rootView);
    }

    private RecyclerView.Adapter getAdapter() {
        return new SheetAdapter(context, datas, R.layout.lib_pub_adapter_popup_menu);
    }

    @Override
    protected void init() {

    }

    private void initView(View rootView) {
        initRecyclerList(rootView, R.id.rv_list, LinearLayoutManager.VERTICAL);
    }

    protected void initRecyclerList(View rootView, @IdRes int id, int orientation) {
        RecyclerView list = (RecyclerView) rootView.findViewById(id);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(orientation);
        list.setLayoutManager(layoutManager);
        list.setAdapter(getAdapter());
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void showAsDropDown(View anchor) {
        if (!isShowing() && context != null && !((Activity) context).isFinishing()) {
            super.showAsDropDown(anchor, -Util.dip2px(context, 135 - 45), 0);
        }
    }

    protected void onItemClick(int position, String item) {
        dismiss();
        if (listener != null) {
            listener.onClick(this, position, item);
        }
    }

    public class SheetAdapter extends CommonAdapter<Bean> {
        SheetAdapter(Context context, List<Bean> datas, int layoutId) {
            super(context, datas, layoutId);
        }

        @Override
        public void convert(final int position, CommonHolder holder, final Bean item) {
            holder.setViewVisibility(R.id.v_menu_line, position != 0 ? View.VISIBLE : View.GONE);
            holder.setText(R.id.tv_menu_item, item.item);
            holder.setTextColor(R.id.tv_menu_item, ContextCompat.getColor(mContext, item.color));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(position, item.item);
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

    public interface OnMenuListener {

        /**
         * Click item
         *
         * @param position From 0 to datas.size()-1;
         */
        void onClick(PopupWindow popup, int position, String item);
    }

    public void setOnMenuListener(OnMenuListener listener) {
        this.listener = listener;
    }
}
