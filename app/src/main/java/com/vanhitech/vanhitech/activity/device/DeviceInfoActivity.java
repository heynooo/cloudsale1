package com.vanhitech.vanhitech.activity.device;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.vanhitech.vanhitech.R;
import com.vanhitech.vanhitech.base.BaseActivityController;
import com.vanhitech.vanhitech.conf.Constants;
import com.vanhitech.vanhitech.manager.ActivityTitleManager;

/**
 * 创建者     heyn
 * 创建时间   2016/3/18 14:42
 * 描述	      ${TODO}
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class DeviceInfoActivity extends BaseActivityController {
    private RelativeLayout mDeviceinfoList;
    private ImageView mDeviceinfoIcon;
    private TextView mDeviceinfoName;
    private TextView mDeviceinfoXxx;
    private String mId;
    private Context mContext;
    private BitmapUtils mBitmapUtils;
    private String mName;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_device_deviceinfo);
        mContext=this;
        ActivityTitleManager.getInstance().init(this);
        ActivityTitleManager.getInstance().showCommonTitle();
        ActivityTitleManager.getInstance().changeTitle("设备信息");
        assignViews();
    }

    private void assignViews() {
        mDeviceinfoList = (RelativeLayout) findViewById(R.id.deviceinfo_list);
        mDeviceinfoIcon = (ImageView) findViewById(R.id.deviceinfo_icon);
        mDeviceinfoName = (TextView) findViewById(R.id.deviceinfo_name);
        mDeviceinfoXxx = (TextView) findViewById(R.id.deviceinfo_xxx);
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

    @Override
    public void initData() {
        super.initData();
        //设备信息显示 拿id 去查找数据库， 显示数据
        //参考homeitem点击
        //
        Intent intent = getIntent();
        String Mac = intent.getStringExtra("Mac");   //
        //
        mName = intent.getStringExtra("name");
        //
        mId = intent.getStringExtra("id");

    }

    @Override
    public void initEvent() {
        super.initEvent();
        mBitmapUtils = new BitmapUtils(mContext);
        idToImg(mId, mDeviceinfoIcon);
        mDeviceinfoXxx.setText(mId);
        mDeviceinfoName.setText(mName);
    }

    private void idToImg(String id, View view) {

        String ID = id.substring(0, 2);
        if (ID.equals(Constants.id1)) {
            mBitmapUtils.display(view, "assets/img/home01.bmp");
        } else if (ID.equals(Constants.id2)) {

        } else if (ID.equals(Constants.id3)) {//2.4G灯开关控制  !
            mBitmapUtils.display(view, "assets/img/home03.bmp");
        } else if (ID.equals(Constants.id4)) {

        } else if (ID.equals(Constants.id5)) {
            mBitmapUtils.display(view, "assets/img/home0a.bmp");
        } else if (ID.equals(Constants.id6)) {
            mBitmapUtils.display(view, "assets/img/home06.bmp");
        } else if (ID.equals(Constants.id7)) {
            mBitmapUtils.display(view, "assets/img/home07.bmp");
        } else if (ID.equals(Constants.id8)) {

        } else if (ID.equals(Constants.id9)) {
            mBitmapUtils.display(view, "assets/img/home00.bmp");
        } else if (ID.equals(Constants.idA)) {
            mBitmapUtils.display(view, "assets/img/home0a.bmp");
        } else if (ID.equals(Constants.idB)) { //2.4G RGB灯控制  !
            mBitmapUtils.display(view, "assets/img/home0b.bmp");
        } else if (ID.equals(Constants.idC)) { //2.4G RGB灯控制  !
            mBitmapUtils.display(view, "assets/img/home06.bmp");
        } else if (ID.equals(Constants.idD)) {//2.4G冷暖灯开关及调光控制 !
            mBitmapUtils.display(view, "assets/img/home0b.bmp");
        } else if (ID.equals(Constants.idE)) {//2.4G RGB灯控制

        } else if (ID.equals(Constants.idF)) {//2.4G RGB灯控制  !
            mBitmapUtils.display(view, "assets/img/home06.bmp");
        } else if (ID.equals(Constants.id10)) {

        } else if (ID.equals(Constants.id11)) {

        } else if (ID.equals(Constants.id12)) {

        } else if (ID.equals(Constants.id13)) {
            mBitmapUtils.display(view, "assets/img/home13.bmp");
        } else if (ID.equals(Constants.id14)) {

        } else {
            //不处理
            mBitmapUtils.display(view, "assets/img/home00.bmp");
        }

    }

}
