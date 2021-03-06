package com.coolcr.taobaocoupon.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.coolcr.taobaocoupon.R;
import com.coolcr.taobaocoupon.base.BaseFragment;
import com.coolcr.taobaocoupon.model.domain.Categories;
import com.coolcr.taobaocoupon.presenter.IHomePresenter;
import com.coolcr.taobaocoupon.ui.activity.MainActivity;
import com.coolcr.taobaocoupon.ui.activity.ScanQrCodeActivity;
import com.coolcr.taobaocoupon.ui.adapter.HomePagerAdapter;
import com.coolcr.taobaocoupon.utils.LogUtils;
import com.coolcr.taobaocoupon.utils.PresenterManger;
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

    @BindView(R.id.home_search_input_box)
    EditText homeSearchInputBox;

    @BindView(R.id.scan_btn)
    ImageView scanBtn;
    @BindView(R.id.search_btn)
    ImageView searchBtn;

    private IHomePresenter mHomePresenter;
    private HomePagerAdapter mHomePagerAdapter;


    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_home;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtils.d(this, "onCreateView...");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        LogUtils.d(this, "onDestroy...");
        super.onDestroy();
    }

    @Override
    protected void initView(View rootView) {
        mTabLayout.setupWithViewPager(mHomePager);
        // ???ViewPager???????????????
        mHomePagerAdapter = new HomePagerAdapter(getChildFragmentManager());
        mHomePager.setAdapter(mHomePagerAdapter);
    }

    @Override
    protected void initPresenter() {
        // ??????Presenter
        mHomePresenter = PresenterManger.getInstance().getHomePresenter();
        mHomePresenter.registerViewCallback(this);
    }

    @Override
    protected void initListener() {
        homeSearchInputBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //?????????????????????
                FragmentActivity activity = getActivity();
                if (activity != null) {
                    ((MainActivity) activity).switch2Search();
                }
            }
        });
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ScanQrCodeActivity.class);
                startActivity(intent);
            }
        });
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //?????????????????????
                FragmentActivity activity = getActivity();
                if (activity != null) {
                    ((MainActivity) activity).switch2Search();
                }
            }
        });
    }

    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.base_home_fragment_layout, container, false);
    }

    @Override
    protected void loadData() {
        // ????????????
        mHomePresenter.getCategories();
    }

    @Override
    public void onCategoriesLoaded(Categories categories) {
        // ????????????????????????
        setUpState(State.SUCCESS);
        LogUtils.d(this, "onCategoriesLoaded...");
        // ????????????????????????????????????
        if (mHomePagerAdapter != null) {
            // ?????????????????????
            mHomePager.setOffscreenPageLimit(2);
            mHomePagerAdapter.setCategories(categories);
        }
    }

    @Override
    public void onError() {
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
        // ??????????????????
        if (mHomePresenter != null) {
            mHomePresenter.unregisterViewCallback(this);
        }
    }

    @Override
    protected void onRetryClick() {
        // ??????????????????????????????????????????
        // ??????????????????
        if (mHomePresenter != null) {
            mHomePresenter.getCategories();
        }
    }
}
