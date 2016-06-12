package com.vanhitech.vanhitech.activity.device;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SaturationBar;
import com.larswerkman.holocolorpicker.ValueBar;
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
import com.vanhitech.protocol.object.device.LightCWDevice;
import com.vanhitech.vanhitech.R;
import com.vanhitech.vanhitech.base.BaseActivityController;
import com.vanhitech.vanhitech.base.CmdBaseActivity;
import com.vanhitech.vanhitech.base.MyApplication;
import com.vanhitech.vanhitech.bean.CWDevice0D;
import com.vanhitech.vanhitech.bean.Power;
import com.vanhitech.vanhitech.conf.Constants;
import com.vanhitech.vanhitech.conf.LoginConstants;
import com.vanhitech.vanhitech.manager.ActivityTitleManager;
import com.vanhitech.vanhitech.utils.AnimationController;
import com.vanhitech.vanhitech.utils.DataUtils;
import com.vanhitech.vanhitech.utils.LogUtils;
import com.vanhitech.vanhitech.utils.StringUtils;
import com.vanhitech.vanhitech.utils.UIUtils;
import com.vanhitech.vanhitech.views.LightRgbTagView;
import com.vanhitech.vanhitech.views.NoScrollViewPager;

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
public class LightWhiteActivity extends BaseActivityController {

