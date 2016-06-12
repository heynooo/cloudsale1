package com.vanhitech.vanhitech.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vanhitech.vanhitech.R;

/**
 * 创建者     heyn
 * 创建时间   2016/3/19 11:25
 * 描述	      标题管理
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class ActivityTitleManager {

    private Intent mMIntent;
    private Context mMContext;
    private Context mMcontext;

    // 单例
    private ActivityTitleManager() {
    }

    private static ActivityTitleManager instance = new ActivityTitleManager();

    public static ActivityTitleManager getInstance() {
        return instance;
    }

    protected static final String TAG = "ActivityTitleManager";
    // 控制标题的显示
    /*********************************************************************************/

    private RelativeLayout commonContainer;
    public ImageView goback;// 返回
    public Button help;// 帮助
    private TextView titleContent;// 标题内容
    private Activity mActivity;

    public void init(Activity activity) {
        mActivity = activity;
        commonContainer = (RelativeLayout) activity.findViewById(R.id.base_title);
        goback = (ImageView) activity.findViewById(R.id.base_ib_menu);
        help = (Button) activity.findViewById(R.id.base_ib_left);
        titleContent = (TextView) activity.findViewById(R.id.base_tv_title);

        setListener();

    }

    private void setListener() {
        goback.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i(TAG, "返回键");//TODO
                mActivity.finish();
//                Intent intent = new Intent();
//
//                intent.setClass(mActivity, MainActivity.class);
//                mActivity.startActivity(intent);//设置一个标志，让那边直接finish 不用判断
//                mActivity.overridePendingTransition(R.animator.preventeranim, R.animator.prevexitanim);
//                mActivity.finish();
//                T.hideToast();
//                mActivity.onBackPressed();


            }
        });

        help.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String temp = (String) help.getText();//拿到文本对比执行//TODO
                if (temp.equals("帮助")) {
                    Log.i(TAG, "帮助");
                } else if (temp.equals("刷新")) {
                    Log.i(TAG, "刷新");
                }  else if (temp.equals("更新")) {
                    Log.i(TAG, "更新");
//                    mActivity.finish();
//                    new HomeController(mActivity).updateViewpager();
                }


            }
        });


    }

//    public void intent(Context context) {
//        mMcontext = context;
//    }


    private void initTitle() {//如果有多个标题，可以用来切换
        commonContainer.setVisibility(View.VISIBLE);

    }



    /**
     * 显示通用标题
     */
    public void showCommonTitle() {
        initTitle();
        commonContainer.setVisibility(View.VISIBLE);
    }

    /**
     * 修改标题内容
     *
     * @param title
     */
    public void changeTitle(String title) {
        titleContent.setText(title);
    }


    /**
     * 修改help按键内容
     *
     * @param text
     */
    public void changehelpText(String text) {
        help.setText(text);
    }

    /**
     * 使用
     * 		ActivityTitleManager.getInstance().init(this);
     ActivityTitleManager.getInstance().showCommonTitle();
     */


}
