package com.yellow5a5.candlesanimlib;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Weiwu on 16/2/24.
 */
public class CandlesAnimView extends View {


    private final static int WIDTH_DEFAULT = 180;
    private final static int HEIGHT_DEFAULT = 120;

    private Paint mPaint;

    private int mDensity;
    private int mHeight;
    private int mWidth;

    //控件中心坐标
    private int mCenterX;
    private int mCenterY;

    //蜡烛左下角坐标
    private int mFirCandleX;
    private int mFirCandleY;
    private int mSecCandleX;
    private int mSecCandleY;
    //蜡烛高度
    private int mFirCandleHeight;
    private int mSecCandleHeight;
    //蜡烛宽度
    private int mFirCandleWidth;
    private int mSecCandleWidth;

    //第一个蜡烛左右眼中心坐标
    private Point mFirEyeLPoint;
    private Point mFirEyeRPoint;
    //第二个蜡烛左右眼中心坐标
    private Point mSecEyeLPoint;
    private Point mSecEyeRPoint;
    //蜡烛眼睛半径
    private int mEyeRadius;
    //眼睛间隔距离
    private int mEyeDevide;

    //第二个蜡烛嘴巴坐标
    private Point mMouthPoint;
    //嘴巴圆半径
    private int mMouthRadius;

    //蜡烛芯
    private Point mCandlewickPoint;
    private int mCandlewickDegrees = 0;

    private boolean mIsInit = false;
    private boolean mIsFire = false;

    private ValueAnimator mCandlesAnimator;
    private ValueAnimator mInvalidateAnimator;


    public CandlesAnimView(Context context) {
        this(context, null);
    }

