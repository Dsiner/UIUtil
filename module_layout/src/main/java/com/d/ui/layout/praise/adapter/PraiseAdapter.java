package com.d.ui.layout.praise.adapter;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.d.lib.common.util.ViewHelper;
import com.d.lib.pulllayout.rv.adapter.CommonAdapter;
import com.d.lib.pulllayout.rv.adapter.CommonHolder;
import com.d.ui.layout.R;

import java.util.List;

/**
 * 点赞榜单
 * Created by D on 2017/10/31.
 */
public class PraiseAdapter extends CommonAdapter<PraiseAdapter.Bean> {
    private RecyclerView mRecyclerView;
    private int mScrollState; // 状态
    private boolean mIsDelay; // 延迟状态
    private boolean mHasPraiseQueue; // 是否有被阻塞的点赞，待刷新
    private int mPosLiving; // 当前正在直播，在榜单中的位置
    private boolean mIsAs; // 放大动画标志位
    private boolean mIsAsAnimation; // 是否正在执行放大动画
    private Handler mHandler;
    private Runnable mRunnable;
    private View mAnimView;
    private ValueAnimator mAnim;

    public PraiseAdapter(Context context, List<Bean> datas, int layoutId, RecyclerView recyclerView) {
        super(context, datas, layoutId);
        this.mRecyclerView = recyclerView;
        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                mIsDelay = false;
                if (mScrollState != RecyclerView.SCROLL_STATE_IDLE) {
                    // 正在滑动直接返回
                    return;
                }
                scrollAdjust();
                if (mHasPraiseQueue) {
                    notifyDataSetChanged();
                    mHasPraiseQueue = false;
                }
            }
        };
    }

    /**
     * 滑动到当前直播
     */
    private void scrollAdjust() {
        final int firstPos = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        if (mPosLiving >= 0 && mPosLiving < firstPos) {
            mRecyclerView.smoothScrollToPosition(mPosLiving);
        }
    }

    /**
     * 设置手指滑动状态
     */
    public void setScrollState(int state) {
        mScrollState = state;
        if (mScrollState == RecyclerView.SCROLL_STATE_IDLE) {
            if (mHasPraiseQueue) {
                notifyDataSetChanged();
                mHasPraiseQueue = false;
            }
        }
    }

    /**
     * 是否静止态，没有手指触发 - 按压|滑动
     *
     * @return true: 静止态, false: 手指触发未静止
     */
    public boolean isStateIdle() {
        return mScrollState == RecyclerView.SCROLL_STATE_IDLE;
    }

    public int getPosLiving() {
        return mPosLiving;
    }

    public void setPosLiving(int position) {
        if (position < 0 || position >= mDatas.size()) {
            return;
        }
        mPosLiving = position;
    }

    /**
     * 点赞
     */
    public void doPraise() {
        if (mDatas.size() <= 0 || mPosLiving < 0 || mPosLiving >= mDatas.size()) {
            return;
        }
        mDatas.get(mPosLiving).count++;
        int fromPosition = mPosLiving;
        int toPosition = mPosLiving;
        for (int i = fromPosition; i > 0; i--) {
            if (mDatas.get(fromPosition).count > mDatas.get(i - 1).count) {
                toPosition = i - 1;
            } else {
                break;
            }
        }
        if (toPosition < fromPosition) {
            Bean fromModel = mDatas.get(fromPosition);
            mDatas.remove(fromPosition);
            mDatas.add(toPosition, fromModel);
            mPosLiving = toPosition;
            if (mScrollState != RecyclerView.SCROLL_STATE_IDLE || mIsDelay) {
                // 手指触发的滑动，未结束时，禁止刷新
                mHasPraiseQueue = true;
            } else {
                mHasPraiseQueue = false;
                notifyItemMoved(fromPosition, toPosition);
                notifyItemChanged(toPosition);
                mIsDelay = true;
                mHandler.postDelayed(mRunnable, 700);
            }
        } else {
            mIsAs = true;
            if (mScrollState != RecyclerView.SCROLL_STATE_IDLE || mIsDelay) {
                // 手指触发的滑动，未结束时，禁止刷新
                mHasPraiseQueue = true;
            } else {
                mHasPraiseQueue = false;
                notifyItemChanged(mPosLiving);
            }
        }
    }

    /**
     * 点赞 - 一次性加上
     *
     * @param count  点赞数
     * @param notify 是否刷新当前新增数
     */
    public void doPraiseAll(long count, boolean notify) {
        if (mDatas == null || mDatas.size() <= 0
                || mPosLiving < 0 || mPosLiving >= mDatas.size()) {
            return;
        }
        mDatas.get(mPosLiving).count += count;
        if (notify) {
            notifyItemChanged(mPosLiving);
        }
    }

    private void initAnim() {
        mAnim = ValueAnimator.ofFloat(1f, 1.2f);
        mAnim.setTarget(mAnimView);
        mAnim.setDuration(150);
        mAnim.setInterpolator(new LinearInterpolator());
        mAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimView.setScaleX((float) animation.getAnimatedValue());
                mAnimView.setScaleY((float) animation.getAnimatedValue());
            }
        });
        mAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (mAnimView != null) {
                    mAnimView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mIsAs = false;
                mIsAsAnimation = false;
                if (mAnimView != null) {
                    mAnimView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    /**
     * 获取格式化后的点赞数
     */
    private String getValueEX(long value) {
        if (value > 9999999) {
            long w = value / 10000;
            if (value % 10000 >= 5000) {
                w++;
            }
            return w + "w";
        } else {
            return "" + value;
        }
    }

    /**
     * 放大动画
     */
    private void startScaleAnim() {
        if (mIsAsAnimation) {
            return;
        }
        mIsAsAnimation = true;
        mAnim.start();
    }

    @Override
    public void convert(int position, CommonHolder holder, Bean item) {
        String count = getValueEX(item.count);
        ViewHelper.autoSize((TextView) holder.getView(R.id.tv_praise), count, 35, 6, 9);
        if (mIsAs && position == mPosLiving) {
            // 执行放大动画
            ViewHelper.autoSize((TextView) holder.getView(R.id.tv_praise_layer), count, 35, 6, 9);
            mAnimView = holder.getView(R.id.llyt_praise_layer);
            if (mAnim == null) {
                initAnim();
            }
            startScaleAnim();
        } else if (position != mPosLiving) {
            holder.setVisibility(R.id.llyt_praise_layer, View.GONE);
        }
    }

    public static class Bean {
        public long userId;
        public String name;
        public long count;
    }
}
