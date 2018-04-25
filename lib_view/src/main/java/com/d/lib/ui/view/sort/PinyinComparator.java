package com.d.lib.ui.view.sort;

import android.text.TextUtils;

import java.util.Comparator;

/**
 * PinyinComparator
 * Created by D on 2017/6/7.
 */
public class PinyinComparator implements Comparator<SortBean> {
    @Override
    public int compare(SortBean o1, SortBean o2) {
        if (TextUtils.equals(o1.letter, "#")) {
            return 1;
        } else if (TextUtils.equals(o2.letter, "#")) {
            return -1;
        } else {
            return o1.pinyin.compareTo(o2.pinyin);
        }
    }
}
