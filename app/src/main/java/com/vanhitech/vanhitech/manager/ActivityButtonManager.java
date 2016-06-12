package com.vanhitech.vanhitech.manager;

import android.app.Activity;

/**
 * 创建者     heyn
 * 创建时间   2016/3/19 11:16
 * 描述	     暂时不使用
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class ActivityButtonManager {

    protected static final String TAG = "BottomManager";
    /******************* 管理对象的创建(单例模式) ***************************************************/
    // 创建一个静态实例
    private static ActivityButtonManager instrance;

    // 构造私有
    private ActivityButtonManager() {
    }

    // 提供统一的对外获取实例的入口
    public static ActivityButtonManager getInstrance() {
        if (instrance == null) {
            instrance = new ActivityButtonManager();
        }
        return instrance;
    }

    /**********************************OK***********************************************************/



    /******************* 初始化各个控件及相关控件设置监听 *********************************/



    public void init(Activity activity) {
//        bottomMenuContainer = (RelativeLayout) activity.findViewById(R.id.ii_bottom);

        // 设置监听
        setListener();
    }

    /**
     * 设置监听
     */
    private void setListener() {
//        // 清空按钮
//        cleanButton.setOnClickListener(new OnClickListener() {
//
//            public void onClick(View v) {
//                Log.i(TAG, "点击清空按钮");
//
//            }
//        });
//        // 选好按钮
//        addButton.setOnClickListener(new OnClickListener() {
//
//            public void onClick(View v) {
//                Log.i(TAG, "点击选好按钮");
//
//            }
//        });
    }

    /*********************************************************************************************/


}
