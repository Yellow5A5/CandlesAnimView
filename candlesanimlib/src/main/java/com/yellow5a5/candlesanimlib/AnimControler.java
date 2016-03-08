package com.yellow5a5.candlesanimlib;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.yellow5a5.candlesanimlib.Model.FirCandle;
import com.yellow5a5.candlesanimlib.Model.SecCandle;

/**
 * Created by Weiwu on 16/2/29.
 */
public class AnimControler {

    private Paint mPaint;

    private FirCandle mFirCandle;
    private SecCandle mSecCandle;

    private int mWidth;
    private int mHeight;

    private int mRelativeX;
    private int mRelativeY;

    //蜡烛高度
    private int mFirCandleHeight;
    private int mSecCandleHeight;
    //蜡烛宽度
    private int mFirCandleWidth;
    private int mSecCandleWidth;

    private boolean mIsNight;

    public AnimControler(int x, int y) {
        mRelativeX = x;
        mRelativeY = y;
    }

    public void initControler(int width, int height) {
        mWidth = width;
        mHeight = height;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(15);

        mFirCandleHeight = mHeight / 8 * 5;
        mFirCandleWidth = mWidth / 5;
        mSecCandleHeight = mHeight;
        mSecCandleWidth = mWidth / 5;

        mFirCandle = new FirCandle(mRelativeX + mWidth / 6, mRelativeY + mHeight);
        mFirCandle.initCandle(mFirCandleWidth, mFirCandleHeight);
        mFirCandle.initAnim();
        mSecCandle = new SecCandle(mRelativeX + mWidth / 2, mRelativeY + mHeight);
        mSecCandle.initCandle(mSecCandleWidth, mSecCandleHeight - 80);
        mSecCandle.initAnim();

        mFirCandle.setFlameStateListener(new FirCandle.FlameStateListener() {
            @Override
            public void flameStart() {
                mIsNight = false;
                //Maybe you can add the other animation or event.
            }
            @Override
            public void flameEnd() {
                mIsNight = true;
            }
        });
    }

    public void stopAnimation(){
        mFirCandle.stopAnim();
        mSecCandle.stopAnim();
    }

    public void drawMyView(Canvas canvas) {
        mPaint.setColor(Color.BLACK);
        mFirCandle.drawSelf(canvas);

        mSecCandle.drawSelf(canvas);
        canvas.drawLine(mRelativeX, mRelativeY + mHeight, mRelativeX + mWidth, mRelativeY + mHeight, mPaint);
    }
}
