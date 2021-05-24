package com.coolcr.taobaocoupon.presenter.impl;

import com.coolcr.taobaocoupon.model.Api;
import com.coolcr.taobaocoupon.model.domain.SelectedContent;
import com.coolcr.taobaocoupon.model.domain.SelectedPageCategory;
import com.coolcr.taobaocoupon.presenter.ISelectedPagePresenter;
import com.coolcr.taobaocoupon.utils.LogUtils;
import com.coolcr.taobaocoupon.utils.RetrofitManager;
import com.coolcr.taobaocoupon.utils.UrlUtils;
import com.coolcr.taobaocoupon.view.ISelectedCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SelectedPagePresenterImpl implements ISelectedPagePresenter {

    private final Api mApi;
    private SelectedPageCategory.DataBean mCurrentCategoryItem = null;

    public SelectedPagePresenterImpl() {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        mApi = retrofit.create(Api.class);
    }

    private ISelectedCallback mViewCallback = null;

    @Override
    public void getCategories() {
        if (mViewCallback != null) {
            mViewCallback.onLoading();
        }
        // 获取分类内容
        Call<SelectedPageCategory> task = mApi.getSelectedPageCategories();
        task.enqueue(new Callback<SelectedPageCategory>() {
            @Override
            public void onResponse(Call<SelectedPageCategory> call, Response<SelectedPageCategory> response) {
                int code = response.code();
                LogUtils.d(SelectedPagePresenterImpl.this, "selected result code -- > " + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    SelectedPageCategory result = response.body();
                    //TODO:通知UI改变
                    if (mViewCallback != null) {
                        mViewCallback.onCategoriesLoad(result);
                    }
                } else {
                    onLoadError();
                }
            }

            @Override
            public void onFailure(Call<SelectedPageCategory> call, Throwable t) {
                onLoadError();
            }
        });
    }

    private void onLoadError() {
        if (mViewCallback != null) {
            mViewCallback.onError();
        }
    }

    @Override
    public void getContentByCategory(SelectedPageCategory.DataBean item) {
        this.mCurrentCategoryItem = item;
        String contentUrl = UrlUtils.getSelectedPageContentUrl(item.getFavorites_id());
        Call<SelectedContent> task = mApi.getSelectedContent(contentUrl);
        task.enqueue(new Callback<SelectedContent>() {
            @Override
            public void onResponse(Call<SelectedContent> call, Response<SelectedContent> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    SelectedContent result = response.body();
                    if (mViewCallback != null) {
                        mViewCallback.onContentLoad(result);
                    }
                } else {
                    onLoadError();
                }
            }

            @Override
            public void onFailure(Call<SelectedContent> call, Throwable t) {
                onLoadError();
            }
        });
    }

    @Override
    public void reloadContent() {
        if (mCurrentCategoryItem != null) {
            this.getCategories();
        }
    }

    @Override
    public void registerViewCallback(ISelectedCallback callback) {
        this.mViewCallback = callback;
    }

    @Override
    public void unregisterViewCallback(ISelectedCallback callback) {
        this.mViewCallback = null;
    }
}
