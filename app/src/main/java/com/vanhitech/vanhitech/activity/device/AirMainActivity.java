package com.vanhitech.vanhitech.activity.device;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.animation.BounceEnter.BounceBottomEnter;
import com.flyco.animation.SlideExit.SlideBottomExit;
import com.flyco.dialog.widget.popup.base.BasePopup;
import com.vanhitech.protocol.cmd.ClientCommand;
import com.vanhitech.protocol.cmd.ServerCommand;
import com.vanhitech.protocol.cmd.client.CMD06_QueryDeviceStatus;
import com.vanhitech.protocol.cmd.client.CMD08_ControlDevice;
import com.vanhitech.protocol.cmd.server.CMD07_ServerRespondDeviceStatus;
import com.vanhitech.protocol.cmd.server.CMD09_ServerControlResult;
import com.vanhitech.protocol.cmd.server.CMDFC_ServerNotifiOnline;
import com.vanhitech.protocol.cmd.server.CMDFF_ServerException;
import com.vanhitech.protocol.object.device.AirType5Device;
import com.vanhitech.protocol.object.device.AirTypeADevice;
import com.vanhitech.protocol.object.device.Device;
import com.vanhitech.protocol.object.device.TranDevice;
import com.vanhitech.vanhitech.R;
import com.vanhitech.vanhitech.base.BaseActivityController;
import com.vanhitech.vanhitech.base.CmdBaseActivity;
import com.vanhitech.vanhitech.manager.ActivityTitleManager;
import com.vanhitech.vanhitech.utils.LogUtils;
import com.vanhitech.vanhitech.utils.T;
import com.vanhitech.vanhitech.utils.UIUtils;
import com.vanhitech.vanhitech.utils.Util;
import com.vanhitech.vanhitech.views.LightRgbTagView;
import com.vanhitech.vanhitech.views.RotatView;

