package com.vanhitech.vanhitech;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.vanhitech.protocol.cmd.ServerCommand;
import com.vanhitech.vanhitech.base.CmdBaseActivity;
import com.vanhitech.vanhitech.base.MyApplication;
import com.vanhitech.vanhitech.conf.Constants;
import com.vanhitech.vanhitech.fragment.ContentFragment;
import com.vanhitech.vanhitech.utils.LogUtils;

/**
 * 不能一起公用一个cmd 暂时 ，使用多次登录
 */
public class MainActivity extends CmdBaseActivity {

    public static final String FRAGMENT_MAIN_CONTENT = "fragment_main_content";
    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        systemBarTint();//覆盖时间电量效果
        //设置它的内容区域(above)
        setContentView(R.layout.fl_main_content);
        initFragment();
//        initClientCMDhelper();

    }

    @Override
    public void onReceiveCommand(ServerCommand cmd) {
        super.onReceiveCommand(cmd);
        LogUtils.i("-----Main----------" + cmd.toString());

    }

    private void initFragment() {
        mFragmentManager = getSupportFragmentManager();
        mTransaction = mFragmentManager.beginTransaction();

        mTransaction.add(R.id.fragment_main_content, new ContentFragment(), FRAGMENT_MAIN_CONTENT);

        mTransaction.commit();
    }
    /**
    Android按返回键退出程序但不销毁，程序后台运行，同QQ退出处理方式
     */
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            moveTaskToBack(false);
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
    @Override
    public void finish() {    // 重写finish(), 在退出的时候弹出对话框
        LogUtils.i("退出");
        if (MyApplication.getIsPrompt()) {
            MainActivity.super.finish();
        } else if (Constants.isPromptfinish) {


        } else {
            //// TODO: 2016/5/18
            /*


             */


            /*

            new Builder(this)
                    .setTitle("确定要退出吗?")
                    .setPositiveButton("确定", new OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            LoginConstants.fresh=false;
                            CmdBaseActivity.getInstance().closeSocket();
                            MainActivity.super.finish();    // 如果点击确定按钮, 执行父类的finish(), 真正退出

                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
            */



            final NormalDialog dialog = new NormalDialog(this);

            dialog.content("确定要退出吗?")//
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
                            CmdBaseActivity.getInstance().closeSocket();
                            MainActivity.super.finish();    // 如果点击确定按钮, 执行父类的finish(), 真正退出
                            dialog.dismiss();
                        }
                    });




        }

    }

}
