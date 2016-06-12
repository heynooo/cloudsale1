package com.vanhitech.vanhitech.activity.device;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vanhitech.protocol.cmd.client.CMD18_AddTimerTask;
import com.vanhitech.protocol.cmd.client.CMD20_ModifyTimer;
import com.vanhitech.protocol.cmd.client.CMD22_DelTimer;
import com.vanhitech.protocol.object.Power;
import com.vanhitech.protocol.object.TimerTask;
import com.vanhitech.protocol.object.device.Device;
import com.vanhitech.vanhitech.R;
import com.vanhitech.vanhitech.base.BaseActivityController;
import com.vanhitech.vanhitech.base.CmdBaseActivity;
import com.vanhitech.vanhitech.base.MyApplication;
import com.vanhitech.vanhitech.bean.TimeTaskDay;
import com.vanhitech.vanhitech.manager.ActivityTitleManager;
import com.vanhitech.vanhitech.utils.DataUtils;
import com.vanhitech.vanhitech.utils.LogUtils;
import com.vanhitech.vanhitech.views.wheelview.LoopView;
import com.vanhitech.vanhitech.views.wheelview.OnItemSelectedListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建者     heyn
 * 创建时间   2016/3/18 14:42
 * 描述	      设备定时的添加/修改
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class TimeingDeviceSettingActivity extends BaseActivityController {

    private String mId, mDeviceId;
    private TextView mTvTimingOn;
    private TextView mTvTimingOff;
    private LinearLayout mT1;
    private LinearLayout mT2;
    private CheckBox mCbWay1, mCbWay2, mCbWay3, mCbDay1, mCbDay2, mCbDay3, mCbDay4, mCbDay5, mCbDay6, mCbDay7;
    private TimerTask mTt;
    private Device mDevice;
    private TimeTaskDay mTimeTaskDay;
    private Boolean mBlTimingOn = true;
    private Boolean mblWay1 = true,
            mblWay2 = true,
            mblWay3 = true,
            mblDay1 = true,
            mblDay2 = true,
            mblDay3 = true,
            mblDay4 = true,
            mblDay5 = true,
            mblDay6 = true,
            mblDay7 = true;
    private Boolean isSaveUpdate = false;//
    private Context mContext = this;
    private TextView mTimeingDelete;
    private TextView mTimeingSave;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);


    }

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_timeingdevice_setting);
        ActivityTitleManager.getInstance().init(this);
        ActivityTitleManager.getInstance().showCommonTitle();


        //-------------------------------
        assignViews();

        //根据数据初始化界面
    }

    private void assignViews() {
        mT1 = (LinearLayout) findViewById(R.id.test2);
        mT2 = (LinearLayout) findViewById(R.id.test1);
        mTvTimingOn = (TextView) findViewById(R.id.tv_timing_on);
        mTvTimingOff = (TextView) findViewById(R.id.tv_timing_off);
        mCbWay1 = (CheckBox) findViewById(R.id.cb_way1);
        mCbWay2 = (CheckBox) findViewById(R.id.cb_way2);
        mCbWay3 = (CheckBox) findViewById(R.id.cb_way3);
        mCbDay1 = (CheckBox) findViewById(R.id.cb_day1);
        mCbDay2 = (CheckBox) findViewById(R.id.cb_day2);
        mCbDay3 = (CheckBox) findViewById(R.id.cb_day3);
        mCbDay4 = (CheckBox) findViewById(R.id.cb_day4);
        mCbDay5 = (CheckBox) findViewById(R.id.cb_day5);
        mCbDay6 = (CheckBox) findViewById(R.id.cb_day6);
        mCbDay7 = (CheckBox) findViewById(R.id.cb_day7);
        mTimeingDelete = (TextView) findViewById(R.id.timeing_delete);
        mTimeingSave = (TextView) findViewById(R.id.timeing_save);


    }


    private void timeHourMinute() {
        LoopView loopView2 = new LoopView(this);
        ArrayList<String> list2 = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            list2.add("" + i);
        }
        //设置是否循环播放
        //loopView.setNotLoop();
        //滚动监听
        loopView2.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {//TODO
                LogUtils.i("debug2", "小时:-- " + index);
                mTt.hour = index;
            }
        });
        //设置原始数据
        loopView2.setItems(list2);
        //设置初始位置
        if (mTimeTaskDay != null) {
            loopView2.setInitPosition(stringToInt(stringIsNullr1(mTimeTaskDay.hour)));//从数据获取设置
            mTt.hour = stringToInt(stringIsNullr1(mTimeTaskDay.hour));
        } else {
            loopView2.setInitPosition(0);//从数据获取设置
            mTt.hour = 0;
        }
        //设置字体大小
        loopView2.setTextSize(18);
        loopView2.setViewPadding(9, 9, 9, 9);

        mT2.addView(loopView2);
        //----------------------

        LoopView loopView = new LoopView(this);
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            list.add("" + i);
        }
        //设置是否循环播放
        //loopView.setNotLoop();
        //滚动监听
        loopView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {//TODO
                LogUtils.i("debug1", "分钟 :--" + index);
                mTt.minute = index;
            }
        });
        //设置原始数据
        loopView.setItems(list);
        //设置初始位置
        if (mTimeTaskDay != null) {
            loopView.setInitPosition(stringToInt(stringIsNullr1(mTimeTaskDay.minute)));//从数据获取设置
            mTt.minute = stringToInt(stringIsNullr1(mTimeTaskDay.minute));
        } else {
            loopView.setInitPosition(0);//从数据获取设置
            mTt.minute = 0;
        }
        //设置字体大小
        loopView2.setViewPadding(9, 9, 9, 9);

        loopView.setTextSize(18);
        mT1.addView(loopView);
    }

    @Override
    public void initData() {
        super.initData();
        Intent intent = getIntent();
        mId = intent.getStringExtra("mId");//这个其实是时间+设备信息的id
        mDeviceId = intent.getStringExtra("mDeviceId");//这个其实是设备信息的id
        mTimeTaskDay = DataUtils.getInstance().timeTaskDaySelect(mId);
        if (mTimeTaskDay != null) {
            isSaveUpdate = false;
            //分成定时,设备
            List<Boolean> day = new ArrayList<>();
            day.add(0, toTrueFalse(mTimeTaskDay.D1));
            day.add(1, toTrueFalse(mTimeTaskDay.D2));
            day.add(2, toTrueFalse(mTimeTaskDay.D3));
            day.add(3, toTrueFalse(mTimeTaskDay.D4));
            day.add(4, toTrueFalse(mTimeTaskDay.D5));
            day.add(5, toTrueFalse(mTimeTaskDay.D6));
            day.add(6, toTrueFalse(mTimeTaskDay.D7));
            //String id, int hour, int minute, List<Boolean> day, boolean repeated, boolean enabled
            mTt = new TimerTask(mTimeTaskDay.id, stringToInt(mTimeTaskDay.hour), stringToInt(mTimeTaskDay.minute), day, toTrueFalse(mTimeTaskDay.repeated), toTrueFalse(mTimeTaskDay.enabled));

            List<Power> power = new ArrayList<>();
            power.add(new Power(0, toTrueFalse(mTimeTaskDay.power0)));
            power.add(new Power(1, toTrueFalse(mTimeTaskDay.power1)));
            power.add(new Power(2, toTrueFalse(mTimeTaskDay.power2)));
            mDevice = new Device(mTimeTaskDay.deviceId, mTimeTaskDay.pid, mTimeTaskDay.name, mTimeTaskDay.place, toTrueFalse(mTimeTaskDay.online), power);
            mDeviceId = mTimeTaskDay.deviceId;
        } else {
            isSaveUpdate = true;
            //new 定时 ,设备
            List<Boolean> day = new ArrayList<>();
            for (int i = 0; i < 7; i++) {
                day.add(false);
            }
            mTt = new TimerTask(null, 0, 0, day, false, false);
            //TODO

            mDevice = DataUtils.getInstance().getDevice(mDeviceId);
        }


    }

    @Override
    public void initEvent() {
        super.initEvent();

        //.setChecked(true);
        if (mTimeTaskDay != null) {
            mCbWay1.setChecked(toTrueFalse(mTimeTaskDay.power0));
            mCbWay2.setChecked(toTrueFalse(mTimeTaskDay.power1));
            mCbWay3.setChecked(toTrueFalse(mTimeTaskDay.power2));
            mCbDay1.setChecked(toTrueFalse(mTimeTaskDay.D1));
            mCbDay2.setChecked(toTrueFalse(mTimeTaskDay.D2));
            mCbDay3.setChecked(toTrueFalse(mTimeTaskDay.D3));
            mCbDay4.setChecked(toTrueFalse(mTimeTaskDay.D4));
            mCbDay5.setChecked(toTrueFalse(mTimeTaskDay.D5));
            mCbDay6.setChecked(toTrueFalse(mTimeTaskDay.D6));
            mCbDay7.setChecked(toTrueFalse(mTimeTaskDay.D7));

            if (toTrueFalse(mTimeTaskDay.enabled)) {
                mTvTimingOn.setTextColor(Color.parseColor("#39A1E8"));
                mTvTimingOff.setTextColor(Color.parseColor("#AEBECC"));
            } else {
                mTvTimingOn.setTextColor(Color.parseColor("#AEBECC"));
                mTvTimingOff.setTextColor(Color.parseColor("#39A1E8"));

            }


        }

        timeHourMinute(); //时间获取/设置

        toggleCheckBox();

        ClickListenerTextView();


        //TODO 数据库获取
        if (mTimeTaskDay != null)
            setBoolean();
        if (mTimeTaskDay != null) {
            ActivityTitleManager.getInstance().changeTitle(stringIsNull(mTimeTaskDay.name) + "设置定时");
        } else {
            ActivityTitleManager.getInstance().changeTitle("设置定时");
        }
        ActivityTitleManager.getInstance().changehelpText("保存");

    }

    @Override
    public void initListener() {
        super.initListener();
        ClickListenerButton();

        ActivityTitleManager.getInstance().goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.finish();
            }
        });
    }

    private void setBoolean() {
        mBlTimingOn = !toTrueFalse(mTimeTaskDay.enabled);
        mblWay1 = !toTrueFalse(mTimeTaskDay.power0);
        mblWay2 = !toTrueFalse(mTimeTaskDay.power1);
        mblWay3 = !toTrueFalse(mTimeTaskDay.power2);
        mblDay1 = !toTrueFalse(mTimeTaskDay.D1);
        mblDay2 = !toTrueFalse(mTimeTaskDay.D2);
        mblDay3 = !toTrueFalse(mTimeTaskDay.D3);
        mblDay4 = !toTrueFalse(mTimeTaskDay.D4);
        mblDay5 = !toTrueFalse(mTimeTaskDay.D5);
        mblDay6 = !toTrueFalse(mTimeTaskDay.D6);
        mblDay7 = !toTrueFalse(mTimeTaskDay.D7);

    }

    private void ClickListenerButton() {
        ActivityTitleManager.getInstance().help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isSaveUpdate) {//保存
                    CMD18_AddTimerTask cmd18 = new CMD18_AddTimerTask(transform(mTt), mDevice);
                    CmdBaseActivity.getInstance().sendCmd(cmd18);
                    MyApplication.setIsbrushTime(true);
                } else {//修改
                    CMD20_ModifyTimer cmd20 = new CMD20_ModifyTimer(transform(mTt), mDevice);
                    CmdBaseActivity.getInstance().sendCmd(cmd20);
                    MyApplication.setIsbrushTime(true);
                }

                finish();
            }
        });

        mTimeingDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CMD22_DelTimer cmd22 = new CMD22_DelTimer(mTimeTaskDay.id);
                CmdBaseActivity.getInstance().sendCmd(cmd22);
                DataUtils.getInstance().deleteTimerTaskSing(mTimeTaskDay.id);
                MyApplication.setIsbrushTime(true);

                finish();
            }
        });
        mTimeingSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CMD18_AddTimerTask cmd18 = new CMD18_AddTimerTask(transform(mTt), mDevice);
                CmdBaseActivity.getInstance().sendCmd(cmd18);
                MyApplication.setIsbrushTime(true);
                finish();
            }
        });

    }

    private void inten(Context context, Class<?> ActivityClass) {
        Intent intent = new Intent();
        intent.setClass(context, ActivityClass);
        intent.putExtra("mDeviceId", mDevice);
        startActivity(intent);
        finish();
    }



    private void ClickListenerTextView() {
        mTvTimingOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBlTimingOn) {
                    mTvTimingOn.setTextColor(Color.parseColor("#39A1E8"));
                    mTvTimingOff.setTextColor(Color.parseColor("#AEBECC"));
                    LogUtils.i("定时开" + mBlTimingOn);
                    mTt.enabled = mBlTimingOn;
                    mBlTimingOn = false;
                } else {
                    mTvTimingOn.setTextColor(Color.parseColor("#AEBECC"));
                    mTvTimingOff.setTextColor(Color.parseColor("#39A1E8"));
                    LogUtils.i("定时开" + mBlTimingOn);
                    mTt.enabled = mBlTimingOn;
                    mBlTimingOn = true;
                }
            }
        });


        mTvTimingOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBlTimingOn) {
                    mTvTimingOn.setTextColor(Color.parseColor("#39A1E8"));
                    mTvTimingOff.setTextColor(Color.parseColor("#AEBECC"));
                    LogUtils.i("定时开" + mBlTimingOn);
                    mTt.enabled = mBlTimingOn;
                    mBlTimingOn = false;
                } else {
                    mTvTimingOn.setTextColor(Color.parseColor("#AEBECC"));
                    mTvTimingOff.setTextColor(Color.parseColor("#39A1E8"));
                    LogUtils.i("定时开" + mBlTimingOn);
                    mTt.enabled = mBlTimingOn;
                    mBlTimingOn = true;
                }
            }
        });


