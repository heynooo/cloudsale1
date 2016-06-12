package com.vanhitech.vanhitech.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.flyco.dialog.widget.popup.BubblePopup;
import com.lidroid.xutils.BitmapUtils;
import com.vanhitech.vanhitech.R;
import com.vanhitech.vanhitech.base.CmdBaseActivity;
import com.vanhitech.vanhitech.conf.LoginConstants;
import com.vanhitech.vanhitech.utils.FileUtils;
import com.vanhitech.vanhitech.utils.LogUtils;

import java.io.File;
import java.io.FileOutputStream;

/**
 * 创建者     heyn
 * 创建时间   2016/3/15 13:35
 * 描述	      ${TODO}
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public abstract class BaseActivity extends CmdBaseActivity  {
    private GestureDetector mGd;
    private BroadcastReceiver mReceiver;
    public BitmapUtils mBitmapUtils;
    public BubblePopup mBubblePopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        initView();
        initData();
        initEvent();

        initGesture();


        View  v=findViewById(R.id.layout_logintest);
        mBitmapUtils = new BitmapUtils(this);
//        bitmapUtils.display(v, "res/mipmap/login_bg.png");
//        bitmapUtils.display(v, "http://7xsovd.com2.z0.glb.clouddn.com/login_bg.png");
//        bitmapUtils.display(v, "assets/img/roottemp.bmp");
        mBitmapUtils.display(v, "assets/img/login_bg.bmp");

    }

    /**
     * 跳转
     */
    public void startActivity(Class type) {
        Intent intent = new Intent(this, type);
        startActivity(intent);
        finish();
    }




    /**
     * 滑动事件,可做可不做
     */
    private void initGesture() {

        mGd = new GestureDetector(new MyOnGestureListener(){
            /**
             * e1 按下的点
             * e2 滑动后结束的点
             * velocityX  x轴方向的速度 单位：像素/秒
             * velocityY  y轴方向的速度
             */
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                   float velocityY) {

                //判断滑动的方向和速度 velocityX
                if (Math.abs(velocityX) > 100) {
                    //速度大于100像素每秒 滑动有效
                    if (e2.getX() - e1.getX() > 50) {
                        LogUtils.i("从左往右滑动");
                        bt_login(null);

                    } else if (e2.getX() - e1.getX() < -50){

                        LogUtils.i("从右往左滑动");
                        bt_login_regsiter(null);

                    }
                }

                //true 自己消费事件
                return true;
            }
        });

    }

    /**
     * 需要覆盖此方法完成事件的处理
     */
    public void initEvent() {



//        mReceiver = new HomeReceiver();
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
//        registerReceiver(mReceiver, filter);


    }
//-------------home test---------------------------TODO
    /**
     * home 按键处理
     */
    private class HomeReceiver  extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                //home键处理
                finish();//关闭自己
            }


        }

    }


    @Override
    public void onBackPressed() {
        // 返回键的处理
        // 回到主界面
		/*
		 * <intent-filter> <action android:name="android.intent.action.MAIN" />
		 * <category android:name="android.intent.category.HOME" /> <category
		 * android:name="android.intent.category.DEFAULT" /> <category
		 * android:name="android.intent.category.MONKEY"/> </intent-filter>
		 */

        Intent main = new Intent("android.intent.action.MAIN");
        main.addCategory("android.intent.category.HOME");
        main.addCategory("android.intent.category.DEFAULT");
        main.addCategory("android.intent.category.MONKEY");

        startActivity(main);
        finish();// 退出任务栈
    }
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
//        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

//--------------------------------

    /**
     * 需要覆盖此方法完成数据的处理
     */
    public void initData() {
    }

    /**
     * 需要覆盖此方法完成界面的显示
     */
    public void initView() {

    }
    /**
    * 下一个页面
    */
    public  void next(){
        startActivity(RegsiterActivity.class);
    }

    /**
     * 上一个页面
     */
    public  void prev(){
        startActivity(LoginActivity.class);

    }

//TODO  还需要修改成下一页是有不同情况  上一页只有返回登录界面
    /**
     * 跳转到注册界面 next 里面是注册
     *
     * @param v
     */
    public void bt_login_regsiter(View v) {
        // activity的跳转
        next();
        // 动画的切换
        overridePendingTransition(R.animator.nextenteranim, R.animator.nextexitanim);
    }
    /**
     * 上一个按钮对应的事件
     *
     * @param v
     */
    public void bt_login(View v) {
        prev();
        // 动画的切换:有Activity之间的切换
        overridePendingTransition(R.animator.preventeranim, R.animator.prevexitanim);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGd.onTouchEvent(event);
        return true;
    }

    private class MyOnGestureListener implements GestureDetector.OnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            // TODO Auto-generated method stub
            return false;
        }



        @Override
        public void onLongPress(MotionEvent e) {
            // TODO Auto-generated method stub

        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            // TODO Auto-generated method stub

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            // TODO Auto-generated method stub
            return false;
        }



        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            // TODO Auto-generated method stub
            return false;
        }

    }

    public void loginDialog(Context context,View bt_login,String text) {
        View infView = View.inflate(context, R.layout.test_popup_bubble_image,null);
        TextView textView= (TextView) infView.findViewById(R.id.tv_bubble);
        TextView textView1= (TextView) infView.findViewById(R.id.tv_bubble1);
        textView1.setText(text);
        mBubblePopup = new BubblePopup(context, infView);
        mBubblePopup.anchorView(bt_login)
//                .showAnim(new BounceRightEnter())
//                .dismissAnim(new SlideLeftExit())
//                .autoDismiss(true)
                .show();
    }

    /**
     * 保存帐号
     */

    public void save(String account, String password) {
        LoginConstants.USERNAME=account;
        File file = new File(FileUtils.getCachePath(), "infoa.txt");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(file);
            fos.write((account + "##" + password).getBytes());
            fos.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }
}
