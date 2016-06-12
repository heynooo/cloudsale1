package com.vanhitech.vanhitech.controller.scener;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.animation.BaseAnimatorSet;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalListDialog;
import com.flyco.dialog.widget.popup.base.BasePopup;
import com.vanhitech.protocol.cmd.ClientCommand;
import com.vanhitech.protocol.cmd.ServerCommand;
import com.vanhitech.protocol.cmd.client.CMD06_QueryDeviceStatus;
import com.vanhitech.protocol.cmd.client.CMD38_DelSceneDevice;
import com.vanhitech.protocol.cmd.client.CMD3A_ModifySceneDevice;
import com.vanhitech.protocol.cmd.client.CMD3C_QuerySceneModeDevices;
import com.vanhitech.protocol.cmd.server.CMD07_ServerRespondDeviceStatus;
import com.vanhitech.protocol.cmd.server.CMD09_ServerControlResult;
import com.vanhitech.protocol.cmd.server.CMD3B_ServerModifySceneDeviceResult;
import com.vanhitech.protocol.cmd.server.CMD3D_ServerQuerySceneModeDevicesResult;
import com.vanhitech.protocol.cmd.server.CMDFC_ServerNotifiOnline;
import com.vanhitech.protocol.cmd.server.CMDFF_ServerException;
import com.vanhitech.protocol.object.Power;
import com.vanhitech.protocol.object.SceneDeviceInfo;
import com.vanhitech.protocol.object.device.AirDevice;
import com.vanhitech.protocol.object.device.AirType5Device;
import com.vanhitech.protocol.object.device.AirTypeADevice;
import com.vanhitech.protocol.object.device.Device;
import com.vanhitech.protocol.object.device.LightCDevice;
import com.vanhitech.protocol.object.device.LightCWDevice;
import com.vanhitech.protocol.object.device.LightRGBDevice;
import com.vanhitech.protocol.object.device.LockDoorDevice;
import com.vanhitech.protocol.object.device.SceneDevice;
import com.vanhitech.protocol.object.device.SmokeDetectorDevice;
import com.vanhitech.protocol.object.device.WaterHeaterDevice;
import com.vanhitech.vanhitech.R;
import com.vanhitech.vanhitech.SwipeMenulistview.PullToRefreshSwipeMenuListView;
import com.vanhitech.vanhitech.SwipeMenulistview.SwipeMenu;
import com.vanhitech.vanhitech.SwipeMenulistview.SwipeMenuCreator;
import com.vanhitech.vanhitech.SwipeMenulistview.SwipeMenuItem;
import com.vanhitech.vanhitech.base.BaseActivityController;
import com.vanhitech.vanhitech.base.CmdBaseActivity;
import com.vanhitech.vanhitech.base.MyApplication;
import com.vanhitech.vanhitech.bean.dbSceneDeviceInfo;
import com.vanhitech.vanhitech.manager.ActivityTitleManager;
import com.vanhitech.vanhitech.pulltorefresh.RefreshTime;
import com.vanhitech.vanhitech.utils.AnimationController;
import com.vanhitech.vanhitech.utils.ClickUtil;
import com.vanhitech.vanhitech.utils.DataUtils;
import com.vanhitech.vanhitech.utils.DevicNameUtils;
import com.vanhitech.vanhitech.utils.LogUtils;
import com.vanhitech.vanhitech.utils.StringUtils;
import com.vanhitech.vanhitech.utils.T;
import com.vanhitech.vanhitech.utils.UIUtils;
import com.vanhitech.vanhitech.utils.Util;
import com.vanhitech.vanhitech.views.GetRGBView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 创建者     heyn
 * 创建时间   2016/3/18 14:42
 * 描述	      ${定时器界面测试}
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class ScenerDeviceActivity extends BaseActivityController implements PullToRefreshSwipeMenuListView.IXListViewListener {

    private List<Device> mSceneDeviceList;
    private SceneDeviceAdapter mAdapter;
    private Handler mHandler;
    private Boolean isEditSave = false;
    private Activity mActivity;
    private PullToRefreshSwipeMenuListView mListView;
    private NormalListDialog mDialog;
    private List<com.vanhitech.protocol.object.Power> mPowers;
    private BaseAnimatorSet mBasIn;
    private BaseAnimatorSet mBasOut;
    private AirType5Device mAirType5Device;
    private AirDevice mAirDevice;
    private AirTypeADevice mAirTypeADevice;
    private LightCDevice mLightCDevice;
    private LightRGBDevice mLightRGBDevice;
    private LockDoorDevice mLockDoorDevice;
    private SmokeDetectorDevice mSmokeDetectorDevice;
    private WaterHeaterDevice mWaterHeaterDevice;
    private aIrCustomPop mAIrCustomPop;
    private rgbCwDeviceCustomPop mRgbCwDeviceCustomPop;
    private int mSpeedColor;
    private LightRGBDevice mRgblight;
    private int mSetTempN;
    private scenerDeviceAdapter mScenerDeviceAdapter;
    private SceneDevice mSceneDevice;
    private CMD3B_ServerModifySceneDeviceResult mCmd3b;
    private List<SceneDeviceInfo> mLis;
    private LinearLayout mAddDevicell;
    private Button mAddDevicebt;
    private String mSceneid;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_scener_device);
        mActivity = this;
        ActivityTitleManager.getInstance().init(ScenerDeviceActivity.this);
        ActivityTitleManager.getInstance().showCommonTitle();
        ActivityTitleManager.getInstance().changeTitle("模式");
        assignViews();
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

    private void assignViews() {
        mListView = (PullToRefreshSwipeMenuListView) findViewById(R.id.scener_device_list);
        mAddDevicell = (LinearLayout) findViewById(R.id.scenerdevice_ll_addmode);
        mAddDevicebt = (Button) findViewById(R.id.scenerdevice_addmode_bt);
    }

    private void isShowAddmodebt() {
        if (mSceneDeviceList.size() == 0) {
            AnimationController.show(mAddDevicell);

            mAddDevicebt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    openActivity(ScenerAddDeviceActivity.class, mSceneDevice, mName, mActivity, false);
                }
            });
        } else {
            AnimationController.hide(mAddDevicell);
        }
    }

    @Override
    public void initData() {
        super.initData();
        //id name
        mSceneDeviceList = new ArrayList<>();


        intenControll();

//        testData();
        isShowAddmodebt();

        //临时数据
        mSceneDeviceList = DataUtils.getInstance().getAllDevice();

        bufferData();


        mAdapter = new SceneDeviceAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setPullRefreshEnable(true);
        mListView.setPullLoadEnable(false);//上拉刷新
        mListView.setXListViewListener(this);
        mHandler = new Handler();

        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(mContext);
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
                // set item width
                openItem.setWidth(dp2px(66));
                // set item title
                openItem.setTitle("打开");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(mContext);
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(66));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        // set creator
        mListView.setMenuCreator(creator);
        // step 2. listener item click event
        mListView.setOnMenuItemClickListener(new PullToRefreshSwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
                Device item = mSceneDeviceList.get(position);
                switch (index) {
                    case 0:
                        System.out.println("open");
                        // open
//                        open(item);
                        break;
                    case 1:
                        // delete
                        delete(item, position);

                        break;
                }
            }
        });

        // set SwipeListener
        mListView.setOnSwipeListener(new PullToRefreshSwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
                System.out.println("onSwipeStart");

            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
                System.out.println("onSwipeEnd");
            }
        });

        // other setting
