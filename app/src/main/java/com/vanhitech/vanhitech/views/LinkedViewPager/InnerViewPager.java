package com.vanhitech.vanhitech.views.LinkedViewPager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @创建者	 伍碧林
 * @创时间 	 2015-12-21 上午11:16:31
 * @描述	     优化事件的拦截和处理
 *
 * @版本       $Rev: 40 $
 * @更新者     $Author: admin $
 * @更新时间    $Date: 2015-12-21 11:35:36 +0800 (星期一, 21 十二月 2015) $
 * @更新描述    TODO
 */

/**
  if(postion==0){
	 if(往右拖动){
		父容器处理
		getParent().requestDisallowInterceptTouchEvent(false);
	 }else{
		自己处理-->希望父容器不拦截(默认情况父容器优先拦截和处理事件)
		getParent().requestDisallowInterceptTouchEvent(true);-->自己处理
	 }
	}else if(postion==最后一个点){
		if(往右拖动){
			自己处理-->希望父容器不拦截(默认情况父容器优先拦截和处理事件)
			getParent().requestDisallowInterceptTouchEvent(true);-->自己处理
		}else{
			父容器处理
			getParent().requestDisallowInterceptTouchEvent(false);
		}
	}else{
		自己处理-->希望父容器不拦截(默认情况父容器优先拦截和处理事件)
		getParent().requestDisallowInterceptTouchEvent(true);-->自己处理
	 }
 */
public class InnerViewPager extends ViewPager {
	public static final String	TAG	= InnerViewPager.class.getSimpleName();
	private float				mDownX;
	private float				mDownY;

	public InnerViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public InnerViewPager(Context context) {
		super(context);
	}

	/**
	 * 1.是否派发
	 */
	/**
	 requestDisallowInterceptTouchEvent(false)-->默认情况,父容器优先处理
	 getParent(父容器).request(请求)Disallow(不)Intercept(拦截)TouchEvent(touch事件)(true(同意))-->请求父容器不拦截-->自己优先处理
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mDownX = ev.getRawX();
			mDownY = ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			float moveX = ev.getRawX();
			float moveY = ev.getRawY();

			int diffX = (int) (moveX - mDownX + .5f);
			int diffY = (int) (moveY - mDownY + .5f);

			int position = this.getCurrentItem();
//			if (position == 0) {//第一页
//				if (diffX > 0) {//往右拖动
//					Log.i(TAG, "第一页->往右拖动-->父容器处理");
//					getParent().requestDisallowInterceptTouchEvent(false);
//				} else {
//					Log.i(TAG, "第一页->往左拖动-->自己处理");
//					getParent().requestDisallowInterceptTouchEvent(true);//改成flase
//				}
//
//			} else if (position == this.getAdapter().getCount() - 1) {//最后一页
//				if (diffX > 0) {//往右拖动
//					Log.i(TAG, "最后一页->往右拖动-->自己处理");
//					getParent().requestDisallowInterceptTouchEvent(true);
//				} else {
//					Log.i(TAG, "最后一页->往左拖动-->父容器处理");
//					getParent().requestDisallowInterceptTouchEvent(false);
//				}
//			} else {//中间页
//				Log.i(TAG, "自己处理");
				getParent().requestDisallowInterceptTouchEvent(true);
//			}

			break;
		case MotionEvent.ACTION_UP:

			break;

		default:
			break;
		}

		return super.dispatchTouchEvent(ev);
	}

	/**
	 * 2.是否拦截
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO
		return super.onInterceptTouchEvent(ev);
	}

	/**
	 * 3.是否消费
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO
		return super.onTouchEvent(ev);
	}

}
