package com.base.baselibrary.base;

import android.content.Context;

/**
 * Created by codeest on 2016/8/2.
 * 基于Rx的Presenter封装,控制订阅的生命周期
 */
public class BasePresenter<T extends BaseMvpView> implements IPresenter<T> {

    protected T mView;


    @Override
    public void attachView(T view, Context context) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        this.mView = null;
    }
}
