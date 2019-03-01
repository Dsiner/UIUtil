package com.d.lib.common.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.d.lib.common.R;

import java.util.List;

public abstract class AbsSheetDialog<T> extends AbstractDialog {
    protected List<T> mDatas;
    protected String mTitle;
    protected OnItemClickListener<T> mListener;

    protected AbsSheetDialog(Context context) {
        super(context, R.style.lib_pub_dialog_style, true,
                Gravity.BOTTOM, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    protected AbsSheetDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected AbsSheetDialog(Context context, int themeResId, boolean isSetWin, int gravity, int width, int heith) {
        super(context, themeResId, isSetWin, gravity, width, heith);
    }

    @Override
    protected void init(View rootView) {

    }

    protected void initRecyclerList(View rootView, @IdRes int id, int orientation) {
        RecyclerView list = (RecyclerView) rootView.findViewById(id);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(orientation);
        list.setLayoutManager(layoutManager);
        list.setAdapter(getAdapter());
    }

    protected void onItemClick(int position, T item) {
        dismiss();
        if (mListener != null) {
            if (position == -1) {
                mListener.onCancel(this);
            } else {
                mListener.onClick(this, position, item);
            }
        }
    }

    protected abstract RecyclerView.Adapter getAdapter();

    protected abstract void initView(View rootView);

    public interface OnItemClickListener<T> {

        /**
         * Click item
         *
         * @param position From 0 to datas.size()-1;
         */
        void onClick(Dialog dlg, int position, T item);

        /**
         * Click cancel
         */
        void onCancel(Dialog dlg);
    }

    public void setOnItemClickListener(OnItemClickListener<T> listener) {
        this.mListener = listener;
    }
}
