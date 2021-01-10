package com.d.lib.ui.layout.arcmenu;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.d.lib.ui.layout.R;

public class ArcMenu extends ViewGroup implements OnClickListener {
    private static final int POS_LEFT_TOP = 0;
    private static final int POS_LEFT_BOTTOM = 1;
    private static final int POS_RIGHT_TOP = 2;
    private static final int POS_RIGHT_BOTTOM = 3;

    private Position mPosition = Position.RIGHT_BOTTOM;
    private int mRadius;

    /**
     * 菜单的状态
     */
    private Status mCurrentStatus = Status.CLOSE;

    /**
     * 菜单的主按钮
     */
    private View mCButton;

    private OnMenuItemClickListener mMenuItemClickListener;
    private int mCount;

    public ArcMenu(Context context) {
        this(context, null);
    }

    public ArcMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // 获取自定义属性的值
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.lib_ui_layout_ArcMenu, defStyle, 0);
        int pos = a.getInt(R.styleable.lib_ui_layout_ArcMenu_lib_ui_layout_am_position, POS_RIGHT_BOTTOM);
        switch (pos) {
            case POS_LEFT_TOP:
                mPosition = Position.LEFT_TOP;
                break;
            case POS_LEFT_BOTTOM:
                mPosition = Position.LEFT_BOTTOM;
                break;
            case POS_RIGHT_TOP:
                mPosition = Position.RIGHT_TOP;
                break;
            case POS_RIGHT_BOTTOM:
                mPosition = Position.RIGHT_BOTTOM;
                break;
        }
        mRadius = (int) a.getDimension(R.styleable.lib_ui_layout_ArcMenu_lib_ui_layout_am_radius, TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics()));
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mCount = getChildCount();
        for (int i = 0; i < mCount; i++) {
            // 测量child
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            layoutCButton();
            for (int i = 0; i < mCount - 1; i++) {
                View child = getChildAt(i);
                child.setVisibility(View.GONE);
                int cl = (int) (mRadius * Math.sin(Math.PI / 2 / (mCount - 2) * i));
                int ct = (int) (mRadius * Math.cos(Math.PI / 2 / (mCount - 2) * i));
                int cWidth = child.getMeasuredWidth();
                int cHeight = child.getMeasuredHeight();
                // 如果菜单位置在底部 左下，右下
                if (mPosition == Position.LEFT_BOTTOM || mPosition == Position.RIGHT_BOTTOM) {
                    ct = getMeasuredHeight() - cHeight - ct;
                }
                // 右上，右下
                if (mPosition == Position.RIGHT_TOP || mPosition == Position.RIGHT_BOTTOM) {
                    cl = getMeasuredWidth() - cWidth - cl;
                }
                child.layout(cl, ct, cl + cWidth, ct + cHeight);
            }
        }
    }

    /**
     * 定位主菜单按钮
     */
    private void layoutCButton() {
        mCButton = getChildAt(mCount - 1);
        mCButton.setOnClickListener(this);
        int l = 0;
        int t = 0;
        int width = mCButton.getMeasuredWidth();
        int height = mCButton.getMeasuredHeight();
        switch (mPosition) {
            case LEFT_TOP:
                l = 0;
                t = 0;
                break;
            case LEFT_BOTTOM:
                l = 0;
                t = getMeasuredHeight() - height;
                break;
            case RIGHT_TOP:
                l = getMeasuredWidth() - width;
                t = 0;
                break;
            case RIGHT_BOTTOM:
                l = getMeasuredWidth() - width;
                t = getMeasuredHeight() - height;
                break;
        }
        mCButton.layout(l, t, l + width, t + width);
    }

    @Override
    public void onClick(View v) {
        v.startAnimation(getRotateAnimation(0f, 360f, 300));
        toggleMenu(300);
    }

    /**
     * 切换菜单
     */
    public void toggleMenu(int duration) {
        // 为menuItem添加平移动画和旋转动画
        for (int i = 0; i < mCount - 1; i++) {
            final View childView = getChildAt(i);
            childView.setVisibility(View.VISIBLE);

            // End 0 , 0
            // Start
            int cl = (int) (mRadius * Math.sin(Math.PI / 2 / (mCount - 2) * i));
            int ct = (int) (mRadius * Math.cos(Math.PI / 2 / (mCount - 2) * i));

            float xflag = 1;
            float yflag = 1;

            if (mPosition == Position.LEFT_TOP || mPosition == Position.LEFT_BOTTOM) {
                xflag = -1;
            }

            if (mPosition == Position.LEFT_TOP || mPosition == Position.RIGHT_TOP) {
                yflag = -1;
            }

            // 可选
//            xflag *= 0.96;
//            yflag *= 0.96;

            AnimationSet animset = new AnimationSet(true);
            Animation tranAnim;

            if (mCurrentStatus == Status.CLOSE) {
                // To open
                tranAnim = new TranslateAnimation(xflag * cl, 0, yflag * ct, 0);
                childView.setClickable(true);
                childView.setFocusable(true);
            } else {
                // To close
                tranAnim = new TranslateAnimation(0, xflag * cl, 0, yflag * ct);
                childView.setClickable(false);
                childView.setFocusable(false);
            }
            tranAnim.setFillAfter(true);
            tranAnim.setDuration(duration);
            // 可选，延迟不规则弹出
//            tranAnim.setStartOffset((i * 100) / mCount);

            tranAnim.setAnimationListener(new AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (mCurrentStatus == Status.CLOSE) {
                        childView.setVisibility(View.GONE);
                    }
                }
            });
            // 旋转动画
