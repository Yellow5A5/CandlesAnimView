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

    //监听动画结束
    private StopAnimListener mStopAnimListener;

    public interface StopAnimListener{
        //结束动画调用后启动的方法。
        public void OnAnimStop();
    }

    public void setStopAnimListener(StopAnimListener l){
        this.mStopAnimListener = l;
    }

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
    
    /*
     初始化
     */
    public void initConfig() {
        int relativeX = (int) getTranslationX();
        int relativeY = (int) getTranslationY();
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        mAnimControler = new AnimControler(relativeX,relativeY);
        mAnimControler.initControler(width,height);
        
        //16ms刷新Canvas
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

    /*
     调用Loading结束动画。
     */
    public void stopAnim(){
        mAnimControler.stopAnimation();
        if(mStopAnimListener != null){
            mStopAnimListener.OnAnimStop();
        }
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
            initConfig();
            mIsInit = true;
        }
        mAnimControler.drawMyView(canvas);
    }
}
