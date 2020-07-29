package com.coolcr.taobaocoupon.presenter.impl;

import com.coolcr.taobaocoupon.model.Api;
import com.coolcr.taobaocoupon.model.domain.TicketParams;
import com.coolcr.taobaocoupon.model.domain.TicketResult;
import com.coolcr.taobaocoupon.presenter.ITicketPresenter;
import com.coolcr.taobaocoupon.utils.LogUtils;
import com.coolcr.taobaocoupon.utils.RetrofitManager;
import com.coolcr.taobaocoupon.utils.UrlUtils;
import com.coolcr.taobaocoupon.view.ITicketCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TicketPresenterImpl implements ITicketPresenter {

    private ITicketCallback mViewCallback = null;
    private String mCover = null;
    private TicketResult mTicketResult;

    enum LoadState {
        LOADING, SUCCESS, ERROR, NONE
    }

    private LoadState mCurrentState = LoadState.NONE;

    @Override
    public void getTicket(String title, String url, String cover) {
        this.onTicketLoading();
        this.mCover = cover;
        LogUtils.d(TicketPresenterImpl.this, "title -- > " + title);
        LogUtils.d(TicketPresenterImpl.this, "url -- > " + url);
        LogUtils.d(TicketPresenterImpl.this, "cover -- > " + cover);
        String ticketUrl = UrlUtils.getTicketUrl(url);
        LogUtils.d(TicketPresenterImpl.this, "ticketUrl -- > " + ticketUrl);
        // 获取淘口令
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        TicketParams ticketParams = new TicketParams(ticketUrl, title);
        Call<TicketResult> task = api.getTicket(ticketParams);
        task.enqueue(new Callback<TicketResult>() {
            @Override
            public void onResponse(Call<TicketResult> call, Response<TicketResult> response) {
                // 数据结果
                int code = response.code();
                LogUtils.d(TicketPresenterImpl.this, "result code -- > " + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    mTicketResult = response.body();
                    LogUtils.d(TicketPresenterImpl.this, "ticketResult -- > " + mTicketResult);
                    // 通知UI更新
                    onTicketLoadedSuccess();
                } else {
                    LogUtils.e(TicketPresenterImpl.this, "result code -- > " + code);
                    onLoadedTicketError();
                }
            }

            @Override
            public void onFailure(Call<TicketResult> call, Throwable t) {
                LogUtils.e(TicketPresenterImpl.this, "result err -- > " + t.toString());
                onLoadedTicketError();
            }
        });
    }

    private void onTicketLoadedSuccess() {
        if (mViewCallback != null) {
            mViewCallback.onTicketLoaded(mCover, mTicketResult);
        } else {
            mCurrentState = LoadState.SUCCESS;
        }
    }

    private void onLoadedTicketError() {
        if (mViewCallback != null) {
            mViewCallback.onError();
        } else {
            mCurrentState = LoadState.ERROR;
        }
    }

    @Override
    public void registerViewCallback(ITicketCallback callback) {
        // 防止数据返回的太快，页面还没有绘制
        if (mCurrentState != LoadState.NONE) {
            // 状态改变
            // 更新Ui
            if (mCurrentState == LoadState.SUCCESS) {
                onTicketLoadedSuccess();
            } else if (mCurrentState == LoadState.ERROR) {
                onLoadedTicketError();
            } else if (mCurrentState == LoadState.LOADING) {
                onTicketLoading();
            }
        }
        this.mViewCallback = callback;
    }

    private void onTicketLoading() {
        if (mViewCallback != null) {
            mViewCallback.onLoading();
        } else {
            mCurrentState = LoadState.LOADING;
        }
    }

    @Override
    public void unregisterViewCallback(ITicketCallback callback) {
        this.mViewCallback = null;
    }
}
