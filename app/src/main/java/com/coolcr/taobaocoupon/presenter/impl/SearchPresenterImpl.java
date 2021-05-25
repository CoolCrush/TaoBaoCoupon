package com.coolcr.taobaocoupon.presenter.impl;

import com.coolcr.taobaocoupon.model.Api;
import com.coolcr.taobaocoupon.model.domain.HotWordsContent;
import com.coolcr.taobaocoupon.model.domain.SearchResult;
import com.coolcr.taobaocoupon.presenter.ISearchPresenter;
import com.coolcr.taobaocoupon.utils.LogUtils;
import com.coolcr.taobaocoupon.utils.RetrofitManager;
import com.coolcr.taobaocoupon.view.ISearchCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchPresenterImpl implements ISearchPresenter {

    private final Api mApi;
    private ISearchCallback mViewCallback;

    private int mCurrentPage = 1;
    private String mCurrentKeyword = null;

    public SearchPresenterImpl() {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        mApi = retrofit.create(Api.class);
    }

    @Override
    public void getHistories() {

    }

    @Override
    public void delHistories() {

    }

    @Override
    public void doSearch(String keyword) {
        this.mCurrentKeyword = keyword;
        if (mViewCallback != null) {
            mViewCallback.onLoading();
        }
        Call<SearchResult> task = mApi.getSearchContent(mCurrentPage, keyword);
        task.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    if (mViewCallback != null) {
                        handleMoreSearchResult(response.body());
                    }
                } else {
                    onError();
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                onError();
            }
        });
    }

    /**
     * 获取更多搜索结果
     *
     * @param result
     */
    private void handleMoreSearchResult(SearchResult result) {
        if (isResultEmpty(result)) {
            mViewCallback.onMoreLoadEmpty();
        } else {
            mViewCallback.onMoreLoaded(result);
        }
    }

    private void onLoadMoreError() {
        mCurrentPage--;
        if (mViewCallback != null) {
            mViewCallback.onMoreLoadError();
        }
    }

    private void handleSearchResult(SearchResult result) {
        if (isResultEmpty(result)) {
            mViewCallback.onMoreLoadEmpty();
        } else {
            mViewCallback.onSearchSuccess(result);
        }

    }

    private boolean isResultEmpty(SearchResult result) {
        try {
            return result == null || result.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data().size() == 0;
        } catch (Exception e) {
            return false;
        }
    }

    private void onError() {
        if (mViewCallback != null) {
            mViewCallback.onError();
        }
    }

    @Override
    public void reSearch() {
        if (mViewCallback != null) {
            if (mCurrentKeyword == null) {
                mViewCallback.onEmpty();
            } else {
                this.doSearch(mCurrentKeyword);
            }
        }
    }

    @Override
    public void loaderMore() {
        mCurrentPage++;
        //进行搜索
        if (mViewCallback != null) {
            if (mCurrentKeyword == null) {
                mViewCallback.onMoreLoadEmpty();
            } else {
                //做搜索的事情
                doSearchMore();
            }
        }
    }

    /**
     * 获取更多搜索内容
     */
    private void doSearchMore() {
        Call<SearchResult> task = mApi.getSearchContent(mCurrentPage, mCurrentKeyword);
        task.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    if (mViewCallback != null) {
                        handleSearchResult(response.body());
                    }
                } else {
                    onLoadMoreError();
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                onLoadMoreError();
            }
        });
    }

    @Override
    public void getHotWords() {
        Call<HotWordsContent> task = mApi.getHotWordsContent();
        task.enqueue(new Callback<HotWordsContent>() {
            @Override
            public void onResponse(Call<HotWordsContent> call, Response<HotWordsContent> response) {
                int code = response.code();
                LogUtils.d(SearchPresenterImpl.this, "hot words code -- > " + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    if (mViewCallback != null) {
                        mViewCallback.getHotWordsSuccess(response.body().getData());
                    }
                }
            }

            @Override
            public void onFailure(Call<HotWordsContent> call, Throwable t) {
                LogUtils.d(SearchPresenterImpl.this, "hot words error");
            }
        });
    }

    @Override
    public void registerViewCallback(ISearchCallback callback) {
        mViewCallback = callback;
    }

    @Override
    public void unregisterViewCallback(ISearchCallback callback) {
        mViewCallback = null;
    }
}
