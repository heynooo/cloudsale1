package com.vanhitech.vanhitech.utils;

import android.content.SharedPreferences.Editor;
import android.support.annotation.NonNull;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.vanhitech.protocol.cmd.ServerCommand;
import com.vanhitech.protocol.cmd.server.CMD05_ServerRespondAllDeviceList;
import com.vanhitech.protocol.cmd.server.CMD09_ServerControlResult;
import com.vanhitech.protocol.object.SceneMode;
import com.vanhitech.protocol.object.TimerTask;
import com.vanhitech.protocol.object.device.AirTypeADevice;
import com.vanhitech.protocol.object.device.Device;
import com.vanhitech.protocol.object.device.LightCWDevice;
import com.vanhitech.protocol.object.device.LightRGBDevice;
import com.vanhitech.protocol.object.device.LockDoorDevice;
import com.vanhitech.vanhitech.base.MyApplication;
import com.vanhitech.vanhitech.bean.CWDevice0D;
import com.vanhitech.vanhitech.bean.Place;
import com.vanhitech.vanhitech.bean.Power;
import com.vanhitech.vanhitech.bean.TimeTaskDay;
import com.vanhitech.vanhitech.bean.UInfo;
import com.vanhitech.vanhitech.bean.UpdateTime;
import com.vanhitech.vanhitech.bean.WInfo;
import com.vanhitech.vanhitech.bean.dbAirDevice;
import com.vanhitech.vanhitech.bean.dbSceneDeviceInfo;
import com.vanhitech.vanhitech.bean.dbTest;
import com.vanhitech.vanhitech.conf.Constants;
import com.vanhitech.vanhitech.conf.LoginConstants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @创建者
 * @创时间
 * @描述 对数据库的操作的封装
 * @版本 $Rev: 3 $
 * @更新者
 * @更新时间
 * @更新描述 TODO
 */
public class DataUtils {
    public DbUtils mDb;
    private Editor mEditor;

    public DataUtils() {
        mDb = DbUtils.create(MyApplication.getContext());
        mDb.configAllowTransaction(true);//??
        mDb.configDebug(true);
    }

    private static DataUtils instance = new DataUtils();

    public static DataUtils getInstance() {
        return instance;
    }

