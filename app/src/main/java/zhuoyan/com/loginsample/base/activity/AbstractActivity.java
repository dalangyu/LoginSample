package zhuoyan.com.loginsample.base.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.base.baselibrary.BaseApp;
import com.base.baselibrary.base.IErrorView;
import com.base.baselibrary.util.ActManager;
import com.base.baselibrary.util.SnackbarUtil;
import com.base.baselibrary.util.ViewUtils;
import com.google.gson.Gson;
import com.trycatch.mysnackbar.Prompt;
import com.trycatch.mysnackbar.TSnackbar;

import org.greenrobot.greendao.annotation.NotNull;

import zhuoyan.com.loginsample.R;


//import com.base.baselibrary.BaseApp;

/**
 * Created by 15600 on 2017/6/19.
 */

public abstract class AbstractActivity extends AppCompatActivity {

    private final static int READ_PHONE_STATE_CODE = 101;
    private final static int WRITE_EXTERNAL_STORAGE_CODE = 102;
    private final static int RECORD_AUDIO_CODE = 103;
    private final static int RECORD_CAMERA_CODE = 104;
    private final static int RECORD_LOCATION_CODE = 105;
    private static PermissionModel[] models = new PermissionModel[]{
            new PermissionModel(Manifest.permission.READ_PHONE_STATE, "我们需要读取手机信息的权限来标识您的身份",
                    READ_PHONE_STATE_CODE),
            new PermissionModel(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    "我们需要您允许我们读写你的存储卡，以方便我们临时保存一些数据",
                    WRITE_EXTERNAL_STORAGE_CODE),
            new PermissionModel(Manifest.permission.RECORD_AUDIO,
                    "我们需要您允许录制音频的权限，以方便您进行聊天",
                    RECORD_AUDIO_CODE),
            new PermissionModel(Manifest.permission.CAMERA,
                    "我们需要您允许录制音频的权限，以方便您进行聊天",
                    RECORD_CAMERA_CODE),
            new PermissionModel(Manifest.permission.ACCESS_FINE_LOCATION,
                    "我们需要您允许获取位置的权限，以方便我们保存正确的数据",
                    RECORD_LOCATION_CODE)
    };

    protected Gson gson = BaseApp.getApp().gson;

    protected final String TAG = getClass().getName();

    private final String key = this.getClass().getCanonicalName();

    protected FragmentActivity mContext;

    protected View mView;
    protected View lodingview;//是否允许控件可点击

    protected View statusBarView = null;

    protected int statusBarColor = 0;

    protected boolean isFistIn = true;

    protected final static int LOAD_MODE_DEFAULT = 1;

    protected final static int LOAD_MODE_DATABINDING = 2;

    protected IErrorView errorView;

    protected IErrorView getErrorView() {
        return null;
    }

    protected int loadMode() {
        return LOAD_MODE_DEFAULT;
    }

    protected @LayoutRes
    @NotNull
    int layoutId() {
        return -1;
    }

    protected View layoutView() {
        return null;
    }

    protected void layout() {
    }

    protected abstract void initialize();

    protected boolean isCheckPermissions() {
        return false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ///
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build
                .VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager
                    .LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        ///

        mContext = this;
        final int mode = loadMode();
        if (mode == LOAD_MODE_DATABINDING) {
            layout();
        } else if (mode == LOAD_MODE_DEFAULT) {
            int id = layoutId();
            View view = layoutView();
            if (id != -1) {
                mView = View.inflate(mContext, layoutId(), null);
                setContentView(mView);
            } else if (view != null) {
                mView = view;
                setContentView(view);
            }
        }
        if (statusBarColor == 0) {
            statusBarView = ViewUtils.INSTANCE.compat(this, ContextCompat.getColor(this, R.color
                    .colorPrimaryDark));
        } else if (statusBarColor != -1) {
            statusBarView = ViewUtils.INSTANCE.compat(this, ContextCompat.getColor(this,statusBarColor));

        }
        errorView = getErrorView();
//        StatusBarUtils.setStatusBar(this);
        if (isCheckPermissions())
            checkPermissions();
        else
            initialize();
        ActManager.getInstance().addActivity(this);
    }


