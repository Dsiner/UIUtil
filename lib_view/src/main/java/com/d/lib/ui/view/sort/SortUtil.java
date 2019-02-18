package com.d.lib.ui.view.sort;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.promeg.pinyinhelper.Pinyin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * SortUtil
 * Created by D on 2017/6/7.
 */
public class SortUtil {
    private List<SortBean> mDatas;
    private Map<String, int[]> mLetterMap;
    private int mLastFirstVisibleItem = -1;

    public void onScrolled(RecyclerView recyclerView, View layout, TextView tvLetter) {
        if (recyclerView == null || layout == null || tvLetter == null || mLetterMap == null || mDatas == null) {
            return;
        }
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager == null || !(manager instanceof LinearLayoutManager)) {
            return;
        }
        int firstVisibleItemPosition = ((LinearLayoutManager) manager).findFirstVisibleItemPosition();

        int[] value = mLetterMap.get(mDatas.get(firstVisibleItemPosition).letter);
        int nextSectionPosition = value != null ? value[1] : -1;
        if (firstVisibleItemPosition != mLastFirstVisibleItem) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) layout.getLayoutParams();
            params.topMargin = 0;
            layout.setLayoutParams(params);
            tvLetter.setText(mDatas.get(firstVisibleItemPosition).letter);
        }
        if (nextSectionPosition == firstVisibleItemPosition + 1) {
            View childView = recyclerView.getChildAt(0);
            if (childView != null) {
                int titleHeight = layout.getHeight();
                int bottom = childView.getBottom();
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) layout.getLayoutParams();
                if (bottom < titleHeight) {
                    float pushedDistance = bottom - titleHeight;
                    params.topMargin = (int) pushedDistance;
                    layout.setLayoutParams(params);
                } else {
                    if (params.topMargin != 0) {
                        params.topMargin = 0;
                        layout.setLayoutParams(params);
                    }
                }
            }
        }
        mLastFirstVisibleItem = firstVisibleItemPosition;
    }

    public void onChange(int index, String c, RecyclerView recyclerView) {
        if (recyclerView == null || mLetterMap == null) {
            return;
        }
        RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();
        if (lm == null || !(lm instanceof LinearLayoutManager)) {
            return;
        }
        LinearLayoutManager manager = (LinearLayoutManager) lm;
        if (index == 0) {
            manager.scrollToPositionWithOffset(0, 0); // 置顶
            return;
        }
        int[] value = mLetterMap.get(c);
        if (value != null) {
            manager.scrollToPositionWithOffset(value[0], 0);
        }
    }

    /**
     * @param list Sort list
     * @return letters
     */
    public List<String> sortDatas(List<SortBean> list) {
        mLetterMap = new LinkedHashMap<>();
        if (list == null || list.size() <= 0) {
            return new ArrayList<>();
        }
        int count = list.size();
        for (int i = 0; i < count; i++) {
            SortBean bean = list.get(i);
            String pinyin = Pinyin.toPinyin(bean.content, "");
            String letter = TextUtils.isEmpty(pinyin) ? "" : pinyin.substring(0, 1).toUpperCase();
            // 正则表达式，判断首字母是否是英文字母/数字/其他
            if (letter.matches("[0-9]")) {
                letter = "☆";
            } else if (!letter.matches("[A-Z]")) {
                letter = "#";
            }
            bean.pinyin = pinyin;
            bean.letter = letter;
            bean.isLetter = false;
        }
        Collections.sort(list, new PinyinComparator());
        String key = null;
        int[] value = null; // is[0]:thisSectionPosition, is[1]:nextSectionPosition
        for (int i = 0; i < count; i++) {
            SortBean b = list.get(i);
            if (!TextUtils.equals(key, b.letter)) {
                key = b.letter;
                b.isLetter = true;
                if (value != null) {
                    value[1] = i;
                }
                value = new int[]{i, -1};
                mLetterMap.put(key, value);
            }
        }
        mDatas = list;
        return getLetters(mLetterMap);
    }

    private List<String> getLetters(Map<String, int[]> letterMap) {
        if (letterMap == null) {
            return new ArrayList<>();
        }
        List<String> letters = new ArrayList<>();
        for (Map.Entry<String, int[]> entry : letterMap.entrySet()) {
            letters.add(entry.getKey());
        }
        return letters;
    }
}
