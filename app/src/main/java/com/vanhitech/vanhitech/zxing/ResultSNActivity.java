package com.vanhitech.vanhitech.zxing;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.dialog.entity.DialogMenuItem;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalListDialog;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.vanhitech.protocol.cmd.client.CMD0E_AddSlaveDevice;
import com.vanhitech.protocol.object.device.Device;
import com.vanhitech.vanhitech.R;
import com.vanhitech.vanhitech.base.BaseActivityController;
import com.vanhitech.vanhitech.base.CmdBaseActivity;
import com.vanhitech.vanhitech.conf.Constants;
import com.vanhitech.vanhitech.manager.ActivityTitleManager;
import com.vanhitech.vanhitech.utils.DataUtils;
import com.vanhitech.vanhitech.utils.DevicNameUtils;
import com.vanhitech.vanhitech.utils.LogUtils;
import com.vanhitech.vanhitech.utils.T;
import com.vanhitech.vanhitech.views.LightRgbTagView;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建者     heyn
 * 创建时间   2016/3/18 14:42
 * 描述	      ${TODO}
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class ResultSNActivity extends BaseActivityController {
    public LightRgbTagView mLrt_tag;
    private String mSn;
    private RelativeLayout itemHomelist;
    private ImageView itemHomelistIcon;
    private TextView itemHomelistName;
    private TextView itemHomelistStatus;
    private BitmapUtils mBitmapUtils;
    private RelativeLayout itemHomelistControl;
    private ImageView itemHomelistIconControl;
    private TextView itemHomelistNameControl;
    private TextView itemHomelistStatusControl;
    private String mName;
    private Button mBt_add;
    private Device mDevice;
    private ArrayList<DialogMenuItem> mMenuItems = new ArrayList<>();
    private List<Device> mList;
    private String mPlace;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);


    }

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_device_gdevice);
        assignViews();
        ActivityTitleManager.getInstance().init(this);
        ActivityTitleManager.getInstance().showCommonTitle();
        ActivityTitleManager.getInstance().changeTitle("2.4G设备");
