package com.d.ui.view.sort;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.d.lib.common.util.ViewHelper;
import com.d.lib.common.widget.popup.MenuPopup;
import com.d.lib.common.widget.popup.PopupWindowFactory;
import com.d.lib.ui.view.sort.SideBar;
import com.d.lib.ui.view.sort.SortBean;
import com.d.lib.ui.view.sort.SortUtil;
import com.d.ui.view.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SortActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout llyt_tin;
    private TextView tv_tin_letter;
    private RecyclerView rv_list;
    private SideBar sb_sidebar;
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
                                    sb_sidebar.setType(SideBar.TYPE_CENTER);
                                    sb_sidebar.reset(sortUtil.sortDatas(getDatas()));
                                    break;
                                case 1:
                                    sb_sidebar.setType(SideBar.TYPE_NORMAL);
                                    sb_sidebar.reset(sortUtil.sortDatas(getDatas()));
                                    break;
                            }
                        }
                    });
            menuPopup.showAsDropDown((View) ViewHelper.findViewById(this, R.id.iv_title_right));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort);
        bindView();
        init();
    }

    private void init() {
        List<SortBean> datas = getDatas();
        sortUtil = new SortUtil();
        rv_list.setLayoutManager(new LinearLayoutManager(this));
        rv_list.setAdapter(new SortAdapter(this, datas, R.layout.adapter_sort));
        rv_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                sortUtil.onScrolled(recyclerView, llyt_tin, tv_tin_letter);
            }
        });
        sb_sidebar.setType(SideBar.TYPE_CENTER);
        sb_sidebar.reset(sortUtil.sortDatas(datas));
        sb_sidebar.setOnLetterChangedListener(new SideBar.OnLetterChangedListener() {
            @Override
            public void onChange(int index, String c) {
                sortUtil.onChange(index, c, rv_list);
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
        llyt_tin = ViewHelper.findViewById(this, R.id.llyt_tin);
        tv_tin_letter = ViewHelper.findViewById(this, R.id.tv_tin_letter);
        rv_list = ViewHelper.findViewById(this, R.id.rv_list);
        sb_sidebar = ViewHelper.findViewById(this, R.id.sb_sidebar);

        ViewHelper.setOnClickListener(this, this, R.id.iv_title_left, R.id.iv_title_right);
    }
}
