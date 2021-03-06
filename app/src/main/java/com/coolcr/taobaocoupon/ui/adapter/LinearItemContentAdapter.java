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
import com.coolcr.taobaocoupon.model.domain.IBaseInfo;
import com.coolcr.taobaocoupon.model.domain.ILinearItemInfo;
import com.coolcr.taobaocoupon.utils.LogUtils;
import com.coolcr.taobaocoupon.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LinearItemContentAdapter extends RecyclerView.Adapter<LinearItemContentAdapter.InnerHolder> {

    List<ILinearItemInfo> mDataBeans = new ArrayList<>();

    private int testCount = 0;
    private OnListItemClickListener mItemClickListener = null;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        testCount++;
        // 一开始绘制的条数
        //LogUtils.d(this, "onCreateViewHolder..." + testCount);
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_linear_goods_content, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        //LogUtils.d(this, "onBindViewHolder... position -- > " + position);
        ILinearItemInfo dataBean = mDataBeans.get(position);
        // 设置数据
        holder.setData(dataBean);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClick(dataBean);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataBeans.size();
    }

    public void setData(List<? extends ILinearItemInfo> contents) {
        mDataBeans.clear();
        mDataBeans.addAll(contents);
        // 数据更新时刷新
        notifyDataSetChanged();
    }

    public void addData(List<? extends ILinearItemInfo> contents) {
        int startSize = mDataBeans.size();
        mDataBeans.addAll(contents);
        notifyItemRangeChanged(startSize - 1, contents.size());
        LogUtils.d(this, "商品个数 -- >" + mDataBeans.size());
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
        public void setData(ILinearItemInfo dataBean) {
            Context context = itemView.getContext();

            tvTitle.setText(dataBean.getTitle());
            ViewGroup.LayoutParams layoutParams = imgCover.getLayoutParams();
            int width = layoutParams.width;
            int height = layoutParams.height;
            int coverSize = Math.max(width, height);
            //LogUtils.d(this, "width -- > " + width);
            //LogUtils.d(this, "height -- > " + height);
            // 加载图片，pic没有https:头
            String coverPath = UrlUtils.getCoverPath(dataBean.getCover(), coverSize);
            //LogUtils.d(this, "url -- > " + coverPath);
            Glide.with(context).load(coverPath).into(imgCover);
            long couponAmount = dataBean.getCouponAmount();
            String finalPrice = dataBean.getFinalPrice();
            // 字符串模板
            tvOffPrice.setText(String.format(context.getString(R.string.text_goods_off_price), couponAmount));
            tvOriginalPrice.setText(String.format("￥%1$s", finalPrice));
            tvOriginalPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            // 卷后价格需要计算
            float resultPrice = Float.parseFloat(finalPrice) - couponAmount;
            //LogUtils.d(this, "result price -- > " + resultPrice);
            tvFinalPrice.setText(String.format("￥%.2f", resultPrice));
            // 已售出
            tvSellCount.setText(String.format("%1$d已购买", dataBean.getVolume()));
        }
    }

    public void setOnListItemClickListener(OnListItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public interface OnListItemClickListener {
        void onItemClick(IBaseInfo item);
    }
}
