package com.coolcr.taobaocoupon.presenter.impl;

import com.coolcr.taobaocoupon.model.Api;
import com.coolcr.taobaocoupon.model.domain.Histories;
import com.coolcr.taobaocoupon.model.domain.HotWordsContent;
import com.coolcr.taobaocoupon.model.domain.SearchResult;
import com.coolcr.taobaocoupon.presenter.ISearchPresenter;
import com.coolcr.taobaocoupon.utils.JsonCacheUtil;
import com.coolcr.taobaocoupon.utils.LogUtils;
import com.coolcr.taobaocoupon.utils.RetrofitManager;
import com.coolcr.taobaocoupon.view.ISearchCallback;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchPresenterImpl implements ISearchPresenter {

    private final Api mApi;
    private ISearchCallback mViewCallback;

    private int mCurrentPage = 1;
    private String mCurrentKeyword = null;
    private final JsonCacheUtil mJsonCacheUtil;

    public SearchPresenterImpl() {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        mApi = retrofit.create(Api.class);
        mJsonCacheUtil = JsonCacheUtil.getInstance();
    }

    @Override
    public void getHistories() {
        Histories histories = mJsonCacheUtil.getValue(KEY_HISTORIES, Histories.class);
        if (mViewCallback != null &&
                histories != null &&
                histories.getHistories() != null &&
                histories.getHistories().size() != 0) {
            mViewCallback.onHistoriesLoaded(histories.getHistories());
        }
    }

    @Override
    public void delHistories() {
        mJsonCacheUtil.delCache(KEY_HISTORIES);
    }

    public static final String KEY_HISTORIES = "key_histories";

    private int historiesMaxSize = 10;

    /**
     * 添加历史记录
     *
     * @param history
     */
    private void saveHistory(String history) {
        //如果说已经存在了，就删掉再添加
        Histories histories = mJsonCacheUtil.getValue(KEY_HISTORIES, Histories.class);
        List<String> historiesList = null;
        //去重
        if (histories != null && histories.getHistories() != null) {
            historiesList = histories.getHistories();
            if (historiesList.contains(history)) {
                historiesList.remove(history);
            }
        }
        //处理没有数据的情况
        if (histories == null) {
            histories = new Histories();
        }
        if (historiesList == null) {
            historiesList = new ArrayList<>();
        }
        if (historiesList.size() > historiesMaxSize) {
            historiesList = historiesList.subList(0, historiesMaxSize);
        }
        //添加记录
        historiesList.add(history);

        histories.setHistories(historiesList);

        mJsonCacheUtil.saveCache(KEY_HISTORIES, histories);
    }

    @Override
    public void doSearch(String keyword) {
        this.mCurrentKeyword = keyword;
        this.saveHistory(keyword);

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
                        handleSearchResult(response.body());
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
                        handleMoreSearchResult(response.body());
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
