package com.d.lib.common.component.lv;

/**
 * CommonHolder for ListView
 * Created by D on 2017/4/25.
 */
public interface MultiItemTypeSupport<T> {
    int getLayoutId(int position, T t);

    int getViewTypeCount();

    int getItemViewType(int position, T t);
}
