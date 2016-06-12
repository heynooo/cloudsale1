package com.vanhitech.vanhitech;

import android.test.AndroidTestCase;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.vanhitech.protocol.cmd.client.CMD10_DelDevice;
import com.vanhitech.protocol.cmd.client.CMD24_QueryTimer;
import com.vanhitech.protocol.object.Power;
import com.vanhitech.protocol.object.device.Device;
import com.vanhitech.vanhitech.base.CmdBaseActivity;
import com.vanhitech.vanhitech.bean.CWDevice0D;
import com.vanhitech.vanhitech.bean.Place;
import com.vanhitech.vanhitech.utils.DataUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 * 创建者     heyn
 * 创建时间   2016/4/9 18:58
 * 描述	      ${TODO}
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class testCmd  extends AndroidTestCase {
    private DbUtils mDb;

    //关闭全部的设备
    public void testCloseLight(){

        //电源的数据库返回
        com.vanhitech.vanhitech.bean.Power power = new com.vanhitech.vanhitech.bean.Power();

        Boolean flag=true;//设置全部关掉
        List<Device> mList = new ArrayList<Device>();//setPowers 设置开关集合,
        List<Device> lList = new ArrayList<Device>();//setPowers 设置后开关集合,
        List<Power> pOList = new ArrayList<Power>();//setPowers 设置后开关集合,
//        Power p0= new Power(0,flag);
//
//        Power p1= new Power(1,flag);
//        pOList.add(p1);
        mList = updateUI();
        List<com.vanhitech.vanhitech.bean.Power> lisp= new ArrayList<>();
        for(Device d:mList){
//            pOList.clear();
//            lisp.clear();
//            try {
//                    //id不能是wif
//                String temp=d.id.substring(1, 3);//TODO
//                if(temp.equals(Constants.id7)){}else {
//                    lisp.addAll(DataUtils.getInstance().getdataOnlyPower(d));
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            lisp.
            List<Power> powers = new ArrayList<Power>();
//           com.vanhitech.vanhitech.bean.Power p= DataUtils.getInstance().getdataPower(d.id+"").get(0);
//               if(p.num!=null){
//                if(p.num.equals("1")){
//                    pOList.add(p0);
//                    d.setPower(flag);
//                    d.power.add(0,p0);

//                    Power p1 = new Power(1, flag);
                    Power p0 = new Power(0, flag);
                    powers.add(p0);
//                    powers.add(p1);
                    d.setPowers(powers);
                }
//            if(p.num.equals("2")){
//                Power p1 = new Power(1, flag);
//                Power p0 = new Power(0, flag);
//                powers.add(p0);
//                powers.add(p1);
//                d.setPowers(powers);
//
//            }
//               }
////            d.setPowers(pOList);
//            lList.add(d);

        }
//        for(Device mP:lList) {
//            CmdBaseActivity.getInstance().cmdLight(mP);
////            PublicCmdHelper.getInstance().closeLight(mP);
//        }
//    }

    private String mIs;
    private Boolean flag = false;

    List<Place> Plist ;
    List<String> lisPager;
    List<Device> devices ;//装设备
    List<CWDevice0D> listCWDDevice;
    //取设备
    private List<Device> updateUI() {
        Plist = new ArrayList<>();
        lisPager = new ArrayList<String>();
        devices = new ArrayList<Device>();//装设备
        listCWDDevice = new ArrayList<CWDevice0D>();
        Plist.addAll(DataUtils.getInstance().getcrePlaceSave());
        for (Place place : Plist) {
            lisPager.add(place.place);
        }

        devices.addAll(DataUtils.getInstance().getdataDevice());//数据库加载数据
        devices.addAll(DataUtils.getInstance().getdataRGBDevice());
//        listCWDDevice= DataUtils.getInstance().getdataCWDDevice();// 如果数据为空,就报错
        Device d;
        for(CWDevice0D cwd:listCWDDevice){
            d=new Device();
            d.name=  cwd.name;
            d.id=cwd.id;
            d.place=cwd.place;
            devices.add(d);
        }
        return devices;
    }



    public void test3() throws DbException {
//        List<CWDevice0D> mList = new ArrayList<CWDevice0D>();
//        List<Place> mPist = new ArrayList<Place>();
//        List<CWDevice0D> mLista = new ArrayList<CWDevice0D>();
//        //查找全部
//        mDb = DbUtils.create(getContext());
//        mDb.configAllowTransaction(true);//??
//        mDb.configDebug(true);
////        mList=  mDb.findAll(Selector.from(Device.class));
////        mPist=  mDb.findAll(Selector.from(Place.class));
//        mList=  mDb.findAll(Selector.from(CWDevice0D.class));
//        //删除全部
//        for(CWDevice0D s:mList){
//            mLista.add(s);
//        }
////        mDb.deleteAll(mLista);
        List<Device> mPist = new ArrayList<Device>();
        try {
            mPist=  mDb.findAll(Selector.from(Device.class));
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void test4() throws DbException {
        List<Device> mList = new ArrayList<Device>();
        List<Place> mPist = new ArrayList<Place>();
        List<CWDevice0D> mLista = new ArrayList<CWDevice0D>();
        //查找全部
        mDb = DbUtils.create(getContext());
        mDb.configAllowTransaction(true);//??
        mDb.configDebug(true);
//        mList=  mDb.findAll(Selector.from(Device.class));
//        mPist=  mDb.findAll(Selector.from(Place.class));
        mList=  mDb.findAll(Selector.from(Device.class));
        //删除全部
        for(Device s:mList){
//            mLista.add(s);
        }
//        mDb.deleteAll(mLista);


    }

    public void testDelDevice(){

        try {//从数据库获取开关是开还是关，然后赋值
            String id="03525300001316";

            CMD10_DelDevice cmd10 = new CMD10_DelDevice(id);

            CmdBaseActivity.getInstance().sendCmd(cmd10);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    public void test0E(){
        //添加设备
//        NormalListDialogCustomAttr();
        //CMD0E_AddSlaveDevice
    }
    /**
     * 测试25,返回某个设备的定时任务指令
     * CMD24_QueryTimer
     */
    public void testCMD24_QueryTimer(){
        CMD24_QueryTimer cmd24=new CMD24_QueryTimer("035253000013B1");
        CmdBaseActivity.getInstance().sendCmd(cmd24);
    }
    public  void test21(){
        int offset = TimeZone.getDefault().getRawOffset() / 1000 / 3600;
        int off= TimeZone.getDefault().getRawOffset();

        String tempoff=off+"";
        String temp =offset+"";



    }


}
