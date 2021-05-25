package com.coolcr.taobaocoupon.presenter.impl;

import com.coolcr.taobaocoupon.model.Api;
import com.coolcr.taobaocoupon.model.domain.OnSellContent;
import com.coolcr.taobaocoupon.presenter.IOnSellPagePresenter;
import com.coolcr.taobaocoupon.utils.RetrofitManager;
import com.coolcr.taobaocoupon.utils.UrlUtils;
import com.coolcr.taobaocoupon.view.IOnSellPageCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OnSellPagePresenterImpl implements IOnSellPagePresenter {

    public static final int DEFAULT_PAGE = 1;
    private int mCurrentPage = DEFAULT_PAGE;

    private final Api mApi;
    private IOnSellPageCallback mViewCallback = null;


    public OnSellPagePresenterImpl() {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        mApi = retrofit.create(Api.class);
    }

    @Override
    public void getOnSellContent() {
        if (mIsLoading) {
            return;
        }
        mIsLoading = true;
        //通知Ui为加载中
        if (mViewCallback != null) {
            mViewCallback.onLoading();
        }
        //获取特惠内容
        String contentUrl = UrlUtils.getOnSellContentUrl(mCurrentPage);
        Call<OnSellContent> task = mApi.getOnSellContent(contentUrl);
        task.enqueue(new Callback<OnSellContent>() {
            @Override
            public void onResponse(Call<OnSellContent> call, Response<OnSellContent> response) {
                mIsLoading = false;
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    OnSellContent result = response.body();
                    OnSuccess(result);
                } else {
                    onError();
                }
            }

            @Override
            public void onFailure(Call<OnSellContent> call, Throwable t) {
                onError();
            }
        });
    }

    private void OnSuccess(OnSellContent result) {
        if (mViewCallback != null) {
            try {
                int size = result.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data().size();
                if (size == 0) {
                    onEmpty();
                } else {
                    mViewCallback.onContentLoadedSuccess(result);
                }
            } catch (Exception e) {
                onEmpty();
            }
        }
    }

    private void onEmpty() {
        if (mViewCallback != null) {
            mViewCallback.onEmpty();
        }
    }

    private void onError() {
        mIsLoading = false;
        if (mViewCallback != null) {
            mViewCallback.onError();
        }
    }

    /**
     * 重新加载
     */
    @Override
    public void reLoad() {
        this.getOnSellContent();
    }

    /**
     * 当前加载状态
     */
    private boolean mIsLoading = false;

    @Override
    public void loadMore() {
        if (mIsLoading) {
            return;
        }
        mIsLoading = true;
        // 加载更多
        mCurrentPage++;
        String targetUrl = UrlUtils.getOnSellContentUrl(mCurrentPage);
        Call<OnSellContent> task = mApi.getOnSellContent(targetUrl);
        task.enqueue(new Callback<OnSellContent>() {
            @Override
            public void onResponse(Call<OnSellContent> call, Response<OnSellContent> response) {
                mIsLoading = false;
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    OnSellContent result = response.body();
                    onMoreLoaded(result);
                } else {
                    onMoreLoadError();
                }
            }

            @Override
            public void onFailure(Call<OnSellContent> call, Throwable t) {
                onMoreLoadError();
            }
        });

    }

    /**
     * 加载更多失败
     */
    private void onMoreLoadError() {
        mIsLoading = false;
        mCurrentPage--;
        mViewCallback.onMoreLoadedError();
    }

    /**
     * 加载更多成功
     *
     * @param result
     */
    private void onMoreLoaded(OnSellContent result) {
        if (mViewCallback != null) {
            try {
                int size = result.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data().size();
                if (size == 0) {
                    mViewCallback.onMoreLoadEmpty();
                    mCurrentPage--;
                } else {
                    mViewCallback.onMoreLoaded(result);
                }
            } catch (Exception e) {
                onMoreLoadError();
            }
        }
    }

    @Override
    public void registerViewCallback(IOnSellPageCallback callback) {
        this.mViewCallback = callback;
    }

    @Override
    public void unregisterViewCallback(IOnSellPageCallback callback) {
        this.mViewCallback = null;
    }
}
