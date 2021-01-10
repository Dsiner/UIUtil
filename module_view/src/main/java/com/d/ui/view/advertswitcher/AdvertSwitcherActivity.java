package com.d.ui.view.advertswitcher;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.d.lib.common.util.ViewHelper;
import com.d.lib.ui.view.advertswitcher.AdvertSwitcher;
import com.d.ui.view.R;

import java.util.ArrayList;
import java.util.List;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class AdvertSwitcherActivity extends Activity implements View.OnClickListener {
    private AdvertSwitcher as_advert_text, as_advert_img;

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
        setContentView(R.layout.activity_advert);
        bindView();
        init();
    }

    private void init() {
        AdvertSwitcherTextAdapter textAdapter = new AdvertSwitcherTextAdapter(this, getTextDatas(),
                R.layout.adapter_advert_text);
        as_advert_text.setAdapter(textAdapter);
        textAdapter.notifyDataSetChanged();

        AdvertSwitcherImgAdapter imgAdapter = new AdvertSwitcherImgAdapter(this, getImgDatas(),
                R.layout.adapter_advert_img);
        as_advert_img.setAdapter(imgAdapter);
        imgAdapter.notifyDataSetChanged();
    }

    private void bindView() {
        as_advert_text = ViewHelper.findViewById(this, R.id.as_advert_text);
        as_advert_img = ViewHelper.findViewById(this, R.id.as_advert_img);

        ViewHelper.setOnClickListener(this, this, R.id.iv_title_left);
    }

    @Override
    protected void onResume() {
        super.onResume();
        as_advert_text.start();
        as_advert_img.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        as_advert_text.stop();
        as_advert_img.stop();
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
