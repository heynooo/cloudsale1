package com.vanhitech.vanhitech.bean;

import android.annotation.SuppressLint;

/**
 * 中控配置使用 1.0
 * 协议工厂
 * @author ytz 2016/3/25
 *
 */
public class CMDFactory {
	public static CMDFactory instance;
    public byte key[] = new byte[]{0x00,0x00,0x00,0x00};
	public synchronized static CMDFactory getInstance() {
		if (instance == null) {
			instance = new CMDFactory();
		}
		return instance;
	}
	private CMDFactory() {

	}
    
	public byte[] getBytes(int k,byte bytes[]) {
		int len = 11 + bytes.length;
		byte senddatas[] = new byte[len];
		senddatas[0] = (byte) 0xFF;
		senddatas[1] = (byte) 0xFF;
		senddatas[2] = (byte) 0x00;
		senddatas[3] = (byte) len;
		senddatas[4] = 0x00;
		senddatas[5] = 0x00;
		senddatas[10] = (byte) k;
		for (int i = 0; i < bytes.length; i++) {
			senddatas[i+11] = bytes[i];
		}
		senddatas[6] = senddatas[7] = senddatas[8] = senddatas[9] = 0;
		//计算校验和
		byte[] sum = new byte[] { 0, 0, 0, 0 };
		for (int j = 0; j < 4; j++) {
			for (int i = j; i < len; i += 4) {
				sum[j] += senddatas[i];
			}
		}
		
		senddatas[6] = (byte) (sum[0] + senddatas[5] + key[0]);
		senddatas[7] = (byte) (sum[1] + senddatas[5] + key[1] + senddatas[6]);
		senddatas[8] = (byte) (sum[2] + senddatas[5] + key[2] + senddatas[7]);
		senddatas[9] = (byte) (sum[3] + senddatas[5] + key[3] + senddatas[8]);
		return senddatas;
    }
	
	@SuppressLint("DefaultLocale")
	public static String bytesToHex(byte[] src) {
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
	@SuppressLint("DefaultLocale")
	public static byte[] hexToBytes(String hexString) {
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
	}
}
