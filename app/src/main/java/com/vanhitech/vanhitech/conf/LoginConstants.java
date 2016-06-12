package com.vanhitech.vanhitech.conf;

import com.vanhitech.protocol.object.UserInfo;

/**
 * @创建者
 * @创建时间 2016/3/10 15:36
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class LoginConstants {
    //这个是定义的一个是否登录的boo
    // lean值
    public static boolean ISLOGIN=false;
    //路由密码
    public static String lumm;
    //这个是当登录成功所记录的当前用户的用户名
    public static String USERNAME;
    public static  String USERPWD;
    //key
    public static  String key;

    public static  String deviceID;

        //登录过,销毁,从新登录标记
    public static boolean IRESL=true;
    //保存place标记
    public static boolean isPlaceSave=false;

    public static Boolean Isautologin=false;
    public static Boolean fresh=true;
    public static Boolean timgingfresh=true;
    public static UserInfo userInfo;

}
