package com.vanhitech.vanhitech.views.LinkedViewPager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class NotouchLayout extends RelativeLayout{ 
  
    public NotouchLayout(Context context) {  
        super(context);  
    }  
  
    public NotouchLayout(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }  
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
//        getParent().requestDisallowInterceptTouchEvent(true);//TODO
    	return false;
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//    	return true;
    	return false;//TODO修改flase 关键
    }
   
}  