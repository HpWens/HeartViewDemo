package com.github.pathmeasuredemo;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;


/**
 * desc:
 * author: wens
 * date: 2017/10/8.
 */

public class ArrowView extends View {

    Paint mPaint;

    Path mPath;

    PathMeasure mPathMeasure;

    float mPathLength;

    float mAnimatedValue;

    float[] pos = new float[2];

    float[] tan = new float[2];

    public ArrowView(Context context) {
        this(context, null);
    }

    public ArrowView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArrowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(6);
        mPaint.setColor(Color.RED);

        mPathMeasure = new PathMeasure();
        mPath = new Path();
        mPath.addCircle(0, 0, 200, Path.Direction.CW);

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

        mPathMeasure.getPosTan(mAnimatedValue * mPathLength, pos, tan);
        float degrees = (float) (Math.atan2(tan[1], tan[0]) * 180.0 / Math.PI);

        canvas.save();
        canvas.translate(400, 400);
        mPaint.setColor(Color.RED);
        canvas.drawPath(mPath, mPaint);

        //绘制箭头
        Path path = new Path(); //不建议在onDraw中直接new一个对象
        path.moveTo(40 * (float) Math.cos(-30 * Math.PI / 180f), 200 + 40 * (float) Math.sin(-30 * Math.PI / 180f));
        path.lineTo(0, 200);
        path.lineTo(40 * (float) Math.cos(30 * Math.PI / 180f), 200 + 40 * (float) Math.sin(30 * Math.PI / 180f));

        canvas.rotate(degrees);
        mPaint.setColor(Color.GREEN);
        canvas.drawPath(path, mPaint);

        canvas.restore();
    }
}