/**
 * 创建者     heyn
 * 创建时间   2016/3/18 14:42
 * 描述	      ${TODO}
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class AirMainActivity extends BaseActivityController implements View.OnClickListener,
        RotatView.OnDegreeChangeListener {
    private TextView setTemp, innerTemp, tv_C, tv_save, tv_room;
    private ImageView img_return,onB;
    private CheckBox iv_timer, iv_device_info;
    private Button iv_air_model, iv_air_windspeed, iv_air_move_wind,
            iv_air_pair,iv_air_data;
    private RotatView rotateView;
    public LightRgbTagView mLrt_tag;
    private Context mContext;
    /*
    如果在执行某个事件,再次点击无效果
     */

    private ModeCustomPop mModeCustomPop;
    private windspeedCustomPop mWindspeedCustomPop;
    private movewindCustomPop mMovewindCustomPop;
    private Device mDevice;
    private Activity mActivity;
    private boolean tran_flag = false;
    private boolean air_A_flag = false;
    private boolean air_5_flag = false;
    private TranDevice mTranDevice;
    private AirType5Device airType5Device;
    private AirTypeADevice airtypeADevice;
    private int left;
    private int updown;
    private int mode;// 模式
    private int on;// 开关
    private boolean isOn = false;
    private int old_temp;
    private int setTempN;// 设置温度
    private int innerTempN;// 室内温度
    private int wind;
    int checkMoveWind = 0;
    private String mName;
    private String mId;
    private boolean isRotateviewMove;
    private Handler mhandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    // rotateView.isOpen = false;
                    // rotateView.isOpen = true;
                    break;
                case 2:
                    // isRotateviewMove = false;
                    CMD06_QueryDeviceStatus cmd06 = new CMD06_QueryDeviceStatus(mId);
                    sendCMD(cmd06);

                    if (mDevice != null) {
                        judgeType();

                    }
                default:
                    break;
            }
            return false;
        }
    });


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
        setContentView(R.layout.activity_airmain);
        ActivityTitleManager.getInstance().init(this);
        ActivityTitleManager.getInstance().showCommonTitle();
        ActivityTitleManager.getInstance().changeTitle("空调功率插头");
        mModeCustomPop = new ModeCustomPop(mContext);
        mWindspeedCustomPop = new windspeedCustomPop(mContext);
        mMovewindCustomPop = new movewindCustomPop(mContext);
        mActivity = this;
        findView();
    }

    private void findView() {
        iv_air_data = (Button) findViewById(R.id.base_ib_left);
        iv_air_data.setText("数据");
        iv_timer = (CheckBox) findViewById(R.id.iv_timer);//定时
        iv_device_info = (CheckBox) findViewById(R.id.iv_device_info);//信息
        onB = (ImageView) findViewById(R.id.onB);//开关
        iv_air_model = (Button) findViewById(R.id.iv_air_model);//模式
        iv_air_windspeed = (Button) findViewById(R.id.iv_air_windspeed);//风速
        iv_air_move_wind = (Button) findViewById(R.id.iv_air_move_wind);//排风
        iv_air_pair = (Button) findViewById(R.id.iv_air_pair);
        setTemp = (TextView) findViewById(R.id.setT);
        rotateView = (RotatView) findViewById(R.id.setC);
        innerTemp = (TextView) findViewById(R.id.tv_indoor_tp);
        tv_C = (TextView) findViewById(R.id.tv_C);
        rotateView.isON = true;
        rotateView.setDegreeChangeListener(this);
        rotateView.setMinValue(0);
        rotateView.setMaxValue(16);

    }


    @Override
    public void initData() {
        super.initData();
//        mDevice;

        Intent intent = getIntent();
        mId = intent.getStringExtra("id");
        mName = intent.getStringExtra("name");
        //


        CMD06_QueryDeviceStatus cmd06 = new CMD06_QueryDeviceStatus(mId);
        sendCMD(cmd06);
    }

    private void sendCMD(ClientCommand cmd) {
        CmdBaseActivity.getInstance().mHelper.setCommandListener(AirMainActivity.this);
        CmdBaseActivity.getInstance().sendCmd(cmd);
    }


    private void judgeType() {
        if (mDevice.type == 0x0A) {
            airtypeADevice = (AirTypeADevice) mDevice;
            air_A_flag = true;
            air_5_flag = false;
        } else if (mDevice.type == 0x05) {
            airType5Device = (AirType5Device) mDevice;
            air_5_flag = true;
            air_A_flag = false;
        }
        if (air_5_flag) {
            refreshView_5_A();
        } else if (air_A_flag) {
            refreshView_5_A();
        }
        old_temp = setTempN - 16;
    }

    private void refreshView_5_A() {
//        if (isRotateviewMove) {
//            mhandler.sendEmptyMessageDelayed(2, 3 * 1000);
//            return;
//        }
        if (mDevice == null) {
            return;
        }
//        if (air_5_flag) {
//            isOn = airType5Device.isPower();
//            mode = airType5Device.mode;
//            setTempN = airType5Device.temp + 16;
//            wind = airType5Device.speed;
//            innerTempN = airType5Device.roomtemp;
//            System.out.println("airType5Device.adirect"
//                    + airType5Device.adirect + "," + "airType5Device.mdirect"
//                    + airType5Device.mdirect);
//            if (airType5Device.adirect == 0 && airType5Device.mdirect == 0) {
//                checkMoveWind = 0;
//            } else if (airType5Device.adirect != 0
//                    && airType5Device.mdirect == 0) {
//                checkMoveWind = 1;
//                left = airType5Device.adirect;
//            } else if (airType5Device.adirect == 0
//                    && airType5Device.mdirect != 0) {
//                checkMoveWind = 2;
//                updown = airType5Device.mdirect;
//            }
//
//        } else
        if (air_A_flag) {
            isOn = airtypeADevice.isPower();
            mode = airtypeADevice.mode;
            setTempN = airtypeADevice.temp + 16;
            wind = airtypeADevice.speed;
            innerTempN = airtypeADevice.roomtemp;
            if (airtypeADevice.adirect == 0 && airtypeADevice.mdirect == 0) {
                checkMoveWind = 0;
            } else if (airtypeADevice.adirect != 0
                    && airtypeADevice.mdirect == 0) {
                checkMoveWind = 1;
                left = airtypeADevice.adirect;
            } else {
                checkMoveWind = 2;
                updown = airtypeADevice.mdirect;
            }

        }
        String temp = setTempN + "";
        setTemp.setText(temp);

        if (mDevice.online) {// 在线或者局域网，数据都要显示
            if (isOn) {
                //setSelected
                onB.setSelected(true);
            } else {
                onB.setSelected(false);
            }
            innerTemp.setVisibility(View.VISIBLE);
            if (isOn) {
                innerTemp.setText("空调开   室内温度" + innerTempN + "℃");
            } else {
                innerTemp.setText("空调关   室内温度" + innerTempN + "℃");
            }

        } else {
            isOn = false;
            onB.setSelected(false);
            innerTemp.setVisibility(View.VISIBLE);
            innerTemp.setText("空调不在线    室内温度 N/A");
        }

        rotateView.setCurrentValue(setTempN - 16);
//        modeIsflaseclick.switchNum(mode);
//        menuWindow_wind_speed.switchNum(wind);
//        menuWindow_move_wind.switchNum(checkMoveWind);
        System.out.println("checkMoveWind" + checkMoveWind);

        setMode(mode);
        setWindSpeed(wind);
        setMoveWind(checkMoveWind);
    }

    @Override
    public void initEvent() {
        super.initEvent();
//        clickListener();
    }

    public void initListener() {
        super.initListener();

            ActivityTitleManager.getInstance().goback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.finish();
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
        //开关
        onB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNull(mDevice)) return;
