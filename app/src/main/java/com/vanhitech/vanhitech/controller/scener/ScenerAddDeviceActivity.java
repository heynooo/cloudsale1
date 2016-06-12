package com.vanhitech.vanhitech.controller.scener;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalListDialog;
import com.flyco.dialog.widget.popup.base.BasePopup;
import com.lidroid.xutils.BitmapUtils;
import com.vanhitech.protocol.cmd.ClientCommand;
import com.vanhitech.protocol.cmd.ServerCommand;
import com.vanhitech.protocol.cmd.client.CMD04_GetAllDeviceList;
import com.vanhitech.protocol.cmd.client.CMD08_ControlDevice;
import com.vanhitech.protocol.cmd.client.CMD36_AddSceneDevice;
import com.vanhitech.protocol.cmd.client.CMD3A_ModifySceneDevice;
import com.vanhitech.protocol.cmd.server.CMD03_ServerLoginRespond;
import com.vanhitech.protocol.cmd.server.CMD05_ServerRespondAllDeviceList;
import com.vanhitech.protocol.cmd.server.CMD09_ServerControlResult;
import com.vanhitech.protocol.cmd.server.CMD2F_ServerAddSceneResult;
import com.vanhitech.protocol.cmd.server.CMD37_ServerAddSceneDeviceResult;
import com.vanhitech.protocol.cmd.server.CMDFC_ServerNotifiOnline;
import com.vanhitech.protocol.cmd.server.CMDFF_ServerException;
import com.vanhitech.protocol.object.Power;
import com.vanhitech.protocol.object.device.AirTypeADevice;
import com.vanhitech.protocol.object.device.Device;
import com.vanhitech.protocol.object.device.LightCDevice;
import com.vanhitech.protocol.object.device.LightCWDevice;
import com.vanhitech.protocol.object.device.LightRGBDevice;
import com.vanhitech.protocol.object.device.SceneDevice;
import com.vanhitech.vanhitech.R;
import com.vanhitech.vanhitech.base.BaseActivityController;
import com.vanhitech.vanhitech.base.CmdBaseActivity;
import com.vanhitech.vanhitech.bean.Place;
import com.vanhitech.vanhitech.bean.dbSceneDeviceInfo;
import com.vanhitech.vanhitech.conf.Constants;
import com.vanhitech.vanhitech.manager.ActivityTitleManager;
import com.vanhitech.vanhitech.utils.AnimationController;
import com.vanhitech.vanhitech.utils.ClickUtil;
import com.vanhitech.vanhitech.utils.DataUtils;
import com.vanhitech.vanhitech.utils.LogUtils;
import com.vanhitech.vanhitech.utils.StringUtils;
import com.vanhitech.vanhitech.utils.T;
import com.vanhitech.vanhitech.utils.UIUtils;
import com.vanhitech.vanhitech.utils.Util;
import com.vanhitech.vanhitech.views.GetRGBView;
import com.zhy.base.adapter.ViewHolder;
import com.zhy.base.adapter.recyclerview.DividerItemDecoration;
import com.zhy.base.adapter.recyclerview.OnItemClickListener;
import com.zhy.base.adapter.recyclerview.support.SectionAdapter;
import com.zhy.base.adapter.recyclerview.support.SectionSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建者     heyn
 * 创建时间   2016/3/18 14:42
 * 描述	      ${情景模式添加设备}
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class ScenerAddDeviceActivity extends BaseActivityController {

    private RecyclerView mRecyclerView;
    private List<String> mDatas = new ArrayList<>();
    private List<Device> mDevices;
    private BitmapUtils mBitmapUtils;
    private List<Place> mPlace;
    private String mSceneid;
    private boolean mEnabled;
    private SceneDevice mSceneDevice;
    private CMD37_ServerAddSceneDeviceResult mCmd37;
    private SectionAdapter<Device> mAdapter;
    private List<dbSceneDeviceInfo> mDbSceneDeviceInfoList;
    private List<String> mDeviceid;
    private List<Power> mPowers;
    private int mSpeedColor;
    private aIrCustomPop mAIrCustomPop;
    private rgbCwDeviceCustomPop mRgbCwDeviceCustomPop;
    private LightRGBDevice mRgblight;
    private LightRGBDevice mLightRGBDevice;
    private LightCDevice mLightCDevice;
    private AirTypeADevice mAirTypeADevice;
    private int mSetTempN;
    private int mPosition;
    private String mScenename;


    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_scener_adddevice);
        ActivityTitleManager.getInstance().init(this);
        ActivityTitleManager.getInstance().showCommonTitle();
        ActivityTitleManager.getInstance().changeTitle("添加设备");
        ActivityTitleManager.getInstance().changehelpText("");

        mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);


    }

    private void sendCmdSaddDevice(ClientCommand cmd04_getAllDeviceList) {
        CmdBaseActivity.getInstance().mHelper.setCommandListener(ScenerAddDeviceActivity.this);
        CmdBaseActivity.getInstance().sendCmd(cmd04_getAllDeviceList);
    }

    private void again() {
        CmdBaseActivity.getInstance().closeSocket();//TODO closeServer
        CmdBaseActivity.getInstance().starToConnectServer();
//        CmdBaseActivity.getInstance().mHelper.setCommandListener(ScenerAddDeviceActivity.this);
    }

    private void sendCmd36(Device device) {
        CMD36_AddSceneDevice cmd36 = new CMD36_AddSceneDevice(mSceneDevice, device);
        CmdBaseActivity.getInstance().mHelper.setCommandListener(ScenerAddDeviceActivity.this);
        CmdBaseActivity.getInstance().sendCmd(cmd36);
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

    @Override
    public void initData() {
        super.initData();
        getIntenData();
        dbData();
        List<Device> temp = DataUtils.getInstance().getAllDevice();
        mDevices = new ArrayList<>();
        if (temp.size() != 0) {
            for (Device device : temp) {
                if (!isExist(device))
                    mDevices.add(device);
            }
        }

        //发下网络命令 更新下 重新连接 发cmd4
        if (mDevices.size() != 0)
            sendCmdSaddDevice(new CMD08_ControlDevice(mDevices.get(0)));
        again();

        mPlace = DataUtils.getInstance().getcrePlaceSave();//mDatas  数据


    }

    private void dbData() {
        mDbSceneDeviceInfoList = DataUtils.getInstance().getSceneDevice(mSceneid);
        List<String> sceneids = new ArrayList<>();
        mDeviceid = new ArrayList<>();
        if (mDbSceneDeviceInfoList != null)
            for (dbSceneDeviceInfo sdinfo : mDbSceneDeviceInfoList) {
                mDeviceid.add(sdinfo.deviceid);
            }

    }

    private void getIntenData() {
        Bundle bundle = this.getIntent().getExtras();
        mSceneid = bundle.getString("sceneid");
        mEnabled = bundle.getBoolean("enabled");
        mScenename = bundle.getString("scenename");
        if (mScenename != null)
            ActivityTitleManager.getInstance().changeTitle(mScenename + "添加设备");
        mSceneDevice = new SceneDevice(mSceneid, mEnabled);

//        ActivityTitleManager.getInstance().goback.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openActivity(ScenerDeviceActivity.class, mSceneDevice, mScenename, mActivity, true);
//            }
//        });

    }


    @Override
    public void initEvent() {
        super.initEvent();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        SectionSupport<Device> sectionSupport = new SectionSupport<Device>() {
            @Override
            public int sectionHeaderLayoutId() {
                return R.layout.scener_header;
            }

            @Override
            public int sectionTitleTextViewId() {
                return R.id.id_header_title;
            }

            @Override
            public String getTitle(Device device) {
                return device.place;
            }

        };
        mAdapter = new SectionAdapter<Device>(this, R.layout.item_homelist1, mDevices, sectionSupport) {
            @Override
            public void convert(ViewHolder holder, Device device) {
                holder.setText(R.id.item_homelist_status, device.name);
                holder.setBackgroundRes(R.id.item_homelist_icon, intIcon(device));
                holder.setText(R.id.item_homelist_name, device.name);

            }
        };
        mAdapter.setOnItemClickListener(getOnItemClickListener());
        mRecyclerView.setAdapter(mAdapter);
    }

    @NonNull
    private OnItemClickListener<Device> getOnItemClickListener() {
        return new OnItemClickListener<Device>() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Device device, int position) {
                if (ClickUtil.isFastDoubleClick()) {
                    return;
                }
                mPosition = position;
                mDevice = device;
                evenPopw(device);
//                sendCmd36(device);
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Device device, int position) {
                return false;
            }


        };
    }

    /**
     * id0="00";       //中控
     * id1="01";       //2.4G插座
     * id2="02";       //2.4G墙壁开关
     * id3="03";       //2.4G灯开关控制  !
     * id4="04";       //2.4G灯开关及调光控
     * id5="05";       //2.4G空调万用遥控器
     * id6="06";       //2.4G RGB灯控制
     * id7="07";       //WIFI电视锁及控制中心
     * id8="08";       //2.4G遥控器
     * id9="09";       //WIFI控制中心
     * idA="0A";       //空调功率插头+遥控器
     * idB="0B";       //2.4G RGB灯控制  !
     * idC="0C";       //2.4G RGB灯控制  !
     * idD="0D";       //2.4G冷暖灯开关及调光控制 !
     * idE="0E";       //机顶盒控制中心
     * idF="0F";       //2.4G RGB灯控制  !
     * id10="10";      //2.4G窗帘开关控制器
     * id11="11";      //2.4G电饭煲
     * id12="12";      //2.4G环境检测仪
     * id13="13";      //待计量插座
     * id14="14";      //中央空调控制面板
     * id15="15";      //带中继2.4G插座
     * id7c="7c";      //一键配置
     *
     * @param device
     */
    private void evenPopw(Device device) {
        int type = device.type;
        if (type == 0) {
//        } else if (type==1) {////2.4G插座
        } else if (type == 2) {//墙壁开关
            NormalListDialog(device);
//        } else if (type==3) {//2.4G灯开关控制  !
//        } else if (type==4) {
        } else if (type == 5 || type == 10) {//2.4G空调万用遥控器
            mAIrCustomPop = new aIrCustomPop(mContext, mDevice);
            mAIrCustomPop.setCanceledOnTouchOutside(false);
            mAIrCustomPop.showAtLocation(UIUtils.getScreenWidth(mContext), UIUtils.getScreenHeight(mContext));
            mAIrCustomPop
                    .offset(50, 100)
                    .dimEnabled(false)
                    .show();
//        } else if (type==6) {
//        } else if (type==7) {
//        } else if (type==8) {
        } else if (type == 9) {   //中控
//        } else if (type==10) {// //空调功率插头+遥控器
//        } else if (type==11) { //2.4G RGB灯控制  !
//        } else if (type==12) { //2.4G RGB灯控制  !
//        } else if (type==13) {//2.4G冷暖灯开关及调光控制 !
//        } else if (type==14) {//2.4G RGB灯控制
//        } else if (type==15) {//2.4G RGB灯控制  ! //TODO
//        } else if (type==16) {//窗帘
//        } else if (type==17) {
//        } else if (type==18) {
//        } else if (type==19) {
//        } else if (type==20) {
        } else {
            mRgbCwDeviceCustomPop = new rgbCwDeviceCustomPop(mContext, mDevice);
            mRgbCwDeviceCustomPop.setCanceledOnTouchOutside(false);
            mRgbCwDeviceCustomPop.showAtLocation(UIUtils.getScreenWidth(mContext), UIUtils.getScreenHeight(mContext));
            mRgbCwDeviceCustomPop
                    .offset(50, 100)
                    .dimEnabled(false)
                    .show();
        }
    }

    private scenerDeviceAdapter mScenerDeviceAdapter;
    private NormalListDialog mDialog;

    private void NormalListDialog(Device device) {//mMenuItems   数据
        mPowers = device.power;
        mScenerDeviceAdapter = new scenerDeviceAdapter(mContext, mPowers);
        mDialog = new NormalListDialog(mContext, mScenerDeviceAdapter);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.title("多路设备  ")//
                .layoutAnimation(null)
                .dimEnabled(false)
                .dividerColor(Color.parseColor("#E1E1E1"))
                .show();
        mDialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPowers.set(position, new Power(position, !mPowers.get(position).on));
//                mDialog.dismiss();

            }
        });
    }

    /* popw#//rgb+cw设备 */
    private class rgbCwDeviceCustomPop extends BasePopup<rgbCwDeviceCustomPop> {
        private LinearLayout mScenerCwRgbLl, mScenerParting1,
                mScenerParting2,
                mScenerParting3,
                mScenerParting4,
                mScenerParting5;
        private LinearLayout mScenerLlColour;
        private CheckBox mScenerCbColor;
        private CheckBox mScenerCbWhite;
        private LinearLayout mScenerLlOpenclose;
        private CheckBox mScenerCbOpen;
        private CheckBox mScenerCbClose;
        private LinearLayout mScenerLlRgb;
        private GetRGBView mGetColorV;
        private LinearLayout mScenerLlBrightness;
        private SeekBar mSeekLight;
        private LinearLayout mScenerLlMode;
        private LinearLayout mLlMode;
        private Button mScenerArrorL;
        private TextView mScenerTvMode;
        private TextView mIsspeed;
        private TextView mScenerTitle;
        private Button mScenerArrorR;
        private LinearLayout mScenerLlSpeed;
        private SeekBar mSeekColorTemp;
        private TextView mScenerCwRgbCancel;
        private TextView mScenerCwRgbSave;
        private Device mDevice;
        private Boolean isRgbCw = true;
        private int mLight;
        private int mMode;
        private LightCWDevice mLightCWDevice;
        private int[] mRgb;
        private boolean mBpower;

        public void rgbCwDeviceCustomPop(int num) {
            mScenerCbColor.setChecked(num == 0);
            mScenerCbWhite.setChecked(num == 1);
            //setChecked 设置选中状态
        }

        public void openCloseCustomPop(int num) {
            mScenerCbOpen.setChecked(num == 1);
            mScenerCbClose.setChecked(num == 0);
        }

        public rgbCwDeviceCustomPop(Context context, Device device) {
            super(context);
            mDevice = device;
            mIsPopupStyle = true;
        }

        public void gradationDevice(Boolean flag) {
            if (flag) {
                AnimationController.show(mScenerLlRgb);
                AnimationController.show(mScenerLlMode);
                //更换背景

//                mScenerCwRgbLl.setBackground(getResources().getDrawable(R.mipmap.pop_scener_power_cw_rgb_ture));
                mIsspeed.setText("速度");
            } else {
                AnimationController.hide(mScenerLlRgb);
                AnimationController.hide(mScenerLlMode);
                mIsspeed.setText("色温");
//                mScenerCwRgbLl.setBackground(getResources().getDrawable(R.mipmap.pop_scener_power_cw_rgb_flase));
                //更换背景
            }
        }


        @Override
        public View onCreatePopupView() {

            View inflate = View.inflate(mContext, R.layout.pop_scener_cw_rgb_wdevice1, null);
            mScenerCwRgbLl = (LinearLayout) inflate.findViewById(R.id.scener_cw_rgb_ll);
            mScenerLlColour = (LinearLayout) inflate.findViewById(R.id.scener_ll_colour);
            mScenerCbColor = (CheckBox) inflate.findViewById(R.id.scener_cb_color);
            mScenerCbWhite = (CheckBox) inflate.findViewById(R.id.scener_cb_white);
            mScenerLlOpenclose = (LinearLayout) inflate.findViewById(R.id.scener_ll_openclose);
            mScenerCbOpen = (CheckBox) inflate.findViewById(R.id.scener_cb_open);
            mScenerCbClose = (CheckBox) inflate.findViewById(R.id.scener_cb_close);
            mScenerLlRgb = (LinearLayout) inflate.findViewById(R.id.scener_ll_rgb);
            mGetColorV = (GetRGBView) inflate.findViewById(R.id.getColorV);
            mScenerLlBrightness = (LinearLayout) inflate.findViewById(R.id.scener_ll_brightness);
            mSeekLight = (SeekBar) inflate.findViewById(R.id.seek_light);
            mScenerLlMode = (LinearLayout) inflate.findViewById(R.id.scener_ll_mode);
            mLlMode = (LinearLayout) inflate.findViewById(R.id.ll_mode);
            mScenerArrorL = (Button) inflate.findViewById(R.id.scener_arror_l);
            mScenerTvMode = (TextView) inflate.findViewById(R.id.scener_tv_mode);
            mScenerArrorR = (Button) inflate.findViewById(R.id.scener_arror_r);
            mScenerLlSpeed = (LinearLayout) inflate.findViewById(R.id.scener_ll_speed);
            mSeekColorTemp = (SeekBar) inflate.findViewById(R.id.seek_color_temp);
            mScenerCwRgbCancel = (TextView) inflate.findViewById(R.id.scener_cw_rgb_cancel);
            mScenerCwRgbSave = (TextView) inflate.findViewById(R.id.scener_cw_rgb_save);
            mScenerTitle = (TextView) inflate.findViewById(R.id.scemer_title);
            mIsspeed = (TextView) inflate.findViewById(R.id.scener_is_tv);
            mScenerParting1 = (LinearLayout) inflate.findViewById(R.id.scener_parting_1);
            mScenerParting2 = (LinearLayout) inflate.findViewById(R.id.scener_parting_2);
            mScenerParting3 = (LinearLayout) inflate.findViewById(R.id.scener_parting_3);
            mScenerParting4 = (LinearLayout) inflate.findViewById(R.id.scener_parting_4);
            mScenerParting5 = (LinearLayout) inflate.findViewById(R.id.scener_parting_5);
            return inflate;
        }


        @Override
        public void setUiBeforShow() {
            displayInterface();
            mScenerCwRgbSave.setText("添加");
            /*
            应该是显示初始数据
             */


            //1 rgb cw 切换
            mScenerCbColor.setOnClickListener(new View.OnClickListener() {//rgb
                @Override
                public void onClick(View v) {
                    if (mDevice.type == 11) {
                        rgbCwDeviceCustomPop(0);
                        gradationDevice(true);
                        isRgbCw = true;
                    } else {

                    }

                }
            });

            mScenerCbWhite.setOnClickListener(new View.OnClickListener() {//cwd
                @Override
                public void onClick(View v) {
                    if (mDevice.type == 11) {
                        rgbCwDeviceCustomPop(1);
                        gradationDevice(false);
                        isRgbCw = false;
                    }
                }
            });

            //2 开关 判断是rgb还是cw
            mScenerCbOpen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openCloseCustomPop(1);
                    mBpower = true;
                    if (mDevice.type == 11) {
                        mDevice.setPower(true);
                    } else {
                        mDevice.setPower(true);
                    }
                }
            });

            mScenerCbClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBpower = false;
                    openCloseCustomPop(0);
                    if (mDevice.type == 11) {
                        mDevice.setPower(false);
                    } else {
                        mDevice.setPower(false);
                    }
                }
            });
            //3 rgb 控制
            mGetColorV.setRgbListener(new GetRGBView.GetRGBListener() {
                @Override
                public void getRgbTouchDown() {

                }

                @Override
                public void getRgbTouchMove() {

                }

                @Override
                public void getRgbTouchUp() {
                    mRgb = mGetColorV.getRGB();
                    if (mDevice.type == 11) {

                    } else {
                        mRgblight.r = mRgb[0];
                        mRgblight.g = mRgb[1];
                        mRgblight.b = mRgb[2];
                    }
                }
            });
            //4 亮度控制
            mSeekLight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    mLight = (int) (seekBar.getProgress() * (0.15));
                    if (mDevice.type == 11) {
                    } else if (mDevice.type == 6 || mDevice.type == 12 || mDevice.type == 15) {//// TODO: 2016/5/14 1650
                        mLightRGBDevice = (LightRGBDevice) mDevice;
                        mLightRGBDevice.brightness2 = mLight;
//                                    mLightCWDevice
                    } else if (mDevice.type == 4) {
                        mLightCDevice = (LightCDevice) mDevice;
                        mLightCDevice.light = mLight;
                    } else if (mDevice.type == 13) {
                        mLightCWDevice = (LightCWDevice) mDevice;
                        mLightCWDevice.brightness = mLight;
                    }
                }
            });
            //5 模式选择  0-15 rgb  都有 暖白没有模式 应该隐藏
            mScenerArrorL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    modeCount(-1);
                }
            });
            mScenerArrorR.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    modeCount(1);
                }
            });
            //6 速度 cw灯应该不带速度  应该色温度 ,代码更改字内容
            mSeekColorTemp.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
                    mSpeedColor = (int) (seekBar.getProgress() * (0.15));
                    if (mDevice.type == 11) {
                    } else if (mDevice.type == 13) {
                        mLightCWDevice = (LightCWDevice) mDevice;
                        mLightCWDevice.colortemp = mSpeedColor;
                    } else if (mDevice.type == 6 || mDevice.type == 12 || mDevice.type == 15) {
                        mLightRGBDevice = (LightRGBDevice) mDevice;
                        mLightRGBDevice.freq = mSpeedColor;
                    }
                }
            });

            //1 取消 设置成最开始的数据 发送  2保存 销毁popw
            mScenerCwRgbCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDevice.type == 11) {
                        String[] str;
                        str = oldRgbCwData();
                        if (isRgbCw) {
                            int rgb[] = new int[3];
                            rgb[0] = Integer.parseInt(str[0]);
                            rgb[1] = Integer.parseInt(str[1]);
                            rgb[2] = Integer.parseInt(str[2]);
                            rgbSwitch(mDevice.power.get(0).on, rgb, str[16], str[8], str[4]);
                        } else {
                            lightSwich(mDevice.power.get(1).on, str[15], str[9]);
                        }
                    } else {

                    }
                    mRgbCwDeviceCustomPop.dismiss();
                }
            });
            mScenerCwRgbSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mDevice.type == 11) {
                        if (isRgbCw) {
                            removeList();
                            rgbSwitch(mBpower, mRgb, mLight + "", mMode + "", mSpeedColor + "");
                        } else {
                            removeList();
                            lightSwich(mBpower, mLight + "", mSpeedColor + "");
                        }
                    } else {
                        removeList();
                        sendCmd36(mDevice);
                    }
                    mRgbCwDeviceCustomPop.dismiss();
                }
            });

        }


        public void modeCount(int i) {

            if (mDevice.type == 11) {
                LightRGBDevice rgblight = (LightRGBDevice) mDevice;
                mMode = rgblight.mode;
                mMode = mMode + i;
                if (mMode == -1) {
                    mMode = 15;
                }
                if (mMode == 16) {
                    mMode = 0;
                }
                rgbSwitch(true, mRgb, mLight + "", mMode + "", mSpeedColor + "");
            } else if (mDevice.type == 13) {
                mLightRGBDevice = (LightRGBDevice) mDevice;
                mMode = mLightRGBDevice.mode;
                mMode = mMode + i;
                if (mMode == -1) {
                    mMode = 15;
                }
                if (mMode == 16) {
                    mMode = 0;
                }
                mLightRGBDevice.mode = mMode;
            }
        }

        /* cw灯操作 */
        private void lightSwich(Boolean flag, String brighteness1, String colortemp) {
            List<com.vanhitech.protocol.object.Power> powers = new ArrayList<com.vanhitech.protocol.object.Power>();
            com.vanhitech.protocol.object.Power p1 = new com.vanhitech.protocol.object.Power(1, false);
            com.vanhitech.protocol.object.Power p0 = new com.vanhitech.protocol.object.Power(0, flag);
            powers.add(p0);
            powers.add(p1);
            mDevice.setPowers(powers);
            LightRGBDevice rgblight = (LightRGBDevice) mDevice;
            if (!StringUtils.isEmpty(brighteness1))
                rgblight.brightness1 = Integer.parseInt(brighteness1);
            if (!StringUtils.isEmpty(colortemp))
                rgblight.colortemp = Integer.parseInt(colortemp);
            sendCmd36(mDevice);
        }

        /* rgb灯操作 */
        private void rgbSwitch(Boolean flag, int[] color, String brightness2, String mode, String freq) {
            List<Power> powers = new ArrayList<Power>();
            Power p1 = new Power(1, flag);
            Power p0 = new Power(0, false);
            powers.add(p0);
            powers.add(p1);
            mDevice.setPowers(powers);
            LightRGBDevice rgblight = (LightRGBDevice) mDevice;
            if (color != null && color.length > 0) {
                rgblight.r = color[0];
                rgblight.g = color[1];
                rgblight.b = color[2];
            }
            if (!StringUtils.isEmpty(brightness2))
                rgblight.brightness2 = Integer.parseInt(brightness2);//TODO
            if (!StringUtils.isEmpty(mode))
                rgblight.mode = Integer.parseInt(mode);
            sendCmd36(mDevice);
        }

        private void displayInterface() {
            /*
            根据类型 显示控件
             */
            hidedisplay();

        }

        private void hidedisplay() {//TODO
            if (mDevice.isPower()) {
                openCloseCustomPop(1);
            } else {
                openCloseCustomPop(0);
            }
            if (StringUtils.isNull(mDevice.name))
//                return;
                mScenerTitle.setText("");

            //插座 窗帘 灯控盒
            if (mDevice.type == 1 || mDevice.type == 2 || mDevice.type == 3 || mDevice.type == 16) {
                //rbg 切换层
                AnimationController.hide(mScenerLlColour);
                //rgb 控制层
                AnimationController.hide(mScenerLlRgb);
                //亮度层
                AnimationController.hide(mScenerLlBrightness);
                //模式层
                AnimationController.hide(mScenerLlMode);
                //速度层
                AnimationController.hide(mScenerLlSpeed);
                //更换背景
//                mScenerCwRgbLl.setBackground(getResources().getDrawable(R.mipmap.pop_scener_openclose_bg));
                AnimationController.hide(mScenerParting1);
                AnimationController.hide(mScenerParting3);
                AnimationController.hide(mScenerParting4);
                mScenerTitle.setText(mDevice.name);
            }
            //调节亮度的灯
            if (mDevice.type == 4) {
                mScenerTitle.setText(mDevice.name + "4");
                //
                mLightCDevice = (LightCDevice) mDevice;
                AnimationController.hide(mScenerLlColour);
                AnimationController.hide(mScenerLlRgb);
                AnimationController.hide(mScenerLlMode);
                AnimationController.hide(mScenerLlSpeed);
//                mScenerCwRgbLl.setBackground(getResources().getDrawable(R.mipmap.pop_scener_power_cw1));
                AnimationController.hide(mScenerParting1);
                AnimationController.hide(mScenerParting3);
                if (mDevice.isPower()) {
                    openCloseCustomPop(1);
                } else {
                    openCloseCustomPop(0);
                }
                mSeekLight.setProgress(mLightCDevice.light);
            }
            //纯rgb设备
            if (mDevice.type == 6 || mDevice.type == 12 || mDevice.type == 15) {
                mScenerTitle.setText(mDevice.name + "4" + "12" + "15");
                mLightRGBDevice = (LightRGBDevice) mDevice;
                AnimationController.hide(mScenerLlColour);
//                AnimationController.hide(mScenerLlSpeed);
//                mScenerCwRgbLl.setBackground(getResources().getDrawable(R.mipmap.pop_scener_power_rgb));
                mSeekLight.setProgress((int) (mLightRGBDevice.brightness2 / (0.15)));
                AnimationController.hide(mScenerParting1);
                mScenerTvMode.setText(mLightRGBDevice.mode + "");
                mSeekColorTemp.setProgress((int) (mLightRGBDevice.freq / (0.15)));
            }
            //纯cw灯
            if (mDevice.type == 13) {
                mScenerTitle.setText(mDevice.name + "13");
                mLightCWDevice = (LightCWDevice) mDevice;
                AnimationController.hide(mScenerLlColour);
                AnimationController.hide(mScenerLlRgb);
                AnimationController.hide(mScenerLlMode);
                mIsspeed.setText("色温");
//                mScenerCwRgbLl.setBackground(getResources().getDrawable(R.mipmap.pop_scener_power_cw2));
                AnimationController.hide(mScenerParting1);
                AnimationController.hide(mScenerParting3);
                mSeekLight.setProgress((int) (mLightCWDevice.brightness / (0.15)));
                mSeekColorTemp.setProgress((int) (mLightCWDevice.colortemp / (0.15)));
            }
        }

        public String[] oldRgbCwData() {
            if (mDevice.type == 11)
                mRgblight = (LightRGBDevice) mDevice;
            String oldr = mRgblight.r + "";
            String oldg = mRgblight.g + "";
            String oldb = mRgblight.b + "";
            String oldfirmve = mRgblight.firmver + "";
            String oldfreq = mRgblight.freq + "";
            String oldpid = mRgblight.pid + "";
            String oldtype = mRgblight.type + "";
            String oldonline = mRgblight.online + "";
            String oldmode = mRgblight.mode + "";
            String oldcolortemp = mRgblight.colortemp + "";
            String oldsubtyp = mRgblight.subtype + "";
            String oldname = mRgblight.name + "";
            String oldreserv = mRgblight.reserve + "";
            String oldgroupi = mRgblight.groupid + "";
            String oldplace = mRgblight.place + "";
            String oldbrightness1 = mRgblight.brightness1 + "";
            String oldbrightness2 = mRgblight.brightness2 + "";
            return new String[]{
                    oldr  //0
                    , oldg  //1
                    , oldb  //2
                    , oldfirmve //3
                    , oldfreq   //4
                    , oldpid    //5
                    , oldtype   //6
                    , oldonline //7
                    , oldmode   //8
                    , oldcolortemp  //9
                    , oldsubtyp //10
                    , oldname   //11
                    , oldreserv //12
                    , oldgroupi //13
                    , oldplace  //14
                    , oldbrightness1    //15
                    , oldbrightness2    //16
            };
        }
    }

    private void removeList() {
        mDevices.remove(mPosition);
        mAdapter.notifyDataSetChanged();
    }


    /* 多路电源的listview 适配器 */
    class scenerDeviceAdapter extends BaseAdapter {

        List<com.vanhitech.protocol.object.Power> list;
        Context context;


        public scenerDeviceAdapter(Context context, List<com.vanhitech.protocol.object.Power> list) {
            this.list = list;
            this.context = context;

        }

        @Override
        public int getCount() {

            return null != list ? list.size() : 0;
        }

        @Override
        public Object getItem(int position) {

            return null != list ? list.get(position) : 0;
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        ViewHolder holder = null;  //根视图

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_list_scener_selection, null);
                holder = new ViewHolder();
                holder.mName = (TextView) convertView.findViewById(R.id.name);
                holder.mCbOpen = (Button) convertView.findViewById(R.id.scener_bt_open);
                holder.mCbClose = (Button) convertView.findViewById(R.id.scener_bt_close);
                holder.mScemerTvLeft = ((TextView) convertView.findViewById(R.id.scemer_tv_left));
                holder.mScemerTvRight = ((TextView) convertView.findViewById(R.id.scemer_tv_right));
                holder.mScenerParting = (LinearLayout) convertView.findViewById(R.id.scener_parting);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final Power mPower = list.get(position);
            holder.mName.setText(mPower.way + "路");
            if (mPower.on) {
                setPower(1);
            } else {
                setPower(0);
            }
            holder.mCbOpen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPower.on = true;
                    setPower(1);
                    mScenerDeviceAdapter.notifyDataSetChanged();
                }
            });
            holder.mCbClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setPower(0);
                    mPower.on = false;
                    mScenerDeviceAdapter.notifyDataSetChanged();

                }
            });
                /*
                 holder.mCbOpen.setText("保存");
                holder.mCbClose.setText("取消");
                 */
            if (position == list.size() - 1) {
                AnimationController.show(holder.mScemerTvLeft);
                AnimationController.show(holder.mScemerTvRight);
                AnimationController.show(holder.mScenerParting);
            } else {
                AnimationController.hide(holder.mScemerTvLeft);
                AnimationController.hide(holder.mScemerTvRight);
                AnimationController.hide(holder.mScenerParting);
            }
            holder.mScemerTvLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mDialog.dismiss();
                }
            });
            holder.mScemerTvRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeList();
                    sendCmd3a(mDevice);
                    sendCmd36(mDevice);
                    mDialog.dismiss();
                }
            });

            return convertView;
        }

        private void setPower(int num) {
            holder.mCbOpen.setSelected(num == 1);
            holder.mCbClose.setSelected(num == 0);
        }

