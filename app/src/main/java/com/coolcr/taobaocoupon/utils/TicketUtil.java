package com.coolcr.taobaocoupon.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.coolcr.taobaocoupon.base.BaseApplication;
import com.coolcr.taobaocoupon.model.domain.IBaseInfo;
import com.coolcr.taobaocoupon.presenter.ITicketPresenter;
import com.coolcr.taobaocoupon.ui.activity.TicketActivity;

public class TicketUtil {
    public static void toTicketPage(Context context, IBaseInfo baseInfo) {
        String title = baseInfo.getTitle();
        // 详情的地址
        String url = baseInfo.getUrl();
        if (TextUtils.isEmpty(url)) {
            url = baseInfo.getUrl();
        }
        String cover = baseInfo.getCover();
        // 拿到ticketPresenter对象
        ITicketPresenter ticketPresenter = PresenterManger.getInstance().getTicketPresenter();
        ticketPresenter.getTicket(title, url, cover);
        context.startActivity(new Intent(BaseApplication.getAppContext(), TicketActivity.class));
    }
}
