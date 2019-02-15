package com.d.lib.ui.view.lrc;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * DefaultLrcParser
 * Edited by D on 2017/5/16.
 */
public class DefaultLrcParser {

    private DefaultLrcParser() {
    }

    /**
     * 将歌词文件里面的字符串 解析成一个List<LrcRow>
     */
    @NonNull
    public static List<LrcRow> getLrcRows(File file) {
        return getLrcRows(converfile(file, "utf-8"));
    }

    @NonNull
    public static List<LrcRow> getLrcRows(String content) {
        if (TextUtils.isEmpty(content)) {
            return new ArrayList<>();
        }
        List<LrcRow> lrcRows = new ArrayList<>();
        BufferedReader br = new BufferedReader(new StringReader(content));
        String lrcLine;
        try {
            while ((lrcLine = br.readLine()) != null) {
                List<LrcRow> rows = LrcRow.createRows(lrcLine);
                if (rows != null && rows.size() > 0) {
                    lrcRows.addAll(rows);
                }
            }
            final int size = lrcRows.size();
            if (size > 0) {
                Collections.sort(lrcRows);
                for (int i = 0; i < size - 1; i++) {
                    LrcRow l = lrcRows.get(i);
                    l.setTotalTime(lrcRows.get(i + 1).getTime() - l.getTime());
                }
                lrcRows.get(size - 1).setTotalTime(5000);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return lrcRows;
    }

    private static String converfile(File file) {
        return converfile(file, null);
    }

    private static String converfile(File file, String charsetName) {
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        BufferedReader reader;
        String text = null;
        try {
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            bis.mark(4);
            byte[] first3bytes = new byte[3];
            // 找到文档的前三个字节并自动判断文档类型
            bis.read(first3bytes);
            bis.reset();
            if (TextUtils.isEmpty(charsetName)) {
                charsetName = getCharset(first3bytes);
            }
            reader = new BufferedReader(new InputStreamReader(bis, charsetName));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            text = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return text;
    }

    @NonNull
    private static String getCharset(byte[] first3bytes) {
        String charsetName;
        if (first3bytes[0] == (byte) 0xEF && first3bytes[1] == (byte) 0xBB && first3bytes[2] == (byte) 0xBF) {
            charsetName = "utf-8";
        } else if (first3bytes[0] == (byte) 0xFF && first3bytes[1] == (byte) 0xFE) {
            charsetName = "unicode";
        } else if (first3bytes[0] == (byte) 0xFE && first3bytes[1] == (byte) 0xFF) {
            charsetName = "utf-16be";
        } else if (first3bytes[0] == (byte) 0xFF && first3bytes[1] == (byte) 0xFF) {
            charsetName = "utf-16le";
        } else {
            charsetName = "GBK";
        }
        return charsetName;
    }
}