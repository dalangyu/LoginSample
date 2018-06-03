package com.base.baselibrary.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.base.baselibrary.R;
import com.base.baselibrary.view.adapter.Action;
import com.base.baselibrary.view.adapter.CommonAdapter;
import com.base.baselibrary.view.adapter.SpaceItemDecoration;


public class RefreshRecyclerView extends FrameLayout {

    private final String TAG = "RefreshRecyclerView";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private CommonAdapter mAdapter;
    private boolean refreshAble;
    private boolean loadMoreAble;

    public RefreshRecyclerView(Context context) {
        this(context, null);
    }

    public RefreshRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = inflate(context, R.layout.view_refresh_recycler, this);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RefreshRecyclerView);
        refreshAble = typedArray.getBoolean(R.styleable.RefreshRecyclerView_refresh_able, false);
        loadMoreAble = typedArray.getBoolean(R.styleable.RefreshRecyclerView_load_more_able, false);
        typedArray.recycle();
        if (!refreshAble) {
            mSwipeRefreshLayout.setEnabled(false);
        }
    }

    public void setAdapter(CommonAdapter adapter) {
        mRecyclerView.setAdapter(adapter);
        adapter.setLoadMoreAble(loadMoreAble);
        this.mAdapter = adapter;
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        mRecyclerView.setLayoutManager(layoutManager);
    }

    public void setRefreshAction(final Action action) {
        if(action!=null){
            refreshAble=true;
            mSwipeRefreshLayout.setEnabled(true);
        }
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.isRefreshing = true;
                mAdapter.isLoadingMore=true;
                mAdapter.isShowNoMore=false;
                action.onAction();
            }
        });
    }

    public void setLoadMoreAction(final Action action) {
        Log.i(TAG, "setLoadMoreAction");
        if (mAdapter.isShowNoMore) {
            return;
        }
        mAdapter.loadMoreAble = true;
        mAdapter.setLoadMoreAction(action);
    }

    public void showNoMore(final boolean isVisible) {
        mAdapter.showNoMore(isVisible);
    }

    public void showMore() {
        mAdapter.showMore();
    }

    public void setItemSpace(int left, int top, int right, int bottom) {
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(left, top, right, bottom));
    }

    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        mRecyclerView.addItemDecoration(itemDecoration);
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return mSwipeRefreshLayout;
    }

    public TextView getNoMoreView() {
        return mAdapter.mNoMoreView;
    }

    public void setSwipeRefreshColorsFromRes(@ColorRes int... colors) {
        mSwipeRefreshLayout.setColorSchemeResources(colors);
    }

    /**
     * 8位16进制数 ARGB
     */
    public void setSwipeRefreshColors(@ColorInt int... colors) {
        mSwipeRefreshLayout.setColorSchemeColors(colors);
    }

    public void showSwipeRefresh() {
        mAdapter.isRefreshing=true;
        mSwipeRefreshLayout.setRefreshing(true);
    }

    public void dismissSwipeRefresh() {
        mAdapter.isRefreshing=false;
        mSwipeRefreshLayout.setRefreshing(false);
    }


    public void setOnItemClick(CommonAdapter.ItemOnClickListener onItemClick) {
        mAdapter.setOnItemClick(onItemClick);
    }

    public void setOnItemLongClick(CommonAdapter.ItemOnLongClickListener onItemLongClick) {
        mAdapter.setOnItemLongClick(onItemLongClick);
    }

}