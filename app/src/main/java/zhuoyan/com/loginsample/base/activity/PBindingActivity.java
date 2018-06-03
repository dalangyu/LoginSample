package zhuoyan.com.loginsample.base.activity;


import android.databinding.ViewDataBinding;

import com.base.baselibrary.base.BaseMvpView;
import com.base.baselibrary.base.IPresenter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by 15600 on 2017/7/3.
 */

public abstract class PBindingActivity<P  extends IPresenter, T extends ViewDataBinding>
        extends BindingActivity<T> implements BaseMvpView {

    protected P mPresenter;

    protected int loadMode(){
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        Class<P> entityClass = (Class<P>) params[0];
        try {
            mPresenter=entityClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
            throw new RuntimeException("InstantiationException 完蛋了");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("IllegalAccessException 完蛋了");
        }
        mPresenter.attachView(this,this);
        return super.loadMode();
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null)
            mPresenter.detachView();
        super.onDestroy();
    }

}
