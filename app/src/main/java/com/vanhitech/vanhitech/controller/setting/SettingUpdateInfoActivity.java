package com.vanhitech.vanhitech.controller.setting;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.EditText;

import com.vanhitech.protocol.cmd.ServerCommand;
import com.vanhitech.protocol.cmd.client.CMD14_ModifyUserInfo;
import com.vanhitech.protocol.cmd.client.CMD16_QueryUserInfo;
import com.vanhitech.protocol.cmd.server.CMD15_ServerModifyUserResult;
import com.vanhitech.protocol.cmd.server.CMD17_ServerQueryUserResult;
import com.vanhitech.protocol.cmd.server.CMDFF_ServerException;
import com.vanhitech.protocol.object.UserInfo;
import com.vanhitech.vanhitech.R;
import com.vanhitech.vanhitech.base.BaseActivityController;
import com.vanhitech.vanhitech.base.CmdBaseActivity;
import com.vanhitech.vanhitech.manager.ActivityTitleManager;
import com.vanhitech.vanhitech.utils.StringUtils;
import com.vanhitech.vanhitech.utils.T;
import com.vanhitech.vanhitech.utils.UIUtils;

import java.util.TimerTask;

/**
 * 创建者     heyn
 * 创建时间   2016/3/18 14:42
 * 描述	      ${TODO}
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class SettingUpdateInfoActivity extends BaseActivityController {
    private EditText mTvUsername;
    private EditText mEtEmail;
    private String mPass;
    private UserInfo mUserinfo;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_set_updateinfo);
        ActivityTitleManager.getInstance().init(this);
        ActivityTitleManager.getInstance().showCommonTitle();
        ActivityTitleManager.getInstance().changeTitle("修改信息");
        ActivityTitleManager.getInstance().changehelpText("保存");
        assignViews();

        CmdBaseActivity.getInstance().mHelper.setCommandListener(SettingUpdateInfoActivity.this);
        CMD16_QueryUserInfo localCMD16_QueryUserInfo = new CMD16_QueryUserInfo();

        CmdBaseActivity.getInstance().sendCmd(localCMD16_QueryUserInfo);
    }


    private void assignViews() {
        mTvUsername = (EditText) findViewById(R.id.tv_username);
        mEtEmail = (EditText) findViewById(R.id.et_email);
    }


    @Override
    public void initData() {
        super.initData();

    }

    @Override
    public void initEvent() {
        super.initEvent();

        /**
         *    GlobalData.userInfo.email = ModifyMsgActivity.this.et_email.getText().toString();
         CMD14_ModifyUserInfo localCMD14_ModifyUserInfo = new CMD14_ModifyUserInfo(false, ModifyMsgActivity.this.sPreferenceUtil.getPw(), GlobalData.userInfo);
         PublicCmdHelper.getInstance().sendCmd(localCMD14_ModifyUserInfo);
         */

        ActivityTitleManager.getInstance().help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEtEmail.getText().toString().trim();
                if (StringUtils.isEmpty(email)) {
                    //不能为空
                    T.show(mContext, R.string.inputemail, 2000);
                    return;
                }

                //输入正确的邮箱
                if (!StringUtils.verEmail(email)) {
                    T.show(mContext, R.string.inputcorrectemail, 2000);
                    return;
                }
                mUserinfo.email = email;
                CMD14_ModifyUserInfo localCMD14_ModifyUserInfo = new CMD14_ModifyUserInfo(false, mPass, mUserinfo);
                CmdBaseActivity.getInstance().sendCmd(localCMD14_ModifyUserInfo);
            }
        });

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
                        if (cmd15.result) {//success
                            T.show(mContext, R.string.success, 2000);
                            finish();
                        } else {
                            T.show(mContext, R.string.error, 2000);

                        }
                    }
                });


                break;


            case CMD17_ServerQueryUserResult.Command:
                final CMD17_ServerQueryUserResult localCMD17_ServerQueryUserResult = (CMD17_ServerQueryUserResult) cmd;
                final int i = localCMD17_ServerQueryUserResult.userInfo.name.indexOf("%");
                mUserinfo = localCMD17_ServerQueryUserResult.userInfo;
                mPass = localCMD17_ServerQueryUserResult.userInfo.pass;

                UIUtils.postTaskSafely(new TimerTask() {
                    @Override
                    public void run() {
                        mTvUsername.setText(localCMD17_ServerQueryUserResult.userInfo.name.substring(0, i));
                        mEtEmail.setText(localCMD17_ServerQueryUserResult.userInfo.email);
                    }
                });
                break;


        }
    }
}
