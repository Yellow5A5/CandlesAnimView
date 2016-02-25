package com.yellow5a5.candlesanimlib;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.yellow5a5.candlesanimlib.Model.FirCandle;
import com.yellow5a5.candlesanimlib.Model.Flame;
import com.yellow5a5.candlesanimlib.Model.ICandle;
import com.yellow5a5.candlesanimlib.Model.SecCandle;

/**
 * Created by Weiwu on 16/2/24.
 */
public class CandlesAnimView extends View {


    private final static int WIDTH_DEFAULT = 180;
    private final static int HEIGHT_DEFAULT = 150;

    private Paint mPaint;
    private ICandle mFirCandle;
    private ICandle mSecCandle;
    private Flame mFlame;

    private int mWidth;
    private int mHeight;
    private int mDensity;

    private int mRelativeX;
    private int mRelativeY;

    //蜡烛高度
    private int mFirCandleHeight;
    private int mSecCandleHeight;
    //蜡烛宽度
    private int mFirCandleWidth;
    private int mSecCandleWidth;

    private boolean mIsInit = false;

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

        mRelativeX = (int) getTranslationX();
        mRelativeY = (int) getTranslationY();

        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();

        mFirCandleHeight = mHeight / 8 * 5;
        mFirCandleWidth = mWidth / 5;
        mSecCandleHeight = mHeight;
        mSecCandleWidth = mWidth / 5;

        mFirCandle = new FirCandle(mRelativeX + mWidth / 6, mRelativeY + mHeight);
        mFirCandle.initCandle(mFirCandleWidth, mFirCandleHeight);
        mFirCandle.initAnim();
        mSecCandle = new SecCandle(mRelativeX + mWidth / 2, mRelativeY + mHeight);
        mSecCandle.initCandle(mSecCandleWidth, mSecCandleHeight - 50);

        mFlame = new Flame(0,100);
        mFlame.initConfig(50,100);
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
        mFirCandle.drawSelf(canvas);
        mSecCandle.drawSelf(canvas);
        mFlame.drawFlame(canvas);
        //地面
        canvas.drawLine(mRelativeX, mRelativeY + mHeight, mRelativeX + mWidth, mRelativeY + mHeight, mPaint);
    }


}
