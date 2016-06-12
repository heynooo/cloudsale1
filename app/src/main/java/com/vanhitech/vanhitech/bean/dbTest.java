package com.vanhitech.vanhitech.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.vanhitech.protocol.object.NetInfo;
import com.vanhitech.protocol.object.Power;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建者     heyn
 * 创建时间   2016/5/18 22:00
 * 描述	      ${TODO}
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${AirTypeADevice}
 */
public class dbTest implements Parcelable {
    public dbTest() {
    }

    public int type;
    public String id;
    public String pid;
    public String name;
    public String place;
    public int subtype;
    public int firmver;
    public boolean online;
    public String groupid;
    public List<Power> power = new ArrayList();
    public NetInfo netinfo;
//    public int group;//TODO
    public int mode;
    public int temp;
    public int speed;
    public int direct;
    public int adirect;
    public int mdirect;
    public int ckhour;
    public int timeon;
    public int timeoff;
    public int keyval;
    public int sysflag;
    public int tmstate7;
    public int tmstate3;
    public int ckminute;
    public int roomtemp;
    public float ttpower;
    public float kw;
    public float voltage;
    public float coeff;
    public float current;

    public dbTest(int type, String id, String pid, String name, String place, int subtype, int firmver, boolean online, String groupid, List<Power> power, NetInfo netinfo, int group, int mode, int temp, int speed, int direct, int adirect, int mdirect, int ckhour, int timeon, int timeoff, int keyval) {
        this.type = type;
        this.id = id;
        this.pid = pid;
        this.name = name;
        this.place = place;
        this.subtype = subtype;
        this.firmver = firmver;
        this.online = online;
        this.groupid = groupid;
        this.power = power;
        this.netinfo = netinfo;
//        this.group = group;
        this.mode = mode;
        this.temp = temp;
        this.speed = speed;
        this.direct = direct;
        this.adirect = adirect;
        this.mdirect = mdirect;
        this.ckhour = ckhour;
        this.timeon = timeon;
        this.timeoff = timeoff;
        this.keyval = keyval;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public int getSubtype() {
        return subtype;
    }

    public void setSubtype(int subtype) {
        this.subtype = subtype;
    }

    public int getFirmver() {
        return firmver;
    }

    public void setFirmver(int firmver) {
        this.firmver = firmver;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public List<Power> getPower() {
        return power;
    }

    public void setPower(List<Power> power) {
        this.power = power;
    }

    public NetInfo getNetinfo() {
        return netinfo;
    }

    public void setNetinfo(NetInfo netinfo) {
        this.netinfo = netinfo;
    }



//    public int getGroup() {
//        return group;
//    }
//
//    public void setGroup(int group) {
//        this.group = group;
//    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getDirect() {
        return direct;
    }

    public void setDirect(int direct) {
        this.direct = direct;
    }

    public int getAdirect() {
        return adirect;
    }

    public void setAdirect(int adirect) {
        this.adirect = adirect;
    }

    public int getMdirect() {
        return mdirect;
    }

    public void setMdirect(int mdirect) {
        this.mdirect = mdirect;
    }

    public int getCkhour() {
        return ckhour;
    }

    public void setCkhour(int ckhour) {
        this.ckhour = ckhour;
    }

    public int getTimeon() {
        return timeon;
    }

    public void setTimeon(int timeon) {
        this.timeon = timeon;
    }

    public int getTimeoff() {
        return timeoff;
    }

    public void setTimeoff(int timeoff) {
        this.timeoff = timeoff;
    }

    public int getKeyval() {
        return keyval;
    }

    public void setKeyval(int keyval) {
        this.keyval = keyval;
    }

    @Override
    public String toString() {
        return "dbTest{" +
                "type=" + type +
                ", id='" + id + '\'' +
                ", pid='" + pid + '\'' +
                ", name='" + name + '\'' +
                ", place='" + place + '\'' +
                ", subtype=" + subtype +
                ", firmver=" + firmver +
                ", online=" + online +
                ", groupid='" + groupid + '\'' +
                ", power=" + power +
                ", netinfo=" + netinfo +
//                ", group=" + group +
                ", mode=" + mode +
                ", temp=" + temp +
                ", speed=" + speed +
                ", direct=" + direct +
                ", adirect=" + adirect +
                ", mdirect=" + mdirect +
                ", ckhour=" + ckhour +
                ", timeon=" + timeon +
                ", timeoff=" + timeoff +
                ", keyval=" + keyval +
                ", sysflag=" + sysflag +
                ", tmstate7=" + tmstate7 +
                ", tmstate3=" + tmstate3 +
                ", ckminute=" + ckminute +
                ", roomtemp=" + roomtemp +
                ", ttpower=" + ttpower +
                ", kw=" + kw +
                ", voltage=" + voltage +
                ", coeff=" + coeff +
                ", current=" + current +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeString(this.id);
        dest.writeString(this.pid);
        dest.writeString(this.name);
        dest.writeString(this.place);
        dest.writeInt(this.subtype);
        dest.writeInt(this.firmver);
        dest.writeByte(online ? (byte) 1 : (byte) 0);
        dest.writeString(this.groupid);
        dest.writeList(this.power);
//        dest.writeInt(this.group);
        dest.writeInt(this.mode);
        dest.writeInt(this.temp);
        dest.writeInt(this.speed);
        dest.writeInt(this.direct);
        dest.writeInt(this.adirect);
        dest.writeInt(this.mdirect);
        dest.writeInt(this.ckhour);
        dest.writeInt(this.timeon);
        dest.writeInt(this.timeoff);
        dest.writeInt(this.keyval);
        dest.writeInt(this.sysflag);
        dest.writeInt(this.tmstate7);
        dest.writeInt(this.tmstate3);
        dest.writeInt(this.ckminute);
        dest.writeInt(this.roomtemp);
        dest.writeFloat(this.ttpower);
        dest.writeFloat(this.kw);
        dest.writeFloat(this.voltage);
        dest.writeFloat(this.coeff);
        dest.writeFloat(this.current);
    }

    private dbTest(Parcel in) {
        this.type = in.readInt();
        this.id = in.readString();
        this.pid = in.readString();
        this.name = in.readString();
        this.place = in.readString();
        this.subtype = in.readInt();
        this.firmver = in.readInt();
        this.online = in.readByte() != 0;
        this.groupid = in.readString();
        this.netinfo = in.readParcelable(NetInfo.class.getClassLoader());
//        this.group = in.readInt();
        this.mode = in.readInt();
        this.temp = in.readInt();
        this.speed = in.readInt();
        this.direct = in.readInt();
        this.adirect = in.readInt();
        this.mdirect = in.readInt();
        this.ckhour = in.readInt();
        this.timeon = in.readInt();
        this.timeoff = in.readInt();
        this.keyval = in.readInt();
        this.sysflag = in.readInt();
        this.tmstate7 = in.readInt();
        this.tmstate3 = in.readInt();
        this.ckminute = in.readInt();
        this.roomtemp = in.readInt();
        this.ttpower = in.readFloat();
        this.kw = in.readFloat();
        this.voltage = in.readFloat();
        this.coeff = in.readFloat();
        this.current = in.readFloat();
    }

    public static final Parcelable.Creator<dbTest> CREATOR = new Parcelable.Creator<dbTest>() {
        public dbTest createFromParcel(Parcel source) {
            return new dbTest(source);
        }

        public dbTest[] newArray(int size) {
            return new dbTest[size];
        }
    };
}
