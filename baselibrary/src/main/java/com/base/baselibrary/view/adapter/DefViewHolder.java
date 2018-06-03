package com.base.baselibrary.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.HashMap;

/**
 * Created by 15600 on 2017/6/22.
 */

public class DefViewHolder extends RecyclerView.ViewHolder{

    private HashMap _$_findViewCache;

    private int viewType;


    public DefViewHolder(View view, int viewType) {
        super(view);
        this.viewType=viewType;
    }

    public int getViewType() {
        return viewType;
    }

    public View findCachedViewById(int var1) {
        if (this._$_findViewCache == null) {
            this._$_findViewCache = new HashMap();
        }
        View var2 = (View) this._$_findViewCache.get(Integer.valueOf(var1));
        if (var2 == null) {
            View var10000 = itemView;
            if (var10000 == null) {
                return null;
            }
            var2 = var10000.findViewById(var1);
            this._$_findViewCache.put(Integer.valueOf(var1), var2);
        }
        return var2;
    }

    public void clearFindViewByIdCache() {
        if (this._$_findViewCache != null) {
            this._$_findViewCache.clear();
        }

    }

}
