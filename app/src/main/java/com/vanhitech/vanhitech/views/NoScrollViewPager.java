package com.vanhitech.vanhitech.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 创建者     heyn
 * 创建时间   2016/3/14 23:22
 * 描述	      不可以滚动的ViewPager ,修改默认事件处理行为
 *          1 ViewPager不可以滚动, 2 不影响子类事件处理
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class NoScrollViewPager extends LazyViewPager{
    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 1是否派发
     * 不处理,子类可以收到事件
     * 一般不做处理
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }
    /**
     *2是否拦截
     * true 拦截 会走到自己的onTouchEvent
     *                      true 事件被处理 结束
     *                       flase 事件传递给父类
     *
     * flase 不拦截 --传递给子类
     * 不做处理 交给父类处理
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//       return  true; //孩子没有机会接收到事件 不可行
        return  false;// 不拦截  可行
//        return super.onInterceptTouchEvent(ev);//有风险 如果有个子viewpager 3.0以上系统,孩子抢夺事件
    }
    /**
     * 3是否消费
     *  true 事件被处理 结束
     *
     *  flase 事件传递给父类
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
//        return super.onTouchEvent(ev);
        return  false;
    }
}
