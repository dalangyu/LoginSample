package zhuoyan.com.loginsample.base.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.base.baselibrary.base.IErrorView;
import com.base.baselibrary.util.IntentUtils;
import com.base.baselibrary.util.LogUtils;
import com.base.baselibrary.util.ViewUtils;
import com.trycatch.mysnackbar.Prompt;
import com.trycatch.mysnackbar.TSnackbar;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

/**
 * Created by jiantao.tu on 2016/10/20.
 */

public abstract class AbstractFragment extends Fragment {


    //更改状态栏
    protected int statusBarColor = 0;


    protected @LayoutRes
    @NotNull
    int layoutId() {
        return -1;
    }

    protected FrameLayout rootView;

    protected IErrorView errorView;

    protected View mView;

    protected View lodingview;//是否允许控件可点击

    protected final String TAG = getClass().getName();

    protected View layoutView(LayoutInflater inflater, @Nullable ViewGroup container) {
        int id = layoutId();
        if (id != -1){
            mView=inflater.inflate(id, container, false);
            return mView;
        }
        return null;
    }

    protected abstract void initialize();

    protected IErrorView getErrorView() {
        return null;
    }

    private View.OnClickListener mErrorViewClick;

    protected void custom() {
    }

    protected FragmentActivity mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        custom();
        errorView = getErrorView();
        rootView = new FrameLayout(mContext);
        rootView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        View view = layoutView(inflater, container);
        rootView.addView(view);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LogUtils.i("ll view-type", getView().getClass().getName());

        //更改状态栏
      if (statusBarColor != -1&&statusBarColor!=0) {
            ViewUtils.INSTANCE.compat(mContext, ContextCompat.getColor(mContext,statusBarColor));
        }
        initialize();
    }

    @Override
    public void onAttach(Context context) {
        this.mContext = (FragmentActivity) context;
        super.onAttach(context);
    }

    protected boolean onBackPressed() {
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getView() != null) {
            getView().setFocusableInTouchMode(true);
            getView().requestFocus();
            getView().setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent
                            .KEYCODE_BACK) {
                        return onBackPressed();
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class
                    .getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void showErrorMsg(@NonNull String msg) {
//        if(null != getActivity().getCurrentFocus()){
//            InputMethodManager im = (InputMethodManager) getActivity().getSystemService(Context
// .INPUT_METHOD_SERVICE);
//            im.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
//        }
//        SnackbarUtil.show(((ViewGroup) getActivity().findViewById(android.R.id.content))
// .getChildAt(0), msg);
        TSnackbar.make(getActivity().findViewById(android.R.id.content).getRootView(), msg,
                TSnackbar.LENGTH_LONG, TSnackbar.APPEAR_FROM_TOP_TO_DOWN).setPromptThemBackground
                (Prompt.WARNING).show();

    }


    private TSnackbar snackBar;


    protected void loding(String msg, Boolean cancle) {


        stopLoading();
        if (msg == null) {
            msg = "正在加载，请稍后...";
        }



        snackBar = TSnackbar.make(getActivity().findViewById(android.R.id.content).getRootView(),
                msg, TSnackbar.LENGTH_INDEFINITE, TSnackbar.APPEAR_FROM_TOP_TO_DOWN);
        snackBar.setAction("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        snackBar.setPromptThemBackground(Prompt.SUCCESS);
        snackBar.addIconProgressLoading(0, true, false);
        snackBar.show();
    }

    public void startLoading() {
        loding(null, true);
    }

    public void startLoading(Boolean cancel) {
        loding(null, cancel);
    }

    public void startLoading(@NonNull String msg) {
        loding(msg, true);
    }


    public void startLoading(@NonNull String msg, Boolean cancel) {
        loding(msg, cancel);
    }



    public void stopLoading() {
        if (snackBar != null && snackBar.isShownOrQueued()) {
            snackBar.dismiss();
        }

        if(lodingview!=null){
            lodingview.setFocusable(true);
        }
    }

    public void showSuccessMsg(@NonNull String msg) {
        TSnackbar.make(getActivity().findViewById(android.R.id.content).getRootView(), msg,
                TSnackbar.LENGTH_LONG, TSnackbar.APPEAR_FROM_TOP_TO_DOWN).setPromptThemBackground
                (Prompt.SUCCESS).show();
    }

    public void showErrorView(@NonNull String msg) {
//        if (errorView == null) {
//            errorView = new NetErrorView(mContext);
//            if (mErrorViewClick != null) errorView.setReloadClick(mErrorViewClick);
//            rootView.addView(errorView.getView());
//        } else {
//            if (errorView.getView().getVisibility() == View.GONE)
//                errorView.getView().setVisibility(View.VISIBLE);
//        }
//        errorView.setErrorMsg(msg);

        TSnackbar.make(getActivity().findViewById(android.R.id.content).getRootView(), msg, TSnackbar
                .LENGTH_LONG, TSnackbar.APPEAR_FROM_TOP_TO_DOWN).setPromptThemBackground(Prompt
                .ERROR).show();
    }

    protected void setErrorViewClick(View.OnClickListener errorViewClick) {
        if (errorView != null) {
            errorView.setReloadClick(errorViewClick);
        }
        this.mErrorViewClick = errorViewClick;
    }

    public void hideErrorView() {
        if (errorView != null) {
            errorView.hideLoading();
            errorView.getView().setVisibility(View.GONE);
        }
    }

}
