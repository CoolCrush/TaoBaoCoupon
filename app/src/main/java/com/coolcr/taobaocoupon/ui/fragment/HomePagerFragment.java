package com.coolcr.taobaocoupon.ui.fragment;

import android.view.View;

import com.coolcr.taobaocoupon.R;
import com.coolcr.taobaocoupon.base.BaseFragment;

public class HomePagerFragment extends BaseFragment {
    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_home_pager;
    }

    @Override
    protected void initView(View rootView) {
        setUpState(State.SUCCESS);
    }
}
