package com.base.baselibrary.model.entity

import java.io.Serializable

/**
 * Created by 15600 on 2017/9/14.
 */
data class Page(

        var total: Int = 0,

        var pages: Int = 1,

        var currentPage: Int = 1,

        var perPage: Int = 10
): Serializable