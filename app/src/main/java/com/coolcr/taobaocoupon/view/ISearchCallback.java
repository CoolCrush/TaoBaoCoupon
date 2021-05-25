package com.coolcr.taobaocoupon.view;

import com.coolcr.taobaocoupon.base.IBaseCallback;
import com.coolcr.taobaocoupon.model.domain.SearchResult;

import java.util.List;

public interface ISearchCallback extends IBaseCallback {

    /**
     * 获取搜索历史
     *
     * @param histories
     */
    void onHistoriesLoaded(List<String> histories);

    /**
     * 删除历史记录
     */
    void onHistoriesDeleted();

    /**
     * 搜索成功的结果
     *
     * @param result
     */
    void onSearchSuccess(List<SearchResult> result);

    /**
     * 加载更多搜索结果
     *
     * @param result
     */
    void onMoreLoaded(List<SearchResult> result);

    /**
     * 加载更多错误
     */
    void onMoreLoadError();

    /**
     * 没有更多内容
     */
    void onMoreLoadEmpty();

    /**
     * 获取热点搜索
     *
     * @param hotWords
     */
    void getHotWordsSuccess(List<String> hotWords);
}
