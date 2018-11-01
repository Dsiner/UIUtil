package com.d.lib.common.view.loading;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.d.lib.common.R;

import java.lang.ref.WeakReference;

/**
 * Loading
 * Created by Administrator on 2016/8/27.
 */
public class LoadingView extends View {
    public final static int TYPE_DAISY = 0;
    public final static int TYPE_DOT = 1;

    private float width;
    private float height;

    private Context context;
    private Paint paint;
    private RectF tempRct;
    private int type = TYPE_DAISY;
    private long daration;
    private int count = 12;
    private int color;
    private int minAlpha;
    private float widthRate;
    private float heightRate;
    private float rectWidth;
    private int j;
    private Handler handler;
    private Task runnable;
    private boolean isFirst;

    private static class Task implements Runnable {

        WeakReference<LoadingView> weakRef;

        Task(LoadingView view) {
            this.weakRef = new WeakReference<>(view);
        }

        @Override
        public void run() {
            if (isFinished()) {
                return;
            }
            LoadingView theView = weakRef.get();
            theView.invalidate();
            theView.handler.postDelayed(theView.runnable, theView.daration / theView.count);
        }

        private boolean isFinished() {
            return weakRef == null || weakRef.get() == null
                    || weakRef.get().context == null
                    || weakRef.get().context instanceof Activity && ((Activity) weakRef.get().context).isFinishing();
        }
    }

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        this.isFirst = true;
        this.color = ContextCompat.getColor(context, R.color.lib_pub_color_main);
        this.paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.paint.setColor(color);
        this.daration = 1000;
        this.minAlpha = 50;
        this.widthRate = 1f / 3;
        this.heightRate = 1f / 2;
        this.handler = new Handler();
        this.runnable = new Task(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (width == 0 || height == 0 || tempRct == null) {
            return;
        }
        canvas.translate(width / 2, height / 2);
        j++;
        j %= count;
        int alpha;
        for (int i = 0; i < count; i++) {
            canvas.rotate(360f / count);
            alpha = (i - j + count) % count;
            alpha = (int) (((alpha) * (255f - minAlpha) / count + minAlpha));
            paint.setAlpha(alpha);
            switch (type) {
                case TYPE_DAISY:
                    /** Daisy rotation **/
                    canvas.drawRoundRect(tempRct, rectWidth / 2, rectWidth / 2, paint);
                    break;
                case TYPE_DOT:
                    /** Dot rotation **/
                    canvas.drawCircle((tempRct.left + tempRct.right) / 2, (tempRct.top + tempRct.bottom) / 2, rectWidth * 2 / 3, paint);
                    break;
            }
        }
        if (isFirst) {
            isFirst = false;
            handler.postDelayed(runnable, daration / count);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        refreshField();
    }

    private void refreshField() {
        final float h = width > height ? height : width;
        final float rectHeight = h * heightRate / 2;
        final float radius = h * (1 - heightRate / 2) / 2;
        rectWidth = rectHeight * widthRate;
        if (tempRct == null) {
            tempRct = new RectF(-rectWidth / 2, -(radius + rectHeight / 2), rectWidth / 2, -(radius - rectHeight / 2));
        } else {
            tempRct.set(-rectWidth / 2, -(radius + rectHeight / 2), rectWidth / 2, -(radius - rectHeight / 2));
        }
    }

    @Override
    public void setVisibility(int visibility) {
        switch (visibility) {
            case VISIBLE:
                restart();
                break;
            case GONE:
            case INVISIBLE:
                stop();
                break;
        }
        super.setVisibility(visibility);
    }

    @Override
    protected void onAttachedToWindow() {
        if (!isFirst) {
            restart();
        }
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        stop();
        super.onDetachedFromWindow();
    }

    public void setType(int type) {
        this.type = type;
        this.invalidate();
    }

    /**
     * Restart
     */
    public void restart() {
        stop();
        handler.post(runnable);
    }

    /**
     * Stop
     */
    public void stop() {
        isFirst = false;
        handler.removeCallbacks(runnable);
    }
}
