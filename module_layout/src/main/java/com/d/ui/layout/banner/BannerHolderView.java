package com.d.ui.layout.banner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.d.lib.common.util.ViewHelper;
import com.d.lib.ui.layout.convenientbanner.holder.Holder;
import com.d.ui.layout.R;

public class BannerHolderView implements Holder<BannerBean> {
    private View root;

    @Override
    public View createView(Context context) {
        root = LayoutInflater.from(context).inflate(R.layout.adapter_banner, null);
        return root;
    }

    @Override
    public void UpdateUI(Context context, int position, BannerBean data, boolean canLoop) {
        ImageView iv_banner_image = ViewHelper.findViewById(root, R.id.iv_banner_image);
        Glide.with(context.getApplicationContext()).load(data.url)
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .dontAnimate())
                .into(iv_banner_image);
    }
}
