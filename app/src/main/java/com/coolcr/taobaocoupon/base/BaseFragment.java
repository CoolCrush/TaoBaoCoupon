package com.coolcr.taobaocoupon.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;

import com.coolcr.taobaocoupon.R;
import com.coolcr.taobaocoupon.utils.LogUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

// abstract 抽象类
public abstract class BaseFragment extends Fragment {

    private State currentState = State.NONE;
    private View mSuccessView;
    private View mLoadingView;
    private View mErrorView;
    private View mEmptyView;

    public enum State {
        NONE, LOADING, SUCCESS, ERROR, EMPTY
    }

    private Unbinder mBind;
    private FrameLayout mBaseContainer;

    @OnClick(R.id.network_error_tips)
    public void retry() {
        // 点击重新加载内容
        LogUtils.d(this, "on retry()...");
        onRetryClick();
    }

    /**
     * 如果子类fragment需要知道网络错误以后的点击，可重写此方法
     */
    protected void onRetryClick() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = loadRootView(inflater, container);
        mBaseContainer = rootView.findViewById(R.id.base_container);
        loadStatesView(inflater, container);
        loadSuccessView(inflater, container);
        mBind = ButterKnife.bind(this, rootView);
        initView(rootView);
        initListener();
        initPresenter();
        loadData();
        return rootView;
    }

    /**
     * 如果子类需要设置使事件,可覆盖次方法
     */
    protected void initListener() {

    }

    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.base_fragment_layout, container, false);
    }

    /**
     * 加载各种状态的view
     *
     * @param inflater
     * @param container
     */
    private void loadStatesView(LayoutInflater inflater, ViewGroup container) {
        // 成功的View
        mSuccessView = loadSuccessView(inflater, container);
        mBaseContainer.addView(mSuccessView);

        // Loading的View
        mLoadingView = loadLoadingView(inflater, container);
        mBaseContainer.addView(mLoadingView);

        // 错误页面
        mErrorView = loadErrorView(inflater, container);
        mBaseContainer.addView(mErrorView);

        // 内容为空的页面
        mEmptyView = loadEmptyView(inflater, container);
        mBaseContainer.addView(mEmptyView);

        // 首先全部不显示
        setUpState(State.NONE);
    }

    /**
     * 加载错误页面
     *
     * @param inflater
     * @param container
     */
    protected View loadErrorView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_error, container, false);
    }

    /**
     * 加载空页面
     *
     * @param inflater
     * @param container
     * @return
     */
    protected View loadEmptyView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_empty, container, false);
    }

    /**
     * 子类通过这个方法来切换状态
     *
     * @param state
     */
    public void setUpState(State state) {
        this.currentState = state;

        mSuccessView.setVisibility(currentState == State.SUCCESS ? View.VISIBLE : View.GONE);
        mLoadingView.setVisibility(currentState == State.LOADING ? View.VISIBLE : View.GONE);
        mErrorView.setVisibility(currentState == State.ERROR ? View.VISIBLE : View.GONE);
        mEmptyView.setVisibility(currentState == State.EMPTY ? View.VISIBLE : View.GONE);
    }

    /**
     * 加载Loading界面
     *
     * @param inflater
     * @param container
     * @return
     */
    protected View loadLoadingView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_loading, container, false);
    }

    protected void initView(View rootView) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mBind != null) {
            mBind.unbind();
        }
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

    protected View loadSuccessView(LayoutInflater inflater, ViewGroup container) {
        int resId = getRootViewResId();
        return inflater.inflate(resId, container, false);
    }

    protected abstract int getRootViewResId();

}
