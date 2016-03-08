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
public class SecCandle extends ICandle {


    private Paint mPaint;

    private int mCenterX;

    //记录初始宽
    private int mPreWidth;
    //记录初始高
    private int mPreHeight;
    //第二个蜡烛嘴巴坐标
    private Point mMouthPoint;
    //嘴巴圆半径
    private int mMouthRadius;

    private ValueAnimator mCandlesAnimator;

    public SecCandle(int x, int y) {
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
        mPaint.setStrokeWidth(15);
        mCandleColor = Color.WHITE;
        mEyeLPoint = new Point();
        mEyeRPoint = new Point();
        mCandlewickPoint = new Point();
        mMouthPoint = new Point();

        //两个蜡烛中心横坐标
        mCenterX = mCurX + mCandleWidth / 2;
        //眼睛半径
        mEyeRadius = 10;
        //眼睛间隔距离
        mEyeDevide = mCandleWidth / 3;
        //眼睛坐标
        mEyeLPoint.x = mCurX + mEyeDevide;
        mEyeLPoint.y = mCurY - mCandleHeight / 10 * 9;
        mEyeRPoint.x = mCurX + mEyeDevide * 2;
        mEyeRPoint.y = mEyeLPoint.y;

        mMouthPoint.x = mCenterX;
        mMouthPoint.y = mCurY - mCandleHeight / 10 * 8;

        mCandlewickPoint.x = mCenterX;
        mCandlewickPoint.y = mCurY - mCandleHeight;
    }

    @Override
    public void initAnim() {
        super.initAnim();
        mCandlesAnimator = ValueAnimator.ofFloat(0, 4).setDuration(3000);
        mCandlesAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float zeroToOne = (float) animation.getAnimatedValue();
                if (zeroToOne >= 2.0f) {
                    zeroToOne = (zeroToOne - 2.0f) / 2;

                    if (zeroToOne <= 0.5f) {
                        zeroToOne = 2 * zeroToOne;//0-1
                        mMouthPoint.x = (int) (mCenterX - 20 * zeroToOne);
                    } else if (zeroToOne <= 0.75f) {
                        return;
                    } else if (zeroToOne <= 1.0f) {
                        zeroToOne = (2 - 2 * zeroToOne);//1-0
                    }
                    int offsetX = (int) (30 * zeroToOne);
                    mCandleWidth = (int) (mPreWidth + 30 * zeroToOne);
                    mCandleHeight = (int) (mPreHeight + 30 * zeroToOne);
                    mMouthRadius = (int) (20 * zeroToOne);
                    refreshPosition(offsetX);
                }
            }
        });
        mCandlesAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                if(mIsAnimStoping){
                    mCandlesAnimator.cancel();
                }
            }
        });
        mCandlesAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mCandlesAnimator.start();
    }

    @Override
    public void stopAnim() {
        super.stopAnim();
        mIsAnimStoping = true;
    }

    @Override
    public void drawSelf(Canvas canvas) {
        super.drawSelf(canvas);
        //绘制身体颜色
        mPaint.setColor(mCandleColor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(mCenterX - mCandleWidth / 2, mCurY - mCandleHeight, mCenterX + mCandleWidth / 2, mCurY, mPaint);

        //绘制眼睛
        mPaint.setColor(Color.BLACK);
        canvas.drawCircle(mEyeLPoint.x, mEyeLPoint.y, mEyeRadius, mPaint);
        canvas.drawCircle(mEyeRPoint.x, mEyeRPoint.y, mEyeRadius, mPaint);

        //绘制嘴边
        canvas.drawCircle(mMouthPoint.x, mMouthPoint.y, mMouthRadius, mPaint);
        //绘制边框
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(mCenterX - mCandleWidth / 2, mCurY - mCandleHeight, mCenterX + mCandleWidth / 2, mCurY, mPaint);

        //绘制蜡烛芯
        canvas.drawLine(mCandlewickPoint.x, mCandlewickPoint.y, mCandlewickPoint.x, mCandlewickPoint.y - 30, mPaint);
    }

    private void refreshPosition(int offset) {
        //眼睛间隔距离
        mEyeDevide = mCandleWidth / 3;
        //眼睛坐标
        mEyeLPoint.x = mCurX + mEyeDevide - offset;
        mEyeLPoint.y = mCurY - mCandleHeight / 10 * 9;
        mEyeRPoint.x = mCurX + mEyeDevide * 2 - offset;
        mEyeRPoint.y = mEyeLPoint.y;

        mMouthPoint.y = mCurY - mCandleHeight / 10 * 8;
        mCandlewickPoint.y = mCurY - mCandleHeight;
    }
}