//
//        mTvTimingOff.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mblTimingOn) {
//                    mTvTimingOff.setTextColor(Color.parseColor("#39A1E8"));
//                    mTvTimingOn.setTextColor(Color.parseColor("#AEBECC"));
//                    LogUtils.i("定时关" + mblTimingOn);
//                    mTt.enabled=mblTimingOff;
//                    mblTimingOn = false;
//                } else {
//                    mTvTimingOff.setTextColor(Color.parseColor("#AEBECC"));
//                    mTvTimingOn.setTextColor(Color.parseColor("#39A1E8"));
//                    LogUtils.i("定时关" + mblTimingOn);
//                    mTt.enabled=mblTimingOff;
//                    mblTimingOn = true;
//                }
//            }
//        });


    }


    private void toggleCheckBox() {

        //测试一路
        mCbWay1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mblWay1) {
                    LogUtils.i("mblWay1" + mblWay1);
                    mDevice.power.add(new Power(0, mblWay1));//TODO 测试下是否可行
                    mblWay1 = false;
                } else {
                    LogUtils.i("mblWay1" + mblWay1);
                    mDevice.power.add(new Power(0, mblWay1));
                    mblWay1 = true;
                }
            }
        });
        //二路
        mCbWay2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mblWay2) {
                    LogUtils.i("mblWay2" + mblWay2);
                    mDevice.power.add(new Power(1, mblWay2));
                    mblWay2 = false;
                } else {
                    mDevice.power.add(new Power(1, mblWay2));
                    LogUtils.i("mblWay2" + mblWay2);
                    mblWay2 = true;
                }
            }
        });
        //三路
        mCbWay3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mblWay3) {
                    LogUtils.i("mblWay3" + mblWay3);
                    mDevice.power.add(new Power(2, mblWay3));
                    mblWay3 = false;
                } else {
                    LogUtils.i("mblWay3" + mblWay3);
                    mDevice.power.add(new Power(3, mblWay3));
                    mblWay3 = true;
                }
            }
        });
        //day1 mCbDay1
        mCbDay1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mblDay1) {
                    LogUtils.i("mCbDay1" + mblDay1);
//                    mTt.day.add(0,);//TODO
                    mTt.day.set(0, mblDay1);
                    mblDay1 = false;
                } else {
                    LogUtils.i("mCbDay1" + mblDay1);
                    mTt.day.set(0, mblDay1);
                    mblDay1 = true;
                }
            }
        });
        //day2
        mCbDay2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mblDay2) {
                    LogUtils.i("mCbDay2" + mblDay2);
                    mTt.day.set(1, mblDay2);
                    mblDay2 = false;
                } else {
                    LogUtils.i("mCbDay2" + mblDay2);
                    mTt.day.set(1, mblDay2);
                    mblDay2 = true;
                }
            }
        });
        //day3
        mCbDay3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mblDay3) {
                    LogUtils.i("mCbDay3" + mblDay3);
                    mTt.day.set(2, mblDay3);
                    mblDay3 = false;
                } else {
                    LogUtils.i("mCbDay3" + mblDay3);
                    mTt.day.set(2, mblDay3);
                    mblDay3 = true;
                }
            }
        });
        //day4
        mCbDay4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mblDay4) {
                    LogUtils.i("mCbDay4" + mblDay4);
                    mTt.day.set(3, mblDay4);
                    mblDay4 = false;
                } else {
                    LogUtils.i("mCbDay4" + mblDay4);
                    mTt.day.set(3, mblDay4);
                    mblDay4 = true;
                }
            }
        });
        //day5
        mCbDay5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mblDay5) {
                    LogUtils.i("mCbDay5" + mblDay5);
                    mTt.day.set(4, mblDay5);
                    mblDay5 = false;
                } else {
                    LogUtils.i("mCbDay5" + mblDay5);
                    mTt.day.set(4, mblDay5);
                    mblDay5 = true;
                }
            }
        });
        //day6
        mCbDay6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mblDay6) {
                    LogUtils.i("mCbDay6" + mblDay6);
                    mTt.day.set(5, mblDay6);
                    mblDay6 = false;
                } else {
                    LogUtils.i("mCbDay6" + mblDay6);
                    mTt.day.set(5, mblDay6);
                    mblDay6 = true;
                }
            }
        });
        //day7
        mCbDay7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mblDay7) {
                    LogUtils.i("mCbDay7" + mblDay7);
                    mTt.day.set(6, mblDay7);
                    mblDay7 = false;
                } else {
                    LogUtils.i("mCbDay7" + mblDay7);
                    mTt.day.set(6, mblDay7);
                    mblDay7 = true;
                }
            }
        });
    }


    //1,0 转换成 true flase
    public Boolean toTrueFalse(String b) {
        Boolean is;
        if (b == null || b.equals("0")) {
            is = false;
        } else {
            is = true;
        }
        return is;
    }

    private int stringToInt(String s) {

        return Integer.parseInt(s);
    }

    private String stringIsNull(String s) {
        if (s == null) {
            return "";
        } else {
            return s;
        }

    }

    private String stringIsNullr1(String s) {
        if (s == null) {
            return "1";
        } else {
            return s;
        }

    }

    private TimerTask transform(TimerTask tt) {
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

}
