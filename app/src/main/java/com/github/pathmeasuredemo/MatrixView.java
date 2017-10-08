package com.github.pathmeasuredemo;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
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

public class MatrixView extends View {

    Paint mPaint;

    Path mPath;

    PathMeasure mPathMeasure;

    Matrix mMatrix;

    float mPathLength;

    float mAnimatedValue;

    Bitmap mBitmap;

    public MatrixView(Context context) {
        this(context, null);
    }

    public MatrixView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MatrixView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
        mMatrix = new Matrix();
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_matrix);

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

        mMatrix.reset();
        mPathMeasure.getMatrix(mAnimatedValue * mPathLength, mMatrix,
                PathMeasure.POSITION_MATRIX_FLAG | PathMeasure.TANGENT_MATRIX_FLAG);
        canvas.translate(400, 400);
        canvas.drawBitmap(mBitmap, mMatrix, mPaint);
        canvas.drawPath(mPath, mPaint);
    }
}
