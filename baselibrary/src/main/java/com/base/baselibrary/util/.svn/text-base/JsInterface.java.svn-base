package com.base.baselibrary.util;

import android.util.Log;
import android.webkit.JavascriptInterface;

import com.base.baselibrary.view.widget.GWebView;


public class JsInterface {
    private static final String TAG = "JsInterface";
    private GWebView webView;

    public JsInterface(GWebView webView) {
        this.webView = webView;
    }

    private void loadUrl(final String url) {
        try {
            webView.post(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl(url);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void exec(String args) {
        try {
            Log.e(TAG, "exec: " + args);
            String[] params = args.split("//")[1].split("/");
            if (params.length == 0)
                return;
            String function = params[0];
            switch (function) {
                case "jump_home":
                    break;
                case "jump_page":
                    break;
                case "version":
                    break;
                case "go_help":
                    break;
                case "website":
                    break;
            }
        } catch (Exception e) {
            Log.e("JSInterface--exec", Log.getStackTraceString(e));
        }
    }

    private void execScript(String callback, String id, String result) {
        String js = "javascript:" + callback + "('" + id + "','" + result + "')";
        loadUrl(js);
    }

    private String[] removeAtIndex(String[] params) {
        String[] array = new String[params.length - 1];
        for (int i = 0; i < params.length; i++) {
            if (i < params.length - 1)
                array[i] = params[i + 1];
        }
        return array;
    }

}