package com.vanhitech.vanhitech.views.view_music;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

public class MusicItem implements IAnimation {
	/**
	 * 抗锯齿
	 */
	private PaintFlagsDrawFilter pfd;
	private Paint paint = new Paint();
//	private Paint paint2 = new Paint();
	private Random random = new Random();
	private int itemWidth;
	// 矩形
	private int maxHeight;
	private int height;//表示距离顶部距离
	private int x;
	// 多矩形
	private int maxRectNum;
	private int rectNum;
	private int distance = 3;

	//镜像
	private int centerY;
	private int musicColor;
	private boolean randColor;
	
	private final int RECT = 1; // 矩形
	private final int MRECT = 2; // 多矩形
	private final int MMRECT = 3; // 镜像
	private int state = MMRECT;

	public MusicItem(int height) {
		this.maxHeight = height;
		init();
	}

	public MusicItem(int x, int itemWidth, int height) {
		this.maxHeight = height;
		this.x = x;
		this.itemWidth = itemWidth;

		init();
	}

	public MusicItem(int x, int itemWidth, int height, int musicColor) {
		this.maxHeight = height;
		this.x = x;
		this.itemWidth = itemWidth;
		this.musicColor = musicColor;
		init();
	}
	
	public MusicItem(int x, int itemWidth, int height, int musicColor, boolean randColor) {
		this.maxHeight = height;
		this.x = x;
		this.itemWidth = itemWidth;
		this.musicColor = musicColor;
		this.randColor = randColor;
		init();
	}
	
	public MusicItem(int x, int itemWidth, int height, int musicColor, boolean randColor, int musicType) {
		this.maxHeight = height;
		this.x = x;
		this.itemWidth = itemWidth;
		this.musicColor = musicColor;
		this.randColor = randColor;
		state = musicType;
		init();
	}
	
	private void init(){
		pfd = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
				| Paint.FILTER_BITMAP_FLAG);
		if(randColor){
			int r = random.nextInt(256);
			int g = random.nextInt(256);
			int b = random.nextInt(256);
			paint.setARGB(255, r, g, b);
			
		}else{
			paint.setColor(musicColor);
		}
		switch(state){
		case MRECT:
			maxRectNum = maxHeight / (itemWidth + distance);
			break;
		case MMRECT:
			centerY = maxHeight / 2;
			maxRectNum = centerY / (itemWidth + distance);
			break;
		}
		
	}
	
	@Override
	public void draw(Canvas canvas) {
		switch (state) {
		case RECT:
			Paint paint2 = new Paint();
//			Bitmap bitmap = ((BitmapDrawable)MainActivity.context.getResources().getDrawable(R.drawable.line)).getBitmap();	
//			canvas.drawBitmap(bitmap, x, 0, paint2);
//			Canvas canvas2 = new Canvas();
			int h = canvas.getHeight();
			int w = canvas.getWidth();
			paint2.setARGB(255, 241, 241, 241);		
			canvas.setDrawFilter(pfd);
			//画背景  灰色
			canvas.drawRect((float)(x*1.0), (float)(itemWidth*1.0/2), (float)(x*1.0 + itemWidth), (float)(maxHeight-itemWidth*1.0/2), paint2);
			canvas.drawCircle((float)(x*1.0 +itemWidth*1.0/2), (float)(itemWidth*1.0/2), (float)(itemWidth*1.0/2), paint2);//顶部添加一个球
			canvas.drawCircle((float)(x*1.0 +itemWidth*1.0/2),(float)(h-itemWidth*1.0/2), (float)(itemWidth*1.0/2), paint2);//底部添加一个球

			
//			Log.e("swg","itemWidth"+itemWidth);
			//画前景
			canvas.drawRect((float)(x*1.0), (float)(height*1.0), (float)(x*1.0 + itemWidth), (float)(maxHeight-itemWidth*1.0/2), paint);
			canvas.drawCircle((float)(x*1.0 +itemWidth*1.0/2), (float)(height*1.0), (float)(itemWidth*1.0/2), paint);//顶部添加一个球
			canvas.drawCircle((float)(x*1.0 +itemWidth*1.0/2), (float)(h-itemWidth*1.0/2), (float)(itemWidth*1.0/2), paint);//底部添加一个球
			break;
//		case MRECT:
//			int num = maxRectNum - rectNum;
//			for (int i = 0; i < rectNum; i++) {
//				canvas.drawRect(x, (num + i) * (itemWidth + distance), x
//						+ itemWidth, (num + i + 1) * (itemWidth + distance)
//						- distance, paint);
//			}
//			break;
//		case MMRECT:
//			paint.setAlpha(255);
//			int num1 = maxRectNum - rectNum;
//			for (int i = 0; i < rectNum; i++) {
//				canvas.drawRect(x, (num1 + i) * (itemWidth + distance), x
//						+ itemWidth, (num1 + i + 1) * (itemWidth + distance)
//						- distance, paint);
//			}
//			paint.setAlpha(60);
//			for (int i = 0; i < rectNum; i++) {
//				canvas.drawRect(x, (maxRectNum + i) * (itemWidth + distance), x
//						+ itemWidth, (maxRectNum + i + 1) * (itemWidth + distance)
//						- distance, paint);
//			}
//			break;
		}

	}

	@Override
	public void move() {

		if(randColor){
//			int r = random.nextInt(256);
//			int g = random.nextInt(256);
//			int b = random.nextInt(256);
			int r = 200;
			int g = 200;
			int b = 200;
			paint.setARGB(255, r, g, b);
//			paint2.setARGB(255, 150, 150, 150);
		}else{
			paint.setColor(musicColor);
		}
		
		switch (state) {
		case RECT:
			height = random.nextInt(maxHeight-itemWidth)+itemWidth/2;
//			height = maxHeight-itemWidth/2;
			break;
//		case MRECT:
//		case MMRECT:
//			rectNum = 1 + random.nextInt(maxRectNum);
//			break;
		}

	}

}
