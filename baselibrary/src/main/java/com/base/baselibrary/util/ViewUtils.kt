package com.base.baselibrary.util

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.view.*
import com.base.baselibrary.R


/**
 * Created by 15600 on 2017/7/21.
 */
object ViewUtils {


    fun setStatusBarColor(activity: Activity, statusColor: Int) {
        val window = activity.window
        //取消状态栏透明
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //添加Flag把状态栏设为可绘制模式
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = statusColor
        }
        //设置系统状态栏处于可见状态
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        //让view不根据系统窗口来调整自己的布局
        val mContentView = window.findViewById<ViewGroup>(Window.ID_ANDROID_CONTENT)
        val mChildView = mContentView.getChildAt(0)
        if (mChildView != null) {
            ViewCompat.setFitsSystemWindows(mChildView, false)
            ViewCompat.requestApplyInsets(mChildView)
        }
    }


    fun setStatusBar(mContext: Context) {
        if (mContext is Activity) {
            val window: Window = mContext.window
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //android6.0以后可以对状态栏文字颜色和图标进行修改
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //5.0及以上
                val decorView = window.decorView
                val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                decorView.systemUiVisibility = option
                //                //根据上面设置是否对状态栏单独设置颜色
                //                if (useThemestatusBarColor) {
                //                    getWindow().setStatusBarColor(resources.getColor(R.color.colorTheme))
                //                } else {
                //                    getWindow().setStatusBarColor(Color.TRANSPARENT)
                //                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                //4.4到5.0
                val localLayoutParams = window.attributes
                localLayoutParams.flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or localLayoutParams.flags
            }
        }

    }

    private val INVALID_VAL = -1

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun compat(activity: Activity, statusColor: Int): View? {
        var color = ContextCompat.getColor(activity, R.color.colorPrimaryDark)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (statusColor != INVALID_VAL) {
                color = statusColor
            }
            activity.window.statusBarColor = color
            return null
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            val contentView = activity.findViewById<ViewGroup>(android.R.id.content) as ViewGroup
            if (statusColor != INVALID_VAL) {
                color = statusColor
            }
            var statusBarView: View? = contentView.getChildAt(0)
            val barHeight = getNavigationBarHeight(activity)
            if (statusBarView != null && statusBarView.measuredHeight == barHeight) {
                statusBarView.setBackgroundColor(color)
                return statusBarView
            }
            statusBarView = View(activity)
            val lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    barHeight)
            statusBarView.setBackgroundColor(color)
            contentView.addView(statusBarView, lp)
            return statusBarView
        }
        return null

    }

//    fun getImageSpanText(content: String, @DrawableRes resId: Int, start: Int, end: Int): SpannableString {
//        val stringSpan = SpannableString(content)
//        val imageSpan = ImageSpan(BaseApp.getApp(), ImageSpan.ALIGN_BASELINE)
//        stringSpan.setSpan(imageSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//        return stringSpan
//    }



    fun isNavigationBarShow(activity: Activity): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            val display = activity.windowManager.defaultDisplay
            val size = Point()
            val realSize = Point()
            display.getSize(size)
            display.getRealSize(realSize)
            return realSize.y != size.y
        } else {
            val menu = ViewConfiguration.get(activity).hasPermanentMenuKey()
            val back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
            return !(menu || back)
        }
    }

    fun getNavigationBarHeight(activity: Activity): Int {
        if (!isNavigationBarShow(activity)) {
            return 0
        }
        val resources = activity.resources
        val resourceId = resources.getIdentifier("navigation_bar_height",
                "dimen", "android")
        //获取NavigationBar的高度
        val height = resources.getDimensionPixelSize(resourceId)
        return height
    }

}