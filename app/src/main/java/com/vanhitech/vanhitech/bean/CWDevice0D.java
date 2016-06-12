package com.vanhitech.vanhitech.bean;

import com.vanhitech.protocol.object.device.LightCWDevice;

/**
 * 创建者     heyn
 * 创建时间   2016/4/7 11:42
     * 描述	      ${CUSSS}
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class CWDevice0D  {
    public String id;
    public String brightness;
    public String colortemp;
    public String mode;
    public String pid;
    public String name;
    public String place;
    public String firmver;
    public String online;
    public String subtype;
    public String groupid;
    public String netinfo;
    public String power;
    public int type;
    public CWDevice0D(){
    }

    public String getId() {
        return id;
    }

    public String getBrightness() {
        return brightness;
    }

    public void setBrightness(String brightness) {
        this.brightness = brightness;
    }

    public String getColortemp() {
        return colortemp;
    }

    public void setColortemp(String colortemp) {
        this.colortemp = colortemp;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getFirmver() {
        return firmver;
    }

    public void setFirmver(String firmver) {
        this.firmver = firmver;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getNetinfo() {
        return netinfo;
    }

    public void setNetinfo(String netinfo) {
        this.netinfo = netinfo;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public CWDevice0D(LightCWDevice cwd){

        this.type = 13;
        this.id = cwd.id;
        this.pid = cwd.pid;
        this.name = cwd.name;
        this.place = cwd.place;
        this.firmver=cwd.firmver+"";
        this.subtype=cwd.subtype+"";
        this.groupid=cwd.groupid+"";
        this.netinfo=cwd.netinfo+"";

    }

    public CWDevice0D(String id, String pid, String name, String place) {

//        this.id = id;
//        this.pid = pid;
//        this.name = name;
//        this.place = place;//TODO
//        com.vanhitech.protocol.object.Power power = new com.vanhitech.protocol.object.Power();
        this.type = 13;
        this.id = id;
        this.pid = pid;
        this.name = name;
        this.place = place;
        com.vanhitech.protocol.object.Power power = new com.vanhitech.protocol.object.Power();

    }

    public String getPlace(){
        return place;
    }
    public  void setId(String id){
        this.id=id;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("(");
        builder.append("id:").append(this.id);
        builder.append(", pid:").append(this.pid);
        builder.append(", name:").append(this.name);
        builder.append(", place:").append(this.place);
        builder.append(", subtype:").append(this.subtype);
        builder.append(", firmver:").append(this.firmver);
        builder.append(", type:").append(this.type);
        builder.append(", onLine:").append(this.online);
        builder.append(", groupid:").append(this.groupid);
        builder.append(", netinfo:").append(this.netinfo);
        builder.append(", power:").append(this.power);
        builder.append(", brightness:").append(this.brightness);
        builder.append(", colortemp:").append(this.colortemp);
        builder.append(", mode:").append(this.mode);
        builder.append(")");
        return builder.toString();
    }

//    public String toString() {
//        StringBuilder builder = new StringBuilder();
//        builder.append("(");
//        builder.append(super.toString());
//        builder.append(", brightness:").append(this.brightness);
//        builder.append(", colortemp:").append(this.colortemp);
//        builder.append(", mode:").append(this.mode);
//        builder.append(")");
//        return builder.toString();
//    }
}
