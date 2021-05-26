package com.coolcr.taobaocoupon.ui.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.coolcr.taobaocoupon.R;
import com.coolcr.taobaocoupon.base.BaseFragment;
import com.coolcr.taobaocoupon.model.domain.Histories;
import com.coolcr.taobaocoupon.model.domain.HotWordsContent;
import com.coolcr.taobaocoupon.model.domain.SearchResult;
import com.coolcr.taobaocoupon.presenter.impl.SearchPresenterImpl;
import com.coolcr.taobaocoupon.ui.custom.TextFlowLayout;
import com.coolcr.taobaocoupon.utils.LogUtils;
import com.coolcr.taobaocoupon.utils.PresenterManger;
import com.coolcr.taobaocoupon.view.ISearchCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends BaseFragment implements ISearchCallback {

    @BindView(R.id.search_recommend_view)
    TextFlowLayout searchRecommendView;
    @BindView(R.id.search_history_container)
    LinearLayout searchHistoryContainer;

    @BindView(R.id.search_history_view)
    TextFlowLayout searchHistoryView;
    @BindView(R.id.search_recommend_container)
    LinearLayout searchRecommendContainer;

    @BindView(R.id.history_delete_btn)
    ImageView historyDeleteBtn;

    private SearchPresenterImpl mSearchPresenter;

    @Override
    protected void initPresenter() {
        mSearchPresenter = PresenterManger.getInstance().getSearchPresenter();
        mSearchPresenter.registerViewCallback(this);

        // 获取搜索推荐词
        mSearchPresenter.getHotWords();
        mSearchPresenter.doSearch("手机");
        mSearchPresenter.getHistories();
    }

    @Override
    protected void release() {
        if (mSearchPresenter != null) {
            mSearchPresenter.registerViewCallback(this);
        }
    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_search;
    }

    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_search_layout, container, false);
    }

    @Override
    protected void initView(View rootView) {
        setUpState(State.SUCCESS);
    }

    @Override
    protected void initListener() {
        historyDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //清空历史记录
                mSearchPresenter.delHistories();
            }
        });
    }

    @Override
    public void onHistoriesLoaded(Histories histories) {
        if (histories == null || histories.getHistories().size() == 0) {
            searchHistoryContainer.setVisibility(View.GONE);
        } else {
            searchHistoryContainer.setVisibility(View.VISIBLE);
            searchHistoryView.setTextList(histories.getHistories());
        }
    }

    @Override
    public void onHistoriesDeleted() {
        mSearchPresenter.getHistories();
    }

    @Override
    public void onSearchSuccess(SearchResult result) {
        LogUtils.d(this, "search result size -- > " + result.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data().size());
    }

    @Override
    public void onMoreLoaded(SearchResult result) {

    }

    @Override
    public void onMoreLoadError() {

    }

    @Override
    public void onMoreLoadEmpty() {

    }

    @Override
    public void getHotWordsSuccess(List<HotWordsContent.DataBean> hotWords) {
        LogUtils.d(this, "hot words -- > " + hotWords.toString());
        List<String> recommendWords = new ArrayList<>();
        for (HotWordsContent.DataBean hotWord : hotWords) {
            recommendWords.add(hotWord.getKeyword());
        }
        if (hotWords == null || hotWords.size() == 0) {
            searchRecommendContainer.setVisibility(View.GONE);
        } else {
            searchRecommendContainer.setVisibility(View.VISIBLE);
            searchRecommendView.setTextList(recommendWords);
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
