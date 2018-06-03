package zhuoyan.com.loginsample.base.fragment;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.base.baselibrary.BaseApp;
import com.google.gson.Gson;

/**
 * Created by jiantao.tu on 2016/10/20.
 */

public abstract class BindingFragment<T extends ViewDataBinding> extends AbstractFragment {

    protected T binding;

    protected Gson gson= BaseApp.getApp().gson;


    @Override
    protected View layoutView(LayoutInflater inflater, @Nullable ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, layoutId(), container, false);
        return binding.getRoot();
    }
}
