package com.vanhitech.vanhitech.base;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;

import com.lidroid.xutils.DbUtils;
import com.vanhitech.protocol.ClientCMDHelper;
import com.vanhitech.protocol.object.device.Device;
import com.vanhitech.vanhitech.cropview.FontUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 创建者     heyn
 * 创建时间   2016/3/9 17:12
 * 描述	      ${TODO}
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class MyApplication extends Application {

    private static Context mContext;
    private static Handler mHandler;
    private static long mMainThreadId;
    public static ClientCMDHelper mMhelper;
    public Bitmap cropped = null;


    /**
     * 得到上下文
     */
    public static Context getContext() {
        return mContext;
    }

    /**
     * 得到数据库
     */
    public static DbUtils getDbUtils() {

        DbUtils db = DbUtils.create(mContext);
        return db;
    }

    /**
     * 得到主线程的handler
     */
    public static Handler getHandler() {
        return mHandler;
    }

    /**
     * 得到主线程的线程id
     */
    public static long getMainThreadId() {
        return mMainThreadId;
    }

    @Override
    public void onCreate() {// 程序的入口方法



		/*--------------- 创建应用里面需要用到的一些共有的属性 ---------------*/
        // 1.上下文
        mContext = getApplicationContext();

        // 2.主线程handler
        mHandler = new Handler();

        // 3.主线程的id
        mMainThreadId = android.os.Process.myTid();
        /**
         * Tid: thread
         * Pid: process
         * Uid: user
         */
        //连接服务器

        super.onCreate();
        FontUtils.loadFont(getApplicationContext(), "Roboto-Light.ttf");
    }

    public  static Boolean isPrompt=false;
    /**
     * 是否销毁
     * @return
     */
    public static Boolean getIsPrompt() {
        return isPrompt;
    }
    public static void setIsPrompt(Boolean isPrompt) {
        MyApplication.isPrompt = isPrompt;
    }

    /**
     * 是否保存SaveDevice
     * @return
     */
    public static Boolean getIsSaveDevice() {
        return IsSaveDevice;
    }
    public static Boolean IsSaveDevice;

    public static void setIsSaveDevice(Boolean isSaveDevice) {
        IsSaveDevice = isSaveDevice;
    }
    /**
     * 防止再次点击发送情景模式
     */
    public static Boolean  isSendScener;

    public static Boolean getIsSendScener() {
        return isSendScener;
    }

    public static void setIsSendScener(Boolean isSendScener) {
        MyApplication.isSendScener = isSendScener;
    }


    public static  Boolean IsbrushRoom;

    /**
     * 主界面是否刷新
     * @return
     */
    public static Boolean getIsbrushRoom() {
        return IsbrushRoom;
    }

    public static void setIsbrushRoom(Boolean isbrushRoom) {
        IsbrushRoom = isbrushRoom;
    }

    /**
     * 定时界面是否刷新
     * @return
     */
    public static Boolean getIsbrushTime() {
        return IsbrushTime;
    }

    public static void setIsbrushTime(Boolean isbrushTime) {
        IsbrushTime = isbrushTime;
    }



    public static  Boolean IsbrushTime;

    /**
     *
     * @return
     */
    public static Boolean getIsbrushStart() {
        return IsbrushStart;
    }

    public static void setIsbrushStart(Boolean isbrushStart) {
        IsbrushStart = isbrushStart;
    }

    public static  Boolean IsbrushStart;

    /**
     * 配置
     */
    public static  Boolean IsSmartConfig;

    public static Boolean getIsSmartConfig() {
        return IsSmartConfig;
    }

    public static void setIsSmartConfig(Boolean isSmartConfig) {
        IsSmartConfig = isSmartConfig;
    }

    /**
     * 设备缓存
     */
    private Map<String,Device> mStringDeviceMay = new HashMap<>();



    private  List<Device> mDevices =new ArrayList<>();

    public  List<Device> getDevices() {
        return mDevices;
    }

    public  void setDevices(List<Device> devices) {
        mDevices = devices;
    }



}
