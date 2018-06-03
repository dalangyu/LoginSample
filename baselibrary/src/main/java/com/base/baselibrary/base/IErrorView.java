package com.base.baselibrary.base;

import android.view.View;

/**
 * Created by 15600 on 2017/8/16.
 */

public interface IErrorView {

    void setReloadClick(final View.OnClickListener clickListener);

    void showLoading();

    void hideLoading();

    void setErrorMsg(String msg);

    View getView();
}
