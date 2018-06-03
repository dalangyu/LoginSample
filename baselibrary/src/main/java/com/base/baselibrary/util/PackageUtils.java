package com.base.baselibrary.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Author：Zhang Jingwei on 2015/11/4 09:10
 * Email：zhangjingwei@dianru.com
 */
public class PackageUtils {

    public static final String TAG = "PackageUtils";

    private PackageUtils() {
    }

    /**
     * 获取应用名
     *
     * @param context
     * @return
     */
    public static String getAppName(Context context) {
        return getAppName(context, null);
    }

    /**
     * 获取应用名
     */
    public static String getAppName(Context context, String packageName) {
        String applicationName;

        if (packageName == null) {
            packageName = context.getPackageName();
        }

        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            applicationName = context.getString(packageInfo.applicationInfo.labelRes);
        } catch (Exception e) {
            Log.w(TAG, "Failed to get version number." + Log.getStackTraceString(e));
            applicationName = "";
        }

        return applicationName;
    }


    /**
     * 根据pid获取进程名
     * @return null may be returned if the specified process not found
     */
    public static String getProcessName(Context context, int pid) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }
    /**
     * 获取版本号
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        return getVersionName(context, null);
    }

    public static String getVersionName(Context context, String packageName) {
        String versionName;

        if (packageName == null) {
            packageName = context.getPackageName();
        }

        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            versionName = packageInfo.versionName;
        } catch (Exception e) {
            Log.w(TAG, "Failed to get version number." + Log.getStackTraceString(e));
            versionName = "1.0.1";
        }

        return versionName;
    }

    public static String getVersionCode(Context context) {
        return getVersionCode(context, null);
    }

    public static String getVersionCode(Context context, String packageName) {
        String versionCode;

        if (packageName == null) {
            packageName = context.getPackageName();
        }

        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            versionCode = Integer.toString(packageInfo.versionCode);
        } catch (Exception e) {
            Log.w(TAG, "Failed to get version code." + Log.getStackTraceString(e));
            versionCode = "";
        }

        return versionCode;
    }

    public static int getSdkVersion() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获取当前手机安装的所以应用
     *
     * @param context
     * @return
     */
    public static List<String> getInstallPackages(Context context) {
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
        List<String> result = new ArrayList<String>();
        for (PackageInfo packageInfo : packages) {
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)
                result.add(packageInfo.applicationInfo.packageName);
        }
        return result;
    }

    /**
     * 判断该包名应用是否安装
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean hasPackageName(Context context, String packageName) {
        List<String> list = getInstallPackages(context);
        if (list != null && list.size() > 0) {
            for (String pkg : list) {
                if (packageName.equals(pkg)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public static void openApp(Context context, String packageName) {
        boolean hasPkg = hasPackageName(context, packageName);
        if (!hasPkg) {
            Toast.makeText(context, "应用不存在，请重新安装！", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 将包名以$拼接
     *
     * @param result
     * @return
     */
    public static String getPackageString(List<String> result) {
        if (result == null || result.isEmpty())
            return "";
        result.remove("");
        StringBuilder sb = new StringBuilder();
        for (String item : result) {
            if (!StringUtils.isEmpty(item))
                sb.append("$").append(item);
        }
        return sb.toString().substring(1);
    }

    /**
     * 获取imei，如果为空，获取 sn
     */
    public static String getIdfa(Context context) {
        String imei = getImei(context);
        return StringUtils.isEmpty(imei) ? getSN() : imei;
    }

    /**
     * 获取设备的Serial Number 唯一值
     *
     * @return sn
     */
    public static String getSN() {
        String sn;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class, String.class);
            sn = (String) (get.invoke(c, "ro.serialno", "unknown"));
        } catch (Exception e) {
            sn = "unknown";
        }
        if (sn.equalsIgnoreCase("unknown"))
            return "";
        return StringUtils.isEmpty(sn) ? "" : sn;
    }


    /**
     * 获取设备的IMEI
     *
     * @param context 上下文
     * @return imei
     */
    public static String getImei(Context context) {
        String deviceid = getTm(context).getDeviceId();
        if (StringUtils.isEmpty(deviceid))
            deviceid = getAndroidId(context);
        return deviceid;
    }
    

    /**
     * 获取android id，重置手机会变更
     */
    public static String getAndroidId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
    
    /**
     * 获取电话管理器
     *
     * @param context mContext
     * @return
     */
    public static TelephonyManager getTm(Context context) {
        return (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }
}
