package com.base.baselibrary.model.entity

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.view.View
//import com.base.baselibrary.BR
import java.io.Serializable

/**
 * Created by 15600 on 2017/6/22.
 */

open class BaseView : Serializable, BaseObservable {

    constructor()

    constructor(loaderView: (() -> View)?) {
        this.loaderView = loaderView
    }

    var resId: Int = RES_ID_DEFAULT

    /**
     * 默认值证明不存在databinding数据绑定源
     */
    var brId: Int = BR_ID_DEFAULT

    /**
     * 自定义View构建器
     */
    var loaderView: (() -> View)? = null


    var mychecked = false
        @Bindable
        get() = field
        set(value) {
            if (value != field)
                field = value
//                notifyPropertyChanged(BR._all)
//                notifyPropertyChanged(BR.mychecked)
        }

    var isVisible = false

    companion object {
        /**
         * 如果为此值加载默认布局
         */
        const val RES_ID_DEFAULT = -1

        /**
         * 如果为此值不加载databinding数据源
         */
        const val BR_ID_DEFAULT = -1
    }

}