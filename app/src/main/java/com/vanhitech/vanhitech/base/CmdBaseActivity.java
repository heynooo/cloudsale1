package com.vanhitech.vanhitech.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.flyco.animation.BounceEnter.BounceRightEnter;
import com.flyco.animation.SlideExit.SlideLeftExit;
import com.flyco.dialog.widget.popup.BubblePopup;
import com.lidroid.xutils.exception.DbException;
import com.umeng.analytics.MobclickAgent;
import com.vanhitech.protocol.ClientCMDHelper;
import com.vanhitech.protocol.cmd.ClientCommand;
import com.vanhitech.protocol.cmd.ServerCommand;
import com.vanhitech.protocol.cmd.client.CMD02_Login;
import com.vanhitech.protocol.cmd.client.CMD04_GetAllDeviceList;
import com.vanhitech.protocol.cmd.client.CMD08_ControlDevice;
import com.vanhitech.protocol.cmd.client.CMD3C_QuerySceneModeDevices;
import com.vanhitech.protocol.cmd.server.CMD01_ServerLoginPermit;
import com.vanhitech.protocol.cmd.server.CMD03_ServerLoginRespond;
import com.vanhitech.protocol.cmd.server.CMD05_ServerRespondAllDeviceList;
import com.vanhitech.protocol.cmd.server.CMD09_ServerControlResult;
import com.vanhitech.protocol.cmd.server.CMD0F_ServerAddSlaveDeviceResult;
import com.vanhitech.protocol.cmd.server.CMD11_ServerDelDeviceResult;
import com.vanhitech.protocol.cmd.server.CMD13_ServerModifyDeviceResult;
import com.vanhitech.protocol.cmd.server.CMD15_ServerModifyUserResult;
import com.vanhitech.protocol.cmd.server.CMD17_ServerQueryUserResult;
import com.vanhitech.protocol.cmd.server.CMD19_ServerAddTimerResult;
import com.vanhitech.protocol.cmd.server.CMD21_ServerModifyTimerResult;
import com.vanhitech.protocol.cmd.server.CMD23_ServerDelTimerResult;
import com.vanhitech.protocol.cmd.server.CMD25_ServerQueryTimerResult;
import com.vanhitech.protocol.cmd.server.CMD35_ServerQuerySceneListResult;
import com.vanhitech.protocol.cmd.server.CMD3D_ServerQuerySceneModeDevicesResult;
import com.vanhitech.protocol.cmd.server.CMD47_ServerForgetPassResult;
import com.vanhitech.protocol.cmd.server.CMDFC_ServerNotifiOnline;
import com.vanhitech.protocol.cmd.server.CMDFF_ServerException;
import com.vanhitech.protocol.object.Power;
import com.vanhitech.protocol.object.SceneDeviceInfo;
import com.vanhitech.protocol.object.TimerInfo;
import com.vanhitech.protocol.object.UserInfo;
import com.vanhitech.protocol.object.device.Device;
import com.vanhitech.protocol.object.device.SceneDevice;
import com.vanhitech.vanhitech.R;
import com.vanhitech.vanhitech.bean.UInfo;
import com.vanhitech.vanhitech.bean.UpdateTime;
import com.vanhitech.vanhitech.conf.Constants;
import com.vanhitech.vanhitech.conf.LoginConstants;
import com.vanhitech.vanhitech.utils.DataUtils;
import com.vanhitech.vanhitech.utils.DateUtils;
import com.vanhitech.vanhitech.utils.FileUtils;
import com.vanhitech.vanhitech.utils.LogUtils;
import com.vanhitech.vanhitech.utils.StringUtils;
import com.vanhitech.vanhitech.utils.T;
import com.vanhitech.vanhitech.utils.UIUtils;
import com.vanhitech.vanhitech.views.SystemBarTintManager;
import com.zhy.autolayout.AutoLayoutActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TimerTask;

import butterknife.ButterKnife;


