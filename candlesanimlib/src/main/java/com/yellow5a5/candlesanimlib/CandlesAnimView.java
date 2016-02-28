package com.yellow5a5.candlesanimlib;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Weiwu on 16/2/24.
 */
public class CandlesAnimView extends View {

    private final static int WIDTH_DEFAULT = 180;
    private final static int HEIGHT_DEFAULT = 150;

    private AnimControler mAnimControler;

    private int mDensity;
    private boolean mIsInit = false;

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
        int relativeX = (int) getTranslationX();
        int relativeY = (int) getTranslationY();
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        mAnimControler = new AnimControler(relativeX,relativeY);
        mAnimControler.initControler(width,height);
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
            result = defaultSize;
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
        mAnimControler.drawMyView(canvas);
    }
}
