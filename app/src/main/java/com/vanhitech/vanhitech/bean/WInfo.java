package com.vanhitech.vanhitech.bean;

/**
 * 创建者     heyn
 * 创建时间   2016/4/18 17:52
 * 描述	      ${TODO}
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class WInfo {
    public String id;
    public String name;
    public String pass;


    public WInfo() {
    }
//UserInfo info
    public WInfo(String id, String name, String pass) {
        this.id=id;
        this.name = name;
        this.pass = pass;


    }



    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("(");
        builder.append("id:").append(this.id);
        builder.append("name:").append(this.name);
        builder.append(", pass:").append(this.pass);
        builder.append("})");
        return builder.toString();
    }
}
