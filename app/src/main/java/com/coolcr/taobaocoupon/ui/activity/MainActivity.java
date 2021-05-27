package com.coolcr.taobaocoupon.ui.activity;

import android.view.View;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.coolcr.taobaocoupon.R;
import com.coolcr.taobaocoupon.base.BaseActivity;
import com.coolcr.taobaocoupon.base.BaseFragment;
import com.coolcr.taobaocoupon.ui.fragment.HomeFragment;
import com.coolcr.taobaocoupon.ui.fragment.OnSellFragment;
import com.coolcr.taobaocoupon.ui.fragment.SearchFragment;
import com.coolcr.taobaocoupon.ui.fragment.SelectedFragment;
import com.coolcr.taobaocoupon.utils.LogUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements IMainActivity {

    private View main_page_container;

    @BindView(R.id.main_navigation_bar)
    BottomNavigationView main_navigation_bar;

    private HomeFragment homeFragment;
    private SelectedFragment selectedFragment;
    private OnSellFragment mOnSellFragment;
    private SearchFragment searchFragment;
    private FragmentManager mFm;

    @Override
    protected void initView() {
        initFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    private void initFragment() {
        homeFragment = new HomeFragment();
        selectedFragment = new SelectedFragment();
        mOnSellFragment = new OnSellFragment();
        searchFragment = new SearchFragment();
        mFm = getSupportFragmentManager();
        switchFragment(homeFragment);
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initEven() {
        initListener();
    }

    private void initListener() {
        main_navigation_bar.setOnNavigationItemSelectedListener(item -> {
            LogUtils.d(MainActivity.class, "title -->" + item.getTitle() + "id -->" + item.getItemId());
            if (item.getItemId() == R.id.menu_home) {
                LogUtils.d(this, "切换到首页");
                switchFragment(homeFragment);
            } else if (item.getItemId() == R.id.menu_selected) {
                LogUtils.d(this, "切换到精选");
                switchFragment(selectedFragment);
            } else if (item.getItemId() == R.id.menu_packet) {
                LogUtils.d(this, "切换到特惠");
                switchFragment(mOnSellFragment);
            } else if (item.getItemId() == R.id.menu_search) {
                LogUtils.d(this, "切换到搜索");
                switchFragment(searchFragment);
            }
            return true;
        });
    }

    /**
     * 上一次显示的fragment
     */
    private BaseFragment lastOneFragment = null;

    /**
     * 切换Fragment
     */
    private void switchFragment(BaseFragment targetFragment) {
        //如果点击的fragment是上一个，则不进行切换
        if (lastOneFragment == targetFragment) {
            return;
        }
        // 修改成add和hide的方式控制Fragment的切换
        // 开始事务
        FragmentTransaction transaction = mFm.beginTransaction();
        if (!targetFragment.isAdded()) {
            transaction.add(R.id.main_page_container, targetFragment);
        } else {
            transaction.show(targetFragment);
        }
        if (lastOneFragment != null) {
            transaction.hide(lastOneFragment);
        }
        lastOneFragment = targetFragment;
        // 取代
        //transaction.replace(R.id.main_page_container, targetFragment);
        // 提交事务
        transaction.commit();
    }

    @Override
    public void switch2Search() {
        main_navigation_bar.setSelectedItemId(R.id.menu_search);
    }
}
