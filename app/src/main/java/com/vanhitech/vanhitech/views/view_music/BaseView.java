package com.vanhitech.vanhitech.views.view_music;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

public abstract class BaseView extends View{
	private MyThread thread;
	protected long sleepTime = 500;
	
	public BaseView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BaseView(Context context) {
		super(context);
	}

	protected abstract void init();
	protected abstract void drawSub(Canvas canvas);
	protected abstract void logic();
	@Override
	protected final void onDraw(Canvas canvas) {
		
		if(thread == null){
			thread = new MyThread();
			thread.start();
		}else{
			drawSub(canvas);
		}
		
	}
	
	@Override
	protected void onDetachedFromWindow() {
		running = false;
		super.onDetachedFromWindow();
	}
	
	private boolean running = true;
	class MyThread extends Thread{
		
		@Override
		public void run() {
			init();
			long workTime;
			while(running){
				workTime = System.currentTimeMillis();
				postInvalidate();
				logic();
				workTime = System.currentTimeMillis() - workTime;
				try {
					if(workTime < sleepTime){
						Thread.sleep(sleepTime);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
