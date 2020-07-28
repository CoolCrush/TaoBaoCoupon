package com.coolcr.taobaocoupon.presenter;

import com.coolcr.taobaocoupon.base.IBasePresenter;
import com.coolcr.taobaocoupon.view.ITicketCallback;

public interface ITicketPresenter extends IBasePresenter<ITicketCallback> {

    // 获取优惠卷，生成淘口令
    void getTicket(String title, String url, String cover);

}
