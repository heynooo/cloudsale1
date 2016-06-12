package com.vanhitech.vanhitech.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.SeekBar;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.vanhitech.protocol.cmd.client.CMD08_ControlDevice;
import com.vanhitech.protocol.cmd.client.CMD24_QueryTimer;
import com.vanhitech.protocol.cmd.client.CMD60_SetMatchStatus;
import com.vanhitech.protocol.object.device.Device;
import com.vanhitech.protocol.object.device.LightRGBDevice;
import com.vanhitech.vanhitech.R;
import com.vanhitech.vanhitech.activity.device.DeviceInfoActivity;
import com.vanhitech.vanhitech.activity.device.TimeingDeviceActivity;
import com.vanhitech.vanhitech.base.CmdBaseActivity;
import com.vanhitech.vanhitech.base.MyApplication;
import com.vanhitech.vanhitech.bean.Power;
import com.vanhitech.vanhitech.utils.DataUtils;
import com.vanhitech.vanhitech.utils.StringUtils;
import com.vanhitech.vanhitech.utils.T;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 创建者     heyn
 * 创建时间   2016/3/29 20:38
 * 描述	      ${TODO}
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class LightWhiteFragment extends Fragment {
    private View view;
    private String mName;
    private SeekBar mLightlimitBar, mLightPaceBar;
    private CheckBox mLightSwitch, cbIsTiming, mCbIsRemoteControl,mCbIsDeviceinfo;
    private DbUtils mDb;
    private String mIs;
    private Boolean mTemp;
    private Boolean flag = false;

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
    private Device mDevice;
    private Power mPower;
    private LightRGBDevice mRgblight;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_light_white, null);
        //灯亮度
        mLightlimitBar = (SeekBar) view.findViewById(R.id.LightLimitBar);

        //模式速度
        mLightPaceBar = (SeekBar) view.findViewById(R.id.LightPaceBar);

        //开关
        mLightSwitch = (CheckBox) view.findViewById(R.id.cb_is_switch);

        //定时
        cbIsTiming = (CheckBox) view.findViewById(R.id.cb_is_timing);

        //匹配
        mCbIsRemoteControl = (CheckBox) view.findViewById(R.id.cb_is_remote_control);

        mCbIsDeviceinfo= (CheckBox) view.findViewById(R.id.cb_is_deviceinfo);

        mDb = MyApplication.getDbUtils();
        try {
            mDevice = mDb.findFirst(Selector.from(LightRGBDevice.class).where("id", "=", mName));
        } catch (DbException e) {
            e.printStackTrace();
        }
        mPower = DataUtils.getInstance().getdataPower(mName);
        initData(mDevice, mPower);

        isPowSwitch();
        isnull();

        OnClickListener();


        return view;
    }

    private void initData(Device device, Power power) {
        mRgblight = DataUtils.getInstance().getdataRGBDevice(mName);
        getPowers(device.id);
        isPower();

    }

    private Boolean isPower() {
        if (!mDevice.getPowers().get(0).on||!mDevice.online) {
            mLightlimitBar.setProgress(0);
            mLightPaceBar.setProgress(0);
            return true;
        } else {
            return false;
        }
    }

    private void getPowers(String name) {
        com.vanhitech.vanhitech.bean.Power power = DataUtils.getInstance().getdataPower(name);
        List<com.vanhitech.protocol.object.Power> lisp = new ArrayList<>();
        lisp.add(0, new com.vanhitech.protocol.object.Power(0, DataUtils.getInstance().isTureFalse((power.power0))));
        lisp.add(1, new com.vanhitech.protocol.object.Power(1, DataUtils.getInstance().isTureFalse((power.power1))));
        mDevice.setPowers(lisp);
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
//                mActivity.finish();
                mActivity.overridePendingTransition(R.animator.nextenteranim, R.animator.nextexitanim);
            }
        });
        //匹配
        mCbIsRemoteControl.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CMD60_SetMatchStatus cmd60 = new CMD60_SetMatchStatus(0x80,
                        mDevice.id);
                CmdBaseActivity.getInstance().sendCmd(cmd60);

            }
        });
        //设备信息
        mCbIsDeviceinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mActivity, DeviceInfoActivity.class);
                intent.putExtra("name",mDevice.name);
                intent.putExtra("id", mDevice.id);
                startActivity(intent);
                mActivity.overridePendingTransition(R.animator.nextenteranim,R.animator.nextexitanim);
            }
        });
    }

    private void isPowSwitch() {
        try {//从数据库获取开关是开还是关，然后赋值

            Power d = mDb.findFirst(Selector.from(Power.class).where("id", "=", mName));

            mIs = d.power0;

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
            //
            if(!mDevice.online){
                mLightSwitch.setChecked(false);
                T.showShort(mActivity,"设备离线");
                return;
            }
            mTemp = !mTemp;
            lightSwich(mTemp, null, null, null);

            if(mTemp){
                mLightSwitch.setChecked(true);
                int pac = (int) (mRgblight.colortemp/(0.15));
                mLightPaceBar.setProgress(pac);//TODO
                int limit = (int) (mRgblight.brightness1/(0.15));
                mLightlimitBar.setProgress(limit);
            }else{
                mLightSwitch.setChecked(false);
                mLightPaceBar.setProgress(0);
                mLightlimitBar.setProgress(0);
            }
        }
    }


    private void lightSwich(Boolean flag, String brighteness1, String brighteness2, String colortemp) {
        //根据id　从数据库拿设备信息　　
        //　根据点击事件 给开/关 灯
        //发送 cmd


//            Device d=lightRgb;
        List<com.vanhitech.protocol.object.Power> powers = new ArrayList<>();
        com.vanhitech.protocol.object.Power p1 = new com.vanhitech.protocol.object.Power(1, false);
        com.vanhitech.protocol.object.Power p0 = new com.vanhitech.protocol.object.Power(0, flag);//TODO
        powers.add(p0);
        powers.add(p1);
        mDevice.setPowers(powers);


//            Boolean temp=!flag;
//            device.setPower(flag);
//           device.power.set(0,temp);
        mRgblight = (LightRGBDevice) mDevice;
        if (!StringUtils.isEmpty(brighteness1))
            mRgblight.brightness1 = Integer.parseInt(brighteness1);
        if (!StringUtils.isEmpty(brighteness2))
            mRgblight.brightness2 = Integer.parseInt(brighteness2);
        if (!StringUtils.isEmpty(colortemp))
            mRgblight.colortemp = Integer.parseInt(colortemp);


        CMD08_ControlDevice cmd08 = new CMD08_ControlDevice(mDevice);
//            try {
//
//                HomeController.mHelper.sendCMD(cmd08);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        CmdBaseActivity.getInstance().sendCmd(cmd08);


    }


    public LightWhiteFragment() {
    }

    public LightWhiteFragment(String name, Activity activity) {
        mName = name;
        mActivity = activity;
    }

    @Override
    public void onStart() {
        super.onStart();



        //给mlightswitch做点击事件,如果是按下后,开关信息取反,然后发送
        mLightSwitch.setOnClickListener(new lightSwitchClickListener());
//        mLightlimitBar.setOnSeekBarChangeListener(new mylimitBarOnSeekBarChangeListener());
//        mLightPaceBar.setOnSeekBarChangeListener(new myPaceBarOnSeekBarChangeListener());
        //速度
        mLightlimitBar.setOnSeekBarChangeListener(getLimitOnSeekBarChangeListener());
        // 色温
        mLightPaceBar.setOnSeekBarChangeListener(getPaceOnSeekBarChangeListener());

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
                if(isPower()) return;
                int temp = (int) (seekBar.getProgress() * (0.15));
                lightSwich(true, temp + "", null, null);
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
                //rgb 是亮度
                //cw 是色温
                if(isPower()) return;
                int temp = (int) (seekBar.getProgress() * (0.15));
                lightSwich(true, null, null, temp + "");
            }
        };
    }
