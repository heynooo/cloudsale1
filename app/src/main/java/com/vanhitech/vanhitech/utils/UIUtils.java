package com.vanhitech.vanhitech.utils;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.view.WindowManager;

import com.vanhitech.vanhitech.base.MyApplication;


/**
 * 创建者     heyn
 * 创建时间   2016/3/9 17:11
 * 描述	      ${TODO}
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class UIUtils {

    /**得到上下文*/
    public static Context getContext() {
        return MyApplication.getContext();
    }

    /**得到Resource对象*/
    public static Resources getResources() {
        return getContext().getResources();
    }

    /**得到string.xml中的字符*/
    public static String getString(int resId) {
        return getResources().getString(resId);
    }

    /**得到string.xml中的字符数组*/
    public static String[] getStringArr(int resId) {
        return getResources().getStringArray(resId);
    }

    /**得到color.xml中的颜色值*/
    public static int getColor(int resId) {
        return getResources().getColor(resId);
    }
    /**得到应用程序的包名*/
    public static String getPackageName() {
        return getContext().getPackageName();
    }

    /**得到主线程的id*/
    public static long getMainThreadId() {
        return MyApplication.getMainThreadId();
    }

    /**得到主线程的handler*/
    public static Handler getHandler() {
        return MyApplication.getHandler();
    }

    /**安全的执行一个task*/
    public static void postTaskSafely(Runnable task) {
        // 得到当前线程的id
        long curThreadId = android.os.Process.myTid();
        long mainThreadId = getMainThreadId();

        if (curThreadId == mainThreadId) {
            // 如果调用该方法的线程是在主线程-->直接执行任务
            task.run();
        } else {
            // 如果调用该方法的线程是在子线程-->把任务post到主线程handler去执行

            // 主线程的handler
            Handler handler = getHandler();
            handler.post(task);
        }
    }
    /** 获取屏幕宽 */
    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return windowManager.getDefaultDisplay().getWidth();
    }
    /** 获取屏幕宽高 */
    public static int getScreenHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return windowManager.getDefaultDisplay().getHeight();
    }
}
