package zhuoyan.com.loginsample.base.fragment;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.base.baselibrary.BaseApp;
import com.google.gson.Gson;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by jiantao.tu on 2016/10/20.
 */

public abstract class RxBindingFragment<T extends ViewDataBinding> extends AbstractFragment {

    protected T binding;

    protected Gson gson= BaseApp.getApp().gson;


    @Override
    protected View layoutView(LayoutInflater inflater, @Nullable ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, layoutId(), container, false);
        return binding.getRoot();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mCompositeDisposable = new CompositeDisposable();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected CompositeDisposable mCompositeDisposable;

    protected void unSubscribe() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
            mCompositeDisposable = null;
        }
    }

    protected void addSubscribe(Disposable subscription) {
        mCompositeDisposable.add(subscription);
    }

    @Override
    public void onDestroyView() {
        unSubscribe();
        super.onDestroyView();
    }
}