//            animset.addAnimation(getRotateAnimation(0, 720, duration));

            animset.addAnimation(tranAnim);
            childView.startAnimation(animset);

            final int pos = i;
            childView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mMenuItemClickListener != null) {
                        mMenuItemClickListener.onClick(childView, pos);
                    }
                    menuItemAnim(pos);
                    changeStatus();
                }
            });
        }
        // 切换菜单状态
        changeStatus();
    }

    /**
     * 添加menuItem的点击动画
     */
    private void menuItemAnim(int pos) {
        for (int i = 0; i < mCount - 1; i++) {
            View childView = getChildAt(i);
            if (i == pos) {
                childView.startAnimation(getScaleAnim(true, 300));
            } else {
                childView.startAnimation(getScaleAnim(false, 300));
            }
            childView.setClickable(false);
            childView.setFocusable(false);
        }
    }

    /**
     * 为当前点击的Item设置变大和透明度降低的动画
     *
     * @param type     true: 放大; false: 缩小
     * @param duration 延时
     * @return animationSet
     */
    private Animation getScaleAnim(boolean type, int duration) {
        AnimationSet animationSet = new AnimationSet(true);
        ScaleAnimation scaleAnim;
        if (type) {
            scaleAnim = new ScaleAnimation(1.0f, 4.0f, 1.0f, 4.0f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        } else {
            scaleAnim = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        }
        AlphaAnimation alphaAnim = new AlphaAnimation(1f, 0.0f);
        animationSet.addAnimation(scaleAnim);
        animationSet.addAnimation(alphaAnim);
        animationSet.setDuration(duration);
        animationSet.setFillAfter(true);
        return animationSet;
    }

    /**
     * 切换菜单状态
     */
    private void changeStatus() {
        mCurrentStatus = (mCurrentStatus == Status.CLOSE ? Status.OPEN : Status.CLOSE);
    }

    public boolean isOpen() {
        return mCurrentStatus == Status.OPEN;
    }

    private RotateAnimation getRotateAnimation(float start, float end, int duration) {
        RotateAnimation anim = new RotateAnimation(start, end, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(duration);
        anim.setFillAfter(true);
        return anim;
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener mMenuItemClickListener) {
        this.mMenuItemClickListener = mMenuItemClickListener;
    }

    public enum Status {
        OPEN, CLOSE
    }

    /**
     * 菜单的位置枚举类
     */
    public enum Position {
        LEFT_TOP, LEFT_BOTTOM, RIGHT_TOP, RIGHT_BOTTOM
    }

    /**
     * 点击子菜单项的回调接口
     */
    public interface OnMenuItemClickListener {
        void onClick(View view, int pos);
    }
}
