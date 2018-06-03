package com.base.baselibrary.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetWorkUtils {


    // 检测网络
    public static boolean isNetworkAvailable(final Context context) {
        boolean hasWifoCon = false;
        boolean hasMobileCon = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfos = cm.getAllNetworkInfo();
        for (NetworkInfo net : netInfos) {

            String type = net.getTypeName();
            if (type.equalsIgnoreCase("WIFI")) {
                if (net.isConnected()) {
                    hasWifoCon = true;
                }
            }

            if (type.equalsIgnoreCase("MOBILE")) {
                if (net.isConnected()) {
                    hasMobileCon = true;
                }
            }
        }


        if (hasWifoCon || hasMobileCon) {
            Runtime runtime = Runtime.getRuntime();
            try {
                Process pingProcess = runtime.exec("/system/bin/ping -c 1 www.baidu.com");
                int exitCode = pingProcess.waitFor();
                return (exitCode == 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        return false;

    }

}
