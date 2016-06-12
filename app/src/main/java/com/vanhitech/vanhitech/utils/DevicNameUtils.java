package com.vanhitech.vanhitech.utils;

import com.vanhitech.protocol.object.device.Device;
import com.vanhitech.vanhitech.R;
import com.vanhitech.vanhitech.conf.Constants;

/**
 * 创建者     heyn
 * 创建时间   2016/3/9 17:09
 * 描述	      ${TODO}
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class DevicNameUtils {


    /**
     * // 设备id 前3识别
     * public static final String id1="01";       //2.4G插座
     * public static final String id2="02";       //2.4G墙壁开关
     * public static final String id3="03";       //2.4G灯开关控制  !
     * public static final String id4="04";       //2.4G灯开关及调光控
     * public static final String id5="05";       //2.4G空调万用遥控器
     * public static final String id6="06";       //2.4G RGB灯控制
     * public static final String id7="07";       //WIFI电视锁及控制中心
     * public static final String id8="08";       //2.4G遥控器
     * public static final String id9="09";       //WIFI控制中心
     * public static final String idA="0A";       //空调功率插头+遥控器
     * public static final String idB="0B";       //2.4G RGB灯控制  !
     * public static final String idC="0C";       //2.4G RGB灯控制  !
     * public static final String idD="0D";       //2.4G冷暖灯开关及调光控制 !
     * public static final String idE="0E";       //机顶盒控制中心
     * public static final String idF="0F";       //2.4G RGB灯控制  !
     * public static final String id10="010";      //2.4G窗帘开关控制器
     * public static final String id11="011";      //2.4G电饭煲
     * public static final String id12="012";      //2.4G环境检测仪
     * public static final String id13="013";      //待计量插座
     * public static final String id14="014";      //中央空调控制面板
     * public static final String id15="015";      //带中继2.4G插座
     *
     * @param id
     * @return
     */


    public static String idToName(String id) {
        String name = null;
        String ID = id.substring(0, 2);
        switch (ID) {
            case Constants.id1: //wifi设备
                name = "2.4G插座";
                break;
            case Constants.id2:  //2.4g设备
                name = "2.4G墙壁开关";
                break;
            case Constants.id3: //wifi设备
                name = "2.4G灯开关控制";
                break;
            case Constants.id4:  //2.4g设备
                name = "2.4G灯开关及调光控";
                break;
            case Constants.id5: //wifi设备
                name = "2.4G空调万用遥控器";
                break;
            case Constants.id6:  //2.4g设备
                name = "2.4G RGB灯控制";
                break;
            case Constants.id7: //wifi设备
                name = "WIFI电视锁及控制中心";
                break;
            case Constants.id8:  //2.4g设备
                name = "2.4G遥控器";
                break;
            case Constants.id9: //wifi设备
                name = "WIFI控制中心";
                break;
            case Constants.idA:  //2.4g设备
                name = "空调功率插头+遥控器";
                break;
            case Constants.idB: //wifi设备
                name = "2.4G RGB灯控制";
                break;
            case Constants.idC:  //2.4g设备
                name = "2.4G RGB灯控制";
                break;
            case Constants.idD: //wifi设备
                name = "2.4G冷暖灯开关及调光控制";
                break;
            case Constants.idE: //wifi设备
                name = "机顶盒控制中心";
                break;
            case Constants.idF: //wifi设备
                name = "2.4G RGB灯控制";
                break;
            case Constants.id10:  //2.4g设备
                name = "2.4G窗帘开关控制器";
                break;

            case Constants.id11:  //2.4g设备
                name = "2.4G电饭煲";
                break;

            case Constants.id12:  //2.4g设备
                name = "2.4G环境检测仪";
                break;

            case Constants.id13:  //2.4g设备
                name = "待计量插座";
                break;

            case Constants.id14:  //2.4g设备
                name = "中央空调控制面板";
                break;

            case Constants.id15:  //2.4g设备
                name = "带中继2.4G插座";
                break;

            case Constants.id7c:  //2.4g设备
                name = "一键中控";
                break;
            default:
                name = "";
                break;
        }

        return name;
    }

    /*
    根据设备id设置设备icon
     */
    public static int intIcon(Device device) {
        int type= device.type;
        if (type==1) {
            return R.mipmap.scener01;
        } else if (type==2) {

        } else if (type==3) {//2.4G灯开关控制  !
            return R.mipmap.scener03;
        } else if (type==4) {

        } else if (type==5) {
            return R.mipmap.scener0a;
        } else if (type==6) {
            return R.mipmap.scener06;
        } else if (type==7) {
            return R.mipmap.scener00;
        } else if (type==8) {

        } else if (type==9) {
            return R.mipmap.scener00;
        } else if (type==10) {
            return R.mipmap.scener0a;
        } else if (type==11) { //2.4G RGB灯控制  !
            return R.mipmap.scener0b;
        } else if (type==12) { //2.4G RGB灯控制  !
            return R.mipmap.scener0b;
        } else if (type==13) {//2.4G冷暖灯开关及调光控制 !
            return R.mipmap.scener0b;
        } else if (type==14) {//2.4G RGB灯控制

        } else if (type==15) {//2.4G RGB灯控制  !
            return R.mipmap.scener06;
        } else if (type==16) {

        } else if (type==17) {

        } else if (type==18) {

        } else if (type==19) {
            return R.mipmap.scener01;
        } else if (type==20) {

        } else {
            //不处理
            return R.mipmap.scener01;
        }
        return R.mipmap.scener01;

//        String ID = device.id.substring(0, 2);
//        if (ID.equals(Constants.id1)) {
//            return R.mipmap.scener01;
//        } else if (ID.equals(Constants.id2)) {
//
//        } else if (ID.equals(Constants.id3)) {//2.4G灯开关控制  !
//            return R.mipmap.scener03;
//        } else if (ID.equals(Constants.id4)) {
//
//        } else if (ID.equals(Constants.id5)) {
//            return R.mipmap.scener0a;
//        } else if (ID.equals(Constants.id6)) {
//            return R.mipmap.scener06;
//        } else if (ID.equals(Constants.id7)) {
//            return R.mipmap.scener00;
//        } else if (ID.equals(Constants.id8)) {
//
//        } else if (ID.equals(Constants.id9)) {
//            return R.mipmap.scener00;
//        } else if (ID.equals(Constants.idA)) {
//            return R.mipmap.scener0a;
//        } else if (ID.equals(Constants.idB)) { //2.4G RGB灯控制  !
//            return R.mipmap.scener0b;
//        } else if (ID.equals(Constants.idC)) { //2.4G RGB灯控制  !
//            return R.mipmap.scener0b;
//        } else if (ID.equals(Constants.idD)) {//2.4G冷暖灯开关及调光控制 !
//            return R.mipmap.scener0b;
//        } else if (ID.equals(Constants.idE)) {//2.4G RGB灯控制
//
//        } else if (ID.equals(Constants.idF)) {//2.4G RGB灯控制  !
//            return R.mipmap.scener06;
//        } else if (ID.equals(Constants.id10)) {
//
//        } else if (ID.equals(Constants.id11)) {
//
//        } else if (ID.equals(Constants.id12)) {
//
//        } else if (ID.equals(Constants.id13)) {
//            return R.mipmap.scener01;
//        } else if (ID.equals(Constants.id14)) {
//
//        } else {
//            //不处理
//            return R.mipmap.scener01;
//        }
//        return R.mipmap.scener01;
    }
}
