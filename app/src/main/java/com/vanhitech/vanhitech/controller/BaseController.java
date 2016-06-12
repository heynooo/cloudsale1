package com.vanhitech.vanhitech.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.vanhitech.protocol.object.SceneMode;
import com.vanhitech.protocol.object.device.Device;
import com.vanhitech.protocol.object.device.SceneDevice;
import com.vanhitech.vanhitech.R;
import com.vanhitech.vanhitech.conf.Constants;

/**
 * 创建者     heyn
 * 创建时间   2016/3/14 21:54
 * 描述	      ${TODO}
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public abstract class BaseController  {
    public View		mRootView;	//提供的视图
    public Context	mContext;
    public Activity mActivity;
    private BitmapUtils mBitmapUtils;
    @ViewInject(R.id.base_content_container)
    FrameLayout mFlContent;

    @ViewInject(R.id.base_ib_menu)
    Button mIbMenu;

    @ViewInject(R.id.base_tv_title)
    TextView mTvTitle;

    public BaseController(Context context,Activity activity) {
        mActivity = activity;
        mContext = context;
        mRootView = initView(mContext);
        mBitmapUtils = new BitmapUtils(mContext);//图片加载处理
    }

    public BaseController() {

    }

    /**
     * 初始化当前controller可以提供的视图
     * 必须实现
     *
     * @return
     */
    public  View initView(Context context){
        View view = View.inflate(mContext, R.layout.layout_base_controller, null);
        ViewUtils.inject(this,view);
        /**
         *
         */
        mFlContent = (FrameLayout) view.findViewById(R.id.base_content_container);
        mIbMenu = (Button) view.findViewById(R.id.base_ib_menu);
        mTvTitle = (TextView) view.findViewById(R.id.base_tv_title);


        //操作标题
        initTitleBar();
        //操作内容区域
        mFlContent.addView(initContentView(mContext));
        //操作右上角的菜单 //TODO
        mIbMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SimpleCustomPop simpleCustomPop =new SimpleCustomPop(mContext);
//                simpleCustomPop
//                        .anchorView(mIbMenu)
////                        .offset(-10, 5)
//                        .gravity(Gravity.BOTTOM)
//                        .alignCenter(true)
////                        .showAnim(new BounceTopEnter())
////                        .dismissAnim(new SlideTopExit())
////                        .dimEnabled(false)
//                        .show();
            }
        });

        return  view;
    }



    /**
     * 初始化容器里面应该具体放置的视图
     * @return
     */
    public abstract View initContentView(Context context);

    /**
     * 对标题进行赋值操作
     * 基类不知道怎么赋值,给子类操作
     */
    public abstract void initTitleBar() ;


    /**
     * 1.加载数据,进行视图的刷新
     * 2.视图和数据的绑定
     */
    public void initData() {

    }



    /**
     * 跳转
     */
    public void startActivity(Class type,Context context) {
        Constants.isPrompt=true;
        Intent intent = new Intent(context, type);
        context.startActivity(intent);
        mActivity.overridePendingTransition(R.animator.nextenteranim, R.animator.nextexitanim);
        mActivity.finish();
        Constants.isPrompt=false;
    }

    /**
     * 携带设备id,name
     * @param pClass
     * @param device
     * @param activity
     * @param finish
     */
    public void openActivity(Class<?> pClass, Device device, Activity activity, Boolean finish) {
        Intent _Intent = new Intent();
        _Intent.setClass(mActivity, pClass);
        _Intent.putExtra("name",device.name);
        _Intent.putExtra("id", device.id);
        activity.startActivity(_Intent);
        activity.overridePendingTransition(R.animator.nextenteranim,R.animator.nextexitanim);
        if(finish){
            activity.finish();
        }
    }

    /**
     * 不携带数据跳转
     * @param pClass
     * @param activity
     * @param finish
     */
    public void openActivity(Class<?> pClass, Activity activity, Boolean finish) {
        Intent _Intent = new Intent();
        _Intent.setClass(mActivity, pClass);
        activity.startActivity(_Intent);
        activity.overridePendingTransition(R.animator.nextenteranim,R.animator.nextexitanim);
        if(finish){
            activity.finish();
        }
    }

    /**
     * 携带场景模式的id,name跳转
     * @param pClass
     * @param sceneMode
     * @param activity
     * @param finish
     */
    public void openActivity(Class<?> pClass, SceneMode sceneMode, Activity activity, Boolean finish) {
        Intent _Intent = new Intent();
        _Intent.setClass(mActivity, pClass);
        _Intent.putExtra("id", sceneMode.id);
        _Intent.putExtra("name",sceneMode.name);
        _Intent.putExtra("imageno", sceneMode.imageno);
        activity.startActivity(_Intent);
        activity.overridePendingTransition(R.animator.nextenteranim,R.animator.nextexitanim);
        if(finish){
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
    public void openActivity(Class<?> pClass, SceneDevice sceneDevice, Activity activity, Boolean finish) {
        Intent _Intent = new Intent();
        _Intent.setClass(mActivity, pClass);
    /* 通过Bundle对象存储需要传递的数据 */
        Bundle bundle = new Bundle();
        bundle.putString("sceneid", sceneDevice.sceneid);
        bundle.putBoolean("enabled", sceneDevice.enabled);
    /*把bundle对象assign给Intent*/
        _Intent.putExtras(bundle);
        activity.startActivity(_Intent);
        activity.overridePendingTransition(R.animator.nextenteranim, R.animator.nextexitanim);
        if (finish) {
            activity.finish();
        }
    }



}
