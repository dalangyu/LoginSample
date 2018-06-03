package com.base.baselibrary.base;

import android.content.Context;

/**
 * Created by codeest on 2016/8/2.
 * Presenter基类
 */
public interface IPresenter<T extends BaseMvpView>{

    void attachView(T view, Context context);

    void detachView();
}
