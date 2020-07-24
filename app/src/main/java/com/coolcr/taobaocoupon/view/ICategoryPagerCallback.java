package com.coolcr.taobaocoupon.view;

import com.coolcr.taobaocoupon.base.IBaseCallback;
import com.coolcr.taobaocoupon.model.domain.HomePagerContent;

import java.util.List;

public interface ICategoryPagerCallback extends IBaseCallback {
    /**
     * 数据加载回来
     *
     * @param contents
     */
    void onContentLoad(List<HomePagerContent.DataBean> contents);

    int getCategoryId();

    /**
     * 加载更多时网络错误
     */
    void onLoaderMoreError();

    /**
     * 加载更多 没有更多内容
     */
    void onLoaderMoreEmpty();

    /**
     * 加载更多 等待页面
     *
     * @param contents
     */
    void onLoaderMoreLoaded(List<HomePagerContent.DataBean> contents);

    /**
     * 轮播图内容加载
     *
     * @param contents
     */
    void onLooperListLoaded(List<HomePagerContent.DataBean> contents);

}
