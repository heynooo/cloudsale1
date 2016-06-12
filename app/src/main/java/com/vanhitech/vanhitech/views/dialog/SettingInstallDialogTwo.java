package com.vanhitech.vanhitech.views.dialog;

import android.content.Context;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.flyco.animation.Attention.Swing;
import com.flyco.dialog.widget.base.BaseDialog;
import com.vanhitech.vanhitech.R;
import com.vanhitech.vanhitech.conf.LoginConstants;
import com.vanhitech.vanhitech.utils.ViewFindUtils;




public class SettingInstallDialogTwo extends BaseDialog<SettingInstallDialogTwo> {

    private EditText setEtPassword1;
    private CheckBox cbIsVisible1;
    public Button btConfigNextBtn;
    private boolean flag =true;
    public String mPwd;
    private Context mContext;

    public SettingInstallDialogTwo(Context context) {
        super(context);
        mContext=context;
    }
    public SettingInstallDialogTwo(Context context, Boolean flag) {
        super(context,flag);
        mContext=context;
    }






    @Override
    public View onCreateView() {
        widthScale(1f);
        heightScale(1f);
        superDismiss();

        showAnim(new Swing());

        // dismissAnim(this, new ZoomOutExit());
        View inflate = View.inflate(mContext, R.layout.activity_set_installdevice_ppw2, null);

        setEtPassword1 = ViewFindUtils.find(inflate, R.id.set_et_password1);
        cbIsVisible1 = ViewFindUtils.find(inflate,R.id.cb_is_visible1);
        btConfigNextBtn = ViewFindUtils.find(inflate,R.id.tv_config_next_btn);

        return inflate;
    }

    @Override
    public void setUiBeforShow() {

        btConfigNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPwd = setEtPassword1.getText().toString().trim();
                //数据库保存密码

//                if (TextUtils.isEmpty(mPwd)) {
//                    setEtPassword1.setError("路由密码还没设置");
//                    //抖动
//                    Animation shake = AnimationUtils.loadAnimation(mContext, R.anim.shake);
//                    setEtPassword1.startAnimation(shake);
//                    return;
//                }
                //暂时没用办法解决密码是否正确的问题
                if(!mPwd.isEmpty())    {
                LoginConstants.lumm=mPwd;
//               UInfo uInfo= DataUtils.getInstance().getUInfo(LoginConstants.USERNAME);
//                uInfo.lumm=mPwd;
//                DataUtils.getInstance().save(uInfo);
                }
                dismiss();//启动下一步
            }
        });
//可见不可见密码

        cbIsVisible1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag){
                    setEtPassword1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    flag=false;
                }else{
                    setEtPassword1.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    flag=true;
                }
            }
        });




}

    private String sayMyName() {
    return mPwd;
    }
}
