package com.coolcr.taobaocoupon.model.domain;

public interface IBaseInfo {

    /**
     * 获取商品封面
     *
     * @return
     */
    String getCover();

    /**
     * 获取商品标题
     *
     * @return
     */
    String getTitle();

    /**
     * 获取商品链接
     * @return
     */
    String getUrl();
}
