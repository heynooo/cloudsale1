package com.vanhitech.vanhitech.activity.device;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.vanhitech.protocol.cmd.ServerCommand;
import com.vanhitech.protocol.cmd.client.CMD06_QueryDeviceStatus;
import com.vanhitech.protocol.cmd.client.CMD08_ControlDevice;
import com.vanhitech.protocol.cmd.server.CMD07_ServerRespondDeviceStatus;
import com.vanhitech.protocol.cmd.server.CMD09_ServerControlResult;
import com.vanhitech.protocol.cmd.server.CMDFF_ServerException;
import com.vanhitech.protocol.object.device.Device;
import com.vanhitech.vanhitech.R;
import com.vanhitech.vanhitech.base.BaseActivityController;
import com.vanhitech.vanhitech.base.CmdBaseActivity;
import com.vanhitech.vanhitech.manager.ActivityTitleManager;
import com.vanhitech.vanhitech.utils.UIUtils;
import com.vanhitech.vanhitech.utils.Util;
import com.vanhitech.vanhitech.views.CurtainView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 创建者     heyn
 * 创建时间   2016/3/18 14:42
 * 描述	      ${窗帘界面}
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class CurtainActivity1 extends BaseActivityController {

    private String mId;
    private int mNum;
    private int mWidth;
    private int mHight;
    private int mVr_int;
    private int mVl_int;

    private CurtainView myview_right, myview_lete;
    private ImageView leafL1, leafL2, leafL3, leafL4, leafL5, leafR1, leafR2, leafR3, leafR4, leafR5;
    private CheckBox cbCuttainClose;
    private CheckBox cbCuttainOpen;
    private CheckBox cbCuttainPause;
    private CheckBox iv_device_info;
    private CheckBox iv_timer;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (mVl_int < mWidth / 2 && mVl_int > -1) {
                        mVl_int = mVl_int + mNum;
                        myview_lete.setRight(mVl_int);
                        mVr_int = mVr_int - mNum;
                        myview_right.setLeft(mVr_int);//右边
                        if (mNum == 1) {
//                            level_left(mVl_int);}
                            if (mNum == -1) {
//                            level_right(mVl_int);
                            }
                        }
                        break;
                    }
            }
        }


    };
    private Device mDevice;

    private void assignViews() {
        myview_right = (CurtainView) findViewById(R.id.myview_right);
        myview_lete = (CurtainView) findViewById(R.id.myview_lete);
        leafL1 = (ImageView) findViewById(R.id.leaf_l1);
        leafL2 = (ImageView) findViewById(R.id.leaf_l2);
        leafL3 = (ImageView) findViewById(R.id.leaf_l3);
        leafL4 = (ImageView) findViewById(R.id.leaf_l4);
        leafL5 = (ImageView) findViewById(R.id.leaf_l5);
        leafR1 = (ImageView) findViewById(R.id.leaf_r1);
        leafR2 = (ImageView) findViewById(R.id.leaf_r2);
        leafR3 = (ImageView) findViewById(R.id.leaf_r3);
        leafR4 = (ImageView) findViewById(R.id.leaf_r4);
        leafR5 = (ImageView) findViewById(R.id.leaf_r5);
        cbCuttainClose = (CheckBox) findViewById(R.id.cb_cuttain_close);
        cbCuttainOpen = (CheckBox) findViewById(R.id.cb_cuttain_open);
        cbCuttainPause = (CheckBox) findViewById(R.id.cb_cuttain_pause);
        iv_device_info = (CheckBox) findViewById(R.id.cb_is_deviceinfo);
        iv_timer = (CheckBox) findViewById(R.id.cb_is_timing);
    }


    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }


    @Override
    protected void onDestroy() {

        super.onDestroy();

    }


    @Override
    public void initView() {
        super.initView();
        mContext = this;
        setContentView(R.layout.activity_device_curtain1);
        ActivityTitleManager.getInstance().init(this);
        ActivityTitleManager.getInstance().showCommonTitle();
        ActivityTitleManager.getInstance().changeTitle("窗帘");
        assignViews();

    }


    @Override
    public void initData() {
        super.initData();
        Intent intent = getIntent();
        //
        mName = intent.getStringExtra("name");
        //
        mId = intent.getStringExtra("id");
//        mDevice = DataUtils.getInstance().getDevice(mId);//数据库获取基本数据
        //网络获取实时数据
        CmdBaseActivity.getInstance().mHelper.setCommandListener(CurtainActivity1.this);
        CMD06_QueryDeviceStatus cmd06 = new CMD06_QueryDeviceStatus(mId);
        CmdBaseActivity.getInstance().sendCmd(cmd06);


        mWidth = UIUtils.getScreenWidth(mContext);
        mHight = UIUtils.getScreenHeight(mContext);
        myview_lete.setVLeft(mWidth);
        myview_lete.setVRight(mWidth);
        myview_lete.setVbuttom(mHight / 10 * 3);
        myview_right.setVLeft(mWidth);
        myview_right.setVRight(mWidth);
        myview_right.setVbuttom(mHight / 10 * 3);
        start();
    }


    private void start() {
        TimerTask tt = new TimerTask() {
            public void run() {
                handler.sendEmptyMessage(1);
            }
        };
        Timer timer = new Timer();
        timer.schedule(tt, 10, 20);
    }

    @Override
    public void initEvent() {
        super.initEvent();
/*
左边的left是0,right是屏幕一半 就是关上创库,靠right 来控制
 */
        myview_lete.setLeft(0);//左边
        mVl_int = 0;
        //vl_int ++到getScreenWidth/2 就是关上
        myview_lete.setRight(mVl_int);
/*
右边 right 是屏幕宽度大小,  left 是屏幕宽的一半是关上,  left 是屏幕宽是打开
 */
        mVr_int = mWidth;
        //vr_int 减少到 getScreenWidth/2 就是关闭窗帘
        myview_right.setLeft(mVr_int);//右边
        myview_right.setRight(mWidth);

        Clicklistener();
    }

    private  Boolean flag= false;
    private void Clicklistener() {

        cbCuttainClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen()){ cbCuttainClose.setChecked(false);return;}
                stint();
                mNum = 1;
                cbCuttainClose.setChecked(true);
                cbCuttainOpen.setChecked(false);
                cbCuttainPause.setChecked(false);
                /*
                1关
                0开
                 */
                flag=true;
                mDevice.getPowers().get(1).on=true;
                CMD08_ControlDevice cmd08 = new CMD08_ControlDevice(
                        mDevice);
                CmdBaseActivity.getInstance().sendCmd(cmd08);



            }
        });
        cbCuttainOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen()){cbCuttainOpen.setChecked(false); return;}
                stint();
                mNum = -1;
                flag=false;
                cbCuttainClose.setChecked(false);
                cbCuttainOpen.setChecked(true);
                cbCuttainPause.setChecked(false);
                mDevice.getPowers().get(0).on=true;
                CMD08_ControlDevice cmd08 = new CMD08_ControlDevice(
                        mDevice);
                CmdBaseActivity.getInstance().sendCmd(cmd08);
            }
        });
        cbCuttainPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen()){cbCuttainPause.setChecked(false); return;}
                stint();
                mNum = 0;
                cbCuttainClose.setChecked(false);
                cbCuttainOpen.setChecked(false);
                cbCuttainPause.setChecked(true);

                if(flag){
                    mDevice.getPowers().get(1).on=false;
                }else {
                    mDevice.getPowers().get(0).on=false;
                }
                CMD08_ControlDevice cmd08 = new CMD08_ControlDevice(
                        mDevice);
                CmdBaseActivity.getInstance().sendCmd(cmd08);
            }
        });

        //定时
        iv_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转定时界面, 不销毁当前界面
                if (isNull(mDevice)) return;
                openActivity(TimeingDeviceActivity.class, mDevice, mActivity, false);
            }
        });


        //设备信息
        iv_device_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNull(mDevice)) return;
                openActivity(DeviceInfoActivity.class, mDevice, mActivity, false);
            }
        });


    }

    @Override
    public void initListener() {
        super.initListener();

        ActivityTitleManager.getInstance().goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.finish();
            }
        });
    }

    private void stint() {//限制
        if (mVl_int < 0) {
            mVl_int = 0;
        }
        if (mVl_int >= mWidth / 2) {
            mVl_int = mWidth / 2 - 1;
        }
        if (mVr_int > mWidth) {
            mVr_int = mWidth;
        }
        if (mVr_int <= mWidth / 2) {
            mVr_int = mWidth / 2 - 1;
        }
    }


    @Override
    public void onReceiveCommand(ServerCommand cmd) {


        switch (cmd.CMDByte) {

            case CMDFF_ServerException.Command:

                CMDFF_ServerException cmdff_serverException = (CMDFF_ServerException) cmd;
                final String info = cmdff_serverException.info;
                UIUtils.postTaskSafely(new Runnable() {
                    @Override
                    public void run() {


                    }
                });
                break;
            case CMD07_ServerRespondDeviceStatus.Command:
                CMD07_ServerRespondDeviceStatus cmd07 = (CMD07_ServerRespondDeviceStatus) cmd;
                Device d07 = cmd07.status;
                mDevice = d07;

                break;
            case CMD09_ServerControlResult.Command:
                CMD09_ServerControlResult cmd09 = (CMD09_ServerControlResult) cmd;
                Device device = cmd09.status;

//                mLightAdapter.notifyDataSetChanged();
                break;
        }
    }

    private boolean isOpen() {
        if (!mDevice.online) {
            Util.showToast(mContext, "空调为关，请打开空调！");
            return true;
        }
        return false;
    }


}