//                airType5Device.setPower(!airType5Device.isPower());
//                if (air_5_flag) {
//                    if (CmdBaseActivity.getInstance().isConnected()) {
//                        airType5Device.setPower(!airType5Device.isPower());
//                        CMD08_ControlDevice cmd08 = new CMD08_ControlDevice(
//                                airType5Device);
//                        CmdBaseActivity.getInstance().sendCmd(cmd08);
//                    } else {
//                        Util.showToast(mContext, "网络异常,请检查网络连接!");
//                    }
//                } else if (air_A_flag) {
                    if (CmdBaseActivity.getInstance().isConnected()) {
                        airtypeADevice.setPower(!airtypeADevice.isPower());
                        CMD08_ControlDevice cmd08 = new CMD08_ControlDevice(
                                airtypeADevice);
                        CmdBaseActivity.getInstance().sendCmd(cmd08);
                    } else {
                        Util.showToast(mContext, "网络异常,请检查网络连接!");
                    }
//                }
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


        iv_air_model.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mModeCustomPop.modeIsflaseclick(mode);

                mModeCustomPop
                        .anchorView(iv_air_model)
                        .offset(-24, 0)
                        .gravity(Gravity.TOP)
                        .showAnim(new BounceBottomEnter())
                        .dismissAnim(new SlideBottomExit())
                        .dimEnabled(false)
                        .show();
            }
        });

        iv_air_windspeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                openActivity(testActivity.class);
