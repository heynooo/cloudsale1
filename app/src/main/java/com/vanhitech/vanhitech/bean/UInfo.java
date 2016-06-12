package com.vanhitech.vanhitech.bean;

import com.vanhitech.protocol.object.UserInfo;

/**
 * 创建者     heyn
 * 创建时间   2016/4/18 17:52
 * 描述	      ${TODO}
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class UInfo {
    public String id;
    public String name;
    public String pass;
    public String userid;
    public String phone;
    public String email;
    public String offset;
    public String remark;//备注
    public String lumm;

    public UInfo() {
    }
//UserInfo info
    public UInfo(String id, String pass, String userid, String phone, String email, String offset) {
        this.id=id;
        this.pass = pass;
        this.userid = userid;
        this.phone = phone;
        this.email = email;
        this.offset = offset;

    }
    public UInfo(String id, UserInfo info,String remark) {
        this.id=id;
        this.pass = info.pass;
        this.userid = info.userid;
        this.phone = info.phone;
        this.email = info.email;
        this.offset = info.offset+"";
        this.remark = remark;

    }


    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("(");
        builder.append("id:").append(this.id);
        builder.append("name:").append(this.name);
        builder.append(", pass:").append(this.pass);
        builder.append(", userid:").append(this.userid);
        builder.append(", phone:").append(this.phone);
        builder.append(", email:").append(this.email);
        builder.append(", offset:").append(this.offset);
        builder.append("})");
        return builder.toString();
    }
}
