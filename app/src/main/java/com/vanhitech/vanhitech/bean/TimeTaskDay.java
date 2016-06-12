package com.vanhitech.vanhitech.bean;

/**
 * 创建者     heyn
 * 创建时间   2016/4/15 13:49
 * 描述	      保存开启/关闭定时的天
 * 集合0-6 表示周一到周日
 * 对应设备id
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class TimeTaskDay {


    public String id;
    public String deviceId;
    public String D1;//周一
    public String D2;
    public String D3;
    public String D4;
    public String D5;
    public String D6;
    public String D7;//周天
    public String power0;//1路
    public String power1;//2路
    public String power2;//3路
    public String type;
    public String pid;//归属中控
    public String name;
    public String place;

    public String enabled;//是否打开
    public String hour;//小时
    public String minute;//分钟
    public String repeated;
    public String online;

    public TimeTaskDay() {
    }

    public TimeTaskDay(String schedinfoId, String deviceId, String d1, String d2, String d3, String d4
            , String d5, String d6, String d7, String power0, String power1, String power2, String enabled,
                       String hour, String minute, String repeated, String name, String place
    ) {
        this.id = schedinfoId;
        this.deviceId = deviceId;
        this.D1 = d1;
        this.D2 = d2;
        this.D3 = d3;
        this.D4 = d4;
        this.D5 = d5;
        this.D6 = d6;
        this.D7 = d7;
        this.power0 = power0;
        this.power1 = power1;
        this.power2 = power2;
        this.enabled = enabled;
        this.hour = hour;
        this.minute = minute;
        this.repeated = repeated;
        this.name = name;
        this.place = place;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getD1() {
        return D1;
    }

    public void setD1(String d1) {
        D1 = d1;
    }

    public String getD2() {
        return D2;
    }

    public void setD2(String d2) {
        D2 = d2;
    }

    public String getD3() {
        return D3;
    }

    public void setD3(String d3) {
        D3 = d3;
    }

    public String getD4() {
        return D4;
    }

    public void setD4(String d4) {
        D4 = d4;
    }

    public String getD5() {
        return D5;
    }

    public void setD5(String d5) {
        D5 = d5;
    }

    public String getD6() {
        return D6;
    }

    public void setD6(String d6) {
        D6 = d6;
    }

    public String getD7() {
        return D7;
    }

    public void setD7(String d7) {
        D7 = d7;
    }

    public String getPower0() {
        return power0;
    }

    public void setPower0(String power0) {
        this.power0 = power0;
    }

    public String getPower1() {
        return power1;
    }

    public void setPower1(String power1) {
        this.power1 = power1;
    }

    public String getPower2() {
        return power2;
    }

    public void setPower2(String power2) {
        this.power2 = power2;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public String getRepeated() {
        return repeated;
    }

    public void setRepeated(String repeated) {
        this.repeated = repeated;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("(");
        builder.append(",id:").append(this.id);
        builder.append(",deviceId:").append(this.deviceId);
        builder.append(",D1:").append(this.D1);
        builder.append(",D2:").append(this.D2);
        builder.append(",D3:").append(this.D3);
        builder.append(",D4:").append(this.D4);
        builder.append(",D5:").append(this.D5);
        builder.append(",D6:").append(this.D6);
        builder.append(",D7:").append(this.D7);
        builder.append(",power:").append(this.power0);//1
        builder.append(",power:").append(this.power1);//2
        builder.append(",power:").append(this.power2);//3
        builder.append(",type:").append(this.type);
        builder.append(",pid:").append(this.pid);
        builder.append(",name:").append(this.name);
        builder.append(",place:").append(this.place);
        builder.append(",enabled:").append(this.enabled);
        builder.append(",hour:").append(this.hour);
        builder.append(",minute:").append(this.minute);
        builder.append(",repeated").append(this.repeated);
        builder.append(",online").append(this.online);
        builder.append(")");
        return builder.toString();
    }
}
