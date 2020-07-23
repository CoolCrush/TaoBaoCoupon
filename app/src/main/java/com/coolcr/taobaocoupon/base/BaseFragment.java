package com.coolcr.taobaocoupon.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

// abstract 抽象类
public abstract class BaseFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = loadRootView(inflater, container, savedInstanceState);
        initPresenter();
        loadData();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        release();
    }

    protected void release() {
        // 释放资源
    }

    protected void initPresenter() {
        // 创建Presenter
    }

    protected void loadData() {
        // 加载数据
    }

    protected View loadRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int resId = getRootViewResId();
        return inflater.inflate(resId, container, false);
    }

    protected abstract int getRootViewResId();

}
