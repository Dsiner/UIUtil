package com.d.uiutil.poi;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;

import com.d.lib.ui.layout.poi.PoiLayout;
import com.d.lib.ui.layout.poi.PoiListView;
import com.d.lib.ui.layout.poi.PoiTextView;
import com.d.uiutil.R;
import com.d.uiutil.loader.CommonLoader;

import java.util.ArrayList;

/**
 * Poi
 * Created by D on 2017/11/1.
 */
public class PoiActivity extends Activity implements PoiLayout.OnChangeListener {
    private PoiLayout poiLayout;
    private PoiListView list;
    private PoiTextView tvBottom;
    private PoiMapAdapter adapter;
    private CommonLoader<PoiModel> commonLoader;
    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi);
        handler = new Handler();
        initView();
        initList();
        initPoiLayout();
        getData(commonLoader.page);
    }

    private void initView() {
        poiLayout = (PoiLayout) findViewById(R.id.poi_layout);
        list = (PoiListView) findViewById(R.id.poi_list);
        tvBottom = (PoiTextView) findViewById(R.id.tv_bottom);
    }

    private void initList() {
        adapter = new PoiMapAdapter(this, new ArrayList<PoiModel>(), R.layout.adapter_poi);
        list.setCanRefresh(false);
        list.setCanLoadMore(true);
        list.showAsList();
        list.setAdapter(adapter);
        commonLoader = new CommonLoader<>(list, adapter);
        commonLoader.setPageCount(10);
        commonLoader.setOnLoaderListener(new CommonLoader.OnLoaderListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                getData(commonLoader.page);
            }

            @Override
            public void loadSuccess() {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void noContent() {

            }

            @Override
            public void loadError(boolean isEmpty) {

            }
        });
    }

    private void initPoiLayout() {
        poiLayout.setOnChangeListener(this);
        tvBottom.setOnTikListener(new PoiTextView.OnTikListener() {
            @Override
            public void onTik(View v) {
                poiLayout.toggle(PoiLayout.STATUS_EXTEND);
            }
        });
    }

    /**
     * 模拟数据获取
     */
    private void getData(final int page) {
        long delayMillis = page == 1 ? 0 : 2000;
        handler.postDelayed(new Runnable() {
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
                setData(datas);
            }
        }, delayMillis);
    }

    /**
     * 数据设置
     */
    private void setData(final ArrayList<PoiModel> data) {
        commonLoader.setData(data);
        if (commonLoader.page == 1) {
            if (data.size() > 0) {
                poiLayout.setVisibility(View.VISIBLE);
                poiLayout.toggle(PoiLayout.STATUS_DEFAULT);
            }
            list.scrollToPosition(0);
        }
    }

    @Override
    public void onChange(int status) {
        tvBottom.setVisibility(status == PoiLayout.STATUS_CLOSE ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onScroll(float offset) {
        tvBottom.setVisibility(offset == 1 ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
