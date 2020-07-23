package com.coolcr.taobaocoupon.ui.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.coolcr.taobaocoupon.R;
import com.coolcr.taobaocoupon.base.BaseFragment;
import com.coolcr.taobaocoupon.model.domain.Categories;
import com.coolcr.taobaocoupon.presenter.IHomePresenter;
import com.coolcr.taobaocoupon.presenter.impl.HomePresenterImpl;
import com.coolcr.taobaocoupon.ui.adapter.HomePagerAdapter;
import com.coolcr.taobaocoupon.utils.LogUtils;
import com.coolcr.taobaocoupon.view.IHomeCallback;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment implements IHomeCallback {

    @BindView(R.id.home_indicator)
    TabLayout mTabLayout;

    @BindView(R.id.home_pager)
    ViewPager mHomePager;

    private IHomePresenter mHomePresenter;
    private HomePagerAdapter mHomePagerAdapter;


    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View rootView) {
        mTabLayout.setupWithViewPager(mHomePager);
        // 给ViewPager设置适配器
        mHomePagerAdapter = new HomePagerAdapter(getChildFragmentManager());
        mHomePager.setAdapter(mHomePagerAdapter);
    }

    @Override
    protected void initPresenter() {
        // 创建Presenter
        mHomePresenter = new HomePresenterImpl();
        mHomePresenter.registerCallback(this);
    }

    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.base_home_fragment_layout, container, false);
    }

    @Override
    protected void loadData() {
        // 加载数据
        mHomePresenter.getCategories();
    }

    @Override
    public void onCategoriesLoaded(Categories categories) {

        setUpState(State.SUCCESS);
        LogUtils.d(this, "onCategoriesLoaded...");
        // 加载的数据就会从这里回来
        if (mHomePagerAdapter != null) {
            mHomePagerAdapter.setCategories(categories);
        }
    }

    @Override
    public void onNetworkError() {
        setUpState(State.ERROR);
    }

    @Override
    public void onLoading() {
        setUpState(State.LOADING);
    }

    @Override
    public void onEmpty() {
        setUpState(State.EMPTY);
    }

    @Override
    protected void release() {
        // 取消回调注册
        if (mHomePresenter != null) {
            mHomePresenter.unregisterCallback(this);
        }
    }

    @Override
    protected void onRetryClick() {
        // 网络错误被点击，重新加载数据
        // 重新加载分类
        if (mHomePresenter != null) {
            mHomePresenter.getCategories();
        }
    }
}
