package com.vanhitech.vanhitech.bean;

/**
 * 创建者     heyn
 * 创建时间   2016/4/16 19:39
 * 描述	      ${TODO}
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class UpdateTime {
    public String id;
    public String time;

    public UpdateTime() {
    }

    public UpdateTime(String id,String time) {
        this.time = time;
        this.id =id;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "UpdateTime{" +
                "id='" + id + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
