package com.d.uiutil.praise.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.d.lib.ui.common.UIUtil;
import com.d.lib.xrv.adapter.CommonAdapter;
import com.d.lib.xrv.adapter.CommonHolder;
import com.d.uiutil.R;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;

import java.util.List;

/**
 * 点赞榜单
 * Created by D on 2017/10/31.
 */
public class PraiseAdapter extends CommonAdapter<PraiseAdapter.Bean> {
    private RecyclerView recyclerView;
    private int scrollState;//状态
    private boolean isDelay;//延迟状态
    private boolean hasPraiseQueue;//是否有被阻塞的点赞，待刷新
    private int posLiving;//当前正在直播，在榜单中的位置
    private boolean isAs;//放大动画标志位
    private boolean isAsAnimation;//是否正在执行放大动画
    private Handler handler;
    private Runnable runnable;
    private View animView;
    private ValueAnimator anim;

    public PraiseAdapter(Context context, List<Bean> datas, int layoutId, RecyclerView recyclerView) {
        super(context, datas, layoutId);
        this.recyclerView = recyclerView;
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                isDelay = false;
                if (scrollState != RecyclerView.SCROLL_STATE_IDLE) {
                    //正在滑动直接返回
                    return;
                }
                scroollAdjust();
                if (hasPraiseQueue) {
                    notifyDataSetChanged();
                    hasPraiseQueue = false;
                }
            }
        };
    }

    /**
     * 滑动到当前直播
     */
    public void scroollAdjust() {
        int firstPos = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        if (posLiving >= 0 && posLiving < firstPos) {
            recyclerView.smoothScrollToPosition(posLiving);
        }
    }

    /**
     * 设置手指滑动状态
     */
    public void setScrollState(int state) {
        scrollState = state;
        if (scrollState == RecyclerView.SCROLL_STATE_IDLE) {
            if (hasPraiseQueue) {
                notifyDataSetChanged();
                hasPraiseQueue = false;
            }
        }
    }

    /**
     * 是否静止态，没有手指触发-按压|滑动
     *
     * @return true:静止态 false:手指触发未静止
     */
    public boolean isStateIdle() {
        return scrollState == RecyclerView.SCROLL_STATE_IDLE;
    }

    public void setPosLiving(int position) {
        if (position < 0 || position >= mDatas.size()) {
            return;
        }
        posLiving = position;
    }

    public int getPosLiving() {
        return posLiving;
    }

    /**
     * 点赞
     */
    public void doPraise() {
        if (mDatas.size() <= 0 || posLiving < 0 || posLiving >= mDatas.size()) {
            return;
        }
        mDatas.get(posLiving).count++;
        int fromPosition = posLiving;
        int toPosition = posLiving;
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
            posLiving = toPosition;
            if (scrollState != RecyclerView.SCROLL_STATE_IDLE || isDelay) {
                //手指触发的滑动，未结束时，禁止刷新
                hasPraiseQueue = true;
            } else {
                hasPraiseQueue = false;
                notifyItemMoved(fromPosition, toPosition);
                notifyItemChanged(toPosition);
                isDelay = true;
                handler.postDelayed(runnable, 700);
            }
        } else {
            isAs = true;
            if (scrollState != RecyclerView.SCROLL_STATE_IDLE || isDelay) {
                //手指触发的滑动，未结束时，禁止刷新
                hasPraiseQueue = true;
            } else {
                hasPraiseQueue = false;
                notifyItemChanged(posLiving);
            }
        }
    }

    /**
     * 点赞-一次性加上
     *
     * @param count:点赞数
     * @param notify:是否刷新当前新增数
     */
    public void doPraiseAll(long count, boolean notify) {
        if (mDatas == null || mDatas.size() <= 0 || posLiving < 0 || posLiving >= mDatas.size()) {
            return;
        }
        mDatas.get(posLiving).count += count;
        if (notify) {
            notifyItemChanged(posLiving);
        }
    }

    private void initAnim() {
        anim = ValueAnimator.ofFloat(1f, 1.2f);
        anim.setTarget(animView);
        anim.setDuration(150);
        anim.setInterpolator(new LinearInterpolator());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ViewHelper.setScaleX(animView, (float) animation.getAnimatedValue());
                ViewHelper.setScaleY(animView, (float) animation.getAnimatedValue());
            }
        });
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (animView != null) {
                    animView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAs = false;
                isAsAnimation = false;
                if (animView != null) {
                    animView.setVisibility(View.GONE);
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
    private void aS() {
        if (isAsAnimation) {
            return;
        }
        isAsAnimation = true;
        anim.start();
    }

    @Override
    public void convert(int position, CommonHolder holder, Bean item) {
        String count = getValueEX(item.count);
        UIUtil.autoSize((TextView) holder.getView(R.id.tv_praise), count, 35, 6, 9);
        if (isAs && position == posLiving) {
            //执行放大动画
            UIUtil.autoSize((TextView) holder.getView(R.id.tv_praise_layer), count, 35, 6, 9);
            animView = holder.getView(R.id.llyt_praise_layer);
            if (anim == null) {
                initAnim();
            }
            aS();
        } else if (position != posLiving) {
            holder.setViewVisibility(R.id.llyt_praise_layer, View.GONE);
        }
    }

    public static class Bean {
        public long usrId;
        public String name;
        public long count;
    }
}
