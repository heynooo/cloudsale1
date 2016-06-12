package com.vanhitech.vanhitech.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.vanhitech.vanhitech.R;

/**
 * Created by Administrator on 2016/5/12.
 * 获取背景图片色值
 */
public class GetRGBView extends RelativeLayout {
    private ImageView smallCircle;  //移动小球
    private Bitmap backIM;          // 取色图片
    private int colorValue;         //获取的色值
    private Context context;
    private GetRGBListener rgbListener;
   private float smallCircleWidth,smallCircleHeight;  //小球的高度和宽度
    public GetRGBView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initSmallCircle(context, attrs);
    }

    private void initSmallCircle(Context context, AttributeSet attrs) {
        backIM= BitmapFactory.decodeResource(getResources(),
                R.drawable.rgb_pix);
        initView();
    }
    private void initView() {
        smallCircle = new ImageView(context);
        smallCircle.setImageBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.rgb_circle));

        addView(smallCircle,55,55); // 添加移动小球到视图中
        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                getViewTreeObserver().removeOnPreDrawListener(this);
                smallCircleWidth = smallCircle.getWidth();
                smallCircleHeight = smallCircle.getHeight();
//                for (int i = (int)smallCircleWidth;i< (getWidth()-smallCircleWidth/2);i++) {
//                    for (int j = (int)smallCircleHeight;j< (getHeight()-smallCircleHeight/2);j++) {
//                        updateValue(i,j);
//                        int rgbs[] = getRGB();
//                        if (rgbs[0] == r&&rgbs[1]==g&&rgbs[2]==b) {
//                            updateSmallCirclePosition(i,j);
//                           return  true;
//                        }
//                    }
//                }
                return true;
            }
        });
    }

    public void setRgbListener(GetRGBListener getRGBListener) {
        this.rgbListener = getRGBListener;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = 0;
        float y = 0;
        x = event.getX();
        y = event.getY();
        if (x < smallCircleWidth/2) {
          x= smallCircleWidth/2;
        }
        if (y < smallCircleHeight/2) {
            y = smallCircleHeight/2;
        }
        if (x >  (getWidth()-smallCircleWidth/2)) {
            x=  (getWidth()-smallCircleWidth/2);
        }
        if (y > (getHeight()-smallCircleHeight/2)) {
            y = (getHeight()-smallCircleHeight/2);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // 按下
                update(x,y);
                if (rgbListener != null) rgbListener.getRgbTouchDown();
                break;
            case MotionEvent.ACTION_MOVE: // 移动
                if (rgbListener != null) rgbListener.getRgbTouchMove();
                update(x,y);
                break;
            case MotionEvent.ACTION_UP: // 抬起
                if (rgbListener != null) rgbListener.getRgbTouchUp();
                update(x,y);
                break;
        }
        return true;
    }
    private void update(float x, float y) {
        updateSmallCirclePosition( x, y);
        updateValue(x, y);
    }
    int r,g,b;
    /**
     * 色值色值
     */
    public void setColorValue(int r,int g,int b) {
        this.r = r;
        this.g = g;
        this.b =b;

    }
    /**
     * 更新小圆的位置
     * @param x
     * @param y
     */
    private void updateSmallCirclePosition(float x,float y) {
        smallCircle.layout((int) (x - smallCircleWidth / 2), (int) (y - smallCircleHeight / 2), (int) (x + smallCircleWidth / 2), (int) (y + smallCircleHeight / 2));
    }
    /**
     * 更新当前色带的值
     *
     * @param x
     * @param y
     */
    private void updateValue(float x, float y) {
        colorValue = backIM.getPixel((int)x, (int)y);
    }

    /**
     * 获取rgb色值
     * @return
     */
    public int[] getRGB() {
        int rgb[] = new int[3];
        rgb[0] = (colorValue & 0x00FF0000) >> 16;
        rgb[1] = (colorValue & 0x0000FF00) >> 8;
        rgb[2] = colorValue & 0x000000FF;

        return rgb;
    }

    public interface  GetRGBListener {
        public  void getRgbTouchDown();
        public void getRgbTouchMove();
        public void getRgbTouchUp();
    }

}
