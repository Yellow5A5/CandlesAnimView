package com.yellow5a5.candlesanimlib;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.yellow5a5.candlesanimlib.Model.FirCandle;
import com.yellow5a5.candlesanimlib.Model.ICandle;
import com.yellow5a5.candlesanimlib.Model.SecCandle;

/**
 * Created by Weiwu on 16/2/29.
 */
public class AnimControler {

    private Paint mPaint;

    private ICandle mFirCandle;
    private ICandle mSecCandle;

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

    public AnimControler(int x, int y){
        mRelativeX = x;
        mRelativeY = y;
    }

    public void initControler(int width,int height){
        mWidth = width;
        mHeight = height;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
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
    }

    public void drawMyView(Canvas canvas){
        mFirCandle.drawSelf(canvas);
        mSecCandle.drawSelf(canvas);
        canvas.drawLine(mRelativeX, mRelativeY + mHeight, mRelativeX + mWidth, mRelativeY + mHeight, mPaint);
    }
}
