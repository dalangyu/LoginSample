package zhuoyan.com.loginsample.base.activity;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


public abstract class RxBindingActivity<T extends ViewDataBinding> extends AbstractActivity {

    protected T binding;

    protected final String TAG=getClass().getName();

    @Override
    protected int loadMode() {
        return LOAD_MODE_DATABINDING;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mCompositeDisposable = new CompositeDisposable();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void layout() {
        binding = DataBindingUtil.setContentView(this, layoutId());
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
    protected void onDestroy() {
        unSubscribe();
        super.onDestroy();
    }

}
