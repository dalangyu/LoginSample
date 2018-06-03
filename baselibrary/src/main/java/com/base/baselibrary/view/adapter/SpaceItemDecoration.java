package com.base.baselibrary.view.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by linlongxin on 2015/12/19.
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int top;
    private int left;
    private int right;
    private int bottom;

    public SpaceItemDecoration(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if(parent.getChildPosition(view) != 0){
            outRect.top = top;
        }
        outRect.left = left;
        outRect.right = right;
        outRect.bottom = bottom;

    }
}