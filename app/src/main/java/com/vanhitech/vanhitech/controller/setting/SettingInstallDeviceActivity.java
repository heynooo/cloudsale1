package com.vanhitech.vanhitech.controller.setting;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.animation.BounceEnter.BounceTopEnter;
import com.flyco.animation.SlideExit.SlideTopExit;
import com.flyco.dialog.entity.DialogMenuItem;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalListDialog;
import com.flyco.dialog.widget.popup.base.BasePopup;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.vanhitech.protocol.cmd.ServerCommand;
import com.vanhitech.protocol.cmd.client.CMD0C_AddMasterDevice;
import com.vanhitech.protocol.cmd.server.CMD15_ServerModifyUserResult;
import com.vanhitech.protocol.cmd.server.CMDFF_ServerException;
import com.vanhitech.vanhitech.R;
import com.vanhitech.vanhitech.base.BaseActivityController;
import com.vanhitech.vanhitech.base.CmdBaseActivity;
import com.vanhitech.vanhitech.base.MyApplication;
import com.vanhitech.vanhitech.bean.LanServerCmd;
import com.vanhitech.vanhitech.bean.Place;
import com.vanhitech.vanhitech.bean.SmartConfig;
import com.vanhitech.vanhitech.bean.UInfo;
import com.vanhitech.vanhitech.bean.WInfo;
import com.vanhitech.vanhitech.conf.LoginConstants;
import com.vanhitech.vanhitech.manager.ActivityTitleManager;
import com.vanhitech.vanhitech.socket.LanSocketClient;
import com.vanhitech.vanhitech.socket.LanSocketClientHandler;
import com.vanhitech.vanhitech.utils.AnimationController;
import com.vanhitech.vanhitech.utils.DataUtils;
import com.vanhitech.vanhitech.utils.DevicNameUtils;
import com.vanhitech.vanhitech.utils.LogUtils;
import com.vanhitech.vanhitech.utils.PromptManager;
import com.vanhitech.vanhitech.utils.T;
import com.vanhitech.vanhitech.utils.UIUtils;
import com.vanhitech.vanhitech.utils.ViewFindUtils;
import com.vanhitech.vanhitech.views.LightRgbTagView;
import com.vanhitech.vanhitech.views.dialog.SettingInstallDialogOne;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * 创建者     heyn
 * 创建时间   2016/3/21 10:35
 * 描述	      ${TODO}
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class SettingInstallDeviceActivity extends BaseActivityController implements LanSocketClientHandler.OnSocketListener {
    public LightRgbTagView mLrt_tag;
    private Boolean flag = true;
    public EditText mEditroom, mEditrouter, mEtRouterPass;
    public TextView mTexvprompt;
    private CheckBox mCbIsVisible;
    private Context mContext = this;
    private Button mbtnWifi;
    private ImageView mImgConfigWifiIcon, mImgConfigWifiIcon1;
    private SmartConfig smartConfig;
    private LanSocketClient lanSocketClient;
    private String sPwd;
    private ImageView mImgroom;
    private LinearLayout mLinConfigWifi;
    private Boolean configsucc = false;

    //配置通知handler类
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {// 一键配置成功之后发送服务器信息
           MyApplication.setIsSmartConfig(true);
            switch (msg.what) {
                case SmartConfig.CONFIGFAILED://配置失败作相应提示
                    T.showShort(mContext, "配置失败");
                    break;
                case SmartConfig.CONFIGSUCCESSED://配置成功建立tcp连接
                    T.showShort(mContext, "配置成功");
                    configSuccessToConnectSocket();
                    configsucc = true;
                    smartConfig.stopConfig();

                    //修改动画
                    aNimation(mImgConfigWifiIcon, true);
                    //去掉占位
                    AnimationController.hide(mImgConfigWifiIcon1);
                    //修改字
                    mbtnWifi.setText("配置成功");

                    AnimationController.transparent(mTexvprompt);

                    break;
                case 1:
//                    if(lanSocketClient.isConnected()){
                    if (!configsucc) {
                        T.showShort(mContext, "没有连接上,请检查输入的路由器密码是否正确\n1.按下设备配置按键，2.再次配置");
//                        Dialog(mContext, mbtnWifi, "配置失败\n请检查输入的路由器密码是否正确\n");
                        PromptManager.showErrorDialog(mContext, "配置失败\n请检查输入的路由器密码是否正确\n");
                        //停止扫描
                        smartConfig.stopConfig();
                        //取消动画
                        mLocalRotateAnimation.cancel();
                        //去掉占位
                        AnimationController.hide(mImgConfigWifiIcon1);
                        //修改字
                        mbtnWifi.setText("再次配置");
                    } else {

                    }
//                    }else{
//                    T.showShort(mContext,"执行判断，提示2");

//                    }
                    break;
            }
        }
    };
    private String mPass;
    private String mMac;
    private String mStr;
    private String mSsid;
    private RotateAnimation mLocalRotateAnimation;
    private SimpleCustomPop mSimpleCustomPop;
    private String mStringExtra;
    private String mInfo;

    /**
     * 10秒没连接上提示
     *
     * @param
     * @param
     * @param flag 开启和关闭 自动刷新
     */
    public void start(Boolean flag) {
        if (flag) {
            TimerTask tt = new TimerTask() {
                public void run() {
                    handler.sendEmptyMessage(1);
                }
            };
            Timer timer = new Timer();
//            timer.schedule(tt, 10000, 20000);
            timer.schedule(tt, 30000);
        }
    }


    @Override
    protected void onDestroy() {

        super.onDestroy();
//        lanSocketClient.close();
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
//        initPopupWindow();
        super.onCreate(savedInstanceState, persistentState);

    }

    //初始化配置信息
    private void initConfigData() {
        if (smartConfig == null) {
            smartConfig = new SmartConfig(mContext, handler);
        }
    }


    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_set_installdevice_2);
