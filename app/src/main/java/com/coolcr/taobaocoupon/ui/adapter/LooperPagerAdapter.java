package com.coolcr.taobaocoupon.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.coolcr.taobaocoupon.model.domain.HomePagerContent;
import com.coolcr.taobaocoupon.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

public class LooperPagerAdapter extends PagerAdapter {

    private List<HomePagerContent.DataBean> mDataBeans = new ArrayList<>();

    public int getDataSize() {
        return mDataBeans.size();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        // 处理position越界问题
        int realPosition = position % mDataBeans.size();
        HomePagerContent.DataBean dataBean = mDataBeans.get(realPosition);
        int measuredHeight = container.getMeasuredHeight();
        int measuredWidth = container.getMeasuredWidth();
        //LogUtils.d(this, "measuredHeight -- > " + measuredHeight);
        //LogUtils.d(this, "measuredWidth -- > " + measuredWidth);
        int imgSize = Math.max(measuredHeight, measuredWidth) / 2;
        String coverUrl = UrlUtils.getCoverPath(dataBean.getPict_url(), imgSize);
        //LogUtils.d(this, "coverUrl -- >" + coverUrl);
        ImageView img = new ImageView(container.getContext());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        img.setLayoutParams(layoutParams);
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(container.getContext()).load(coverUrl).into(img);
        container.addView(img);
        return img;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    public void setData(List<HomePagerContent.DataBean> contents) {
        mDataBeans.clear();
        mDataBeans.addAll(contents);
        notifyDataSetChanged();
    }
}
