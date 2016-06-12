package com.vanhitech.vanhitech.cropview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.vanhitech.vanhitech.R;
import com.vanhitech.vanhitech.base.BaseActivityController;
import com.vanhitech.vanhitech.base.MyApplication;
import com.vanhitech.vanhitech.controller.home.HomeRoomInfoActivity;
import com.vanhitech.vanhitech.manager.ActivityTitleManager;


public class CorpActivity extends BaseActivityController {

    private CropImageView mCropView;
    private RelativeLayout mRootLayout;
    private String mIntentPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_crop);
        initViews();
        FontUtils.setFont(mRootLayout);
        mCropView.setImageBitmap(((MyApplication) getApplication()).cropped);
        ActivityTitleManager.getInstance().init(this);
        ActivityTitleManager.getInstance().showCommonTitle();
        ActivityTitleManager.getInstance().changeTitle("设置图片");
        Intent intent=getIntent();
        mIntentPlace= intent.getStringExtra("place");//Todo 这里需要修改成Place

    }

    private final View.OnClickListener btnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCropView.setInitialFrameScale(0.75f);
            switch (v.getId()) {
                case R.id.buttonDone:
                    ((MyApplication) getApplication()).cropped = mCropView.getCroppedBitmap();
                    Intent intent = new Intent(CorpActivity.this, HomeRoomInfoActivity.class);//TODO
                    intent.putExtra("place",mIntentPlace+"");//TODO 这里需要修改成place
                    overridePendingTransition(R.animator.nextenteranim, R.animator.nextexitanim);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.buttonFitImage:
                    mCropView.setCropMode(CropImageView.CropMode.RATIO_FIT_IMAGE);
                    break;
                case R.id.button1_1:
                    mCropView.setCropMode(CropImageView.CropMode.RATIO_1_1);
                    break;
                case R.id.button3_4:
                    mCropView.setCropMode(CropImageView.CropMode.RATIO_3_4);
                    break;
                case R.id.button4_3:
                    mCropView.setCropMode(CropImageView.CropMode.RATIO_4_3);
                    break;
                case R.id.button9_16:
                    mCropView.setCropMode(CropImageView.CropMode.RATIO_9_16);
                    break;
                case R.id.button16_9:
                    mCropView.setCropMode(CropImageView.CropMode.RATIO_16_9);
                    break;
                case R.id.buttonCustom:
                    mCropView.setCustomRatio(7, 5);
                    break;
                case R.id.buttonFree:
                    mCropView.setCropMode(CropImageView.CropMode.RATIO_FREE);
                    break;
                case R.id.buttonCircle:
                    mCropView.setCropMode(CropImageView.CropMode.CIRCLE);
                    break;
                case R.id.buttonRotateImage:
                    mCropView.rotateImage(CropImageView.RotateDegrees.ROTATE_90D);
                    break;
            }
        }
    };

    private void initViews() {
        mCropView = (CropImageView) findViewById(R.id.cropImageView);

        //自定义充满宽度 - 比例7：5
        mCropView.setInitialFrameScale(1.0f);
        mCropView.setCustomRatio(10, 5); //TODO 图片比例
//        mCropView.
        findViewById(R.id.buttonDone).setOnClickListener(btnListener);
        findViewById(R.id.buttonFitImage).setOnClickListener(btnListener);
        findViewById(R.id.button1_1).setOnClickListener(btnListener);
        findViewById(R.id.button3_4).setOnClickListener(btnListener);
        findViewById(R.id.button4_3).setOnClickListener(btnListener);
        findViewById(R.id.button9_16).setOnClickListener(btnListener);
        findViewById(R.id.button16_9).setOnClickListener(btnListener);
        findViewById(R.id.buttonFree).setOnClickListener(btnListener);
        findViewById(R.id.buttonRotateImage).setOnClickListener(btnListener);
        findViewById(R.id.buttonCustom).setOnClickListener(btnListener);
        findViewById(R.id.buttonCircle).setOnClickListener(btnListener);
        mRootLayout = (RelativeLayout) findViewById(R.id.layout_root);
    }

}
