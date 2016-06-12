package com.vanhitech.vanhitech.controller.setting;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.vanhitech.protocol.cmd.ServerCommand;
import com.vanhitech.protocol.cmd.client.CMD14_ModifyUserInfo;
import com.vanhitech.protocol.cmd.server.CMD15_ServerModifyUserResult;
import com.vanhitech.protocol.cmd.server.CMDFF_ServerException;
import com.vanhitech.vanhitech.R;
import com.vanhitech.vanhitech.base.BaseActivityController;
import com.vanhitech.vanhitech.base.CmdBaseActivity;
import com.vanhitech.vanhitech.conf.LoginConstants;
import com.vanhitech.vanhitech.manager.ActivityTitleManager;
import com.vanhitech.vanhitech.utils.FileUtils;
import com.vanhitech.vanhitech.utils.StringUtils;
import com.vanhitech.vanhitech.utils.T;
import com.vanhitech.vanhitech.utils.UIUtils;
import com.vanhitech.vanhitech.views.LightRgbTagView;

import java.io.File;
import java.io.FileOutputStream;

/**
 * 创建者     heyn
 * 创建时间   2016/3/18 14:42
 * 描述	      ${TODO}
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class SettingUpdateCodeActivity extends BaseActivityController {
    public LightRgbTagView mLrt_tag;
    private EditText mEtOldPwd;
    private EditText mEtNewPwd;
    private EditText mEtConfirmPwd;
    private String mOldPwd;
    private String mNewPwd;
    private String mConfirmPwd;
    private Context mContext;
    private CheckBox mCbIsOldPwd;
    private CheckBox mCbIsNewPwd;
    private CheckBox mCbIsConfirmPwd;
    private boolean flagNew = true;
    private boolean flagConfi =true;
    private boolean flagOld=true;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_set_updatecode);
        ActivityTitleManager.getInstance().init(this);
        ActivityTitleManager.getInstance().showCommonTitle();
//        ActivityTitleManager.getInstance().changehelpText(getResources().getString(R.string.save));
        ActivityTitleManager.getInstance().changehelpText(StringUtils.getString(SettingUpdateCodeActivity.this,R.string.save));
        ActivityTitleManager.getInstance().changeTitle(UIUtils.getString(R.string.modifyPwd));
        assignViews();
        mContext = this;
    }


    private void assignViews() {
        mEtOldPwd = (EditText) findViewById(R.id.et_oldPwd);
        mEtNewPwd = (EditText) findViewById(R.id.et_newPwd);
        mEtConfirmPwd = (EditText) findViewById(R.id.et_confirmPwd);
        mCbIsOldPwd = (CheckBox) findViewById(R.id.cb_is_oldPwd);
        mCbIsNewPwd = (CheckBox) findViewById(R.id.cb_is_newPwd);
        mCbIsConfirmPwd = (CheckBox) findViewById(R.id.cb_is_confirmPwd);

    }


    @Override
    public void initData() {
        super.initData();

    }

    @Override
    public void initListener() {
        super.initListener();
        mCbIsOldPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagOld){
                    mEtOldPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    flagOld=false;
                }else{
                    mEtOldPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    flagOld=true;
                }
            }
        });

        mCbIsNewPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagNew){
                    mEtNewPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    flagNew=false;
                }else{
                    mEtNewPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    flagNew=true;
                }
            }
        });


        mCbIsConfirmPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagConfi){
                    mEtConfirmPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    flagConfi=false;
                }else{
                    mEtConfirmPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    flagConfi=true;
                }
            }
        });
    }

    @Override
    public void initEvent() {
        super.initEvent();
        ActivityTitleManager.getInstance().help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 *  2 获取旧密码 对比刚登录的密码是否正确
                 *
                 * 1 对比新密码和确定新密码是否一样
                 *
                 * 3发送
                 *
                 *     <string name="passnotnull">密码不能为空!</string>
                 <string name="oldpassnotnull">旧密码不能为空!</string>
                 <string name="newpassnotnull">新密码不能为空!</string>
                 <string name="oldpasserror">旧密码输入错误!</string>
                 <string name="twoinputerror">两次输入的新密码不一致，请重新输入!</string>
                 */

                mOldPwd = mEtOldPwd.getText().toString().trim();
                mNewPwd = mEtNewPwd.getText().toString().trim();
                mConfirmPwd = mEtConfirmPwd.getText().toString().trim();
                String[] s = router();
                //s[0], s[1];
                if (StringUtils.isEmpty(mOldPwd)||!mOldPwd.equals(s[1])) {
//                    T.showLong(SettingUpdateCodeActivity.this,getResources().getString(R.string.save));
                    T.showLong(SettingUpdateCodeActivity.this,R.string.oldpasserror);
                return;
                }
                if (StringUtils.isEmpty(mNewPwd)||StringUtils.isEmpty(mConfirmPwd)) {
                    T.showLong(SettingUpdateCodeActivity.this,R.string.twoinputerror);
                return;
                }
                CmdBaseActivity.getInstance().mHelper.setCommandListener(SettingUpdateCodeActivity.this);

                //发送命令
                LoginConstants.userInfo.pass = mNewPwd;
                CMD14_ModifyUserInfo localCMD14_ModifyUserInfo = new CMD14_ModifyUserInfo(true, mOldPwd, LoginConstants.userInfo);
                CmdBaseActivity.getInstance().sendCmd(localCMD14_ModifyUserInfo);



            }
        });


//        CmdBaseActivity.getInstance().mHelper.setCommandListener(new ClientCMDHelper.CommandListener() {
//            @Override
//            public void onReceiveCommand(ServerCommand serverCommand) {
//
//            }
//
//            @Override
//            public void onSocketConnected() {
//
//            }
//
//            @Override
//            public void onSocketClosed() {
//
//            }
//        });
    }
    /**
     * 保存帐号
     */



    public void save(String account, String password) {
        LoginConstants.USERNAME=account;
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
    @Override
    public void onReceiveCommand(ServerCommand cmd) {


        switch (cmd.CMDByte) {

            case CMDFF_ServerException.Command:

                CMDFF_ServerException cmdff_serverException = (CMDFF_ServerException) cmd;
                final String info = cmdff_serverException.info;
                UIUtils.postTaskSafely(new Runnable() {
                    @Override
                    public void run() {
//                clickTopLeftBtn(mContext, null, info, null, true);
//                T.show(mContext,);
                    }
                });
                break;
            case CMD15_ServerModifyUserResult.Command:
                final CMD15_ServerModifyUserResult cmd15 = (CMD15_ServerModifyUserResult) cmd;
                UIUtils.postTaskSafely(new Runnable() {
                    @Override
                    public void run() {
                        if(cmd15.result){//success
                      T.show(mContext,R.string.success,2000);
//                        clickTopLeftBtn(mContext, null, StringUtils.getString(mContext,R.string.success), null, true);
                       save(cmd15.info.phone,mNewPwd);
                          finish();
                        }else{
//                        clickTopLeftBtn(mContext, null, StringUtils.getString(mContext,R.string.error), null, true);
                      T.show(mContext,R.string.error,2000);

                        }
                    }
                });


                break;

        }
    }

}
