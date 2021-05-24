package com.coolcr.taobaocoupon.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.coolcr.taobaocoupon.R;
import com.coolcr.taobaocoupon.model.domain.SelectedPageCategory;

import java.util.ArrayList;
import java.util.List;

public class SelectedPageLeftAdapter extends RecyclerView.Adapter<SelectedPageLeftAdapter.InnerHolder> {

    private List<SelectedPageCategory.DataBean> mData = new ArrayList<>();

    //当前选中那个分类
    private int mCurrentSelectedPosition = 0;

    private OnLeftItemClickListener mLeftItemClickListener = null;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected_page_left, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        View itemView = holder.itemView;
        TextView categoryTv = itemView.findViewById(R.id.left_category_tv);
        if (mCurrentSelectedPosition == position) {
            categoryTv.setBackgroundColor(categoryTv.getResources().getColor(R.color.colorTvSelected));
        } else {
            categoryTv.setBackgroundColor(categoryTv.getResources().getColor(R.color.white));
        }
        SelectedPageCategory.DataBean dataBean = mData.get(position);
        categoryTv.setText(dataBean.getFavorites_title());

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 不相等才修改
                if (mLeftItemClickListener != null && mCurrentSelectedPosition != position) {
                    //修改当前选中位置
                    mCurrentSelectedPosition = position;
                    mLeftItemClickListener.onLeftItemClick(dataBean);
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(SelectedPageCategory categories) {
        mData.clear();
        if (mData != null) {
            mData.addAll(categories.getData());
            notifyDataSetChanged();
        }
        if (mData.size() > 0) {
            mLeftItemClickListener.onLeftItemClick(mData.get(0));
        }
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public void setLeftItemClickListener(OnLeftItemClickListener listener) {
        mLeftItemClickListener = listener;
    }

    public interface OnLeftItemClickListener {
        void onLeftItemClick(SelectedPageCategory.DataBean dataBean);
    }
}
