package com.coolcr.taobaocoupon.ui.fragment;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.coolcr.taobaocoupon.R;
import com.coolcr.taobaocoupon.base.BaseFragment;
import com.coolcr.taobaocoupon.model.domain.SelectedContent;
import com.coolcr.taobaocoupon.model.domain.SelectedPageCategory;
import com.coolcr.taobaocoupon.presenter.impl.SelectedPagePresenterImpl;
import com.coolcr.taobaocoupon.ui.adapter.SelectedPageContentAdapter;
import com.coolcr.taobaocoupon.ui.adapter.SelectedPageLeftAdapter;
import com.coolcr.taobaocoupon.utils.LogUtils;
import com.coolcr.taobaocoupon.utils.PresenterManger;
import com.coolcr.taobaocoupon.utils.SizeUtils;
import com.coolcr.taobaocoupon.view.ISelectedCallback;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectedFragment extends BaseFragment implements ISelectedCallback {

    @BindView(R.id.left_category_list)
    RecyclerView leftCategoryList;

    @BindView(R.id.content_list)
    RecyclerView contentList;

    private SelectedPagePresenterImpl mSelectedPagePresenter;
    private SelectedPageLeftAdapter mLeftAdapter;
    private SelectedPageContentAdapter mContentAdapter;

    @Override
    protected void initPresenter() {
        super.initPresenter();
        mSelectedPagePresenter = PresenterManger.getInstance().getSelectedPagePresenter();
        mSelectedPagePresenter.registerViewCallback(this);
        mSelectedPagePresenter.getCategories();
    }

    @Override
    protected void release() {
        super.release();
        if (mSelectedPagePresenter != null) {
            mSelectedPagePresenter.unregisterViewCallback(this);
        }
    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_selected;
    }

    @Override
    protected void initView(View rootView) {
        setUpState(State.SUCCESS);
        //左边列表
        leftCategoryList.setLayoutManager(new LinearLayoutManager(getContext()));
        mLeftAdapter = new SelectedPageLeftAdapter();
        leftCategoryList.setAdapter(mLeftAdapter);
        //右边列表
        contentList.setLayoutManager(new LinearLayoutManager(getContext()));
        mContentAdapter = new SelectedPageContentAdapter();
        contentList.setAdapter(mContentAdapter);
        contentList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.bottom = SizeUtils.dip2px(parent.getContext(), 4);
                outRect.top = SizeUtils.dip2px(parent.getContext(), 4);
                outRect.right = SizeUtils.dip2px(parent.getContext(), 8);
                outRect.left = SizeUtils.dip2px(parent.getContext(), 8);

            }
        });
    }

    @Override
    protected void initListener() {
        super.initListener();
        mLeftAdapter.setLeftItemClickListener(new SelectedPageLeftAdapter.OnLeftItemClickListener() {
            @Override
            public void onLeftItemClick(SelectedPageCategory.DataBean dataBean) {
                //左边的分类被点击
                LogUtils.d(SelectedFragment.this, "click title -- > " + dataBean.getFavorites_title());

                //获取右侧列表数据
                mSelectedPagePresenter.getContentByCategory(dataBean);
            }
        });
    }

    @Override
    public void onCategoriesLoad(SelectedPageCategory categories) {
        setUpState(State.SUCCESS);
        //分类内容返回
        LogUtils.d(this, "categories -- > " + categories.toString());
        mLeftAdapter.setData(categories);
    }

    @Override
    public void onContentLoad(SelectedContent content) {
        LogUtils.d(this, "onContentLoad: content size -- > " + content.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data().size());
        if (content.getData() != null) {
            mContentAdapter.setData(content);
        }
        contentList.scrollToPosition(0);
    }

    @Override
    public void onLoading() {
        setUpState(State.LOADING);
    }

    @Override
    public void onError() {

    }

    @Override
    public void onEmpty() {

    }
}
