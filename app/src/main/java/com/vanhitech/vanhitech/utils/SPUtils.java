package com.vanhitech.vanhitech.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @创建者
 * @创时间 	 2015-12-18 上午10:28:13
 * @描述	     对sp的操作的封装
 *
 * @版本       $Rev: 3 $
 * @更新者     $Author: admin $
 * @更新时间    $Date: 2015-12-18 10:54:44 +0800 (星期五, 18 十二月 2015) $
 * @更新描述    TODO
 */
public class SPUtils {
	private SharedPreferences	mSp;
	private Editor				mEditor;

	public SPUtils(Context context) {
		mSp = context.getSharedPreferences("Vanhitech", Context.MODE_PRIVATE);
		mEditor = mSp.edit();
	}

	/**取出string*/
	public String getString(String key, String defValue) {
		return mSp.getString(key, defValue);
	}

	/**取出int*/
	public int getInt(String key, int defValue) {
		return mSp.getInt(key, defValue);
	}

	/**取出boolean*/
	public boolean getBoolean(String key, boolean defValue) {
		return mSp.getBoolean(key, defValue);
	}

	/**存入string*/
	public void putString(String key, String value) {
		mEditor.putString(key, value);
		mEditor.commit();
	}

	/**存入int*/
	public void putInt(String key, int value) {
		mEditor.putInt(key, value);
		mEditor.commit();
	}

	/**存入boolean*/
	public void putBoolean(String key, boolean value) {
		mEditor.putBoolean(key, value);
		mEditor.commit();
	}
}
