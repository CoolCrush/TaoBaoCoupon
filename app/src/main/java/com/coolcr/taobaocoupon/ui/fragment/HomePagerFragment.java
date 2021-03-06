package com.coolcr.taobaocoupon.ui.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.coolcr.taobaocoupon.R;
import com.coolcr.taobaocoupon.base.BaseFragment;
import com.coolcr.taobaocoupon.model.domain.Categories;
import com.coolcr.taobaocoupon.model.domain.HomePagerContent;
import com.coolcr.taobaocoupon.model.domain.IBaseInfo;
import com.coolcr.taobaocoupon.presenter.ICategoryPagerPresenter;
import com.coolcr.taobaocoupon.ui.adapter.LinearItemContentAdapter;
import com.coolcr.taobaocoupon.ui.adapter.LooperPagerAdapter;
import com.coolcr.taobaocoupon.ui.custom.AutoLoopViewPager;
import com.coolcr.taobaocoupon.utils.Constants;
import com.coolcr.taobaocoupon.utils.LogUtils;
import com.coolcr.taobaocoupon.utils.PresenterManger;
import com.coolcr.taobaocoupon.utils.SizeUtils;
import com.coolcr.taobaocoupon.utils.TicketUtil;
import com.coolcr.taobaocoupon.utils.ToastUtil;
import com.coolcr.taobaocoupon.view.ICategoryPagerCallback;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.views.TbNestedScrollView;

import java.util.List;

import butterknife.BindView;

public class HomePagerFragment extends BaseFragment implements ICategoryPagerCallback, LinearItemContentAdapter.OnListItemClickListener, LooperPagerAdapter.OnLooperPagerItemClickListener {

    private ICategoryPagerPresenter mCategoryPagerPresenter;
    private int mMaterialId;
    private LinearItemContentAdapter mContentAdapter;
    private LooperPagerAdapter mLooperPagerAdapter;

