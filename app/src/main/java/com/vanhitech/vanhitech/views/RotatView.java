package com.vanhitech.vanhitech.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.vanhitech.vanhitech.R;


/**
 * 参考丸子的RotatView自己升级的 旋转带动标记颜色的变化显示
 * 
 * @author jxm
 * @version 0.0
 */
public class RotatView extends View {

	/**
	 * 原心坐标x
	 */
	float o_x;

	/**
	 * 原心坐标y
	 */
	float o_y;

	/**
	 * 图片的宽度
	 */
	int width;

	/**
	 * 图片的高度
	 */
	int height;

	/**
	 * view的真实宽度与高度:因为是旋转，所以这个view是正方形，它的值是图片的对角线长度
	 */
	double maxwidth;

	/**
	 * 旋转的图片
	 */
	Bitmap rotatBitmap;
	Bitmap back1Bitmap;
	Bitmap back2Bitmap;
	int circleRingSize = 0;

	float cR;
	boolean isUp = false;
	public boolean isOpen; // 抽屉打开的时候不能动
	public boolean isON; // 抽屉打开的时候不能动
	/**
	 * 抗锯齿
	 */
	private PaintFlagsDrawFilter pfd;

	OnDegreeChangeListener degreeChangeListener;

	public void setDegreeChangeListener(
			OnDegreeChangeListener degreeChangeListener) {
		this.degreeChangeListener = degreeChangeListener;
	}

