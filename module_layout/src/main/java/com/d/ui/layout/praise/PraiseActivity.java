package com.d.ui.layout.praise;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.d.lib.common.util.DimenUtils;
import com.d.lib.common.util.ScreenUtils;
import com.d.lib.common.util.ViewHelper;
import com.d.lib.common.util.log.ULog;
import com.d.lib.ui.layout.praise.IPraise;
import com.d.lib.ui.layout.praise.PraiseLayout;
import com.d.ui.layout.R;
import com.d.ui.layout.praise.adapter.PraiseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by D on 2017/10/31.
 */
public class PraiseActivity extends Activity implements View.OnClickListener {

    private RecyclerView rv_list;
    private PraiseLayout pl_praise;
    private LinearLayoutManager mLinearLayoutManager;
    private PraiseAdapter mAdapter;
    private int mScreenWidth;
    private int mEndPosFirst;

    @Override
    public void onClick(View v) {
        int resId = v.getId();
        if (R.id.iv_back == resId) {
            finish();
        } else if (R.id.llyt_do_praise == resId) {
            int posLiving = mAdapter.getPosLiving();
            if (posLiving < 0 || posLiving >= mAdapter.getItemCount()) {
                return;
            }
            pl_praise.setEndP(getPosition(posLiving)); // 设置此次红心飘移的终点横坐标
            pl_praise.addHeart(""); // 开始一次点赞，动画开始
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_praise);
        bindView();
        mScreenWidth = ScreenUtils.getScreenSize(this)[0];
        mEndPosFirst = DimenUtils.dp2px(this, 64 / 2 - 15 / 2);
        initPraise();
    }

    private void bindView() {
        rv_list = ViewHelper.findViewById(this, R.id.rv_list);
        pl_praise = ViewHelper.findViewById(this, R.id.pl_praise);

        ViewHelper.setOnClickListener(this, this, R.id.iv_back, R.id.llyt_do_praise);
    }

    private void initPraise() {
        mAdapter = new PraiseAdapter(this, getDatas(), R.layout.adapter_praise, rv_list);
        mAdapter.setPosLiving(mAdapter.getItemCount() - 2);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_list.setHasFixedSize(true);
        rv_list.setLayoutManager(mLinearLayoutManager);
        rv_list.setItemAnimator(new DefaultItemAnimator());
        rv_list.setAdapter(mAdapter);
        rv_list.setItemAnimator(new DefaultItemAnimator());
        rv_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                /**
                 * newState
                 * 1: 手指拖动，开始滑动
                 * 2: 手指松开，自由惯性滑动
                 * 0: 滑动停止/惯性滑动停止
                 */
                mAdapter.setScrollState(newState);
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        pl_praise.setIPraise(new IPraise() {
            @Override
            public void onAnimationEnd() {
                mAdapter.doPraise();
            }
        });
    }

    /**
     * 获取当前直播Item距屏幕左边距
     */
    private int getPosition(int position) {
        int endPos;
        int firstPos = mLinearLayoutManager.findFirstVisibleItemPosition();
        int lastPos = mLinearLayoutManager.findLastVisibleItemPosition();
        if (position < firstPos) {
            rv_list.smoothScrollToPosition(position);
            endPos = mEndPosFirst;
        } else if (position <= lastPos) {
            endPos = rv_list.getChildAt(position - firstPos).getLeft() + mEndPosFirst;
        } else {
            rv_list.smoothScrollToPosition(position);
            endPos = mScreenWidth;
        }
        ULog.d("dsiner end position: " + endPos);
        return endPos;
    }

    private List<PraiseAdapter.Bean> getDatas() {
        List<PraiseAdapter.Bean> datas = new ArrayList<PraiseAdapter.Bean>();
        for (int i = 0; i < 20; i++) {
            PraiseAdapter.Bean b = new PraiseAdapter.Bean();
            b.userId = i;
            b.count = 5 * (20 - i) * (20 - i);
            datas.add(b);
        }
        return datas;
    }
}
