package com.vanhitech.vanhitech.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.vanhitech.protocol.cmd.ServerCommand;
import com.vanhitech.protocol.cmd.client.CMD46_ForgetPass;
import com.vanhitech.vanhitech.R;
import com.vanhitech.vanhitech.base.CmdBaseActivity;
import com.vanhitech.vanhitech.utils.T;

/**
 * 创建者     heyn
 * 创建时间   2016/3/15 14:22
 * 描述	      ${TODO}
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class ForgetActivity extends BaseActivity {
    private LinearLayout layoutLogintest;
    private EditText loginEtUsername;
    private EditText loginEtEmail;
    private Button btnSubmit;
    private ImageView imageView2;
    private Button btLogin;

    @Override
    public void initEvent() {

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = loginEtUsername.getText().toString().trim();
                String email = loginEtEmail.getText().toString().trim();
                CMD46_ForgetPass localCMD46_ForgetPass = new CMD46_ForgetPass(name + "%10000", email, "zh_cn");
                CmdBaseActivity.getInstance().sendCmd(localCMD46_ForgetPass);
                T.showLong(ForgetActivity.this, "请梢后");
            }
        });

        super.initEvent();
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_forget);
        assignViews();
        super.initView();

    }

    @Override
    public void next() {

    }

    @Override
    public void prev() {
        super.prev();
    }

    @Override
    public void onReceiveCommand(ServerCommand serverCommand) {

    }

    @Override
    public void onSocketConnected() {

    }

    @Override
    public void onSocketClosed() {

    }

    private void assignViews() {
        layoutLogintest = (LinearLayout) findViewById(R.id.layout_logintest);
        loginEtUsername = (EditText) findViewById(R.id.login_et_username);
        loginEtEmail = (EditText) findViewById(R.id.login_et_email);
        btnSubmit = (Button) findViewById(R.id.btn_submit);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        btLogin = (Button) findViewById(R.id.bt_login);


    }
}
