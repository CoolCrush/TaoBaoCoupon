package com.coolcr.taobaocoupon.ui.fragment;

import android.view.View;

import androidx.fragment.app.Fragment;

import com.coolcr.taobaocoupon.R;
import com.coolcr.taobaocoupon.base.BaseFragment;
import com.coolcr.taobaocoupon.model.domain.SelectedContent;
import com.coolcr.taobaocoupon.model.domain.SelectedPageCategory;
import com.coolcr.taobaocoupon.presenter.impl.SelectedPagePresenterImpl;
import com.coolcr.taobaocoupon.utils.LogUtils;
import com.coolcr.taobaocoupon.utils.PresenterManger;
import com.coolcr.taobaocoupon.view.ISelectedCallback;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectedFragment extends BaseFragment implements ISelectedCallback {

    private SelectedPagePresenterImpl mSelectedPagePresenter;

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
    }

    @Override
    public void onCategoriesLoad(SelectedPageCategory categories) {
        //分类内容返回
        LogUtils.d(this, "categories -- > " + categories.toString());
        List<SelectedPageCategory.DataBean> data = categories.getData();
        mSelectedPagePresenter.getContentByCategory(data.get(0));
    }

    @Override
    public void onContentLoad(SelectedContent content) {
        LogUtils.d(this, "onContentLoad: content size -- > " + content.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data().size());
    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onError() {

    }

    @Override
    public void onEmpty() {

    }
}
