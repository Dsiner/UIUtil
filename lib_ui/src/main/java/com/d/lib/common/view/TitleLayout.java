package com.d.lib.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.d.lib.common.R;

/**
 * TitleLayout
 * Created by D on 2017/5/3.
 */
public class TitleLayout extends RelativeLayout {
    protected Context mContext;
    protected View mRootView;
    protected final int mMenuRes;
    private final String[] mTexts = new String[3];
    private final Drawable[] mDrawables = new Drawable[3];
    private final int[] mResIds = new int[3];

    public TitleLayout(Context context) {
        this(context, null);
    }

    public TitleLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.lib_pub_TitleLayout);
        mTexts[0] = typedArray.getString(R.styleable.lib_pub_TitleLayout_lib_pub_tl_leftText);
        mTexts[1] = typedArray.getString(R.styleable.lib_pub_TitleLayout_lib_pub_tl_rightText);
        mTexts[2] = typedArray.getString(R.styleable.lib_pub_TitleLayout_lib_pub_tl_middleText);

        mDrawables[0] = typedArray.getDrawable(R.styleable.lib_pub_TitleLayout_lib_pub_tl_leftDrawable);
        mDrawables[1] = typedArray.getDrawable(R.styleable.lib_pub_TitleLayout_lib_pub_tl_rightDrawable);
        mDrawables[2] = typedArray.getDrawable(R.styleable.lib_pub_TitleLayout_lib_pub_tl_middleDrawable);

        mResIds[0] = typedArray.getResourceId(R.styleable.lib_pub_TitleLayout_lib_pub_tl_leftRes, -1);
        mResIds[1] = typedArray.getResourceId(R.styleable.lib_pub_TitleLayout_lib_pub_tl_rightRes, -1);
        mResIds[2] = typedArray.getResourceId(R.styleable.lib_pub_TitleLayout_lib_pub_tl_middleRes, -1);

        mMenuRes = typedArray.getResourceId(R.styleable.lib_pub_TitleLayout_lib_pub_tl_menu, -1);
        typedArray.recycle();
        init(context);
    }

    protected void init(Context context) {
        this.mContext = context;
        mRootView = LayoutInflater.from(context).inflate(R.layout.lib_pub_layout_title, this);

        // Left
        inflate(context, mRootView, mTexts[0], mDrawables[0], mResIds[0],
                R.id.tv_title_left, R.id.iv_title_left, ALIGN_PARENT_LEFT);
        // Right
        inflate(context, mRootView, mTexts[1], mDrawables[1], mResIds[1],
                R.id.tv_title_right, R.id.iv_title_right, ALIGN_PARENT_RIGHT);
        // Middle
        inflate(context, mRootView, mTexts[2], mDrawables[2], mResIds[2],
                R.id.tv_title_title, R.id.iv_title_middle, CENTER_IN_PARENT);
    }

    private void inflate(Context context, View root, String text, Drawable drawable, int res,
                         int tv_id, int iv_id, int verb) {
        if (!TextUtils.isEmpty(text)) {
            TextView tv = (TextView) root.findViewById(tv_id);
            if (tv != null) {
                tv.setText(text);
                tv.setVisibility(VISIBLE);
            }
        } else if (drawable != null) {
            ImageView iv = (ImageView) root.findViewById(iv_id);
            if (iv != null) {
                iv.setImageDrawable(drawable);
                iv.setVisibility(VISIBLE);
            }
        } else if (res != -1) {
            View view = LayoutInflater.from(context).inflate(res, this, false);
            addView(view);
            LayoutParams lp = (LayoutParams) view.getLayoutParams();
            lp.addRule(verb);
            lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
            lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            view.setLayoutParams(lp);
        }
    }

    public void setVisibility(@IdRes int resId, int visibility) {
        View v = findViewById(resId);
        if (v != null) {
            v.setVisibility(visibility);
        }
    }

    public void setText(@IdRes int resId, CharSequence text) {
        View v = findViewById(resId);
        if (v != null && v instanceof TextView) {
            ((TextView) v).setText(text);
        }
    }

    public void setOnClickListener(@IdRes int resId, final OnClickListener l) {
        View v = findViewById(resId);
        if (v != null) {
            v.setOnClickListener(l);
        }
    }
}