/**
 * 创建者     heyn
 * 创建时间   2016/3/17 15:06
 * 描述	      ${TODO}
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class CmdBaseActivity extends AutoLayoutActivity implements ClientCMDHelper.CommandListener {

    //    public  static ClientCMDHelper mHelper;
    private static CmdBaseActivity instance;
    private int closeSocketTag = 1;
    //    private CMDReceiveListener cmdlistener;
    private int connectCount;
    public ClientCMDHelper mHelper;
    private CMDFF_ServerException mCmdff;
    public List<Device> mAllDevices;
    public Boolean mIsSaveDevice;
    private String mControllScenerid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //状态栏颜色测试start


        //状态栏颜色测试start


//        initClientCMDhelper();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //横竖屏固定


    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    public void systemBarTint() {//覆盖电量时间wifi背景效果
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(this, true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintColor(0);
    }

    @TargetApi(19)
    private static void setTranslucentStatus(Activity activity, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    public CmdBaseActivity() {
        if (mHelper == null) {
            mHelper = ClientCMDHelper.getInstance();
        }
        mHelper.setCommandListener(this);
    }

    public static CmdBaseActivity getInstance() {
        if (instance == null) {
            instance = new CmdBaseActivity();
        }
        return instance;
    }

    public void starToConnectServer() {
        connectoServer();
    }

    public void closeSocket() {
//        cancelIdleTimer();定时
        mHelper.closeServer();
    }

    private void connectoServer()//断开重新连接
    {
        if (isConnected()) {
            return;
        }
        initConnected();
    }

    public boolean isConnected() {
        return mHelper.isConnected();
    }

    public void initConnected() {//是否需要多ip连接?

        new Thread(new Runnable() {
            // 连接服务器
            @Override
            public void run() {
                mHelper.connectToServer(Constants.IP, Constants.PORT);
                System.out.println("连接服务器");
            }
        }).start();
    }

    //发送命令
    public void sendCmd(ClientCommand paramClientCommand) {
        if (!isConnected()) {
            new Thread(new Runnable() {
                public void run() {
                    CmdBaseActivity.this.connectoServer();
                }
            }).start();
            return;
        }
        try {
            mHelper.sendCMD(paramClientCommand);
            return;
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    public void sendCmd04() {
        CMD04_GetAllDeviceList cmd4 = new CMD04_GetAllDeviceList();
        sendCmd(cmd4);
    }

    public void openScener(String Scenerid, Boolean mIsSaveDevice) {
        mControllScenerid = Scenerid;
        mIsSaveDevice = mIsSaveDevice;
        sendCmd(new CMD3C_QuerySceneModeDevices(Scenerid));
    }

    public void initClientCMDhelper() {
        if (mHelper == null) {
            mHelper = ClientCMDHelper.getInstance();
        }
        mHelper.setCommandListener(this); //TODO 这里可能会有风险

        if (!mHelper.isConnected()) {
            new Thread(new Runnable() {
                // 连接服务器
                @Override
                public void run() {
                    mHelper.connectToServer(Constants.IP, Constants.PORT);
                    System.out.println("连接服务器");
                }
            }).start();
        }
    }

    public void CmdHelper(ClientCommand cmd) {
        try {

            mHelper.sendCMD(cmd);

        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }


    public void setDevices(List<Device> devices) {
        mAllDevices = devices;
    }

    public List<Device> getDevices() {
        return mAllDevices;
    }

    public Boolean getSaveDevice() {
        return mIsSaveDevice;
    }

    public void setSaveDevice(Boolean saveDevice) {
        mIsSaveDevice = saveDevice;
    }

    private CmdBaseActivity.CMDReceiveListener listener;

    @Override
    public void onReceiveCommand(ServerCommand cmd) {
        LogUtils.i("cmBaseAcitivity");
        switch (cmd.CMDByte) {
            case CMDFF_ServerException.Command:
                mCmdff = (CMDFF_ServerException) cmd;
                if (mCmdff.info.equals("用户未登录")) {
                    String[] s = router();
                    goLogin(s[0], s[1]);
                }
                UIUtils.postTaskSafely(new TimerTask() {
                    @Override
                    public void run() {
                        T.show(MyApplication.getContext(), mCmdff.info + "\n请重试", 5000);

                    }
                });
//                PromptManager.showToast(MyApplication.getContext(),cmdff.toString());

                notifyObserver(cmd);//给观察者发送
                break;
            case CMD01_ServerLoginPermit.Command:
                CMD01_ServerLoginPermit cmd01 = (CMD01_ServerLoginPermit) cmd;
//                T.showShort(this,cmd01.toString());
                //key值
                CMD01_ServerLoginPermit cmd01_ServerLoginPermit = (CMD01_ServerLoginPermit) cmd;
                mHelper.setKey(cmd01_ServerLoginPermit.key);
                LoginConstants.key = cmd01_ServerLoginPermit.key;
                System.out.println("BaseActivity给了key");
                if (LoginConstants.IRESL) {
                    String[] s = router();
                    goLogin(s[0], s[1]);
                }
                break;
            case CMD03_ServerLoginRespond.Command:
                CMD03_ServerLoginRespond cmd03 = (CMD03_ServerLoginRespond) cmd;
//              T.showShort(MyApplication.getContext(),cmd03.toString());
                LogUtils.i("基类cmd3");
                UserInfo u = LoginConstants.userInfo = cmd03.info;
                UInfo uInfo = new UInfo();
                uInfo.id = 1 + "";
                uInfo.name = u.name;
                uInfo.pass = u.pass;
                uInfo.userid = u.userid;
                uInfo.phone = u.phone;
                uInfo.email = u.email;
                uInfo.offset = u.offset + "";
                DataUtils.getInstance().UInfoSaveUpdate(uInfo, null);
                sendCmd04();//TODO
                break;
            case CMD05_ServerRespondAllDeviceList.Command:
                CMD05_ServerRespondAllDeviceList cmd05 = (CMD05_ServerRespondAllDeviceList) cmd;
                if (cmd05.deviceList.size() != 0) {
                    mAllDevices = new ArrayList<>();
                    mAllDevices = cmd05.deviceList;
                }
//                LogUtils.i("................服务器返回数据..数据库更新中...........................");
                UpdateTime time = new UpdateTime(0 + "", DateUtils.getCurTimeAddXM(25));
                DataUtils.getInstance().save(time);

                //-------------全部删掉 ,重新添加------1-
//                DataUtils.getInstance().deleteAllDevice();//TODO
//                DataUtils.getInstance().crePlaceSave(cmd);
//                DataUtils.getInstance().lightPowerSave(cmd);
//                DataUtils.getInstance().cwdDevice0DSave(cmd);
                //------------------------------------------
                Set<String> set = new HashSet<>();
                for (Device device : cmd05.deviceList) {
                    DataUtils.getInstance().updateDevice(device);
                    DataUtils.getInstance().lightPowerIsSaveUpdate(device);//处理power更新
                    set.add(device.place);
                }
                if (cmd05.deviceList != null)
                    DataUtils.getInstance().updateDevicedelete(cmd05.deviceList);
//                if (LoginConstants.isPlaceSave) {
//                    DataUtils.getInstance().crePlaceUpdate(set);
//                }
//                DataUtils.getInstance().crePlaceSave(cmd);
                LogUtils.i("基类cmd5");
                notifyObserver(cmd);//给观察者发送
                break;
            case CMD09_ServerControlResult.Command:
                CMD09_ServerControlResult cmd09 = (CMD09_ServerControlResult) cmd;
//                 T.showShort(MyApplication.getContext(),cmd09.toString());
//                DataUtils.getInstance().crePlaceUpdate(cmd);
                DataUtils.getInstance().lightPowerUpdate(cmd);
//                DataUtils.getInstance().cwdDevice0DUpdateCmd09(cmd);//暂时不收这个消息
//                DataUtils.getInstance().crePlaceUpdate(cmd);
                LogUtils.i("基类cmd9");
                notifyObserver(cmd);//给观察者发送
                break;
            //TODO
            case CMD0F_ServerAddSlaveDeviceResult.Command:
                //添加2.4g设备返回结果
                CMD0F_ServerAddSlaveDeviceResult cmd0f = (CMD0F_ServerAddSlaveDeviceResult) cmd;
                Device d = cmd0f.status;

                DataUtils.getInstance().updateDevice(d);


                break;

            case CMD11_ServerDelDeviceResult.Command:
                //服务器返回删除某个设备结果
                CMD11_ServerDelDeviceResult cmd11 = (CMD11_ServerDelDeviceResult) cmd;
                DataUtils.getInstance().save(new UpdateTime(0 + "", DateUtils.getCurTimeAddXM(25)));
//                 T.showShort(MyApplication.getContext(),cmd11.toString());
//                isTf(cmd11.result);
                //数据库删除 该设备
                Boolean b = cmd11.result;
                Device device = cmd11.status;
                if (b) {
                    if (device.type == 13) {
                        DataUtils.getInstance().deleteCWDevice0D(device.id + "");
                    } else {
                        DataUtils.getInstance().deleteDevice(device.id + "");
                    }
                }

                break;

            case CMD13_ServerModifyDeviceResult.Command:
                //服务器返回修改某个设备结果
                CMD13_ServerModifyDeviceResult cmd13 = (CMD13_ServerModifyDeviceResult) cmd;
                T.show(MyApplication.getContext(), isTf(cmd13.result), 2000);
                break;


            case CMD15_ServerModifyUserResult.Command:
                //服务器返回修改用户信息结果
                CMD15_ServerModifyUserResult cmd15 = (CMD15_ServerModifyUserResult) cmd;
                break;

            case CMD17_ServerQueryUserResult.Command:
                //服务器返回查询用户信息结果
                CMD17_ServerQueryUserResult cmd17 = (CMD17_ServerQueryUserResult) cmd;

                break;

            case CMD19_ServerAddTimerResult.Command:
                //服务器返回添加定时任务结果
                CMD19_ServerAddTimerResult cmd19 = (CMD19_ServerAddTimerResult) cmd;
                break;


            case CMD21_ServerModifyTimerResult.Command:
                //服务器返回添加定时任务结果
                CMD21_ServerModifyTimerResult cmd21 = (CMD21_ServerModifyTimerResult) cmd;
                DataUtils.getInstance().save(new UpdateTime(1 + "", DateUtils.getCurTimeAddXM(25)));

                try {
                    DataUtils.getInstance().TimeTaskDaySaveUpdate(cmd21.schedinfo, cmd21.ctrlinfo);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                UIUtils.postTaskSafely(new TimerTask() {
                    @Override
                    public void run() {


                    }
                });
                break;

            case CMD23_ServerDelTimerResult.Command:
                //服务器返回删除定时任务结果
                CMD23_ServerDelTimerResult cmd23 = (CMD23_ServerDelTimerResult) cmd;
                break;

            case CMD25_ServerQueryTimerResult.Command:
                //服务器返回查询某个设备定时任务等指令

                DataUtils.getInstance().save(new UpdateTime(1 + "", DateUtils.getCurTimeAddXM(25)));
                CMD25_ServerQueryTimerResult cmd25 = (CMD25_ServerQueryTimerResult) cmd;
                List<TimerInfo> timerInfos = cmd25.timerList;
                for (TimerInfo info : timerInfos) {
                    try {
                        DataUtils.getInstance().TimeTaskDaySaveUpdate(info.schedinfo, info.ctrlinfo);
                        //保存TimeTaskDay
                        //保存TimeTaskId
//                        DataUtils.getInstance().updateDevice();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //定时时间信息,只用来查看
                    try {
                        DataUtils.getInstance().mDb.saveOrUpdate(info.schedinfo);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }

                }
                LogUtils.i(cmd25.toString());
                break;
            case CMD35_ServerQuerySceneListResult.Command:
                CMD35_ServerQuerySceneListResult cmd35 = (CMD35_ServerQuerySceneListResult) cmd;
                if (cmd35.sceneList != null) {
                    DataUtils.getInstance().sCeneDelete();
                }
                DataUtils.getInstance().sceneModeSave(cmd35.sceneList);
                break;
            case CMD3D_ServerQuerySceneModeDevicesResult.Command:
                CMD3D_ServerQuerySceneModeDevicesResult cmd3d = (CMD3D_ServerQuerySceneModeDevicesResult) cmd;

                List<SceneDeviceInfo> DeviceList = cmd3d.sceneDeviceList;
                List<Device> devices = new ArrayList<>();
                List<SceneDevice> SceneDevices = new ArrayList<>();

                for (SceneDeviceInfo lis : DeviceList) {
                    devices.add(lis.ctrlinfo);
                    SceneDevices.add(lis.scenedev);
                }
                MyApplication.setIsSendScener(true);

                if (devices.size() != 0) {

                    if (MyApplication.getIsSaveDevice()) {

                        sceneDeviceList(devices);

                    }
                }

                break;
            case CMD47_ServerForgetPassResult.Command:
                CMD47_ServerForgetPassResult cmd47_serverForgetPassResult = (CMD47_ServerForgetPassResult) cmd;
                T.showLong(MyApplication.getContext(), cmd47_serverForgetPassResult.toString());
                break;
            case CMDFC_ServerNotifiOnline.Command:
                MyApplication.setIsbrushStart(true);
                //设备信息变化
                CMDFC_ServerNotifiOnline cmdfc = (CMDFC_ServerNotifiOnline) cmd;
                for (Device devicefc : cmdfc.deviceList) {//不明白这个返回的变化值，哪些是有用的，删除设备也会返回值，操作设备也返回值
//                    DataUtils.getInstance().updateDevice(device);
                }
                break;


        }


    }

    /**
     * @param devices
     */
    private void sceneDeviceList(List<Device> devices) {
        List<Device> savedevice = new ArrayList<>();
        for (Device ds : devices) {
            String id = ds.id;
            Device tempData = DataUtils.getInstance().getDevice(id);
            ds.name = tempData.name;
            ds.place = tempData.place;
            savedevice.add(ds);
        }
        MyApplication.setIsSaveDevice(false);
        LogUtils.i("情景模式-------------------" + savedevice.toString());
        for (Device d : savedevice) {
            CMD08_ControlDevice cmd08 = new CMD08_ControlDevice(d);
            sendCmd(cmd08);
        }
        UIUtils.postTaskSafely(new Runnable() {
            @Override
            public void run() {
//                T.showShort(MyApplication.getContext(),"发送完成");
            }
        });

    }

    private String isTf(boolean result) {
        if (result) {
            return StringUtils.getString(MyApplication.getContext(), R.string.success);
        } else {
            return StringUtils.getString(MyApplication.getContext(), R.string.error);
        }

    }

    /**
     * 读取帐号
     */
    public String[] router() {

        File file = new File(FileUtils.getCachePath(), "infoa.txt");
        if (file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                //把字节流转换成字符流
                BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                //读取txt文件里的用户名和密码
                String text = br.readLine();
                String[] s = text.split("##");

                String name = s[0];
                String pwd = s[1];
                return s;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return new String[0];
    }

    /**
     * 登录
     */
    public void goLogin(String account, String password) {
//        String accountTemp = account + Constants.FACTORY_ID;
        String accountTemp = account + "%000";
//        AppEnv appEnv = new AppEnv("Android%10000", null, 10000, null,
//                null, null, null);
        CMD02_Login cmd02 = new CMD02_Login(accountTemp, password, 0, Constants.appEnv);
        CmdHelper(cmd02);
    }

    @Override
    public void onSocketConnected() {

    }

    @Override
    public void onSocketClosed() {
//定时的作用 ,如果标签是1的时候, 进入定时,时间到,重新连接,发送命令
//        System.out.println("Socket close!");
//        if (this.closeSocketTag == 1)
//        {
//            System.out.println("exception");
//            cancelIdleTimer();
//            TimerTaskHelper.getInstance().cancelTimer();
//            TimerTaskHelper.getInstance().startTimer(new TimerTask()
//            {
//                public void run()
//                {
//                    PublicCmdHelper.this.starToConnectServer();
//                    if ((PublicCmdHelper.this.connectCount >= 4) && (PublicCmdHelper.this.cmdlistener != null))
//                    {
//                        PublicCmdHelper.this.connectCount = 0;
//                        PublicCmdHelper.this.cmdlistener.errorMessage(-1);
//                    }
//                }
//            }, 3000L, -1L);
//        }


    }

    //设置标识
    public void setCloseSocketTag(int paramInt) {
        this.closeSocketTag = paramInt;
    }

    public void setCMDReceiveListener(CMDReceiveListener listener) {
        this.listener = listener;
    }

    public interface CMDReceiveListener {
        void errorMessage(int paramInt);

        void receiveCMD(ServerCommand paramServerCommand);
    }

    //创建一个单个设备关闭的方法  先实现单个 抽取过来
    public void closeLight(Device mDevice) {
        try {//从数据库获取开关是开还是关，然后赋值
            mDevice.power.size();//遍历出来power出来,都设置成flase
            List<Power> power = new ArrayList();
            for (Power P : mDevice.power) {
                P.on = false;
                power.add(P);
            }
            mDevice.setPowers(power);

            CMD08_ControlDevice cmd08 = new CMD08_ControlDevice(mDevice);

            CmdBaseActivity.getInstance().sendCmd(cmd08);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //发送设备
    public void cmdLight(Device mDevice) {
        try {//从数据库获取开关是开还是关，然后赋值

            CMD08_ControlDevice cmd08 = new CMD08_ControlDevice(mDevice);

            CmdBaseActivity.getInstance().sendCmd(cmd08);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    //创建一个集合设备全部关闭的方法

    //创建一个单个设备开启的方法
    public void openLight(Device mDevice) {
        try {//从数据库获取开关是开还是关，然后赋值
            mDevice.power.size();//遍历出来power出来,都设置成flase
            List<Power> power = new ArrayList();
            for (Power P : mDevice.power) {
                P.on = true;
                power.add(P);
            }
            mDevice.setPowers(power);

            CMD08_ControlDevice cmd08 = new CMD08_ControlDevice(mDevice);

            CmdBaseActivity.getInstance().sendCmd(cmd08);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    ////创建一个集合全部开启的方法


    /**
     * 按手机返回按键处理
     */
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//    }

    /**
     * 实现观察者模式
     */
    public interface MyObserver {
        void update(ServerCommand cmd);

    }

    /**
     * 保存所有的接口对象
     */
    LinkedList<MyObserver> observer = new LinkedList<MyObserver>();

    /**
     * 添加观察者
     */
    public synchronized void addObserver(MyObserver o) {
        if (o == null) {
            throw new NullPointerException();
        }
        if (!observer.contains(o)) {
            observer.add(o);
        }
    }


    /**
     * 移除观察者
     */
    public synchronized void deleteObserver(MyObserver o) {
        observer.remove(o);
    }


    /**
     * 通知观察者
     */
    public void notifyObserver(ServerCommand message) {
        for (MyObserver o : observer) {
            o.update(message);
        }
    }

    /**
     * dolg
     */
    public void clickTopLeftBtn(final Context context, View view, String text, final String show, Boolean automiss) {
        View inflate = View.inflate(context, R.layout.popup_bubble_text, null);
        TextView tv = ButterKnife.findById(inflate, R.id.tv_bubble);
        BubblePopup bubblePopup = new BubblePopup(context, inflate);
        tv.setText(text);

        bubblePopup
//                .triangleWidth(200)
//                .triangleHeight(100)
                .anchorView(view)
//                .gravity(Gravity.TOP)
//                .location(1000,1000)
//                .show();
                .showAnim(new BounceRightEnter())
                .dismissAnim(new SlideLeftExit())
                .autoDismiss(automiss)
                .location(200, 1200)

                .show();

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T.showShort(context, show);
            }
        });
    }

    public boolean isNull(Object obj) {
        if (obj == null) {
            return true;
        }
        return false;
    }

    public List<Device> loadData() throws IOException {

        //1.从内存
        MyApplication app = (MyApplication) UIUtils.getContext();
        List<Device> list = app.getDevices();


        return list;
    }

}
