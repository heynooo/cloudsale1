package com.vanhitech.vanhitech.activity.device;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vanhitech.protocol.cmd.ClientCommand;
import com.vanhitech.protocol.cmd.ServerCommand;
import com.vanhitech.protocol.cmd.client.CMD06_QueryDeviceStatus;
import com.vanhitech.protocol.cmd.client.CMD08_ControlDevice;
import com.vanhitech.protocol.cmd.server.CMD07_ServerRespondDeviceStatus;
import com.vanhitech.protocol.cmd.server.CMD09_ServerControlResult;
import com.vanhitech.protocol.cmd.server.CMDFF_ServerException;
import com.vanhitech.protocol.object.device.AirType5Device;
import com.vanhitech.protocol.object.device.AirTypeADevice;
import com.vanhitech.protocol.object.device.Device;
import com.vanhitech.protocol.object.device.TranDevice;
import com.vanhitech.vanhitech.R;
import com.vanhitech.vanhitech.base.BaseActivityController;
import com.vanhitech.vanhitech.base.CmdBaseActivity;
import com.vanhitech.vanhitech.bean.CMDFactory;
import com.vanhitech.vanhitech.conf.Constants;
import com.vanhitech.vanhitech.manager.ActivityTitleManager;
import com.vanhitech.vanhitech.utils.LogUtils;
import com.vanhitech.vanhitech.utils.StringUtils;
import com.vanhitech.vanhitech.utils.T;
import com.vanhitech.vanhitech.utils.UIUtils;
import com.vanhitech.vanhitech.utils.Util;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 创建者     heyn
 * 创建时间   2016/3/18 14:42
 * 描述	      空调遥控器配对
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class AirPairActivity extends BaseActivityController {
    private LinearLayout ll_air_name;
    private ImageView img_return, iv_Up, iv_next, iv_start, iv_tp_up,
            iv_tp_down, iv_switch;
    private Timer timer;
    private TextView tv_room;
    private int currentGroup = -1;
    private int currentIndex = -1;
    private boolean isSearch = false; // 正在查找
    private boolean isAutoSearch = true;// 是否自动搜索
    private int autoIndex = -1;
    private int autoSum;
    private TextView tv_start, tv_air_name;
    private Device device;
    private String device_id;
    private int on = 0;
    private int setTempN;
    private int currntNum = -1;
    private int lightOn;
    private TranDevice tranDevice;
    private AirType5Device airType5Device;
    private AirTypeADevice airtypeADevice;
    private boolean tran_flag = false;
    private boolean air_A_flag = false;
    private boolean air_5_flag = false;
    private int mode;
    public static boolean is_pairing = false;
    private Context mContext;
    private String mName;
    private String mId;


    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_pair_air);
        ActivityTitleManager.getInstance().init(this);
        ActivityTitleManager.getInstance().showCommonTitle();
        ActivityTitleManager.getInstance().changeTitle("空调匹配");
        assignViews();
        mContext = this;
    }

    private void assignViews() {
        ll_air_name = (LinearLayout) findViewById(R.id.ll_air_name);
        iv_Up = (ImageView) findViewById(R.id.iv_Up);
        iv_next = (ImageView) findViewById(R.id.iv_next);
        iv_start = (ImageView) findViewById(R.id.iv_start);
        iv_tp_up = (ImageView) findViewById(R.id.iv_tp_up);
        iv_tp_down = (ImageView) findViewById(R.id.iv_tp_down);
        iv_switch = (ImageView) findViewById(R.id.iv_switch);
        tv_start = (TextView) findViewById(R.id.tv_start);
        tv_air_name = (TextView) findViewById(R.id.tv_air_name);
    }

    @Override
    public void initData() {
        super.initData();
        Intent intent = getIntent();
        mId = intent.getStringExtra("id");
        mName = intent.getStringExtra("name");
        CMD06_QueryDeviceStatus cmd06 = new CMD06_QueryDeviceStatus(mId);
        sendCMD(cmd06);
    }

    private void sendCMD(ClientCommand cmd) {
        CmdBaseActivity.getInstance().mHelper.setCommandListener(AirPairActivity.this);
       CmdBaseActivity.getInstance().sendCmd(cmd);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        clickListener();
    }

    private void clickListener() {
        ll_air_name.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                isSearch = false;
                cancelTimer();
                isAutoSearch = false;
                autoIndex = -1;
                currentIndex = -1;
                tv_start.setText(getResources().getString(R.string.start));
                iv_start.setSelected(false);
                startActivityForResult(new Intent(mContext,
                        AirBrandActivity.class), 1);
            }
        });
    }

    private void judgeType() {

        if (device.type == 0x0A) {
            airtypeADevice = (AirTypeADevice) device;
            air_A_flag = true;
            air_5_flag = false;

        } else if (device.type == 0x05) {
            airType5Device = (AirType5Device) device;
            air_5_flag = true;
            air_A_flag = false;

        }
        if (air_5_flag) {
            refreshView_5();
        } else if (air_A_flag) {
            refreshView_A();
        }
    }

    private void refreshView_5() {
        if (airType5Device == null) {
            return;
        }
        setTempN = airType5Device.temp;
        iv_switch.setSelected(airType5Device.isPower());

    }

    private void refreshView_A() {
        if (airtypeADevice == null) {
            return;
        }
        setTempN = airtypeADevice.temp;
        iv_switch.setSelected(airtypeADevice.isPower());

    }

    private void cancelTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void initListener() {
        super.initListener();

        ActivityTitleManager.getInstance().goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.finish();
            }
        });

        iv_Up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stint()) return;
                T.showShort(mContext, "upteset");
                cancelTimer();
                isSearch = false;
                tv_start.setText("开始");
                iv_start.setSelected(false);
                pre();
            }
        });


        iv_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stint()) return;
                if (TextUtils.isEmpty(tv_air_name.getText().toString().trim())) {
                    T.showShort(mContext, "请选择空调品牌!");
                    return;
                }
                start();
            }
        });

        iv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stint()) return;
                cancelTimer();
                isSearch = false;
                tv_start.setText(getResources().getString(R.string.start));
                iv_start.setSelected(false);
                next();
            }
        });

        iv_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stint()) return;