//        private void setPower(int num) {
//            holder.mCbOpen.setChecked(num == 1);
//            holder.mCbClose.setChecked(num == 0);
//        }


        class ViewHolder {
            public TextView mName;
            public Button mCbOpen;
            public Button mCbClose;
            public TextView mScemerTvLeft;
            public TextView mScemerTvRight;
            public LinearLayout mScenerParting;
        }
    }


    /* 空调的弹框*/
    private class aIrCustomPop extends BasePopup<aIrCustomPop> {
        private CheckBox mScenerCbOpen;
        private CheckBox mScenerCbClose;
        private Button mScenerArrorTemperatureL;
        private TextView mScenerTemperatureNum;
        private TextView mScenerTemperatureTv;
        private Button mScenerArrorTemperatureR;
        private Button mScenerArrorModeL;
        private TextView mScenerArrorModeTv;
        private Button mScenerArrorModeR;
        private Button mScenerAirWindL;
        private TextView mScenerAirWindTv;
        private Button mScenerAirWindR;
        private Button mScenerAirMovewindL;
        private TextView mScenerAirMovewindTv;
        private Button mScenerAirMovewindR;
        private Button mScenerAirCancel;
        private Button mScenerAirSave;


        public aIrCustomPop(Context context, Device device) {
            super(context);
            mIsPopupStyle = true;
        }

        @Override
        public View onCreatePopupView() {

            View inflate = View.inflate(mContext, R.layout.pop_scener_air, null);

            mScenerCbOpen = (CheckBox) inflate.findViewById(R.id.scener_cb_open);
            mScenerCbClose = (CheckBox) inflate.findViewById(R.id.scener_cb_close);
            mScenerArrorTemperatureL = (Button) inflate.findViewById(R.id.scener_arror_temperature_l);
            mScenerTemperatureNum = (TextView) inflate.findViewById(R.id.scener_temperature_num);
            mScenerTemperatureTv = (TextView) inflate.findViewById(R.id.scener_temperature_tv);
            mScenerArrorTemperatureR = (Button) inflate.findViewById(R.id.scener_arror_temperature_r);
            mScenerArrorModeL = (Button) inflate.findViewById(R.id.scener_arror_mode_l);
            mScenerArrorModeTv = (TextView) inflate.findViewById(R.id.scener_arror_mode_tv);
            mScenerArrorModeR = (Button) inflate.findViewById(R.id.scener_arror_mode_r);
            mScenerAirWindL = (Button) inflate.findViewById(R.id.scener_air_wind_l);
            mScenerAirWindTv = (TextView) inflate.findViewById(R.id.scener_air_wind_tv);
            mScenerAirWindR = (Button) inflate.findViewById(R.id.scener_air_wind_r);
            mScenerAirMovewindL = (Button) inflate.findViewById(R.id.scener_air_movewind_l);
            mScenerAirMovewindTv = (TextView) inflate.findViewById(R.id.scener_air_movewind_tv);
            mScenerAirMovewindR = (Button) inflate.findViewById(R.id.scener_air_movewind_r);
            mScenerAirCancel = (Button) inflate.findViewById(R.id.scener_air_cancel);
            mScenerAirSave = (Button) inflate.findViewById(R.id.scener_air_save);
            return inflate;
        }

        @Override
        public void setUiBeforShow() {
            mScenerAirSave.setText("添加");
            mAirTypeADevice = (AirTypeADevice) mDevice;
            mSetTempN = mAirTypeADevice.temp + 16;
            if (mSetTempN + "" == null) {
                mScenerTemperatureNum.setText(mSetTempN);
            }
            //事件处理
            //1 开关
            mScenerCbOpen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAirTypeADevice.setPower(true);
                    setPower(1);
                }
            });
            mScenerCbClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAirTypeADevice.setPower(false);
                    setPower(0);
                }
            });

            //2 温度(判断是否在线|是否打开)
            mScenerArrorModeL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {// -

                    mAirTypeADevice.mode = mAirTypeADevice.mode - 1;
                    if (mAirTypeADevice.mode == -1) {
                        mAirTypeADevice.mode = 0;
                    }
                    setMode(mAirTypeADevice.mode);
                }
            });
            mScenerArrorModeR.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {// +
                    mAirTypeADevice.mode = mAirTypeADevice.mode + 1;
                    if (mAirTypeADevice.mode == 5) {
                        mAirTypeADevice.mode = 0;
                    }
                    setMode(mAirTypeADevice.mode);
                }
            });
            //3 模式 风速
            mScenerAirWindL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {//-
                    mAirTypeADevice.speed = mAirTypeADevice.speed - 1;
                    if (mAirTypeADevice.speed == -1) {
                        mAirTypeADevice.speed = 3;
                    }
                    setWindSpeed(mAirTypeADevice.speed);

                }
            });
            mScenerAirWindR.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {//+
                    mAirTypeADevice.speed = mAirTypeADevice.speed + 1;
                    if (mAirTypeADevice.speed == 4) {
                        mAirTypeADevice.speed = 0;
                    }
                    setWindSpeed(mAirTypeADevice.speed);
                }
            });
            //4 摆风
            mScenerAirMovewindL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {//-
                    mAirTypeADevice.adirect = 0;
                    mAirTypeADevice.mdirect = 0;
                    setMoveWind(0);
                }
            });
            mScenerAirMovewindR.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAirTypeADevice.adirect = 1;
                    mAirTypeADevice.mdirect = 0;
                    setMoveWind(1);
                }
            });

            //5 保存取消
            //保存发送
            mScenerAirSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeList();
                    sendCmd36(mAirTypeADevice);
                    mAIrCustomPop.dismiss();
                }
            });
            mScenerAirCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAIrCustomPop.dismiss();
                }
            });
            //6 温度显示 mScenerTemperatureNum 16-32
            mScenerArrorTemperatureL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setAirTemperature(-1);
                }
            });
            mScenerArrorTemperatureR.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setAirTemperature(1);
                }
            });


        }

        private void setAirTemperature(int i) {

            mSetTempN = mSetTempN + i;
            if (mSetTempN < 16) {
                mSetTempN = 32;
            } else if (mSetTempN > 32) {
                mSetTempN = 16;
            }
            String temp = mSetTempN + "";
            mScenerTemperatureNum.setText(temp);
            mAirTypeADevice.temp = mSetTempN;
        }

        private void setMode(int mode2) {
            // TODO Auto-generated method stub
            if (mode2 == 0) {
                mScenerArrorModeTv.setText("自动");
            } else if (mode2 == 1) {
                mScenerArrorModeTv.setText("制冷");
            } else if (mode2 == 2) {
                mScenerArrorModeTv.setText("制热");
            } else if (mode2 == 3) {
                mScenerArrorModeTv.setText("送风");
            } else if (mode2 == 4) {
                mScenerArrorModeTv.setText("除湿");
            } else {
                mScenerArrorModeTv.setText("模式");
            }
        }

        private void setWindSpeed(int wind2) {
            // TODO Auto-generated method stub
            if (wind2 == 0) {
                mScenerAirWindTv.setText("高");
            } else if (wind2 == 1) {
                mScenerAirWindTv.setText("中");
            } else if (wind2 == 2) {
                mScenerAirWindTv.setText("低");
            } else if (wind2 == 3) {
                mScenerAirWindTv.setText("自动");
            } else {
                mScenerAirWindTv.setText("风速");
            }

        }

        private void setMoveWind(int checkMoveWind2) {
            // TODO Auto-generated method stub
            if (checkMoveWind2 == 0) {
                mScenerAirMovewindTv.setText("自动");
            } else if (checkMoveWind2 == 1) {
                mScenerAirMovewindTv.setText("手动");
            } else {
                mScenerAirMovewindTv.setText("摆风");
            }
        }


        private void setPower(int num) {
            mScenerCbOpen.setChecked(num == 1);
            mScenerCbClose.setChecked(num == 0);
        }

        private boolean isOpen() {
            if (!mAirTypeADevice.online || !mAirTypeADevice.power.get(0).on) {
                Util.showToast(mContext, "空调为关,或者离线，请打开空调！");
                return true;
            }
            return false;
        }

    }


    private int intIcon(Device device) {
        String ID = device.id.substring(0, 2);
        if (ID.equals(Constants.id1)) {
            return R.mipmap.scener01;
        } else if (ID.equals(Constants.id2)) {

        } else if (ID.equals(Constants.id3)) {//2.4G灯开关控制  !
            return R.mipmap.scener03;
        } else if (ID.equals(Constants.id4)) {

        } else if (ID.equals(Constants.id5)) {
            return R.mipmap.scener0a;
        } else if (ID.equals(Constants.id6)) {
            return R.mipmap.scener06;
        } else if (ID.equals(Constants.id7)) {
            return R.mipmap.scener00;
        } else if (ID.equals(Constants.id8)) {

        } else if (ID.equals(Constants.id9)) {
            return R.mipmap.scener00;
        } else if (ID.equals(Constants.idA)) {
            return R.mipmap.scener0a;
        } else if (ID.equals(Constants.idB)) { //2.4G RGB灯控制  !
            return R.mipmap.scener0b;
        } else if (ID.equals(Constants.idC)) { //2.4G RGB灯控制  !
            return R.mipmap.scener0b;
        } else if (ID.equals(Constants.idD)) {//2.4G冷暖灯开关及调光控制 !
            return R.mipmap.scener0b;
        } else if (ID.equals(Constants.idE)) {//2.4G RGB灯控制

        } else if (ID.equals(Constants.idF)) {//2.4G RGB灯控制  !
            return R.mipmap.scener06;
        } else if (ID.equals(Constants.id10)) {

        } else if (ID.equals(Constants.id11)) {

        } else if (ID.equals(Constants.id12)) {

        } else if (ID.equals(Constants.id13)) {
            return R.mipmap.scener01;
        } else if (ID.equals(Constants.id14)) {

        } else {
            //不处理
            return R.mipmap.scener01;
        }
        return R.mipmap.scener01;
    }
