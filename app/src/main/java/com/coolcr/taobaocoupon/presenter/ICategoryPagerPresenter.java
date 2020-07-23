package com.coolcr.taobaocoupon.presenter;

import com.coolcr.taobaocoupon.base.IBasePresenter;
import com.coolcr.taobaocoupon.view.ICategoryPagerCallback;

public interface ICategoryPagerPresenter extends IBasePresenter<ICategoryPagerCallback> {

    /**
     * 根据分类id去获取内容
     *
     * @param categoryId
     */
    void getContentByCategoryById(int categoryId);

    void loaderMore(int categoryId);

    void reload(int categoryId);

}
