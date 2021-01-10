package com.d.ui.view.verinor.adapter;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.d.lib.pulllayout.rv.adapter.CommonAdapter;
import com.d.lib.pulllayout.rv.adapter.CommonHolder;
import com.d.lib.pulllayout.rv.adapter.MultiItemTypeSupport;
import com.d.ui.view.R;
import com.d.ui.view.verinor.models.OffsetBean;
import com.d.ui.view.verinor.models.VerInorModel;
import com.d.ui.view.verinor.models.VerModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 纵向Adapter
 * Created by D on 2017/1/4.
 */
public class VerAdapter extends CommonAdapter<VerModel> {
    private RecyclerView.RecycledViewPool mViewPool;

    public VerAdapter(Context context, List<VerModel> datas, MultiItemTypeSupport<VerModel> multiItemTypeSupport) {
        super(context, datas, multiItemTypeSupport);
        mViewPool = new RecyclerView.RecycledViewPool();
    }

    @Override
    public void convert(final int position, CommonHolder holder, VerModel item) {
        if (holder.layoutId == R.layout.adapter_ver_text) {
            // Text type
            holder.setText(R.id.tv_desc, item.content);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext.getApplicationContext(),
                            "Click at: " + position, Toast.LENGTH_SHORT).show();
                }
            });
        } else if (holder.layoutId == R.layout.adapter_ver_inor) {
            // Nested type
            loadInor((RecyclerView) holder.getView(R.id.rv_ver_inor), item, position);
        }
    }

    /**
     * Load horizontal Recyclerview
     */
    private void loadInor(final RecyclerView list, final VerModel item, int position) {
        list.setTag(item);
        VerInorAdapter adapter = (VerInorAdapter) list.getAdapter();
        if (adapter == null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            adapter = new VerInorAdapter(mContext, new ArrayList<VerInorModel>(),
                    R.layout.adapter_ver_inor_text);
            list.setRecycledViewPool(mViewPool);
            list.setHasFixedSize(true);
            list.setLayoutManager(layoutManager);
            list.setItemAnimator(new DefaultItemAnimator());
            list.setAdapter(adapter);
        }
        adapter.setParentPosition(position);
        adapter.setDatas(item.datas);
        adapter.notifyDataSetChanged();
        OffsetBean.scrollToPositionWithOffset(list);
    }
}
