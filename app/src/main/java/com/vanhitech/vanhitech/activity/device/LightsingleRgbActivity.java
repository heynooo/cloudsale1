package com.vanhitech.vanhitech.activity.device;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.vanhitech.protocol.cmd.ServerCommand;
import com.vanhitech.protocol.cmd.client.CMD06_QueryDeviceStatus;
import com.vanhitech.protocol.cmd.client.CMD08_ControlDevice;
import com.vanhitech.protocol.cmd.client.CMD24_QueryTimer;
import com.vanhitech.protocol.cmd.client.CMD60_SetMatchStatus;
import com.vanhitech.protocol.cmd.server.CMD07_ServerRespondDeviceStatus;
import com.vanhitech.protocol.cmd.server.CMD09_ServerControlResult;
import com.vanhitech.protocol.cmd.server.CMDFF_ServerException;
import com.vanhitech.protocol.object.device.Device;
import com.vanhitech.protocol.object.device.LightRGBDevice;
import com.vanhitech.vanhitech.R;
import com.vanhitech.vanhitech.base.BaseActivityController;
import com.vanhitech.vanhitech.base.CmdBaseActivity;
import com.vanhitech.vanhitech.base.MyApplication;
import com.vanhitech.vanhitech.conf.Constants;
import com.vanhitech.vanhitech.manager.ActivityTitleManager;
import com.vanhitech.vanhitech.utils.AnimationController;
import com.vanhitech.vanhitech.utils.DataUtils;
import com.vanhitech.vanhitech.utils.LogUtils;
import com.vanhitech.vanhitech.utils.StringUtils;
import com.vanhitech.vanhitech.utils.T;
import com.vanhitech.vanhitech.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 创建者     heyn
 * 创建时间   2016/3/18 10:52
 * 描述	      ${TODO}
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class LightsingleRgbActivity extends BaseActivityController {

    private SeekBar mLightlimitBar, mLightPaceBar;
    String mName;
    int mRed;
    int mGreen;
    int mBlue;
    private DbUtils mDb;
    private Button buttonModel;
    private CheckBox mLightSwitch, cbIsTiming, mCbIsRemoteControl,mCbIsDeviceinfo;
    public Activity mActivity;


    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    mLightlimitBar.setProgress(mLightlimitBar.getProgress() + 5);
                    mLightPaceBar.setProgress(mLightPaceBar.getProgress() + 10);
                    break;
            }
        }
    };
    private ColorPicker mPicker;
    private int mMcolor;
    private String mBrightness2;
    private String mMode;
    private LightRGBDevice mLightRGBDevice;
    private ImageView mImgRgb;
    private String mFreq;
    private com.vanhitech.vanhitech.bean.Power mPower;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }


    public void initListener() {
        super.initListener();

        ActivityTitleManager.getInstance().goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.finish();
            }
        });
    }

    @Override
    public void initView() {
        super.initView();//
        setContentView(R.layout.activity_device_singlergblight);
        ActivityTitleManager.getInstance().init(this);
        ActivityTitleManager.getInstance().showCommonTitle();
        ActivityTitleManager.getInstance().changeTitle("纯RGB");

        mActivity=this;
        mContext = LightsingleRgbActivity.this;

        Intent intent = getIntent();
        mName = intent.getStringExtra("name");   //这里其实拿到的是id


        sendCmd06(mName);

        //灯亮度
        mLightlimitBar = (SeekBar) findViewById(R.id.LightLimitBar);

        //模式速度
        mLightPaceBar = (SeekBar) findViewById(R.id.LightPaceBar);

        mPicker = (ColorPicker) findViewById(R.id.picker);
        //开关
        mLightSwitch = (CheckBox) findViewById(R.id.cb_is_switch);
        //定时
        cbIsTiming = (CheckBox) findViewById(R.id.cb_is_timing);
        //模式
        buttonModel = (Button) findViewById(R.id.iv_model);
        //匹配
        mCbIsRemoteControl = (CheckBox) findViewById(R.id.cb_is_remote_control);
        //信息
        mCbIsDeviceinfo = (CheckBox) findViewById(R.id.cb_is_deviceinfo);
        //rbg盘
        mImgRgb = (ImageView) findViewById(R.id.image_rgb);

        OnClickListener();


    }


    private void sendCmd06(String id) {
        CmdBaseActivity.getInstance().mHelper.setCommandListener(LightsingleRgbActivity.this);
        CMD06_QueryDeviceStatus cmd06 = new CMD06_QueryDeviceStatus(id);
        CmdBaseActivity.getInstance().sendCmd(cmd06);
    }

    public void initData() {
        super.initData();
        mLightRGBDevice = DataUtils.getInstance().getdataRGBDevice(mName);

        if (mLightRGBDevice.name != null) {
            ActivityTitleManager.getInstance().changeTitle(mLightRGBDevice.name);
        }
        buttonModel.setText("模式" + mLightRGBDevice.mode);
        getPowers(mName, mLightRGBDevice);
        if(!mLightRGBDevice.getPowers().get(0).on)
        buttonModel.setSelected(true);
    }

    private void getPowers(String name, LightRGBDevice lightRGBDevice) {
        com.vanhitech.vanhitech.bean.Power power = DataUtils.getInstance().getdataPower(name);
        List<com.vanhitech.protocol.object.Power> lisp = new ArrayList<>();
        if (power.power0 != null && power.power1 != null) {
            lisp.add(0, new com.vanhitech.protocol.object.Power(0, StringtoBoolea(power.power0)));
            lisp.add(1, new com.vanhitech.protocol.object.Power(1, StringtoBoolea(power.power1)));
        } else if (power.power0 != null) {
            lisp.add(0, new com.vanhitech.protocol.object.Power(0, StringtoBoolea(power.power0)));
        }
        lightRGBDevice.setPowers(lisp);
    }

    private Boolean StringtoBoolea(String s) {
        if (s.equals("1")) {
            return true;
        } else {
            return false;
        }

    }

    public void setbuttonModelcB(int num) {
        buttonModel.setSelected(num == 0);
        buttonModel.setSelected(num == 1);

    }

    /**
     * 点击定时跳转/点击设备信息 处理事件
     */
    private void OnClickListener() {
        cbIsTiming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                CMD24_QueryTimer cmd24 = new CMD24_QueryTimer(mName);
                CmdBaseActivity.getInstance().sendCmd(cmd24);
                intent.setClass(mActivity, TimeingDeviceActivity.class);
                intent.putExtra("mDeviceId", mName);
                startActivity(intent);
                mActivity.overridePendingTransition(R.animator.nextenteranim, R.animator.nextexitanim);
            }
        });

        //匹配
        mCbIsRemoteControl.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CMD60_SetMatchStatus cmd60 = new CMD60_SetMatchStatus(0x80,
                        mLightRGBDevice.id);
                CmdBaseActivity.getInstance().sendCmd(cmd60);

            }
        });

        //设备信息
        mCbIsDeviceinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mContext, DeviceInfoActivity.class);
                intent.putExtra("name",mDevice.name);
                intent.putExtra("id", mDevice.id);
                startActivity(intent);
                overridePendingTransition(R.animator.nextenteranim,R.animator.nextexitanim);
            }
        });

    }

    private String mIs;
    private Boolean flag = false;

    private void isPowSwitch() {
        try {//从数据库获取开关是开还是关，然后赋值

            com.vanhitech.vanhitech.bean.Power d = mDb.findFirst(Selector.from(com.vanhitech.vanhitech.bean.Power.class).where("id", "=", mName));
            if (mName.substring(0, 2).equals(Constants.idF)) {
                mIs = d.power0;
            } else {    //substring(0, 2);
                mIs = d.power1;
            }
            if (mIs.equals("1")) {//checkbox.setChecked(true);
                mLightSwitch.setChecked(true);
                mImgRgb.setSelected(true);
                AnimationController.show(mPicker);
                //mPicker
                flag = true;
            } else {
                mLightSwitch.setChecked(false);
                mImgRgb.setSelected(false);
                AnimationController.hide(mPicker);
                flag = false;
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private Boolean mTemp;

    private Boolean isnull() {
        if (flag != null) {
            mTemp = flag;

            return mTemp;
        }
        return false;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        mDb = MyApplication.getDbUtils();

        isPowSwitch();
        isnull();

        //拿到rgb的颜色值
        mPicker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener() {
            @Override
            public void onColorChanged(int color) {
                LogUtils.i(color + "color--onColorChanged");
            }
        });

        mPicker.setOnColorSelectedListener(new ColorPicker.OnColorSelectedListener() {


            @Override
            public void onColorSelected(int color) {//用这里来发送rgb
                if(!mLightRGBDevice.getPowers().get(0).on||!mLightRGBDevice.online){//
                    int colortemp= Color.rgb(mLightRGBDevice.r, mLightRGBDevice.g, mLightRGBDevice.b) ;
                    mPicker.setColor(colortemp);
                    return;
                }
                mMcolor = color;
                rgbSwitch(color, null, null, null, mTemp);

            }


        });

        mPicker.setShowOldCenterColor(false);

        mLightSwitch.setOnClickListener(new lightSwitchClickListener());
        buttonModel.setOnClickListener(new modelickListener());


        //4 亮度控制
        mLightlimitBar.setOnSeekBarChangeListener(getOnSeekBarChangeListener());

        //6 速度 cw灯应该不带速度  应该色温度 ,代码更改字内容
        mLightPaceBar.setOnSeekBarChangeListener(new lightPaceSeekBarchangListener());
    }

    //6 速度
    class lightPaceSeekBarchangListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if (!mLightRGBDevice.getPowers().get(0).on || !mLightRGBDevice.online) {//
                mLightPaceBar.setProgress(0);
                buttonModel.setSelected(true);
                return;
            }
            int temp = (int) (seekBar.getProgress() * (0.15));
            LogUtils.i(seekBar.getProgress() + "progress的onStopTrackingTouch值大小");
            mFreq = temp + "";
            rgbSwitch(mMcolor, null, mFreq, mMode, mTemp);
        }
    }

    //4 控制
    @NonNull
    private SeekBar.OnSeekBarChangeListener getOnSeekBarChangeListener() {
        return new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (!mLightRGBDevice.getPowers().get(0).on || !mLightRGBDevice.online) {//
                    mLightlimitBar.setProgress(0);
                    buttonModel.setSelected(true);
                    return;
                }
                LogUtils.i(seekBar.getProgress() + "progress的onStopTrackingTouch值大小");
                int temp = (int) (seekBar.getProgress() * (0.15));
                mBrightness2 = temp + "";
                rgbSwitch(mMcolor, mBrightness2, mFreq, null, mTemp);
            }
        };
    }


    private class lightSwitchClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (!mLightRGBDevice.online) {
                mLightSwitch.setChecked(false);
                T.showShort(mActivity, "设备离线");
                return;
            }
            mTemp = !mTemp;
            if (mTemp) {
                buttonModel.setSelected(false);
                mLightSwitch.setChecked(true);
                mImgRgb.setSelected(true);
                /*
                 int temp = (int) (seekBar.getProgress() * (0.15));
                 */
                int pac = (int) (mLightRGBDevice.freq/(0.15));
                mLightPaceBar.setProgress(pac);//TODO
                int limit = (int) (mLightRGBDevice.brightness2/(0.15));
                mLightlimitBar.setProgress(limit);
                AnimationController.show(mPicker);
            } else {
                buttonModel.setSelected(true);
                mLightSwitch.setChecked(false);
                mImgRgb.setSelected(false);
                mLightPaceBar.setProgress(0);
                mLightlimitBar.setProgress(0);
                AnimationController.hide(mPicker);
            }
            List<com.vanhitech.protocol.object.Power> lisp = new ArrayList<>();
            lisp.add(new com.vanhitech.protocol.object.Power(0, mTemp));

            mLightRGBDevice.setPowers(lisp);

            rgbSwitch(mMcolor, mBrightness2, null, mMode, mTemp);
        }
    }
    private int stringToInt(String s) {

        return Integer.parseInt(s);
    }

    //模式
    private class modelickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (!mLightRGBDevice.getPowers().get(0).on || !mLightRGBDevice.online) {//
                setbuttonModelcB(1);
                return;
            }

            mLightRGBDevice.mode = mLightRGBDevice.mode + 1;
            if (mLightRGBDevice.mode > 15) {
                mLightRGBDevice.mode = 0;
            }
            mMode = mLightRGBDevice.mode + "";
            buttonModel.setText("模式" + mMode);
            rgbSwitch(mMcolor, mBrightness2, null, mMode, mTemp);
        }
    }

    //freq 少了模式速度
    private void rgbSwitch(int color, String brightness2, String freq, String mode, Boolean flag) {
        LogUtils.i(color + "color--onColorSelected");
        if (!StringUtils.isEmpty(color + "")) {
            mRed = (color >> 16) & 0xff;
            mGreen = (color >> 8) & 0xff;
            mBlue = (color >> 0) & 0xff;
        }


        List<com.vanhitech.protocol.object.Power> powers = new ArrayList<com.vanhitech.protocol.object.Power>();
        com.vanhitech.protocol.object.Power p1 = new com.vanhitech.protocol.object.Power(0, flag);
        powers.add(p1);
        mLightRGBDevice.setPowers(powers);
        mLightRGBDevice.r = mRed;
        mLightRGBDevice.g = mGreen;
        mLightRGBDevice.b = mBlue;

        if (!StringUtils.isEmpty(brightness2))
            mLightRGBDevice.brightness2 = Integer.parseInt(brightness2);
        if (!StringUtils.isEmpty(freq))
            mLightRGBDevice.freq = Integer.parseInt(freq);
        if (!StringUtils.isEmpty(mode))
            mLightRGBDevice.mode = Integer.parseInt(mode);
        CMD08_ControlDevice cmd08 = new CMD08_ControlDevice(mLightRGBDevice);
        CmdBaseActivity.getInstance().sendCmd(cmd08);

    }

    private void start() {
        TimerTask tt = new TimerTask() {
            public void run() {
                handler.sendEmptyMessage(1);
            }
        };
        Timer timer = new Timer();
        timer.schedule(tt, 1000, 2000);
    }


    @Override
    public void onReceiveCommand(ServerCommand cmd) {
        super.onReceiveCommand(cmd);

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
                if (d07.id.equals(mName)) {
                    mDevice = d07;
                    mLightRGBDevice = (LightRGBDevice) mDevice;
                }
                break;
            case CMD09_ServerControlResult.Command:
                CMD09_ServerControlResult cmd09 = (CMD09_ServerControlResult) cmd;
                Device device = cmd09.status;
                sendCmd06(mName);

                break;
        }
    }


}
