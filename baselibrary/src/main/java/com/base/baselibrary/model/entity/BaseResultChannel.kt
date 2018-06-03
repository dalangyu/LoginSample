package com.base.baselibrary.model.entity

import com.base.baselibrary.util.StringUtils
import com.google.gson.annotations.SerializedName
import java.io.Serializable


/**
 * Created by 15600 on 2017/7/1.
 */
 data class BaseResultChannel<E>(
        @SerializedName("code")
        var code: String? = null,
        @SerializedName("message")
        var msg: String? = null,
        @SerializedName("data")
        var data: E? = null,
        @SerializedName("result")
        var result: E? = null,
        @SerializedName("success")
        var success: E? = null
): Serializable,BaseView(){
    fun isSuccess(): Boolean {
        return StringUtils.equals(code,"E000000")
    }

}