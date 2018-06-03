package zhuoyan.com.loginsample.base.activity;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;


public abstract class BindingActivity<T extends ViewDataBinding> extends AbstractActivity {

    protected T binding;

    protected final String TAG=getClass().getName();

    @Override
    protected int loadMode() {
        return LOAD_MODE_DATABINDING;
    }

    @Override
    protected void layout() {
        binding = DataBindingUtil.setContentView(this, layoutId());
    }

}
