package com.vanhitech.vanhitech.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 创建者     heyn
 * 创建时间   2016/5/16 19:43
 * 描述	     情景和设备的id类
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class dbSceneDeviceInfo implements Parcelable {
    public String id;
    public String sceneid;
    public String deviceid;


    public dbSceneDeviceInfo() {
    }


    public dbSceneDeviceInfo(String id, String sceneid, String deviceid) {
        this.id = id;
        this.sceneid = sceneid;
        this.deviceid = deviceid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSceneid() {
        return sceneid;
    }

    public void setSceneid(String sceneid) {
        this.sceneid = sceneid;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.sceneid);
        dest.writeString(this.deviceid);
    }

    private dbSceneDeviceInfo(Parcel in) {
        this.id = in.readString();
        this.sceneid = in.readString();
        this.deviceid = in.readString();
    }

    public static final Parcelable.Creator<dbSceneDeviceInfo> CREATOR = new Parcelable.Creator<dbSceneDeviceInfo>() {
        public dbSceneDeviceInfo createFromParcel(Parcel source) {
            return new dbSceneDeviceInfo(source);
        }

        public dbSceneDeviceInfo[] newArray(int size) {
            return new dbSceneDeviceInfo[size];
        }
    };

    @Override
    public String toString() {
        return "dbSceneDeviceInfo{" +
                "id='" + id + '\'' +
                ", sceneid='" + sceneid + '\'' +
                ", deviceid='" + deviceid + '\'' +
                '}';
    }
}
