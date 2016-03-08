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

    private Flame mFlame;
    private boolean mIsFire = false;
    private boolean mIsStateOnStart = false;
    private boolean mIsStateOnEnd = false;
    private boolean mFlagStop = false;

    private ValueAnimator mCandlesAnimator;

    public interface FlameStateListener {
        public void flameStart();

        public void flameEnd();
    }

    private FlameStateListener mFlameStateListener;

    public void setFlameStateListener(FlameStateListener l) {
        mFlameStateListener = l;
    }

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
        mCandleColor = Color.WHITE;
        mEyeLPoint = new Point();
        mEyeRPoint = new Point();
        mCandlewickPoint = new Point();

        //两个蜡烛中心横坐标
        mCenterX = mCurX + mCandleWidth / 2;

        //眼睛半径
        mEyeRadius = 10;
        //眼睛间隔距离
        mEyeDevide = mCandleWidth / 3;
        //眼睛坐标
        mEyeLPoint.x = mCurX + mEyeDevide;
        mEyeLPoint.y = mCurY - mCandleHeight / 4 * 3;
        mEyeRPoint.x = mCurX + mEyeDevide * 2;
        mEyeRPoint.y = mEyeLPoint.y;

        mCandlewickPoint.x = mCenterX;
        mCandlewickPoint.y = mCurY - mCandleHeight;

        mFlame = new Flame(mCandlewickPoint.x - 25, mCandlewickPoint.y - 30);
        mFlame.initConfig(50, 100);
        mFlame.initAnim();
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
                    //蜡烛芯蓄力下拉
                    mIsFire = true;
                    mCandleWidth = mPreWidth + (int) (zeroToOne * 40);
                    mCandleHeight = mPreHeight - (int) (zeroToOne * 30);
                    mCandlewickDegrees = (int) (-60 + (180 + 60) * zeroToOne);
                    refreshEyePosition();
                } else if (zeroToOne <= 2.0f) {
                    zeroToOne = zeroToOne - 1.0f;
                    //蜡烛芯上摆
                    if (zeroToOne <= 0.2f) {
                        zeroToOne = 1.0f - 5 * zeroToOne;
                        mIsFire = false;
                        mCandleWidth = mPreWidth + (int) (zeroToOne * 40);
                        mCandleHeight = mPreHeight - (int) (zeroToOne * 30);
                        mCandlewickDegrees = (int) (180 * zeroToOne);
                    } else {
                        if (mFlameStateListener != null && !mIsStateOnStart) {
                            mFlameStateListener.flameStart();
                            mIsStateOnStart = true;
                        }
                        mCandleWidth = mPreWidth;
                        mCandleHeight = mPreHeight;
                        mCandlewickDegrees = 0;
                        if(mFlagStop){
                            mCandlesAnimator.cancel();
                        }
                    }
                    refreshEyePosition();
                } else if (zeroToOne >= 3.5f) {
                    //蜡烛芯被吹歪
                    zeroToOne = 2 * (zeroToOne - 3.5f);//0-1
                    mCandlewickDegrees = (int) (-60 * zeroToOne);
                    if(mFlameStateListener != null && !mIsStateOnEnd){
                        mFlameStateListener.flameEnd();
                        mIsStateOnEnd = true;
                    }
                }
            }
        });
        mCandlesAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mCandlesAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                mIsStateOnStart = false;
                mIsStateOnEnd = false;
                mFlame.setmCurX(mCandlewickPoint.x - 25);
                mCandlewickDegrees = -60;
                if(mIsAnimStoping){
                    mFlagStop = true;
                }
            }
        });
        mCandlesAnimator.start();
    }

    @Override
    public void stopAnim() {
        super.stopAnim();
        mFlame.stopFlame();
        mIsAnimStoping = true;
    }

    @Override
    public void drawSelf(Canvas canvas) {
        super.drawSelf(canvas);
        //绘制身体颜色
        if (!mIsFire) {
            mPaint.setColor(mCandleColor);
        } else {
            mPaint.setColor(Color.parseColor("#55ff5777"));
        }
        mPaint.setStrokeWidth(15);
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
        canvas.drawLine(mCandlewickPoint.x, mCandlewickPoint.y, mCandlewickPoint.x, mCandlewickPoint.y - 30, mPaint);
        //绘制火焰
        mFlame.drawFlame(canvas);
        canvas.restore();
    }

    private void refreshEyePosition() {
        mEyeDevide = mCandleWidth / 3;
        mEyeLPoint.y = mCurY - mCandleHeight / 4 * 3;
        mEyeRPoint.y = mEyeLPoint.y;
        mCandlewickPoint.y = mCurY - mCandleHeight;
    }
}
