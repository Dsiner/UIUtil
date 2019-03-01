package com.d.lib.common.component.lv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * CommonAdapter for ListView
 * Created by D on 2017/4/25.
 */
public abstract class CommonAdapter<T> extends BaseAdapter {
    protected Context mContext;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;
    protected int mLayoutId;
    protected MultiItemTypeSupport<T> mMultiItemTypeSupport;

    public CommonAdapter(Context context, List<T> datas, int layoutId) {
        mContext = context;
        mDatas = datas == null ? new ArrayList<T>() : datas;
        mInflater = LayoutInflater.from(mContext);
        mLayoutId = layoutId;
    }

    public CommonAdapter(Context context, List<T> datas, MultiItemTypeSupport<T> multiItemTypeSupport) {
        mContext = context;
        mDatas = datas == null ? new ArrayList<T>() : datas;
        mInflater = LayoutInflater.from(mContext);
        mMultiItemTypeSupport = multiItemTypeSupport;
    }

    public void setDatas(List<T> datas) {
        if (mDatas != null && datas != null) {
            mDatas.clear();
            mDatas.addAll(datas);
        }
    }

    public List<T> getDatas() {
        return mDatas;
    }

    @Override
    public int getItemViewType(int position) {
        if (mMultiItemTypeSupport != null) {
            mMultiItemTypeSupport.getItemViewType(position, position < mDatas.size() ? mDatas.get(position) : null);
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        if (mMultiItemTypeSupport != null) {
            return mMultiItemTypeSupport.getViewTypeCount();
        }
        return super.getViewTypeCount();
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas == null ? null : mDatas.size() == 0 ? null : mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final CommonHolder holder = getViewHolder(position, convertView, parent);
        convert(position, holder, getItem(position));
        return holder.getConvertView();
    }

    /**
     * @param position Position
     * @param holder   Holder
     * @param item     Position corresponding data
     */
    public abstract void convert(int position, CommonHolder holder, T item);

    private CommonHolder getViewHolder(int position, View convertView, ViewGroup parent) {
        if (mMultiItemTypeSupport != null) {
            if (mDatas != null && mDatas.size() > 0) {
                return CommonHolder.get(mContext, convertView, parent,
                        mMultiItemTypeSupport.getLayoutId(position, mDatas.get(position)), position);
            }
            return CommonHolder.get(mContext, convertView, parent,
                    mMultiItemTypeSupport.getLayoutId(position, null), position);
        }
        return CommonHolder.get(mContext, convertView, parent, mLayoutId, position);
    }
}
