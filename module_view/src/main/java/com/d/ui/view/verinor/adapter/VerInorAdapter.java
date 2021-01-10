package com.d.ui.view.verinor.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.d.lib.pulllayout.rv.adapter.CommonAdapter;
import com.d.lib.pulllayout.rv.adapter.CommonHolder;
import com.d.ui.view.R;
import com.d.ui.view.verinor.models.VerInorModel;

import java.util.List;

/**
 * Horizontal Adapter
 * Created by D on 2017/1/4.
 */
public class VerInorAdapter extends CommonAdapter<VerInorModel> {
    private int mParentPosition; // Parent position

    public VerInorAdapter(Context context, List<VerInorModel> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    public void setParentPosition(int position) {
        this.mParentPosition = position;
    }

    @Override
    public void convert(final int position, CommonHolder holder, VerInorModel item) {
        holder.setText(R.id.tv_desc, item.content);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext.getApplicationContext(),
                        "Click at: " + mParentPosition + "_" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
