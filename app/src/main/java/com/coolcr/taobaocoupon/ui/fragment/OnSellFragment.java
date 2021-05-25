package com.coolcr.taobaocoupon.ui.fragment;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.coolcr.taobaocoupon.R;
import com.coolcr.taobaocoupon.base.BaseFragment;
import com.coolcr.taobaocoupon.model.domain.OnSellContent;
import com.coolcr.taobaocoupon.presenter.impl.OnSellPagePresenterImpl;
import com.coolcr.taobaocoupon.ui.adapter.OnSellContentAdapter;
import com.coolcr.taobaocoupon.utils.PresenterManger;
import com.coolcr.taobaocoupon.utils.SizeUtils;
import com.coolcr.taobaocoupon.utils.ToastUtil;
import com.coolcr.taobaocoupon.view.IOnSellPageCallback;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class OnSellFragment extends BaseFragment implements IOnSellPageCallback {


    private OnSellPagePresenterImpl mOnSellPagePresenter;

    public static final int DEFAULT_SPAN_COUNT = 2;

    @BindView(R.id.on_sell_content_list)
    RecyclerView contentList;
    @BindView(R.id.on_sell_refresh_layout)
    TwinklingRefreshLayout refreshLayout;

    private OnSellContentAdapter mContentAdapter;

    @Override
    protected void initPresenter() {
        super.initPresenter();
        mOnSellPagePresenter = PresenterManger.getInstance().getOnSellPagePresenter();
        mOnSellPagePresenter.registerViewCallback(this);
        mOnSellPagePresenter.getOnSellContent();
    }

    @Override
    protected void release() {
        super.release();
        if (mOnSellPagePresenter != null) {
            mOnSellPagePresenter.unregisterViewCallback(this);
        }
    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_red_packet;
    }

    @Override
    protected void initView(View rootView) {
        contentList.setLayoutManager(new GridLayoutManager(rootView.getContext(), DEFAULT_SPAN_COUNT));
        mContentAdapter = new OnSellContentAdapter();
        contentList.setAdapter(mContentAdapter);
        contentList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = SizeUtils.dip2px(getContext(), 3);
                outRect.bottom = SizeUtils.dip2px(getContext(), 3);
                outRect.left = SizeUtils.dip2px(getContext(), 3);
                outRect.right = SizeUtils.dip2px(getContext(), 3);
            }
        });

        refreshLayout.setEnableLoadmore(true);
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setEnableOverScroll(true);
    }

    @Override
    protected void initListener() {
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                //去加载更多的内容
                if (mOnSellPagePresenter != null) {
                    mOnSellPagePresenter.loadMore();
                }
            }
        });
    }

    @Override
    public void onContentLoadedSuccess(OnSellContent result) {
        setUpState(State.SUCCESS);
        //数据从这里回来
        mContentAdapter.setData(result);
    }

    @Override
    public void onMoreLoaded(OnSellContent moreResult) {
        //加载更多结果从这里回来
        refreshLayout.finishLoadmore();
        //将数据添加到适配器中
        mContentAdapter.addData(moreResult);
    }

    @Override
    public void onMoreLoadedError() {
        ToastUtil.showToast("网络错误，请稍后重试...");
        refreshLayout.finishLoadmore();
    }

    @Override
    public void onMoreLoadEmpty() {
        refreshLayout.finishLoadmore();
        ToastUtil.showToast("没有更多的内容...");
        refreshLayout.finishLoadmore();
    }

    @Override
    public void onLoading() {
        setUpState(State.LOADING);
    }

    @Override
    public void onError() {
        setUpState(State.ERROR);
    }

    @Override
    public void onEmpty() {
        setUpState(State.EMPTY);
    }
}