//         listView.setCloseInterpolator(new BounceInterpolator());
//        mListView.setCloseInterpolator(new BounceInterpolator());

        // test item long click
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(mContext, position + " long click", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        mListView.setOnItemClickListener(new scenerlistviewClickListener());


    }

    private void bufferData() {
        List<dbSceneDeviceInfo> mDbSceneDeviceInfoList = DataUtils.getInstance().getSceneDevice(mId);
        List<String> deviceids = new ArrayList<>();
        if (mDbSceneDeviceInfoList != null)
            for (dbSceneDeviceInfo sdinfo : mDbSceneDeviceInfoList) {
                deviceids.add(sdinfo.deviceid);
            }
        if (mSceneDeviceList != null) {
            List<Device> devices = new ArrayList<>();
            for (Device d : mSceneDeviceList) {
                if (isExist(d.id, deviceids))
                    devices.add(d);
            }
            mSceneDeviceList.clear();
            mSceneDeviceList.addAll(devices);
        }
    }


    public boolean isExist(String id, List<String> mids) {

        Boolean flag = false;
        for (String i : mids) {
            if (id.equals(id)) {
                flag = true;
            }
            if (flag) {
                return flag;
            }
        }
        return flag;
    }


    private void delete(Device item, int position) {
        /*
        private void delete(int position, SceneMode item) {
        DataUtils.getInstance().deleteSceneMode(item.id + "");
        //网络命令
        CMD30_DelScene cmd30 = new CMD30_DelScene(item.id);
        CmdBaseActivity.getInstance().sendCmd(cmd30);
        mSceneModeList.remove(position);
        isShowAddmodebt();
        mAdapter.notifyDataSetChanged();
    }
         */
//public CMD38_DelSceneDevice(String sceneid, String devid) {
        CMD38_DelSceneDevice cmd38 = new CMD38_DelSceneDevice(mId, item.id);
        DataUtils.getInstance().deletedbSceneDeviceInfo(mId + item.id);
        mSceneDeviceList.remove(position);
        CmdBaseActivity.getInstance().sendCmd(cmd38);
        isShowAddmodebt();
        mAdapter.notifyDataSetChanged();
    }

    private void intenControll() {
        Boolean flag = false;
        getConterIntent();
        getActivityIntent();
        if (!StringUtils.isEmpty(mId)) {
            sendCmd3c();
//            sendCmdsdevic(new CMD3C_QuerySceneModeDevices(mId));
            if (!StringUtils.isEmpty(mName)) {
                ActivityTitleManager.getInstance().changeTitle(mName + "模式");
            }
            if (!StringUtils.isEmpty(mSimageno + "")) {
            }
            mSceneDevice = new SceneDevice(mId, flag);
        }

    }

    private void getActivityIntent() {
        Bundle bundle = this.getIntent().getExtras();
        mSceneid = bundle.getString("sceneid");
        if (mSceneid != null) {
            mId = mSceneid;
        }
    }

    private void sendCmdsdevic(ClientCommand cmd30) {
        CmdBaseActivity.getInstance().mHelper.setCommandListener(ScenerDeviceActivity.this);
        CmdBaseActivity.getInstance().sendCmd(cmd30);
    }

    /**
     * 模拟测试数据
     */
    private void testData() {
        mSceneDeviceList = new ArrayList<>();
        //SceneMode
        //添加假数据测试
//        for (int i = 0; i < 20; i++) {
//            Device device = new Device("id" + i, i + "", "name" + i, "place" + i, true, null);
//            device.type=i;
//            mSceneDeviceList.add(device);
//        }
        Device device1 = new Device("id", "", "name", "place", true, null);
        mSceneDeviceList.add(device1);
        LightCDevice device2 = new LightCDevice("id", "2", "name", "place", true, null, 22);
        mSceneDeviceList.add(device2);
        LightCWDevice device3 = new LightCWDevice("id", "2", "name", "place");
        mSceneDeviceList.add(device3);
        LightRGBDevice device4 = new LightRGBDevice("id", "2", "name", "place", 11);
        mSceneDeviceList.add(device4);
        AirTypeADevice device5 = new AirTypeADevice();
        mSceneDeviceList.add(device5);
        Device device7 = new Device("id", "", "name", "place", true, null);
        device7.type = 2;
        mSceneDeviceList.add(device7);

        mPowers = new ArrayList<>();
       /*
       模拟数据 电源
        */
        com.vanhitech.protocol.object.Power power0 = new com.vanhitech.protocol.object.Power(0, false);
        com.vanhitech.protocol.object.Power power1 = new com.vanhitech.protocol.object.Power(1, false);
        com.vanhitech.protocol.object.Power power2 = new com.vanhitech.protocol.object.Power(2, true);
        com.vanhitech.protocol.object.Power power3 = new com.vanhitech.protocol.object.Power(3, false);
        com.vanhitech.protocol.object.Power power4 = new com.vanhitech.protocol.object.Power(4, false);
        com.vanhitech.protocol.object.Power power5 = new com.vanhitech.protocol.object.Power(5, false);
        mPowers.add(power0);
        mPowers.add(power1);
        mPowers.add(power2);
        mPowers.add(power3);
        mPowers.add(power4);
        mPowers.add(power5);
    }


    @Override
    public void initEvent() {
        super.initEvent();
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, mContext.getResources().getDisplayMetrics());
    }

    @Override
    public void onRefresh() {
        System.out.println("onRefresh");
        sendCmd3c();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());
                RefreshTime.setRefreshTime(mContext.getApplicationContext(), df.format(new Date()));
                onLoad();
            }
        }, 1);
    }

    private void sendCmd3c() {
        MyApplication.setIsSaveDevice(false);
        sendCmd(new CMD3C_QuerySceneModeDevices(mId), ScenerDeviceActivity.this);
    }

    private void sendCmd(CMD3C_QuerySceneModeDevices cmd3c, ScenerDeviceActivity listener) {
        if (CmdBaseActivity.getInstance().isConnected()) {
            CmdBaseActivity.getInstance().mHelper.setCommandListener(listener);
            CmdBaseActivity.getInstance().sendCmd(cmd3c);
        } else {
            Util.showToast(mContext, "网络异常,请检查网络连接!");
        }
    }

    private void onLoad() {

        new Handler().postDelayed(new Runnable() {//延迟2秒去更新
            public void run() {
//                mAdapter.notifyDataSetChanged();

            }
        }, 10);

        System.out.println("onLoad");
        mListView.setRefreshTime(RefreshTime.getRefreshTime(mContext.getApplicationContext()));
        mListView.stopRefresh();
        mListView.stopLoadMore();


    }

    @Override
    public void onLoadMore() {

    }

    class SceneDeviceAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return null != mSceneDeviceList ? mSceneDeviceList.size() : 0;
        }

        @Override
        public Device getItem(int position) {
            return mSceneDeviceList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_scener_device, null);
                new ViewHolder(convertView);
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            Device item = getItem(position);
//            holder.iv_icon.setImageDrawable(item.loadIcon(mContext.getPackageManager()));

            holder.mScenerDeviceName.setText(item.name);
            holder.mScenerDevicePlace.setText(item.place);
            if (item.online) {
                holder.mScenerDeviceStatus.setText("在线");
            } else {
                holder.mScenerDeviceStatus.setText("离线");
            }
            holder.mScenerDeviceIcon.setBackground(getResources().getDrawable(DevicNameUtils.intIcon(item)));
            if (position == mSceneDeviceList.size() - 1) {
                AnimationController.show(holder.mScenerAdddeviceBt);
            } else {
                AnimationController.hide(holder.mScenerAdddeviceBt);
            }
            holder.mScenerAdddeviceBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    openActivity(ScenerAddDeviceActivity.class, mSceneDevice, mName, mActivity, false);
                }
            });

            return convertView;
        }


        class ViewHolder {
            LinearLayout mItemScenerDeviceLl;
            ImageView mScenerDeviceIcon;
            LinearLayout mLlHome2;
            TextView mScenerDeviceName;
            TextView mScenerDevicePlace;
            TextView mScenerDeviceStatus;
            ImageView mItemArrow;
            Button mScenerAdddeviceBt;


            public ViewHolder(View view) {
                mItemScenerDeviceLl = (LinearLayout) view.findViewById(R.id.item_scener_device_ll);
                mScenerDeviceIcon = (ImageView) view.findViewById(R.id.scener_device_icon);
                mLlHome2 = (LinearLayout) view.findViewById(R.id.ll_home_2);
                mScenerDeviceName = (TextView) view.findViewById(R.id.scener_device_name);
                mScenerDevicePlace = (TextView) view.findViewById(R.id.scener_device_place);
                mScenerDeviceStatus = (TextView) view.findViewById(R.id.scener_device_status);
                mItemArrow = (ImageView) view.findViewById(R.id.item_arrow);
                mScenerAdddeviceBt = (Button) view.findViewById(R.id.scener_adddevice_bt);

                view.setTag(this);
            }
        }
    }

    class scenerlistviewClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (ClickUtil.isFastDoubleClick()) {
                return;
            }
            mDevice = mSceneDeviceList.get(position - 1);
            position = position - 1;

            //根据type判断跳出哪个ppw
            evenPopw(mDevice);
        }
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
            NormalListDialog();
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

            //
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

    /**
     * 判断类型,
     */
    private void judgeType() {
        switch (mDevice.type) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                mLightCDevice = (LightCDevice) mDevice;
                break;
            case 5:
                mAirType5Device = (AirType5Device) mDevice;
                break;
            case 6:
                mLightRGBDevice = (LightRGBDevice) mDevice;
                break;
            case 7:
                break;
            case 8:
                break;
            case 9:
                break;
            case 10:
                mAirTypeADevice = (AirTypeADevice) mDevice;
                break;
            case 11:
                break;
            case 12:
                break;
            case 13:
                break;
            case 14:
                break;
            case 21:
                mLockDoorDevice = (LockDoorDevice) mDevice;
                break;
            case 23:
                mWaterHeaterDevice = (WaterHeaterDevice) mDevice;
                break;
            case 254:
                mSmokeDetectorDevice = (SmokeDetectorDevice) mDevice;
                break;

            default:

                break;
        }

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
                AnimationController.show(mScenerParting3);
                //更换背景

