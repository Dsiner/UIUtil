package com.d.lib.common.view.tab;

/**
 * TabItem
 * Created by D on 2017/8/25.
 */
public interface TabView {
    void setText(String text);

    void setPadding(int padding);

    void setNumber(String text, int visibility);

    void notifyData(boolean focus);

    void onScroll(float factor);
}
