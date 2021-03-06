package com.base.baselibrary.base;

import android.support.annotation.NonNull;
import android.view.View;

/**
 * Created by codeest on 2016/8/2.
 * View基类
 */
public interface BaseMvpView {

    void showErrorMsg(@NonNull String msg);

    void showSuccessMsg(@NonNull String msg);

    ///////////////////////////

    void startLoading(@NonNull String msg);

    void startLoading(@NonNull Boolean cancel);

    void startLoading(@NonNull String msg,@NonNull Boolean cancel);

    void startLoading();

    void stopLoading();

    void showErrorView(@NonNull String msg);

    void hideErrorView();
}