//
//    private Bitmap getImageFromAssetsFile(String fileName) {
//        Bitmap image = null;
//
//        AssetManager am = getResources().getAssets();
//
//        try {
//
//            InputStream is = am.open(fileName);
//            image = BitmapFactory.decodeStream(is);
//            is.close();
//        } catch (IOException e) {
//
//            e.printStackTrace();
//      8  }
//
//        return image;
//
//    }

    @Override
    public void onReceiveCommand(ServerCommand cmd) {
        super.onReceiveCommand(cmd);
        LogUtils.i("情景模式添加设备收到:" + cmd.toString());

        switch (cmd.CMDByte) {
            case CMDFC_ServerNotifiOnline.Command:
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
            case CMD2F_ServerAddSceneResult.Command:
                CMD2F_ServerAddSceneResult cmd2F = (CMD2F_ServerAddSceneResult) cmd;
                break;
            case CMD03_ServerLoginRespond.Command:
                CMD03_ServerLoginRespond cmd03 = (CMD03_ServerLoginRespond) cmd;
                cnd04();
                break;
            case CMD05_ServerRespondAllDeviceList.Command:
                CMD05_ServerRespondAllDeviceList cmd05 = (CMD05_ServerRespondAllDeviceList) cmd;
                getCmdData(cmd05.deviceList);
                break;
            case CMD09_ServerControlResult.Command:

                break;
            case CMD37_ServerAddSceneDeviceResult.Command:
                mCmd37 = (CMD37_ServerAddSceneDeviceResult) cmd;
                /*
                public CMD37_ServerAddSceneDeviceResult(boolean result, SceneDevice scenedev, Device ctrlinfo) {
                 */
                if (mCmd37.result) {
                    UIUtils.postTaskSafely(new Runnable() {
                        @Override
                        public void run() {
                            T.showShort(mContext, "添加成功");
                        }
                    });
                } else {
                    T.showShort(mContext, "添加失败");
                }

                break;

        }
    }

    private void getCmdData(List<Device> deviceList) {
        if (deviceList.size() > 0) {
            mDevices.clear();
            for (Device device : deviceList) {
                if (!isExist(device))
                    mDevices.add(device);
            }
            UIUtils.postTaskSafely(new Runnable() {
                @Override
                public void run() {
                    mAdapter.notifyDataSetChanged();
                }
            });

        }
    }

    private void cnd04() {
        CmdBaseActivity.getInstance().mHelper.setCommandListener(ScenerAddDeviceActivity.this);
        CMD04_GetAllDeviceList cmd4 = new CMD04_GetAllDeviceList();
        CmdBaseActivity.getInstance().sendCmd(cmd4);
    }

    /**
     * @param device
     */
    /* 发送控制命令 */
    private void sendCmd3a(Device device) {//TODO

        if (CmdBaseActivity.getInstance().isConnected()) {
            CMD3A_ModifySceneDevice cmd3a = new CMD3A_ModifySceneDevice(mSceneDevice, device);
            CmdBaseActivity.getInstance().mHelper.setCommandListener(ScenerAddDeviceActivity.this);
            CmdBaseActivity.getInstance().sendCmd(cmd3a);
        } else {
            Util.showToast(mContext, "网络异常,请检查网络连接!");
        }
    }


    /**
     * 判断是否已经添加过
     *
     * @param device
     * @return
     */
    public boolean isExist(Device device) {

        Boolean flag = false;
        for (String id : mDeviceid) {
            if (device.id.equals(id)) {
                flag = true;
            }
            if (flag) {
                return flag;
            }
        }
        return flag;
    }


}
