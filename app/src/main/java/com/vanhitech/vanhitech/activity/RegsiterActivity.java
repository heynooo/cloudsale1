package com.vanhitech.vanhitech.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.lidroid.xutils.exception.DbException;
import com.vanhitech.protocol.ClientCMDHelper;
import com.vanhitech.protocol.cmd.ServerCommand;
import com.vanhitech.protocol.cmd.client.CMD0A_Register;
import com.vanhitech.protocol.cmd.server.CMD01_ServerLoginPermit;
import com.vanhitech.protocol.cmd.server.CMD0B_ServerRegisterResult;
import com.vanhitech.protocol.cmd.server.CMDFF_ServerException;
import com.vanhitech.vanhitech.R;
import com.vanhitech.vanhitech.base.CmdBaseActivity;
import com.vanhitech.vanhitech.bean.UInfo;
import com.vanhitech.vanhitech.conf.Constants;
import com.vanhitech.vanhitech.utils.DataUtils;
import com.vanhitech.vanhitech.utils.HttpUtil;
import com.vanhitech.vanhitech.utils.LogUtils;
import com.vanhitech.vanhitech.utils.T;
import com.vanhitech.vanhitech.utils.UIUtils;

import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 创建者     heyn
 * 创建时间   2016/3/15 14:19
 * 描述	      注册界面
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class RegsiterActivity extends BaseActivity {


    private EditText mEt_phone;
    private EditText mEt_email;
    private Button mBt_regsiter;
    private EditText mEt_password;
    private ProgressDialog mDialog;
    private  CheckBox mCb_visible;
    private  Boolean flag=true;
    private Context mContext=this;
    private String mPhone;
    private String mEmail;
    private String mPassword;

    @Override
    public void initEvent() {
        super.initEvent();
        //-----------登录------------------------

        //可见不可见密码
        mCb_visible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag){
                    mEt_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    flag=false;
                }else{
                    mEt_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    flag=true;
                }
            }
        });

        mBt_regsiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!HttpUtil.isNetWorkAvailable(RegsiterActivity.this)){
                    T.showLong(RegsiterActivity.this,"网络连接失败");
                }else {

                    //手机
                    mPhone = mEt_phone.getText().toString().trim();
                    //邮箱
                    mEmail = mEt_email.getText().toString().trim();
                    //密码
                    mPassword = mEt_password.getText().toString().trim();
                    //判断空
                    if (TextUtils.isEmpty(mPhone)) {
                        mEt_phone.setError("手机号码不能为空");
                        //背景变色 TODO
                        error(mEt_phone);
                        if(mPhone.length() <= 5&&mPhone.length() >= 15){
                            mEt_phone.setError("手机号码格式错误");
                            error(mEt_phone);
                        }
                        return;
                    }else{
                        focused(mEt_phone);
                    }
                    if (TextUtils.isEmpty(mPassword)) {
                        mEt_password.setError("密码不能为空");
                        error(mEt_password);
                        //背景变色
                        return;
                    }else{
                        focused(mEt_password);
                    }
                    if (TextUtils.isEmpty(mEmail) || mEmail.length() > 31) {

                        mEt_email.setError("邮箱不能为空");
                        //背景变色
                        error(mEt_email);
                        if( !EmailFormat(mEmail) ){
                            mEt_email.setError("邮箱格式不对");
                            //背景变色
                            error(mEt_email);
                        }
                        return;
                    }else{
                        focused(mEt_email);

                    }
                    if(CmdBaseActivity.getInstance().isConnected())
                        CmdBaseActivity.getInstance().closeSocket();

                    initClientCMDhelperRegsiter();

//                  loginDialog(RegsiterActivity.this,mBt_regsiter,"注册中");
                  T.showShort(mContext,"注册中");




                }

            }
        });
    }

    private void error(EditText et_phone) {
        et_phone.setBackgroundDrawable(getResources().getDrawable(R.drawable.edit_edittext_error));
        //抖动
        Animation shake = AnimationUtils.loadAnimation(mContext, R.anim.shake);
        et_phone.startAnimation(shake);
    }
    private void focused(EditText et_username) {
        et_username.setBackgroundDrawable(getResources().getDrawable(R.drawable.edit_edittext_focused));
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_regsiter);


        //手机
        mEt_phone = (EditText) findViewById(R.id.login_et_phone);
        //邮箱
        mEt_email = (EditText) findViewById(R.id.login_et_email);
        //密码
        mEt_password = (EditText) findViewById(R.id.login_et_password);
        //登录注册
        mBt_regsiter = (Button) findViewById(R.id.btn_regsiter_go);
        //是否显示密码  cb_is_visible
        mCb_visible = (CheckBox) findViewById(R.id.cb_is_visible);
        mDialog = new ProgressDialog(RegsiterActivity.this);
        super.initView();
    }

    @Override
    public void next() {
//   不允许右边动画     super.next();
    }

    @Override
    public void prev() {
        super.prev();
    }

    @Override
    public void onReceiveCommand(ServerCommand cmd) {
        switch (cmd.CMDByte) {

            case CMDFF_ServerException.Command:

                CMDFF_ServerException cmdff_serverException = (CMDFF_ServerException) cmd;
                final String info = cmdff_serverException.info;
                UIUtils.postTaskSafely(new Runnable() {
                    @Override
                    public void run() {
                        T.showShort(mContext, info);

                    }
                });
                break;
            case CMD01_ServerLoginPermit.Command:
////提示diolog 等连接成功后 消除
//                                            System.out.println("************RegsiterActivity********************连接服务器成功");
                CMD01_ServerLoginPermit cmd01_ServerLoginPermit = (CMD01_ServerLoginPermit) cmd;
                CmdBaseActivity.getInstance().mHelper.setKey(cmd01_ServerLoginPermit.key);
                LogUtils.i("登录Helper给了key");
//
//                CMD0A_Register cmd0a =new CMD0A_Register(mPhone+"%000",mPassword,mPhone,mEmail, Constants.appEnv);
                CMD0A_Register cmd0a =new CMD0A_Register(mPhone+Constants.FACTORY_ID,mPassword,mPhone,mEmail, Constants.appEnv);
                CmdBaseActivity.getInstance().sendCmd(cmd0a);
                break;
            case CMD0B_ServerRegisterResult.Command:
                //TODO
                //成功就跳转到登录界面
                LogUtils.s("收到注册返回信息:"+cmd.toString());
                CMD0B_ServerRegisterResult cmd0B_serverRegisterResult= (CMD0B_ServerRegisterResult)cmd;

                if(cmd0B_serverRegisterResult.result){
                    //注册成功 跳转到登录界面 提示登录帐号是手机
                UIUtils.postTaskSafely(new TimerTask() {
                    @Override
                    public void run() {
                        T.showShort(RegsiterActivity.this,"注册成功,登录帐号是手机号");
                    }
                });
                    save(cmd0B_serverRegisterResult.info.phone,mPassword);
                    bt_login(null);
                    try {
                        DataUtils.getInstance().mDb.saveOrUpdate(new UInfo(2+"",cmd0B_serverRegisterResult.info,"注册信息"));
                    } catch (DbException e) {
                        e.printStackTrace();
                    }

                    helper.closeServer();
                }else{
                    T.showShort(RegsiterActivity.this,"注册失败");
                }
                break;
        }

    }

    @Override
    public void onSocketConnected() {

    }

    @Override
    public void onSocketClosed() {

    }


//注册的信息 发送命令
//    void receive01_setLoginParam()
//    {
//        System.out.println("RegisterActivity NetWorkHelper::CMD01_ServerLoginPermit");
//        CMD0A_Register localCMD0A_Register = new CMD0A_Register(this.phone + "%20000", this.pwd, this.phone, this.Email, GlobalData.appEnv);
//        System.out.println("czq     0A");
//        PublicCmdHelper.getInstance().sendCmd(localCMD0A_Register);
//    }

    private boolean EmailFormat(String eMAIL1) {//邮箱判断正则表达式
        Pattern pattern = Pattern
                .compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
        Matcher mc = pattern.matcher(eMAIL1);
        return mc.matches();
    }
    private ClientCMDHelper helper ;
    private void initClientCMDhelperRegsiter() {

        if(helper==null){
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
}
