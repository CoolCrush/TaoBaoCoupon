package com.coolcr.taobaocoupon.ui.fragment;

import android.content.Intent;
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
import com.coolcr.taobaocoupon.presenter.ICategoryPagerPresenter;
import com.coolcr.taobaocoupon.presenter.ITicketPresenter;
import com.coolcr.taobaocoupon.ui.activity.TicketActivity;
import com.coolcr.taobaocoupon.ui.adapter.HomePageContentAdapter;
import com.coolcr.taobaocoupon.ui.adapter.LooperPagerAdapter;
import com.coolcr.taobaocoupon.ui.custom.AutoLoopViewPager;
import com.coolcr.taobaocoupon.utils.Constants;
import com.coolcr.taobaocoupon.utils.LogUtils;
import com.coolcr.taobaocoupon.utils.PresenterManger;
import com.coolcr.taobaocoupon.utils.SizeUtils;
import com.coolcr.taobaocoupon.utils.ToastUtil;
import com.coolcr.taobaocoupon.view.ICategoryPagerCallback;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.views.TbNestedScrollView;

import java.util.List;

import butterknife.BindView;

public class HomePagerFragment extends BaseFragment implements ICategoryPagerCallback, HomePageContentAdapter.OnListItemClickListener, LooperPagerAdapter.OnLooperPagerItemClickListener {

    private ICategoryPagerPresenter mCategoryPagerPresenter;
    private int mMaterialId;
    private HomePageContentAdapter mContentAdapter;
    private LooperPagerAdapter mLooperPagerAdapter;

    public HomePagerFragment newInstance(Categories.DataBean category) {
        HomePagerFragment homePagerFragment = new HomePagerFragment();
        // Fragment传递数据
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
        // 可见的时候调用轮播
        mLooperPager.startLoop();
        LogUtils.d(this, "onResume...");
    }

    @Override
    public void onPause() {
        super.onPause();
        // 不可见的暂停轮播
        mLooperPager.stopLoop();
        LogUtils.d(this, "onPause...");
    }

    @Override
    protected void initView(View rootView) {
        // 设置布局管理器
        mContentList.setLayoutManager(new LinearLayoutManager(getContext()));
        mContentList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = 8;
                outRect.bottom = 8;
            }
        });
        // 创建适配器
        mContentAdapter = new HomePageContentAdapter();
        // 设置适配器
        mContentList.setAdapter(mContentAdapter);

        // 创建轮播图适配器
        mLooperPagerAdapter = new LooperPagerAdapter();
        // 设置适配器
        mLooperPager.setAdapter(mLooperPagerAdapter);
        // 设置RefreshLayout相关属性
        mTwinklingRefreshLayout.setEnableRefresh(false);
        mTwinklingRefreshLayout.setEnableLoadmore(true);
    }

    @Override
    protected void initListener() {
        // 列表的点击事件
        mContentAdapter.setOnListItemClickListener(this);
        // 轮播图的点击事件
        mLooperPagerAdapter.setOnLooperPagerItemClickListener(this);
        homePagerParent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (homeHeaderContainer == null) {
                    return;
                }
                // 获取要滑走的header的高度
                int headerHeight = homeHeaderContainer.getMeasuredHeight();
                //LogUtils.d(HomePagerFragment.this, "headerHeight -- > " + headerHeight);
                homePagerNestedView.setHeaderHeight(headerHeight);

                int measuredHeight = homePagerParent.getMeasuredHeight();
                //LogUtils.d(HomePagerFragment.this, "measuredHeight -- > " + measuredHeight);
                ViewGroup.LayoutParams layoutParams = mContentList.getLayoutParams();
                layoutParams.height = measuredHeight;
                mContentList.setLayoutParams(layoutParams);
                // 当measuredHeight有值的时候就清楚这个监听
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
        // 轮播图
        mLooperPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // 页面滑动
            }

            @Override
            public void onPageSelected(int position) {
                if (mLooperPagerAdapter.getDataSize() == 0) {
                    return;
                }
                // 切换指示器
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
                    // TODO:完成下拉刷新
                    mCategoryPagerPresenter.reload(mMaterialId);
                }
            }
        });
    }

    /**
     * 切换指示器
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
        // 加载数据
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
        // 数据列表加载
        mContentAdapter.setData(contents);
        setUpState(State.SUCCESS);
    }

    @Override
    public int getCategoryId() {
        return mMaterialId;
    }

    @Override
    public void onLoading() {
        // 数据加载
        setUpState(State.LOADING);
    }

    @Override
    public void onError() {
        // 网络错误
        setUpState(State.ERROR);
    }

    @Override
    public void onEmpty() {
        setUpState(State.EMPTY);
    }

    @Override
    public void onLoaderMoreError() {
        if (mTwinklingRefreshLayout != null) {
            // 结束刷新
            mTwinklingRefreshLayout.finishLoadmore();
        }
        ToastUtil.showToast("网络异常，请稍后重试");
    }

    @Override
    public void onLoaderMoreEmpty() {
        if (mTwinklingRefreshLayout != null) {
            // 结束刷新
            mTwinklingRefreshLayout.finishLoadmore();
        }
        // 提示用户没有更多数据
        ToastUtil.showToast("我也是有底线的app");
    }

    @Override
    public void onLoaderMoreLoaded(List<HomePagerContent.DataBean> contents) {
        // 添加到适配器数据的底部
        mContentAdapter.addData(contents);
        if (mTwinklingRefreshLayout != null) {
            // 结束刷新
            mTwinklingRefreshLayout.finishLoadmore();
        }
        ToastUtil.showToast("加载了" + contents.size() + "个商品");
    }

    @Override
    public void onLooperListLoaded(List<HomePagerContent.DataBean> contents) {
        // 获取到轮播图数据
        LogUtils.d(this, "looper size -- > " + contents.size());
        mLooperPagerAdapter.setData(contents);
        // 设置中间点size不一定为0
        int dx = (Integer.MAX_VALUE / 2) % contents.size();
        int targetPosition = (Integer.MAX_VALUE / 2) - dx;
        LogUtils.d(this, "targetPosition -- >" + targetPosition);
        mLooperPager.setCurrentItem(targetPosition);
        looperPointContainer.removeAllViews();
        // 添加轮播图指示点
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
    public void onItemClick(HomePagerContent.DataBean item) {
        // 列表内容被点击了
        LogUtils.d(this, "item click -- >" + item.getTitle());
        handleItemClick(item);
    }

    private void handleItemClick(HomePagerContent.DataBean item) {
        //TODO:处理数据
        String title = item.getTitle();
        String url = item.getClick_url();
        String cover = item.getPict_url();
        // 拿到ticketPresenter对象
        ITicketPresenter ticketPresenter = PresenterManger.getInstance().getTicketPresenter();
        ticketPresenter.getTicket(title, url, cover);
        startActivity(new Intent(getContext(), TicketActivity.class));
    }

    @Override
    public void onLooperItemClick(HomePagerContent.DataBean item) {
        // 轮播图被点击
        LogUtils.d(this, "img click -- > " + item.getTitle());
        handleItemClick(item);
    }
}
