package com.coolcr.taobaocoupon.ui.adapter;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.coolcr.taobaocoupon.R;
import com.coolcr.taobaocoupon.model.domain.SelectedContent;
import com.coolcr.taobaocoupon.utils.Constants;
import com.coolcr.taobaocoupon.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectedPageContentAdapter extends RecyclerView.Adapter<SelectedPageContentAdapter.InnerHolder> {

    private List<SelectedContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean> mData = new ArrayList<>();
    private OnSelectContentItemClickListener mItemClickListener = null;

    @NonNull
    @Override
    public SelectedPageContentAdapter.InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected_page_content, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectedPageContentAdapter.InnerHolder holder, int position) {
        SelectedContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean dataBean = mData.get(position);
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
        return mData.size();
    }

    public void setData(SelectedContent content) {
        if (content.getCode() == Constants.SUCCESS_CODE) {
            List<SelectedContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean> mapData = content.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data();
            mData.clear();
            mData.addAll(mapData);
            notifyDataSetChanged();
        }
    }

    public class InnerHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.selected_cover_img)
        ImageView coverImg;

        @BindView(R.id.selected_title_tv)
        TextView titleTv;

        @BindView(R.id.thrifty_tv)
        TextView thriftyTv;

        @BindView(R.id.selected_after_off_price_tv)
        TextView afterOffPriceTv;

        @BindView(R.id.selected_buy_tv)
        TextView buyTv;


        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @SuppressLint("DefaultLocale")
        public void setData(SelectedContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean dataBean) {
            titleTv.setText(dataBean.getTitle());
            String coverPath = UrlUtils.getCoverPath(dataBean.getPict_url(), 400);
            Glide.with(itemView.getContext()).load(coverPath).into(coverImg);
            if (TextUtils.isEmpty(dataBean.getCoupon_click_url())) {
                buyTv.setVisibility(View.GONE);
                thriftyTv.setVisibility(View.GONE);
                afterOffPriceTv.setText("晚啦，优惠卷抢光了");
            } else {
                buyTv.setVisibility(View.VISIBLE);
                thriftyTv.setVisibility(View.VISIBLE);
                thriftyTv.setText(String.format("领卷省%d元", dataBean.getCoupon_amount()));
                afterOffPriceTv.setText(String.format("原价：%s元", dataBean.getReserve_price()));
            }
        }
    }

    public void setItemClickListener(OnSelectContentItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public interface OnSelectContentItemClickListener {
        void onItemClick(SelectedContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean dataBean);
    }
}
