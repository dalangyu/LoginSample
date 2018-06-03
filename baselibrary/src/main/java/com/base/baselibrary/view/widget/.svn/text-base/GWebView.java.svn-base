package com.base.baselibrary.view.widget;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.base.baselibrary.util.JsInterface;
import com.base.baselibrary.util.ScreenUtils;


public class GWebView extends WebView {

    private WebControler controler;
    private ProgressBar progress;
    private boolean needBack = true;

    public void setNeedBack(boolean needBack) {
        this.needBack = needBack;
    }

    public GWebView(Context context) {
        super(context);
        initialize(context);
    }

    private void initialize(Context context) {
        progress = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progress.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, ScreenUtils.dip2px
                (3), 0, 0));
//        GradientDrawable gd = new GradientDrawable();// 创建drawable
//        gd.setColor(Color.parseColor("#33b5e5"));
//        progress.setProgressDrawable(gd);
        ClipDrawable d = new ClipDrawable(new ColorDrawable(Color.parseColor("#33b5e5")), Gravity
                .LEFT, ClipDrawable.HORIZONTAL);
        progress.setProgressDrawable(d);
        addView(progress);
        initJavaScript();
        setWebChromeClient(new WebChromeClient());
    }

    public GWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public GWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public GWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize(context);
    }

    public WebControler getControler() {
        return controler;
    }

    public void setControler(WebControler controler) {
        this.controler = controler;
    }

    @SuppressLint({"AddJavascriptInterface", "SetJavaScriptEnabled"})
    @SuppressWarnings("deprecation")
    private void initJavaScript() {
        WebSettings settings = this.getSettings();
        if (null != settings) {
            settings.setJavaScriptEnabled(true);
            settings.setDefaultTextEncodingName("UTF-8");
            settings.setJavaScriptCanOpenWindowsAutomatically(true);

            settings.setUseWideViewPort(true);  //将图片调整到适合webview的大小
            settings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
            settings.setSupportZoom(true);  //支持缩放，默认为true。是下面那个的前提。
            settings.setBuiltInZoomControls(true); //设置内置的缩放控件。
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                settings.setDisplayZoomControls(false); //隐藏原生的缩放控件
            }

            settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); //支持内容重新布局
            settings.supportMultipleWindows();  //多窗口
            settings.setAllowFileAccess(true);  //设置可以访问文件
            settings.setNeedInitialFocus(true); //当webview调用requestFocus时为webview设置节点
//            settings.setSupportMultipleWindows(false);
            settings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
            settings.setLoadsImagesAutomatically(true);  //支持自动加载图片
        }
        //支持获取手势焦点，输入用户名、密码或其他
        requestFocusFromTouch();
        this.setHorizontalScrollBarEnabled(false);
        this.setHorizontalFadingEdgeEnabled(false);
        this.setHorizontalScrollbarOverlay(false);
        this.setVerticalScrollBarEnabled(false);
        this.setVerticalFadingEdgeEnabled(false);
        this.setVerticalScrollbarOverlay(false);
        if (Build.VERSION.SDK_INT >= 11) {
            removeJavascriptInterface("searchBoxJavaBredge_");
        }
        this.addJavascriptInterface(new JsInterface(this), "muilt");
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldL, int oldT) {
        LayoutParams lp = (LayoutParams) progress.getLayoutParams();
        lp.x = l;
        lp.y = t;
        progress.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldL, oldT);
    }

    public void setContent(String url) {
        this.loadUrl(url);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && needBack) {
            if (canGoBack()) {
                goBack();
            } else {
                goHome();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public void goHome() {
        if (controler != null) {
            controler.onClose();
        }
    }

    public void goHelp() {
        if (controler != null) {
            controler.goHelp();
        }
    }

    public interface WebControler {
        void onClose();

        void goHelp();
    }

    public class WebChromeClient extends android.webkit.WebChromeClient {
        //=========HTML5定位==========================================================
        //需要先加入权限
        //<uses-permission android:name="android.permission.INTERNET"/>
        //<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
        //<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>

//        @Override
//        public void onGeolocationPermissionsShowPrompt(final String origin, final
// GeolocationPermissions.Callback callback) {
//            callback.invoke(origin, true, false);//注意个函数，第二个参数就是是否同意定位权限，第三个是是否希望内核记住
//            super.onGeolocationPermissionsShowPrompt(origin, callback);
//        }
        //=========HTML5定位==========================================================

        //=========多窗口的问题==========================================================
        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture,
                                      Message resultMsg) {
            WebViewTransport transport = (WebViewTransport) resultMsg.obj;
            transport.setWebView(GWebView.this);
            resultMsg.sendToTarget();
            return true;
        }

        /**
         * 处理js警告对话框 注意返回值，true表示在此拦截，不再继续向下传递
         */
        @Override
        public boolean onJsAlert(WebView view, String url, String message,
                                 final JsResult result) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("提示").setMessage(message)
                    .setPositiveButton("确定", new AlertDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            result.confirm();
                        }
                    }).setCancelable(false).create().show();
            return true;
        }

        /**
         * 处理js确认对话框
         */
        @Override
        public boolean onJsConfirm(WebView view, String url, String message,
                                   final JsResult result) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("提示").setMessage(message)
                    .setPositiveButton("确定", new AlertDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            result.confirm();
                        }
                    }).setNegativeButton("取消", new AlertDialog.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    result.cancel();
                }
            }).setCancelable(false).create().show();
            return true;
        }

        /**
         * 处理js输入对话框
         */
        @Override
        public boolean onJsPrompt(WebView view, String url, String message,
                                  String defaultValue, final JsPromptResult result) {
            final EditText messageEdit = new EditText(getContext());
            messageEdit.setInputType(InputType.TYPE_CLASS_TEXT);
            messageEdit.setHint("请输入内容");
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("提示").setMessage(message)
                    .setView(messageEdit)
                    .setPositiveButton("确定", new AlertDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            result.confirm();
                        }
                    }).setNegativeButton("取消", new AlertDialog.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    result.cancel();
                }
            }).setCancelable(false).create().show();
            return true;
        }

        // =========多窗口的问题==========================================================
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progress.setVisibility(GONE);
            } else {
                if (progress.getVisibility() == GONE)
                    progress.setVisibility(VISIBLE);
                progress.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }
    }

}
