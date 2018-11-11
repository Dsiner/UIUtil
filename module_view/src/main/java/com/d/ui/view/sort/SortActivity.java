package com.d.ui.view.sort;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.d.lib.common.utils.ViewHelper;
import com.d.lib.ui.view.sort.SideBar;
import com.d.lib.ui.view.sort.SortBean;
import com.d.lib.ui.view.sort.SortUtil;
import com.d.ui.view.R;

import java.util.ArrayList;
import java.util.List;

public class SortActivity extends AppCompatActivity {
    private LinearLayout llytTin;
    private TextView tvTinLetter;
    private RecyclerView rvList;
    private SideBar sideBar;
    private SortUtil sortUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort);
        llytTin = (LinearLayout) findViewById(R.id.llyt_tin);
        tvTinLetter = (TextView) findViewById(R.id.tv_tin_letter);
        rvList = (RecyclerView) findViewById(R.id.rv_list);
        sideBar = (SideBar) findViewById(R.id.sb_sidebar);
        initBack();
        init();
    }

    private void init() {
        List<SortBean> datas = getDatas();
        sortUtil = new SortUtil();
        List<String> letters = sortUtil.sortDatas(datas);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.setAdapter(new SortAdapter(this, datas, R.layout.adapter_sort));
        rvList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                sortUtil.onScrolled(recyclerView, llytTin, tvTinLetter);
            }
        });
        sideBar.reset(letters);
        sideBar.setOnLetterChangedListener(new SideBar.OnLetterChangedListener() {
            @Override
            public void onChange(int index, String c) {
                sortUtil.onChange(index, c, rvList);
            }
        });
    }

    public List<SortBean> getDatas() {
        String[] arrays = getResources().getStringArray(R.array.arrays_sort);
        List<SortBean> datas = new ArrayList<>();
        for (String c : arrays) {
            SortBean bean = new SortBean(c);
            bean.content = c;
            datas.add(bean);
        }
        return datas;
    }

    private void initBack() {
        ViewHelper.setOnClick(this, R.id.iv_title_left, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
