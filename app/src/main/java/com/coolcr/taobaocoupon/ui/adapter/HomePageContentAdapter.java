package com.coolcr.taobaocoupon.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.coolcr.taobaocoupon.R;
import com.coolcr.taobaocoupon.model.domain.HomePagerContent;
import com.coolcr.taobaocoupon.utils.LogUtils;
import com.coolcr.taobaocoupon.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomePageContentAdapter extends RecyclerView.Adapter<HomePageContentAdapter.InnerHolder> {

    List<HomePagerContent.DataBean> mDataBeans = new ArrayList<>();

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_pager_content, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        HomePagerContent.DataBean dataBean = mDataBeans.get(position);
        // 设置数据
        holder.setData(dataBean);
    }

    @Override
    public int getItemCount() {
        return mDataBeans.size();
    }

    public void setData(List<HomePagerContent.DataBean> contents) {
        mDataBeans.clear();
        mDataBeans.addAll(contents);
        // 数据更新时刷新
        notifyDataSetChanged();
    }

    public void addData(List<HomePagerContent.DataBean> contents) {
        mDataBeans.addAll(contents);
        notifyItemRangeChanged(mDataBeans.size(), contents.size());
    }

    public class InnerHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.goods_cover)
        ImageView imgCover;

        @BindView(R.id.goods_title)
        TextView tvTitle;

        @BindView(R.id.goods_off_price)
        TextView tvOffPrice;

        @BindView(R.id.goods_after_off_price)
        TextView tvFinalPrice;

        @BindView(R.id.goods_original_price)
        TextView tvOriginalPrice;

        @BindView(R.id.sell_count)
        TextView tvSellCount;


        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @SuppressLint("DefaultLocale")
        public void setData(HomePagerContent.DataBean dataBean) {
            Context context = itemView.getContext();

            tvTitle.setText(dataBean.getTitle());
            //LogUtils.d(this, "url -- > " + dataBean.getPict_url());
            // 加载图片，pic没有https:头
            Glide.with(context).load(UrlUtils.getCoverPath(dataBean.getPict_url())).into(imgCover);

            int couponAmount = dataBean.getCoupon_amount();
            String finalPrice = dataBean.getZk_final_price();
            // 字符串模板
            tvOffPrice.setText(String.format(context.getString(R.string.text_goods_off_price), couponAmount));
            tvOriginalPrice.setText(String.format("￥%1$s", finalPrice));
            tvOriginalPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            // 卷后价格需要计算
            float resultPrice = Float.parseFloat(finalPrice) - couponAmount;
            LogUtils.d(this, "result price -- > " + resultPrice);
            tvFinalPrice.setText(String.format("￥%.2f", resultPrice));
            // 已售出
            tvSellCount.setText(String.format("%1$d已购买", dataBean.getVolume()));
        }
    }
}
