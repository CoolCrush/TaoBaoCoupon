package com.coolcr.taobaocoupon.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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
import com.coolcr.taobaocoupon.utils.ToastUtil;
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

    @BindView(R.id.ticket_cover_loading)
    View loadingView;

    @BindView(R.id.ticket_load_retry)
    TextView tvLoadRetry;

    private ITicketPresenter mTicketPresenter;

    private boolean mHasTaoBaoApp = false;

    @Override
    protected void initPresenter() {
        mTicketPresenter = PresenterManger.getInstance().getTicketPresenter();
        if (mTicketPresenter != null) {
            mTicketPresenter.registerViewCallback(this);
        }
        // 判断是否安装淘宝
        // 淘宝报名 com.taobao.taobao
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo("com.taobao.taobao", PackageManager.MATCH_UNINSTALLED_PACKAGES);
            mHasTaoBaoApp = packageInfo != null;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            mHasTaoBaoApp = false;
        }
        LogUtils.d(this, "has taobao app -- > " + mHasTaoBaoApp);
        mOpenOrCopy.setText(mHasTaoBaoApp ? "打开淘宝领卷" : "复制口令");
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

        mOpenOrCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 复制淘口令
                String ticketCode = tvTicketCode.getText().toString().trim();
                LogUtils.d(this, "ticketCode -- >" + ticketCode);
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 复制到粘贴板
                ClipData clipData = ClipData.newPlainText("sob_taobao_ticket_code", ticketCode);
                cm.setPrimaryClip(clipData);

                if (mHasTaoBaoApp) {
                    // 打开淘宝
                    Intent taobaoIntent = new Intent();
                    //taobaoIntent.setAction("android.intent.action.MAIN");
                    //taobaoIntent.addCategory("android.intent.category.LAUNCHER");
                    ComponentName componentName = new ComponentName("com.taobao.taobao", "com.taobao.tao.TBMainActivity");
                    taobaoIntent.setComponent(componentName);
                    startActivity(taobaoIntent);
                } else {
                    ToastUtil.showToast("复制成功,粘贴分享或打开淘宝");
                }
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
        if (loadingView != null) {
            loadingView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoading() {
        if (tvLoadRetry != null) {
            tvLoadRetry.setVisibility(View.GONE);
        }
        if (loadingView != null) {
            loadingView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onError() {
        if (loadingView != null) {
            loadingView.setVisibility(View.GONE);
        }
        if (tvLoadRetry != null) {
            tvLoadRetry.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onEmpty() {

    }
}
