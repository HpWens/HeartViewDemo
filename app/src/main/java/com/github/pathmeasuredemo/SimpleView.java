package com.github.pathmeasuredemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * desc:
 * author: wens
 * date: 2017/10/7.
 */

public class SimpleView extends View {

    Paint mPaint;

    Path mPath;

    PathMeasure mPathMeasure;

    public SimpleView(Context context) {
        this(context, null);
    }

    public SimpleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(8);
        mPaint.setColor(Color.RED);

        mPathMeasure = new PathMeasure();
        mPath = new Path();

        //水平绘制长为600的线段
        mPath.moveTo(800, 200);
        mPath.lineTo(200, 200);
        //绘制长为800的字段
        mPath.lineTo(200, 1000);

        mPathMeasure.setPath(mPath, false);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mPath, mPaint);
    }
}
