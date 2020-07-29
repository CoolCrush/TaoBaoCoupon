package com.coolcr.taobaocoupon.ui.activity;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.coolcr.taobaocoupon.R;
import com.coolcr.taobaocoupon.base.BaseActivity;
import com.coolcr.taobaocoupon.model.domain.TicketResult;
import com.coolcr.taobaocoupon.presenter.ITicketPresenter;
import com.coolcr.taobaocoupon.utils.LogUtils;
import com.coolcr.taobaocoupon.utils.PresenterManger;
import com.coolcr.taobaocoupon.utils.UrlUtils;
import com.coolcr.taobaocoupon.view.ITicketCallback;

import butterknife.BindView;

public class TicketActivity extends BaseActivity implements ITicketCallback {

    @BindView(R.id.ticket_back)
    ImageView imgBack;
    @BindView(R.id.ticket_cover)
    ImageView imgCover;
    @BindView(R.id.ticket_code)
    TextView tvTicketCode;
    @BindView(R.id.ticket_copy_or_open_btn)
    TextView mOpenOrCopy;

    private ITicketPresenter mTicketPresenter;

    @Override
    protected void initPresenter() {
        mTicketPresenter = PresenterManger.getInstance().getTicketPresenter();
        if (mTicketPresenter != null) {
            mTicketPresenter.registerViewCallback(this);
        }
    }

    @Override
    protected void release() {
        if (mTicketPresenter != null) {
            mTicketPresenter.unregisterViewCallback(this);
        }
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initEven() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_ticket;
    }

    @Override
    public void onTicketLoaded(String cover, TicketResult result) {
        LogUtils.d(this, "cover url -- >" + cover);
        // 页面从这里过来
        if (imgCover != null && !TextUtils.isEmpty(cover)) {
            ViewGroup.LayoutParams layoutParams = imgCover.getLayoutParams();
            int coverSize = Math.max(layoutParams.height, layoutParams.width);
            LogUtils.d(this, "coverSize - > " + coverSize);
            String coverUrl = UrlUtils.getCoverPath(cover);
            LogUtils.d(this, "coverUlr -- > " + coverUrl);
            Glide.with(this).load(coverUrl).into(imgCover);
        }

        if (result != null && result.getData().getTbk_tpwd_create_response() != null) {
            tvTicketCode.setText(result.getData().getTbk_tpwd_create_response().getData().getModel());
        }
    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onError() {

    }

    @Override
    public void onEmpty() {

    }
}
