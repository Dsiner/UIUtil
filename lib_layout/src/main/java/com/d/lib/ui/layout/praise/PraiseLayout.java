package com.d.lib.ui.layout.praise;


import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.d.lib.ui.common.Util;
import com.d.lib.ui.layout.R;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;

import java.util.Random;

/**
 * Praise
 * Created by D on 2016/11/8.
 */
public class PraiseLayout extends RelativeLayout {

    private IPraise iPraise;
    private int mHeight;
    private int mWidth;
    private int dHeight; // 图片高度
    private int dWidth; // 图片宽度
    private int marginLeft;
    private int marginBottom;
    private Drawable heart;
    private RelativeLayout.LayoutParams lp;
    private Interpolator line = new LinearInterpolator(); // 线性
    private Random random = new Random();
    private int endPX;

    public PraiseLayout(Context context) {
        super(context);
        init();
    }

    public PraiseLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PraiseLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        heart = ContextCompat.getDrawable(getContext(), R.drawable.lib_ui_layout_pl_ic_heart);
        dHeight = Util.dip2px(getContext(), 15);
        dWidth = Util.dip2px(getContext(), 15);
        setMarginLeft(true);
        lp = new RelativeLayout.LayoutParams(dWidth, dHeight);
        lp.addRule(ALIGN_PARENT_BOTTOM, TRUE);
        lp.setMargins(marginLeft, 0, 0, marginBottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }


    public void addHeart(String usrId) {
        ImageView imageView = new ImageView(getContext());
        imageView.setImageDrawable(heart);
        imageView.setLayoutParams(lp);
        imageView.setTag(usrId);
        addView(imageView);
        Animator set = getAnimator(imageView);
        set.addListener(new AnimEndListener(imageView));
        set.start();
    }

    private Animator getAnimator(View target) {
        ValueAnimator bezierValueAnimator = getBezierValueAnimator(target);
        AnimatorSet finalSet = new AnimatorSet();
        finalSet.playSequentially(bezierValueAnimator);
        finalSet.setInterpolator(line);
        finalSet.setTarget(target);
        return finalSet;
    }

    /**
     * 获取贝塞尔曲线动画
     *
     * @param target 执行动画的目标View
     * @return animator
     */
    private ValueAnimator getBezierValueAnimator(View target) {
        BezierEvaluator evaluator = new BezierEvaluator(getPointF(2), getPointF(1));
        ValueAnimator animator = ValueAnimator.ofObject(evaluator, new PointF(marginLeft, mHeight - marginBottom - dHeight), new PointF(endPX, -dHeight));
        animator.addUpdateListener(new BezierListener(target));
        animator.setTarget(target);
        animator.setDuration(1500);
        return animator;
    }

    /**
     * 获取中间的两个随机点
     *
     * @param scale 范围约束
     */
    private PointF getPointF(int scale) {
        PointF pointF = new PointF();
        pointF.x = random.nextInt(mWidth);
        pointF.y = random.nextInt(mHeight) / scale;
        return pointF;
    }

    /**
     * 设置心形距屏幕左边距
     *
     * @param isPortrait 是否竖屏
     */
    public void setMarginLeft(boolean isPortrait) {
        if (isPortrait) {
            // 竖屏
            marginLeft = Util.dip2px(getContext(), 275);
            marginBottom = Util.dip2px(getContext(), 22);
        } else {
            // 横屏
            marginLeft = Util.dip2px(getContext(), 275 + 14);
            marginBottom = Util.dip2px(getContext(), 25);
        }
    }

    /**
     * 设置终点
     *
     * @param endPX 终点横坐标
     */
    public void setEndP(int endPX) {
        this.endPX = endPX;
    }

    /**
     * 清空所有View及动画
     */
    public void clearAllAnimators() {
        int size = getChildCount();
        for (int i = 0; i < size; i++) {
            View v = getChildAt(i);
            if (v != null) {
                v.setTag(null);
            }
        }
        removeAllViews();
    }

    public void setIPraise(IPraise iPraise) {
        this.iPraise = iPraise;
    }

    private class BezierListener implements ValueAnimator.AnimatorUpdateListener {

        private View target;

        BezierListener(View target) {
            this.target = target;
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            PointF pointF = (PointF) animation.getAnimatedValue();
            ViewHelper.setX(target, pointF.x);
            ViewHelper.setY(target, pointF.y);
            ViewHelper.setAlpha(target, 1 - animation.getAnimatedFraction());
        }
    }


    private class AnimEndListener extends AnimatorListenerAdapter {
        private View target;

        AnimEndListener(View target) {
            this.target = target;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            if (target != null && target.getTag() != null && iPraise != null) {
                iPraise.onAnimationEnd();
            }
            removeView(target);
        }
    }
}
