package com.d.lib.ui.tab;

/**
 * TabItem
 * Created by D on 2017/8/25.
 */
public interface TabView {
    void setText(String text);

    void notifyData(boolean focus);

    void onScroll(float factor);
}
