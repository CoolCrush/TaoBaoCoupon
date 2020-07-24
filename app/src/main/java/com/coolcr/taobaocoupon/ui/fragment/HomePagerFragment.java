package com.coolcr.taobaocoupon.ui.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.coolcr.taobaocoupon.R;
import com.coolcr.taobaocoupon.base.BaseFragment;
import com.coolcr.taobaocoupon.model.domain.Categories;
import com.coolcr.taobaocoupon.model.domain.HomePagerContent;
import com.coolcr.taobaocoupon.presenter.ICategoryPagerPresenter;
import com.coolcr.taobaocoupon.presenter.impl.CategoryPagerPresenterImpl;
import com.coolcr.taobaocoupon.ui.adapter.HomePageContentAdapter;
import com.coolcr.taobaocoupon.utils.Constants;
import com.coolcr.taobaocoupon.utils.LogUtils;
import com.coolcr.taobaocoupon.view.ICategoryPagerCallback;

import java.util.List;

import butterknife.BindView;

public class HomePagerFragment extends BaseFragment implements ICategoryPagerCallback {

    private ICategoryPagerPresenter mCategoryPagerPresenter;
    private int mMaterialId;
    private HomePageContentAdapter mContentAdapter;

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
    public RecyclerView mContentList;

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_home_pager;
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
    }

    @Override
    protected void initPresenter() {
        mCategoryPagerPresenter = CategoryPagerPresenterImpl.getInstance();
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

    }

    @Override
    public void onLoaderMoreEmpty() {

    }

    @Override
    public void onLoaderMoreLoaded(List<HomePagerContent.DataBean> contents) {

    }

    @Override
    public void onLooperListLoaded(List<HomePagerContent.DataBean> contents) {

    }

    @Override
    protected void release() {
        mCategoryPagerPresenter.unregisterViewCallback(this);
    }
}
