package com.d.uiutil.lrc;

import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * DefaultLrcParser
 * Edited by D on 2017/5/16.
 */
public class DefaultLrcParser implements ILrcParser {
    private static final DefaultLrcParser instance = new DefaultLrcParser();

    public static DefaultLrcParser getInstance() {
        return instance;
    }

    private DefaultLrcParser() {
    }

    /***
     * 将歌词文件里面的字符串 解析成一个List<LrcRow>
     */
    @Override
    public List<LrcRow> getLrcRows(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        BufferedReader br = new BufferedReader(new StringReader(str));
        List<LrcRow> lrcRows = new ArrayList<>();
        String lrcLine;
        try {
            while ((lrcLine = br.readLine()) != null) {
                List<LrcRow> rows = LrcRow.createRows(lrcLine);
                if (rows != null && rows.size() > 0) {
                    lrcRows.addAll(rows);
                }
            }
            Collections.sort(lrcRows);
            for (int i = 0; i < lrcRows.size() - 1; i++) {
                lrcRows.get(i).setTotalTime(lrcRows.get(i + 1).getTime() - lrcRows.get(i).getTime());
            }
            lrcRows.get(lrcRows.size() - 1).setTotalTime(5000);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return lrcRows;
    }
}