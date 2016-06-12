package com.vanhitech.vanhitech.zxing;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.vanhitech.vanhitech.R;
import com.vanhitech.vanhitech.base.BaseActivityController;
import com.vanhitech.vanhitech.manager.ActivityTitleManager;
import com.vanhitech.vanhitech.utils.AllCapTransformationMethod;
import com.vanhitech.vanhitech.utils.T;

/**
 * 创建者     heyn
 * 创建时间   2016/3/18 14:42
 * 描述	      ${TODO}
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class ImporSnActivity extends BaseActivityController {
    private SnEdittext itemHomelistName;
    private Button btnGdeviceAdd;
    private String mSn,mName;
    private String mPlace;



    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_device_importsn);
        ActivityTitleManager.getInstance().init(this);
        ActivityTitleManager.getInstance().showCommonTitle();
        ActivityTitleManager.getInstance().changeTitle("输入设备SN");
        assignViews();
    }

    private void assignViews() {
        itemHomelistName = (SnEdittext) findViewById(R.id.item_homelist_name);
        itemHomelistName.setTransformationMethod(new AllCapTransformationMethod());
        btnGdeviceAdd = (Button) findViewById(R.id.btn_gdevice_add);
    }



    @Override
    public void initData() {
        super.initData();
        Intent intent = getIntent();
        mPlace = intent.getStringExtra("place");

    }

    @Override
    public void initEvent() {
        super.initEvent();
        btnGdeviceAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSn = itemHomelistName.getText().toString().trim();

                if (TextUtils.isEmpty(mSn)) {
                    itemHomelistName.setError("sn不能为空");
                }else if(mSn.length()<16){
                    T.showShort(ImporSnActivity.this,"sn输入不正确,长度不够");
                }
                else{
                    //跳转到发送界面
                    Intent intent = new Intent();
                    intent.setClass(ImporSnActivity.this, ResultSNActivity.class);
                    intent.putExtra("place", mPlace);
                    intent.putExtra("SN", mSn.toUpperCase());
                    startActivity(intent);
                    finish();
                }
            }
        });

    }




}
