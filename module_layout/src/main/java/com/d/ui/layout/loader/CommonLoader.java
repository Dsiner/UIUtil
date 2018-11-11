package com.d.ui.layout.loader;

import com.d.lib.xrv.XRecyclerView;
import com.d.lib.xrv.adapter.CommonAdapter;
import com.d.lib.xrv.listener.IRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CommonLoader<T> {
    public final static int PAGE_COUNT = 10;

    private XRecyclerView mList;
    private CommonAdapter<T> mAdapter;
    private List<T> mDatas;
    private int mPageCount = PAGE_COUNT;
    private OnLoaderListener mListener;
    public int page = 1;

    public CommonLoader(XRecyclerView list, CommonAdapter<T> adapter) {
        this.mDatas = new ArrayList<T>();
        this.mList = list;
        this.mAdapter = adapter;
        this.mList.setLoadingListener(new IRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page = 1;
                if (mListener != null) {
                    mListener.onRefresh();
                }
            }

            @Override
            public void onLoadMore() {
                page++;
                if (mListener != null) {
                    mListener.onLoadMore();
                }
            }
        });
    }

    public void setPageCount(int count) {
        this.mPageCount = count;
    }

    public void setData(List<T> data) {
        if (data == null) {
            return;
        }
        int sizeLoad = data.size();
        initList(data);
        if (page == 1) {
            mList.refreshComplete();
        } else {
            mList.loadMoreComplete();
        }
        if (sizeLoad < mPageCount) {
            mList.setNoMore(true);
        }
        if (mListener == null) {
            return;
        }
        if (page == 1 && sizeLoad <= 0) {
            mListener.noContent();
        } else {
            mListener.loadSuccess();
        }
    }

    public void addTop(T data) {
        if (mDatas != null && data != null) {
            mDatas.add(0, data);
            mAdapter.setDatas(mDatas);
            mAdapter.notifyDataSetChanged();
            mList.scrollToPosition(0);
        }
    }

    public void addTop(List<T> datas) {
        if (mDatas != null && datas != null) {
            mDatas.addAll(0, datas);
            mAdapter.setDatas(mDatas);
            mAdapter.notifyDataSetChanged();
            mList.scrollToPosition(0);
        }
    }

    public void addData(T data) {
        if (mDatas != null && data != null) {
            mDatas.add(data);
            mAdapter.setDatas(mDatas);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void addData(List<T> datas) {
        if (mDatas != null && datas != null) {
            mDatas.addAll(datas);
            mAdapter.setDatas(mDatas);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void addData(int position, T data) {
        if (mDatas != null && data != null && position >= 0 && position <= mDatas.size()) {
            mDatas.add(position, data);
            mAdapter.setDatas(mDatas);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void addData(int position, List<T> datas) {
        if (mDatas != null && datas != null && position >= 0 && position <= mDatas.size()) {
            mDatas.addAll(position, datas);
            mAdapter.setDatas(mDatas);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void loadError() {
        if (page == 1) {
            mList.refreshComplete();
        } else {
            page--;
            mList.loadMoreError();
        }
        if (mListener != null) {
            mListener.loadError(mDatas.size() <= 0);
        }
    }

    private void initList(List<T> cacher) {
        if (page == 1) {
            mDatas.clear();
            mDatas.addAll(cacher);
            mAdapter.setDatas(mDatas);
            mAdapter.notifyDataSetChanged();
        } else {
            mDatas.addAll(cacher);
            mAdapter.setDatas(mDatas);
            mAdapter.notifyDataSetChanged();
        }
    }

    public List<T> getDatas() {
        return mDatas;
    }

    public interface OnLoaderListener {
        void onRefresh();

        void onLoadMore();

        void noContent();

        void loadSuccess();

        void loadError(boolean isEmpty);
    }

    public void setOnLoaderListener(OnLoaderListener listener) {
        this.mListener = listener;
    }
}
