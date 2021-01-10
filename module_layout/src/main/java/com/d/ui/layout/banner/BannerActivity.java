package com.d.ui.layout.banner;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.d.lib.common.util.ScreenUtils;
import com.d.lib.common.util.ViewHelper;
import com.d.lib.ui.layout.convenientbanner.ConvenientBanner;
import com.d.lib.ui.layout.convenientbanner.holder.CBViewHolderCreator;
import com.d.lib.ui.layout.convenientbanner.listener.OnItemClickListener;
import com.d.ui.layout.R;

import java.util.ArrayList;
import java.util.List;

/**
 * BannerActivity
 * Created by D on 2019/12/6.
 */
public class BannerActivity extends Activity {
    private final static int AUTO_TURNING_TIME = 3000;
    private static final float BANNER_ASPECT_RATIO = 2.1428f; // 15:7

    private ConvenientBanner cb_banner, cb_banner_vertical;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        bindView();
        init();
    }

    private void bindView() {
        cb_banner = ViewHelper.findViewById(this, R.id.cb_banner);
        cb_banner_vertical = ViewHelper.findViewById(this, R.id.cb_banner_vertical);
    }

    private void init() {
        initBanner(cb_banner);
        initBanner(cb_banner_vertical);
    }

    private void initBanner(ConvenientBanner banner) {
        banner.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                ((int) (ScreenUtils.getScreenSize(this)[0] / BANNER_ASPECT_RATIO))));
        banner.setCanLoop(true);
        banner.getViewPager().setClipToPadding(false);
        banner.setPageIndicator(new int[]{R.drawable.banner_indicator,
                R.drawable.banner_indicator_foucus});
        setBanner(banner, getBannerDatas());
    }

    private List<BannerBean> getBannerDatas() {
        List<BannerBean> list = new ArrayList<>();
        list.add(new BannerBean("http://f.hiphotos.baidu.com/image/h%3D300/sign=0c78105b888ba61ec0eece2f713597cc/0e2442a7d933c8956c0e8eeadb1373f08202002a.jpg"));
        list.add(new BannerBean("http://f.hiphotos.baidu.com/image/pic/item/dc54564e9258d109cee38223db58ccbf6c814d1a.jpg"));
        list.add(new BannerBean("http://e.hiphotos.baidu.com/image/pic/item/f2deb48f8c5494ee9c38248827f5e0fe99257e0c.jpg"));
        list.add(new BannerBean("http://d.hiphotos.baidu.com/image/pic/item/7dd98d1001e939014adfbfbe71ec54e737d19654.jpg"));
        list.add(new BannerBean("http://h.hiphotos.baidu.com/image/pic/item/a686c9177f3e67097e188c6031c79f3df8dc5511.jpg"));
        return list;
    }

    private void setBanner(final ConvenientBanner banner, final List<BannerBean> banners) {
        if (banner == null) {
            return;
        }
        if (banners != null && banners.size() > 0) {
            if (banners.size() <= 1) {
                banner.setPointViewVisible(false);
                banner.setCanLoop(false);
                banner.stopTurning();
            } else {
                banner.setPointViewVisible(true);
                banner.setCanLoop(true);
                banner.startTurning(AUTO_TURNING_TIME);
            }
            banner.setPages(new CBViewHolderCreator<BannerHolderView>() {
                @Override
                public BannerHolderView createHolder() {
                    return new BannerHolderView();
                }
            }, banners)
                    .setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            if (position >= 0 && position < banners.size()) {
                            }
                        }
                    });
            banner.setVisibility(View.VISIBLE);
        } else {
            banner.stopTurning();
            banner.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        cb_banner.startTurning(AUTO_TURNING_TIME);
        cb_banner_vertical.startTurning(AUTO_TURNING_TIME);
        super.onResume();
    }

    @Override
    public void onPause() {
        cb_banner.stopTurning();
        cb_banner_vertical.stopTurning();
        super.onPause();
    }
}