	public RotatView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	/**
	 * 初始化handler与速度计算器
	 */
	private void init() {
		setRotatDrawableResource(R.mipmap.img_air_tp_mark);
		initOutMark();
		initInnerMark();
		pfd = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
				| Paint.FILTER_BITMAP_FLAG);
	}

	public void setRotatBitmap(Bitmap bitmap) {
		rotatBitmap = bitmap;
		back1Bitmap = ((BitmapDrawable) getResources().getDrawable(
				R.mipmap.img_air_num_point_bg)).getBitmap();
		initSize();
		postInvalidate();
	}

	public void setRotatDrawableResource(int id) {

		BitmapDrawable drawable = (BitmapDrawable) getContext().getResources()
				.getDrawable(id);

		setRotatDrawable(drawable);
	}

	public void setRotatDrawable(BitmapDrawable drawable) {
		rotatBitmap = drawable.getBitmap();
		back1Bitmap = ((BitmapDrawable) getResources().getDrawable(
				R.mipmap.img_air_num_point_bg)).getBitmap();
		initSize();
		postInvalidate();
	}

	private void initSize() {
		if (rotatBitmap == null) {

			// throw new NoBitMapError("Error,No bitmap in RotatView!");
			return;
		}
		width = back1Bitmap.getWidth();
		height = back1Bitmap.getHeight();

		circleRingSize = width / 12;
		cR = width * 1 / 11;
		maxwidth = Math.sqrt(width * width + height * height) * 3 / 4;

		o_x = o_y = (float) (maxwidth / 2);// 确定圆心坐标
	}

	/**
	 * 通过此方法来控制旋转度数，如果超过360，让它求余，防止，该值过大造成越界
	 * 
	 * @param added
	 */
	private void addDegree(float added) {
		System.out.println("当前圆盘所转的弧度增量:" + added);
		deta_degree += added;
		if (deta_degree > 360 || deta_degree < -360) {
			deta_degree = deta_degree % 360;
		}
		System.out.println("当前圆盘所转的弧度     :" + deta_degree);
		if (degreeChangeListener != null)
			degreeChangeListener.degreeChange(deta_degree, isUp);
	}

	/**
	 * 画外围标识
	 */
	private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private int bgColor = Color.rgb(177, 177, 177);
	private int markColor = Color.rgb(56, 199, 249);
	private int[] color = new int[] { Color.rgb(108, 170, 233),
			Color.rgb(108, 170, 233), Color.rgb(108, 170, 233),
			Color.rgb(241, 36, 106), Color.rgb(241, 36, 106) };

	private Paint iPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

	private void initInnerMark() {
		// iPaint.setStyle(Style.STROKE);
		// iPaint.setStrokeWidth(10);
	}

	private void initOutMark() {
		// mShader = new SweepGradient(o_x, o_y, new int[] {
		// markColor,Color.BLACK
		// }, new float[]{0.5f,0f});
		// mPaint.setShader(mShader);
		// mPaint.setStyle(Style.STROKE);
		// PathEffect effect = new DashPathEffect(new float[] { 8, 8 }, 1);
		// mPaint.setPathEffect(effect);
		// mPaint.setStrokeWidth(circleRingSize);
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		back1Bitmap = ((BitmapDrawable) getResources().getDrawable(
				R.mipmap.img_air_num_point_bg)).getBitmap();

		back2Bitmap = ((BitmapDrawable) getResources().getDrawable(
				R.mipmap.img_air_circle)).getBitmap();
		// setRotatDrawableResource(R.drawable.img_air_tp_mark);
		int width2 = back2Bitmap.getWidth();
		int height2 = back2Bitmap.getHeight();

		Matrix matrix = new Matrix();
		Matrix matrix1 = new Matrix();

		matrix1.postTranslate((float) (maxwidth - width2) / 2,
				(float) (maxwidth - height2) / 2);
		// 设置转轴位置
		matrix.setTranslate((float) width2 / 2, (float) height2 / 2);
		System.out.println("czq" + (float) height2 / 2);

		// 开始转
		matrix.preRotate(195 + deta_degree);
		Log.e("czq", "deta_degree:   " + deta_degree);
		// 转轴还原
		matrix.preTranslate(-(float) width2 / 2, -(float) height2 / 2);

		// 将位置送到view的中心
		matrix.postTranslate((float) (maxwidth - width2) / 2,
				(float) (maxwidth - height2) / 2);
		// int w2 = (int) (o_x - width * 3 - cR);
		// int h2 = (int) (o_y + width * 3 + cR);
		int w2 = (int) (o_x - maxwidth / 1.83 + cR);
		int h2 = (int) (o_y + maxwidth / 1.83 - cR);
		// 画虚线圆
		RectF oval = new RectF(w2, w2, h2, h2); // 用于定义的圆弧的形状和大小的界限
		// mPaint.setColor(bgColor);
		//
		// // canvas.drawArc(oval, -225, 270, false, mPaint);
		//
		// mPaint.setColor(markColor);
		iPaint.setColor(bgColor);
		canvas.drawArc(oval, -215, 248, true, iPaint);
		Shader shader = new SweepGradient(w2, h2, color, null);
		Matrix matrix2 = new Matrix();
		matrix2.setRotate(90, w2, h2);
		shader.setLocalMatrix(matrix2);
		mPaint.setShader(shader);
		canvas.drawArc(oval, -215, deta_degree + 8, true, mPaint);

		canvas.drawBitmap(back1Bitmap, o_x - (width / 2), o_y - (height / 2),
				mPaint);
		canvas.drawBitmap(back2Bitmap, o_x - (width2 / 2), o_y - (height2 / 2),
				paint);

		// // 画实心圆
		// RectF oval1 = new RectF(w2 + circleRingSize, w2 + circleRingSize, h2
		// - circleRingSize, h2 - circleRingSize); // 用于定义的圆弧的形状和大小的界限
		//
		// iPaint.setColor(markColor);
		// canvas.drawArc(oval1, -225, deta_degree, false, iPaint);
		//
		// iPaint.setColor(bgColor);
		// canvas.drawArc(oval1, -225 + deta_degree, 270 - deta_degree, false,
		// iPaint);
		// 抗锯齿
		canvas.setDrawFilter(pfd);
		// if (!rotatBitmap.isRecycled()) {
		canvas.drawBitmap(rotatBitmap, matrix, paint);
		// }
		super.onDraw(canvas);
	}

	Paint paint = new Paint();

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// 它的宽高不是图片的宽高，而是以宽高为直角的矩形的对角线的长度
		setMeasuredDimension((int) maxwidth, (int) maxwidth);

	}

	/**
	 * 手指触屏的初始x的坐标
	 */
	float down_x;

	/**
	 * 手指触屏的初始y的坐标
	 */
	float down_y;

	/**
	 * 移动时的x的坐标
	 */
	float target_x;

	/**
	 * 移动时的y的坐标
	 */
	float target_y;

	/**
	 * 放手时的x的坐标
	 */
	float up_x;

	/**
	 * 放手时的y的坐标
	 */
	float up_y;

	/**
	 * 当前的弧度(以该 view 的中心为圆点)
	 */
	float current_degree;

	/**
	 * 放手时的弧度(以该 view 的中心为圆点)
	 */
	float up_degree;

	/**
	 * 当前圆盘所转的弧度(以该 view 的中心为圆点)
	 */
	float deta_degree;

	/**
	 * 是否为顺时针旋转
	 */
	boolean isClockWise;

	private int isInrange(float x, float y) {
		int isInrange = -1;
		if (x > 0 && y > 0) {
			float distance = calDistanceFromCenter(x, y);
			if (distance >= maxwidth / 5 && distance <= maxwidth / 2) {
				isInrange = 0;
			} else if (distance > maxwidth / 2) {
				isInrange = 1;
			} else {
				isInrange = -1;
			}
		}
		return isInrange;
	}

	/**
	 * 计算当前的点离圆心距离
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private float calDistanceFromCenter(float x, float y) {
		float distance = 0;
		distance = (float) Math.sqrt((x - o_x) * (x - o_x) + (y - o_y)
				* (y - o_y));
		return distance;
	}

	private boolean isTouchUsed = false;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (isOpen) {
			return true;
		}
		if (!isON) {
			return true;
		}
		if (rotatBitmap == null) {
			throw new NoBitMapError("Error,No bitmap in RotatView!");
		}
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			down_x = event.getX();
			down_y = event.getY();
			if (isInrange(down_x, down_y) != 0) {
				isTouchUsed = false;
				break;
			}
			isTouchUsed = true;
			isUp = false;
			current_degree = detaDegree(o_x, o_y, down_x, down_y);
			System.out.println("Click down!   当前的幅度值:" + current_degree);
			break;

		}
		case MotionEvent.ACTION_MOVE: {
			down_x = target_x = event.getX();
			down_y = target_y = event.getY();
			if (!isTouchUsed) {
				break;
			}
			float degree = detaDegree(o_x, o_y, target_x, target_y);
			isUp = false;
			// 滑过的弧度增量
			float dete = degree - current_degree;
			System.out.println("Click move!  滑过的弧度增量:" + dete + " (未处理)");
			// 如果小于-90度说明 它跨周了，需要特殊处理350->17,
			if (dete < -240) {
				dete = dete + 360;
				// 如果大于90度说明 它跨周了，需要特殊处理-350->-17,
			} else if (dete > 240) {
				dete = dete - 360;
			}

			// 限制范围
			if (dete < 0) {
				if (deta_degree + dete < 0) {
					dete = -deta_degree;
				}
			}
			if (dete > 0) {
				if (deta_degree + dete > 240) {
					dete = 240 - deta_degree;
				}
			}
			System.out.println("Click move!  滑过的弧度增量:" + dete + " (已处理)");
			addDegree(dete);
			current_degree = degree;
			System.out.println("Click move!   当前的幅度值:" + deta_degree);
			postInvalidate();

			break;
		}
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP: {
			up_x = event.getX();
			up_y = event.getY();
			if (!isTouchUsed) {
				break;
			}
			isUp = true;
			up_degree = detaDegree(o_x, o_y, up_x, up_y);
			System.out.println("Click   up!   抬起的幅度值:" + up_degree);
			// 滑过的弧度增量
			float dete = up_degree - current_degree;
			System.out.println("Click move!  滑过的弧度增量:" + dete + " (未处理)");
			// 如果小于-90度说明 它跨周了，需要特殊处理350->17,
			if (dete < -240) {
				dete = dete + 360;
				// 如果大于90度说明 它跨周了，需要特殊处理-350->-17,
			} else if (dete > 240) {
				dete = dete - 360;
			}

			// 限制范围
			if (dete < 0) {
				if (deta_degree + dete < 0) {
					dete = -deta_degree;
				}
			}
			if (dete > 0) {
				if (deta_degree + dete > 240) {
					dete = 240 - deta_degree;
				}
			}
			System.out.println("Click up!  滑过的弧度增量:" + dete + " (已处理)");
			addDegree(dete);
			current_degree = up_degree;
			System.out.println("Click up!   当前的幅度值:" + deta_degree);
			postInvalidate();
			break;
		}
		}
		return true;
	}

	/**
	 * 计算以(src_x,src_y)为坐标圆点，建立直角体系，求出(target_x,target_y)坐标与x轴的夹角
	 * 主要是利用反正切函数的知识求出夹角
	 * 
	 * @param src_x
	 * @param src_y
	 * @param target_x
	 * @param target_y
	 * @return
	 */
	float detaDegree(float src_x, float src_y, float target_x, float target_y) {

		float detaX = target_x - src_x;
		float detaY = target_y - src_y;
		double d;
		if (detaX != 0) {
			float tan = Math.abs(detaY / detaX);

			if (detaX > 0) {

				if (detaY >= 0) {
					d = Math.atan(tan);

				} else {
					d = 2 * Math.PI - Math.atan(tan);
				}

			} else {
				if (detaY >= 0) {

					d = Math.PI - Math.atan(tan);
				} else {
					d = Math.PI + Math.atan(tan);
				}
			}

		} else {
			if (detaY > 0) {
				d = Math.PI / 2;
			} else {

				d = -Math.PI / 2;
			}
		}

		return (float) ((d * 180) / Math.PI);
	}

	/**
	 * 一个异常，用来判断是否有rotatbitmap
	 * 
	 * @author sun.shine
	 */
	static class NoBitMapError extends RuntimeException {

		/**
         * 
         */
		private static final long serialVersionUID = 1L;

		public NoBitMapError(String detailMessage) {
			super(detailMessage);
		}

	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		// if (rotatBitmap != null) {
		// rotatBitmap.recycle();
		// rotatBitmap = null;
		// }
	}

	public interface OnDegreeChangeListener {
		void degreeChange(float currentDegree, boolean isUp);
	}

	private int maxValue = 100, minValue = 0;

	public int getCurrentValue() {
		int value = (int) ((maxValue - minValue) * (deta_degree / 240));
		if (value >= maxValue)
			value = maxValue;
		else if (value < minValue) {
			value = minValue;
		}
		Log.e("czq", "Value:  " + value);
		return value;
	}

	public void setCurrentValue(int value) {
		Log.e("czq", "now_value1:  " + value);
		if (value > maxValue)
			deta_degree = 240;
		else if (value < minValue)
			deta_degree = 0;
		else {
			deta_degree = 240 * (value - minValue) / (maxValue - minValue);
		}
		System.out.println("set deta_degree = " + deta_degree);
		postInvalidate();
	}

	public int getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}

	public int getMinValue() {
		return minValue;
	}

	public void setMinValue(int minValue) {
		this.minValue = minValue;
	}

	private static Bitmap small(Bitmap bitmap) {
		Matrix matrix = new Matrix();
		matrix.postScale(0.65f, 0.65f); // 长和宽放大缩小的比例
		Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
		return resizeBmp;
	}
}