//        mLayout = (LinearLayout)findViewById(R.id.layout);

        mCbIsVisible = (CheckBox) findViewById(R.id.cb_is_visible);
        mImgroom = (ImageView) findViewById(R.id.img_config_addroom);
        mEditroom = (EditText) findViewById(R.id.et_my_edittext);
        mTexvprompt = (TextView) findViewById(R.id.tv_prompt);
        mEditrouter = (EditText) findViewById(R.id.et_routerName);
        mEtRouterPass = (EditText) findViewById(R.id.et_router_pass);
        mLinConfigWifi = (LinearLayout) findViewById(R.id.lin_config_wifi);
        mbtnWifi = (Button) findViewById(R.id.btn_config);
        mImgConfigWifiIcon = (ImageView) findViewById(R.id.img_config_wifi_icon);
        mImgConfigWifiIcon1 = (ImageView) findViewById(R.id.img_config_wifi_icon1);
//        mTest = (Button)findViewById(R.id.test);

        ActivityTitleManager.getInstance().init(this);
        ActivityTitleManager.getInstance().showCommonTitle();
        ActivityTitleManager.getInstance().changeTitle("配置设备");
//        mLinConfigWifi.setVisibility(View.GONE);
        AnimationController.transparent(mLinConfigWifi);

    }

    private List<Place> mList;

    @Override
    public void initData() {
        super.initData();
//        Set<String> set = new HashSet();
//        set.add("客厅");
//        set.add("厨房");
//        set.add("卧室");
//        set.add("餐厅");
//        DataUtils.getInstance().crePlaceUpdate(set);
        Intent intent = new Intent();
        mStringExtra = intent.getStringExtra("place");
        mEditroom.setText(mStringExtra);

        try {
            mList = DataUtils.getInstance().mDb.findAll(Selector.from(Place.class));
        } catch (DbException e) {
            e.printStackTrace();
        }
        mSsid = getSSid();
        mEditrouter.setText(mSsid);
        String RouterPass = mEtRouterPass.getText().toString();
        if (RouterPass.isEmpty()) {

            WInfo wInfo = DataUtils.getInstance().getWInfo(mSsid);
            if (wInfo != null && wInfo.pass != null)
                mPass = wInfo.pass;

            mEtRouterPass.setText(mPass);
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        MyApplication.setIsSmartConfig(true);
    }

    @Override
    public void initEvent() {
        super.initEvent();

        //可见不可见密码
        mCbIsVisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEtRouterPass.getText().toString().trim() == null) {
                    mEtRouterPass.setText(LoginConstants.lumm);
                }
                if (flag) {
                    mEtRouterPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mEtRouterPass.setSelection(mEtRouterPass.getText().length());
                    flag = false;
                } else {
                    mEtRouterPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mEtRouterPass.setSelection(mEtRouterPass.getText().length());
                    flag = true;
                }
            }
        });

        initConfigData();
        dialog();


