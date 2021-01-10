package com.d.lib.ui.view.lrc;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.d.lib.common.util.log.ULog;

import java.util.ArrayList;
import java.util.List;

/**
 * LrcRow
 * Edited by D on 2017/5/16.
 */
public class LrcRow implements Comparable<LrcRow> {

    /**
     * 开始时间 为00:10:00
     */
    private String timeStr;

    /**
     * 开始时间 毫米数  00:10:00  为10000
     */
    private int time;

    /**
     * 歌词内容
     */
    private String content;

    /**
     * 该行歌词显示的总时间
     */
    private int totalTime;

    public LrcRow() {
        super();
    }

    public LrcRow(String timeStr, int time, String content) {
        super();
        this.timeStr = timeStr;
        this.time = time;
        this.content = content;
    }

    /**
     * 将歌词文件中的某一行 解析成一个List<LrcRow>
     * 因为一行中可能包含了多个LrcRow对象
     * 比如  [03:33.02][00:36.37]当鸽子不再象征和平  ，就包含了2个对象
     */
    public static List<LrcRow> createRows(String lrcLine) {
        if (lrcLine == null || !lrcLine.startsWith("[") || lrcLine.indexOf("]") != 9) {
            return null;
        }
        // 最后一个"]"
        int lastIndexOfRightBracket = lrcLine.lastIndexOf("]");
        // 歌词内容
        String content = lrcLine.substring(lastIndexOfRightBracket + 1, lrcLine.length());
        // 截取出歌词时间，并将"[" 和"]" 替换为"-"   [offset:0]
        ULog.d("lrcLine=" + lrcLine);
        // -03:33.02--00:36.37-
        String times = lrcLine.substring(0, lastIndexOfRightBracket + 1).replace("[", "-").replace("]", "-");
        String[] timesArray = times.split("-");
        List<LrcRow> lrcRows = new ArrayList<>();
        for (String tem : timesArray) {
            if (TextUtils.isEmpty(tem.trim())) {
                continue;
            }
            try {
                LrcRow lrcRow = new LrcRow(tem, formatTime(tem), content);
                lrcRows.add(lrcRow);
            } catch (Exception e) {
                ULog.d("LrcRow" + e.getMessage());
            }
        }
        return lrcRows;
    }

    /**
     * 把歌词时间转换为毫秒值  如 将00:10.00  转为10000
     */
    private static int formatTime(String timeStr) {
        timeStr = timeStr.replace('.', ':');
        String[] times = timeStr.split(":");

        return Integer.parseInt(times[0]) * 60 * 1000
                + Integer.parseInt(times[1]) * 1000
                + Integer.parseInt(times[2]);
    }

    public String getTimeStr() {
        return timeStr;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    @Override
    public int compareTo(@NonNull LrcRow anotherLrcRow) {
        return this.time - anotherLrcRow.time;
    }

    @Override
    public String toString() {
        return "LrcRow [timeStr=" + timeStr + ", time=" + time + ", content=" + content + "]";
    }
}