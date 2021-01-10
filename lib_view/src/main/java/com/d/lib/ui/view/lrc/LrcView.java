package com.d.lib.ui.view.lrc;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;

import com.d.lib.common.util.DimenUtils;
import com.d.lib.ui.view.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * LrcView
 * https://github.com/android-lili/CustomLrcView-master
 * Edited by D on 2017/5/16.
 */
public class LrcView extends View implements ILrcView {
    private final String DEFAULT_TEXT = "畅音乐，享自由";
    /***移动一句歌词的持续时间***/
    private final int DURATION_AUTO_SCROLL = 1500;
    /***停止触摸 如果View需要滚动的持续时间***/
    private final int DURATION_RESET = 400;

    private final float SIZE_TEXT_DEFAULT; // Variables 文字大小
    private final float SIZE_TEXT_CUR_DEFAULT;
    private final float SIZE_TIME_DEFAULT;
    private final float PADDING_DEFAULT;
    private final float PADDING_TIME_DEFAULT;

    private final float MIN_SCALE; // 最小缩放
    private final float MAX_SCALE; // 最大缩放

    private int mWidth;
    private int mHeight;
    private Context mContext;
    private Scroller mScroller;
    private List<LrcRow> mLrcRows;
    private int mTouchSlop;
    private float mDX, mDY; // ActionDown的坐标(dx,dy)
    private float mLastY; // TouchEvent最后一次坐标(lastX,lastY)
    private boolean mDVaild;
    private boolean mCanDrag;

    private int mColorText;
    private int mColorTextCur;
    private int mColorTime;
    private float mSizeText; // Variables 文字大小
    private float mSizeTextCur;
    private float mSizeTime;
    private float mPadding;
    private float mPaddingTime;
    private Paint mPaint; // 仅用于普通文字的画笔
    private Paint mPaintCur; // 仅用于当前高亮文字的画笔
    private Paint mPaintLine; // 仅用于画线的画笔

    private int mRowCount;
    private int mLastRow = -1;
    private int mCurRow = -1;

    private int mRowHeight; // One row height(text+padding)
    private int mOffsetY;

    private ValueAnimator mAnimation;
    private float mTextOffsetX; // 当前歌词水平滚动偏移
    private float mScaleFactor; // 进度因子
    private boolean mWithLine;

    private OnClickListener mOnClickListener;
    private OnSeekChangeListener mOnSeekChangeListener;

    public LrcView(Context context) {
        this(context, null);
    }