//                mEditText.setText("21212121");


        mImgroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NormalListDialogCustomAttr(mList);
            }
        });


        mbtnWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (MyApplication.getIsSmartConfig()) {
                    mPass = mEtRouterPass.getText().toString().trim();//TODO
                    if (!mPass.isEmpty()) {
                        //数据库中取
                        //保存到数据库
                        UInfo uInfo = DataUtils.getInstance().getUInfo(LoginConstants.USERNAME);
                        LoginConstants.lumm = mPass;
                        DataUtils.getInstance().UInfoSaveUpdate(uInfo, mPass);

                        WInfo wInfo = new WInfo(LoginConstants.USERNAME, mSsid, mPass);
                        DataUtils.getInstance().save(wInfo);

                    } else {
                        T.showShort(mContext, "路由器密码为空，是否继续\\n配置");
                        return;
                    }
                    String room = mEditroom.getText().toString().trim();
                    if (room.isEmpty()) {
                        T.showShort(mContext, "选择所在房间");
                        return;
                    }


                    start(true);
                    mbtnWifi.setText("配置中...");
                    MyApplication.setIsSmartConfig(false);
                    AnimationController.show(mLinConfigWifi);//
                    AnimationController.transparent(mImgConfigWifiIcon1); //TODO 2
                    //mImgConfigWifiIcon1

                    aNimation(mImgConfigWifiIcon, false);//TODO

                    configDevice(mSsid, mPass);
                }

            }
        });
    }

    private void aNimation(ImageView imgConfigWifiIcon, Boolean flag) {

        if (flag) {
            AlphaAnimation animation = new AlphaAnimation(0.2f, 1.0f);

            // 定义动画的执行时间
            animation.setDuration(100);

            // 指定循环播放这个动画
            animation.setRepeatCount(Animation.INFINITE);

            // 指定动画重复的时候，是倒退着播放
            animation.setRepeatMode(Animation.REVERSE);

            // 指定某个空间播放动画
            imgConfigWifiIcon.startAnimation(animation);

        } else {
            mLocalRotateAnimation = new RotateAnimation(0.0F, 359.0F, imgConfigWifiIcon.getWidth() / 2, imgConfigWifiIcon.getHeight());
            mLocalRotateAnimation.setDuration(2000L);
            mLocalRotateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
            mLocalRotateAnimation.setRepeatCount(-1);
            imgConfigWifiIcon.startAnimation(mLocalRotateAnimation);
        }

    }

    private void dialog() {
        final SettingInstallDialogOne dialog = new SettingInstallDialogOne(mContext, true);


        dialog.superDismiss();
        dialog.show();
        dialog.dimEnabled(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.tvConfigFlashBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                mSimpleCustomPop = new SimpleCustomPop(mContext);
                mSimpleCustomPop.setCanceledOnTouchOutside(false);
                mSimpleCustomPop.gravity(Gravity.BOTTOM)
                        .offset(0, 0)
                        .showAnim(new BounceTopEnter())
                        .dismissAnim(new SlideTopExit())
                        .dimEnabled(true)
                        .show();
            }
        });

    }
    private Place mPlace;
    private ArrayList<DialogMenuItem> mMenuItems = new ArrayList<>();

    private void NormalListDialogCustomAttr(final List<Place> list) {

        mMenuItems = new ArrayList<>();
        if (!list.isEmpty())
            for (Place d : list) {
                mMenuItems.add(new DialogMenuItem(d.place, R.mipmap.device_control_choose));
            }
        final NormalListDialog dialog = new NormalListDialog(this, mMenuItems);
        int x = mEditroom.getTop();
        dialog.showAtLocation(mEditroom.getMeasuredHeight(), mEditroom.getMeasuredWidth() + 70);
        dialog.title("请选择")//
                .titleTextSize_SP(18)//
                .titleBgColor(Color.parseColor("#409ED7"))//
                .itemPressColor(Color.parseColor("#85D3EF"))//
                .itemTextColor(Color.parseColor("#303030"))//
                .itemTextSize(14)//
                .cornerRadius(11)//
                .widthScale(0.8f)//
                .dimEnabled(true)
                .isTitleShow(false)
                .setItemExtraPadding(0, 0, -10, -10)
                .show(R.style.myDialogAnim);


        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                //选中后更新ui　
                mPlace = list.get(position);
                mEditroom.setText(mPlace.place);
            }
        });

    }

    /**
     * 获取控件宽
     */
    public static int getWidth(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return (view.getMeasuredWidth());
    }

    /*
    * 获取控件高
    */
    public static int getHeight(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return (view.getMeasuredHeight());
    }


    //获取当前连接wifi的名称
    private String getSSid() {

        WifiManager wm = (WifiManager) getSystemService(Activity.WIFI_SERVICE);
        LogUtils.i(wm.toString());
        if (wm != null) {
            WifiInfo wi = wm.getConnectionInfo();
            if (wi != null) {
                String s = wi.getSSID();
                if (s != null) {
                    if (s.length() > 2 && s.charAt(0) == '"'
                            && s.charAt(s.length() - 1) == '"') {
                        return s.substring(1, s.length() - 1);
                    }
                }

            }
        }
        return "";
    }

    /**
     * 配置成功,建立与设备的tcp连接
     */
    private void configSuccessToConnectSocket() {
        if (lanSocketClient == null) {
            lanSocketClient = new LanSocketClient();
            lanSocketClient.setOnSocketListener(this);
        }
        //tcp连接要建立线程
        new Thread(new Runnable() {
            public void run() {
                lanSocketClient.connect(smartConfig.getIp(), 220);
            }
        }).start();
    }


    //发送服务器的ip和端口给设备
    private void sendServerIPAndPortToDevice() {
        lanSocketClient.sendConfigData("218.67.54.154", 221);
    }

    //配置设备
    private void configDevice(String ssid, String lumm) {
        smartConfig.startConfig(ssid, lumm);
    }


    @Override
    public void onSocketClosed() {

    }

    @Override
    public void onSocketReceive(LanServerCmd serverCmd) {
        switch (serverCmd.cmd) {
            case 0x00:
                //发送服务器的ip和端口给设备
                sendServerIPAndPortToDevice();
                break;
            case 0x10:
                if (serverCmd.datas[0] == 1) {
                    //配置成功
                    lanSocketClient.close();
                    System.out.println("===============配置成功=====================================");
                    //发送cmd0c  String pass, String mac, String name, String place, String groupid
                    mMac = smartConfig.getMac();//TODO
                    mStr = DevicNameUtils.idToName(mMac);

                    CMD0C_AddMasterDevice cmd0C_addMasterDevice = new CMD0C_AddMasterDevice("123", mMac, "中控", mEditroom.getText().toString().trim(), "");
                    CmdBaseActivity.getInstance().mHelper.setCommandListener(SettingInstallDeviceActivity.this);
                    CmdBaseActivity.getInstance().sendCmd(cmd0C_addMasterDevice);
                    UIUtils.postTaskSafely(new Runnable() {
                        @Override
                        public void run() {
                            T.showShort(mContext, "添加中控完成");
                        }
                    });
                }
            default:
                break;
        }
    }

    private class SimpleCustomPop extends BasePopup<SimpleCustomPop> {

        private LinearLayout mRelBoard;
        private RelativeLayout mA;
        private ImageView mBtnRouterPwd;
        private EditText mSetEtPassword1;
        private CheckBox mCbIsVisible1;
        private Button mTvConfigNextBtn;

        public SimpleCustomPop(Context context) {
            super(context);
        }

        @Override
        public View onCreatePopupView() {
            widthScale(1f);
            heightScale(1f);
//            widthScale(1f);
//            heightScale(1f);
            View inflate = View.inflate(mContext, R.layout.activity_set_installdevice_ppw2, null);
            mRelBoard = ViewFindUtils.find(inflate, R.id.rel_board);
            mA = ViewFindUtils.find(inflate, R.id.a);
            mBtnRouterPwd = ViewFindUtils.find(inflate, R.id.btn_router_pwd);
            mSetEtPassword1 = ViewFindUtils.find(inflate, R.id.set_et_password1);
            mCbIsVisible1 = ViewFindUtils.find(inflate, R.id.cb_is_visible1);
            mTvConfigNextBtn = ViewFindUtils.find(inflate, R.id.tv_config_next_btn);
            return inflate;
        }

        @Override
        public void setUiBeforShow() {
            /**
             *   if (!mPass.isEmpty()) {
             //数据库中取
             //保存到数据库

             */

            String pp2Pass = mSetEtPassword1.getText().toString().trim();
            if (!pp2Pass.isEmpty()) {
                LoginConstants.lumm = pp2Pass;
                WInfo wInfo = new WInfo(LoginConstants.USERNAME, mSsid, pp2Pass);
                DataUtils.getInstance().save(wInfo);
            } else {
                WInfo wInfo = DataUtils.getInstance().getWInfo(mSsid);
                if (wInfo != null && wInfo.pass != null)
                    mPass = wInfo.pass;
                mSetEtPassword1.setText(mPass);
            }

            mTvConfigNextBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEtRouterPass.setText(mSetEtPassword1.getText());
                    mSimpleCustomPop.dismiss();
                }
            });
            mCbIsVisible1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (flag) {
                        mSetEtPassword1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        mSetEtPassword1.setSelection(mSetEtPassword1.getText().length());
                        flag = false;
                    } else {
                        mSetEtPassword1.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        mSetEtPassword1.setSelection(mSetEtPassword1.getText().length());
                        flag = true;
                    }
                }
            });
        }
    }


    class roomAdapter extends BaseAdapter {


        /**
         * getCount 里面存放 比如说 客厅有多少设备 这里就返回多少个
         * 这里的device 是一个
         *
         * @return
         */
        @Override
        public int getCount() {
            if (mList != null) {
                int i = mList.size();
                return i;//TODO
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (mList != null) {
                return mList.get(position);//TODO
            }
            return null;
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;  //根视图
            if (convertView == null) {

                convertView = View.inflate(mContext, R.layout.item_list_room, null);
                holder = new ViewHolder();
                holder.mIvIcon = (CheckBox) convertView.findViewById(R.id.iv_icon);
                holder.mTvName = (TextView) convertView.findViewById(R.id.tv_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            //数据绑定
            holder.mTvName.setText(mList.get(position).place);
            holder.mIvIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (flag) {
                        flag = false;
                    } else {
                        flag = true;
                    }
                }
            });


            return convertView;
        }

        class ViewHolder {
            CheckBox mIvIcon;
            TextView mTvName;
        }
    }

    /**
     * : ClientCMDHelperread CMDFF_ServerException command su
     * : CMDFF_ServerException:{ code:110, info:设备已被添加过,uid:D
     * 基类收到:com.vanhitech.vanhitech.activity.device.LightWhi
     */
    @Override
    public void onReceiveCommand(ServerCommand cmd) {
        super.onReceiveCommand(cmd);
        switch (cmd.CMDByte) {

            case CMDFF_ServerException.Command:

                CMDFF_ServerException cmdff_serverException = (CMDFF_ServerException) cmd;

                mInfo = cmdff_serverException.info;

                UIUtils.postTaskSafely(new Runnable() {
                    @Override
                    public void run() {
                        PromptManager.showErrorDialog(mContext, mInfo);
                    }
                });
                break;
            case CMD15_ServerModifyUserResult.Command:
                final CMD15_ServerModifyUserResult cmd15 = (CMD15_ServerModifyUserResult) cmd;

                UIUtils.postTaskSafely(new Runnable() {
                    @Override
                    public void run() {


                    }
                });
                break;

        }
    }

}