    public void save(Object entity) {
        try {
            mDb.saveOrUpdate(entity);
        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    public Void findFirst(Class<T> entityType) {

        try {
            return mDb.findFirst(Selector.from(entityType));
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    //#################SceneDevice ##############################        */

    /**
     * 删除dbSceneDeviceInfo
     * 2 * 通过场景id获取dbSceneDeviceInfo
     * 3 * SceneMode表给删掉
     * 4* 存sceneMode
     * 5* 取出SceneMode数据
     * * 删除SceneMode
     */

    public Boolean deletedbSceneDeviceInfo(String id) {
        try {
            mDb.delete(dbSceneDeviceInfo.class, WhereBuilder.b("id", "=", id.replaceAll(" ", "")));
            return true;
        } catch (DbException e) {
            e.printStackTrace();
            return false;
        }

    }

    public List<dbSceneDeviceInfo> getSceneDevice(String sceneid) {
        List<dbSceneDeviceInfo> mList = new ArrayList<dbSceneDeviceInfo>();
        try {
            mList = mDb.findAll(Selector.from(dbSceneDeviceInfo.class).where("sceneid", "=", sceneid));
            return mList;
        } catch (DbException e) {
            e.printStackTrace();
            return mList;
        }
    }

    public void sCeneDelete() {
        try {
            DataUtils.getInstance().mDb.deleteAll(SceneMode.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void sceneModeSave(List<SceneMode> sceneList1) {
        List<SceneMode> sceneList = sceneList1;
        for (SceneMode scene : sceneList) {
//           if( scene.order+""==null){
//               scene.order=0;
//           }
            save(scene);
        }
    }

    public List<SceneMode> getdataSceneMode(Class<SceneMode> entityType, List<SceneMode> mList) {
        try {
            mList = mDb.findAll(Selector.from(entityType));
        } catch (DbException e) {
            e.printStackTrace();
        }
        return mList;
    }

    public List<?> getdata(Class<?> entityType, List<?> mList) {
        try {
            mList = mDb.findAll(Selector.from(entityType));
        } catch (DbException e) {
            e.printStackTrace();
        }
        return mList;
    }

    public Boolean deleteSceneMode(String id) {
        try {
            mDb.delete(SceneMode.class, WhereBuilder.b("id", "=", id.replaceAll(" ", "")));
            return true;
        } catch (DbException e) {
            e.printStackTrace();
            return false;
        }

    }

    //#################Device ##############################        */

    /**
     * 取出Device数据
     * * 存入Device 再更新一下
     * * 取出空调基本数据
     * * 删除设备
     * * 删除已知的表
     * // deleteAll
     * //获取所有的设备
     */
    public List<Device> getdataDevice() {
        List<Device> mList = new ArrayList<Device>();
        try {
            mList = mDb.findAll(Selector.from(Device.class));
        } catch (DbException e) {
            e.printStackTrace();
        }
        return mList;
    }

    public void dataDevice(ServerCommand cmd) {
        CMD05_ServerRespondAllDeviceList cmd05 = (CMD05_ServerRespondAllDeviceList) cmd;
        for (Device device : cmd05.deviceList) {
            try {
                mDb.saveOrUpdate(device);//添加数据库

            } catch (DbException e) {
                e.printStackTrace();
            }
        }
        for (Device device : cmd05.deviceList) {
            try {
                mDb.update(device);//添加数据库

            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Device> getdataAIRDevice() {

        try {
            List<dbAirDevice> mAirList = mDb.findAll(Selector.from(dbAirDevice.class));
            List<Device> mList = new ArrayList<>();
            if (!StringUtils.isNull(mAirList)) {
                for (dbAirDevice air : mAirList) {
                    Device device = airToDevice(air);
                    mList.add(device);
                }
            }
            return mList;
        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Boolean deleteDevice(String id) {
        try {
            mDb.delete(Device.class, WhereBuilder.b("id", "=", id));
            return true;
        } catch (DbException e) {
            e.printStackTrace();
            return false;
        }

    }

    public void deleteAllDevice() {
        try {
            mDb.deleteAll(Device.class);

        } catch (DbException e) {
            e.printStackTrace();
        }
        try {
            mDb.deleteAll(CWDevice0D.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
//        try {
//            mDb.deleteAll(Place.class);
//        } catch (DbException e) {
//            e.printStackTrace();
//        }
        try {
            mDb.deleteAll(LightRGBDevice.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        try {
            mDb.deleteAll(LightCWDevice.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        try {
            mDb.deleteAll(Power.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        try {
            mDb.deleteAll(dbAirDevice.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
//        try {
//            mDb.deleteAll(Place.class);
//        } catch (DbException e) {
//            e.printStackTrace();
//        }

    }

    public Device getDevice(String id) { //除了查找device 还要查找其他俩个 cwd 和rgb
        Device mDevice = deviceSelect(id);
        if (mDevice == null) {
            mDevice = rGBdeviceSelect(id);
        }
        if (mDevice == null) {
            mDevice = cWDdeviceSelect(id);
//            CWDevice0D cwd = cwdDevice0DSelect(id);
//            List<com.vanhitech.protocol.object.Power> lisp = new ArrayList<>();
//            lisp.add(new com.vanhitech.protocol.object.Power(0, false));
//            lisp.add(new com.vanhitech.protocol.object.Power(0, false));
//            lisp.add(new com.vanhitech.protocol.object.Power(0, false));
//            mDevice = new Device(cwd.id, cwd.pid, cwd.name, cwd.place, isTureFalse(cwd.online), lisp);
        }
        if (mDevice == null) {
            mDevice = aIRdeviceSelect(id);
        }

        return mDevice;

    }

    public List<Device> getAllDevice() {
        List<Device> devices = new ArrayList<Device>();//装设备
        List<Device> device = new ArrayList<Device>();//装设备
        device = getdataDevice();
        nulladd(devices, device);
        device = getdataRGBDevice();
        nulladd(devices, device);
        device = getdataCWDDevice();// 如果数据为空,就报错
        nulladd(devices, device);
        device = getdataAIRDevice();// 如果数据为空,就报错
        nulladd(devices, device);
        return devices;
    }
    //################# Light ##############################        */

    /**
     * 取出CWDevice0Device数据
     * 取出RGBDevice数据
     * * 取出rgbLight数据
     * 删除CWDevice0D设备
     * 删除RGBevice设备
     */
    public List<Device> getdataLightCWDDevice() {
        List<Device> mList = new ArrayList<Device>();
        try {
            mList = mDb.findAll(Selector.from(LightCWDevice.class));
        } catch (DbException e) {
            e.printStackTrace();
        }
        return mList;
    }

    public List<Device> getdataRGBDevice() {
        List<Device> mList = new ArrayList<Device>();
        try {
            mList = mDb.findAll(Selector.from(LightRGBDevice.class));
        } catch (DbException e) {
            e.printStackTrace();
        }
        return mList;
    }

    public LightRGBDevice getdataRGBDevice(String id) {
        LightRGBDevice power = null;
        try {
            power = mDb.findFirst(Selector.from(LightRGBDevice.class).where("id", "=", id));
        } catch (DbException e) {
            e.printStackTrace();
        }
        return power;
    }

    public Boolean deleteCWDevice0D(String id) {
        try {
            mDb.delete(CWDevice0D.class, WhereBuilder.b("id", "=", id));
            return true;
        } catch (DbException e) {
            e.printStackTrace();
            return false;
        }

    }

    public Boolean deleteRGBevice0D(String id) {
        try {
            mDb.delete(LightRGBDevice.class, WhereBuilder.b("id", "=", id));
            return true;
        } catch (DbException e) {
            e.printStackTrace();
            return false;
        }

    }

    //#################CWDevice0D ##############################        */

    /**
     * cwdDevice0D查找
     * <p/>
     * //用id 判断是否 存在
     * //用过 id　查找　返回　device
     * //
     * //RGBdevice
     * //RGBdevice
     * /**
     * 存入暖白灯
     * * 更新device
     * 需要先查找数据库 是否有一样， 一样的修改重新存储
     */

    public CWDevice0D cwdDevice0DSelect(String id) {

        try {
            CWDevice0D mDevice2 = mDb.findFirst(Selector.from(CWDevice0D.class).where("id", "=", id));
            return mDevice2;
        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }

    }

    public Boolean cwdDevice0Selectboolean(String id) {
        CWDevice0D c = cwdDevice0DSelect(id);
        if (c == null) {
            return false;
        } else {
            return true;
        }
    }

    public Device deviceSelect(String id) { //除了查找device 还要查找其他俩个 cwd 和rgb

        try {
            Device mDevice = mDb.findFirst(Selector.from(Device.class).where("id", "=", id));
            return mDevice;
        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }

    }

    public boolean deviceSelectboolean(String id) {

        Device c = deviceSelect(id);
        if (c == null) {
            return false;
        } else {
            return true;
        }

    }

    public LightRGBDevice rGBdeviceSelect(String id) {

        try {
            LightRGBDevice mDevice = mDb.findFirst(Selector.from(LightRGBDevice.class).where("id", "=", id));
            return mDevice;
        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }

    }

    public boolean LightRGBDeviceboolean(String id) {

        Device c = rGBdeviceSelect(id);
        if (c == null) {
            return false;
        } else {
            return true;
        }

    }

    public LightCWDevice cWDdeviceSelect(String id) {

        try {
            LightCWDevice mDevice = mDb.findFirst(Selector.from(LightCWDevice.class).where("id", "=", id));
            return mDevice;
        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }

    }

    public void cwdDevice0DSave(ServerCommand cmd) {

        CMD05_ServerRespondAllDeviceList cmd05 = (CMD05_ServerRespondAllDeviceList) cmd;
        for (Device device : cmd05.deviceList) {
            //BAOICU保存暖白灯 根据id号前２位判断
            if (device.id.substring(0, 2).equals(Constants.idD)) {
                cwdDevice0DSaveDevice(device);
            }
        }
    }

    public void cwdDevice0DSaveDevice(Device device) {
        try {
            LightCWDevice cwdvice = (LightCWDevice) device;
//            CWDevice0D d = new CWDevice0D(cwdvice);
            CWDevice0D d = new CWDevice0D(cwdvice.id, cwdvice.pid + "", cwdvice.name + "", cwdvice.place + "");
            d.setId(device.id);
            d.brightness = cwdvice.brightness + "";
            d.colortemp = cwdvice.colortemp + "";
            d.mode = cwdvice.mode + "";
            d.name = cwdvice.name + "";
            d.pid = cwdvice.pid + "";
            d.place = cwdvice.place + "";
            d.type = cwdvice.type;
            d.firmver = cwdvice.firmver + "";
            d.subtype = cwdvice.subtype + "";
            d.groupid = cwdvice.groupid + "";
            d.netinfo = cwdvice.netinfo + "";
            if (cwdvice.online) {
                d.online = 1 + "";
            } else {
                d.online = 0 + "";
            }

            //判断是更新还是保存,如果是5的话,就保存,如果是9就更新,
//更改 ,如果存在就更新 ,不存在就保存,先看数据库,数据是否对,如果不对就更改
            //根据id 查找单个,　到工具类里面处理
            if (cwdDevice0DSelect(cwdvice.id) == null) {
                mDb.save(d);//单独添加设备的开关状态
            } else {
                mDb.update(d);
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void updateDevice(Device d) {
        try {//判断 存在 update 不存在 保存
            if (deviceSelectboolean(d.id)) {
                //存在
                mDb.update(d);
            } else if (cwdDevice0Selectboolean(d.id)) {
                //存在
                cwdDevice0DUpdate(d);

            } else if (LightRGBDeviceboolean(d.id)) {
                //存在 加个判断 ,如果是空调设备 ,

                mDb.update(d);

            } else if (d.type == 10 || d.type == 0x05 || d.type == 254) {
                //不能向下转型 ,只能存在 ,然后再像办法取出了
//                dbAirDevice  dbairDevice = (dbAirDevice)d;
                dbAirDevice airDevice = getDbAirDevice(d);
                mDb.saveOrUpdate(airDevice);
                saveTest(d);
            } else {
//                mDb.save(d);
                mDb.saveOrUpdate(d);

            }
//            mDb.update(d);

        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    private void saveTest(Device d) throws DbException {
        AirTypeADevice a = (AirTypeADevice) d;
        dbTest db = new dbTest(a.type, a.id, a.pid, a.name, a.place
                , a.subtype, a.firmver, a.online, a.groupid, a.power
                , a.netinfo, a.group, a.mode, a.temp, a.speed, a.direct
                , a.adirect, a.mdirect, a.ckhour, a.timeon, a.timeoff, a.keyval);
        mDb.saveOrUpdate(db);
    }

    //#################Place ##############################        */

    /**
     * 按用户id 房间名字，取出房间对象
     * 按用户id  取出房间对象
     * * 取出数据place
     * 更新场景 只能用于修改添加的时候
     * 更新场景 只能用于修改添加的时候
     * 保存place
     * * 存入场景
     * * 删除Place
     */

    public Place getPlace(String place) {

        try {
            Place mPower = mDb.findFirst(Selector.from(Place.class).where("place", "=", place));
            return mPower;
        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }

    }

    public Place getPlaceUserid(String place, String userid) {

        try {
            return mDb.findFirst(Selector.from(Place.class).where("place", "=", place).and("userid", "=", userid));
        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }

    }

    public List<Place> getPlaceUserid(String userid) {
        List<Place> mList = null;
        try {
            return mList = mDb.findAll(Selector.from(Place.class).where("userid", "=", userid));
        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }

    }

    public List<Place> getcrePlaceSave() {
        List<Place> mPist = new ArrayList<Place>();
        try {
            mPist = mDb.findAll(Selector.from(Place.class));
        } catch (DbException e) {
            e.printStackTrace();
        }
        return mPist;
    }

    public void crePlaceUpdate(Set<String> place) {

        if (place != null) {//不为空才加进去,先查找到之前的  不需要更新先
            List<Place> dblist = getcrePlaceSave();//TODO 数据库里面的place
            int id = 0;
            if (dblist != null) {
                id = dblist.size();
                for (String s : place) { //遍历出来新的，如果不存在，就添加，如果存在不做处理
                    Boolean flag = true;
                    for (Place pl : dblist) {
                        if (StringUtils.isEmpty(pl.place)) {
                            pl.place = "null";
                        }
                        if (StringUtils.isEmpty(s)) {
                            s = "null";
                        }
                        if (s.equals(pl.place)) {//存在
                            LogUtils.i("---------存在--------------------");
                            flag = false;
                        } else { //不存在

                        }
                    }
                    if (flag) {

                        id = savePlace(id, s);
                        LogUtils.i("---------保存--------------------");

                    }

                }
            } else {//数据库里是空的直接保存
                /*
                如果里面有一个设备的房间是空的
                */

                savePlace(place);
            }
        }
    }

    public void savePlaceRoom(Place place) throws DbException {
//        deletePlace(place.place);
//        int count = (int) mDb.count(Place.class);
//        place.id = count + 1 + "";
        String temp = place.place + LoginConstants.USERNAME;
        place.id = temp.hashCode() + "";
        LogUtils.i(place.id + "--------------------------");
        mDb.saveOrUpdate(place);
    }

    public int savePlace(int id, String d) {
        Place place = new Place();
        String temp = place.place + LoginConstants.USERNAME;
        place.id = temp.hashCode() + "";
        place.place = d.replaceAll(" ", "");
        place.setUserid(LoginConstants.USERNAME);
        try {
            mDb.save(place);   //更新数据  TODO
        } catch (DbException e) {
            e.printStackTrace();
        }
        return id;
    }

    public void crePlaceSave(ServerCommand cmd) {
        //分场景,创建数据表
        Set<String> set = new HashSet<String>();
        CMD05_ServerRespondAllDeviceList cmd05 = (CMD05_ServerRespondAllDeviceList) cmd;
        for (Device device : cmd05.deviceList) {
            set.add(device.place);
        }
//        savePlace(set);
//        crePlaceUpdate(set);
        crePlaceSave(set);
    }

    private void crePlaceSave(Set<String> set) {
        Place place = new Place();
        for (String s : set) {
            place.place = s;
            place.userid = LoginConstants.USERNAME;
            try {
                savePlaceRoom(place);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }

    public void savePlace(Set<String> set) {
        int ID = 0;
        for (String s : set) {
            Place place = new Place();
            place.id = ID + "";
            ID++;
            if (StringUtils.isEmpty(s)) {
                s = "null";
            }
            place.place = s.replaceAll(" ", "");
            try {
                mDb.save(place);//添加数据库
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }

    public Boolean deletePlace(String place) {
        try {
            mDb.delete(Place.class, WhereBuilder.b("place", "=", place.replaceAll(" ", "")));
            return true;
        } catch (DbException e) {
            e.printStackTrace();
            return false;
        }

    }

    //#################dbAirDevice ##############################        */
    /**
     * 取出CWDevice0Device数据
     */

    public Boolean deletedbAirDevice(String id) {
        try {
            mDb.delete(dbAirDevice.class, WhereBuilder.b("id", "=", id));
            return true;
        } catch (DbException e) {
            e.printStackTrace();
            return false;
        }

    }

    @NonNull
    private Device airToDevice(dbAirDevice air) {
        if (air != null) {
            return new Device(air.getId(), air.getPid(), air.getName(), air.getPlace(), isTureFalse(air.getOnLine()), null);
        } else {
            return null;
        }
    }

    public List<Device> getdataCWDDevice() {

        try {
            List<Device> mList = mDb.findAll(Selector.from(LightCWDevice.class));
            return mList;
        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }
    }
    //#################门锁 ##############################        */

    /**
     * //
     * //用过 id　查找　返回　lockDoorDevice
     * 取出LockDoorDevice数据
     */
    public List<Device> getdataLockDoorDevice() {

        try {
            List<Device> mList = mDb.findAll(Selector.from(LockDoorDevice.class));
            return mList;
        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Device lockDoorDeviceSelect(String id) {

        try {
            LockDoorDevice lockDoorDevice = mDb.findFirst(Selector.from(LockDoorDevice.class).where("id", "=", id));
            return lockDoorDevice;
        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }
    }

    //#################Power ##############################        */

    /**
     * 取出power数据
     * 存入power开关状态
     * //lightPower 保存　　１.
     * //lightPower 从设备里面提取power
     * 更新power开关状态
     * // lightPower　更新　　２.
     * //lightPower  用过 id　查找　返回　device　 ３.
     * //lightPower   返回是否存在　，true 存在　，　false 不存在　　４.
     * //lightPower   判断 存在就更新,不存在就保存　　５
     **/
    public Power getdataPower(String id) {
        Power power = null;
        try {
            power = mDb.findFirst(Selector.from(Power.class).where("id", "=", id));
        } catch (DbException e) {
            e.printStackTrace();
        }
        return power;
    }

    public void lightPowerSave(ServerCommand cmd) {
        CMD05_ServerRespondAllDeviceList cmd05 = (CMD05_ServerRespondAllDeviceList) cmd;
        for (Device device : cmd05.deviceList) {
            lightPowerSaveDevice(device);
        }
    }

    public void lightPowerUpdate(ServerCommand cmd) {

        CMD09_ServerControlResult cmd09 = (CMD09_ServerControlResult) cmd;
        Device device = cmd09.status;
        lightPowerUpdateDevice(device);
    }

    public void lightPowerSaveDevice(Device device) {
        try {
            Power p = getToPower(device);
            mDb.save(p);//单独添加设备的开关状态
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    public Power getToPower(Device device) {
        Power p = new Power();
        p.num = device.power.size() + "";
        p.id = device.id;
        int size = device.power.size();
        if (size == 1) {
            if (device.power.get(0).on) {
                p.power0 = 1 + "";  //灯亮为1
            } else {
                p.power0 = 0 + "";  //灯灭为0
            }
        } else if (size == 2) {
            if (device.power.get(0).on) {
                p.power0 = 1 + "";  //灯亮为1
            } else {
                p.power0 = 0 + "";  //灯灭为0
            }
            if (device.power.get(1).on) {
                p.power1 = 1 + "";  //灯亮为1
            } else {
                p.power1 = 0 + "";  //灯灭为0
            }
        }
        return p;
    }

    public void lightPowerUpdateDevice(Device device) {
        try {
            Power p = new Power();
            p.id = device.id;
            int size = device.power.size();
            if (size == 1) {
                if (device.power.get(0).on) {
                    p.power0 = 1 + "";  //灯亮为1
                } else {
                    p.power0 = 0 + "";  //灯灭为0
                }
            } else if (size == 2) {
                if (device.power.get(0).on) {
                    p.power0 = 1 + "";  //灯亮为1
                } else {
                    p.power0 = 0 + "";  //灯灭为0
                }
                if (device.power.get(1).on) {
                    p.power1 = 1 + "";  //灯亮为1
                } else {
                    p.power1 = 0 + "";  //灯灭为0
                }
            }
            mDb.update(p);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public Power powerSelect(String id) { //除了查找device 还要查找其他俩个 cwd 和rgb

        try {
            Power mPower = mDb.findFirst(Selector.from(Power.class).where("id", "=", id));
            return mPower;
        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }

    }

    public boolean lightPowerSelectboolean(String id) {

        Power c = powerSelect(id);
        if (c == null) {
            return false;
        } else {
            return true;
        }

    }


    public void lightPowerIsSaveUpdate(Device d) {
        if (lightPowerSelectboolean(d.id)) {
            lightPowerUpdateDevice(d);
        } else {
            lightPowerSaveDevice(d);
        }
    }

    //#################TimeTaskDay　TimerTask  ##############################        */

    /**
     * //不能保存布尔值 用1表示ture 0表示f
     * // 1 提取
     * //TimeTaskDay 查找返回TimeTskDay
     * //TimeTaskDay  返回是否存在 true 存在　false 不存在
     * <p/>
     * //TimeTaskDay 更新
     * <p/>
     * //TimeTaskDay 保存
     * //TimeTaskDay 判断 存在就更新,不存在就保存
     * //删除
     * /**
     * 删除
     * /**
     * 删除
     * //通过设备id获取该设备下全部的定时信息
     */
    public TimeTaskDay getToTimeTaskDay(TimerTask schedinfo, Device ctrlinfo) {
        TimeTaskDay timeTaskDay = new TimeTaskDay();
        timeTaskDay.id = schedinfo.id;
        timeTaskDay.deviceId = ctrlinfo.id;

        timeTaskDay.minute = schedinfo.minute + "";
        timeTaskDay.pid = ctrlinfo.pid;
        if (ctrlinfo.online) {
            timeTaskDay.online = 1 + "";
        } else {
            timeTaskDay.online = 0 + "";
        }
        if (schedinfo.repeated) {
            timeTaskDay.repeated = 1 + "";
        } else {
            timeTaskDay.repeated = 0 + "";
        }
        if (schedinfo.enabled) {
            timeTaskDay.enabled = 1 + "";
        } else {
            timeTaskDay.enabled = 0 + "";
        }
        /**
         * 如果小时+时区差>24小时
         *  小时=小时-23
         *  get(0) 对应 D2
         *
         *  24>如果小时+时区差>-1
         *  get(0) 对于 D1
         *
         *  如果小时+时区差<0
         *  5-8+24 小时=小时+24
         *  get(0) 对应着D7
         *
         */
        int houtInt = schedinfo.hour - 16;
        if (houtInt >= 25) {

            houtInt -= 24;
            String houtString = houtInt + "";
            timeTaskDay.hour = houtString;

            if (schedinfo.day.get(6)) {
                timeTaskDay.D1 = 1 + "";
            } else {
                timeTaskDay.D1 = 0 + "";
            }

            if (schedinfo.day.get(0)) {
                timeTaskDay.D2 = 1 + "";
            } else {
                timeTaskDay.D2 = 0 + "";
            }
            if (schedinfo.day.get(1)) {
                timeTaskDay.D3 = 1 + "";
            } else {
                timeTaskDay.D3 = 0 + "";
            }
            if (schedinfo.day.get(2)) {
                timeTaskDay.D4 = 1 + "";
            } else {
                timeTaskDay.D4 = 0 + "";
            }
            if (schedinfo.day.get(3)) {
                timeTaskDay.D5 = 1 + "";
            } else {
                timeTaskDay.D5 = 0 + "";
            }
            if (schedinfo.day.get(4)) {
                timeTaskDay.D6 = 1 + "";
            } else {
                timeTaskDay.D6 = 0 + "";
            }
            if (schedinfo.day.get(5)) {
                timeTaskDay.D7 = 1 + "";
            } else {
                timeTaskDay.D7 = 0 + "";
            }

        } else if (houtInt < 0) {
            houtInt += 24;
            String houtString = houtInt + "";
            timeTaskDay.hour = houtString;
            if (schedinfo.day.get(1)) {
                timeTaskDay.D1 = 1 + "";
            } else {
                timeTaskDay.D1 = 0 + "";
            }

            if (schedinfo.day.get(2)) {
                timeTaskDay.D2 = 1 + "";
            } else {
                timeTaskDay.D2 = 0 + "";
            }
            if (schedinfo.day.get(3)) {
                timeTaskDay.D3 = 1 + "";
            } else {
                timeTaskDay.D3 = 0 + "";
            }
            if (schedinfo.day.get(4)) {
                timeTaskDay.D4 = 1 + "";
            } else {
                timeTaskDay.D4 = 0 + "";
            }
            if (schedinfo.day.get(5)) {
                timeTaskDay.D5 = 1 + "";
            } else {
                timeTaskDay.D5 = 0 + "";
            }
            if (schedinfo.day.get(6)) {
                timeTaskDay.D6 = 1 + "";
            } else {
                timeTaskDay.D6 = 0 + "";
            }
            if (schedinfo.day.get(0)) {
                timeTaskDay.D7 = 1 + "";
            } else {
                timeTaskDay.D7 = 0 + "";
            }
        } else {
            String houtString = houtInt + "";
            timeTaskDay.hour = houtString;

            if (schedinfo.day.get(0)) {
                timeTaskDay.D1 = 1 + "";
            } else {
                timeTaskDay.D1 = 0 + "";
            }

            if (schedinfo.day.get(1)) {
                timeTaskDay.D2 = 1 + "";
            } else {
                timeTaskDay.D2 = 0 + "";
            }
            if (schedinfo.day.get(2)) {
                timeTaskDay.D3 = 1 + "";
            } else {
                timeTaskDay.D3 = 0 + "";
            }
            if (schedinfo.day.get(3)) {
                timeTaskDay.D4 = 1 + "";
            } else {
                timeTaskDay.D4 = 0 + "";
            }
            if (schedinfo.day.get(4)) {
                timeTaskDay.D5 = 1 + "";
            } else {
                timeTaskDay.D5 = 0 + "";
            }
            if (schedinfo.day.get(5)) {
                timeTaskDay.D6 = 1 + "";
            } else {
                timeTaskDay.D6 = 0 + "";
            }
            if (schedinfo.day.get(6)) {
                timeTaskDay.D7 = 1 + "";
            } else {
                timeTaskDay.D7 = 0 + "";
            }
        }

        List<com.vanhitech.protocol.object.Power> powers = ctrlinfo.power;
        if (powers.size() == 0) {

        } else if (powers.size() == 1) {
            if (powers.get(0) != null && powers.get(0).on) {
                timeTaskDay.power0 = 1 + "";
            } else {
                timeTaskDay.power0 = 0 + "";
            }
        } else if (powers.size() == 2) {
            if (powers.get(0) != null && powers.get(0).on) {
                timeTaskDay.power0 = 1 + "";
            } else {
                timeTaskDay.power0 = 0 + "";
            }
            if (powers.get(1) != null && powers.get(1).on) {
                timeTaskDay.power1 = 1 + "";
            } else {
                timeTaskDay.power1 = 0 + "";
            }
        } else if (powers.size() == 3) {
            if (powers.get(0) != null && powers.get(0).on) {
                timeTaskDay.power0 = 1 + "";
            } else {
                timeTaskDay.power0 = 0 + "";
            }
            if (powers.get(1) != null && powers.get(1).on) {
                timeTaskDay.power1 = 1 + "";
            } else {
                timeTaskDay.power1 = 0 + "";
            }
            if (powers.get(2) != null && powers.get(2).on) {
                timeTaskDay.power2 = 1 + "";
            } else {
                timeTaskDay.power2 = 0 + "";
            }

        }
        return timeTaskDay;
    }

    public TimeTaskDay timeTaskDaySelect(String schedinfoID) {
        try {
            TimeTaskDay mTimeTaskDay = mDb.findFirst(Selector.from(TimeTaskDay.class).where("id", "=", schedinfoID));
            return mTimeTaskDay;
        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean timeTaskDaySelectBoolean(String schedinfoID) {
        TimeTaskDay tt = timeTaskDaySelect(schedinfoID);
        if (tt == null) {
            return false;
        } else {
            return true;
        }
    }

    public void TimeTaskDayUpdate(TimerTask schedinfo, Device ctrlinfo) {
        TimeTaskDay timeTaskDay = getToTimeTaskDay(schedinfo, ctrlinfo);
        try {
            mDb.saveOrUpdate(timeTaskDay);//单独添加设备的开关状态
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void TimeTaskDaySave(TimerTask schedinfo, Device ctrlinfo) {
        TimeTaskDay timeTaskDay = getToTimeTaskDay(schedinfo, ctrlinfo);
        try {
            mDb.saveOrUpdate(timeTaskDay);//单独添加设备的开关状态
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void TimeTaskDaySaveUpdate(TimerTask schedinfo, Device ctrlinfo) {
        if (timeTaskDaySelectBoolean(schedinfo.id)) {
            TimeTaskDayUpdate(schedinfo, ctrlinfo);
        } else {
            TimeTaskDaySave(schedinfo, ctrlinfo);
        }
    }

    public Boolean deleteTimerTask(String deviceId) {
        try {
            mDb.delete(TimeTaskDay.class, WhereBuilder.b("deviceId", "=", deviceId.replaceAll(" ", "")));
            return true;
        } catch (DbException e) {
            e.printStackTrace();
            return false;
        }

    }

    public Boolean deleteTimerTaskSing(String id) {
        try {
            mDb.delete(TimeTaskDay.class, WhereBuilder.b("id", "=", id.replaceAll(" ", "")));
            return true;
        } catch (DbException e) {
            e.printStackTrace();
            return false;
        }

    }

    public List<TimeTaskDay> getTimeTaskDayDeviceId(String deviceId) {
        List<TimeTaskDay> mList = new ArrayList<TimeTaskDay>();
        try {
            mList = mDb.findAll(Selector.from(TimeTaskDay.class).where("deviceId", "=", deviceId));
            return mList;
        } catch (DbException e) {
            e.printStackTrace();
            return mList;
        }
    }

    public TimerTask transform(TimerTask tt) {
        /**
         * 如果小时+时区差>24小时
         *  小时=小时-23
         *  get(0) 对应 D2
         *
         *  24>如果小时+时区差>-1
         *  get(0) 对于 D1
         *
         *  如果小时+时区差<0
         *  5-8+24 小时=小时+24
         *  get(0) 对应着D7
         *
         */
        int houtInt = tt.hour + 16;
        List<Boolean> d;
        if (houtInt >= 25) {

            houtInt -= 24;
            tt.hour = houtInt;
            d = new ArrayList<>();
            if (tt.day.get(6)) {
                d.add(0, tt.day.get(6));
            } else {
                d.add(0, tt.day.get(6));
            }

            if (tt.day.get(0)) {
                d.add(1, tt.day.get(0));
            } else {
                d.add(1, tt.day.get(0));
            }
            if (tt.day.get(1)) {
                d.add(2, tt.day.get(1));
            } else {
                d.add(2, tt.day.get(1));
            }
            if (tt.day.get(2)) {
                d.add(3, tt.day.get(2));
            } else {
                d.add(3, tt.day.get(2));
            }
            if (tt.day.get(3)) {
                d.add(4, tt.day.get(3));
            } else {
                d.add(4, tt.day.get(3));
            }
            if (tt.day.get(4)) {
                d.add(5, tt.day.get(4));
            } else {
                d.add(5, tt.day.get(4));
            }
            if (tt.day.get(5)) {
                d.add(6, tt.day.get(5));
            } else {
                d.add(6, tt.day.get(5));
            }
            tt.day = d;

        } else if (houtInt < 0) {
            houtInt += 24;
            d = new ArrayList<>();
            tt.hour = houtInt;
            if (tt.day.get(1)) {
                d.add(0, tt.day.get(1));
            } else {
                d.add(0, tt.day.get(1));
            }

            if (tt.day.get(2)) {
                d.add(1, tt.day.get(2));
            } else {
                d.add(1, tt.day.get(2));
            }
            if (tt.day.get(3)) {
                d.add(2, tt.day.get(3));
            } else {
                d.add(2, tt.day.get(3));
            }
            if (tt.day.get(4)) {
                d.add(3, tt.day.get(4));
            } else {
                d.add(3, tt.day.get(4));
            }
            if (tt.day.get(5)) {
                d.add(4, tt.day.get(5));
            } else {
                d.add(4, tt.day.get(5));
            }
            if (tt.day.get(6)) {
                d.add(5, tt.day.get(6));
            } else {
                d.add(5, tt.day.get(6));
            }
            if (tt.day.get(0)) {
                d.add(6, tt.day.get(0));
            } else {
                d.add(6, tt.day.get(0));
            }
            tt.day = d;
        } else {
            tt.hour = houtInt;
        }


        return tt;
    }


    //#################空调##############################        */

    /**
     * //AIRdevice
     * /**
     * 进行删除操作,删除,先从数据库获取数据的id遍历,和返回的数值逐个比较,不存在就删除)
     * /**
     * 更新暖白灯
     */
    @NonNull
    private dbAirDevice getDbAirDevice(Device d) {
        dbAirDevice airDevice = new dbAirDevice();
        airDevice.setId(d.id);
        airDevice.setName(d.name);
        String onli;
        if (d.online) {
            onli = 1 + "";
        } else {
            onli = 0 + "";
        }
        airDevice.setPower(isBooleanString(d.isPower()));
        airDevice.setOnLine(onli);
        airDevice.setPid(d.pid);
        airDevice.setPlace(d.place);
        airDevice.setPower(0 + "");
        airDevice.setType(d.type + "");
        airDevice.setOnline(d.online);
        return airDevice;
    }

    public Device aIRdeviceSelect(String id) {

        try {
            dbAirDevice air = mDb.findFirst(Selector.from(dbAirDevice.class).where("id", "=", id));
            if (air != null) {
                Device mDevice = airToDevice(air);
                return mDevice;
            }
        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public void updateDevicedelete(List<Device> d) {
        List<Device> lis = getdataDevice();
        if (getdataCWDDevice() != null)
            lis.addAll(getdataCWDDevice());
        if (getdataRGBDevice() != null)
            lis.addAll(getdataRGBDevice());
        Boolean flag = false;//默认没有

        if (lis != null)
            for (Device dbDevice : lis) {//把数据库的设备和返回的数据逐个对比,只要存在,flag就为true,flag为真,不操作,反之,删除

                for (Device device : d) {

                    if (dbDevice.id != null)
                        if (dbDevice.id.equals(device.id)) {
                            flag = true;
                        }
                }
                if (!flag) {
                    flag = false;
                    //删除这个数据
//                if (dbDevice.id.substring(0, 2).equals(Constants.idD)) {
//                    cwdDevice0DUpdate((LightCWDevice) device);
//                }else if(){
//
//                }else{
//                deleteDevice(dbDevice.id);
//                }
//
//                    if (!deleteDevice(dbDevice.id)) {
//
//                    } else if (deleteCWDevice0D(dbDevice.id)) {
//                    } else if (deleteRGBevice0D(dbDevice.id)) {
//                    }

                    deleteDevice(dbDevice.id);
                    deleteCWDevice0D(dbDevice.id);
                    deleteRGBevice0D(dbDevice.id);
                    deletedbAirDevice(dbDevice.id);

                } else {
                    flag = false;
                }
            }

    }

    public void cwdDevice0DUpdateCmd09(ServerCommand cmd) {//TODO

        CMD09_ServerControlResult cmd09 = (CMD09_ServerControlResult) cmd;
        Device device = cmd09.status;
        //BAOICU保存暖白灯 根据id号前２位判断
        if (device.id.substring(0, 2).equals(Constants.idD)) {
            cwdDevice0DUpdate(device);
        }
    }

    public void cwdDevice0DUpdate(Device device) {
//TODO
        try {
            LightCWDevice cwdvice = (LightCWDevice) device;
            CWDevice0D d = new CWDevice0D(cwdvice);
            d.id = cwdvice.id;
            d.brightness = cwdvice.brightness + "";
            d.colortemp = cwdvice.colortemp + "";
            d.mode = cwdvice.mode + "";
            d.name = device.name + "";
            d.pid = device.pid + "";
            d.firmver = cwdvice.firmver + "";
            d.subtype = cwdvice.subtype + "";
            d.groupid = device.groupid + "";
            d.netinfo = device.netinfo + "";
            d.place = device.place;

            if (device.online) {
                d.online = 1 + "";
            } else {
                d.online = 0 + "";
            }

            //判断是更新还是保存,如果是5的话,就保存,如果是9就更新,
            mDb.saveOrUpdate(d);

        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    //#################其他 ##############################        */

    /**
     * 取出数据库上次更新的时间
     * //用户信息
     * //TimeTaskDay 查找返回TimeTskDay
     * //wifi info
     * //TimeTaskDay 查找返回TimeTskDay
     * <p/>
     * //保存Uinfo 先判断是否存在， 存在取出来，赋新值 ，  不存在new 一个新的
     * //TimeTaskDay 判断 存在就更新,不存在就保存
     */
    public String updateTime(String id) {//Selector.from(UpdateTime.class).where("id", "=", "0")
        try {
            UpdateTime time = mDb.findFirst(Selector.from(UpdateTime.class).where("id", "=", id));
            if (time == null) {
                time = new UpdateTime();
            }
            String t = time.time;
            if (t == null) {
                t = "0";
            }
            return t;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public UInfo getUInfo(String phone) {
        try {
            UInfo uInfo = mDb.findFirst(Selector.from(UInfo.class).where("phone", "=", phone));
            return uInfo;
        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }
    }

    public WInfo getWInfo(String phone) {//
        try {
            WInfo uInfo = mDb.findFirst(Selector.from(WInfo.class).where("name", "=", phone));
            return uInfo;
        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void UInfoSaveUpdate(UInfo uInfo, String lumm) {
        UInfo u;
        if (getUInfo(uInfo.phone) == null) {
            u = new UInfo(1 + "", uInfo.pass, uInfo.userid, uInfo.phone, uInfo.email, uInfo.offset + "");
            save(u);
        } else {
            u = getUInfo(uInfo.phone);
            if (lumm != null && !lumm.isEmpty()) {
                u.lumm = lumm;
                save(u);
            }
        }
    }

    //#################change##############################        */

    private void nulladd(List<Device> devices, List<Device> device) {
        if (device != null) {
            devices.addAll(device);//数据库加载数据
            device.clear();
        }
    }

    public Boolean isTureFalse(String ttd) {
        Boolean online = false;
        if (ttd == null || ttd.equals("0")) {
            online = false;
        } else {
            online = true;
        }
        return online;
    }

    public String isBooleanString(Boolean flag) {
        if (flag) {
            return 1 + "";
        } else {
            return 0 + "";
        }
    }
}