    public LightRgbTagView mLrt_tag;
    private NoScrollViewPager mViewpager;
    private FrameLayout mFl_content;
    private OpacityBar mOpacityBar;
    private ValueBar mValueBar;
    private SaturationBar mSaturationBar;
    private SeekBar mLightlimitBar, mLightPaceBar;
    private String mName;
    private Boolean flag = false;
    private CheckBox cbIsTiming, mCbIsRemoteControl;
    private CheckBox cbIsDeviceinfo;
    private LinearLayout mLayoutLightLimit, mLayoutLightPace;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
//                    mLightPaceBar.setProgress(mLightPaceBar.getProgress() + 10);
                    ServerCommand cmd = (ServerCommand) msg.obj;
                    LogUtils.i("LightWhiteActivity主线程得到命令,建议从数据库获取数据更新ui" + "cmd:" + cmd);
                    CMD09_ServerControlResult cmd09 = (CMD09_ServerControlResult) cmd;
                    Device device = cmd09.status;
                    //获取id 号,如果一样,更新否则不处理  id　号,保存到全局变量里面
                    if (device.id.equals(LoginConstants.deviceID)) {
                        LogUtils.i("执行更新ui" + device.id);
//                        //灯亮度
                        if (device.id.substring(0, 2).equals(Constants.idD)) {
                            LightCWDevice L = (LightCWDevice) device;
                            mBrightness = L.brightness + "";
                            mColortemp = L.colortemp + "";

                            new LightWhiteActivity(mBrightness + "", mColortemp + "");
//                            test();
                        }
                    }
                    break;
            }
        }
    };
    private CheckBox mLightSwitch;
    private DbUtils mDb;
    private String mIs;
    private Boolean mTemp;
    private Device mDevice;
    private String mBrightness;
    private String mColortemp;
    private Context mContext;
    private Power mPower;
    private LightCWDevice mLightCWDevice;


    public LightWhiteActivity(String s, String s1) {
        super();
        this.mBrightness = s;
        this.mColortemp = s1;
    }


    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_device_lightwhite);
        mContext = LightWhiteActivity.this;
        cbIsTiming = (CheckBox) findViewById(R.id.cb_is_timing);
        cbIsDeviceinfo = (CheckBox) findViewById(R.id.cb_is_deviceinfo);
        mLayoutLightLimit = (LinearLayout) findViewById(R.id.layoutLightLimit);
        mLayoutLightPace = (LinearLayout) findViewById(R.id.layoutLightPace);
        mCbIsRemoteControl = (CheckBox) findViewById(R.id.cb_is_remote_control);

        Intent intent = getIntent();
        mName = intent.getStringExtra("name");   //这里其实拿到的是id
        //
        sendCmd06(mName,LightWhiteActivity.this);
        LoginConstants.deviceID = mName;
        mDb = MyApplication.getDbUtils();
        //更新默认值


        ActivityTitleManager.getInstance().init(this);
        ActivityTitleManager.getInstance().showCommonTitle();


        //灯亮度
        mLightlimitBar = (SeekBar) findViewById(R.id.LightLimitBar);

        //模式速度
        mLightPaceBar = (SeekBar) findViewById(R.id.LightPaceBar);

        //开关
        mLightSwitch = (CheckBox) findViewById(R.id.cb_is_switch);
        isPowSwitch();
        isnull();
        lightUiUpdate();
    }

    /**
     * 进入的时候更新ui
     * 实时更新采用通知者模式更新
     */
    private void lightUiUpdate() {
        if (mName.substring(0, 2).equals(Constants.idD)) {
            //数据库获取
            CWDevice0D cwdevice = null;
            try {
                cwdevice = mDb.findFirst(Selector.from(CWDevice0D.class).where("id", "=", mName));
            } catch (DbException e) {
                e.printStackTrace();
            }
            mBrightness = cwdevice.brightness;
            if (!StringUtils.isEmpty(mBrightness)) {
                int b = (int) ((Integer.parseInt(mBrightness) / (0.15)));
                mLightlimitBar.setProgress(b);
            }
            mColortemp = cwdevice.colortemp;
            if (!StringUtils.isEmpty(mColortemp)) {
                int c = (int) ((Integer.parseInt(mColortemp) / (0.15)));
                mLightPaceBar.setProgress(c);
            }
        }
    }

    private void isPowSwitch() {
        try {
            Power d = mDb.findFirst(Selector.from(Power.class).where("id", "=", mName));
           if(d!=null){
               mIs = d.power0;
           }else{
               mIs=0+"";
           }
            if (mIs.equals("1")) {//checkbox.setChecked(true);
                mLightSwitch.setChecked(true);
                flag = true;
            } else {
                mLightSwitch.setChecked(false);
                flag = false;
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void initData() {
        super.initData();

        mDevice = DataUtils.getInstance().getDevice(mName);
        sendCmd06(mName, LightWhiteActivity.this);
        mPower = DataUtils.getInstance().getdataPower(mName);
        getPowers(mPower);
        isPower();
    }

    private void sendCmd06(String id, LightWhiteActivity listener) {
        CmdBaseActivity.getInstance().mHelper.setCommandListener(listener);
        CMD06_QueryDeviceStatus cmd06 = new CMD06_QueryDeviceStatus(id);
        CmdBaseActivity.getInstance().sendCmd(cmd06);
    }


    private void getPowers(Power power) {
        List<com.vanhitech.protocol.object.Power> lisp = new ArrayList<>();
        lisp.add(0, new com.vanhitech.protocol.object.Power(0, DataUtils.getInstance().isTureFalse((power.power0))));
        lisp.add(1, new com.vanhitech.protocol.object.Power(1, DataUtils.getInstance().isTureFalse((power.power1))));
        mDevice.setPowers(lisp);
    }

    private Boolean isPower() {
        if (!mDevice.getPowers().get(0).on) {
            mLightlimitBar.setProgress(0);
            mLightPaceBar.setProgress(0);
            return true;
        } else {
//            mLightCWDevice
            /*
            mLightlimitBar.setProgress(Integer.parseInt(mBrightness));
            mLightPaceBar.setProgress(Integer.parseInt(mColortemp));
             int pac = (int) (mLightRGBDevice.freq/(0.15));
                mLightPaceBar.setProgress(pac);//TODO
                int limit = (int) (mLightRGBDevice.brightness2/(0.15));
                mLightlimitBar.setProgress(limit);
             */

            return false;
        }
    }


    @Override
    public void initEvent() {
        super.initEvent();

//        start();

        //给mlightswitch做点击事件,如果是按下后,开关信息取反,然后发送
        ActivityTitleManager.getInstance().changeTitle(mDevice.name);
        mLightSwitch.setOnClickListener(new lightSwitchClickListener());

//        mLightlimitBar.setOnSeekBarChangeListener(new mylimitBarOnSeekBarChangeListener());
//        mLightPaceBar.setOnSeekBarChangeListener(new myPaceBarOnSeekBarChangeListener());

        //速度
        mLightlimitBar.setOnSeekBarChangeListener(getLimitOnSeekBarChangeListener());
        // 色温
        mLightPaceBar.setOnSeekBarChangeListener(getPaceOnSeekBarChangeListener());

        //跳转定时
        cbIsTiming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                CMD24_QueryTimer cmd24 = new CMD24_QueryTimer(mName);
                CmdBaseActivity.getInstance().sendCmd(cmd24);

                intent.setClass(LightWhiteActivity.this, TimeingDeviceActivity.class);
                intent.putExtra("mDeviceId", mName);
                startActivity(intent);

//                finish();
                overridePendingTransition(R.animator.nextenteranim, R.animator.nextexitanim);
            }
        });

        cbIsDeviceinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mContext, DeviceInfoActivity.class);
                intent.putExtra("name", mDevice.name);
                intent.putExtra("id", mDevice.id);
                startActivity(intent);
                overridePendingTransition(R.animator.nextenteranim, R.animator.nextexitanim);
            }
        });


        if (mName.substring(0, 2).equals(Constants.id3)) {//2.4G灯开关控制  !
            //隐藏 亮度和速度
            AnimationController.transparent(mLayoutLightLimit);
            AnimationController.transparent(mLayoutLightPace);

        }

        //匹配
        mCbIsRemoteControl.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CMD60_SetMatchStatus cmd60 = new CMD60_SetMatchStatus(0x80,
                        mDevice.id);
                CmdBaseActivity.getInstance().sendCmd(cmd60);

            }
        });
    }

    @Override
    public void  initListener(){
        super.initListener();

        ActivityTitleManager.getInstance().goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.finish();
            }
        });
    }


    @NonNull
    private SeekBar.OnSeekBarChangeListener getLimitOnSeekBarChangeListener() {
        return new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (isPower()) return;
                int temp = (int) (seekBar.getProgress() * (0.15));
                mBrightness = temp + "";
                lightSwich(mTemp, mBrightness, null);
            }
        };
    }

    @NonNull
    private SeekBar.OnSeekBarChangeListener getPaceOnSeekBarChangeListener() {
        return new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (isPower()) return;
                int temp = (int) (seekBar.getProgress() * (0.15));
                mColortemp = temp + "";
                lightSwich(mTemp, mBrightness, mColortemp);
            }
        };
    }


    /**
     * 直接通过构造方法,调用
     */
    public LightWhiteActivity() {

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


    private Boolean isnull() {
        if (flag != null) {
            mTemp = flag;

            return mTemp;
        }
        return false;
    }

    private class lightSwitchClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            mTemp = !mTemp;
            lightSwich(mTemp, mBrightness, mColortemp);
            if (mDevice.type == 13) {
                mLightCWDevice.setPower(mTemp);
                if (mTemp) {
                    int b = (int) (mLightCWDevice.brightness / (0.15));
                    int temp = (int) (mLightCWDevice.colortemp / (0.15));
                    mLightlimitBar.setProgress(b);
                    mLightPaceBar.setProgress(temp);
                } else {
                    mLightlimitBar.setProgress(0);//TODO
                    mLightPaceBar.setProgress(0);
                }
            } else {
                mDevice.setPower(mTemp);
            }

        }
    }


    private void lightSwich(Boolean flag, String brightness, String colortemp) {

        if (mName.substring(0, 2).equals(Constants.idD)) {
            whiteSwitch(flag, mBrightness, mColortemp);
        } else {
            mDevice.setPower(flag);
        }
        CMD08_ControlDevice cmd08 = new CMD08_ControlDevice(mDevice);
        CmdBaseActivity.getInstance().sendCmd(cmd08);

    }

    private void whiteSwitch(Boolean flag, String brightness, String colortemp) {
        CWDevice0D cwdevice = null;
        try {
            cwdevice = mDb.findFirst(Selector.from(CWDevice0D.class).where("id", "=", mName));
        } catch (DbException e) {
            e.printStackTrace();
        }
        mLightCWDevice = new LightCWDevice(cwdevice.id, cwdevice.pid, cwdevice.name, cwdevice.place);
        if (!StringUtils.isEmpty(brightness))
            mLightCWDevice.brightness = Integer.parseInt(brightness);
        if (!StringUtils.isEmpty(colortemp))
            mLightCWDevice.colortemp = Integer.parseInt(colortemp);
        mDevice = mLightCWDevice;
        List<com.vanhitech.protocol.object.Power> powers = new ArrayList<>();//flag
        com.vanhitech.protocol.object.Power power = new com.vanhitech.protocol.object.Power(0, flag);
        powers.add(power);
        mDevice.setPowers(powers);

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
                    if (mDevice.type == 13) {
                        mLightCWDevice = (LightCWDevice) mDevice;
                    }
                }
                break;
            case CMD09_ServerControlResult.Command:
                CMD09_ServerControlResult cmd09 = (CMD09_ServerControlResult) cmd;
                Device device = cmd09.status;
                if (mDevice!=null&&mDevice.type == 13) {
                    sendCmd06(mName, LightWhiteActivity.this);
                }

                break;
        }
    }

}
