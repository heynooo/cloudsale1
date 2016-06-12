package com.vanhitech.vanhitech.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.vanhitech.vanhitech.R;
import com.vanhitech.vanhitech.activity.LoginActivity;
import com.vanhitech.vanhitech.base.MyApplication;
import com.vanhitech.vanhitech.controller.setting.SettingExpQuestionActivity;
import com.vanhitech.vanhitech.controller.setting.SettingInstallDeviceActivity;
import com.vanhitech.vanhitech.controller.setting.SettingPertatinActivity;
import com.vanhitech.vanhitech.controller.setting.SettingUpdateCodeActivity;
import com.vanhitech.vanhitech.controller.setting.SettingUpdateInfoActivity;

/**
 * 创建者     heyn
 * 创建时间   2016/3/14 21:39
 * 描述	     1.提供视图,2接收数据,加载数据,3数据和视图的绑定
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class settingController extends BaseController {
    @ViewInject(R.id.test)
    Button test;

    @ViewInject(R.id.deploydevice)
    RelativeLayout deploydevice;

//    @ViewInject(R.id.forgetCode)
//    RelativeLayout forgetCode;

    @ViewInject(R.id.updateCode)
    RelativeLayout updatecode;

    @ViewInject(R.id.updateInfo)
    RelativeLayout updateInfo;

    @ViewInject(R.id.exampleQuestion)
    RelativeLayout exampleQuestion;


    @ViewInject(R.id.pertain)
    RelativeLayout pertain;

    public settingController(Context context, Activity activity) {
        super(context, activity);
//        test.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////-----------test start----------------------------
//
//                Intent intent = new Intent();
////                intent.setClass(mContext, test.class); 跳转测试
//                intent.setClass(mContext, LightRgbActivity.class);
//                mActivity.startActivity(intent);
////-----------test end----------------------------
//            }
//        });
        initEvent();

    }

    private void initEvent() {
        View.OnClickListener listener = new View.OnClickListener() {

            private Intent mIntent;


            @Override
            public void onClick(View v) {
                if (mIntent == null) {
                    mIntent = new Intent();
                }
                switch (v.getId()) {
                    case R.id.deploydevice://配置设备
                        mIntent.setClass(mContext, SettingInstallDeviceActivity.class);

                        break;
//                    case R.id.forgetCode: //忘记密码
//                        mIntent.setClass(mContext, SettingForgetCodeActivity.class);
////                        mActivity.startActivity(mIntent);
//                        break;
                     case R.id.updateCode: //修改密码
                        mIntent.setClass(mContext, SettingUpdateCodeActivity.class);
//                        mActivity.startActivity(mIntent);
                        break;
                    case R.id.updateInfo: //修改信息
//TODO
                        mIntent.setClass(mContext, SettingUpdateInfoActivity.class);
//                        mActivity.startActivity(mIntent);
                        break;
                    case R.id.exampleQuestion: //常见问题
                        mIntent.setClass(mContext, SettingExpQuestionActivity.class);
//                        mActivity.startActivity(mIntent);
                        break;
                    case R.id.pertain: //关于
                        mIntent.setClass(mContext, SettingPertatinActivity.class);
//                        mActivity.startActivity(mIntent);
                        break;
//                    case R.id.test: //退出
//                        mIntent.setClass(mContext, testActivity.class);//TODO
////                        mActivity.startActivity(mIntent);



//                        break;
                    default:
                        break;
                }
                mActivity.startActivity(mIntent);
                mActivity.overridePendingTransition(R.animator.nextenteranim, R.animator.nextexitanim);
//                Constants.isPrompt=true;//这里让finish不提示
//                mActivity.finish();
//                Constants.isPrompt=false;

                //做事件回调处理
                //TODO
            }
        };

        deploydevice.setOnClickListener(listener);
//        forgetCode.setOnClickListener(listener);
        updatecode.setOnClickListener(listener);
        updateInfo.setOnClickListener(listener);
        exampleQuestion.setOnClickListener(listener);
        pertain.setOnClickListener(listener);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                NormalDialogCustomAttr();
                NormalDialogStyleTwo();
            }
        });


    }

    /**
     * @param context
     * @return
     * @des 初始化容器布局里面具体应该放置的视图
     */

    @Override
    public View initContentView(Context context) {

        /**
         * 布局文件， 然后找到孩子  显示界面
         */
        View view = View.inflate(mContext, R.layout.setting_controller_layout, null);
        ViewUtils.inject(this, view);
        return view;

    }

    //  mViewPager.setCurrentItem
    @Override
    public void initTitleBar() {
        mTvTitle.setText("设置");

    }

    /**
     * 1.加载数据,进行视图的刷新
     * 2.视图和数据的绑定
     */
    public void initData() {


    }

    private void NormalDialogCustomAttr() {
        final NormalDialog dialog = new NormalDialog(mContext);
        dialog.isTitleShow(false)//
                .bgColor(Color.parseColor("#383838"))//
                .cornerRadius(5)//
                .content("是否确定退出登录?")//
                .contentGravity(Gravity.CENTER)//
                .contentTextColor(Color.parseColor("#ffffff"))//
                .dividerColor(Color.parseColor("#222222"))//
                .btnTextSize(15.5f, 15.5f)//
                .btnTextColor(Color.parseColor("#aebecc"), Color.parseColor("#aebecc"))//
                .btnPressColor(Color.parseColor("#2B2B2B"))//
                .widthScale(0.85f)//
                .show();

        dialog.setOnBtnClickL(
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                    }
                },
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
//                        startActivity(LoginActivity.class,mContext);
                        openActivity(LoginActivity.class,mActivity,true);
                        dialog.dismiss();
                    }
                });
    }
    private void NormalDialogStyleTwo() {

        final NormalDialog dialog = new NormalDialog(mContext);

        dialog.content("是否确定退出帐号。")//
                .style(NormalDialog.STYLE_TWO)//
                .titleTextSize(23)//
                .show();

        dialog.setOnBtnClickL(
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                    }
                },
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        MyApplication.setIsPrompt(true);
                        openActivity(LoginActivity.class,mActivity,true);
                        dialog.dismiss();
                        mActivity.finish();
                    }
                });

    }

}
