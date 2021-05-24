package com.coolcr.taobaocoupon.view;

import com.coolcr.taobaocoupon.base.IBaseCallback;
import com.coolcr.taobaocoupon.model.domain.SelectedContent;
import com.coolcr.taobaocoupon.model.domain.SelectedPageCategory;

public interface ISelectedCallback extends IBaseCallback {

    /**
     * 分类内容结果
     *
     * @param categories
     */
    void onCategoriesLoad(SelectedPageCategory categories);


    /**
     * 根据分类id查询的内容
     *
     * @param content
     */
    void onContentLoad(SelectedContent content);
}
