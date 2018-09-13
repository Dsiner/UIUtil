package com.d.uiutil.verinor.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.d.lib.xrv.adapter.CommonAdapter;
import com.d.lib.xrv.adapter.CommonHolder;
import com.d.uiutil.R;
import com.d.uiutil.verinor.models.VerInorModel;

import java.util.List;

/**
 * 横向Adapter
 * Created by D on 2017/1/4.
 */
public class VerInorAdapter extends CommonAdapter<VerInorModel> {
    private int upPos; // 父position

    public VerInorAdapter(Context context, List<VerInorModel> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    public void setUpPos(int upPos) {
        this.upPos = upPos;
    }

    @Override
    public void convert(final int position, CommonHolder holder, VerInorModel item) {
        holder.setText(R.id.tv_desc, item.content);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext.getApplicationContext(), "Click at: " + upPos + "_" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
