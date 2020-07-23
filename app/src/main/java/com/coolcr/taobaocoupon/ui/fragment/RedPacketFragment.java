package com.coolcr.taobaocoupon.ui.fragment;

import android.view.View;

import androidx.fragment.app.Fragment;

import com.coolcr.taobaocoupon.R;
import com.coolcr.taobaocoupon.base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class RedPacketFragment extends BaseFragment {

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_red_packet;
    }

    @Override
    protected void initView(View rootView) {
        setUpState(State.SUCCESS);
    }
}
