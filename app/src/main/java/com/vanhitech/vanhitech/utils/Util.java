package com.vanhitech.vanhitech.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Util {
	public static boolean isUpdate(String current_version, String latest_version) {
		boolean isUpdate = true;
		String current_v[] = new String[] { "", "", "" };
		String latest_v[] = new String[] { "", "", "" };
		int temp = 0;
		for (int i = 0; i < current_version.length(); i++) {

			if (current_version.charAt(i) == '.') {
				temp++;
			} else {
				current_v[temp] += current_version.charAt(i);
			}
		}
		Log.e("swg", current_v[0] + " " + current_v[1] + " " + current_v[2]);
		// Log.e("swg",current_version);
		// Log.e("swg",latest_version);
		// if(v!=null)
		// Log.e("swg",v[0]+" "+v[1]+" "+v[2]);
		temp = 0;
		for (int i = 0; i < latest_version.length(); i++) {

			if (latest_version.charAt(i) == '.') {
				temp++;
			} else {
				latest_v[temp] += latest_version.charAt(i);
			}
		}
		// Log.e("swg","latest"+latest_v[0]+" "+latest_v[1]+" "+latest_v[2]);

		// Log.e("swg",Integer.parseInt(latest_v[i])+"");
		if (Integer.parseInt(latest_v[0]) < Integer.parseInt(current_v[0])) {
			isUpdate = false;
		} else if (Integer.parseInt(latest_v[0]) == Integer
				.parseInt(current_v[0])) {
			if (Integer.parseInt(latest_v[1]) < Integer.parseInt(current_v[1])) {
				isUpdate = false;
			} else if (Integer.parseInt(latest_v[1]) == Integer
					.parseInt(current_v[1])) {
				if (Integer.parseInt(latest_v[2]) <= Integer
						.parseInt(current_v[2])) {
					isUpdate = false;
				} else {
					isUpdate = true;
				}
			} else {
				isUpdate = true;
			}
		} else {
			isUpdate = true;
		}
		return isUpdate;
	}

	public static String connectNeworkForVersionUpdate(String urlPath,
			String requestStr) {

		try {
			// 创建URL
			URL url = new URL(urlPath);
			// 获得连接
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			// 设置请求参数
			connection.setReadTimeout(3000);// 超时
			connection.setRequestMethod("POST");// 请求方式
			connection.setDoInput(true);// 可读写
			connection.setDoOutput(true);
			// connection.setRequestProperty("Content-Type",
			// "application/x-www-form-urlencoded");// 设置请求 参数类型
			byte[] sendData = requestStr.getBytes("UTF-8");// 将请求字符串转成UTF-8格式的字节数组
			connection.setRequestProperty("Content-Length", sendData.length
					+ "");// 请求参数的长度

			OutputStream outputStream = connection.getOutputStream();// 得到输出流对象
			outputStream.write(sendData);// 发送写入数据

			/*
			 * //获得 服务器响应代码 int code = connection.getResponseCode();
			 * if(code==20){ //获取数据 }
			 */
			// 响应
			InputStream inputStream = connection.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream);
			BufferedReader bReader = new BufferedReader(inputStreamReader);
			String str = "";
			String temp = "";
			while ((temp = bReader.readLine()) != null) {
				str = str + temp + "\n";
			}
			return str;// 返回响应数据

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String[] binaryArray = { "0000", "0001", "0010", "0011",
			"0100", "0101", "0110", "0111", "1000", "1001", "1010", "1011",
			"1100", "1101", "1110", "1111" };

	public static int Dp2Px(Context context, float dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}

	public static int Px2Dp(Context context, float px) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (px / scale + 0.5f);
	}

	// hex String to bytes[]
	@SuppressLint("DefaultLocale")
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toLowerCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	private static byte charToByte(char c) {
		return (byte) "0123456789abcdef".indexOf(c);
		// return (byte) "0123456789ABCDEF".indexOf(c);
	}

	// bytes to binary
	public static String bytes2BinaryStr(byte bArray) {

		String outStr = "";
		int pos = 0;
		// 高四位
		pos = (bArray & 0xF0) >> 4;
		outStr += binaryArray[pos];
		// 低四位
		pos = bArray & 0x0F;
		outStr += binaryArray[pos];
		return outStr;

	}

	/**
	 * ��ʾshortToast
	 * 
	 * @param context
	 * @param msg
	 *            Ҫ��ʾ����Ϣ
	 */

	private static Toast t;

	public static void showToast(Context context, String msg) {
		if (t != null) {
			t.cancel();
		}
		t = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		t.show();
	}

	public static void cancelToast() {
		if (t != null) {
			t.cancel();
		}
	}

	private static ProgressDialog pd;

	public static void showProgressDialog(Context context, CharSequence title,
			CharSequence message) {
		cancelProgressDialog();
		if (context == null) {
			return;
		}
		if (title == null) {
			title = "";
		}
		pd = ProgressDialog.show(context, title, message);
	}

	public static void cancelProgressDialog() {
		if (pd != null) {
			pd.cancel();
		}
		pd = null;
	}

	@SuppressLint("DefaultLocale")
	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString().toUpperCase();
	}

	private static Dialog ad;

	public static void cancelAllDialog() {
		if (ad != null)
			ad.cancel();
		cancelProgressDialog();
		cancelToast();
	}

	/**
	 * To determine whether a network connection is normal
	 * 
	 * @param act
	 * @return
	 */
	public static boolean detect(Activity act) {
		if (act == null) {
			return false;
		}
		ConnectivityManager manager = (ConnectivityManager) act
				.getApplicationContext().getSystemService(
						Context.CONNECTIVITY_SERVICE);

		if (manager == null) {
			return false;
		}

		NetworkInfo networkinfo = manager.getActiveNetworkInfo();

		if (networkinfo == null || !networkinfo.isAvailable()) {
			return false;
		}

		return true;
	}

	/**
	 * show AlertDialog
	 * 
	 * @param context
	 * @param title
	 * @param msg
	 * @param positiveDetail
	 * @param posLsn
	 * @param negDetail
	 * @param negLsn
	 */
	public static void showAlertDialog(Context context, String title,
			String msg, String positiveDetail, OnClickListener posLsn,
			String negDetail, OnClickListener negLsn) {
		// ((Activity) context).removeDialog(0);
		if (ad != null) {
			ad.cancel();
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setCancelable(false);
		if (title != null)
			builder.setTitle(title);
		if (msg != null)
			builder.setMessage(msg);
		if (positiveDetail != null && posLsn != null)
			builder.setPositiveButton(positiveDetail, posLsn);
		if (negLsn != null && negDetail != null)
			builder.setNegativeButton(negDetail, negLsn);

		if (negLsn == null && negDetail != null) {
			builder.setNegativeButton(negDetail, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// cancelAllDialog();
					dialog.dismiss();
				}
			});
		}
		ad = builder.show();

	}

	/**
	 * 用更节省内存的方式获取图片
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	public static Bitmap getEconomizeBitmap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		Bitmap bitmap = null;
		try {
			InputStream is = context.getResources().openRawResource(resId);
			bitmap = BitmapFactory.decodeStream(is, null, opt);
			is.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bitmap;
	}

	public static Bitmap getEconomizeBitmap(Context context, Uri uri) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		Bitmap bitmap = null;
		try {
			InputStream is = context.getContentResolver().openInputStream(uri);
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}

}
