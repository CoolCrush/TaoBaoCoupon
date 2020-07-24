package com.coolcr.taobaocoupon.presenter.impl;

import com.coolcr.taobaocoupon.model.Api;
import com.coolcr.taobaocoupon.model.domain.HomePagerContent;
import com.coolcr.taobaocoupon.presenter.ICategoryPagerPresenter;
import com.coolcr.taobaocoupon.utils.LogUtils;
import com.coolcr.taobaocoupon.utils.RetrofitManager;
import com.coolcr.taobaocoupon.utils.UrlUtils;
import com.coolcr.taobaocoupon.view.ICategoryPagerCallback;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CategoryPagerPresenterImpl implements ICategoryPagerPresenter {

    private Map<Integer, Integer> pagesInfo = new HashMap<>();

    private static final int DEFAULT_PAGE = 1;

    private CategoryPagerPresenterImpl() {

    }

    private static ICategoryPagerPresenter sInstance = null;

    public static ICategoryPagerPresenter getInstance() {
        if (sInstance == null) {
            sInstance = new CategoryPagerPresenterImpl();
        }
        return sInstance;
    }

    @Override
    public void getContentByCategoryById(int categoryId) {
        // 根据分类Id去加载内容
        for (ICategoryPagerCallback callback : mCallbacks) {
            if (callback.getCategoryId() == categoryId) {
                callback.onLoading();
            }
        }
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Integer targetPage = pagesInfo.get(categoryId);
        // 判断是否存在改分类的页面
        if (targetPage == null) {
            targetPage = DEFAULT_PAGE;
            pagesInfo.put(categoryId, targetPage);
        }
        String homePagerUrl = UrlUtils.createHomePagerUrl(categoryId, targetPage);
        LogUtils.d(this, "home pager url -- > " + homePagerUrl);
        Call<HomePagerContent> task = api.getHomePagerContent(homePagerUrl);
        task.enqueue(new Callback<HomePagerContent>() {
            @Override
            public void onResponse(Call<HomePagerContent> call, Response<HomePagerContent> response) {
                int code = response.code();
                LogUtils.d(this, "code -- > " + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    HomePagerContent pageContent = response.body();
                    LogUtils.d(this, pageContent.toString());
                    // 获取到数据，给UI更新
                    handleHomePageContentResult(pageContent, categoryId);
                } else {
                    handleNetworkError(categoryId);
                }
            }

            @Override
            public void onFailure(Call<HomePagerContent> call, Throwable t) {
                LogUtils.e(this, "onFailure -- > " + t.toString());
            }
        });
    }

    private void handleNetworkError(int categoryId) {
        for (ICategoryPagerCallback callback : mCallbacks) {
            if (callback.getCategoryId() == categoryId) {
                callback.onError();
            }
        }
    }

    private void handleHomePageContentResult(HomePagerContent pageContent, int categoryId) {
        // 通知UI层更新数据
        for (ICategoryPagerCallback callback : mCallbacks) {
            if (callback.getCategoryId() == categoryId) {
                if (pageContent == null || pageContent.getData().size() == 0) {
                    callback.onEmpty();
                } else {
                    callback.onContentLoad(pageContent.getData());
                }
            }
        }
    }

    @Override
    public void loaderMore(int categoryId) {

    }

    @Override
    public void reload(int categoryId) {

    }

    private List<ICategoryPagerCallback> mCallbacks = new ArrayList<>();

    @Override
    public void registerViewCallback(ICategoryPagerCallback callback) {
        if (!mCallbacks.contains(callback)) {
            mCallbacks.add(callback);
        }
    }

    @Override
    public void unregisterViewCallback(ICategoryPagerCallback callback) {
        mCallbacks.remove(callback);
    }
}
