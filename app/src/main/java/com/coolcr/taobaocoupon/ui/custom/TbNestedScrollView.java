package com.coolcr.taobaocoupon.ui.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;

public class TbNestedScrollView extends NestedScrollView {

    private int mHeaderHeight = 250;
    private int originScroll = 0;

    public void setHeaderHeight(int headerHeight) {
        this.mHeaderHeight = headerHeight;
    }

    public TbNestedScrollView(@NonNull Context context) {
        super(context);
    }

    public TbNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TbNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 复写NestedScrollView滑动的一个方法
     *
     * @param target
     * @param dx
     * @param dy
     * @param consumed
     * @param type
     */
    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed,
                                  @ViewCompat.NestedScrollType int type) {
        // 滑动的距离
        //LogUtils.d(this, "dy == > " + dy);
        // 设置页面的滑动距离
        if (originScroll < mHeaderHeight) {
            scrollBy(dx, dy);
            consumed[0] = dx;
            consumed[1] = dy;
        }
        super.onNestedPreScroll(target, dx, dy, consumed, type);
    }

    /**
     * 界面的一个滑动方法
     *
     * @param l
     * @param t
     * @param oldl
     * @param oldt
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        this.originScroll = t;
        //LogUtils.d(this, "vertical -- >" + t);
        super.onScrollChanged(l, t, oldl, oldt);
    }
}
