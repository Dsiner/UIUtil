package com.d.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.d.lib.common.util.ViewHelper;
import com.d.ui.layout.arcmenu.ArcMenuActivity;
import com.d.ui.layout.banner.BannerActivity;
import com.d.ui.layout.heartlayout.HeartActivity;
import com.d.ui.layout.poi.PoiActivity;
import com.d.ui.layout.praise.PraiseActivity;
import com.d.ui.layout.shadow.ShadowActivity;
import com.d.ui.view.advertswitcher.AdvertSwitcherActivity;
import com.d.ui.view.clock.ClockSetActivity;
import com.d.ui.view.flowlayout.FlowLayoutActivity;
import com.d.ui.view.lrc.LrcActivity;
import com.d.ui.view.progress.ProgressActivity;
import com.d.ui.view.recordtrigger.RecordTriggerActivity;
import com.d.ui.view.replybg.ReplyBgActivity;
import com.d.ui.view.sort.SortActivity;
import com.d.ui.view.stroke.HoleBgActivity;
import com.d.ui.view.tick.TickActivity;
import com.d.ui.view.toggle.ToggleActivity;
import com.d.ui.view.verinor.VerInorActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_lrc:
                startActivity(new Intent(MainActivity.this, LrcActivity.class));
                break;

            case R.id.btn_sort:
                startActivity(new Intent(MainActivity.this, SortActivity.class));
                break;

            case R.id.btn_tick:
                startActivity(new Intent(MainActivity.this, TickActivity.class));
                break;

            case R.id.btn_toggle:
                startActivity(new Intent(MainActivity.this, ToggleActivity.class));
                break;

            case R.id.btn_replybg:
                startActivity(new Intent(MainActivity.this, ReplyBgActivity.class));
                break;

            case R.id.btn_stoke:
                startActivity(new Intent(MainActivity.this, HoleBgActivity.class));
                break;

            case R.id.btn_arcmenu:
                startActivity(new Intent(MainActivity.this, ArcMenuActivity.class));
                break;

            case R.id.btn_heart:
                startActivity(new Intent(MainActivity.this, HeartActivity.class));
                break;

            case R.id.btn_progress:
                startActivity(new Intent(MainActivity.this, ProgressActivity.class));
                break;

            case R.id.btn_praise:
                startActivity(new Intent(MainActivity.this, PraiseActivity.class));
                break;

            case R.id.btn_shadow:
                startActivity(new Intent(MainActivity.this, ShadowActivity.class));
                break;

            case R.id.btn_recordtrigger:
                startActivity(new Intent(MainActivity.this, RecordTriggerActivity.class));
                break;

            case R.id.btn_verinor:
                startActivity(new Intent(MainActivity.this, VerInorActivity.class));
                break;

            case R.id.btn_poi:
                startActivity(new Intent(MainActivity.this, PoiActivity.class));
                break;

            case R.id.btn_clockset:
                startActivity(new Intent(MainActivity.this, ClockSetActivity.class));
                break;

            case R.id.btn_advert_switcher:
                startActivity(new Intent(MainActivity.this, AdvertSwitcherActivity.class));
                break;

            case R.id.btn_advert_flow_layout:
                startActivity(new Intent(MainActivity.this, FlowLayoutActivity.class));
                break;

            case R.id.btn_banner:
                startActivity(new Intent(MainActivity.this, BannerActivity.class));
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindView();
    }

    private void bindView() {
        ViewHelper.setOnClickListener(this, this, R.id.btn_lrc, R.id.btn_sort,
                R.id.btn_tick, R.id.btn_toggle, R.id.btn_stoke, R.id.btn_replybg, R.id.btn_arcmenu,
                R.id.btn_heart, R.id.btn_progress, R.id.btn_praise, R.id.btn_shadow,
                R.id.btn_recordtrigger, R.id.btn_verinor, R.id.btn_poi, R.id.btn_clockset,
                R.id.btn_advert_switcher, R.id.btn_advert_flow_layout, R.id.btn_banner);
    }
}
