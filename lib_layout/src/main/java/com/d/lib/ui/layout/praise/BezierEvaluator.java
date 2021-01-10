package com.d.lib.ui.layout.praise;

import android.animation.TypeEvaluator;
import android.graphics.PointF;

/**
 * Created by D on 2016/11/8.
 */
class BezierEvaluator implements TypeEvaluator<PointF> {

    private PointF mPointF1;
    private PointF mPointF2;

    BezierEvaluator(PointF pointF1, PointF pointF2) {
        this.mPointF1 = pointF1;
        this.mPointF2 = pointF2;
    }

    @Override
    public PointF evaluate(float time, PointF startValue,
                           PointF endValue) {

        float timeLeft = 1.0f - time;
        PointF point = new PointF();

        point.x = timeLeft * timeLeft * timeLeft * (startValue.x)
                + 3 * timeLeft * timeLeft * time * (mPointF1.x)
                + 3 * timeLeft * time * time * (mPointF2.x)
                + time * time * time * (endValue.x);

        point.y = timeLeft * timeLeft * timeLeft * (startValue.y)
                + 3 * timeLeft * timeLeft * time * (mPointF1.y)
                + 3 * timeLeft * time * time * (mPointF2.y)
                + time * time * time * (endValue.y);
        return point;
    }
}
