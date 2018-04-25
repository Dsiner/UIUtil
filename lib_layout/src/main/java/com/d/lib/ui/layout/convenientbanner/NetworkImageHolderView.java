package com.d.lib.ui.layout.convenientbanner;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.d.lib.ui.layout.convenientbanner.holder.Holder;

/**
 * Created by Sai on 15/8/4.
 * 网络图片加载例子
 */
public class NetworkImageHolderView implements Holder<String> {

    private ImageView imageView;

    @Override
    public View createView(Context context) {
        //你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, int position, String data, boolean canLoop) {
//        Glide.with(context.getApplicationContext()).load(data).apply(new RequestOptions()
//                .placeholder(R.color.black)
//                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//                .dontAnimate())
//                .into(imageView);
    }
}
