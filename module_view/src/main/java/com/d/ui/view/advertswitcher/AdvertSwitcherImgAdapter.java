package com.d.ui.view.advertswitcher;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.d.lib.ui.view.advertswitcher.AdvertSwitcher;
import com.d.ui.view.R;

import java.util.List;

class AdvertSwitcherImgAdapter extends AdvertSwitcher.Adapter<AdvertSwitcherBean> {

    AdvertSwitcherImgAdapter(Context context, List<AdvertSwitcherBean> datas, int resId) {
        super(context, datas, resId);
    }

    @Override
    public View makeView() {
        return LayoutInflater.from(mContext).inflate(mResId, null);
    }

    @Override
    public void bindView(View view, AdvertSwitcherBean item, final int position) {
        TextView tvTag = (TextView) view.findViewById(R.id.tv_no);
        tvTag.setText(item.no);
        tvTag.setVisibility(View.GONE);
        TextView tvContent = (TextView) view.findViewById(R.id.tv_content);
        tvContent.setText(item.content);
        ImageView ivICon = (ImageView) view.findViewById(R.id.iv_icon);
        ivICon.setImageDrawable(ContextCompat.getDrawable(mContext, item.imgRes));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Click at: " + position, Toast.LENGTH_LONG).show();
            }
        });
    }
}
