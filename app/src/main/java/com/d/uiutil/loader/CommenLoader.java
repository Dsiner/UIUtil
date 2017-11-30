package com.d.uiutil.loader;

import com.d.lib.xrv.XRecyclerView;
import com.d.lib.xrv.adapter.CommonAdapter;
import com.d.lib.xrv.listener.IRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CommenLoader<T> {
    private XRecyclerView list;
    private CommonAdapter adapter;
    private ArrayList<T> mDatas;
    private int pageCount = 10;
    private OnLoaderListener listener;
    public int page = 1;

    public CommenLoader(XRecyclerView list, CommonAdapter adapter) {
        this.mDatas = new ArrayList<T>();
        this.list = list;
        this.adapter = adapter;
        this.list.setLoadingListener(new IRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page = 1;
                if (listener != null) {
                    listener.onRefresh();
                }
            }

            @Override
            public void onLoadMore() {
                page++;
                if (listener != null) {
                    listener.onLoadMore();
                }
            }
        });
    }

    public void setPageCount(int count) {
        this.pageCount = count;
    }

    public void setData(List<T> data) {
        if (data == null) {
            return;
        }
        int sizeLoad = data.size();
        initList(data);
        if (page == 1) {
            list.refreshComplete();
        } else {
            list.loadMoreComplete();
        }
        if (sizeLoad < pageCount) {
            list.setNoMore(true);
        }
        if (listener == null) {
            return;
        }
        if (page == 1 && sizeLoad <= 0) {
            listener.noContent();
        } else {
            listener.loadSuccess();
        }
    }

    public void addToTop(T data) {
        if (mDatas != null && data != null) {
            mDatas.add(0, data);
            adapter.setDatas(mDatas);
            adapter.notifyDataSetChanged();
            list.scrollToPosition(0);
        }
    }

    public void addToTop(List<T> datas) {
        if (mDatas != null && datas != null) {
            mDatas.addAll(0, datas);
            adapter.notifyDataSetChanged();
            list.scrollToPosition(0);
        }
    }

    public void addData(T data, int position) {
        if (mDatas != null && data != null) {
            mDatas.add(position, data);
            adapter.setDatas(mDatas);
            adapter.notifyDataSetChanged();
        }
    }

    public void loadError() {
        if (page == 1) {
            list.refreshComplete();
        } else {
            page--;
            list.loadMoreError();
        }
        if (listener != null) {
            listener.loadError(mDatas.size() <= 0);
        }
    }

    private void initList(List<T> cacher) {
        if (page == 1) {
            mDatas.clear();
            mDatas.addAll(cacher);
            adapter.setDatas(mDatas);
            adapter.notifyDataSetChanged();
        } else {
            mDatas.addAll(cacher);
            adapter.setDatas(mDatas);
            adapter.notifyDataSetChanged();
        }
    }

    public ArrayList<T> getDatas() {
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
        this.listener = listener;
    }
}
