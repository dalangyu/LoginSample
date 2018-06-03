package com.base.baselibrary.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.List;
import java.util.Stack;

/**
 * User:  Windy (rongcheng_feng@163.com)
 * Date: 2015-12-04
 * Time: 17:44
 * FIXME  应用程序Activity管理类：用于Activity管理和应用程序退出
 */
public class ActManager {
    private static Stack<Activity> activityStack;
    private static Stack<Activity> activityAddress;
    private static ActManager instance;

    private ActManager() {
    }

    /**
     * 单实例 , UI无需考虑多线程同步问题
     */
    public static ActManager getInstance() {
        if (instance == null) {
            instance = new ActManager();
        }
        return instance;
    }

    /**
     * 添加地址相关的Activity到栈
     */
    public void addActivityAddress(Activity activity) {
        if (activityAddress == null) {
            activityAddress = new Stack<>();
        }
        activityAddress.add(activity);
    }


    /**
     * 结束所有Activity
     */
    public void finishAllActivityAddress() {
        if (activityAddress == null) {
            activityAddress = new Stack<>();
        }
        for (int i = 0, size = activityAddress.size(); i < size; i++) {
            if (null != activityAddress.get(i)) {
                activityAddress.get(i).finish();
            }
        }
        activityAddress.clear();
    }



    /**
     * 添加Activity到栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（栈顶Activity）
     */
    public Activity currentActivity() {
        if (activityStack == null || activityStack.isEmpty()) {
            return null;
        }
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 查找Activity 没有找到则返回null
     */
    public Activity findActivity(Class<?> cls) {
        Activity activity = null;
        for (Activity aty : activityStack) {
            if (aty.getClass().equals(cls)) {
                activity = aty;
                break;
            }
        }
        return activity;
    }

    /**
     * 结束当前Activity（栈顶Activity）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity(重载)
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定的Activity(重载)
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 关闭除了指定activity以外的全部activity 如果cls不存在于栈中，则栈全部清空
     *
     * @param cls
     */
    public void finishOthersActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (!(activity.getClass().equals(cls))) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }



    /**
     * 判断activity是否在运行
     *
     * @param activity
     * @return
     */
    public boolean isTopActivity(Activity activity) {
        if (activity == null)
            return false;
        if (activity.isFinishing())
            return false;
        String packageName = "com.d7health";
        ActivityManager activityManager = (ActivityManager) activity
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
        if (tasksInfo.size() > 0) {
            System.out.println("---------------包名-----------"
                    + tasksInfo.get(0).topActivity.getPackageName());
            // 应用程序位于堆栈的顶层
            if (packageName.equals(tasksInfo.get(0).topActivity
                    .getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 应用程序退出
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
//            ActivityManager activityMgr = (ActivityManager) context
//                    .getSystemService(Context.ACTIVITY_SERVICE);
//            activityMgr.killBackgroundProcesses(context.getPackageName());
//            System.exit(0);
        } catch (Exception e) {
            System.exit(0);
        }
    }

}