//                windspeedIsflaseclick
                mWindspeedCustomPop.windspeedIsflaseclick(wind);
                mWindspeedCustomPop
                        .anchorView(iv_air_windspeed)
                        .offset(-22, 0)
                        .gravity(Gravity.TOP)
                        .showAnim(new BounceBottomEnter())
                        .dismissAnim(new SlideBottomExit())
                        .dimEnabled(false)//背景是否覆盖一层
                        .show();
            }
        });
        iv_air_move_wind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMovewindCustomPop.movewindIsflaseclick(checkMoveWind);
                mMovewindCustomPop.anchorView(iv_air_move_wind)
                        .offset(-22, 0)
                        .gravity(Gravity.TOP)
                        .showAnim(new BounceBottomEnter())
                        .dismissAnim(new SlideBottomExit())
                        .dimEnabled(false)
                        .show();
            }
        });


        iv_air_pair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(AirPairActivity.class, mDevice, mActivity, false);
            }
        });
        iv_air_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(AirDataSelectionActivity.class, mDevice, mActivity, false);
            }
        });
    }


    private void popwindow() {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void degreeChange(float currentDegree, boolean isUp) {
        if (isUp) {
            if (!isOn || !mDevice.online) {
                Util.showToast(mContext, "空调为关闭状态，请打开空调!");
                rotateView.setCurrentValue(old_temp);
                setTemp.setText(old_temp + 16 + "");
                return;
            }
        }
        setTempN = (rotateView.getCurrentValue() + 16);
        setTemp.setText("" + setTempN);
        if (isUp) { // 发送控制命令
            if (air_5_flag) {
                if (airType5Device == null) {
                    return;
                }
                if (mode == 0) {
                    Util.showToast(mContext, "自动模式不能调节温度!");
                }
                if (CmdBaseActivity.getInstance().isConnected()) {
                    airType5Device.temp = rotateView.getCurrentValue();
                    CMD08_ControlDevice cmd08 = new CMD08_ControlDevice(
                            airType5Device);
                    CmdBaseActivity.getInstance().sendCmd(cmd08);
                } else {
                    Util.showToast(mContext, "网络异常,请检查网络连接!");
                }

            } else if (air_A_flag) {
                if (airtypeADevice == null) {
                    return;
                }
                if (mode == 0) {
                    Util.showToast(mContext, "自动模式不能调节温度!");
                }
                if (CmdBaseActivity.getInstance().isConnected()) {
                    airtypeADevice.temp = rotateView.getCurrentValue();
                    CMD08_ControlDevice cmd08 = new CMD08_ControlDevice(
                            airtypeADevice);
                    CmdBaseActivity.getInstance().sendCmd(cmd08);
                } else {
                    Util.showToast(mContext, "网络异常,请检查网络连接!");
                }
            }
        }
    }


    //模式按键的ppwind
    private class ModeCustomPop extends BasePopup<ModeCustomPop> {

        private LinearLayout mPopLayout;
        private Button mIvAirAuto;
        private ImageView mIvAirMakeCold;
        private ImageView mIvAirMakeHot;
        private ImageView mIvAirSendWind;
        private ImageView mIvAirRemoveWet;

        public ModeCustomPop(Context context) {
            super(context);
        }


        @Override
        public View onCreatePopupView() {

            View inflate = View.inflate(mContext, R.layout.pop_dialog_air_mode, null);
            mIvAirAuto = (Button) inflate.findViewById(R.id.iv_air_auto);
            mIvAirMakeCold = (ImageView) inflate.findViewById(R.id.iv_air_make_cold);
            mIvAirMakeHot = (ImageView) inflate.findViewById(R.id.iv_air_make_hot);
            mIvAirSendWind = (ImageView) inflate.findViewById(R.id.iv_air_send_wind);
            mIvAirRemoveWet = (ImageView) inflate.findViewById(R.id.iv_air_remove_wet);
//            mIvAirAuto.setSelected(true);
//            mIvAirMakeCold.setSelected(true);


            return inflate;
        }

        @Override
        public void setUiBeforShow() {


            mIvAirAuto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {//自动
                    if (isOpen()) return;

                    if (isNull(airtypeADevice)) return;

                    if (CmdBaseActivity.getInstance().isConnected()) {
                        airtypeADevice.mode = 0;
                        CMD08_ControlDevice cmd08 = new CMD08_ControlDevice(
                                airtypeADevice);
                        CmdBaseActivity.getInstance().sendCmd(cmd08);
                        modeIsflaseclick(0);
                        setMode(0);
                    } else {
                        T.showShort(mContext, "网络异常,请检查网络连接");
                    }
                    mModeCustomPop.dismiss();
                }
            });
            mIvAirMakeCold.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {//冷风
                    if (isOpen()) return;
                    if (isNull(airtypeADevice)) return;

                    if (CmdBaseActivity.getInstance().isConnected()) {
                        airtypeADevice.mode = 1;
                        CMD08_ControlDevice cmd08 = new CMD08_ControlDevice(
                                airtypeADevice);
                        CmdBaseActivity.getInstance().sendCmd(cmd08);

                        //设置选中后效果
                        modeIsflaseclick(1);
                        setMode(1);
                    } else {
                        T.showShort(mContext, "网络异常,请检查网络连接!");
                    }

                    mModeCustomPop.dismiss();

                }
            });
            mIvAirMakeHot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {//热风
                    if (isOpen()) return;
                    if (isNull(airtypeADevice)) return;
                    if (CmdBaseActivity.getInstance().isConnected()) {
                        airtypeADevice.mode = 4;
                        CMD08_ControlDevice cmd08 = new CMD08_ControlDevice(
                                airtypeADevice);
                        CmdBaseActivity.getInstance().sendCmd(cmd08);

                        //设置选中后效果
                        modeIsflaseclick(2);
                        setMode(2);
                    } else {
                        T.showShort(mContext, "网络异常,请检查网络连接!");
                    }

                    mModeCustomPop.dismiss();
                }
            });
            mIvAirSendWind.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {//送风
                    if (isNull(airtypeADevice)) return;
                    if (isOpen()) return;
                    if (CmdBaseActivity.getInstance().isConnected()) {
                        airtypeADevice.mode = 3;
                        CMD08_ControlDevice cmd08 = new CMD08_ControlDevice(
                                airtypeADevice);
                        CmdBaseActivity.getInstance().sendCmd(cmd08);

                        //设置选中后效果
                        modeIsflaseclick(3);
                        setMode(3);
                    } else {
                        T.showShort(mContext, "网络异常,请检查网络连接!");
                    }

                    mModeCustomPop.dismiss();

                }
            });
            //ivAirRemoveWet
            mIvAirRemoveWet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {//除湿
                    if (isNull(airtypeADevice)) return;
                    if (isOpen()) return;
                    if (CmdBaseActivity.getInstance().isConnected()) {
                        airtypeADevice.mode = 2;
                        CMD08_ControlDevice cmd08 = new CMD08_ControlDevice(
                                airtypeADevice);
                        CmdBaseActivity.getInstance().sendCmd(cmd08);

                        //设置选中后效果
                        modeIsflaseclick(4);
                        setMode(4);

                    } else {
                        T.showShort(mContext, "网络异常,请检查网络连接!");
                    }

                    mModeCustomPop.dismiss();
                }
            });


        }

        public void modeIsflaseclick(int num) {
            mIvAirAuto.setSelected(num == 0);
            mIvAirMakeCold.setSelected(num == 1);
            mIvAirMakeHot.setSelected(num == 2);
            mIvAirSendWind.setSelected(num == 3);
            mIvAirRemoveWet.setSelected(num == 4);
        }


    }

    private boolean isOpen() {
        if (!isOn) {
            Util.showToast(mContext, "空调为关，请打开空调！");
            return true;
        }
        return false;
    }

    public boolean isNull(Object airtypeADevice) {
        if (airtypeADevice == null) {
            T.showLong(mContext, "空");
            return true;
        }
        return false;
    }


    //模式按键的ppwind  iv_air_windspeed
    private class windspeedCustomPop extends BasePopup<windspeedCustomPop> {

        private LinearLayout mPopLayout;
        private ImageView mIvAirWindSpeedHigh;
        private ImageView mIvAirWindSpeedMiddle;
        private ImageView mIvAirWindSpeedLow;
        private ImageView mIvAirWindSpeedAuto;


        public void windspeedIsflaseclick(int num) {
            mIvAirWindSpeedHigh.setSelected(num == 0);
            mIvAirWindSpeedMiddle.setSelected(num == 1);
            mIvAirWindSpeedLow.setSelected(num == 2);
            mIvAirWindSpeedAuto.setSelected(num == 3);

        }

        public windspeedCustomPop(Context context) {
            super(context);
        }


        @Override
        public View onCreatePopupView() {

            View inflate = View.inflate(mContext, R.layout.pop_dialog_air_wind_speed, null);
            mIvAirWindSpeedHigh = (ImageView) inflate.findViewById(R.id.iv_air_wind_speed_high);
            mIvAirWindSpeedMiddle = (ImageView) inflate.findViewById(R.id.iv_air_wind_speed_middle);
            mIvAirWindSpeedLow = (ImageView) inflate.findViewById(R.id.iv_air_wind_speed_low);
            mIvAirWindSpeedAuto = (ImageView) inflate.findViewById(R.id.iv_air_wind_speed_auto);

            return inflate;
        }


        @Override
        public void setUiBeforShow() {
            mIvAirWindSpeedHigh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isNull(airtypeADevice)) return;
                    if (isOpen()) return;
                    if (CmdBaseActivity.getInstance().isConnected()) {
                        airtypeADevice.speed = 3;
                        CMD08_ControlDevice cmd08 = new CMD08_ControlDevice(
                                airtypeADevice);
                        CmdBaseActivity.getInstance().sendCmd(cmd08);

                        //设置选中后效果
                        windspeedIsflaseclick(0);
                        setWindSpeed(0);
                    } else {
                        T.showShort(mContext, "网络异常,请检查网络连接!");
                    }
                    mWindspeedCustomPop.dismiss();


                }
            });
            mIvAirWindSpeedMiddle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isNull(airtypeADevice)) return;
                    if (isOpen()) return;
                    if (CmdBaseActivity.getInstance().isConnected()) {
                        airtypeADevice.speed = 2;
                        CMD08_ControlDevice cmd08 = new CMD08_ControlDevice(
                                airtypeADevice);
                        CmdBaseActivity.getInstance().sendCmd(cmd08);
                        //设置选中后效果
                        windspeedIsflaseclick(1);
                        setWindSpeed(1);
                    } else {
                        T.showShort(mContext, "网络异常,请检查网络连接!");
                    }
                    mWindspeedCustomPop.dismiss();

                }
            });
            mIvAirWindSpeedLow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isNull(airtypeADevice)) return;
                    if (isOpen()) return;

                    if (CmdBaseActivity.getInstance().isConnected()) {
                        airtypeADevice.speed = 1;
                        CMD08_ControlDevice cmd08 = new CMD08_ControlDevice(
                                airtypeADevice);
                        CmdBaseActivity.getInstance().sendCmd(cmd08);
                        //设置选中后效果
                        setWindSpeed(2);
                        windspeedIsflaseclick(2);
                    } else {
                        T.showShort(mContext, "网络异常,请检查网络连接!");
                    }
                    mWindspeedCustomPop.dismiss();
                }
            });
            mIvAirWindSpeedAuto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isNull(airtypeADevice)) return;
                    if (isOpen()) return;

                    if (CmdBaseActivity.getInstance().isConnected()) {
                        airtypeADevice.speed = 0;
                        CMD08_ControlDevice cmd08 = new CMD08_ControlDevice(
                                airtypeADevice);
                        CmdBaseActivity.getInstance().sendCmd(cmd08);
                        //设置选中后效果
                        setWindSpeed(3);
                        windspeedIsflaseclick(3);
                    } else {
                        T.showShort(mContext, "网络异常,请检查网络连接!");
                    }
                    mWindspeedCustomPop.dismiss();
                }
            });

            //windspeedCustomPop


        }
    }

    //iv_air_move_wind  move_wind 排风
    private class movewindCustomPop extends BasePopup<movewindCustomPop> {

        private LinearLayout mPopLayout;
        private ImageView mIvAirMoveWindLeftRight;
        private ImageView mIvAirMoveWindUpDown;

        public void movewindIsflaseclick(int num) {
            mIvAirMoveWindLeftRight.setSelected(num == 0);
            mIvAirMoveWindUpDown.setSelected(num == 1);
        }

        public movewindCustomPop(Context context) {
            super(context);
        }


        @Override
        public View onCreatePopupView() {

            View inflate = View.inflate(mContext, R.layout.pop_dialog_air_move_wind, null);
            mIvAirMoveWindLeftRight = (ImageView) inflate.findViewById(R.id.iv_air_move_wind_left_right);
            mIvAirMoveWindUpDown = (ImageView) inflate.findViewById(R.id.iv_air_move_wind_up_down);
            return inflate;
        }


        @Override
        public void setUiBeforShow() {
            mIvAirMoveWindLeftRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isOpen()) return;
                    if (isNull(airtypeADevice)) return;
                    left++;
                    if (left >= 9) {
                        left = 0;
                    }
                    if (CmdBaseActivity.getInstance().isConnected()) {
                        airtypeADevice.adirect = left;
                        airtypeADevice.mdirect = 0;
                        CMD08_ControlDevice cmd08 = new CMD08_ControlDevice(
                                airtypeADevice);
                        CmdBaseActivity.getInstance().sendCmd(cmd08);
                        setMoveWind(0);
                        movewindIsflaseclick(0);
                    } else {
                        T.showShort(mContext, "网络异常,请检查网络连接!");
                    }
                    mMovewindCustomPop.dismiss();
                }
            });
            mIvAirMoveWindUpDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isOpen()) return;
                    if (isNull(airtypeADevice)) return;
                    updown++;
                    if (updown >= 9) {
                        updown = 0;
                    }
                    if (CmdBaseActivity.getInstance().isConnected()) {
                        airtypeADevice.mdirect = updown;
                        airtypeADevice.adirect = 0;
                        CMD08_ControlDevice cmd08 = new CMD08_ControlDevice(
                                airtypeADevice);
                        CmdBaseActivity.getInstance().sendCmd(cmd08);
                        setMoveWind(1);
                        movewindIsflaseclick(1);
                    } else {
                        T.showShort(mContext, "网络异常,请检查网络连接!");
                    }

                    mMovewindCustomPop.dismiss();
                }
            });

            //movewindCustomPop
        }


    }

    private void buttontrueClickable(ImageView iv) {
        iv.setClickable(true);
    }

    private void buttonfalseClickable(ImageView iv) {
        iv.setClickable(false);
    }

    private void setMoveWind(int checkMoveWind2) {
        // TODO Auto-generated method stub
        if (checkMoveWind2 == 0) {
            iv_air_move_wind.setText("自动");
        } else if (checkMoveWind2 == 1) {
            iv_air_move_wind.setText("手动");
        } else {
            iv_air_move_wind.setText("摆风");
        }
    }

    private void setWindSpeed(int wind2) {
        // TODO Auto-generated method stub
        if (wind2 == 0) {
            iv_air_windspeed.setText("高");
        } else if (wind2 == 1) {
            iv_air_windspeed.setText("中");
        } else if (wind2 == 2) {
            iv_air_windspeed.setText("低");
        } else if (wind2 == 3) {
            iv_air_windspeed.setText("自动");
        } else {
            iv_air_windspeed.setText("风速");
        }

    }

    private void setMode(int mode2) {
        // TODO Auto-generated method stub
        if (mode2 == 0) {
            iv_air_model.setText("自动");
        } else if (mode2 == 1) {
            iv_air_model.setText("制冷");
        } else if (mode2 == 2) {
            iv_air_model.setText("制热");
        } else if (mode2 == 3) {
            iv_air_model.setText("送风");
        } else if (mode2 == 4) {
            iv_air_model.setText("除湿");
        } else {
            iv_air_model.setText("模式");
        }
    }

    public void refreshView() {
        if (isRotateviewMove) {
            mhandler.sendEmptyMessageDelayed(2, 3 * 1000);
            return;
        }

    }


    @Override
    public void onReceiveCommand(ServerCommand cmd) {
        super.onReceiveCommand(cmd);
        LogUtils.i("空调功率插头+遥控器收到" + cmd.toString());

        switch (cmd.CMDByte) {


            case CMDFC_ServerNotifiOnline.Command:

                sendCMD(new CMD06_QueryDeviceStatus(mId));
                break;

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

//                mTranDevice = (TranDevice) mDevice;
                UIUtils.postTaskSafely(new Runnable() {
                    @Override
                    public void run() {
                        judgeType();
                    }
                });
                break;
            case CMD09_ServerControlResult.Command:
                CMD09_ServerControlResult cmd09 = (CMD09_ServerControlResult) cmd;
                Device device = cmd09.status;
                mDevice.power=device.power;
//                mDevice.
                CMD06_QueryDeviceStatus cmd06 = new CMD06_QueryDeviceStatus(mId);
                sendCMD(cmd06);



                refreshView();
//                mLightAdapter.notifyDataSetChanged();
                break;
        }
    }

}
