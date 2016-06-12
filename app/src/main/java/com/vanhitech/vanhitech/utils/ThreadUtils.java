package com.vanhitech.vanhitech.utils;

import android.os.Handler;

/**
 * Created by heyn on 2016/3/8.
 */
public class ThreadUtils {
    /**
     * 主线程的handler
     */
    public static Handler mHander = new Handler();

    /**
     * 主线程执行的任务
     */
    public static void runInUiThread(Runnable task) {
        mHander.post(task);
    }

    /**
     * 在子线程执行任务
     */
    public static void runInThread(Runnable task) {
        new Thread(task).start();
    }

}
