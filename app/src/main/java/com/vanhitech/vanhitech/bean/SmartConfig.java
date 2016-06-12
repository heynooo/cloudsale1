package com.vanhitech.vanhitech.bean;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Handler;

import com.winnermicro.smartconfig.ConfigType;
import com.winnermicro.smartconfig.ISmartConfig;
import com.winnermicro.smartconfig.OneShotException;
import com.winnermicro.smartconfig.SmartConfigFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/**
 * 中控配置使用 1.0
 * 
 * 配置流程
 * 1.先调用联盛德威配置接口ISmarconfig
 * 2.监听50001端口的广播，一旦配置成功，会接收到设备返回的设备信息
 * 3.监听到广播之后，建立tcp连接,向设备发送设备所连接的服务器的ip和端口
 * 
 * @author ytz
 *
 */

@SuppressLint("DefaultLocale")
public class SmartConfig {
	public static final int CONFIGSUCCESSED = 1000;
	public static final int CONFIGFAILED = 1001;
	private int errorId = 0;
	private Handler handler;
	private String pwd;
	private boolean isStart = false;
	private String mac = "";
	private String ip = "";
	private Boolean isThreadDisable = false;
	private ISmartConfig smartConfig = null;
	private ReceiveThread tReceived;
	private WifiManager wifiManager;

	
	public SmartConfig(Context context, Handler handler) {
		this.handler = handler;
		this.wifiManager = (WifiManager) context
				.getSystemService(Activity.WIFI_SERVICE);
		SmartConfigFactory factory = new SmartConfigFactory();
		smartConfig = factory.createSmartConfig(ConfigType.UDP, context);


	}
	/**
	 * 开始配置设备
	 * @param ssid 路由器名称
	 * @param pwd  路由器密码 （密码可以为空）
	 */
	public void startConfig(String ssid, String pwd) {
		this.pwd = pwd;

		isStart = true;
		if (tReceived != null) {
			isThreadDisable = true;
			tReceived.interrupt();
		}
		isThreadDisable = false;
		//启动接收线程，接受设备的返回值
		tReceived = new ReceiveThread();
		tReceived.start();
		//启动配置线程
		new Thread(new UDPReqThread()).start();
	}
    /**
     * 获取设备的mac地址
     * @return
     */
	public String getMac() {
		return mac;
	}
	 /**
     * 获取设备的IP地址
     * @return
     */
	public String getIp() {
		return ip;
	}
	/**
     * 配置完成结束配置
     */
	public void stopConfig() {
		isThreadDisable = true;
		isStart = false;
		smartConfig.stopConfig();

	}

	class ReceiveThread extends Thread {
		private WifiManager.MulticastLock lock; //获取组播锁

		// WifiManager manager;
		public ReceiveThread() {
			this.lock = wifiManager.createMulticastLock("UDPwifi"); 
		}

		@Override
		public void run() {
			super.run();
			this.lock.acquire();
			StartListen();
			this.lock.release();
		}

		public void StartListen() {
			Integer port = 50001;
			byte[] message = new byte[100];
			try {
				DatagramSocket datagramSocket = new DatagramSocket(null);
				datagramSocket.bind(new InetSocketAddress(port));
				datagramSocket.setBroadcast(true);
				datagramSocket.setSoTimeout(1000);
				DatagramPacket datagramPacket = new DatagramPacket(message,
						message.length);
				try {
					while (!isThreadDisable) {
						try {
							
							datagramSocket.receive(datagramPacket);
						
							int count = datagramPacket.getData().length;
							if (count < 33) {
								continue;
							}
							byte bytes[] = datagramPacket.getData();
							int i = 0;
							boolean hasCorrectData = false;
							for (i = 0; i < bytes.length - 4; i++) {
								if ((bytes[i] & 0xff) == 238
										&& (bytes[i + 1] & 0xff) == 238
										&& (bytes[i + 2] & 0xff) == 0
										) {
									hasCorrectData = true;
									break;
								}
							}
							if (i > count - 33) {
								continue;
							}
							if (hasCorrectData) {
								String strMsg = "";
								for (int j = i + 11; j < i + 17; j++) {
									strMsg += String.format("%02x", bytes[j]);
								}
								mac = strMsg.toUpperCase();
								ip = (bytes[i + 18] & 0xff) + "."
										+ (bytes[i + 19] & 0xff) + "."
										+ (bytes[i + 20] & 0xff) + "."
										+ (bytes[i + 21] & 0xff);
								stopConfig();
								handler.sendEmptyMessage(CONFIGSUCCESSED);//TODO 配置成功
							}

							datagramSocket.close();
						} catch (SocketTimeoutException ex) {
						}

					}
				} catch (IOException e) {// IOException
					e.printStackTrace();
				}
				datagramSocket.close();
			} catch (SocketException e) {
				e.printStackTrace();
			}
		}
	}

	class UDPReqThread implements Runnable {
		public void run() {
			errorId = 0;
			try {
				if (wifiManager.isWifiEnabled()) {
					while (isStart) {
						if (smartConfig.startConfig(pwd) == false) {	//TODO
							break;
						}
						Thread.sleep(10);
					}
				}
			} catch (OneShotException oe) {
				oe.printStackTrace();
				errorId = oe.getErrorID();
				if (errorId == OneShotException.ERROR_NETWORK_NOT_SUPPORT) {
					stopConfig();
					handler.sendEmptyMessage(CONFIGFAILED);//TODO
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			}
		}
	}

	@SuppressLint("DefaultLocale")
	public String bytesToHexString(byte[] src) {
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
}
