package com.base.baselibrary.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by 15600 on 2017/9/20.
 */

public class GsonUtils {
    private  static Gson gson;

    public static Gson buildGson() {
        if (gson == null) {
            gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
//                    .registerTypeAdapter(Integer.class, new JavaClassDefaultAdapter())
                    .create();
        }
        return gson;
    }

}
