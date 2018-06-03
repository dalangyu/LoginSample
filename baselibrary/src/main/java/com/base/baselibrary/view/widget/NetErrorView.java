package com.base.baselibrary.view.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.baselibrary.R;
import com.base.baselibrary.base.IErrorView;
import com.base.baselibrary.view.drawable.MaterialProgressDrawable;

/**
 * Created by 15600 on 2017/8/15.
 */

public class NetErrorView extends LinearLayout implements IErrorView {

    private TextView msgTxt;

    private Button reloadBtn;

    private ImageView netErrorImg;

    private ImageView loadImg;

    private OnClickListener clickListener;

    private MaterialProgressDrawable mProgress;

    public NetErrorView(Context context) {
        this(context, null);
    }

    public NetErrorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
        setClickable(true);
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        LayoutInflater.from(context).inflate(R.layout.net_error_layout, this);
        msgTxt = (TextView) findViewById(R.id.net_error_txt);
        reloadBtn = (Button) findViewById(R.id.reload_btn);
        netErrorImg = (ImageView) findViewById(R.id.net_error_img);
        loadImg = (ImageView) findViewById(R.id.reset_loading_img);
        mProgress = new MaterialProgressDrawable(getContext(), this);
        final int CIRCLE_BG_LIGHT = 0xFFFAFAFA;
        mProgress.setBackgroundColor(CIRCLE_BG_LIGHT);
        loadImg.setImageDrawable(mProgress);
        mProgress.setColorSchemeColors(ContextCompat.getColor(context,R.color.blue_grey));
        reloadBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading();
                if (clickListener != null) {
                    clickListener.onClick(v);
                }
            }
        });
    }

    public NetErrorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setReloadClick(final OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public void showLoading() {
        msgTxt.setVisibility(View.GONE);
        reloadBtn.setVisibility(View.GONE);
        netErrorImg.setVisibility(View.GONE);
        setGravity(Gravity.CENTER);
        mProgress.setAlpha(255);
        mProgress.setStartEndTrim(0f, 0.8f);
        mProgress.setArrowScale(1f); //0~1之间
        mProgress.setProgressRotation(1f);
        mProgress.showArrow(false);
        mProgress.setBackgroundColor(Color.WHITE);
        mProgress.start();
        loadImg.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        setGravity(Gravity.CENTER_HORIZONTAL);
        msgTxt.setVisibility(View.VISIBLE);
        reloadBtn.setVisibility(View.VISIBLE);
        netErrorImg.setVisibility(View.VISIBLE);
        mProgress.stop();
        loadImg.setVisibility(View.GONE);
    }

    @Override
    public void setErrorMsg(String msg) {
        msgTxt.setText(msg);
    }

    @Override
    public View getView() {
        return this;
    }
}
