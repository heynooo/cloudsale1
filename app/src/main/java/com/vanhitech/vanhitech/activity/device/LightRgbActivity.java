package com.vanhitech.vanhitech.activity.device;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;

import com.vanhitech.protocol.object.device.LightRGBDevice;
import com.vanhitech.vanhitech.R;
import com.vanhitech.vanhitech.base.BaseActivityController;
import com.vanhitech.vanhitech.fragment.LightRgbFragment;
import com.vanhitech.vanhitech.fragment.LightWhiteFragment;
import com.vanhitech.vanhitech.manager.ActivityTitleManager;
import com.vanhitech.vanhitech.utils.DataUtils;
import com.vanhitech.vanhitech.views.LightRgbTagView;

/**
 * 创建者     heyn
 * 创建时间   2016/3/18 10:52
 * 描述	      ${TODO}
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class LightRgbActivity extends BaseActivityController {

    public LightRgbTagView mLrt_tag;

    private FrameLayout fl_content;
    private FragmentManager fragmentManager;
    private LightRgbFragment mLightRgbFragment;
    private LightWhiteFragment mLightWhiteFragment;
    private String mName;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);


    }

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_device_lightrgb);
        //标签
        mLrt_tag = (LightRgbTagView) findViewById(R.id.lrt_islight);


        fl_content = (FrameLayout) findViewById(R.id.fl_content);

//        mTvTitle = (TextView) findViewById(R.id.base_tv_title);
        ActivityTitleManager.getInstance().init(this);
        ActivityTitleManager.getInstance().showCommonTitle();
        ActivityTitleManager.getInstance().changeTitle("RGB灯");


        fragmentManager = getSupportFragmentManager();
        //开启事务
        FragmentTransaction mTransaction = fragmentManager.beginTransaction();
        Intent intent=getIntent();
        mName = intent.getStringExtra("name");
        if(mName!=null){
            LightRGBDevice mLightRGBDevice = DataUtils.getInstance().getdataRGBDevice(mName);
            if(mLightRGBDevice!=null)
            ActivityTitleManager.getInstance().changeTitle(mLightRGBDevice.name);
        }
        mLightRgbFragment = new LightRgbFragment(mName,this);

        mLightWhiteFragment = new LightWhiteFragment(mName,this);

        mTransaction.replace(R.id.fl_content, mLightRgbFragment).commit();




    }



    @Override
    public void initEvent() {
        super.initEvent();


        mLrt_tag.setOnTagChangeListener(new LightRgbTagView.OnTagChangeListener() {
            @Override
            public void tagChange(View view, boolean isLeftSelect) {
                selectView(isLeftSelect);
            }
        });

    }


    private void selectView(Boolean isLeftSelect) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (isLeftSelect) {
            ft.replace(R.id.fl_content, mLightWhiteFragment);
        } else {
            ft.replace(R.id.fl_content, mLightRgbFragment);
        }
        ft.commit();
    }

    @Override
    public void initData() {
        super.initData();
        selectView(false);
    }

    @Override
    public void initListener() {
        super.initListener();

        ActivityTitleManager.getInstance().goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.finish();
            }
        });
    }
}
