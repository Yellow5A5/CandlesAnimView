package com.yellow5a5.candlesanimlib.Model;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

/**
 * Created by Weiwu on 16/2/24.
 */
public class FirCandle extends ICandle {

    private Paint mPaint;

    //中心X坐标
    private int mCenterX;
    //记录初始宽
    private int mPreWidth;
    //记录初始高
    private int mPreHeight;
    //蜡烛芯旋转角
    private int mCandlewickDegrees = 0;

    private boolean mIsFire = false;

    private ValueAnimator mCandlesAnimator;

    public FirCandle(int x, int y) {
        super(x, y);
    }

    @Override
    public void initCandle(int width, int height) {
        super.initCandle(width, height);
        mPreWidth = width;
        mPreHeight = height;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mEyeLPoint = new Point();
        mEyeRPoint = new Point();
        mCandlewickPoint = new Point();

        //两个蜡烛中心横坐标
        mCenterX = mCurX + mCandleWidth / 2;

        //眼睛半径
        mEyeRadius = mCandleWidth / 12;
        //眼睛间隔距离
        mEyeDevide = mCandleWidth / 3;
        //眼睛坐标
        mEyeLPoint.x = mCurX + mEyeDevide;
        mEyeLPoint.y = mCurY - mCandleHeight / 4 * 3;
        mEyeRPoint.x = mCurX + mEyeDevide * 2;
        mEyeRPoint.y = mEyeLPoint.y;
    }

    @Override
    public void initAnim() {
        super.initAnim();
        mCandlesAnimator = ValueAnimator.ofFloat(0, 4).setDuration(3000);
        mCandlesAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float zeroToOne = (float) animation.getAnimatedValue();
                if (zeroToOne <= 1.0f) {
                    mIsFire = true;
                    mCandleWidth = mPreWidth + (int) (zeroToOne * 40);
                    mCandleHeight = mPreHeight - (int) (zeroToOne * 30);
                    setEyePosition();
                    //蜡烛芯下拉
                    mCandlewickDegrees = (int) (180 * zeroToOne);
                } else if (zeroToOne <= 2.0f) {
                    zeroToOne = zeroToOne - 1.0f;
                    if (zeroToOne <= 0.2f) {
                        zeroToOne = 1.0f - 5 * zeroToOne;
                        mIsFire = false;
                        mCandleWidth = mPreWidth + (int) (zeroToOne * 40);
                        mCandleHeight = mPreHeight - (int) (zeroToOne * 30);
                        setEyePosition();
                        mCandlewickDegrees = (int) (180 * zeroToOne);
                    } else {
                        mCandleWidth = mPreWidth;
                        mCandleHeight = mPreHeight;
                        setEyePosition();
                        mCandlewickDegrees = 0;
                    }
                }

            }
        });
        mCandlesAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mCandlesAnimator.start();
    }

    @Override
    public void drawSelf(Canvas canvas) {
        super.drawSelf(canvas);
        //绘制身体颜色
        if (!mIsFire) {
            mPaint.setColor(Color.WHITE);
        } else {
            mPaint.setColor(Color.parseColor("#55ff5777"));
        }
        mPaint.setStrokeWidth(5 * 3);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(mCenterX - mCandleWidth / 2, mCurY - mCandleHeight, mCenterX + mCandleWidth / 2, mCurY, mPaint);

        //绘制眼睛
        if (!mIsFire) {
            mPaint.setColor(Color.BLACK);
            canvas.drawCircle(mEyeLPoint.x, mEyeLPoint.y, mEyeRadius, mPaint);
            canvas.drawCircle(mEyeRPoint.x, mEyeRPoint.y, mEyeRadius, mPaint);
        } else {
            mPaint.setColor(Color.RED);
            mPaint.setStrokeWidth(10);
            canvas.drawLine(mEyeLPoint.x - 15, mEyeLPoint.y - 15, mEyeLPoint.x, mEyeLPoint.y, mPaint);
            canvas.drawLine(mEyeLPoint.x, mEyeLPoint.y, mEyeLPoint.x - 15, mEyeLPoint.y + 15, mPaint);
            canvas.drawLine(mEyeRPoint.x + 15, mEyeRPoint.y - 15, mEyeRPoint.x, mEyeRPoint.y, mPaint);
            canvas.drawLine(mEyeRPoint.x, mEyeRPoint.y, mEyeRPoint.x + 15, mEyeRPoint.y + 15, mPaint);
            mPaint.setStrokeWidth(5 * 3);
            mPaint.setColor(Color.BLACK);
        }

        //绘制边框
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(mCenterX - mCandleWidth / 2, mCurY - mCandleHeight, mCenterX + mCandleWidth / 2, mCurY, mPaint);

        //绘制蜡烛芯
        canvas.save();
        canvas.rotate(mCandlewickDegrees, mCenterX, mCurY - mCandleHeight);
        canvas.drawLine(mCenterX, mCurY - mCandleHeight, mCenterX, mCurY - mCandleHeight - 50, mPaint);
        canvas.restore();
    }


    private void setEyePosition() {
        mEyeDevide = mCandleWidth / 3;
        mEyeLPoint.y = mCurY - mCandleHeight / 4 * 3;
        mEyeRPoint.y = mEyeLPoint.y;
    }
}