package com.vanhitech.vanhitech.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * 创建者     heyn
 * 创建时间   2016/5/9 21:37
 * 描述	      ${TODO}
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class CurtainView extends View {

    private Context mContext;
    private int pressure;
    private int max;
    private int left;
    private int right;
    private int buttom;

    public CurtainView(Context context) {
        super(context);
        mContext = context;
    }

    public CurtainView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CurtainView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public void setMax(int max) {
        this.max = max;
    }
    public void setVLeft(int left) {
        this.left = left;
    }
    public void setVRight(int right) {
        this.right = right;
    }
    public void setVbuttom(int buttom){
        this.buttom=buttom;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(0xff66B6ED);
        canvas.drawRect(0, 0, right, buttom, paint);
    }

}
