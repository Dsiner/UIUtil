package com.d.lib.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.d.lib.common.R;
import com.d.lib.common.utils.Util;
import com.d.lib.common.view.loading.LoadingLayout;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Default State
 * Created by D on 2017/5/7.
 */
public class DSLayout extends FrameLayout {

    /*************** Type ***************/
    public final static int STATE_LOADING = 0x10; // Default State: loading state
    public final static int STATE_EMPTY = 0x11; // Default State: no data
    public final static int STATE_NET_ERROR = 0x12; // Default State: network error

    /*************** Centered type ***************/
    private final static int CENT_TYPE_MAIN = 1;
    private final static int CENT_TYPE_LOCAL = 2;
    private final static float[] AJUST_HEIGHT = new float[]{0, 50, 70};

    @IntDef({STATE_LOADING, STATE_EMPTY, STATE_NET_ERROR,
            DSLayout.VISIBLE, DSLayout.INVISIBLE, DSLayout.GONE})
    @Target({ElementType.PARAMETER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {

    }

    private int mLayoutId;
    private int mCenterType; // Centered type
    private float mAdjustHeightT; // Correction height
    private float mAdjustHeightB; // Correction height
    private int mResIdEmpty, mResIdNetError;

    private LinearLayout mLlDsl;
    private ImageView mIvIcon;
    private TextView mTvDesc;
    private Button mButton;
    private LoadingLayout mLdlLoading;

    public DSLayout(Context context) {
        this(context, null);
    }

    public DSLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DSLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.lib_pub_DSLayout);
        mLayoutId = typedArray.getResourceId(R.styleable.lib_pub_DSLayout_lib_pub_dsl_layout, R.layout.lib_pub_layout_ds);
        mCenterType = typedArray.getInteger(R.styleable.lib_pub_DSLayout_lib_pub_dsl_ceterType, 0);
        mAdjustHeightT = typedArray.getDimension(R.styleable.lib_pub_DSLayout_lib_pub_dsl_adjustHeightT, 0);
        mAdjustHeightB = typedArray.getDimension(R.styleable.lib_pub_DSLayout_lib_pub_dsl_adjustHeightB, 0);
        mResIdEmpty = typedArray.getResourceId(R.styleable.lib_pub_DSLayout_lib_pub_dsl_emptyDrawable, R.drawable.lib_pub_ic_no_data);
        mResIdNetError = typedArray.getResourceId(R.styleable.lib_pub_DSLayout_lib_pub_dsl_netErroDrawable, R.drawable.lib_pub_ic_network_err);
        typedArray.recycle();
        init(context);
    }

    protected void init(Context context) {
        View root = LayoutInflater.from(context).inflate(mLayoutId, this);
        View vT = root.findViewById(R.id.v_dsl_t);
        View vB = root.findViewById(R.id.v_dsl_b);

        mLlDsl = (LinearLayout) root.findViewById(R.id.llyt_dsl);
        mIvIcon = (ImageView) root.findViewById(R.id.iv_dsl_icon);
        mTvDesc = (TextView) root.findViewById(R.id.tv_dsl_desc);
        mButton = (Button) root.findViewById(R.id.btn_dsl);

        mLdlLoading = (LoadingLayout) root.findViewById(R.id.ldl_loading);

        ViewGroup.LayoutParams paramsT = vT.getLayoutParams();
        ViewGroup.LayoutParams paramsB = vB.getLayoutParams();
        switch (mCenterType) {
            case CENT_TYPE_MAIN:
                paramsB.height = Util.dip2px(context, AJUST_HEIGHT[CENT_TYPE_MAIN]);
                break;
            case CENT_TYPE_LOCAL:
                paramsB.height = Util.dip2px(context, AJUST_HEIGHT[CENT_TYPE_LOCAL]);
                break;
            default:
                // Do nothing, default center 0/0
                break;
        }
        if (mAdjustHeightT != 0 || mAdjustHeightB != 0) {
            // Priority is greater than and overrides centerType
            paramsT.height = (int) mAdjustHeightT;
            paramsB.height = (int) mAdjustHeightB;
        }
        vT.setLayoutParams(paramsT); // Set the top correction height
        vB.setLayoutParams(paramsB); // Set bottom correction height
    }

    private void showLoading() {
        setVisibility(VISIBLE);
        mLdlLoading.setVisibility(VISIBLE);
        mLlDsl.setVisibility(GONE);
    }

    private void showEmpty() {
        setVisibility(VISIBLE);
        mLdlLoading.setVisibility(GONE);
        mLlDsl.setVisibility(VISIBLE);
        mIvIcon.setImageResource(mResIdEmpty);
        mTvDesc.setText(getResources().getString(R.string.lib_pub_no_data));
        mButton.setVisibility(GONE);
    }

    private void showNetError() {
        setVisibility(VISIBLE);
        mLdlLoading.setVisibility(GONE);
        mLlDsl.setVisibility(VISIBLE);
        mIvIcon.setImageResource(mResIdNetError);
        mTvDesc.setText(getResources().getString(R.string.lib_pub_net_error));
        mButton.setText(getResources().getString(R.string.lib_pub_retry));
        mButton.setVisibility(VISIBLE);
    }

    /**
     * Display image
     */
    public DSLayout icon(@DrawableRes int resId) {
        mIvIcon.setImageResource(resId);
        return this;
    }

    /**
     * Display image
     */
    public DSLayout icon(@Nullable Drawable drawable) {
        mIvIcon.setImageDrawable(drawable);
        return this;
    }

    /**
     * Display image
     */
    public DSLayout icon(String url) {
        Glide.with(getContext())
                .load(url)
                .apply(new RequestOptions().dontTransform().dontAnimate())
                .into(mIvIcon);
        return this;
    }

    /**
     * Display gif
     */
    public DSLayout gif(int resId) {
        Glide.with(getContext())
                .asGif()
                .load(resId)
                .into(mIvIcon);
        return this;
    }

    /**
     * Display gif
     */
    public DSLayout gif(String url) {
        Glide.with(getContext())
                .asGif()
                .load(url)
                .into(mIvIcon);
        return this;
    }

    /**
     * Set prompt text
     */
    public DSLayout desc(CharSequence text) {
        mTvDesc.setText(text);
        return this;
    }

    /**
     * Set button text, visibility state
     */
    public DSLayout button(CharSequence text, int visibility) {
        mButton.setText(text);
        mButton.setVisibility(visibility);
        return this;
    }

    /**
     * Set state
     */
    public DSLayout setState(@State int state) {
        switch (state) {
            case GONE:
            case VISIBLE:
            case INVISIBLE:
                setVisibility(state);
                break;
            case STATE_LOADING:
                showLoading();
                break;
            case STATE_EMPTY:
                showEmpty();
                break;
            case STATE_NET_ERROR:
                showNetError();
                break;
        }
        return this;
    }
}
