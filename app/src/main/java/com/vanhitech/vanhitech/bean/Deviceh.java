package com.vanhitech.vanhitech.bean;

import com.vanhitech.protocol.object.JSONObject;
import com.vanhitech.protocol.object.NetInfo;
import com.vanhitech.protocol.object.Power;
import com.vanhitech.protocol.object.device.Device;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建者     heyn
 * 创建时间   2016/3/30 15:33
 * 描述	      ${TODO}
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class Deviceh extends JSONObject{
    //
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
        private static final long serialVersionUID = 6396727198013709511L;
       public String url;
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
        public Boolean power0;
        public Boolean power1;
        public NetInfo netinfo;

        public Deviceh() {
            this.type = 3;
        }

        public Deviceh(String id, String pid, String name, String place, boolean onLine, List<Power> power) {
            this.type = 3;
            this.id = id;
            this.pid = pid;
            this.name = name;
            this.place = place;
            this.online = onLine;
            this.power = power;
        }

        public Deviceh(Device device) {
            this.id = String.valueOf(device.id);
            this.pid = String.valueOf(device.pid);
            this.name = String.valueOf(device.name);
            this.place = String.valueOf(device.place);
            this.subtype = device.subtype;
            this.firmver = device.firmver;
            this.type = device.type;
            this.online = device.online;
            this.netinfo = device.netinfo;
            this.power = new ArrayList();

            for(int i = 0; i < device.power.size(); ++i) {
                Power temp = (Power)device.power.get(i);
                this.power.add(new Power(temp.way, temp.on));
            }

        }

        public List<Power> getPowers() {
            return this.power;
        }

        public void setPowers(List<Power> powers) {
            this.power = powers;
        }

        public boolean isPower() {
            return this.power != null && this.power.size() != 0?((Power)this.power.get(0)).on:false;
        }

        public void setPower(boolean power) {
            if(this.power != null && this.power.size() != 0) {
                ((Power)this.power.get(0)).on = power;
            } else {
                this.power = new ArrayList();
                Power p = new Power(0, power);
                this.power.add(p);
            }

        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
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
            return builder.toString();
        }


}
