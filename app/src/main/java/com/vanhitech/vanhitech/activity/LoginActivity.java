package com.vanhitech.vanhitech.activity;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.animation.BounceEnter.BounceRightEnter;
import com.flyco.animation.SlideExit.SlideLeftExit;
import com.flyco.dialog.widget.popup.BubblePopup;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.vanhitech.protocol.ClientCMDHelper;
import com.vanhitech.protocol.cmd.ServerCommand;
import com.vanhitech.protocol.cmd.client.CMD02_Login;
import com.vanhitech.protocol.cmd.client.CMD04_GetAllDeviceList;
import com.vanhitech.protocol.cmd.client.CMD0A_Register;
import com.vanhitech.protocol.cmd.client.CMD34_QuerySceneList;
import com.vanhitech.protocol.cmd.server.CMD01_ServerLoginPermit;
import com.vanhitech.protocol.cmd.server.CMD03_ServerLoginRespond;
import com.vanhitech.protocol.cmd.server.CMD05_ServerRespondAllDeviceList;
import com.vanhitech.protocol.cmd.server.CMD35_ServerQuerySceneListResult;
import com.vanhitech.protocol.cmd.server.CMDFF_ServerException;
import com.vanhitech.protocol.object.SceneMode;
import com.vanhitech.vanhitech.MainActivity;
import com.vanhitech.vanhitech.R;
import com.vanhitech.vanhitech.base.CmdBaseActivity;
import com.vanhitech.vanhitech.conf.Constants;
import com.vanhitech.vanhitech.conf.LoginConstants;
import com.vanhitech.vanhitech.utils.DataUtils;
import com.vanhitech.vanhitech.utils.FileUtils;
import com.vanhitech.vanhitech.utils.HttpUtil;
import com.vanhitech.vanhitech.utils.PromptManager;
import com.vanhitech.vanhitech.utils.T;
import com.vanhitech.vanhitech.utils.UIUtils;
import com.vanhitech.vanhitech.views.LinearLayoutView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.TimeZone;


