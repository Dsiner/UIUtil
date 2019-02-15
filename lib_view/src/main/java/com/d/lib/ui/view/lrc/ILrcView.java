package com.d.lib.ui.view.lrc;

import android.content.Context;

import java.util.List;

/**
 * ILrcView
 * Edited by D on 2017/5/16.
 */
interface ILrcView {

    /**
     * 初始化画笔，颜色，字体大小等设置
     */
    void init(Context context);

    /**
     * 设置数据源
     */
    void setLrcRows(List<LrcRow> lrcRows);

    /**
     * 指定时间
     */
    void seekTo(int progress, boolean fromUser);

    /***
     * 设置歌词文字的缩放比例
     */
    void setLrcScale(float factor);

    /**
     * 重置
     */
    void reset();
}