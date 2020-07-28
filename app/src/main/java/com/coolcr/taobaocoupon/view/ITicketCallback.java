package com.coolcr.taobaocoupon.view;

import com.coolcr.taobaocoupon.base.IBaseCallback;
import com.coolcr.taobaocoupon.model.domain.TicketResult;

public interface ITicketCallback extends IBaseCallback {

    /**
     * 淘口令加载结果
     *
     * @param cover
     * @param result
     */
    void onTicketLoaded(String cover, TicketResult result);
}
