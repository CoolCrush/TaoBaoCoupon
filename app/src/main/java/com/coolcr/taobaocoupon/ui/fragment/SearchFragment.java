package com.coolcr.taobaocoupon.ui.fragment;

import android.graphics.Rect;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.coolcr.taobaocoupon.R;
import com.coolcr.taobaocoupon.base.BaseFragment;
import com.coolcr.taobaocoupon.model.domain.Histories;
import com.coolcr.taobaocoupon.model.domain.HotWordsContent;
import com.coolcr.taobaocoupon.model.domain.IBaseInfo;
import com.coolcr.taobaocoupon.model.domain.SearchResult;
import com.coolcr.taobaocoupon.presenter.impl.SearchPresenterImpl;
import com.coolcr.taobaocoupon.ui.adapter.LinearItemContentAdapter;
import com.coolcr.taobaocoupon.ui.custom.TextFlowLayout;
import com.coolcr.taobaocoupon.utils.KeyboardUtil;
import com.coolcr.taobaocoupon.utils.LogUtils;
import com.coolcr.taobaocoupon.utils.PresenterManger;
import com.coolcr.taobaocoupon.utils.SizeUtils;
import com.coolcr.taobaocoupon.utils.TicketUtil;
import com.coolcr.taobaocoupon.utils.ToastUtil;
import com.coolcr.taobaocoupon.view.ISearchCallback;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends BaseFragment implements ISearchCallback, TextFlowLayout.OnFlowTextItemClickListener {

    @BindView(R.id.search_input_box)
    EditText searchInputBox;
    @BindView(R.id.search_btn)
    TextView searchBtn;
    @BindView(R.id.search_remove_img)
    ImageView searchRemoveImg;

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

    @BindView(R.id.search_result_list)
    RecyclerView searchResultList;

    @BindView(R.id.search_result_container)
    TwinklingRefreshLayout searchRefreshLayout;

    private SearchPresenterImpl mSearchPresenter;
    private LinearItemContentAdapter mSearchResultAdapter;

    @Override
    protected void initPresenter() {
        mSearchPresenter = PresenterManger.getInstance().getSearchPresenter();
        mSearchPresenter.registerViewCallback(this);

        // 获取搜索推荐词
        mSearchPresenter.getHotWords();
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
        searchResultList.setLayoutManager(new LinearLayoutManager(getContext()));
        mSearchResultAdapter = new LinearItemContentAdapter();
        searchResultList.setAdapter(mSearchResultAdapter);
        searchResultList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = SizeUtils.dip2px(getContext(), 2);
                outRect.bottom = SizeUtils.dip2px(getContext(), 2);
            }
        });
        //刷新控件
        searchRefreshLayout.setEnableRefresh(false);
        searchRefreshLayout.setEnableLoadmore(true);
        searchRefreshLayout.setEnableOverScroll(true);
    }

    @Override
    protected void initListener() {
        searchHistoryView.setOnFlowTextItemClickListener(this);
        searchRecommendView.setOnFlowTextItemClickListener(this);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = searchInputBox.getText().toString().trim();
                if (searchInputBox.getText().toString().trim().length() > 0) {
                    mSearchPresenter.doSearch(keyword);
                    searchResultList.scrollToPosition(0);
                    KeyboardUtil.hide(getContext(), v);
                } else {
                    KeyboardUtil.hide(getContext(), v);
                }
            }
        });
        historyDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //清空历史记录
                mSearchPresenter.delHistories();
            }
        });

        mSearchResultAdapter.setOnListItemClickListener(new LinearItemContentAdapter.OnListItemClickListener() {
            @Override
            public void onItemClick(IBaseInfo item) {
                //item被点击了
                TicketUtil.toTicketPage(getContext(), item);
            }
        });

        searchRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                if (mSearchPresenter != null) {
                    mSearchPresenter.loaderMore();
                }
            }
        });
        searchRemoveImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchInputBox.setText("");
                //回到历史记录页面
                switch2HistoryPage();
            }
        });
        searchInputBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() > 0) {
                    searchRemoveImg.setVisibility(View.VISIBLE);
                    searchBtn.setText("搜索");
                } else {
                    searchRemoveImg.setVisibility(View.GONE);
                    searchBtn.setText("取消");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        searchInputBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                LogUtils.d(this, "actionId -- > " + actionId);
                if (actionId == EditorInfo.IME_ACTION_SEARCH && mSearchPresenter != null) {
                    //点击键盘上的搜索按钮
                    String searchWord = v.getText().toString().trim();
                    if (TextUtils.isEmpty(searchWord)) {
                        return false;
                    }
                    LogUtils.d(this, "searchWord -- > " + searchWord);
                    mSearchPresenter.doSearch(searchWord);
                    searchResultList.scrollToPosition(0);
                }
                return false;
            }
        });
    }

    /**
     * 切换历史和推荐页面
     */
    private void switch2HistoryPage() {

        if (mSearchPresenter != null) {
            mSearchPresenter.getHistories();
        }


        List<String> recommendList = searchRecommendView.getTextList();
        if (recommendList.size() != 0) {
            searchRecommendContainer.setVisibility(View.VISIBLE);
        }
        searchResultList.setVisibility(View.GONE);
    }

    @Override
    protected void onRetryClick() {
        //重新加载
        if (mSearchPresenter != null) {
            mSearchPresenter.reSearch();
        }
    }

    @Override
    public void onHistoriesLoaded(Histories histories) {
        setUpState(State.SUCCESS);
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

    /**
     * 获取数据成功
     *
     * @param result
     */
    @Override
    public void onSearchSuccess(SearchResult result) {
        setUpState(State.SUCCESS);
        LogUtils.d(this, "search result size -- > " + result.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data().size());
        //隐藏掉历史记录和推荐
        searchHistoryContainer.setVisibility(View.GONE);
        searchRecommendContainer.setVisibility(View.GONE);
        searchResultList.setVisibility(View.VISIBLE);
        //给adapter设置数据，显示数据
        mSearchResultAdapter.setData(result.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data());
    }

    @Override
    public void onMoreLoaded(SearchResult result) {
        //加载更多成功
        mSearchResultAdapter.addData(result.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data());
        searchRefreshLayout.finishLoadmore();
    }

    @Override
    public void onMoreLoadError() {
        ToastUtil.showToast("网络异常，请稍后重试");
    }

    @Override
    public void onMoreLoadEmpty() {
        ToastUtil.showToast("没有更多数据...");
    }

    @Override
    public void getHotWordsSuccess(List<HotWordsContent.DataBean> hotWords) {
        setUpState(State.SUCCESS);
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
        setUpState(State.LOADING);
    }

    @Override
    public void onError() {
        setUpState(State.ERROR);
    }

    @Override
    public void onEmpty() {
        setUpState(State.EMPTY);
    }

    /**
     * 点击热门搜索，历史搜素
     *
     * @param text
     */
    @Override
    public void onFlowItemClick(String text) {
        if (mSearchPresenter != null) {
            searchResultList.scrollToPosition(0);
            mSearchPresenter.doSearch(text);
            searchInputBox.setText(text);
        }
    }
}
