package com.base.baselibrary.model.http;

import android.support.annotation.NonNull;
import android.util.Log;

import com.base.baselibrary.base.BaseMvpView;
import com.base.baselibrary.base.RxPresenter;
import com.base.baselibrary.model.entity.BaseResult;
import com.base.baselibrary.model.entity.BaseResultChannel;
import com.base.baselibrary.util.LogUtils;
import com.base.baselibrary.util.StringUtils;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;

/**
 * BaseObserver
 * Created by langlang on 2017/6/23.
 */
public abstract class BaseObserverChannel<T> implements Observer<BaseResultChannel<T>>,
        Subscriber<BaseResultChannel<T>> {

    private static final String TAG = "BaseObserver";

    private BaseMvpView mView;

    private RxPresenter<BaseMvpView> mPresenter;

    private boolean isShowErrorView = false;

    public BaseObserverChannel(@NonNull BaseMvpView view, Boolean... isShowErrorView) {
        this(isShowErrorView);
        this.mView = view;
    }

    public <R extends BaseMvpView> BaseObserverChannel(@NonNull RxPresenter<R> presenter, Boolean...
            isShowErrorView) {
        this(isShowErrorView);
        this.mPresenter = (RxPresenter<BaseMvpView>) presenter;

    }

    public BaseObserverChannel(Boolean... isShowErrorView) {
        if (isShowErrorView.length > 0) {
            this.isShowErrorView = isShowErrorView[0];
        }
    }

    private boolean checkedNet() {
//        if (!AppUtil.isNetworkAvailable(App.getApp())) {//接下来可以检查网络连接等操作
//            ToastUtils.showLongToast("当前网络不可用，请检查网络情况");
//            onComplete();// 一定好主动调用下面这一句
//            return false;
//        }
        return true;
    }

    @Override
    public void onSubscribe(Disposable d) {
        if (checkedNet()) {
            if (mPresenter != null) {
                mPresenter.addSubscribe(d);
            }
        } else {
            LogUtils.i("hllMain -observer", Thread.currentThread().getName());
            BaseMvpView view = getBaseView();
            if (view != null) {
                if (isShowErrorView) {
                    view.hideErrorView();
                    view.showErrorView("网络异常~");
                }
                view.stopLoading();
            }
            d.dispose();
        }
    }

    @Override
    public void onSubscribe(Subscription s) {
        if (checkedNet()) {
            if (mPresenter != null) {
                mPresenter.addSubscribe(Disposables.fromSubscription(s));
            }
        }

    }

    private BaseMvpView getBaseView() {
        if (mView != null) {
            return mView;
        } else if (mPresenter != null && mPresenter.mView != null) {
            return mPresenter.mView;
        }
        return null;
    }

    @Override
    public void onNext(BaseResultChannel<T> value) {
        BaseMvpView view = getBaseView();
        if (view != null) {
            view.stopLoading();
            //此处可以统一关闭错误提示视图
            view.hideErrorView();
        }
        if (value.isSuccess()) {
            T t = value.getData();
            boolean isDataNull = false;
            if (t != null) {
                if (t instanceof List) {
                    if (!((List) t).isEmpty()) {
                        onDataNoNullOK(t);
                    } else {
                        isDataNull = true;
                    }
                } else {
                    onDataNoNullOK(t);
                }
            } else {
                isDataNull = true;
            }
            if (isDataNull && view != null) {
//                view.showErrorMsg("无法解析数据");
            }
            onHandleSuccess(t);
        } else {
            onHandleError(value.getMsg());
        }
    }

    @Override
    public void onError(Throwable e) {
        BaseMvpView view = getBaseView();
        if (view != null) {
            view.stopLoading();
            //此处可以统一关闭错误提示视图
            view.hideErrorView();
        }
        if (e instanceof Exception) {
            //访问获得对应的Exception
            onError(ExceptionHandle.handleException(e));
        } else {
            //将Throwable 和 未知错误的status code返回
            onError(new ExceptionHandle.ResponeThrowable(e, ExceptionHandle.ERROR.UNKNOWN));
        }
        Log.e(TAG, "onError:" + e.toString());
    }

    @Override
    public void onComplete() {
        Log.d(TAG, "onComplete");
    }

    protected void onHandleSuccess(T t) {

    }

    protected void onDataNoNullOK(@NonNull T t) {

    }

    public void onError(ExceptionHandle.ResponeThrowable responeThrowable) {
        //接下来就可以根据状态码进行处理...
        int statusCode = responeThrowable.code;
        switch (statusCode) {
            case ExceptionHandle.ERROR.SSL_ERROR:
                break;
            case ExceptionHandle.ERROR.UNKNOWN:
                break;
            case ExceptionHandle.ERROR.PARSE_ERROR:
//                BaseMvpView view = getBaseView();
//                if (view != null) {
//                    view.stopLoading();
//                    //此处可以统一关闭错误提示视图
//                    view.hideErrorView();
//                }
                break;
            case ExceptionHandle.ERROR.NETWORD_ERROR:
                break;
            case ExceptionHandle.ERROR.HTTP_ERROR:
                break;
        }
        BaseMvpView view = getBaseView();
        if (view != null) {
            view.showErrorMsg("网络异常~");
            //因为有很多比如添加，删除数据等等不是加载一类的请求，所以最好不要统一处理
            if (isShowErrorView) {
                view.showErrorView("网络异常~");
            }
        }
    }

    protected void onHandleError(String msg) {
        BaseMvpView view = getBaseView();
        if (view != null) {
            if (!StringUtils.isEmpty(msg)) {

                view.showErrorView(msg);
//
//                //因为有很多比如添加，删除数据等等不是加载一类的请求，所以最好不要统一处理
//                if (isShowErrorView) {
//                    view.showErrorView(msg);
//                }else{
//                    view.showErrorMsg(msg);
//                }
            }
        }
    }


}
