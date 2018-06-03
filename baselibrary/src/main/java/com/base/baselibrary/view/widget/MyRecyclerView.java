package com.base.baselibrary.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.base.baselibrary.R;
import com.base.baselibrary.view.adapter.Action;
import com.base.baselibrary.view.adapter.CommonAdapter;
import com.base.baselibrary.view.adapter.SpaceItemDecoration;

/**
 * Created by 15600 on 2017/6/9.
 */

public class MyRecyclerView extends RecyclerView {


    public final static String TAG=MyRecyclerView.class.getSimpleName();
    private CommonAdapter mAdapter;
    private boolean loadMoreAble;

    public MyRecyclerView(Context context) {
        this(context, null);
    }

    public MyRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyRecyclerView);
        loadMoreAble = typedArray.getBoolean(R.styleable.MyRecyclerView_my_load_more_able, false);
        typedArray.recycle();
    }

    public void setAdapter(CommonAdapter adapter) {
        super.setAdapter(adapter);
        adapter.setLoadMoreAble(loadMoreAble);
        this.mAdapter = adapter;
    }

    public void setLoadMoreAction(final Action action) {
        Log.i(TAG, "setLoadMoreAction");
        if (mAdapter.isShowNoMore || !loadMoreAble) {
            return;
        }
        mAdapter.loadMoreAble = true;
        mAdapter.setLoadMoreAction(action);
    }

    public void setItemSpace(int left, int top, int right, int bottom) {
        addItemDecoration(new SpaceItemDecoration(left, top, right, bottom));
    }
    public void showNoMore() {
        mAdapter.showNoMore(true);
    }


    public TextView getNoMoreView() {
        return mAdapter.mNoMoreView;
    }

    public void setOnItemClick(CommonAdapter.ItemOnClickListener onItemClick) {
        mAdapter.setOnItemClick(onItemClick);
    }

    public void setOnItemLongClick(CommonAdapter.ItemOnLongClickListener onItemLongClick) {
        mAdapter.setOnItemLongClick(onItemLongClick);
    }
}
