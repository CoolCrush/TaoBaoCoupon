package com.coolcr.taobaocoupon.utils;

import com.coolcr.taobaocoupon.presenter.ICategoryPagerPresenter;
import com.coolcr.taobaocoupon.presenter.IHomePresenter;
import com.coolcr.taobaocoupon.presenter.ITicketPresenter;
import com.coolcr.taobaocoupon.presenter.impl.CategoryPagerPresenterImpl;
import com.coolcr.taobaocoupon.presenter.impl.HomePresenterImpl;
import com.coolcr.taobaocoupon.presenter.impl.OnSellPagePresenterImpl;
import com.coolcr.taobaocoupon.presenter.impl.SelectedPagePresenterImpl;
import com.coolcr.taobaocoupon.presenter.impl.TicketPresenterImpl;

public class PresenterManger {

    private static final PresenterManger ourInstance = new PresenterManger();

    private final ICategoryPagerPresenter mCategoryPagerPresenter;
    private final IHomePresenter mHomePresenter;
    private final ITicketPresenter mTicketPresenter;
    private final SelectedPagePresenterImpl mSelectedPagePresenter;
    private final OnSellPagePresenterImpl mOnSellPagePresenter;

    public ITicketPresenter getTicketPresenter() {
        return mTicketPresenter;
    }

    public IHomePresenter getHomePresenter() {
        return mHomePresenter;
    }

    public ICategoryPagerPresenter getCategoryPagerPresenter() {
        return mCategoryPagerPresenter;
    }

    public static PresenterManger getInstance() {
        return ourInstance;
    }

    public SelectedPagePresenterImpl getSelectedPagePresenter() {
        return mSelectedPagePresenter;
    }

    public OnSellPagePresenterImpl getOnSellPagePresenter() {
        return mOnSellPagePresenter;
    }

    private PresenterManger() {
        mCategoryPagerPresenter = new CategoryPagerPresenterImpl();
        mHomePresenter = new HomePresenterImpl();
        mTicketPresenter = new TicketPresenterImpl();
        mSelectedPagePresenter = new SelectedPagePresenterImpl();
        mOnSellPagePresenter = new OnSellPagePresenterImpl();
    }

}
