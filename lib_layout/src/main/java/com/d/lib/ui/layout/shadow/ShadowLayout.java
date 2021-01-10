/*
 * Copyright (C) 2015 Basil Miller
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.d.lib.ui.layout.shadow;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.d.lib.ui.layout.R;

/**
 * Created by GIGAMOLE on 13.04.2016.
 */
public class ShadowLayout extends FrameLayout {

    // Default shadow values
    private final static float DEFAULT_SHADOW_RADIUS = 30.0F;
    private final static float DEFAULT_SHADOW_DISTANCE = 15.0F;
    private final static float DEFAULT_SHADOW_ANGLE = 45.0F;
    private final static int DEFAULT_SHADOW_COLOR = Color.DKGRAY;

    // Shadow bounds values
    private final static int MAX_ALPHA = 255;
    private final static float MAX_ANGLE = 360.0F;
    private final static float MIN_RADIUS = 0.1F;
    private final static float MIN_ANGLE = 0.0F;
    // Shadow paint
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG) {
        {
            setDither(true);
            setFilterBitmap(true);
        }
    };
    private final Canvas mCanvas = new Canvas();
    // View bounds
    private final Rect mBounds = new Rect();
    // Shadow bitmap and canvas
    private Bitmap mBitmap;
    // Check whether need to redraw shadow
    private boolean mInvalidateShadow = true;

    // Detect if shadow is visible
    private boolean mIsShadowed;

    // Shadow variables
    private int mShadowColor;
    private int mShadowAlpha;
    private float mShadowRadius;
    private float mShadowDistance;
    private float mShadowAngle;
    private float mShadowDx;
    private float mShadowDy;

    public ShadowLayout(final Context context) {
        this(context, null);
    }

    public ShadowLayout(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadowLayout(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setWillNotDraw(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(LAYER_TYPE_HARDWARE, mPaint);
        }

        // Retrieve attributes from xml
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.lib_ui_layout_ShadowLayout);
        try {
            setIsShadowed(typedArray.getBoolean(R.styleable.lib_ui_layout_ShadowLayout_lib_ui_layout_sl_shadowed, true));
            setShadowRadius(
                    typedArray.getDimension(
                            R.styleable.lib_ui_layout_ShadowLayout_lib_ui_layout_sl_shadowRadius, DEFAULT_SHADOW_RADIUS
                    )
            );
            setShadowDistance(
                    typedArray.getDimension(
                            R.styleable.lib_ui_layout_ShadowLayout_lib_ui_layout_sl_shadowDistance, DEFAULT_SHADOW_DISTANCE
                    )
            );
            setShadowAngle(
                    typedArray.getInteger(
                            R.styleable.lib_ui_layout_ShadowLayout_lib_ui_layout_sl_shadowAngle, (int) DEFAULT_SHADOW_ANGLE
                    )
            );
            setShadowColor(
                    typedArray.getColor(
                            R.styleable.lib_ui_layout_ShadowLayout_lib_ui_layout_sl_shadowColor, DEFAULT_SHADOW_COLOR
                    )
            );
        } finally {
            typedArray.recycle();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // Clear shadow bitmap
        if (mBitmap != null) {
            mBitmap.recycle();
            mBitmap = null;
        }
    }

    public boolean isShadowed() {
        return mIsShadowed;
    }

    public void setIsShadowed(final boolean isShadowed) {
        mIsShadowed = isShadowed;
        postInvalidate();
    }

    public float getShadowDistance() {
        return mShadowDistance;
    }

    public void setShadowDistance(final float shadowDistance) {
        mShadowDistance = shadowDistance;
        resetShadow();
    }

    public float getShadowAngle() {
        return mShadowAngle;
    }

    public void setShadowAngle(final float shadowAngle) {
        mShadowAngle = Math.max(MIN_ANGLE, Math.min(shadowAngle, MAX_ANGLE));
        resetShadow();
    }

    public float getShadowRadius() {
        return mShadowRadius;
    }

    public void setShadowRadius(final float shadowRadius) {
        mShadowRadius = Math.max(MIN_RADIUS, shadowRadius);

        if (isInEditMode()) return;
        // Set blur filter to paint
        mPaint.setMaskFilter(new BlurMaskFilter(mShadowRadius, BlurMaskFilter.Blur.NORMAL));
        resetShadow();
    }

    public int getShadowColor() {
        return mShadowColor;
    }

    public void setShadowColor(final int shadowColor) {
        mShadowColor = shadowColor;
        mShadowAlpha = Color.alpha(shadowColor);

        resetShadow();
    }

    public float getShadowDx() {
        return mShadowDx;
    }

    public float getShadowDy() {
        return mShadowDy;
    }

    // Reset shadow layer
    private void resetShadow() {
        // Detect shadow axis offset
        mShadowDx = (float) ((mShadowDistance) * Math.cos(mShadowAngle / 180.0F * Math.PI));
        mShadowDy = (float) ((mShadowDistance) * Math.sin(mShadowAngle / 180.0F * Math.PI));

        // Set padding for shadow bitmap
//        final int padding = (int) (mShadowDistance + mShadowRadius);
//        setPadding(padding, padding, padding, padding);
        requestLayout();
    }

    private int adjustShadowAlpha(final boolean adjust) {
        return Color.argb(
                adjust ? MAX_ALPHA : mShadowAlpha,
                Color.red(mShadowColor),
                Color.green(mShadowColor),
                Color.blue(mShadowColor)
        );
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // Set ShadowLayout bounds
        mBounds.set(
                0, 0, MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec)
        );
    }

    @Override
    public void requestLayout() {
        // Redraw shadow
        mInvalidateShadow = true;
        super.requestLayout();
    }

    @Override
    protected void dispatchDraw(final Canvas canvas) {
        // If is not shadowed, skip
        if (mIsShadowed) {
            // If need to redraw shadow
            if (mInvalidateShadow) {
                // If bounds is zero
                if (mBounds.width() != 0 && mBounds.height() != 0) {
                    // Reset bitmap to bounds
                    mBitmap = Bitmap.createBitmap(
                            mBounds.width(), mBounds.height(), Bitmap.Config.ARGB_8888
                    );
                    // Canvas reset
                    mCanvas.setBitmap(mBitmap);

                    // We just redraw
//                    mInvalidateShadow = false;
                    // Main feature of this lib. We create the local copy of all content, so now
                    // we can draw bitmap as a bottom layer of natural canvas.
                    // We draw shadow like blur effect on bitmap, cause of setShadowLayer() method of
                    // paint does`t draw shadow, it draw another copy of bitmap
                    super.dispatchDraw(mCanvas);

                    // Get the alpha bounds of bitmap
                    final Bitmap extractedAlpha = mBitmap.extractAlpha();
                    // Clear past content content to draw shadow
                    mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);

                    // Draw extracted alpha bounds of our local canvas
                    mPaint.setColor(adjustShadowAlpha(false));
                    mCanvas.drawBitmap(extractedAlpha, mShadowDx, mShadowDy, mPaint);

                    // Recycle and clear extracted alpha
                    extractedAlpha.recycle();
                } else {
                    // Create placeholder bitmap when size is zero and wait until new size coming up
                    mBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565);
                }
            }

            // Reset alpha to draw child with full alpha
            mPaint.setColor(adjustShadowAlpha(true));
            // Draw shadow bitmap
            if (mCanvas != null && mBitmap != null && !mBitmap.isRecycled())
                canvas.drawBitmap(mBitmap, 0.0F, 0.0F, mPaint);
        }

        // Draw child`s
        super.dispatchDraw(canvas);
    }
}
