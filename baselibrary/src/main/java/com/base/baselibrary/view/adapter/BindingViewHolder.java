/*
* @Title ViewHolder.java
* @Copyright Copyright 2010-2015 Yann Software Co,.Ltd All Rights Reserved.
* @Description��
* @author Yann
* @date 2015-8-5 ����9:08:31
* @version 1.0
*/
package com.base.baselibrary.view.adapter;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;


public class BindingViewHolder extends RecyclerView.ViewHolder{

    private ViewDataBinding binding;

    private int viewType;


    public BindingViewHolder(ViewDataBinding binding, int viewType) {
        super(binding.getRoot());
        this.binding = binding;
        this.viewType=viewType;
    }


    public <T extends ViewDataBinding> T getBinding() {
        return (T)binding;
    }

    public int getViewType() {
        return viewType;
    }
}