//
//    class mylimitBarOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
//
//        @Override
//        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//            LogUtils.i(progress + "progress的onProgressChanged值大小");
//        }
//
//        @Override
//        public void onStartTrackingTouch(SeekBar seekBar) {
//            LogUtils.i(seekBar.getProgress() + "progress的onStartTrackingTouch值大小");
//        }
//
//        @Override
//        public void onStopTrackingTouch(SeekBar seekBar) {
//            LogUtils.i(seekBar.getProgress() + "progress的onStopTrackingTouch值大小");
//            lightSwich(true, seekBar.getProgress() + "", null, null);
//        }
//    }
//
//    class myPaceBarOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
//
//        @Override
//        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//            LogUtils.i(progress + "progress的onProgressChanged值大小");
//        }
//
//        @Override
//        public void onStartTrackingTouch(SeekBar seekBar) {
//            LogUtils.i(seekBar.getProgress() + "progress的onStartTrackingTouch值大小");
//        }
//
//        @Override
//        public void onStopTrackingTouch(SeekBar seekBar) {
//            LogUtils.i(seekBar.getProgress() + "progress的onStopTrackingTouch值大小");
//            lightSwich(true, null, null, seekBar.getProgress() + "");
//        }
//    }


    //滑动测试

    public void display() {


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


}
