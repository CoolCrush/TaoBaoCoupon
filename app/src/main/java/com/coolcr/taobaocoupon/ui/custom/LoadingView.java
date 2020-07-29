package com.coolcr.taobaocoupon.ui.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.coolcr.taobaocoupon.R;

public class LoadingView extends androidx.appcompat.widget.AppCompatImageView {

    private float mDegrees = 30;

    private float changeDegrees = 10;

    private int delayMillis = 10;

    private boolean isRotate = true;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setImageResource(R.mipmap.loading);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //LogUtils.d(this, ":onAttachedToWindow...");
        startRotate();
    }

    private void startRotate() {
        isRotate = true;
        post(new Runnable() {
            @Override
            public void run() {
                mDegrees += changeDegrees;
                invalidate();
                //LogUtils.d(LoadingView.this, "rotate...");
                // 判断是否继续旋转
                // 如果不可见，或者已DetachedFromWindow就不转了
                if (getVisibility() != VISIBLE && !isRotate) {
                    removeCallbacks(this);
                } else {
                    postDelayed(this, delayMillis);
                }
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //LogUtils.d(this, ":onDetachedFromWindow...");
        stopRotate();
    }

    private void stopRotate() {
        isRotate = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.rotate(mDegrees, getWidth() / 2, getHeight() / 2);
        super.onDraw(canvas);
    }
}
