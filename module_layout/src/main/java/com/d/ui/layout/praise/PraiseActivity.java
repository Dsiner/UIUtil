package com.d.ui.layout.praise;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.d.lib.common.utils.Util;
import com.d.lib.common.utils.ViewHelper;
import com.d.lib.common.utils.log.ULog;
import com.d.lib.ui.layout.praise.IPraise;
import com.d.lib.ui.layout.praise.PraiseLayout;
import com.d.ui.layout.R;
import com.d.ui.layout.praise.adapter.PraiseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by D on 2017/10/31.
 */
public class PraiseActivity extends Activity {
    private int screenWidth;
    private int endPosFirst;
    private RecyclerView rvList;
    private LinearLayoutManager linearLayoutManager;
    private PraiseLayout plPraise;
    private PraiseAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_praise);
        screenWidth = Util.getScreenSize(this)[0];
        endPosFirst = Util.dip2px(this, 64 / 2 - 15 / 2);
        initView();
        initPraise();
        initClick();
    }

    private void initView() {
        rvList = (RecyclerView) findViewById(R.id.rv_list);
        plPraise = (PraiseLayout) findViewById(R.id.pl_praise);
    }

    private void initPraise() {
        adapter = new PraiseAdapter(this, getDatas(), R.layout.adapter_praise, rvList);
        adapter.setPosLiving(adapter.getItemCount() - 2);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvList.setHasFixedSize(true);
        rvList.setLayoutManager(linearLayoutManager);
        rvList.setItemAnimator(new DefaultItemAnimator());
        rvList.setAdapter(adapter);
        rvList.setItemAnimator(new DefaultItemAnimator());
        rvList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                /**
                 * newState
                 * 1: 手指拖动，开始滑动
                 * 2: 手指松开，自由惯性滑动
                 * 0: 滑动停止/惯性滑动停止
                 */
                adapter.setScrollState(newState);
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        plPraise.setIPraise(new IPraise() {
            @Override
            public void onAnimationEnd() {
                adapter.doPraise();
            }
        });
    }

    private void initClick() {
        ViewHelper.setOnClick(this, R.id.llyt_do_praise, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ViewHelper.setOnClick(this, R.id.llyt_do_praise, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int posLiving = adapter.getPosLiving();
                if (posLiving < 0 || posLiving >= adapter.getItemCount()) {
                    return;
                }
                plPraise.setEndP(getPosition(posLiving)); // 设置此次红心飘移的终点横坐标
                plPraise.addHeart(""); // 开始一次点赞，动画开始
            }
        });
    }

    /**
     * 获取当前直播Item距屏幕左边距
     */
    private int getPosition(int position) {
        int endPos;
        int firstPos = linearLayoutManager.findFirstVisibleItemPosition();
        int lastPos = linearLayoutManager.findLastVisibleItemPosition();
        if (position < firstPos) {
            rvList.smoothScrollToPosition(position);
            endPos = endPosFirst;
        } else if (position <= lastPos) {
            endPos = rvList.getChildAt(position - firstPos).getLeft() + endPosFirst;
        } else {
            rvList.smoothScrollToPosition(position);
            endPos = screenWidth;
        }
        ULog.d("dsiner endPos: " + endPos);
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
