package com.coolcr.taobaocoupon.view;

import com.coolcr.taobaocoupon.model.domain.HomePagerContent;

import java.util.List;

public interface ICategoryPagerCallback {
    /**
     * 数据加载回来
     *
     * @param contents
     */
    void onContentLoad(List<HomePagerContent.DataBean> contents);

    /**
     * 加载中
     *
     * @param categoryId
     */
    void onLoading(int categoryId);

    /**
     * 网络错误
     *
     * @param categoryId
     */
    void onError(int categoryId);

    /**
     * 数据为空
     *
     * @param categoryId
     */
    void onEmpty(int categoryId);

    /**
     * 加载更多时网络错误
     *
     * @param categoryId
     */
    void onLoaderMoreError(int categoryId);

    /**
     * 加载更多 没有更多内容
     *
     * @param categoryId
     */
    void onLoaderMoreEmpty(int categoryId);

    /**
     * 加载更多 等待页面
     *
     * @param contents
     */
    void onLoaderMoreLoading(List<HomePagerContent.DataBean> contents);

    /**
     * 轮播图内容加载
     *
     * @param contents
     */
    void onLooperListLoaded(List<HomePagerContent.DataBean> contents);

}
