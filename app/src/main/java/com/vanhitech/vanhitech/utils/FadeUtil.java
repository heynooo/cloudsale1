package com.vanhitech.vanhitech.utils;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

public class FadeUtil {
	// ①第一个界面淡出，执行时间
	// ②在第一个界面执行淡出，第二个界面处于等待状态，等待的时间（第一个界面执行时间）
	// ③第二界面淡入，执行时间
	
	private static Handler handler=new Handler(){
		public void handleMessage(Message msg) {
			View view = (View) msg.obj;
			ViewGroup parent = (ViewGroup)view.getParent();//RelativeLayout
			parent.removeView(view);
		}
	};

	/**
	 * 淡出
	 * 
	 * @param view
	 *            ：第一个界面
	 * @param duration
	 *            ：时间
	 */
	public static void fadeOut(final View view, long duration) {
		AlphaAnimation animation = new AlphaAnimation(1, 0);
		animation.setDuration(duration);
		// animation.setFillAfter(true);
		view.startAnimation(animation);
		
		
		// 删除第一个界面，动画执行完成
		// 设置动画监听
		animation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				//依据view获取到父容器
//				ViewGroup parent = (ViewGroup)view.getParent();//RelativeLayout
//				parent.removeView(view);
				Message msg=Message.obtain();
				msg.obj=view;
				handler.sendMessage(msg);
				// 模拟器2.3
				// 模拟器4.0无问题
			}
		});
	}

	/**
	 * 淡如
	 * 
	 * @param view
	 *            ：第二个界面
	 * @param duration
	 *            ：时间
	 * @param delay
	 *            :等待时间
	 */
	public static void fadeIn(View view, long duration, long delay) {
		AlphaAnimation animation = new AlphaAnimation(0, 1);
		animation.setStartOffset(delay);
		animation.setDuration(duration);

		view.startAnimation(animation);
	}
}
