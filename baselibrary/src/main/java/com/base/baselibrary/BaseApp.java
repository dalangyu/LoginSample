package com.base.baselibrary;

import com.base.baselibrary.base.MainApplication;
import com.base.baselibrary.util.PreferencesUtils;
import com.google.gson.Gson;

public class BaseApp extends MainApplication {

    private static BaseApp gDefault;

    public final Gson gson = new Gson();

    public static BaseApp getApp() {
        return gDefault;
    }


    @Override
    public void onCreate() {
        gDefault = this;
        super.onCreate();

        PreferencesUtils.initPrefs(this);

        gDefault = this;
    }


}
