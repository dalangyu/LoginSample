package com.base.baselibrary.base;

import android.content.Context;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by codeest on 2016/8/2.
 * 基于Rx的Presenter封装,控制订阅的生命周期
 */
public class RxPresenter<T extends BaseMvpView> extends BasePresenter<T> {

    public T mView;

    protected Context mContext;

    protected CompositeDisposable mCompositeDisposable;

    public RxPresenter() {
        mCompositeDisposable = new CompositeDisposable();
    }

    protected void unSubscribe() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
            mCompositeDisposable = null;
        }
    }

    public void addSubscribe(Disposable subscription) {
        mCompositeDisposable.add(subscription);
    }

    @Override
    public void attachView(T view, Context context) {
        this.mView = view;
        mContext = context;
    }

    @Override
    public void detachView() {
        this.mView = null;
        this.mContext = null;
        unSubscribe();
    }
}