//                mScenerCwRgbLl.setBackground(getResources().getDrawable(R.mipmap.pop_scener_power_cw_rgb_ture));
                mIsspeed.setText("速度");
            } else {
                AnimationController.hide(mScenerLlRgb);
                AnimationController.hide(mScenerLlMode);
                AnimationController.hide(mScenerParting3);
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
            /*
            应该是显示初始数据
             */


            //1 rgb cw 切换
            mScenerCbColor.setOnClickListener(new View.OnClickListener() {//rgb
                @Override
                public void onClick(View v) {
                    if (mDevice.type == 11 || mDevice.type == 12) {
                        rgbCwDeviceCustomPop(0);
                        gradationDevice(true);
                        isRgbCw = true;

                        if (mDevice.getPowers().get(1).on) {
                            openCloseCustomPop(1);
                        } else {
                            openCloseCustomPop(0);
                        }
                        rgbDisplay();

                    } else {

                    }

                }
            });

            mScenerCbWhite.setOnClickListener(new View.OnClickListener() {//cwd
                @Override
                public void onClick(View v) {
                    if (mDevice.type == 11 || mDevice.type == 12) {
                        mLightRGBDevice = (LightRGBDevice) mDevice;
                        rgbCwDeviceCustomPop(1);
                        gradationDevice(false);
                        isRgbCw = false;
                        if (mDevice.getPowers().get(0).on) {
                            openCloseCustomPop(1);
                        } else {
                            openCloseCustomPop(0);
                        }

                        mSeekLight.setProgress((int) (mLightRGBDevice.brightness1 / (0.15)));
                        mSeekColorTemp.setProgress((int) (mLightRGBDevice.colortemp / (0.15)));
                    }
                }
            });

            //2 开关 判断是rgb还是cw
            mScenerCbOpen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openCloseCustomPop(1);
                    mBpower = true;
                    if (mDevice.type == 11 || mDevice.type == 12) {
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
                    if (mDevice.type == 11 || mDevice.type == 12) {
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
                    if (mDevice.type == 11 || mDevice.type == 12) {

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
                    if (mDevice.type == 11 || mDevice.type == 12) {
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
                    if (mDevice.type == 11 || mDevice.type == 12) {

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
//                    if (mDevice.type == 11) {
//                        String[] str;
//                        str = oldRgbCwData();
//                        if (isRgbCw) {
//                            int rgb[] = new int[3];
//                            rgb[0] = Integer.parseInt(str[0]);
//                            rgb[1] = Integer.parseInt(str[1]);
//                            rgb[2] = Integer.parseInt(str[2]);
//                            rgbSwitch(mDevice.power.get(0).on, rgb, str[16], str[8], str[4]);
//                        } else {
//                            lightSwich(mDevice.power.get(1).on, str[15], str[9]);
//                        }
//                    } else {
//
//                    }
                    mRgbCwDeviceCustomPop.dismiss();
                }
            });
            mScenerCwRgbSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDevice.type == 11 || mDevice.type == 12) {
                        if (isRgbCw) {
                            rgbSwitch(mBpower, mRgb, mLight + "", mMode + "", mSpeedColor + "");
                        } else {
                            lightSwich(mBpower, mLight + "", mSpeedColor + "");
                        }
                    } else {
                        sendCmd3a(mDevice);
                    }
                    mRgbCwDeviceCustomPop.dismiss();
                }
            });

        }

        public void modeCount(int i) {
            if (mDevice.type == 11 || mDevice.type == 12) {
                LightRGBDevice rgblight = (LightRGBDevice) mDevice;
                mMode = rgblight.mode;
                mMode = mMode + i;
                if (mMode == -1) {
                    mMode = 15;
                }
                if (mMode == 16) {
                    mMode = 0;
                }
                mRgblight = (LightRGBDevice) mDevice;
                mRgblight.mode = mMode;
                mScenerTvMode.setText(mMode + "");
//                rgbSwitch(true, mRgb, mLight + "", mMode + "", mSpeedColor + "");
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
            sendCmd3a(mDevice);
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
            if (!StringUtils.isEmpty(freq))
                rgblight.freq = Integer.parseInt(freq);
            sendCmd3a(mDevice);
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
            //rgb+cw
            if (mDevice.type == 11 || mDevice.type == 12) {
                rgbCwDeviceCustomPop(0);
                isRgbCw = true;
                mBpower = mDevice.power.get(1).on;
                if (mDevice.getPowers().get(1).on) {
                    openCloseCustomPop(1);
                } else {
                    openCloseCustomPop(0);
                }
                rgbDisplay();
            }

            //纯rgb设备
            if (mDevice.type == 6 || mDevice.type == 15) {
                mScenerTitle.setText(mDevice.name + "4" + "12" + "15");
                mLightRGBDevice = (LightRGBDevice) mDevice;
                AnimationController.hide(mScenerLlColour);
//                AnimationController.hide(mScenerLlSpeed);
//                mScenerCwRgbLl.setBackground(getResources().getDrawable(R.mipmap.pop_scener_power_rgb));
                AnimationController.hide(mScenerParting1);
                mSeekLight.setProgress((int) (mLightRGBDevice.brightness2 / (0.15)));
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
                AnimationController.hide(mScenerParting1);
                AnimationController.hide(mScenerParting3);
                cwDisplay();
            }
        }

        private void cwDisplay() {

            mIsspeed.setText("色温");
//            mScenerCwRgbLl.setBackground(getResources().getDrawable(R.mipmap.pop_scener_power_cw2));
            mSeekLight.setProgress((int) (mLightCWDevice.brightness / (0.15)));
            mSeekColorTemp.setProgress((int) (mLightCWDevice.colortemp / (0.15)));
        }

        private void rgbDisplay() {
            mScenerTitle.setText(mDevice.name);
            mLightRGBDevice = (LightRGBDevice) mDevice;
            String[] str = oldRgbCwData();
            int mRgb[] = new int[3];
            String r = str[0];
            String g = str[1];
            String b = str[2];
            String brightness2 = str[16];
            String mode = str[8];
            String freq = str[4];
            mRgb[0] = Integer.parseInt(r);
            mRgb[1] = Integer.parseInt(g);
            mRgb[2] = Integer.parseInt(b);
            mLight = Integer.parseInt(brightness2);
            mMode = Integer.parseInt(mode);
            mSpeedColor = Integer.parseInt(freq);


            mSeekLight.setProgress((int) (mLightRGBDevice.brightness2 / (0.15)));
            mScenerTvMode.setText(mLightRGBDevice.mode + "");
            mSeekColorTemp.setProgress((int) (mLightRGBDevice.freq / (0.15)));
        }

        public String[] oldRgbCwData() {
            if (mDevice.type == 11 || mDevice.type == 12)
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

    /* 多路开关#####适配器 */

    /**
     * @param bas_in
     */
    public void setBasIn(BaseAnimatorSet bas_in) {
        this.mBasIn = bas_in;
    }

    public void setBasOut(BaseAnimatorSet bas_out) {
        this.mBasOut = bas_out;
    }

    private void NormalListDialog() {//mMenuItems   数据
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

            }
        });
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
                    sendCmd3a(mDevice);

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
            mAirTypeADevice = (AirTypeADevice) mDevice;
            mSetTempN = mAirTypeADevice.temp;
            displayData();


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

            //2 温度
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
                    sendCmd3a(mAirTypeADevice);
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

        private void displayData() {
            //开关
            if (mAirTypeADevice.isPower()) {
                setPower(1);
            } else {
                setPower(0);

            }

            //温度文本
            mScenerTemperatureNum.setText(mSetTempN + "");
            //模式
            setWindSpeed(mAirTypeADevice.speed);
            setMode(mAirTypeADevice.mode);
            setMoveWind(mAirTypeADevice.adirect);

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


    //#########################接收数据##################################
    @Override
    public void onReceiveCommand(ServerCommand cmd) {
        super.onReceiveCommand(cmd);
        LogUtils.i("情景模式设备收到:" + cmd.toString());

        switch (cmd.CMDByte) {
            case CMDFC_ServerNotifiOnline.Command:

//                sendCMD(new CMD06_QueryDeviceStatus(mId));
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
                if (mDevice != null)
                    mDevice.power = device.power;
                CMD06_QueryDeviceStatus cmd06 = new CMD06_QueryDeviceStatus(mId);
                break;
            case CMD3D_ServerQuerySceneModeDevicesResult.Command:
                CMD3D_ServerQuerySceneModeDevicesResult cmd3d = (CMD3D_ServerQuerySceneModeDevicesResult) cmd;
                mLis = cmd3d.sceneDeviceList;
                UIUtils.postTaskSafely(new Runnable() {
                    @Override
                    public void run() {
                        if (mLis.size() != 0) {
                            mSceneDeviceList = new ArrayList<>();
                            for (SceneDeviceInfo sdInfo : mLis) {
                                mSceneDevice = sdInfo.scenedev;
                                if (sdInfo.scenedev.sceneid.equals(mId)) {
                                    replenishDevce(sdInfo.ctrlinfo);
                                }
                            }
                            isShowAddmodebt();
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                });
                break;
            case CMD3B_ServerModifySceneDeviceResult.Command:
                mCmd3b = (CMD3B_ServerModifySceneDeviceResult) cmd;
                UIUtils.postTaskSafely(new Runnable() {
                    @Override
                    public void run() {
                        if (mCmd3b.result) {
                            T.showShort(mContext, "操作成功");
                        } else {
                            {
                                T.showShort(mContext, "操作失败");
                            }

                        }
                    }
                });
        }
    }

    private void replenishDevce(Device dtemp) {
        Device dbDevice = DataUtils.getInstance().getDevice(dtemp.id);
        if (dbDevice != null) {
            dtemp.name = dbDevice.name;
            dtemp.place = dbDevice.place;
            dtemp.online = dbDevice.online;
            mSceneDeviceList.add(dtemp);
            sceneDeviceSave(dtemp);
        }
    }

    private void sceneDeviceSave(Device dtemp) {
        dbSceneDeviceInfo dbSceneDeviceInfo = new dbSceneDeviceInfo(mId + dtemp.id, mId, dtemp.id);
        DataUtils.getInstance().save(dbSceneDeviceInfo);
    }

    /**
     * @param device
     */
    /* 发送控制命令 */
    private void sendCmd3a(Device device) {//TODO

        if (CmdBaseActivity.getInstance().isConnected()) {
            CMD3A_ModifySceneDevice cmd3a = new CMD3A_ModifySceneDevice(mSceneDevice, device);
            CmdBaseActivity.getInstance().mHelper.setCommandListener(ScenerDeviceActivity.this);
            CmdBaseActivity.getInstance().sendCmd(cmd3a);
        } else {
            Util.showToast(mContext, "网络异常,请检查网络连接!");
        }
    }

}