//                if (TextUtils.isEmpty(tv_air_name.getText().toString().trim())) {
//                    T.showShort(mContext, "请选择空调品牌!");
//                    return;
//                }
                start();
            }
        });

        iv_tp_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stint()) return;
                if (setTempN < 16) {
                    setTempN = setTempN + 1;
                }
                System.out.println(setTempN);
                if (tran_flag) {
                    Constants.set_air[4] = (byte) setTempN;
                    Constants.set_air[12] = 1;
                    sendDataCor();
                } else if (air_5_flag) {
                    if (CmdBaseActivity.getInstance().isConnected()) {
                        airType5Device.temp = setTempN;
                        CMD08_ControlDevice cmd08 = new CMD08_ControlDevice(
                                airType5Device);
                        sendCMD(cmd08);
                    } else {
                        Util.showToast(mContext, "网络异常,请检查网络连接!");
                    }
                } else if (air_A_flag) {
                    if (CmdBaseActivity.getInstance().isConnected()) {
                        airtypeADevice.temp = setTempN;     //TODP
                        CMD08_ControlDevice cmd08 = new CMD08_ControlDevice(
                                airtypeADevice);
                       sendCMD(cmd08);
                    } else {
                        Util.showToast(mContext, "网络异常,请检查网络连接!");
                    }
                }
            }
        });


        iv_tp_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stint()) return;
                if (setTempN > 0) {
                    setTempN = setTempN - 1;
                }
                System.out.println(setTempN);
                if (tran_flag) {
                    Constants.set_air[4] = (byte) setTempN;
                    Constants.set_air[12] = 2;
                    sendDataCor();
                } else if (air_5_flag) {
                    if (CmdBaseActivity.getInstance().isConnected()) {
                        airType5Device.temp = setTempN;
                        CMD08_ControlDevice cmd08 = new CMD08_ControlDevice(
                                airType5Device);
                       sendCMD(cmd08);
                    } else {
                        Util.showToast(mContext, "网络异常,请检查网络连接!");
                    }
                } else if (air_A_flag) {
                    if (CmdBaseActivity.getInstance().isConnected()) {
                        airtypeADevice.temp = setTempN;
                        CMD08_ControlDevice cmd08 = new CMD08_ControlDevice(
                                airtypeADevice);
                       sendCMD(cmd08);
                    } else {
                        Util.showToast(mContext, "网络异常,请检查网络连接!");
                    }
                }
            }
        });

        iv_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stint()) return;
                if (tran_flag) {
                    if ((on & 0x01) >= 1) {
                        on = on - 1;
                    } else {
                        on = (on | 0x01);
                    }
                    Constants.set_air[13] = (byte) on;
                    Constants.set_air[12] = 0;
                    sendDataCor();
                } else if (air_5_flag) {
                    if (CmdBaseActivity.getInstance().isConnected()) {
                        airType5Device.setPower(!airType5Device.isPower());
                        CMD08_ControlDevice cmd08 = new CMD08_ControlDevice(
                                airType5Device);
                       sendCMD(cmd08);
                    } else {
                        Util.showToast(mContext, "网络异常,请检查网络连接!");
                    }

                } else if (air_A_flag) {
                    if (CmdBaseActivity.getInstance().isConnected()) {
                        airtypeADevice.setPower(!airtypeADevice.isPower());
                        CMD08_ControlDevice cmd08 = new CMD08_ControlDevice(
                                airtypeADevice);
                       sendCMD(cmd08);
                    } else {
                        Util.showToast(mContext, "网络异常,请检查网络连接!");
                    }

                }
            }
        });
    }

    private boolean stint() {
        if (currentGroup == -1) {
            T.showShort(mContext, "请点击空调品牌，选择空调品牌！");
            return true;
        }
        return false;
    }

    private void sendDataCor() {
        if (tranDevice == null) {
            T.showShort(mContext, "当前设备已被删除!");
            return;
        }
        byte macs[] = Util.hexStringToBytes(tranDevice.id);
        byte bytes[] = new byte[7 + Constants.set_air.length + 1];
        bytes[0] = macs[0];
        bytes[1] = macs[1];
        bytes[2] = macs[2];
        bytes[3] = macs[3];
        bytes[4] = macs[4];
        bytes[5] = macs[5];
        bytes[6] = macs[6];
        bytes[7] = 0x11;
        for (int i = 0; i < Constants.set_air.length; i++) {
            bytes[i + 8] = Constants.set_air[i];
        }

        CMDFactory cmdFactory = CMDFactory.getInstance();
        if (CmdBaseActivity.getInstance().isConnected()) {
            Log.e("czq", "控制devdata" + tranDevice.devdata);
            tranDevice.devdata = Util.bytesToHexString(Constants.set_air);
            Log.e("czq", "devdata2:" + tranDevice.devdata);
            CMD08_ControlDevice cmd08 = new CMD08_ControlDevice(tranDevice);
           sendCMD(cmd08);
        } else {
            Util.showToast(mContext, "网络异常,请检查网络连接!");
        }

    }

    private void start() {
        isSearch = !isSearch;
        if (isSearch) {
            if (!isAutoSearch) {
                currentIndex = -1;
            }
            tv_start.setText(getResources().getString(R.string.stop));
            iv_start.setSelected(true);
            startTimer();

        } else {
            tv_start.setText(getResources().getString(R.string.start));
            iv_start.setSelected(false);
            cancelTimer();

        }
    }

    private void startTimer() {
        if (timer == null) {
            timer = new Timer();
            TimerTask task = new TimerTask() {

                @Override
                public void run() {
                    next();

                }
            };
            timer.schedule(task, 0, 25 * 100);
        }
    }

    private void next() {
        currentIndex++;
        if (isAutoSearch) {// 如果自动搜索则组号要移动
            autoIndex++;
            if (autoIndex >= autoSum) {
                autoIndex = autoSum;
            }
            if (currentIndex >= Constants.air_type[currentGroup].length) {
                currentIndex = -1;
                currentGroup++;
                if (currentGroup >= Constants.air_type.length) {
                    currentGroup = Constants.air_type.length - 1;
                    currentIndex = Constants.air_type[currentGroup].length - 1;
                    isSearch = false;
                    cancelTimer();
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            tv_start.setText(StringUtils.getString(mContext, R.string.start));
                            iv_start.setSelected(false);
                        }
                    });
                }
            }
        } else { // 不是自动搜索则组号不变
            if (currentIndex >= Constants.air_type[currentGroup].length) {
                currentIndex = Constants.air_type[currentGroup].length - 1;
                isSearch = false;
                cancelTimer();
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        tv_start.setText(StringUtils.getString(mContext, R.string.start));
                        iv_start.setSelected(false);
                    }
                });

            }
        }
        is_pairing = true;
        if (tran_flag) {
            sendData();
        } else if (air_5_flag) {
            judgesend_5();
        } else if (air_A_flag) {
            judgesend_A();
        }
        setTempN = 9;

    }

    private void sendData() {
        // is_pairing = true;
        if (tranDevice == null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Util.showToast(mContext, "当前设备已被删除!");
                }
            });
            return;
        }
        if (currentIndex == -1) {
            currentIndex = 0;
        }

        Log.e("czq", tranDevice + "");
        Constants.match[1] = (byte) (0xff & Constants.air_type[currentGroup][currentIndex]);
        Constants.match[2] = (byte) (0xff & (Constants.air_type[currentGroup][currentIndex] >> 8));
        Constants.set_air[1] = Constants.match[1];
        Constants.set_air[2] = Constants.match[2];
        byte macs[] = Util.hexStringToBytes(tranDevice.id);
        byte bytes[] = new byte[7 + Constants.match.length + 1];
        bytes[0] = macs[0];
        bytes[1] = macs[1];
        bytes[2] = macs[2];
        bytes[3] = macs[3];
        bytes[4] = macs[4];
        bytes[5] = macs[5];
        bytes[6] = macs[6];
        bytes[7] = 0x11;
        for (int i = 0; i < Constants.match.length; i++) {
            bytes[i + 8] = Constants.match[i];
        }
        CMDFactory cmdFactory = CMDFactory.getInstance();
        UIUtils.postTaskSafely(new TimerTask() {
            @Override
            public void run() {
                ActivityTitleManager.getInstance().init(AirPairActivity.this);
                ActivityTitleManager.getInstance().changeTitle("空调匹配");
            }
        });

        if (CmdBaseActivity.getInstance().isConnected()) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    int x = currentIndex + 1;
                    ActivityTitleManager.getInstance().changeTitle("进度:" + x + "/"
                            + Constants.air_type[currentGroup].length
                            + " 匹配码:"
                            + Constants.air_type[currentGroup][currentIndex]);

                }
            });
            Constants.match[3] = 0x01;
            Constants.match[4] = 0x09;
            tranDevice.devdata = Util.bytesToHexString(Constants.match);

            Log.e("czq", "devdata:" + tranDevice.devdata);
            CMD08_ControlDevice cmd08 = new CMD08_ControlDevice(tranDevice);
           sendCMD(cmd08);
        } else {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    Util.showToast(mContext, "网络异常,请检查网络连接!");
                    tv_start.setText(getResources().getString(R.string.start));
                    iv_start.setSelected(false);
                    cancelTimer();
                    currentIndex = -1;
                }
            });

        }
    }

    private void pre() {
        currentIndex--;
        if (isAutoSearch) {// 如果自动搜索则组号要移动
            autoIndex--;
            if (autoIndex < 0) {
                autoIndex = -1;
            }
            if (currentIndex < 0) {
                currentIndex = -1;
                currentGroup--;
                if (currentGroup < 0) {
                    currentGroup = 0;
                    currentIndex = -1;
                }
            }
        } else { // 不是自动搜索则组号不变
            if (currentIndex < 0) {
                currentIndex = -1;
            }
        }
        if (tran_flag) {
            sendData();
        } else if (air_5_flag) {
            judgesend_5();
        } else if (air_A_flag) {
            judgesend_A();
        }
        setTempN = 9;
    }

    private void judgesend_5() {
        if (airType5Device == null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Util.showToast(mContext, "当前设备已被删除!");
                }
            });
            return;
        }
        if (currentIndex == -1) {
            currentIndex = 0;
        }
        UIUtils.postTaskSafely(new TimerTask() {
            @Override
            public void run() {
                ActivityTitleManager.getInstance().init(AirPairActivity.this);
                ActivityTitleManager.getInstance().changeTitle("空调匹配");
            }
        });

        int group = Constants.air_type[currentGroup][currentIndex];
        if (CmdBaseActivity.getInstance().isConnected()) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    int x = currentIndex + 1;
                    ActivityTitleManager.getInstance().changeTitle("进度:" + x + "/"
                            + Constants.air_type[currentGroup].length
                            + " 匹配码:"
                            + Constants.air_type[currentGroup][currentIndex]);

                }
            });
            airType5Device.group = group;
            airType5Device.mode = 1;
            airType5Device.setPower(true);
            airType5Device.temp = 9;
            CMD08_ControlDevice cmd08 = new CMD08_ControlDevice(airType5Device);
           sendCMD(cmd08);
        } else {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    Util.showToast(mContext, "网络异常,请检查网络连接!");
                    tv_start.setText(getResources().getString(R.string.start));
                    iv_start.setSelected(false);
                    cancelTimer();
                    currentIndex = -1;
                }
            });

        }

    }

    private void judgesend_A() {
        if (airtypeADevice == null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Util.showToast(mContext, "当前设备已被删除!");
                }
            });
            return;
        }
        if (currentIndex == -1) {
            currentIndex = 0;
        }
        UIUtils.postTaskSafely(new TimerTask() {
            @Override
            public void run() {
        ActivityTitleManager.getInstance().init(AirPairActivity.this);
        ActivityTitleManager.getInstance().changeTitle("空调匹配");
            }
        });

        int group = Constants.air_type[currentGroup][currentIndex];
        if (CmdBaseActivity.getInstance().isConnected()) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    int x = currentIndex + 1;
                    ActivityTitleManager.getInstance().changeTitle("进度:" + x + "/"
                            + Constants.air_type[currentGroup].length
                            + " 匹配码:"
                            + Constants.air_type[currentGroup][currentIndex]);
            LogUtils.i("进度:" + x + "/"
                    + Constants.air_type[currentGroup].length
                    + " 匹配码:"
                    + Constants.air_type[currentGroup][currentIndex]);
                }
            });
            airtypeADevice.group = group;
            airtypeADevice.mode = 1;
            airtypeADevice.setPower(true);
            airtypeADevice.sysflag = 1;
            airtypeADevice.temp = 9;
            airtypeADevice.ckhour = 0;
            airtypeADevice.ckminute = 0;
            CMD08_ControlDevice cmd08 = new CMD08_ControlDevice(airtypeADevice);
           sendCMD(cmd08);
        } else {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    Util.showToast(mContext, "网络异常,请检查网络连接!");
                    Log.e("czq", "0A");
                    tv_start.setText(getResources().getString(R.string.start));
                    iv_start.setSelected(false);
                    cancelTimer();
                    currentIndex = -1;
                }
            });

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        switch (requestCode) {
            case 1:
                try {
                    currntNum = data.getExtras().getInt("currentnum");
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();

                }

                if (currntNum > -1) {
                    currentGroup = currntNum;
                    isAutoSearch = false;
                    autoIndex = -1;
                    currentIndex = -1;
                    initAirData();
                    tv_air_name.setText("");
                    Log.e("czq1111", AirBrandActivity.str[currntNum]);
                    tv_air_name.setText(AirBrandActivity.str[currntNum]);
                }
                ActivityTitleManager.getInstance().changeTitle("进度:" + 0 + "/"
                        + Constants.air_type[currentGroup].length + " 匹配码:"
                        + Constants.air_type[currentGroup][0]);


                break;

            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initAirData() {
        if (isAutoSearch) {
            if (currentGroup == -1) {
                currentGroup = 0;
            }
            autoSum = 0;
            for (int i = 0; i < Constants.air_type.length; i++) {
                for (int j = 0; j < Constants.air_type[i].length; j++) {
                    autoSum++;
                }
            }
        }
    }

    @Override
    public void onReceiveCommand(ServerCommand cmd) {
//        super.onReceiveCommand(cmd);
        LogUtils.i("空调匹配收到:",cmd.toString());

        switch (cmd.CMDByte) {

            case CMDFF_ServerException.Command:

                CMDFF_ServerException cmdff_serverException = (CMDFF_ServerException) cmd;
                final String info = cmdff_serverException.info;
                UIUtils.postTaskSafely(new Runnable() {
                    @Override
                    public void run() {

//                        T.showShort(mContext,info);
                    }
                });
                break;
            case CMD07_ServerRespondDeviceStatus.Command:
                CMD07_ServerRespondDeviceStatus cmd07 = (CMD07_ServerRespondDeviceStatus) cmd;
                Device d07 = cmd07.status;
                    device =d07;
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

//                mLightAdapter.notifyDataSetChanged();
                break;
        }
    }


}
