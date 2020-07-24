package com.coolcr.taobaocoupon.model;


import com.coolcr.taobaocoupon.model.domain.Categories;
import com.coolcr.taobaocoupon.model.domain.HomePagerContent;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface Api {

    @GET("discovery/categories")
    Call<Categories> getCategories();

    @GET
    Call<HomePagerContent> getHomePagerContent(@Url String url);
}
