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
import com.coolcr.taobaocoupon.view.IOnSellPageCallback;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class OnSellFragment extends BaseFragment implements IOnSellPageCallback {


    private OnSellPagePresenterImpl mOnSellPagePresenter;

    public static final int DEFAULT_SPAN_COUNT = 2;

    @BindView(R.id.on_sell_content_list)
    RecyclerView contentList;
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
    }

    @Override
    public void onContentLoadedSuccess(OnSellContent result) {
        setUpState(State.SUCCESS);
        //数据从这里回来
        mContentAdapter.setData(result);
    }

    @Override
    public void onMoreLoaded(OnSellContent moreResult) {

    }

    @Override
    public void onMoreLoadedError() {

    }

    @Override
    public void onMoreLoadEmpty() {

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
