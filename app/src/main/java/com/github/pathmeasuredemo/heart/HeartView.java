package com.github.pathmeasuredemo.heart;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.github.pathmeasuredemo.R;

import java.util.Random;


/**
 * desc:心动画
 * author: wens
 * date: 2017/9/30.
 */

public class HeartView extends View {

    private SparseArray<Bitmap> mBitmapSparseArray = new SparseArray<>();

    private SparseArray<Heart> mHeartSparseArray;

    private Paint mPaint;

    private int mWidth;

    private int mHeight;

    private Matrix mMatrix;

    //动画时长
    private int mDuration;

    //最大速率
    private int mMaxRate;

    //是否控制速率
    private boolean mRateEnable;

    //是否控制透明度速率
    private boolean mAlphaEnable;

    //是否控制缩放
    private boolean mScaleEnable;

    //动画时长
    private final static int DURATION_TIME = 3000;

    //最大速率
    private final static int MAX_RATE = 2000;

    public HeartView(Context context) {
        this(context, null);
    }

    public HeartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mHeartSparseArray = new SparseArray<>();
        mMatrix = new Matrix();

        mDuration = DURATION_TIME;
        mMaxRate = MAX_RATE;
        mRateEnable = true;
        mAlphaEnable = true;
        mScaleEnable = true;

        initBitmap(context);
    }

    private void initBitmap(Context context) {
        Bitmap bitmap1 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.live_heart1);
        Bitmap bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.live_heart2);
        Bitmap bitmap3 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.live_heart3);
        Bitmap bitmap4 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.live_heart4);
        Bitmap bitmap5 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.live_heart5);
        Bitmap bitmap6 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.live_heart6);
        Bitmap bitmap7 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.live_heart7);
        mBitmapSparseArray.put(HeartType.BLUE, bitmap1);
        mBitmapSparseArray.put(HeartType.GREEN, bitmap2);
        mBitmapSparseArray.put(HeartType.YELLOW, bitmap3);
        mBitmapSparseArray.put(HeartType.PINK, bitmap4);
        mBitmapSparseArray.put(HeartType.BROWN, bitmap5);
        mBitmapSparseArray.put(HeartType.PURPLE, bitmap6);
        mBitmapSparseArray.put(HeartType.RED, bitmap7);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        //wrap_content情况-默认高度为200宽度100
        int defaultWidth = (int) dp2px(100);

        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(defaultWidth, defaultWidth * 3);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(defaultWidth, heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, defaultWidth * 3);
        } else {
            setMeasuredDimension(widthSpecSize, heightSpecSize);
        }
    }

    public void addHeart(int arrayIndex) {
        if (arrayIndex < 0 || arrayIndex > (mBitmapSparseArray.size() - 1)) return;
        Path path = new Path();
        final PathMeasure pathMeasure = new PathMeasure();
        final float pathLength;
        final Heart heart = new Heart();
        final int bitmapIndex = arrayIndex;

        //绘制三阶贝塞尔曲线
        PointF start = new PointF();//起点位置
        PointF control1 = new PointF(); //贝塞尔控制点
        PointF control2 = new PointF(); //贝塞尔控制点
        PointF end = new PointF(); //贝塞尔结束点

        initStartAndEnd(start, end);
        initControl(control1, control2);

        path.moveTo(start.x, start.y);
        path.cubicTo(control1.x, control1.y, control2.x, control2.y, end.x, end.y);

        pathMeasure.setPath(path, false);

        pathLength = pathMeasure.getLength();

        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1.0f);
        //先加速后减速
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        //动画的长短来控制速率
        animator.setDuration(mDuration + (mRateEnable ? (int) (Math.random() * mMaxRate) : 0));
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float fraction = valueAnimator.getAnimatedFraction();

                float[] pos = new float[2];
                pathMeasure.getPosTan(fraction * pathLength, pos, new float[2]);
                heart.setX(pos[0]);
                heart.setY(pos[1]);
                heart.setProgress(fraction);

                invalidate();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mHeartSparseArray.remove(heart.hashCode());
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mHeartSparseArray.put(heart.hashCode(), heart);
                heart.setIndex(bitmapIndex);
            }
        });
        animator.start();
    }

    public void addHeart() {
        addHeart(new Random().nextInt(mBitmapSparseArray.size() - 1));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mHeartSparseArray == null || mHeartSparseArray.size() == 0) return;

        canvasHeart(canvas);
    }

    private void canvasHeart(Canvas canvas) {
        for (int i = 0; i < mHeartSparseArray.size(); i++) {

            Heart heart = mHeartSparseArray.valueAt(i);

            //设置画笔透明度
            mPaint.setAlpha(mAlphaEnable ? (int) (255 * (1.0f - heart.getProgress())) : 255);

            //会覆盖掉之前的x,y数值
            mMatrix.setTranslate(0, 0);
            //位移到x,y
            mMatrix.postTranslate(heart.getX(), heart.getY());

            mMatrix.postScale(dealToScale(heart.getProgress()), dealToScale(heart.getProgress()),
                    mWidth / 2, mHeight);

            if (heart != null) {
                canvas.drawBitmap(mBitmapSparseArray.get(heart.getIndex()), mMatrix, mPaint);
            }

        }
    }

    private float dealToScale(float fraction) {
        if (fraction < 0.1f && mScaleEnable) {
            return 0.5f + fraction / 0.1f * 0.5f;
        }
        return 1.0f;
    }

    public void initControl(PointF control1, PointF control2) {
        control1.x = (float) (Math.random() * mWidth);
        control1.y = (float) (Math.random() * mHeight);

        control2.x = (float) (Math.random() * mWidth);
        control2.y = (float) (Math.random() * mHeight);

        if (control1.x == control2.x && control1.y == control2.y) {
            initControl(control1, control2);
        }
    }

    public void initStartAndEnd(PointF start, PointF end) {
        start.x = mWidth / 2;
        start.y = mHeight;

        end.x = mWidth / 2;
        end.y = 0;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDetachedFromWindow() {
        cancel();
        super.onDetachedFromWindow();
    }


    /**
     * 取消已有动画，释放资源
     */
    public void cancel() {
        //回收bitmap
        for (int i = 0; i < mBitmapSparseArray.size(); i++) {
            if (mBitmapSparseArray.valueAt(i) != null) {
                mBitmapSparseArray.valueAt(i).recycle();
            }
        }
    }

    public int getDuration() {
        return mDuration;
    }

    public void setDuration(int duration) {
        mDuration = duration;
    }

    public int getMaxRate() {
        return mMaxRate;
    }

    public void setMaxRate(int maxRate) {
        mMaxRate = maxRate;
    }

    public boolean getRateEnable() {
        return mRateEnable;
    }

    public void setRateEnable(boolean rateEnable) {
        mRateEnable = rateEnable;
    }

    public boolean getAlphaEnable() {
        return mAlphaEnable;
    }

    public void setAlphaEnable(boolean alphaEnable) {
        mAlphaEnable = alphaEnable;
    }

    public boolean getScaleEnable() {
        return mScaleEnable;
    }

    public void setScaleEnable(boolean scaleEnable) {
        mScaleEnable = scaleEnable;
    }

    private float dp2px(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

}