    public HomePagerFragment newInstance(Categories.DataBean category) {
        HomePagerFragment homePagerFragment = new HomePagerFragment();
        // Fragment????????????
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_HOME_PAGER_TITLE, category.getTitle());
        bundle.putInt(Constants.KEY_HOME_PAGER_MATERIAL_ID, category.getId());
        homePagerFragment.setArguments(bundle);
        return homePagerFragment;
    }

    @BindView(R.id.home_pager_content_list)
    RecyclerView mContentList;

    @BindView(R.id.looper_pager)
    AutoLoopViewPager mLooperPager;

    @BindView(R.id.home_pager_title)
    TextView tvCurrentCategoryTitle;

    @BindView(R.id.looper_point_container)
    LinearLayout looperPointContainer;

    @BindView(R.id.home_pager_parent)
    LinearLayout homePagerParent;

    @BindView(R.id.home_pager_header_container)
    LinearLayout homeHeaderContainer;

    @BindView(R.id.home_pager_nested_scroller)
    TbNestedScrollView homePagerNestedView;

    @BindView(R.id.home_pager_refresh)
    TwinklingRefreshLayout mTwinklingRefreshLayout;

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_home_pager;
    }

    @Override
    public void onResume() {
        super.onResume();
        // ???????????????????????????
        mLooperPager.startLoop();
        LogUtils.d(this, "onResume...");
    }

    @Override
    public void onPause() {
        super.onPause();
        // ????????????????????????
        mLooperPager.stopLoop();
        LogUtils.d(this, "onPause...");
    }

    @Override
    protected void initView(View rootView) {
        // ?????????????????????
        mContentList.setLayoutManager(new LinearLayoutManager(getContext()));
        mContentList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = SizeUtils.dip2px(getContext(), 2);
                outRect.bottom = SizeUtils.dip2px(getContext(), 2);
            }
        });
        // ???????????????
        mContentAdapter = new LinearItemContentAdapter();
        // ???????????????
        mContentList.setAdapter(mContentAdapter);

        // ????????????????????????
        mLooperPagerAdapter = new LooperPagerAdapter();
        // ???????????????
        mLooperPager.setAdapter(mLooperPagerAdapter);
        // ??????RefreshLayout????????????
        mTwinklingRefreshLayout.setEnableRefresh(false);
        mTwinklingRefreshLayout.setEnableLoadmore(true);
    }

    @Override
    protected void initListener() {
        // ?????????????????????
        mContentAdapter.setOnListItemClickListener(this);
        // ????????????????????????
        mLooperPagerAdapter.setOnLooperPagerItemClickListener(this);
        homePagerParent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (homeHeaderContainer == null) {
                    return;
                }
                // ??????????????????header?????????
                int headerHeight = homeHeaderContainer.getMeasuredHeight();
                //LogUtils.d(HomePagerFragment.this, "headerHeight -- > " + headerHeight);
                homePagerNestedView.setHeaderHeight(headerHeight);

                int measuredHeight = homePagerParent.getMeasuredHeight();
                //LogUtils.d(HomePagerFragment.this, "measuredHeight -- > " + measuredHeight);
                ViewGroup.LayoutParams layoutParams = mContentList.getLayoutParams();
                layoutParams.height = measuredHeight;
                mContentList.setLayoutParams(layoutParams);
                // ???measuredHeight????????????????????????????????????
                if (measuredHeight != 0) {
                    homePagerParent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
        tvCurrentCategoryTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int measuredHeight = mContentList.getMeasuredHeight();
                LogUtils.d(HomePagerFragment.this, "measuredHeight -- >" + measuredHeight);
            }
        });
        // ?????????
        mLooperPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // ????????????
            }

            @Override
            public void onPageSelected(int position) {
                if (mLooperPagerAdapter.getDataSize() == 0) {
                    return;
                }
                // ???????????????
                int targetPosition = position % mLooperPagerAdapter.getDataSize();
                updateLooperIndicator(targetPosition);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //LogUtils.d(this, "onPageScrollStateChanged");
            }
        });
        mTwinklingRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                LogUtils.d(this, "onLoadMore...");
                if (mCategoryPagerPresenter != null) {
                    mCategoryPagerPresenter.loaderMore(mMaterialId);
                }
            }

            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                LogUtils.d(this, "onRefresh...");
                if (mCategoryPagerPresenter != null) {
                    // TODO:??????????????????
                    mCategoryPagerPresenter.reload(mMaterialId);
                }
            }
        });
    }

    /**
     * ???????????????
     *
     * @param targetPosition
     */
    private void updateLooperIndicator(int targetPosition) {
        for (int i = 0; i < looperPointContainer.getChildCount(); i++) {
            View point = looperPointContainer.getChildAt(i);
            if (i == targetPosition) {
                point.setBackgroundResource(R.drawable.shape_indicator_point_selected);
            } else {
                point.setBackgroundResource(R.drawable.shape_indicator_point_normal);
            }
        }
    }

    @Override
    protected void initPresenter() {
        mCategoryPagerPresenter = PresenterManger.getInstance().getCategoryPagerPresenter();
        mCategoryPagerPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        // ????????????
        Bundle arguments = getArguments();
        String title = arguments.getString(Constants.KEY_HOME_PAGER_TITLE);
        mMaterialId = arguments.getInt(Constants.KEY_HOME_PAGER_MATERIAL_ID);
        LogUtils.d(this, "title -- >" + title);
        LogUtils.d(this, "materialId -- >" + mMaterialId);
        if (mCategoryPagerPresenter != null) {
            mCategoryPagerPresenter.getContentByCategoryById(mMaterialId);
        }
        if (tvCurrentCategoryTitle != null) {
            tvCurrentCategoryTitle.setText(title);
        }
    }

    @Override
    public void onContentLoad(List<HomePagerContent.DataBean> contents) {
        // ??????????????????
        mContentAdapter.setData(contents);
        setUpState(State.SUCCESS);
    }

    @Override
    public int getCategoryId() {
        return mMaterialId;
    }

    @Override
    public void onLoading() {
        // ????????????
        setUpState(State.LOADING);
    }

    @Override
    public void onError() {
        // ????????????
        setUpState(State.ERROR);
    }

    @Override
    public void onEmpty() {
        setUpState(State.EMPTY);
    }

    @Override
    public void onLoaderMoreError() {
        if (mTwinklingRefreshLayout != null) {
            // ????????????
            mTwinklingRefreshLayout.finishLoadmore();
        }
        ToastUtil.showToast("??????????????????????????????");
    }

    @Override
    public void onLoaderMoreEmpty() {
        if (mTwinklingRefreshLayout != null) {
            // ????????????
            mTwinklingRefreshLayout.finishLoadmore();
        }
        // ??????????????????????????????
        ToastUtil.showToast("?????????????????????app");
    }

    @Override
    public void onLoaderMoreLoaded(List<HomePagerContent.DataBean> contents) {
        // ?????????????????????????????????
        mContentAdapter.addData(contents);
        if (mTwinklingRefreshLayout != null) {
            // ????????????
            mTwinklingRefreshLayout.finishLoadmore();
        }
        ToastUtil.showToast("?????????" + contents.size() + "?????????");
    }

    @Override
    public void onLooperListLoaded(List<HomePagerContent.DataBean> contents) {
        // ????????????????????????
        LogUtils.d(this, "looper size -- > " + contents.size());
        mLooperPagerAdapter.setData(contents);
        // ???????????????size????????????0
        int dx = (Integer.MAX_VALUE / 2) % contents.size();
        int targetPosition = (Integer.MAX_VALUE / 2) - dx;
        LogUtils.d(this, "targetPosition -- >" + targetPosition);
        mLooperPager.setCurrentItem(targetPosition);
        looperPointContainer.removeAllViews();
        // ????????????????????????
        for (int i = 0; i < contents.size(); i++) {
            View point = new View(getContext());
            int size = SizeUtils.dip2px(getContext(), 8);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size, size);
            layoutParams.leftMargin = SizeUtils.dip2px(getContext(), 5);
            point.setLayoutParams(layoutParams);
            if (i == 0) {
                point.setBackgroundResource(R.drawable.shape_indicator_point_selected);
            } else {
                point.setBackgroundResource(R.drawable.shape_indicator_point_normal);
            }
            looperPointContainer.addView(point);
        }
    }

    @Override
    protected void release() {
        mCategoryPagerPresenter.unregisterViewCallback(this);
    }

    @Override
    public void onItemClick(IBaseInfo item) {
        // ????????????????????????
        LogUtils.d(this, "item click -- >" + item.getTitle());
        handleItemClick(item);
    }

    /**
     * ?????????????????????
     *
     * @param item
     */
    private void handleItemClick(IBaseInfo item) {
        TicketUtil.toTicketPage(getContext(), item);
    }

    @Override
    public void onLooperItemClick(HomePagerContent.DataBean item) {
        // ??????????????????
        LogUtils.d(this, "img click -- > " + item.getTitle());
        handleItemClick(item);
    }
}
