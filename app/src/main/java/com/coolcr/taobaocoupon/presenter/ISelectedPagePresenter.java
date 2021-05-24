package com.coolcr.taobaocoupon.presenter;

import com.coolcr.taobaocoupon.base.IBasePresenter;
import com.coolcr.taobaocoupon.model.domain.SelectedPageCategory;
import com.coolcr.taobaocoupon.view.ISelectedCallback;

public interface ISelectedPagePresenter extends IBasePresenter<ISelectedCallback> {

    /**
     * 获取精选分类
     */
    void getCategories();

    /**
     * 根据精选分类ID获取精选内容
     *
     * @param item
     */
    void getContentByCategoryId(SelectedPageCategory.DataBean item);

    /**
     * 重新加载内容
     */
    void reloadContent();
}
