package com.vanhitech.vanhitech.controller.setting;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.EditText;

import com.vanhitech.vanhitech.R;
import com.vanhitech.vanhitech.base.BaseActivityController;
import com.vanhitech.vanhitech.manager.ActivityTitleManager;

/**
 * 创建者     heyn
 * 创建时间   2016/3/18 14:42
 * 描述	      ${TODO}
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class SettingForgetCodeActivity extends BaseActivityController {
    private EditText mNewPw;
    private EditText mNewPwAgain;


    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_set_forgetcode);
        ActivityTitleManager.getInstance().init(this);
        ActivityTitleManager.getInstance().showCommonTitle();
        ActivityTitleManager.getInstance().changeTitle("忘记密码");
        ActivityTitleManager.getInstance().changehelpText(getResources().getString(R.string.save));
        assignViews();
    }
    private void assignViews() {
        mNewPw = (EditText) findViewById(R.id.new_pw);
        mNewPwAgain = (EditText) findViewById(R.id.new_pw_again);
    }


    @Override
    public void initData() {
        super.initData();

    }

    @Override
    public void initEvent() {
        super.initEvent();

    }
}
