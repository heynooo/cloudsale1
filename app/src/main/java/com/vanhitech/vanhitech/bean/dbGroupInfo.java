package com.vanhitech.vanhitech.bean;

/**
 * 创建者     heyn
 * 创建时间   2016/5/24 16:23
 * 描述	      ${TODO}
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class dbGroupInfo {

    public String id;
    public String groupid;
    public String name;
    public int sortidx;


    public dbGroupInfo() {
    }


    public dbGroupInfo(String id, String groupid, String name, int sortidx) {
        this.id = id;
        this.groupid = groupid;
        this.name = name;
        this.sortidx = sortidx;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSortidx() {
        return sortidx;
    }

    public void setSortidx(int sortidx) {
        this.sortidx = sortidx;
    }

    @Override
    public String toString() {
        return "dbGroupInfo{" +
                "id='" + id + '\'' +
                ", groupid='" + groupid + '\'' +
                ", name='" + name + '\'' +
                ", sortidx=" + sortidx +
                '}';
    }
}
