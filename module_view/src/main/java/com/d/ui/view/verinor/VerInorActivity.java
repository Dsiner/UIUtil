package com.d.ui.view.verinor;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.d.lib.common.util.ViewHelper;
import com.d.lib.xrv.XRecyclerView;
import com.d.lib.xrv.adapter.CommonHolder;
import com.d.lib.xrv.adapter.MultiItemTypeSupport;
import com.d.ui.view.R;
import com.d.ui.view.verinor.adapter.VerAdapter;
import com.d.ui.view.verinor.models.OffsetBean;
import com.d.ui.view.verinor.models.VerInorModel;
import com.d.ui.view.verinor.models.VerModel;

import java.util.ArrayList;
import java.util.List;

public class VerInorActivity extends Activity implements View.OnClickListener {
    private XRecyclerView xrv_list;

    @Override
    public void onClick(View v) {
        int resId = v.getId();
        if (R.id.iv_title_left == resId) {
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verinor);
        bindView();
        init();
    }

    private void bindView() {
        xrv_list = ViewHelper.findView(this, R.id.xrv_list);

        ViewHelper.setOnClick(this, this, R.id.iv_title_left);
    }

    private void init() {
        VerAdapter adapter = new VerAdapter(VerInorActivity.this, getVerDatas(), new MultiItemTypeSupport<VerModel>() {
            @Override
            public int getLayoutId(int viewType) {
                switch (viewType) {
                    case 0:
                        return R.layout.adapter_ver0;
                    case 1:
                        return R.layout.adapter_ver1;
                    default:
                        return R.layout.adapter_ver0;
                }
            }

            @Override
            public int getItemViewType(int position, VerModel horModel) {
                return horModel.type;
            }
        });
        xrv_list.setCanRefresh(false);
        xrv_list.setCanLoadMore(false);
        xrv_list.showAsList();
        xrv_list.setAdapter(adapter);
        xrv_list.setRecyclerListener(new RecyclerView.RecyclerListener() {
            @Override
            public void onViewRecycled(RecyclerView.ViewHolder vh) {
                if (vh instanceof CommonHolder && ((CommonHolder) vh).mLayoutId == R.layout.adapter_ver1) {
                    CommonHolder holder = (CommonHolder) vh;
                    OffsetBean.setPositionAndOffset((RecyclerView) holder.getView(R.id.rv_ver_inor));
                }
            }
        });
    }

    private List<VerModel> getVerDatas() {
        List<VerModel> datas = new ArrayList<>();
        VerModel model;
        for (int i = 0; i < 30; i++) {
            switch (i) {
                case 1:
                case 3:
                case 4:
                case 10:
                case 12:
                case 22:
                    model = new VerModel(1);
                    model.datas = getVerInorDatas(i);
                    break;
                default:
                    model = new VerModel(0);
                    model.content = "" + i;
                    break;
            }
            datas.add(model);
        }
        return datas;
    }

    private List<VerInorModel> getVerInorDatas(final int position) {
        List<VerInorModel> models = new ArrayList<>();
        for (int i = 0; i < 7 * position; i++) {
            VerInorModel m = new VerInorModel();
            m.content = "" + position + "_" + i;
            models.add(m);
        }
        return models;
    }
}
