package com.yellow5a5.candlesanimlib.Model;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;

import java.util.Random;

/**
 * Created by Weiwu on 16/2/25.
 */
public class Flame {

    private Paint mPaint;
    private Path mPath;

    private int mCurX;
    private int mCurY;

    private float mSumWidth;
    private float mSumHeight;

    private boolean isFirstAnim = true;
    private ValueAnimator mLayerAnim;

    private Random mRandom;
    private float CHANGE_FACTOR = 20;

    public Flame(int x, int y) {
        mCurX = x;
        mCurY = y;
    }

    public void initConfig(int width, int height) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPath = new Path();
        mRandom = new Random();

        mSumWidth = width;
        mSumHeight = height;

        mPaint.setShader(new LinearGradient(mSumWidth / 2, mSumHeight / 3 * 4, mSumWidth / 2, 0, Color.YELLOW, Color.RED, Shader.TileMode.REPEAT));
    }

    public void initAnim() {

    }

    public void drawFlame(Canvas canvas) {
        mPath.reset();

        mPath.moveTo(mCurX + 0, mCurY);

        mPath.quadTo(mCurX + mSumWidth / 2, mCurY + mSumHeight / 3, mCurX + mSumWidth, mCurY);

        mPath.quadTo(mCurX + mSumWidth / 2 + ((1 - mRandom.nextFloat()) * CHANGE_FACTOR), mCurY - 2 * mSumHeight, mCurX, mCurY);

        canvas.drawPath(mPath, mPaint);
    }
}
