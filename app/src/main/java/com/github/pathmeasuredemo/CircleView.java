package com.github.pathmeasuredemo;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * desc:
 * author: wens
 * date: 2017/10/7.
 */

public class CircleView extends View {

    Paint mPaint;

    Path mPath;

    Path mDstPath;

    PathMeasure mPathMeasure;

    float mPathLength;

    float mAnimatedValue;

    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2);
        mPaint.setColor(Color.RED);

        mPathMeasure = new PathMeasure();
        mPath = new Path();
        mDstPath = new Path();

        //圆弧路径
        mPath.addArc(new RectF(200, 200, 600, 600), 0, 359.6f);

        //绘制五角星
        for (int i = 1; i < 6; i++) {
            Point p = getPoint(200, -144 * i);
            mPath.lineTo(400 + p.x, 400 + p.y);
        }

        mPath.close();

        mPathMeasure.setPath(mPath, false);

        mPathLength = mPathMeasure.getLength();

        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1.0f);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(3000);
        animator.setRepeatCount(-1);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mAnimatedValue = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        animator.start();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mDstPath.reset();
        // 硬件加速的BUG
        mDstPath.lineTo(0, 0);

        //获取到路径片段
        mPathMeasure.getSegment(0, mAnimatedValue * mPathLength, mDstPath, true);
        canvas.drawPath(mDstPath, mPaint);
    }

    private Point getPoint(float radius, float angle) {
        float x = (float) ((radius) * Math.cos(angle * Math.PI / 180f));
        float y = (float) ((radius) * Math.sin(angle * Math.PI / 180f));
        Point p = new Point(x, y);
        return p;
    }

    private class Point {
        private float x;
        private float y;

        private Point(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }
}
