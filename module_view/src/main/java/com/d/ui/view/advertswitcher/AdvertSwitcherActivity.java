package com.d.ui.view.advertswitcher;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.d.lib.common.utils.ViewHelper;
import com.d.lib.ui.view.advertswitcher.AdvertSwitcher;
import com.d.ui.view.R;

import java.util.ArrayList;
import java.util.List;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class AdvertSwitcherActivity extends Activity {
    private AdvertSwitcher textSwitcher, imgSwitcher;
    private AdvertSwitcherTextAdapter textAdapter;
    private AdvertSwitcherImgAdapter imgAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advert);
        bindView();
        init();
    }

    private void init() {
        textAdapter = new AdvertSwitcherTextAdapter(this, getTextDatas(), R.layout.adapter_advert_text);
        textSwitcher.setAdapter(textAdapter);
        textAdapter.notifyDataSetChanged();

        imgAdapter = new AdvertSwitcherImgAdapter(this, getImgDatas(), R.layout.adapter_advert_img);
        imgSwitcher.setAdapter(imgAdapter);
        imgAdapter.notifyDataSetChanged();
    }

    private void bindView() {
        textSwitcher = ViewHelper.findView(this, R.id.as_advert_text);
        imgSwitcher = ViewHelper.findView(this, R.id.as_advert_img);
        ViewHelper.setOnClick(this, R.id.iv_title_left, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        textSwitcher.start();
        imgSwitcher.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        textSwitcher.stop();
        imgSwitcher.stop();
    }

    private List<AdvertSwitcherBean> getTextDatas() {
        List<AdvertSwitcherBean> datas = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            datas.add(new AdvertSwitcherBean("Hot " + i, "Popular events!!!", 0));
        }
        return datas;
    }

    private List<AdvertSwitcherBean> getImgDatas() {
        List<AdvertSwitcherBean> datas = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            datas.add(new AdvertSwitcherBean("" + i, i + ". Promotions", R.drawable.lib_pub_ic_btb_icon));
        }
        return datas;
    }
}
