package com.d.lib.ui.layout.convenientbanner.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Vertical ViewPager which overrides onTouchEvent to swap the MotionEvent
 */
@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
public class CBVerticalLoopViewPager extends CBLoopViewPager {

    public CBVerticalLoopViewPager(Context context) {
        super(context);
        setPageTransformer(true, new VerticalPageTransformer());
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    public CBVerticalLoopViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPageTransformer(true, new VerticalPageTransformer());
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        event.setLocation(event.getY() * getWidth() / getHeight(),
                event.getX() * getHeight() / getWidth());
        return super.onTouchEvent(event);
    }

    /**
     * Vertical PageTransformer which changes the animation for from horizontal to vertical.
     */
    private class VerticalPageTransformer implements ViewPager.PageTransformer {

        @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
        @Override
        public void transformPage(View view, float position) {
            view.setTranslationX(-view.getWidth() * position);
            view.setTranslationY(view.getHeight() * position);
        }
    }
}