//        ActivityTitleManager.getInstance().changeTitle("常见问题");
         mBitmapUtils = new BitmapUtils(this);
    }

    private void assignViews() {
        //扫描出来的设备信息
        itemHomelist = (RelativeLayout) findViewById(R.id.item_homelist);
        itemHomelistIcon = (ImageView) findViewById(R.id.item_homelist_icon);
        itemHomelistName = (TextView) findViewById(R.id.item_homelist_name);
        itemHomelistStatus = (TextView) findViewById(R.id.item_homelist_status);
        //控制中心
        itemHomelistControl = (RelativeLayout) findViewById(R.id.item_homelist_control);
        itemHomelistIconControl = (ImageView) findViewById(R.id.item_homelist_icon_control);//要添加设备的icon
        itemHomelistNameControl = (TextView) findViewById(R.id.item_homelist_name_control);
        itemHomelistStatusControl = (TextView) findViewById(R.id.item_homelist_status_control);
        mBt_add = (Button) findViewById(R.id.btn_gdevice_add);

    }

    @Override
    public void initData() {
        super.initData();
        Intent intent = getIntent();
        mPlace = intent.getStringExtra("place");
        mSn = intent.getStringExtra("SN");//id.substring(0, 2);
        mName = DevicNameUtils.idToName(mSn);
        LogUtils.i("---------------" + mName);
        itemHomelistName.setText(mName);
        itemHomelistStatus.setText(mSn);
        mSn.toString();

        try {
            mList = DataUtils.getInstance().mDb.findAll(Selector.from(Device.class)
                    .where("type", "=", 9));
        } catch (DbException e) {
            e.printStackTrace();
        }
        if(mList.size()>0){
            mDevice = mList.get(0);
            updateUI(mDevice);
        }
    }

    @Override
    public void initEvent() {
        super.initEvent();
        //发送添加设备命令
        itemHomelistControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NormalListDialogCustomAttr(mList);
            }
        });

        mBt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDevice != null) {
                    T.showShort(ResultSNActivity.this, "添加设备");
                    sendCmd(mDevice);
                    //传值跳转
                    finish();
                } else {
                    T.showShort(ResultSNActivity.this, "请选择中控设备");
                }
            }
        });

        //mSn
        //itemHomelistIconControl
        viewIcon(itemHomelistIcon,mSn);
    }


    private void NormalListDialogCustomAttr(final List<Device> list) {
        //查找数据库里面的控制中心 条件是type =9

        mMenuItems.clear();
        for (Device d : list) {
            mMenuItems.add(new DialogMenuItem(d.id + d.name, R.mipmap.devicedefault));
        }
        final NormalListDialog dialog = new NormalListDialog(this, mMenuItems);
        dialog.title("请选择")//
                .titleTextSize_SP(18)//
                .titleBgColor(Color.parseColor("#409ED7"))//
                .itemPressColor(Color.parseColor("#85D3EF"))//
                .itemTextColor(Color.parseColor("#303030"))//
                .itemTextSize(14)//
                .cornerRadius(0)//
                .widthScale(0.8f)//
                .show(R.style.myDialogAnim);


        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                T.showShort(ResultSNActivity.this, mMenuItems.get(position).mOperName);
                dialog.dismiss();
                //选中后更新ui　
                mDevice = list.get(position);
                updateUI(mDevice);
                //弹出弹框　添加到哪个房间　

            }
        });

    }

    private void sendCmd(Device d) {
        if(mSn.contains("-")){
        String[] s = mSn.split("-");
        String pass =  s[1];
        String sn =  s[0];

        String pid = d.id;
        String name = mName;
        String groupid = "0";
        String place = mPlace;
        CMD0E_AddSlaveDevice cmd0E_addSlaveDevice = new CMD0E_AddSlaveDevice(pass, sn, pid, name, place, groupid);
        CmdBaseActivity.getInstance().sendCmd(cmd0E_addSlaveDevice);
        }
    }

    private void updateUI(Device d) {
        itemHomelistNameControl.setText(d.name);
        itemHomelistStatusControl.setText(d.place);
    }

    private void  viewIcon(View view ,String id){

        String ID = id.substring(0, 2);
        if (ID.equals(Constants.id1)) {
            mBitmapUtils.display(view, "assets/img/home01.bmp");
        } else if (ID.equals(Constants.id2)) {

        } else if (ID.equals(Constants.id3)) {//2.4G灯开关控制  !
            mBitmapUtils.display(view, "assets/img/home03.bmp");
        } else if (ID.equals(Constants.id4)) {

        } else if (ID.equals(Constants.id5)) {
            mBitmapUtils.display(view, "assets/img/home0a.bmp");
        } else if (ID.equals(Constants.id6)) {
            mBitmapUtils.display(view, "assets/img/home06.bmp");
        } else if (ID.equals(Constants.id7)) {
            mBitmapUtils.display(view, "assets/img/home07.bmp");
        } else if (ID.equals(Constants.id8)) {

        } else if (ID.equals(Constants.id9)) {
            mBitmapUtils.display(view, "assets/img/home00.bmp");
        } else if (ID.equals(Constants.idA)) {
            mBitmapUtils.display(view, "assets/img/home0a.bmp");
        } else if (ID.equals(Constants.idB)) { //2.4G RGB灯控制  !
            mBitmapUtils.display(view, "assets/img/home0b.bmp");
        } else if (ID.equals(Constants.idC)) { //2.4G RGB灯控制  !
            mBitmapUtils.display(view, "assets/img/home06.bmp");
        } else if (ID.equals(Constants.idD)) {//2.4G冷暖灯开关及调光控制 !
            mBitmapUtils.display(view, "assets/img/home0b.bmp");
        } else if (ID.equals(Constants.idE)) {//2.4G RGB灯控制

        } else if (ID.equals(Constants.idF)) {//2.4G RGB灯控制  !
            mBitmapUtils.display(view, "assets/img/home06.bmp");
        } else if (ID.equals(Constants.id10)) {

        } else if (ID.equals(Constants.id11)) {

        } else if (ID.equals(Constants.id12)) {

        } else if (ID.equals(Constants.id13)) {
            mBitmapUtils.display(view, "assets/img/home13.bmp");
        } else if (ID.equals(Constants.id14)) {

        } else {
            //不处理
            mBitmapUtils.display(view, "assets/img/home00.bmp");
        }
    }

}
