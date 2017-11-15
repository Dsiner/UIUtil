package com.d.uiutil.verinor.adapter;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.d.lib.xrv.adapter.CommonAdapter;
import com.d.lib.xrv.adapter.CommonHolder;
import com.d.lib.xrv.adapter.MultiItemTypeSupport;
import com.d.uiutil.R;
import com.d.uiutil.verinor.models.VerInorModel;
import com.d.uiutil.verinor.models.VerModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 纵向adapter
 * Created by D on 2017/1/4.
 */
public class VerAdapter extends CommonAdapter<VerModel> {
    private RecyclerView.RecycledViewPool viewPool;

    public VerAdapter(Context context, List<VerModel> datas, MultiItemTypeSupport<VerModel> multiItemTypeSupport) {
        super(context, datas, multiItemTypeSupport);
        viewPool = new RecyclerView.RecycledViewPool();
    }

    @Override
    public void convert(final int position, CommonHolder holder, VerModel item) {
        if (holder.mLayoutId == R.layout.adapter_ver0) {
            //文本类型
            holder.setText(R.id.tv_desc, item.content);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext.getApplicationContext(), "click: " + position, Toast.LENGTH_SHORT).show();
                }
            });
        } else if (holder.mLayoutId == R.layout.adapter_ver1) {
            //嵌套类型
            initInor((RecyclerView) holder.getView(R.id.rv_ver_inor), item, position);
        }
    }

    /**
     * 装载横向recyclerview
     */
    private void initInor(final RecyclerView list, final VerModel item, int position) {
        VerInorAdapter adapter = (VerInorAdapter) list.getAdapter();
        if (adapter == null) {
            LinearLayoutManager llManager = new LinearLayoutManager(mContext);
            llManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            adapter = new VerInorAdapter(mContext, new ArrayList<VerInorModel>(), R.layout.adapter_ver_inor, position);
            list.setRecycledViewPool(viewPool);
            list.setHasFixedSize(true);
            list.setLayoutManager(llManager);
            list.setItemAnimator(new DefaultItemAnimator());
            list.setAdapter(adapter);
        }
        adapter.setDatas(item.datas);
        adapter.notifyDataSetChanged();
        list.setTag(item);
        item.scrollToPositionWithOffset(list);
    }

    public static class OffsetBean {
        public int lastOffset;
        public int lastPosition;

        public void setPositionAndOffset(RecyclerView rv) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) rv.getLayoutManager();
            //获取可视的第一个view
            View topView = layoutManager.getChildAt(0);
            if (topView != null) {
                //获取与该view的左部的偏移量
                lastOffset = topView.getLeft();
                //得到该View的数组位置
                lastPosition = layoutManager.getPosition(topView);
            }
        }

        public void scrollToPositionWithOffset(RecyclerView rv) {
            LinearLayoutManager llManager = (LinearLayoutManager) rv.getLayoutManager();
            llManager.scrollToPositionWithOffset(lastPosition, lastOffset);
        }
    }
}
