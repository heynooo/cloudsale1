package com.vanhitech.vanhitech.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.flyco.animation.BaseAnimatorSet;
import com.flyco.animation.BounceEnter.BounceRightEnter;
import com.flyco.animation.SlideExit.SlideLeftExit;
import com.flyco.dialog.widget.popup.BubblePopup;
import com.vanhitech.protocol.ClientCMDHelper;
import com.vanhitech.protocol.cmd.client.CMD06_QueryDeviceStatus;
import com.vanhitech.protocol.object.device.Device;
import com.vanhitech.protocol.object.device.SceneDevice;
import com.vanhitech.vanhitech.R;
import com.vanhitech.vanhitech.conf.LoginConstants;

/**
 * 创建者     heyn
 * 创建时间   2016/3/18 0:17
 * 描述	      ${TODO}
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class BaseActivityController extends CmdBaseActivity {


    public TextView mTvTitle;
    private GestureDetector mGda;
    public Context mContext;
    public Activity mActivity;
    public String mName;
    public int mSimageno;
    public String mId;
    public Device mDevice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        systemBarTint();
        initView();
        initData();//先获取数据
        initTitleBar();
        initEvent();
        initListener();


    }

    public void initData() {
//        ActivityTitleManager.getInstance().init(this);
//        ActivityTitleManager.getInstance().showCommonTitle();
    }

    public void getConterIntent() {
        Intent intent = getIntent();
        mId = intent.getStringExtra("id");
        mName = intent.getStringExtra("name");
        mSimageno = intent.getExtras().getInt("imageno");
    }

    public void initListener() {

//        ActivityTitleManager.getInstance().goback.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mActivity.finish();
//            }
//        });
    }

    public void initEvent() {

    }


    public void initView() {
        mContext = this;
        mActivity = this;
    }

    public void initTitleBar() {
//        mTvTitle.setText("云海智能家居");
    }

    @Override
    public void finish() {

//        super.finish();
//        Intent intent =new Intent();
//        Constants.isRid=R.id.content_rb_setting;
//            intent.setClass(this, MainActivity.class);
//            this.startActivity(intent);//设置一个标志，让那边直接finish 不用判断
//        T.hideToast();
        LoginConstants.fresh = true;
        LoginConstants.timgingfresh = false;
        super.finish();
        overridePendingTransition(R.animator.preventeranim, R.animator.prevexitanim);


    }

    public BubblePopup mBubblePopup;

    public void Dialog(Context context, Button bt_login, String text) {
        View infView = View.inflate(context, R.layout.test_popup_bubble_image, null);
        TextView textView = (TextView) infView.findViewById(R.id.tv_bubble);
        textView.setText(text);
        mBubblePopup = new BubblePopup(context, infView);
        mBubblePopup.anchorView(bt_login)
                .showAnim(new BounceRightEnter())
                .dismissAnim(new SlideLeftExit())
//                .autoDismiss(true)
                .show();
    }




    private BaseAnimatorSet mBasIn;
    private BaseAnimatorSet mBasOut;

    public void setBasIn(BaseAnimatorSet bas_in) {
        this.mBasIn = bas_in;
    }

    public void setBasOut(BaseAnimatorSet bas_out) {
        this.mBasOut = bas_out;
    }


    public Boolean toTrueFalse(String b) {
        Boolean is;
        if (b == null || b.equals("0")) {
            is = false;
        } else {
            is = true;
        }
        return is;
    }

    private void inten(Context context, Class<?> ActivityClass, String id) {
        Intent intent = new Intent();
        intent.setClass(context, ActivityClass);
        intent.putExtra("mDeviceId", id);
        startActivity(intent);
        finish();
    }

    public void openActivity(Class<?> pClass, Device device, Activity activity, Boolean finish) {
        Intent _Intent = new Intent();
        _Intent.setClass(this, pClass);
        _Intent.putExtra("name", device.name);
        _Intent.putExtra("id", device.id);
        _Intent.putExtra("mDeviceId", device.id);
        activity.startActivity(_Intent);
        activity.overridePendingTransition(R.animator.nextenteranim, R.animator.nextexitanim);
        if (finish) {
            activity.finish();
        }
    }

    public void openActivity(Class<?> pClass, Activity activity, Boolean finish) {
        Intent _Intent = new Intent();
        _Intent.setClass(mActivity, pClass);
        activity.startActivity(_Intent);
        activity.overridePendingTransition(R.animator.nextenteranim, R.animator.nextexitanim);
        if (finish) {
            activity.finish();
        }
    }

    /**
     *
     * @param pClass
     * @param sceneDevice
     * @param activity
     * @param finish
     */
    public void openActivity(Class<?> pClass, SceneDevice sceneDevice,String scenename, Activity activity, Boolean finish) {
        Intent _Intent = new Intent();
        _Intent.setClass(mActivity, pClass);
    /* 通过Bundle对象存储需要传递的数据 */
        Bundle bundle = new Bundle();
        bundle.putString("sceneid", sceneDevice.sceneid);
        bundle.putBoolean("enabled", sceneDevice.enabled);
        bundle.putString("scenename", scenename);
    /*把bundle对象assign给Intent*/
        _Intent.putExtras(bundle);
        activity.startActivity(_Intent);
        activity.overridePendingTransition(R.animator.nextenteranim, R.animator.nextexitanim);
        if (finish) {
            activity.finish();
        }
    }

    /**]
     * 发送查询设备信息命令
     * @param id
     * @param listener
     */
    public void sendCmd06(String id, ClientCMDHelper.CommandListener  listener) {
        CmdBaseActivity.getInstance().mHelper.setCommandListener(listener);
        CMD06_QueryDeviceStatus cmd06 = new CMD06_QueryDeviceStatus(id);
        CmdBaseActivity.getInstance().sendCmd(cmd06);
    }




}
