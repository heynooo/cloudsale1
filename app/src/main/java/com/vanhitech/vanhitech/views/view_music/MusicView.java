package com.vanhitech.vanhitech.views.view_music;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.vanhitech.vanhitech.R;

import java.util.ArrayList;

public class MusicView extends BaseView {

	// private MusicItem musicItem;
	private ArrayList<MusicItem> list = new ArrayList<MusicItem>();
	private int itemNum = 20;
	private int itemWidth = 20;
	private int musicColor;
	private boolean randColor;
	private int musicType;

	public MusicView(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray ta = context.obtainStyledAttributes(attrs,
				R.styleable.MusicView);

		itemNum = ta.getInteger(R.styleable.MusicView_itemNum, 20);
		itemWidth = ta.getDimensionPixelSize(R.styleable.MusicView_itemWidth,
				15);
		musicColor = ta.getColor(R.styleable.MusicView_musicColor, 0xfff1f1f1);
		randColor = ta.getBoolean(R.styleable.MusicView_randColor, false);
		musicType = ta.getInteger(R.styleable.MusicView_musicType, 1);

		ta.recycle();

	}

	public MusicView(Context context) {
		super(context);
	}

	@Override
	protected void init() {
		// musicItem = new MusicItem(getHeight());
		// sleepTime = 150;

		for (int i = 0; i < itemNum; i++) {
			list.add(new MusicItem(i * getWidth() / itemNum, itemWidth,
					getHeight(), musicColor, randColor, musicType));
		}
	}

	@Override
	protected void drawSub(Canvas canvas) {
		// musicItem.draw(canvas);

		for (MusicItem item : list) {
			item.draw(canvas);
		}
	}

	@Override
	protected void logic() {
		// musicItem.move();

		for (MusicItem item : list) {
			item.move();
		}
	}

}
