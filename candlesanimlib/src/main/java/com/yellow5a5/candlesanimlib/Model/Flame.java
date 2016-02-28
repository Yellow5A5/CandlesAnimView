package com.yellow5a5.candlesanimlib.Model;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.SweepGradient;

import java.util.Random;

/**
 * Created by Weiwu on 16/2/25.
 */
public class Flame {

    private static float CHANGE_FACTOR = 20;

    private Paint mPaint;
    private Path mPath;
    //左下点坐标
    private int mCurX;
    private int mCurY;
    //火焰宽度
    private int mWidth;
    //火焰高度
    private int mHeight;
    //记录初始高度
    private int mPreHeight;
    //火焰顶部贝塞尔曲线控制点变化参数
    private int mTopXFactor;
    private int mTopYFactor;
    //用于实现火焰的抖动
    private Random mRandom;
    //烟球坐标
    private Point mSmokePoint;
    //烟球半径
    private int mSmokeRadius;
    //光环半径
    private int mHaloRadius;
    //正在燃烧
    private boolean mIsFiring;
    private LinearGradient mLinearGradient;

    private ValueAnimator mFlameAnimator;


    public int getmCurY() {
        return mCurY;
    }

    public void setmCurY(int mCurY) {
        this.mCurY = mCurY;
    }

    public int getmCurX() {
        return mCurX;
    }

    public void setmCurX(int mCurX) {
        this.mCurX = mCurX;
    }

    public Flame(int x, int y) {
        mCurX = x;
        mCurY = y;
    }

    public void initConfig(int width, int height) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.GRAY);
        mPath = new Path();
        mRandom = new Random();
        mSmokePoint = new Point();
        mWidth = width;
        mHeight = 0;
        mPreHeight = height;
        mLinearGradient = new LinearGradient(mCurX + mWidth / 2, mCurY + mPreHeight / 3, mCurX + mWidth / 2, mCurY - mPreHeight / 3 * 4, Color.YELLOW, Color.RED, Shader.TileMode.REPEAT);
        mPaint.setShader(mLinearGradient);
        mSmokePoint.x = mCurX - 20;
        mSmokePoint.y = mCurY - 20;
        mHaloRadius = 70;
    }

    public void initAnim() {
        mFlameAnimator = ValueAnimator.ofFloat(0, 4).setDuration(3000);
        mFlameAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mFlameAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float zeroToOne = (float) animation.getAnimatedValue();
                if (mIsFiring) {
                    mHaloRadius = (int) (70 + zeroToOne % 1.0f * 20);
                }
                if (zeroToOne >= 1.0f && zeroToOne <= 1.2f) {
                    //火焰燃起
                    zeroToOne = 1.0f - 5 * (zeroToOne - 1.0f);//1-0
                    mHeight = (int) (mPreHeight * (1 - zeroToOne));
                    mIsFiring = true;
                } else if (zeroToOne >= 3.5f) {
                    //火焰被吹灭
                    zeroToOne = 2 * (zeroToOne - 3.0f);
                    mTopXFactor = (int) (10 * zeroToOne);
                    mTopYFactor = (int) (80 * zeroToOne);
                    mIsFiring = false;
                }
            }
        });
        mFlameAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                mTopXFactor = 0;
                mTopYFactor = 0;
                mHeight = 0;
            }
        });
        mFlameAnimator.start();
    }

    public void drawFlame(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setShader(mLinearGradient);
        mPath.reset();
        mPath.moveTo(mCurX, mCurY);
        mPath.quadTo(mCurX + mWidth / 2, mCurY + mHeight / 3, mCurX + mWidth, mCurY);
        mPath.quadTo(mCurX + mWidth / 2 + ((1 - mRandom.nextFloat()) * CHANGE_FACTOR) - mTopXFactor, mCurY - 2 * mHeight + mTopYFactor, mCurX, mCurY);

        canvas.drawPath(mPath, mPaint);

        if (mIsFiring) {
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(5);
            mPaint.setShader(new RadialGradient(mCurX + mWidth / 2, mCurY - mHeight / 2, mHaloRadius,
                    new int[]{Color.WHITE, Color.TRANSPARENT}, null, Shader.TileMode.REPEAT));
            canvas.drawCircle(mCurX + mWidth / 2, mCurY - mHeight / 2, mHaloRadius, mPaint);
            canvas.drawCircle(mCurX + mWidth / 2, mCurY - mHeight / 2, mHaloRadius - 3, mPaint);
            canvas.drawCircle(mCurX + mWidth / 2, mCurY - mHeight / 2, mHaloRadius - 6, mPaint);
        }
    }
}
