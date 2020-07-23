package com.coolcr.taobaocoupon.ui;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.coolcr.taobaocoupon.R;
import com.coolcr.taobaocoupon.base.BaseFragment;
import com.coolcr.taobaocoupon.ui.fragment.HomeFragment;
import com.coolcr.taobaocoupon.ui.fragment.RedPacketFragment;
import com.coolcr.taobaocoupon.ui.fragment.SearchFragment;
import com.coolcr.taobaocoupon.ui.fragment.SelectedFragment;
import com.coolcr.taobaocoupon.utils.LogUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {

    private View main_page_container;

    @BindView(R.id.main_navigation_bar)
    BottomNavigationView main_navigation_bar;
    private HomeFragment homeFragment;
    private SelectedFragment selectedFragment;
    private RedPacketFragment redPacketFragment;
    private SearchFragment searchFragment;
    private Unbinder mBind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBind = ButterKnife.bind(this);
        initFragment();
        initData();
        initListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBind != null) {
            mBind.unbind();
        }
    }

    private void initFragment() {
        homeFragment = new HomeFragment();
        selectedFragment = new SelectedFragment();
        redPacketFragment = new RedPacketFragment();
        searchFragment = new SearchFragment();
        switchFragment(homeFragment);
    }

    private void initData() {
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
                switchFragment(redPacketFragment);
            } else if (item.getItemId() == R.id.menu_search) {
                LogUtils.d(this, "切换到搜索");
                switchFragment(searchFragment);
            }
            return true;
        });
    }

    /**
     * 切换Fragment
     */
    private void switchFragment(BaseFragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        // 开始事务
        FragmentTransaction transaction = fm.beginTransaction();
        // 取代
        transaction.replace(R.id.main_page_container, fragment);
        // 提交事务
        transaction.commit();
    }
}
