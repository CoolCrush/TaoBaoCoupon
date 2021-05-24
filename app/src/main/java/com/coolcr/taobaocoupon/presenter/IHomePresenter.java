package com.coolcr.taobaocoupon.presenter;

import com.coolcr.taobaocoupon.base.IBasePresenter;
import com.coolcr.taobaocoupon.view.IHomeCallback;

public interface IHomePresenter extends IBasePresenter<IHomeCallback> {
    /**
     * 获取商品分类
     */
    void getCategories();

}
