# CandlesAnimView
---
&emsp;&emsp;动画设计想法来自于Gal Shir的GIF设计，右边的蜡烛吹灭左边的蜡烛，并不断循环作为Loading，加入了停止动画的接口，但是暂时没有想好结束动画应该用什么形式展示更自然，目前动画结束为停止蜡烛。

# DEMO
---
![](https://github.com/Yellow5A5/CandlesAnimView/blob/master/demo.gif)

# 结构
---
![](https://github.com/Yellow5A5/CandlesAnimView/blob/master/structure.png)

# API
---
&emsp;&emsp;暂时只写了结束动画的接口，但也加入了一些蜡烛动作的回调，如果有需要可以自行拓展其他方面的效果。

```
    //监听动画结束
    private StopAnimListener mStopAnimListener;

    public interface StopAnimListener{
        //结束动画调用后启动的方法。
        public void OnAnimStop();
    }

    public void setStopAnimListener(StopAnimListener l){
        this.mStopAnimListener = l;
    }
```

```
    /*
     调用Loading结束动画。
     */
    public void stopAnim(){
        mAnimControler.stopAnimation();
        if(mStopAnimListener != null){
            mStopAnimListener.OnAnimStop();
        }
    }
```

```
        mCandlesAnimView.setStopAnimListener(new CandlesAnimView.StopAnimListener() {
            @Override
            public void OnAnimStop() {
                Toast.makeText(MainActivity.this,"End Anim.",Toast.LENGTH_SHORT).show();
            }
        });
```

# 调用方式
---
 1. 配置XML

```
    <com.yellow5a5.candlesanimlib.CandlesAnimView
        android:id="@+id/candles_view"
        android:layout_centerInParent="true"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />
```

 2. Java代码创建
```
        CandlesAnimView mCandlesAnimView = new CandlesAnimView(MainActivity.this);
        //...addView..
```

# 其它
---
&emsp;&emsp;目前动画结束为停止吹熄蜡烛。适配上通过View本身的宽高的比例进行设置并不理想，可自行修改固定mHeight和mWidth的内部逻辑完善。