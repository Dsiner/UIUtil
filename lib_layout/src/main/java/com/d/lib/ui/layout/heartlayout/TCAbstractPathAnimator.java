/*
 * Copyright (C) 2015 tyrantgit
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.d.lib.ui.layout.heartlayout;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Path;
import android.view.View;
import android.view.ViewGroup;

import com.d.lib.ui.layout.R;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 飘心路径
 */
public abstract class TCAbstractPathAnimator {
    protected final Config mConfig;
    private final Random mRandom;

    public TCAbstractPathAnimator(Config config) {
        mConfig = config;
        mRandom = new Random();
    }

    public float randomRotation() {
        return mRandom.nextFloat() * 28.6F - 14.3F;
    }

    public Path createPath(AtomicInteger counter, View view, int factor) {
        Random r = mRandom;
        int x = r.nextInt(mConfig.xRand);
        int x2 = r.nextInt(mConfig.xRand);
        int y = view.getHeight() - mConfig.initY;
        int y2 = counter.intValue() * 15 + mConfig.animLength * factor + r.nextInt(mConfig.animLengthRand);
        factor = y2 / mConfig.bezierFactor;
        x = mConfig.xPointFactor + x;
        x2 = mConfig.xPointFactor + x2;
        int y3 = y - y2;
        y2 = y - y2 / 2;
        Path p = new Path();
        p.moveTo(mConfig.initX, y);
        p.cubicTo(mConfig.initX, y - factor, x, y2 + factor, x, y2);
        p.moveTo(x, y2);
        p.cubicTo(x, y2 - factor, x2, y3 + factor, x2, y3);
        return p;
    }

    public abstract void start(View child, ViewGroup parent);

    public static class Config {
        public int initX;
        public int initY;
        public int xRand;
        public int animLengthRand;
        public int bezierFactor;
        public int xPointFactor;
        public int animLength;
        public int heartWidth;
        public int heartHeight;
        public int animDuration;

        static public Config fromTypeArray(TypedArray typedArray, float x, float y, int pointx, int heartWidth, int heartHeight) {
            Config config = new Config();
            Resources res = typedArray.getResources();
            config.initX = (int) typedArray.getDimension(R.styleable.lib_ui_layout_HeartLayout_lib_ui_layout_hl_initX, x);
            config.initY = (int) typedArray.getDimension(R.styleable.lib_ui_layout_HeartLayout_lib_ui_layout_hl_initY, y);
            config.xRand = (int) typedArray.getDimension(R.styleable.lib_ui_layout_HeartLayout_lib_ui_layout_hl_xRand, 100);
            config.animLength = (int) typedArray.getDimension(R.styleable.lib_ui_layout_HeartLayout_lib_ui_layout_hl_animLength, 200);//动画长度
            config.animLengthRand = (int) typedArray.getDimension(R.styleable.lib_ui_layout_HeartLayout_lib_ui_layout_hl_animLengthRand, 700);
            config.bezierFactor = typedArray.getInteger(R.styleable.lib_ui_layout_HeartLayout_lib_ui_layout_hl_bezierFactor, 6);
            config.xPointFactor = pointx;
//            config.heartWidth = (int) typedArray.getDimension(R.styleable.HeartLayout_heart_width,
//                    res.getDimensionPixelOffset(R.dimen.heart_size_width));//动画图片宽度
//            config.heartHeight = (int) typedArray.getDimension(R.styleable.HeartLayout_heart_height,
//                    res.getDimensionPixelOffset(R.dimen.heart_size_height));//动画图片高度
            config.heartWidth = heartWidth;
            config.heartHeight = heartHeight;
            config.animDuration = typedArray.getInteger(R.styleable.lib_ui_layout_HeartLayout_lib_ui_layout_hl_animDuration, 1000);//持续期
            return config;
        }
    }
}

