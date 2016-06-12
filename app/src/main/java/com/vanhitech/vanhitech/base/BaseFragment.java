package com.vanhitech.vanhitech.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 创建者     heyn
 * 创建时间   2016/3/14 19:45
 * 描述	      ${TODO}
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public abstract class BaseFragment  extends Fragment {
    public Activity mActivity;
    public Context mContext;	//基类里面的存储了一个上下文

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mContext = getActivity();
        mActivity= getActivity();
        init();//接收别人传递过来的参数
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView();//初始化Fragment持有的视图信息
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        initData();
        initListener();
        super.onActivityCreated(savedInstanceState);
    }


    /**
     * 接收别人传递过来的参数
     */
    public void init() {

    }

    /**
     * 初始化Fragment持有的视图信息
     * @return
     */
    public abstract View initView();

    /**
     * 加载数据
     */
    public void initData() {

    }

    /**
     * 设置监听
     */
    public void initListener() {

    }

}
