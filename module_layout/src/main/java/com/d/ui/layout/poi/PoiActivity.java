package com.d.ui.layout.poi;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.d.lib.common.util.ViewHelper;
import com.d.lib.pulllayout.loader.CommonLoader;
import com.d.lib.taskscheduler.TaskScheduler;
import com.d.lib.ui.layout.poilayout.PoiLayout;
import com.d.lib.ui.layout.poilayout.PoiListView;
import com.d.lib.ui.layout.poilayout.PoiTextView;
import com.d.ui.layout.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Poi
 * Created by D on 2017/11/1.
 */
public class PoiActivity extends Activity implements View.OnClickListener {
    private PoiLayout poi_layout;
    private PoiListView poi_list;
    private PoiTextView tv_bottom;
    private PoiMapAdapter mAdapter;
    private CommonLoader<PoiModel> mCommonLoader;

    @Override
    public void onClick(View v) {
        int resId = v.getId();
        if (R.id.iv_title_left == resId) {
            finish();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi);
        bindView();
        init();
        onLoad(mCommonLoader.page);
    }

    private void bindView() {
        poi_layout = ViewHelper.findViewById(this, R.id.poi_layout);
        poi_list = ViewHelper.findViewById(this, R.id.poi_list);
        tv_bottom = ViewHelper.findViewById(this, R.id.tv_bottom);

        ViewHelper.setOnClickListener(this, this, R.id.iv_title_left);
    }

    private void init() {
        mAdapter = new PoiMapAdapter(this, new ArrayList<PoiModel>(), R.layout.adapter_poi);
        poi_list.setCanPullDown(false);
        poi_list.setCanPullUp(true);
        poi_list.setAdapter(mAdapter);
        mCommonLoader = new CommonLoader<>(poi_list, mAdapter);
        mCommonLoader.setPageCount(20);
        mCommonLoader.setOnLoaderListener(new CommonLoader.OnLoaderListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                onLoad(mCommonLoader.page);
            }

            @Override
            public void loadSuccess() {
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void noContent() {

            }

            @Override
            public void loadError(boolean isEmpty) {

            }
        });

        poi_layout.setOnChangeListener(new PoiLayout.OnChangeListener() {
            @Override
            public void onChange(int status) {
                tv_bottom.setVisibility(status == PoiLayout.STATUS_CLOSE ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onScroll(float offset) {
                tv_bottom.setVisibility(offset == 1 ? View.VISIBLE : View.GONE);
            }
        });
        tv_bottom.setOnTikListener(new PoiTextView.OnTikListener() {
            @Override
            public void onTik(View v) {
                poi_layout.toggle(PoiLayout.STATUS_EXTEND);
            }
        });
    }

    /**
     * Simulation data acquisition
     */
    private void onLoad(final int page) {
        final long delayMillis = page == 1 ? 0 : 2000;
        TaskScheduler.postMainDelayed(new Runnable() {
            @Override
            public void run() {
                if (isFinishing()) {
                    return;
                }
                ArrayList<PoiModel> datas = new ArrayList<>();
                int count = page < 10 ? 10 : 6;
                for (int i = 0; i < count; i++) {
                    int index = 10 * (page - 1) + i;
                    datas.add(new PoiModel("标题:" + index, "xxxxxxxxxx" + index));
                }
                loadSuccess(datas);
            }
        }, delayMillis);
    }

    /**
     * Data loaded successfully
     */
    private void loadSuccess(final List<PoiModel> data) {
        mCommonLoader.loadSuccess(data);
        if (mCommonLoader.page == 1) {
            if (data.size() > 0) {
                poi_layout.setVisibility(View.VISIBLE);
                poi_layout.toggle(PoiLayout.STATUS_DEFAULT);
            }
            poi_list.scrollToPosition(0);
        }
    }
}