    /**
     * 这里我们演示如何在Android 6.0+上运行时申请权限
     */
    private void checkPermissions() {
        try {
            for (PermissionModel model : models) {
                if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this,
                        model.permission)) {
                    ActivityCompat.requestPermissions(this, new String[]{model.permission}, model
                            .requestCode);
                    return;
                }
            }
        } catch (Throwable e) {
            Log.e(TAG, "测试权限问题", e);
        }
        init();

    }

    private void init() {
        initialize();
        isFistIn = false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RECORD_CAMERA_CODE:
            case RECORD_LOCATION_CODE:
            case RECORD_AUDIO_CODE:
            case READ_PHONE_STATE_CODE:
            case WRITE_EXTERNAL_STORAGE_CODE:
                // 如果用户不允许，我们视情况发起二次请求或者引导用户到应用页面手动打开
                if (PackageManager.PERMISSION_GRANTED != grantResults[0]) {

                    // 二次请求，表现为：以前请求过这个权限，但是用户拒接了
                    // 在二次请求的时候，会有一个“不再提示的”checkbox
                    // 因此这里需要给用户解释一下我们为什么需要这个权限，否则用户可能会永久不在激活这个申请
                    // 方便用户理解我们为什么需要这个权限
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                        new android.app.AlertDialog.Builder(this).setTitle("权限申请").setMessage
                                (findPermissionExplain(permissions[0]))
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        checkPermissions();
                                    }
                                })
                                .show();
                    }
                    // 到这里就表示已经是第3+次请求，而且此时用户已经永久拒绝了，这个时候，我们引导用户到应用权限页面，让用户自己手动打开
                    else {
                        Toast.makeText(this, "部分权限被拒绝获取，将会会影响后续功能的使用，建议重新打开", Toast.LENGTH_LONG)
                                .show();
                        this.finish();//先关了
                        openAppPermissionSetting(123456789);
                    }
                    return;
                }

                // 到这里就表示用户允许了本次请求，我们继续检查是否还有待申请的权限没有申请
                if (isAllRequestedPermissionGranted() == null) {
                    init();
                } else {
                    checkPermissions();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        switch (requestCode) {
            case 123456789:
                if (isAllRequestedPermissionGranted() == null) {
                    init();
                } else {
                    Toast.makeText(this, "部分权限被拒绝获取，退出", Toast.LENGTH_LONG).show();
                    this.finish();
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private String findPermissionExplain(String permission) {
        if (models != null) {
            for (PermissionModel model : models) {
                if (model != null && model.permission != null && model.permission.equals
                        (permission)) {
                    return model.explain;
                }
            }
        }
        return null;
    }

    private PermissionModel isAllRequestedPermissionGranted() {
        for (final PermissionModel model : models) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this,
                    model.permission)) {
                return model;
            }
        }
        return null;
    }

    private boolean openAppPermissionSetting(int requestCode) {
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse
                    ("package:" + this.getPackageName()));
            intent.addCategory(Intent.CATEGORY_DEFAULT);

            // Android L 之后Activity的启动模式发生了一些变化
            // 如果用了下面的 Intent.FLAG_ACTIVITY_NEW_TASK ，并且是 startActivityForResult
            // 那么会在打开新的activity的时候就会立即回调 onActivityResult
            // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivityForResult(intent, requestCode);
            return true;
        } catch (Throwable ignore) {
        }
        return false;
    }

    public Fragment findFragmentById(@IdRes int id) {
        return getSupportFragmentManager().findFragmentById(id);
    }

    public void replaceFragment(@IdRes int id, Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(id, fragment).commit();
    }

    public void showErrorMsg(@NonNull String msg) {

        if(null != this.getCurrentFocus()){
            InputMethodManager im = (InputMethodManager) getSystemService(Context
 .INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }

        SnackbarUtil.show(((ViewGroup) findViewById(android.R.id.content)).getChildAt(0), msg);
//
//        TSnackbar.make(findViewById(android.R.id.content).getRootView(), msg, TSnackbar
//                .LENGTH_LONG, TSnackbar.APPEAR_FROM_TOP_TO_DOWN).setPromptThemBackground(Prompt
//                .ERROR).show();

    }


    private TSnackbar snackBar;

    protected void loding(String msg, Boolean cancle) {

        if (msg == null) {
            msg = "正在拼命连接，请稍后...";
        }

        snackBar = TSnackbar.make(findViewById(android.R.id.content).getRootView(), msg,
                TSnackbar.LENGTH_INDEFINITE, TSnackbar.APPEAR_FROM_TOP_TO_DOWN);
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

        if (lodingview != null) {
            lodingview.setFocusable(true);
            lodingview.setClickable(true);
        }
    }


    public void dismissSnackBar() {
        if (snackBar != null)
            snackBar.dismiss();
    }

    public void showSuccessMsg(@NonNull String msg) {
        TSnackbar.make(findViewById(android.R.id.content).getRootView(), msg, TSnackbar
                .LENGTH_LONG, TSnackbar.APPEAR_FROM_TOP_TO_DOWN).setPromptThemBackground(Prompt
                .SUCCESS).show();
    }

    public void showErrorView(@NonNull String msg) {
        if (errorView != null) {
            if (errorView.getView().getVisibility() == View.GONE)
                errorView.getView().setVisibility(View.VISIBLE);

            errorView.setErrorMsg(msg);
        } else {

            showErrorMsg(msg);
        }

    }

    public void hideErrorView() {
        if (errorView != null) {
            errorView.hideLoading();
            errorView.getView().setVisibility(View.GONE);
        }
    }

    protected boolean isVisible(View view) {
        return view.getVisibility() == View.VISIBLE;
    }

    protected void hideStatusBar() {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
        if (statusBarView != null) {
            statusBarView.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    protected void showStatusBar() {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
        if (statusBarView != null) {
            statusBarView.setBackgroundColor(statusBarColor);
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
    }

    public void destroy() {
        finish();
    }

    protected void gone(final View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                if (view != null) {
                    view.setVisibility(View.GONE);
                }
            }
        }
    }

    protected void visible(final View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                if (view != null) {
                    view.setVisibility(View.VISIBLE);
                }
            }
        }

    }

    @Override
    protected void onDestroy() {
//        ((MainApplication) getApplication()).removeActivity(key);
        super.onDestroy();
    }

    public static class PermissionModel {

        /**
         * 请求的权限
         */
        String permission;

        /**
         * 解析为什么请求这个权限
         */
        String explain;

        /**
         * 请求代码
         */
        int requestCode;

        PermissionModel(String permission, String explain, int requestCode) {
            this.permission = permission;
            this.explain = explain;
            this.requestCode = requestCode;
        }
    }


}
