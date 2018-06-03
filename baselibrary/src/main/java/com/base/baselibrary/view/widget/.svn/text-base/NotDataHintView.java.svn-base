package com.base.baselibrary.view.widget;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.baselibrary.R;


/**
 * Created by HLL on 2017/3/31.
 */

public class NotDataHintView {


    private boolean titleLoaded = false; // 标题是否加载成功
    private LinearLayout notdataView;
    private ImageView imgShow;
    private TextView tvShow;
    private static NotDataHintView notDataHint;

    private NotDataHintView(View view) {
        loadNotData(view);
    }

    public static NotDataHintView getNotDataHintView(View view) {
        notDataHint = new NotDataHintView(view);
        return notDataHint;
    }

    private void loadNotData(View view) {
        notdataView = view.findViewById(R.id.notdata_ll);
        tvShow = view.findViewById(R.id.notdata_tvshow);
        imgShow = view.findViewById(R.id.notdata_imgshow);
        if (notdataView != null) {
            titleLoaded = true;
        }
    }


    /**
     * 设置ImageView
     */
    public void setImage(@DrawableRes int drawable) {
        if (titleLoaded) {
            imgShow.setImageResource(drawable);
        }
    }


    /**
     * 设置Title名字
     */
    public void settvName(@StringRes int Name) {
        if (titleLoaded) {
            tvShow.setText(Name);
        }
    }

    /**
     * 设置Title名字
     */
    public void settvName(String Name) {
        if (titleLoaded) {
            tvShow.setText(Name);
//            tvShow.setText("暂无信息");
        }
    }

    /**
     * 设置Title字体颜色
     */
    public void settvColor(int color) {
        if (titleLoaded) {
            tvShow.setTextColor(color);
        }
    }

    /**
     * 设置整个Title背景色
     */
    public void setBgColor(int color) {
        if (titleLoaded) {
            notdataView.setBackgroundColor(color);
        }
    }


    /**
     * 显示没有数据时候的状态
     */
    public void show() {
        notdataView.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏没有数据时候的状态
     */
    public void hide() {
        notdataView.setVisibility(View.GONE);
    }

}

