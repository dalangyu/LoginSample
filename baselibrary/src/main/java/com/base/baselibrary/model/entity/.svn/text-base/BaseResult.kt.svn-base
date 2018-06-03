package com.base.baselibrary.model.entity

import com.base.baselibrary.util.StringUtils
import com.google.gson.annotations.SerializedName
import java.io.Serializable


/**
 * Created by 15600 on 2017/7/1.
 */
 data class BaseResult<E>(
        @SerializedName("retCode")
        var code: String? = null,
        @SerializedName("retMessage")
        var msg: String? = null,
        @SerializedName("body")
        var data: E? = null
): Serializable,BaseView(){
    fun isSuccess(): Boolean {
        return StringUtils.equals(code,"0000")
    }

    fun isTokenError(): Boolean {
        return StringUtils.equals(code,"3102")||StringUtils.equals(code,"3103")||StringUtils.equals(code,"3104")
    }

}