package com.vanhitech.vanhitech.bean;

/**
 * 创建者     heyn
 * 创建时间   2016/5/9 10:54
 * 描述	      ${TODO}
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class dbAirDevice  {
    public String id;
    public String pid;
    public String name;
    public String place;
    public String onLine;
    public String Power;
    public boolean online;
    public String type;


    /*
    public Device(String id, String pid, String name, String place, boolean onLine, List<Power> power) {
     this.type = 3;
     this.id = id;
     this.pid = pid;
     this.name = name;
     this.place = place;
     this.online = onLine;
     this.power = power;
     }
     */
    public dbAirDevice() {
    }
    public dbAirDevice(String id, String pid, String name, String place, String onLine,String power,String type) {
        this.id=id;
        this.id=pid;
        this.id=name;
        this.id=place;
        this.id=onLine;
        this.Power=power;
        this.type=type;
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

    public String getOnLine() {
        return onLine;
    }

    public void setOnLine(String onLine) {
        this.onLine = onLine;
    }



    public String getPower() {
        return Power;
    }

    public void setPower(String power) {
        Power = power;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    @Override
    public String toString() {
        return "dbAirDevice{" +
                "id='" + id + '\'' +
                ", pid='" + pid + '\'' +
                ", name='" + name + '\'' +
                ", place='" + place + '\'' +
                ", onLine='" + onLine + '\'' +
                ", Power='" + Power + '\'' +
                ", online=" + online +
                ", type='" + type + '\'' +
                '}';
    }
}