/**
 * 创建者     heyn
 * 创建时间   2016/3/15 14:17
 * 描述	      ${TODO}
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class LoginActivity extends BaseActivity implements ClientCMDHelper.CommandListener, LinearLayoutView.KeyBordStateListener {

    private LinearLayoutView resizeLayout;
    private ImageView mIg_login;
    private EditText mEt_username;
    private EditText mEt_password;
    private Button mBt_login;
    private Button mBt_regsiter;
    private Button mBt_forger;
    private TranslateAnimation mHiddenAction;
    private AlphaAnimation mAlphaAnimation;
    private TranslateAnimation mBackHiddenAction;
    private AlphaAnimation mBackAlphaAnimation;
    private AnimationSet mAnimationSet;
    private LinearLayout mLinearLayout;
    private ProgressDialog mDialog;
    private String mUsertel;
    private String mPassword;
    private DbUtils mDb;
    private ClientCMDHelper helper;
    private Context mContext;
    private TextView more;
    private String mInfo;

    @Override
    public void initView() {

        setContentView(R.layout.activity_login);
        resizeLayout = (LinearLayoutView) findViewById(R.id.layout_logintest);
        mIg_login = (ImageView) findViewById(R.id.login_login);
        mEt_username = (EditText) findViewById(R.id.login_et_username);//帐号
        mEt_password = (EditText) findViewById(R.id.login_et_password);//密码
        mBt_login = (Button) findViewById(R.id.btn_login_go); //登录按键
        mBt_regsiter = (Button) findViewById(R.id.bt_login_regsiter);
        mBt_forger = (Button) findViewById(R.id.bt_login_forget);
        mLinearLayout = (LinearLayout) findViewById(R.id.login_linearlayout);
        more = (TextView) findViewById(R.id.login_more);
        mIg_login.setVisibility(View.VISIBLE);
        mDialog = new ProgressDialog(LoginActivity.this);
        mDb = DataUtils.getInstance().mDb;
        DataUtils.getInstance().deleteAllDevice();//TODO 删除表
        mContext = this;
        super.initView();
    }

    @Override
    public void initData() {
        //连接服务器
        router();
        super.initData();
    }

    private void initClientCMDhelperlog() {

        if (helper == null) {
            helper = ClientCMDHelper.getInstance();
            helper.setCommandListener(this);
        }

        if (!helper.isConnected()) {
            new Thread(new Runnable() {
                // 连接服务器
                @Override
                public void run() {
                    helper.connectToServer(Constants.IP, Constants.PORT);
                    System.out.println("连接服务器");
                }
            }).start();
        }
    }

    @Override
    public void initEvent() {
        super.initEvent();

        resizeLayout.setKeyBordStateListener(this);

        mBt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!HttpUtil.isNetWorkAvailable(LoginActivity.this)) {
                    T.showLong(LoginActivity.this, "网络连接失败");
                } else {
                    //帐号
                    mUsertel = mEt_username.getText().toString().trim();
                    mPassword = mEt_password.getText().toString().trim();
                    if (TextUtils.isEmpty(mUsertel)) {
                        mEt_username.setError("帐号不能为空");
                        //背景变色
                        mEt_username.setBackgroundDrawable(getResources().getDrawable(R.drawable.edit_edittext_error));
                        //抖动
                        Animation shake = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.shake);
                        mEt_username.startAnimation(shake);
                        return;
                    } else {
                        focused(mEt_username);
                    }
                    if (TextUtils.isEmpty(mPassword)) {
                        mEt_password.setError("密码不能为空");
                        mEt_password.setBackgroundDrawable(getResources().getDrawable(R.drawable.edit_edittext_error));
                        return;
                    } else {
                        mEt_password.setBackgroundDrawable(getResources().getDrawable(R.drawable.edit_edittext_focused));
                    }
                    PromptManager.showProgressDialog(mContext);
                    if (CmdBaseActivity.getInstance().isConnected())
                        CmdBaseActivity.getInstance().closeSocket();

                    initClientCMDhelperlog();
                }
            }
        });
        mBt_forger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ForgetActivity.class);
                overridePendingTransition(R.animator.nextenteranim, R.animator.nextexitanim);
            }
        });
        mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,//上移效果
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -0.07f);
        mBackHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,//上移效果
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1.0f
        );
        mHiddenAction.setDuration(2000);
        mHiddenAction.setFillAfter(true);
        mAlphaAnimation = new AlphaAnimation(1.0f, 0.0f);//淡化效果
        mBackAlphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        mAlphaAnimation.setDuration(2000);
        mBackAlphaAnimation.setDuration(2000);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://www.vanhitech.com");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

    }


    private void focused(EditText et_username) {
        et_username.setBackgroundDrawable(getResources().getDrawable(R.drawable.edit_edittext_focused));
    }


    /**
     * 登录
     */
    public void goLogin(String account, String password) {

        String accountTemp = account + "%000";
//        String accountTemp = account + Constants.FACTORY_ID;
        // app运行环境
//        AppEnv appEnv = new AppEnv("Android%10000", null, 10000, null,
//                null, null, null);
        CMD0A_Register cmd0a = new CMD0A_Register("15814412772%10002",
                "123", "15814412772", "", Constants.appEnv);

        int offset = TimeZone.getDefault().getRawOffset() / 1000 / 3600;
//        int offset = 0;

        CMD02_Login cmd02 = new CMD02_Login(accountTemp, password, offset, Constants.appEnv);
        try {

            helper.sendCMD(cmd02);

        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }


    /**
     * 跳转
     */


    @Override
    public void next() {
//        startActivity(RegsiterActivity.class);
        super.next();
    }


    @Override
    public void prev() {
//        startActivity(LoginActivity.class);
        super.prev();  //不允许左滑动画
    }


    @Override
    public void onReceiveCommand(ServerCommand cmd) {
//        super.onReceiveCommand(cmd);

        switch (cmd.CMDByte) {

            case CMDFF_ServerException.Command:
                CMDFF_ServerException cmdff_serverException = (CMDFF_ServerException) cmd;
                mInfo = cmdff_serverException.info;
                UIUtils.postTaskSafely(new Runnable() {
                    @Override
                    public void run() {
                        PromptManager.closeProgressDialog();
                        PromptManager.showErrorDialog(mContext,mInfo);
                    }
                });

                break;
            case CMD01_ServerLoginPermit.Command:
                CMD01_ServerLoginPermit cmd01_ServerLoginPermit = (CMD01_ServerLoginPermit) cmd;
                helper.setKey(cmd01_ServerLoginPermit.key);
                goLogin(mUsertel, mPassword);
                save(mUsertel, mPassword);//保存密码
                break;
            case CMD03_ServerLoginRespond.Command:
                CMD03_ServerLoginRespond cmd03 = (CMD03_ServerLoginRespond) cmd;
                if (cmd03.result) {
                    sendCmd34(new CMD34_QuerySceneList());
                } else {
                    T.showShort(mContext, "登录失败");
                }


                break;
            case CMD05_ServerRespondAllDeviceList.Command:
                CMD05_ServerRespondAllDeviceList cmd05 = (CMD05_ServerRespondAllDeviceList) cmd;
                DataUtils.getInstance().dataDevice(cmd05);
                DataUtils.getInstance().crePlaceSave(cmd);
                DataUtils.getInstance().lightPowerSave(cmd);
                DataUtils.getInstance().cwdDevice0DSave(cmd);
                PromptManager.closeProgressDialog();
                startActivity(MainActivity.class);
                overridePendingTransition(R.animator.nextenteranim, R.animator.nextexitanim);
                helper.closeServer();
                BaseActivity.getInstance().closeSocket();
                LoginConstants.IRESL = true;
                LoginConstants.Isautologin = true;
                PromptManager.closeProgressDialog();
                finish();
                break;
            case CMD35_ServerQuerySceneListResult.Command:
                sCeneDelete();
                CMD35_ServerQuerySceneListResult cmd35 = (CMD35_ServerQuerySceneListResult) cmd;
                DataUtils.getInstance().sceneModeSave(cmd35.sceneList);
                CMD04_GetAllDeviceList cmd04_GetAllDeviceList = new CMD04_GetAllDeviceList();
                sendCmd04(cmd04_GetAllDeviceList);
                break;

        }
    }

    private void sCeneDelete() {
        try {
            DataUtils.getInstance().mDb.deleteAll(SceneMode.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private void sendCmd04(CMD04_GetAllDeviceList cmd04_GetAllDeviceList) {
        try {
            helper.sendCMD(cmd04_GetAllDeviceList);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void sendCmd34(CMD34_QuerySceneList cmd34) {
        try {
            helper.sendCMD(cmd34);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onSocketConnected() {

    }

    @Override
    public void onSocketClosed() {

    }

    /**
     * 保存帐号
     */

    public void save(String account, String password) {
        LoginConstants.USERNAME = account;
        File file = new File(FileUtils.getCachePath(), "infoa.txt");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(file);
            fos.write((account + "##" + password).getBytes());
            fos.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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

                mEt_username.setText(s[0]);
                mEt_username.setSelection(s[0].length());
                mEt_password.setText(s[1]);
                mEt_password.setSelection(s[1].length());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


        return new String[0];
    }


    @Override
    public void stateChange(int state) {
        switch (state) {
            case LinearLayoutView.KEYBORAD_HIDE:
//                AnimationController.fadeIn(mIg_login,100,100);
                mIg_login.setVisibility(View.VISIBLE);

                break;
            case LinearLayoutView.KEYBORAD_SHOW:
//                AnimationController.fadeOut(mIg_login,100,100);
                mIg_login.setVisibility(View.GONE);
                break;
        }
    }


    void clickCenterBtn() {
        View inflate = View.inflate(LoginActivity.this, R.layout.test_popup_bubble_image, null);
        BubblePopup bubblePopup = new BubblePopup(LoginActivity.this, inflate);
        bubblePopup.anchorView(mBt_login)
                .showAnim(new BounceRightEnter())
                .dismissAnim(new SlideLeftExit())
                .autoDismiss(true)
                .show();
    }
}
