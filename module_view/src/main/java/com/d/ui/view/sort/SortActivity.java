package com.d.ui.view.sort;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.d.lib.common.utils.ViewHelper;
import com.d.lib.common.view.popup.MenuPopup;
import com.d.lib.common.view.popup.PopupWindowFactory;
import com.d.lib.ui.view.sort.SideBar;
import com.d.lib.ui.view.sort.SortBean;
import com.d.lib.ui.view.sort.SortUtil;
import com.d.ui.view.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SortActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout llytTin;
    private TextView tvTinLetter;
    private RecyclerView rvList;
    private SideBar sideBar;
    private SortUtil sortUtil;

    @Override
    public void onClick(View v) {
        int resId = v.getId();
        if (resId == R.id.iv_title_left) {
            finish();
        } else if (resId == R.id.iv_title_right) {
            MenuPopup menuPopup = PopupWindowFactory.createFactory(this)
                    .getMenuPopup(Arrays.asList(new MenuPopup.Bean("Center",
                                    R.color.lib_pub_color_white, false),
                            new MenuPopup.Bean("Normal",
                                    R.color.lib_pub_color_white, false)), new MenuPopup.OnMenuListener() {
                        @Override
                        public void onClick(PopupWindow popup, int position, String item) {
                            switch (position) {
                                case 0:
                                    sideBar.setType(SideBar.TYPE_CENTER);
                                    sideBar.reset(sortUtil.sortDatas(getDatas()));
                                    break;
                                case 1:
                                    sideBar.setType(SideBar.TYPE_NORMAL);
                                    sideBar.reset(sortUtil.sortDatas(getDatas()));
                                    break;
                            }
                        }
                    });
            menuPopup.showAsDropDown((View) ViewHelper.findView(this, R.id.iv_title_right));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort);
        llytTin = (LinearLayout) findViewById(R.id.llyt_tin);
        tvTinLetter = (TextView) findViewById(R.id.tv_tin_letter);
        rvList = (RecyclerView) findViewById(R.id.rv_list);
        sideBar = (SideBar) findViewById(R.id.sb_sidebar);
        bindView();
        init();
    }

    private void init() {
        List<SortBean> datas = getDatas();
        sortUtil = new SortUtil();
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
        sideBar.setType(SideBar.TYPE_CENTER);
        sideBar.reset(sortUtil.sortDatas(datas));
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

    private void bindView() {
        ViewHelper.setOnClick(this, this, R.id.iv_title_left, R.id.iv_title_right);
    }
}