    public LrcView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LrcView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.lib_ui_view_LrcView);
        mColorText = typedArray.getColor(R.styleable.lib_ui_view_LrcView_lib_ui_view_lrc_textColor, Color.parseColor("#4577B7"));
        mColorTextCur = typedArray.getColor(R.styleable.lib_ui_view_LrcView_lib_ui_view_lrc_textColorCur, Color.parseColor("#FF4081"));
        mColorTime = typedArray.getColor(R.styleable.lib_ui_view_LrcView_lib_ui_view_lrc_timeColor, Color.parseColor("#FF4081"));
        mSizeText = SIZE_TEXT_DEFAULT = typedArray.getDimension(R.styleable.lib_ui_view_LrcView_lib_ui_view_lrc_textSize, 15);
        mSizeTextCur = SIZE_TEXT_CUR_DEFAULT = typedArray.getDimension(R.styleable.lib_ui_view_LrcView_lib_ui_view_lrc_textSizeCur, 17);
        mSizeTime = SIZE_TIME_DEFAULT = typedArray.getDimension(R.styleable.lib_ui_view_LrcView_lib_ui_view_lrc_timeSize, 8);
        mPadding = PADDING_DEFAULT = typedArray.getDimension(R.styleable.lib_ui_view_LrcView_lib_ui_view_lrc_padding, 17);
        MIN_SCALE = typedArray.getFloat(R.styleable.lib_ui_view_LrcView_lib_ui_view_lrc_minScale, 0.7f);
        MAX_SCALE = typedArray.getFloat(R.styleable.lib_ui_view_LrcView_lib_ui_view_lrc_maxScale, 1.7f);
        typedArray.recycle();
        mPaddingTime = PADDING_TIME_DEFAULT = DimenUtils.dp2px(context, 5);
        init(context);
    }

    @Override
    public void init(Context context) {
        mContext = context;
        mLrcRows = new ArrayList<>();
        mScroller = new Scroller(context);
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextAlign(Paint.Align.LEFT);
        mPaint.setColor(mColorText);

        mPaintCur = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintCur.setTextAlign(Paint.Align.LEFT);
        mPaintCur.setColor(mColorTextCur);

        mPaintLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintLine.setTextAlign(Paint.Align.LEFT);
        mPaintLine.setColor(mColorTime);

        resetValues();
        initAnim();
    }

    private void initAnim() {
        mAnimation = new ValueAnimator();
        mAnimation.setInterpolator(new LinearInterpolator());
        mAnimation.addUpdateListener(new UpdateListener(this));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mLrcRows.size() <= 0) {
            // 画默认的显示文字
            mPaint.setColor(mColorText);
            drawLine(canvas, mPaint,
                    mHeight / 2 + DimenUtils.getTextHeight(mPaint) / 2,
                    DEFAULT_TEXT);
            return;
        }

        float cy = mHeight / 2 + mOffsetY;
        if (mRowHeight != 0) {
            mRowCount = mHeight / mRowHeight + 4; // 初始化将要绘制的歌词行数,因为不需要将所有歌词画出来
        }
        int minRaw = mCurRow - mRowCount / 2;
        int maxRaw = mCurRow + mRowCount / 2;
        minRaw = Math.max(minRaw, 0); // 处理上边界
        maxRaw = Math.min(maxRaw, mLrcRows.size() - 1); // 处理下边界
        // 实现渐变的最大歌词行数
        int count = Math.max(maxRaw - mCurRow, mCurRow - minRaw);
        // 两行歌词间字体颜色变化的透明度
        int alpha = 0;
        if (count != 0) {
            alpha = (0xFF - 0x11) / count;
        }
        // 画出来的第一行歌词的y坐标
        float rowY = cy + minRaw * mRowHeight;

        for (int i = minRaw; i <= maxRaw; i++) {
            String text = mLrcRows.get(i).getContent();//获取到高亮歌词
            if (mCurRow == i) {
                // 画高亮歌词
                // 因为有缩放效果，所有需要动态设置歌词的字体大小
                float size = mSizeTextCur + (mSizeTextCur - mSizeText) * mScaleFactor;
                mPaintCur.setTextSize(size);
                // 用画笔测量歌词的宽度
                float textWidth = mPaintCur.measureText(text);
                if (textWidth > mWidth) {
                    // 如果歌词宽度大于view的宽，则需要动态设置歌词的起始x坐标，以实现水平滚动
                    canvas.drawText(text, mTextOffsetX, rowY, mPaintCur);
                } else {
                    // 如果歌词宽度小于view的宽，则让歌词居中显示
                    float rowX = (mWidth - textWidth) / 2;
                    canvas.drawText(text, rowX, rowY, mPaintCur);
                }

                // 画时间线和时间
                if (mWithLine) {
                    float lineY = mHeight / 2 + getScrollY();
                    canvas.drawText(mLrcRows.get(mCurRow).getTimeStr(), 0,
                            lineY - 5, mPaintLine);

                    float stopX = textWidth < mWidth - mPaddingTime
                            ? (mWidth - textWidth) / 2 - mPaddingTime : 0;
                    canvas.drawLine(0, lineY, stopX, lineY, mPaintLine);
                    float startX = textWidth < mWidth - mPaddingTime
                            ? mWidth - (mWidth - textWidth) / 2 + mPaddingTime : mWidth;
                    canvas.drawLine(startX, lineY, mWidth, lineY, mPaintLine);
                }
            } else {
                if (i == mLastRow) {
                    // 画高亮歌词的上一句
                    // 因为有缩放效果，所有需要动态设置歌词的字体大小
                    float size = mSizeTextCur - (mSizeTextCur - mSizeText) * mScaleFactor;
                    mPaint.setTextSize(size);
                } else {
                    // 画其他的歌词
                    mPaint.setTextSize(mSizeText);
                }
                float textWidth = mPaint.measureText(text);
                float rowX = (getWidth() - textWidth) / 2;
                // 如果计算出的cx为负数,将cx置为0(实现：如果歌词宽大于view宽，则居左显示，否则居中显示)
                if (rowX < 0) {
                    rowX = 0;
                }
                // 实现颜色渐变
                mPaint.setColor(0x1000000 * (255 - Math.abs(i - mCurRow) * alpha) + mColorText);
                canvas.drawText(text, rowX, rowY, mPaint);
            }
            // 计算出下一行歌词绘制的y坐标
            rowY += mRowHeight;
        }
    }

    private void drawLine(Canvas canvas, Paint paint, float y, String text) {
        float textWidth = paint.measureText(text);
        float textX = (mWidth - textWidth) / 2;
        if (textX < 0) {
            textX = 0;
        }
        canvas.drawText(text, textX, y, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }

    private void smoothScrollTo(int dstY, int duration) {
        int oldScrollY = getScrollY();
        int offset = dstY - oldScrollY;
        mScroller.startScroll(getScrollX(), oldScrollY, getScrollX(), offset, duration);
        invalidate();
    }

    private void forceFinished() {
        stopHorizontalScrollLrc();
        if (!mScroller.isFinished()) {
            mScroller.forceFinished(true);
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            mScaleFactor = mScroller.timePassed() * 3f / DURATION_AUTO_SCROLL;
            if (mScaleFactor > 1) {
                mScaleFactor = 1;
            }
            invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mLrcRows.size() <= 0) {
            return false;
        }
        float eX = event.getX();
        float eY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDX = event.getX();
                mDY = mLastY = event.getY();
                mDVaild = true;
                return true;
            case MotionEvent.ACTION_MOVE:
                if (mDVaild && (Math.abs(eX - mDX) > mTouchSlop || Math.abs(eY - mDY) > mTouchSlop)) {
                    // 点击无效
                    mDVaild = false;
                }
                if (!mCanDrag && Math.abs(eY - mDY) > mTouchSlop
                        && Math.abs(eY - mDY) > Math.abs(eX - mDX)) {
                    mCanDrag = true;
                    forceFinished();
                    stopHorizontalScrollLrc();
                    mScaleFactor = 1;
                    mWithLine = true;
                }
                if (mCanDrag) {
                    // 偏移量
                    float offset = eY - mLastY;
                    scrollBy(getScrollX(), -(int) offset);
                    int curRow = (getScrollY() + mRowHeight / 2) / mRowHeight;
                    curRow = Math.max(curRow, 0);
                    curRow = Math.min(curRow, mLrcRows.size() - 1);
                    seekTo(mLrcRows.get(curRow).getTime(), true);
                }
                mLastY = event.getY();
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mDVaild) {
                    if (mOnClickListener != null) {
                        mOnClickListener.onClick();
                    }
                } else {
                    if (mCanDrag) {
                        if (mLrcRows.size() > 0 && mCurRow < mLrcRows.size()
                                && mOnSeekChangeListener != null) {
                            mOnSeekChangeListener.onProgressChanged(mLrcRows.get(mCurRow).getTime());
                        }
                    }
                }
                if (getScrollY() < 0) {
                    smoothScrollTo(0, DURATION_RESET);
                } else {
                    int offset = (int) (mLrcRows.size() * mRowHeight - mPadding);
                    if (getScrollY() > offset) {
                        smoothScrollTo(offset, DURATION_RESET);
                    }
                }
                resetTouch();
                invalidate();
                return true;
        }
        return super.onTouchEvent(event);
    }

    private void resetTouch() {
        mDX = mDY = 0;
        mDVaild = mCanDrag = false;
        mWithLine = false;
    }

    @Override
    public void setLrcRows(List<LrcRow> lrcRows) {
        if (lrcRows != null) {
            forceFinished();
            mLastRow = -1;
            mCurRow = -1;
            mLrcRows.clear();
            mLrcRows.addAll(lrcRows);
            scrollTo(0, 0);
            invalidate();
        }
    }

    public void setLrcRows(List<LrcRow> lrcRows, int progress) {
        if (progress <= 0) {
            setLrcRows(lrcRows);
            return;
        }
        if (lrcRows != null) {
            forceFinished();
            mLastRow = -1;
            mCurRow = -1;
            mLrcRows.clear();
            mLrcRows.addAll(lrcRows);
            seekTo(progress, true);
            invalidate();
        }
    }

    @Override
    public void seekTo(int progress, boolean fromUser) {
        if (mLrcRows.size() <= 0) {
            return;
        }
        // 如果是由seekbar的进度改变触发 并且这时候处于拖动状态，则返回
        if (!fromUser && mCanDrag) {
            return;
        }
        int size = mLrcRows.size();
        for (int i = size - 1; i >= 0; i--) {
            LrcRow lrcRow = mLrcRows.get(i);
            if (progress >= lrcRow.getTime()) {
                if (mCurRow != i) {
                    mLastRow = mCurRow;
                    mCurRow = i;
                    handleProgress(lrcRow, fromUser);
                }
                break;
            }
        }
    }

    private void handleProgress(LrcRow lrcRow, boolean fromUser) {
        if (mCanDrag) {
            invalidate();
            return;
        }
        int dstY = mCurRow * mRowHeight;
        if (fromUser) {
            forceFinished();
            scrollTo(getScrollX(), dstY);
            invalidate();
        } else {
            smoothScrollTo(dstY, DURATION_AUTO_SCROLL);
            // 如果高亮歌词的宽度大于View的宽，就需要开启属性动画，让它水平滚动
            float textWidth = mPaintCur.measureText(lrcRow.getContent());
            if (textWidth > mWidth) {
                startHorizontalScrollLrc(mWidth - textWidth, (long) (lrcRow.getTotalTime() * 0.6));
            }
        }
    }

    @Override
    public void setLrcScale(float factor) {
        if (factor < 0 || factor > 1) {
            return;
        }
        factor = MIN_SCALE + (MAX_SCALE - MIN_SCALE) * factor;
        mSizeText = SIZE_TEXT_DEFAULT * factor;
        mSizeTextCur = SIZE_TEXT_CUR_DEFAULT * factor;
        mSizeTime = SIZE_TIME_DEFAULT * factor;
        mPadding = PADDING_DEFAULT * factor;
        mPaddingTime = PADDING_TIME_DEFAULT * factor;
        resetValues();
        forceFinished();
        if (mCurRow != -1) {
            scrollTo(getScrollX(), mCurRow * mRowHeight);
        }
        invalidate();
    }

    private void resetValues() {
        mPaint.setTextSize(mSizeText);
        mPaintCur.setTextSize(mSizeTextCur);
        mPaintLine.setTextSize(mSizeTime);
        mOffsetY = (int) (DimenUtils.getTextHeight(mPaintCur) / 2);
        mRowHeight = (int) (DimenUtils.getTextHeight(mPaintCur) + mPadding);
    }

    @Override
    public void reset() {
        forceFinished();
        mLastRow = -1;
        mCurRow = -1;
        mLrcRows.clear();
        scrollTo(0, 0);
        invalidate();
    }

    private void startHorizontalScrollLrc(float endX, long duration) {
        if (mAnimation == null) {
            initAnim();
        } else {
            stopHorizontalScrollLrc();
        }
        mAnimation.setFloatValues(0, endX);
        mAnimation.setDuration(duration);
        // 延迟执行属性动画
        mAnimation.setStartDelay((long) (duration * 0.3));
        mAnimation.start();
    }

    private void stopHorizontalScrollLrc() {
        if (mAnimation != null) {
            mTextOffsetX = 0;
            mAnimation.cancel();
        }
    }

    public void setOnClickListener(OnClickListener l) {
        this.mOnClickListener = l;
    }

    public void setOnSeekChangeListener(OnSeekChangeListener l) {
        this.mOnSeekChangeListener = l;
    }

    public interface OnClickListener {
        void onClick();
    }

    public interface OnSeekChangeListener {
        void onProgressChanged(int progress);
    }

    static class UpdateListener implements ValueAnimator.AnimatorUpdateListener {
        private final WeakReference<LrcView> reference;

        UpdateListener(LrcView view) {
            this.reference = new WeakReference<>(view);
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            LrcView view = reference.get();
            if (view == null || view.mContext == null
                    || view.mContext instanceof Activity && ((Activity) view.mContext).isFinishing()) {
                return;
            }
            view.mTextOffsetX = (float) animation.getAnimatedValue();
            view.invalidate();
        }
    }
}
