package com.coolcr.taobaocoupon.presenter;

import com.coolcr.taobaocoupon.base.IBasePresenter;

public interface ICategoryPagerPresenter extends IBasePresenter<ICategoryPagerPresenter> {

    /**
     * 根据分类id去获取内容
     *
     * @param categoryId
     */
    void getContentByCategoryById(int categoryId);

    void loaderMore(int categoryId);

    void reload(int categoryId);

}
