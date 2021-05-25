package com.coolcr.taobaocoupon.presenter;

import com.coolcr.taobaocoupon.base.IBasePresenter;
import com.coolcr.taobaocoupon.view.ISearchCallback;

public interface ISearchPresenter extends IBasePresenter<ISearchCallback> {

    /**
     * 获取搜索历史
     */
    void getHistories();

    /**
     * 删除搜索历史
     */
    void delHistories();

    /**
     * 搜索圣品
     */
    void doSearch(String keyword);

    /**
     * 重试
     */
    void reSearch();

    /**
     * 获取更多搜素结果
     */
    void loaderMore();

    /**
     * 获取推荐搜索词
     */
    void getHotWords();
}
