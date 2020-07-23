package com.coolcr.taobaocoupon.presenter;

import com.coolcr.taobaocoupon.base.IBasePresenter;

public interface IHomePresenter extends IBasePresenter<IHomePresenter> {
    /**
     * 获取商品分类
     */
    void getCategories();


}
