package com.vanhitech.vanhitech.bean;

/**
 * 创建者     heyn
 * 创建时间   2016/3/30 16:47
 * 描述	      ${场所：比如客厅，书房，标识该设备在什么场所}
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class Place {

    public String url;
    public String id;
    public String place;
    public String userid;

    public Place(String url, String id, String place, String userid) {
        this.url = url;
        this.id = id;
        this.place = place;
        this.userid = userid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }



    public Place(String url, String id, String place) {
        this.url = url;
        this.id = id;
        this.place = place;
    }

    public Place() {
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
