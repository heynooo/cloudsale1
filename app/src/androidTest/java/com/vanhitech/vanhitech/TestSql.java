package com.vanhitech.vanhitech;

import android.content.Context;
import android.test.AndroidTestCase;
import android.widget.Toast;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.vanhitech.protocol.cmd.client.CMD04_GetAllDeviceList;
import com.vanhitech.protocol.object.SceneMode;
import com.vanhitech.protocol.object.device.Device;
import com.vanhitech.protocol.object.device.LightCDevice;
import com.vanhitech.protocol.object.device.LightCWDevice;
import com.vanhitech.protocol.object.device.LightRGBDevice;
import com.vanhitech.protocol.object.device.TranDevice;
import com.vanhitech.vanhitech.base.CmdBaseActivity;
import com.vanhitech.vanhitech.base.MyApplication;
import com.vanhitech.vanhitech.bean.CWDevice0D;
import com.vanhitech.vanhitech.bean.Place;
import com.vanhitech.vanhitech.bean.Power;
import com.vanhitech.vanhitech.bean.TimeTaskDay;
import com.vanhitech.vanhitech.bean.UpdateTime;
import com.vanhitech.vanhitech.utils.DataUtils;
import com.vanhitech.vanhitech.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建者     heyn
 * 创建时间   2016/3/30 14:15
 * 描述	      ${TODO}
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class TestSql extends AndroidTestCase {

    private DbUtils mDb;

    @Override
    public Context getContext() {
        return super.getContext();
    }

    public TestSql() {
        super();
    }

    public void test1() {
        //创建数据库
        mDb = DbUtils.create(getContext());
        mDb.configAllowTransaction(true);//??
        mDb.configDebug(true);

    }

    public void test2() {
        mDb = DbUtils.create(getContext());
        mDb.configAllowTransaction(true);//??
        mDb.configDebug(true);

        //添加数据
        Device stu = null;
        List<Device> mList = new ArrayList<Device>();
        for (int i = 0; i < 20; i++) {
            stu = new Device();
            stu.id = "" + i;
            stu.type = i;
            stu.name = ("jack" + i);
            mList.add(stu);
            try {
                mDb.save(stu);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }

    }

    public void test3() throws DbException {
        List<CWDevice0D> mList = new ArrayList<CWDevice0D>();
        List<Place> mPist = new ArrayList<Place>();
        List<CWDevice0D> mLista = new ArrayList<CWDevice0D>();
        //查找全部
        mDb = DbUtils.create(getContext());
        mDb.configAllowTransaction(true);//??
        mDb.configDebug(true);
//        mList=  mDb.findAll(Selector.from(Device.class));
//        mPist=  mDb.findAll(Selector.from(Place.class));
        mList = mDb.findAll(Selector.from(CWDevice0D.class));
        //删除全部
        for (CWDevice0D s : mList) {
            mLista.add(s);
        }
        mDb.deleteAll(mLista);


    }

    public void test4() {
        mDb = DbUtils.create(getContext());
        mDb.configAllowTransaction(true);//??
        mDb.configDebug(true);
        try {
            List<Device> stus = mDb.findAll(Selector.from(Device.class));
//            mDb.delete(stus.get(0)); //删除第一个
//            mDb.deleteAll(stus);        //无效果                     //jack1
            mDb.deleteById(Device.class, WhereBuilder.b("name", "=", "哈哈")); //??

//            mDb.dropTable(Device.class);//整个表都删除
//            mDb.dropDb();         //删除数据库
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void test5() {//修改数据
        mDb = DbUtils.create(getContext());
        mDb.configAllowTransaction(true);//??
        mDb.configDebug(true);
        try {

            List<Device> stus = mDb.findAll(Selector.from(Device.class));
            Device stu = stus.get(0);           //这里是按第几个去修改，有效，
            stu.name = "哈哈";                  //
            mDb.update(stu);
        } catch (DbException e) {
            e.printStackTrace();
        }


    }

    public void test6() {
        mDb = DbUtils.create(getContext());


        try {

            Device d = mDb.findFirst(Selector.from(Device.class).where("id", "between", new String[]{"1", "星期三"}));

//        Device user = mDb.findFirst(Selector.from(Device.class).where("name", "=", "张三4"));

//            mDb.delete(d);//删除

//            单个创建

//            Device stu= new Device();
//            stu.name="星期四";
//            stu.id="1";
//            mDb.save(stu);
            d.name = "星期四";
            //单个修改
            mDb.update(d);  //单个修改

        } catch (DbException e) {
            e.printStackTrace();
        }


    }

    public void test7() {

        //   2）创建数据库
        DbUtils.DaoConfig config = new DbUtils.DaoConfig(getContext());

//数据库的名字
//        config.setDbName("xUtils-demo");
        config.setDbName("yunhai");

//版本号
        config.setDbVersion(1);

//db还有其他的一些构造方法，比如含有更新表版本的监听器的
        DbUtils db = DbUtils.create(config);


//        //创建一个表Student    //得有id 才能创建成功
//        db.createTableIfNotExist(Student.class);
        try {
//            db.createTableIfNotExist(Device.class);
            db.createTableIfNotExist(Place.class);
//            db.createTableIfNotExist(DeviceTypeConst.class);
            db.createTableIfNotExist(LightCDevice.class);
            db.createTableIfNotExist(LightCWDevice.class);
            db.createTableIfNotExist(LightRGBDevice.class);
//            db.createTableIfNotExist(Query.class);
//            db.createTableIfNotExist(SceneDevice.class);
            db.createTableIfNotExist(TranDevice.class);

        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void test8() {
        DbUtils db = MyApplication.getDbUtils();
        try {
            LightCWDevice lightCWDevice = new LightCWDevice("0001", "0002", "0003", "00004");
            db.save(lightCWDevice);
            db.update(lightCWDevice);
//           Device mDevice = db.findFirst(Selector.from(Device.class).where("id", "=", "0D52530000051C"));
//           Device mDevice = db.findFirst(Selector.from(Device.class).where("id", "=", "007CC70924C6F9"));
            Power mDevice1 = db.findFirst(Selector.from(Power.class).where("id", "=", "0F525300000035"));
            Device mDevice2 = db.findFirst(Selector.from(LightCWDevice.class).where("id", "=", "0001"));
            String o = mDevice1.power0;
        } catch (DbException e) {
            e.printStackTrace();
        }

        try {
            Device mDevice2 = db.findFirst(Selector.from(LightCWDevice.class).where("id", "=", "0001"));
            mDevice2.toString();
        } catch (DbException e) {
            e.printStackTrace();
        }


    }

    public void test9() {
        DbUtils db = MyApplication.getDbUtils();//007CC70924C6F9 中控id

        try {
            CWDevice0D mDevice2 = db.findFirst(Selector.from(CWDevice0D.class).where("id", "=", "0D52530000051C"));
            mDevice2.toString();
        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    public void onlyslect() {
        DbUtils db = MyApplication.getDbUtils();
        try {
            Device d = db.findFirst(Selector.from(CWDevice0D.class).where("id", "between", new String[]{"0", "哈哈"}));
        } catch (DbException e) {
            e.printStackTrace();
        }


    }

    public void test10() {
        //返回一个符合条件的集合

        DbUtils db = MyApplication.getDbUtils();//007CC70924C6F9 中控id
        List<CWDevice0D> c = new ArrayList<CWDevice0D>();
        try {
            List<Device> list = db.findAll(Selector.from(Device.class)
                    .where("firmver", "=", 0));
//                    .and(WhereBuilder.b("age", ">", 20).or("age", " < ", 30))
//                    .orderBy("id")
//                    .limit(pageSize)
//                    .offset(pageSize * pageIndex));


//            c = db.findFirst(Selector.from(CWDevice0D.class).where("id", "=", "0D52530000051C"));
            list.toString();
        } catch (DbException e) {
            e.printStackTrace();
        }


    }

    //单个删除 dbutils.delete(User.class, WhereBuilder.b("name", "=","仓老师"));
    public void test11() {
        try {
            DataUtils.getInstance().mDb.delete(Device.class, WhereBuilder.b("id", "=", "035253000011A2"));
        } catch (DbException e) {
            e.printStackTrace();
        }


    }

    //添加一个设备
    public void test14() {
        //
        Device d = new Device("035253000011A2", "007CC70924C6F9", "2.4G灯开关控制", "客厅", false, null);
        try {
            DataUtils.getInstance().mDb.save(d);
        } catch (DbException e) {
            e.printStackTrace();
        }

    }


    //测试是否发送cmd4,就能更新数据  没效果
    public void test13() {
        CMD04_GetAllDeviceList cmd04 = new CMD04_GetAllDeviceList();
        CmdBaseActivity.getInstance().sendCmd(cmd04);
    }

    //LightCWDevice 查找cwdevice0d
    public void test17() {
        DbUtils db = MyApplication.getDbUtils();//007CC70924C6F9 中控id

        try {
            CWDevice0D mDevice2 = db.findFirst(Selector.from(CWDevice0D.class).where("id", "=", "0D52530000051C"));
            mDevice2.toString();
        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    //查找表下的数量
    public void test18() {
        try {
            long count = DataUtils.getInstance().mDb.count(LightCWDevice.class);
            Toast.makeText(mContext, "count:" + count, Toast.LENGTH_SHORT).show();
        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    //saveOrUpdateAll 这个是保存或者更新?
    public void test19() {
        CWDevice0D d = new CWDevice0D("1", "2", "3", "5");
        Device c = new Device();
        c.id = d.id;
        c.pid = d.pid;
        c.name = d.name;
        c.place = d.place;
        try {
            DataUtils.getInstance().mDb.saveOrUpdate(c);
        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    //通过设备的id,查找全部的定时设置
    public void test20() {
        List<TimeTaskDay> mList = new ArrayList<TimeTaskDay>();
        try {
            mList = DataUtils.getInstance().mDb.findAll(Selector.from(TimeTaskDay.class).where("deviceId", "=", "035253000013B1"));
            mList.toString();
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    //查找全部
    public void test12() {
        List<LightCWDevice> mList = new ArrayList<LightCWDevice>();
        try {
            mList = DataUtils.getInstance().mDb.findAll(Selector.from(LightRGBDevice.class));
            mList.toString();
        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    //查找时间
    public void test15() {
        try {
            UpdateTime time = DataUtils.getInstance().mDb.findFirst(UpdateTime.class);
            String t = time.time;
            t.toString();
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    //优化
    public void test16() {
//        String s= DataUtils.getInstance().updateTime();
//        s.toString();
    }

    //查找时间
    public void test21() {
        try {                //mDb.findFirst(Selector.from(Device.class).where("id","between", new String[]{"1", "星期三"}));
            UpdateTime time = DataUtils.getInstance().mDb.findFirst(Selector.from(UpdateTime.class).where("id", "=", "0"));
            String t = time.time;
            t.toString();
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    //保存
    public void test22() {
//        SceneMode resultdata1 = new SceneMode(DateUtils.getCurTime(),1,"哈哈1");
        SceneMode resultdata2 = new SceneMode(DateUtils.getCurTime(), 2, "哈哈2");
//        SceneMode resultdata3 = new SceneMode(DateUtils.getCurTime(),3,"哈哈3");
//        SceneMode resultdata4 = new SceneMode(DateUtils.getCurTime(),4,"哈哈4");
        DataUtils.getInstance().save(resultdata2);

    }

    //取
    public void test23() {

        List<SceneMode> list = new ArrayList<SceneMode>();
        list = DataUtils.getInstance().getdataSceneMode(SceneMode.class, list);
        list.toString();
    }

    /**
     * 按用户id 取出房间
     *
     * @param userid
     * @return
     */
    public List<Place> test25(String place, String userid) {
        List<Place> mList = new ArrayList<Place>();
        try {
            mList = mDb.findAll(Selector.from(TimeTaskDay.class).where("place", "userid", new String[]{place, userid}));
            return mList;
        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }

    }
    public List<Place> test27()
    {
        String place = "其他";
        String userid= "13900000010";
        List<Place> mList = null;
        try {
            mList = DataUtils.getInstance().mDb.findAll(Selector.from(Place.class).where("place", "userid", new String[]{place, userid}));
        } catch (DbException e) {
            e.printStackTrace();
        }
        mList.toString();
        return  mList;
    }

    /**
     * 查找全部
     * @return
     */
    public List<Place> test28()
    {
        String place = "书房";
        String userid= "13900000010";
        List<Place> mList = null;
        try {//where("id","between", new String[]{"0", "哈哈"}));  .where("name","=","test"));
            mList = DataUtils.getInstance().mDb.findAll(Selector.from(Place.class).where("place","=",place).and("userid","=",userid));
        } catch (DbException e) {
            e.printStackTrace();
        }
        mList.toString();
        return  mList;
    }


    /**
     * 查找全部
     * @return
     */
    public List<Place> test29()
    {
        String place = "书房";
        String userid= "13900000010";
        List<Place> mList = null;
        try {//where("id","between", new String[]{"0", "哈哈"}));  .where("name","=","test"));
            mList = DataUtils.getInstance().mDb.findAll(Selector.from(Place.class).where("userid","=",userid));
        } catch (DbException e) {
            e.printStackTrace();
        }
        mList.toString();
        return  mList;
    }

    //findFirst(Selector.from(Place.class).where("place", "=", place).and("userid", "=", userid));

    /**
     * 单个
     * @return
     */
    public Place test30()
    {
        String place = "书房";
        String userid= "13900000010";
        Place mList =null;
        try {//where("id","between", new String[]{"0", "哈哈"}));  .where("name","=","test"));
            mList = DataUtils.getInstance().mDb.findFirst(Selector.from(Place.class).where("place", "=", place).and("userid", "=", userid));
        } catch (DbException e) {
            e.printStackTrace();
        }
        mList.toString();
        return  mList;
    }
    public Place getPlace(String place, String userid) {

        try {//
            Place mPower = DataUtils.getInstance().mDb.findFirst(Selector.from(Place.class).where("place", "userid", new String[]{place, userid}));
            return mPower;
        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }

    }
}
