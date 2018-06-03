package zhuoyan.com.loginsample.base.fragment;

import com.base.baselibrary.base.BaseMvpView;
import com.base.baselibrary.base.IPresenter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by 15600 on 2017/7/3.
 */

public abstract class PFragment<P  extends IPresenter> extends AbstractFragment implements BaseMvpView {

    protected P mPresenter;

    @Override
    protected void custom() {
        initPresenter();
    }

    protected void initPresenter(){
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        Class<P> entityClass = (Class<P>) params[0];
        try {
            try {
                mPresenter=entityClass.newInstance();
            } catch (java.lang.InstantiationException e) {
                e.printStackTrace();
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
            throw new RuntimeException("InstantiationException 完蛋了");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("IllegalAccessException 完蛋了");
        }
        mPresenter.attachView(this,getActivity());
    }

    @Override
    public void onDestroyView() {
        if (mPresenter != null) mPresenter.detachView();
        super.onDestroyView();
    }

}
