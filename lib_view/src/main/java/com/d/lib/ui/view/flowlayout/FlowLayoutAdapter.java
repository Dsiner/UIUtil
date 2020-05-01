package com.d.lib.ui.view.flowlayout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.d.lib.common.view.btb.CommonHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * FlowLayoutAdapter
 * Created by D on 2018/7/11.
 */
public abstract class FlowLayoutAdapter<T> {
    protected Context mContext;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;
    protected int mLayoutId;
    protected DataSetObserver mDataSetObserver;

    public FlowLayoutAdapter(Context context, List<T> datas, int layoutId) {
        mContext = context;
        mDatas = new ArrayList<T>();
        if (datas != null && datas.size() > 0) {
            mDatas.addAll(datas);
        }
        mInflater = LayoutInflater.from(mContext);
        mLayoutId = layoutId;
    }

    public void registerDataSetObserver(DataSetObserver observer) {
        this.mDataSetObserver = observer;
    }

    public void notifyDataSetChanged() {
        if (mDataSetObserver != null) {
            mDataSetObserver.notifyChanged();
        }
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

    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public T getItem(int position) {
        return mDatas == null ? null : mDatas.size() == 0 ? null : mDatas.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final CommonHolder holder = getViewHolder(position, convertView, parent);
        convert(position, holder, getItem(position));
        return holder.itemView;
    }

    public abstract void convert(int position, CommonHolder holder, T item);

    private CommonHolder getViewHolder(int position, View convertView, ViewGroup parent) {
        return CommonHolder.createViewHolder(mContext, convertView, parent,
                mLayoutId, position);
    }

    public interface DataSetObserver {
        void notifyChanged();
    }
}