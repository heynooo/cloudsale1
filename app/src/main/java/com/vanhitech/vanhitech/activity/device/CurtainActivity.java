package com.vanhitech.vanhitech.activity.device;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.vanhitech.vanhitech.R;
import com.vanhitech.vanhitech.base.BaseActivityController;
import com.vanhitech.vanhitech.manager.ActivityTitleManager;
import com.vanhitech.vanhitech.utils.AnimationController;
import com.vanhitech.vanhitech.views.LightRgbTagView;

/**
 * 创建者     heyn
 * 创建时间   2016/3/18 14:42
 * 描述	      ${TODO}
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class CurtainActivity extends BaseActivityController {
    public LightRgbTagView mLrt_tag;
    private Context mContext;
    private Button mBt;
    private String mName;
    private String mId;

    private int normal = 0; //1 关窗帘   ,  2开窗帘 , 3暂停  , 0表示在执行 如果是0 可以执行否则无效
    private  boolean flag=true;
    /*
    如果在执行某个事件,再次点击无效果
     */

    public Runnable runnable = new Runnable() {
        @Override
        public void run() {


        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }


    @Override
    protected void onDestroy() {

        super.onDestroy();

    }


    @Override
    public void initView() {
        super.initView();
        mContext = this;
        setContentView(R.layout.activity_device_curtain);
        ActivityTitleManager.getInstance().init(this);
        ActivityTitleManager.getInstance().showCommonTitle();
        ActivityTitleManager.getInstance().changeTitle("窗帘");
        assignViews();
    }

    private CheckBox mTest1;
    private CheckBox mTest2;
    private CheckBox mTest3;
    private Button mTest4;
    private ImageView mIm1;
    private ImageView mIm2;
    private ImageView mIm3;
    private ImageView mIm4;
    private ImageView mIm5;
    private ImageView mIm6;
    private ImageView mRm1;
    private ImageView mRm2;
    private ImageView mRm3;
    private ImageView mRm4;
    private ImageView mRm5;
    private ImageView mRm6;

    private void assignViews() {
        mTest1 = (CheckBox) findViewById(R.id.cb_cuttain_close);
        mTest2 = (CheckBox) findViewById(R.id.cb_cuttain_open);
        mTest3 = (CheckBox) findViewById(R.id.cb_cuttain_pause);
//        mTest4 = (Button) findViewById(R.id.test4);
        mIm1 = (ImageView) findViewById(R.id.im_1);
        mIm2 = (ImageView) findViewById(R.id.im_2);
        mIm3 = (ImageView) findViewById(R.id.im_3);
        mIm4 = (ImageView) findViewById(R.id.im_4);
        mIm5 = (ImageView) findViewById(R.id.im_5);
        mIm6 = (ImageView) findViewById(R.id.im_6);
        mRm1 = (ImageView) findViewById(R.id.rm_1);
        mRm2 = (ImageView) findViewById(R.id.rm_2);
        mRm3 = (ImageView) findViewById(R.id.rm_3);
        mRm4 = (ImageView) findViewById(R.id.rm_4);
        mRm5 = (ImageView) findViewById(R.id.rm_5);
        mRm6 = (ImageView) findViewById(R.id.rm_6);
    }
    //transparent


    @Override
    public void initData() {
        super.initData();
        Intent intent = getIntent();
        mId = intent.getStringExtra("id");
        mName = intent.getStringExtra("name");

    }

    @Override
    public void initEvent() {
        super.initEvent();
        mTest1.setOnClickListener(new View.OnClickListener() {//左边进来
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                /*
                做个状态判断,如果是合的状态,再次点击没有效果,参照手机影音
                 */
//                mTest3.setChecked(true);
                if(flag)
                if (normal == 0) {
                    normal = 1;
                    mTest2.setChecked(false);
                    mTest3.setChecked(false);
                    AnimationController.slideFadeInlete(mIm1, 12000, 10);
                    AnimationController.slideFadeIn(mRm1, 12000, 10);

                    AnimationController.slideFadeInlete(mIm2, 12000, 10);
                    AnimationController.slideFadeIn(mRm6, 12000, 10);
                    normal = 0;
                }

//
//                ObjectAnimator animator = ObjectAnimator.ofFloat(im_scan, "rotation", 0f, 360.0f);
//
//                animator.setDuration(2000);
//
//                animator.setInterpolator(new LinearInterpolator());//不停顿
//
//                animator.setRepeatCount(-1);//设置动画重复次数
//
//                animator.setRepeatMode(ValueAnimator.RESTART);//动画重复模式
//
//                animator.start();//开始动画
//
//                animator.pause();//暂停动画
//
//                animator.resume();//恢复动画
                //其他的就做显示出来 (延迟显示)
//                AnimationController.fadeIn(mIm2, 1000,2000);
//                AnimationController.fadeIn(mRm6, 1000,2000);
//                AnimationController.fadeIn(mIm3, 1000,4000);
//                AnimationController.fadeIn(mRm5, 1000,4000);
//                AnimationController.fadeIn(mIm4, 1000,6000);
//                AnimationController.fadeIn(mRm4, 1000,6000);
//                AnimationController.fadeIn(mIm5, 1000,8000);
//                AnimationController.fadeIn(mRm3, 1000,8000);
//                AnimationController.fadeIn(mIm6, 1000,10000);
//                AnimationController.fadeIn(mRm2, 1000,10000);


//                mIm1.set


                //右边

                //其他的就做显示出来 (延迟显示)

            }
        });

        mTest2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (normal == 0) {
                    normal = 2;
                    mTest1.setChecked(false);
                    mTest3.setChecked(false);
                    normal = 0;
                }
            }
        });

        mTest3.setOnClickListener(new View.OnClickListener() {//左边出去
            @Override
            public void onClick(View v) {
                if (normal == 0) {
                    normal = 3;
                    mTest2.setChecked(false);
                    mTest1.setChecked(false);
                    AnimationController.slideOut(mRm1, 1000, 200);
                    AnimationController.slideOut(mRm2, 1000, 200);
                    AnimationController.slideOut(mRm3, 1000, 200);
                    AnimationController.slideOut(mRm4, 1000, 200);
                    AnimationController.slideOut(mRm5, 1000, 200);
                    AnimationController.slideOut(mRm6, 1000, 200);


                    AnimationController.transparent(mRm1);
                    AnimationController.transparent(mRm2);
                    AnimationController.transparent(mRm3);
                    AnimationController.transparent(mRm4);
                    AnimationController.transparent(mRm5);
                    AnimationController.transparent(mRm6);
                    normal = 0;
                }
            }
        });


    }


//
//    public void onClick(View v, int id) {
//
//        }
////        sendHideCtrlLayoutMessage();
//    };


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
