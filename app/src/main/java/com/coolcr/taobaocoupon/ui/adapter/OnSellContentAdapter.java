package com.coolcr.taobaocoupon.ui.adapter;

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
import com.coolcr.taobaocoupon.model.domain.OnSellContent;
import com.coolcr.taobaocoupon.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OnSellContentAdapter extends RecyclerView.Adapter<OnSellContentAdapter.InnerHolder> {

    private List<OnSellContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean> mData = new ArrayList<>();

    @NonNull
    @Override

    public OnSellContentAdapter.InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_on_sell_content, parent, false);

        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        OnSellContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean mapDataBean = mData.get(position);
        holder.setData(mapDataBean);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(OnSellContent result) {
        mData.clear();
        List<OnSellContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean> mapData = result.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data();
        mData.addAll(mapData);
        notifyDataSetChanged();
    }

    public void addData(OnSellContent moreResult) {
        int startSize = mData.size();
        List<OnSellContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean> mapData = moreResult.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data();
        int itemCount = mapData.size();
        mData.addAll(mapData);
        notifyItemRangeChanged(startSize - 1, itemCount);
    }

    public class InnerHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.on_sell_cover_img)
        ImageView coverImg;

        @BindView(R.id.on_sell_title_tv)
        TextView titleTv;

        @BindView(R.id.on_sell_origin_price_tv)
        TextView originPriceTv;

        @BindView(R.id.on_sell_off_price_tv)
        TextView offPriceTv;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(OnSellContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean dataBean) {
            String coverPath = UrlUtils.getCoverPath(dataBean.getPict_url());
            Glide.with(itemView.getContext()).load(coverPath).into(coverImg);
            titleTv.setText(dataBean.getTitle());
            String originalPrice = dataBean.getZk_final_price();
            originPriceTv.setText("￥" + originalPrice);
            originPriceTv.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            int couponAmount = dataBean.getCoupon_amount();
            float originalPriceFloat = Float.parseFloat(originalPrice);
            float offPrice = originalPriceFloat - couponAmount;
            offPriceTv.setText(String.format("卷后价：%.1f", offPrice));

        }
    }
}
