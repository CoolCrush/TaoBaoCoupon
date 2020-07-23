package com.coolcr.taobaocoupon.view;

import com.coolcr.taobaocoupon.model.domain.Categories;

public interface IHomeCallback {
    /**
     *
     */
    void onCategoriesLoaded(Categories categories);

    void onNetworkError();

    void onLoading();

    void onEmpty();
}