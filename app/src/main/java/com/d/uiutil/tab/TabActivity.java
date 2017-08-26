package com.d.uiutil.tab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.d.lib.ui.tab.ScrollTab;
import com.d.uiutil.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * TabActivity
 * Created by D on 2017/8/27.
 */
public class TabActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private ScrollTab scrollTab;
    private ViewPager pager;
    private ArrayList<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        scrollTab = (ScrollTab) findViewById(R.id.stab_tab);
        pager = (ViewPager) findViewById(R.id.pager);
        init();
    }

    private void init() {
        List<String> titles = Arrays.asList("", "");

        fragments = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            TabFragment fragment = new TabFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("index", i);
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return fragments.get(arg0);
            }
        };
        pager.setOffscreenPageLimit(2);
        pager.setAdapter(fragmentPagerAdapter);
        pager.addOnPageChangeListener(this);
        scrollTab.setViewPager(pager);
        scrollTab.setOnTabListener(new ScrollTab.OnTabListener() {
            @Override
            public void onChange(int index, View v) {
                pager.setCurrentItem(index, true);
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}