package com.coolcr.taobaocoupon.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.coolcr.taobaocoupon.R;
import com.coolcr.taobaocoupon.utils.LogUtils;

/**
 * 自动轮播
 */
public class AutoLoopViewPager extends ViewPager {

    // 切换间隔时长
    public static final long DEFAULT_DURATION = 3000;

    private long mDuration = DEFAULT_DURATION;

    /**
     * 设置切换时长
     *
     * @param duration 时长，单位：毫秒
     */
    public void setDuration(int duration) {
        this.mDuration = duration;
    }

    public AutoLoopViewPager(@NonNull Context context) {
        super(context, null);
    }

    public AutoLoopViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        // 读取属性
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AutoLoopStyle);
        // 获取属性
        mDuration = typedArray.getInteger(R.styleable.AutoLoopStyle_duration, (int) DEFAULT_DURATION);
        LogUtils.d(this, "mDuration -- > " + mDuration);
        // 回收
        typedArray.recycle();
    }

    private boolean isLoop = false;

    public void startLoop() {
        isLoop = true;
        post(mTask);
    }

    private Runnable mTask = new Runnable() {
        @Override
        public void run() {
            // 先拿到当前的位置
            int currentItem = getCurrentItem();
            currentItem++;
            setCurrentItem(currentItem);
            // LogUtils.d(this, "looping...");
            if (isLoop) {
                // 三秒钟变一次
                postDelayed(this, mDuration);
            }
        }
    };

    public void stopLoop() {
        removeCallbacks(mTask);
        isLoop = false;
    }
}