    public CandlesAnimView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CandlesAnimView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDensity = (int) getResources().getDisplayMetrics().density;
    }

    private void initParameter() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mFirEyeLPoint = new Point();
        mFirEyeRPoint = new Point();
        mSecEyeLPoint = new Point();
        mSecEyeRPoint = new Point();
        mCandlewickPoint = new Point();
        mMouthPoint = new Point();
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight() - 40;
        mCenterX = (int) (getTranslationX() + mWidth / 2);
        mCenterY = (int) (getTranslationY() + mHeight / 2) + 40;

        mFirCandleHeight = mHeight / 3 * 2;
        mSecCandleHeight = mHeight;
        mFirCandleWidth = mWidth / 5;
        mSecCandleWidth = mWidth / 5;

        //两个蜡烛左下角的坐标
        mFirCandleX = mCenterX - mWidth / 6;
        mFirCandleY = mCenterY + mHeight / 2;
        mSecCandleX = mCenterX + mWidth / 6;
        mSecCandleY = mFirCandleY;

        //眼睛间隔距离
        mEyeDevide = mFirCandleWidth / 3;
        //眼睛半径
        mEyeRadius = mFirCandleWidth / 12;
        //眼睛坐标
        mFirEyeLPoint.x = mFirCandleX - mEyeDevide / 2;
        mFirEyeLPoint.y = mFirCandleY - mFirCandleHeight / 4 * 3;

        mFirEyeRPoint.x = mFirCandleX + mEyeDevide / 2;
        mFirEyeRPoint.y = mFirCandleY - mFirCandleHeight / 4 * 3;

        //第二个蜡烛的眼睛坐标
        mSecEyeLPoint.x = mSecCandleX + mEyeDevide / 2;
        mSecEyeLPoint.y = mSecCandleY - mSecCandleHeight / 8 * 7;
        mSecEyeRPoint.x = mSecCandleX - mEyeDevide / 2;
        mSecEyeRPoint.y = mSecCandleY - mSecCandleHeight / 8 * 7;
        //嘴巴坐标
        mMouthPoint.x = mSecCandleX;
        mMouthPoint.y = mSecEyeLPoint.y + 30;
        mMouthRadius = 10;

        mCandlewickPoint.x = mSecCandleX;
        mCandlewickPoint.y = mSecCandleY - mSecCandleHeight - 50;
    }

    public void initAnim() {
        //刷新View
        mInvalidateAnimator = ValueAnimator.ofInt(0, 1).setDuration(16);
        mInvalidateAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mInvalidateAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                invalidate();
            }
        });
        mInvalidateAnimator.start();

        mCandlesAnimator = ValueAnimator.ofFloat(0, 4).setDuration(3000);
        mCandlesAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float zeroToOne = (float) animation.getAnimatedValue();
                if (zeroToOne <= 1.0f) {
                    mIsFire = true;
                    mFirCandleWidth = mWidth / 5 + (int) (zeroToOne * 40);
                    mFirCandleHeight = mHeight / 3 * 2 - (int) (zeroToOne * 30);

                    mEyeDevide = mFirCandleWidth / 3;
                    mFirEyeLPoint.x = mFirCandleX - mEyeDevide / 2;
                    mFirEyeLPoint.y = mFirCandleY - mFirCandleHeight / 4 * 3;

                    mFirEyeRPoint.x = mFirCandleX + mEyeDevide / 2;
                    mFirEyeRPoint.y = mFirCandleY - mFirCandleHeight / 4 * 3;

                    //蜡烛芯下拉
                    mCandlewickDegrees = (int) (180 * zeroToOne);
                } else if (zeroToOne <= 2.0f) {
                    zeroToOne = zeroToOne - 1.0f;
                    if (zeroToOne <= 0.2f) {
                        zeroToOne = 1.0f - 5 * zeroToOne;
                        mIsFire = false;
                        mFirCandleWidth = mWidth / 5 + (int) (zeroToOne * 40);
                        mFirCandleHeight = mHeight / 3 * 2 - (int) (zeroToOne * 30);
                        setEyePosition();
                        mCandlewickDegrees = (int) (180 * zeroToOne);
                    } else {
                        mFirCandleWidth = mWidth / 5;
                        mFirCandleHeight = mHeight / 3 * 2;
                        mEyeDevide = mFirCandleWidth / 3;
                        mFirEyeLPoint.x = mFirCandleX - mEyeDevide / 2;
                        mFirEyeLPoint.y = mFirCandleY - mFirCandleHeight / 4 * 3;

                        mFirEyeRPoint.x = mFirCandleX + mEyeDevide / 2;
                        mFirEyeRPoint.y = mFirCandleY - mFirCandleHeight / 4 * 3;
                        mCandlewickDegrees = 0;
                    }
                }

            }
        });
        mCandlesAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mCandlesAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mCandlesAnimator.start();
    }

    private void setEyePosition() {
        mEyeDevide = mFirCandleWidth / 3;
        mFirEyeLPoint.x = mFirCandleX - mEyeDevide / 2;
        mFirEyeLPoint.y = mFirCandleY - mFirCandleHeight / 4 * 3;

        mFirEyeRPoint.x = mFirCandleX + mEyeDevide / 2;
        mFirEyeRPoint.y = mFirCandleY - mFirCandleHeight / 4 * 3;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measureDimension(WIDTH_DEFAULT * mDensity, widthMeasureSpec);
        int height = measureDimension(HEIGHT_DEFAULT * mDensity, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    public int measureDimension(int defaultSize, int measureSpec) {
        int result;

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = defaultSize;   //UNSPECIFIED
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!mIsInit) {
            initParameter();
            initAnim();
            mIsInit = true;
        }
        drawFirCandle(canvas);
        drawSecCandle(canvas);
        //地面
        canvas.drawLine(mCenterX - mWidth / 2, mCenterY + mHeight / 2, mCenterX + mWidth / 2, mCenterY + mHeight / 2, mPaint);
    }

    public void drawFirCandle(Canvas canvas) {
        //绘制身体颜色
        if (!mIsFire) {
            mPaint.setColor(Color.WHITE);
        } else {
            mPaint.setColor(Color.parseColor("#55ff5777"));
        }
        mPaint.setStrokeWidth(5 * mDensity);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(mFirCandleX - mFirCandleWidth / 2, mFirCandleY - mFirCandleHeight, mFirCandleX + mFirCandleWidth / 2, mFirCandleY, mPaint);

        //绘制眼睛
        if (!mIsFire) {
            mPaint.setColor(Color.BLACK);
            canvas.drawCircle(mFirEyeLPoint.x, mFirEyeLPoint.y, mEyeRadius, mPaint);
            canvas.drawCircle(mFirEyeRPoint.x, mFirEyeRPoint.y, mEyeRadius, mPaint);
        } else {
            mPaint.setColor(Color.RED);
            mPaint.setStrokeWidth(10);
            canvas.drawLine(mFirEyeLPoint.x - 15, mFirEyeLPoint.y - 15, mFirEyeLPoint.x, mFirEyeLPoint.y, mPaint);
            canvas.drawLine(mFirEyeLPoint.x, mFirEyeLPoint.y, mFirEyeLPoint.x - 15, mFirEyeLPoint.y + 15, mPaint);
            canvas.drawLine(mFirEyeRPoint.x + 15, mFirEyeRPoint.y - 15, mFirEyeRPoint.x, mFirEyeRPoint.y, mPaint);
            canvas.drawLine(mFirEyeRPoint.x, mFirEyeRPoint.y, mFirEyeRPoint.x + 15, mFirEyeRPoint.y + 15, mPaint);
            mPaint.setStrokeWidth(5 * mDensity);
            mPaint.setColor(Color.BLACK);
        }

        //绘制边框
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(mFirCandleX - mFirCandleWidth / 2, mFirCandleY - mFirCandleHeight, mFirCandleX + mFirCandleWidth / 2, mFirCandleY, mPaint);

        //绘制蜡烛芯
        canvas.save();
        canvas.rotate(mCandlewickDegrees, mFirCandleX, mFirCandleY - mFirCandleHeight);
        canvas.drawLine(mFirCandleX, mFirCandleY - mFirCandleHeight, mFirCandleX, mFirCandleY - mFirCandleHeight - 50, mPaint);
        canvas.restore();
    }

    private void drawSecCandle(Canvas canvas) {
        //绘制身体颜色
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(mSecCandleX - mSecCandleWidth / 2, mSecCandleY - mSecCandleHeight, mSecCandleX + mSecCandleWidth / 2, mSecCandleY, mPaint);

        //绘制眼睛
        mPaint.setColor(Color.BLACK);
        canvas.drawCircle(mSecEyeLPoint.x, mSecEyeLPoint.y, mEyeRadius, mPaint);
        canvas.drawCircle(mSecEyeRPoint.x, mSecEyeRPoint.y, mEyeRadius, mPaint);

        //绘制嘴边
        canvas.drawCircle(mMouthPoint.x, mMouthPoint.y, mMouthRadius, mPaint);
        //绘制边框
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(mSecCandleX - mSecCandleWidth / 2, mSecCandleY - mSecCandleHeight, mSecCandleX + mSecCandleWidth / 2, mSecCandleY, mPaint);

        //绘制蜡烛芯
        canvas.drawLine(mSecCandleX, mSecCandleY - mSecCandleHeight, mCandlewickPoint.x, mCandlewickPoint.y, mPaint);
    }

}
