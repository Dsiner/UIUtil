package com.d.lib.ui.view.flowlayout;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.d.lib.pulllayout.lv.adapter.CommonHolder;
import com.d.lib.pulllayout.lv.adapter.MultiItemTypeSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * FlowLayoutAdapter
 * Created by D on 2018/7/11.
 */
public abstract class FlowLayoutAdapter<T> {
    protected Context mContext;
    protected List<T> mDatas;
    protected int mLayoutId;
    protected MultiItemTypeSupport<T> mMultiItemTypeSupport;
    protected DataSetObserver mDataSetObserver;

    public FlowLayoutAdapter(@NonNull Context context, List<T> datas, int layoutId) {
        mContext = context;
        mDatas = datas != null ? new ArrayList<>(datas) : new ArrayList<T>();
        mLayoutId = layoutId;
    }

    public FlowLayoutAdapter(@NonNull Context context, List<T> datas, MultiItemTypeSupport<T> multiItemTypeSupport) {
        mContext = context;
        mDatas = datas != null ? new ArrayList<>(datas) : new ArrayList<T>();
        mMultiItemTypeSupport = multiItemTypeSupport;
    }

    public void registerDataSetObserver(DataSetObserver observer) {
        this.mDataSetObserver = observer;
    }

    public void notifyDataSetChanged() {
        if (mDataSetObserver != null) {
            mDataSetObserver.notifyChanged();
        }
    }

    public List<T> getDatas() {
        return mDatas;
    }

    public void setDatas(List<T> datas) {
        if (mDatas != null && datas != null) {
            mDatas.clear();
            mDatas.addAll(datas);
        }
    }

    public int getCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    public int getViewTypeCount() {
        if (mMultiItemTypeSupport != null) {
            return mMultiItemTypeSupport.getViewTypeCount();
        }
        return 1;
    }

    public int getItemViewType(int position) {
        if (mMultiItemTypeSupport != null) {
            if (mDatas != null && mDatas.size() > 0) {
                return mMultiItemTypeSupport.getItemViewType(position, mDatas.get(position));
            }
        }
        return 0;
    }

    public T getItem(int position) {
        return mDatas != null && mDatas.size() > 0 ? mDatas.get(position) : null;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final int itemViewType = getItemViewType(position);
        CommonHolder holder;
        if (convertView == null) {
            holder = onCreateViewHolder(parent, itemViewType);
        } else {
            // getTag
            holder = (CommonHolder) convertView.getTag();
            if (holder.getItemViewType() != itemViewType) {
                holder = onCreateViewHolder(parent, itemViewType);
            }
        }
        convert(position, holder, mDatas.get(position));
        return holder.itemView;
    }

    @NonNull
    public CommonHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = mLayoutId;
        if (mMultiItemTypeSupport != null) {
            // MultiType
            if (mDatas != null && mDatas.size() > 0) {
                layoutId = mMultiItemTypeSupport.getLayoutId(viewType);
            }
        }
        CommonHolder holder = CommonHolder.create(mContext, parent, layoutId);
        holder.setItemViewType(viewType);
        // setTag
        holder.itemView.setTag(holder);
        onViewHolderCreated(holder, holder.itemView);
        return holder;
    }

    public void onViewHolderCreated(CommonHolder holder, View itemView) {
    }

    /**
     * @param position The position of the item within the adapter's data set.
     * @param holder   Holder
     * @param item     Data
     */
    public abstract void convert(int position, CommonHolder holder, T item);

    public interface DataSetObserver {
        void notifyChanged();
    }
}