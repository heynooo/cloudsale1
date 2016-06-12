package com.vanhitech.vanhitech.socket;

import com.vanhitech.vanhitech.bean.CMDFactory;
import com.vanhitech.vanhitech.bean.LanServerCmd;
import com.vanhitech.vanhitech.bean.LanServerCmd00;

import java.util.Timer;
import java.util.TimerTask;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

public class LanSocketClientHandler extends ChannelHandlerAdapter {
	private ChannelHandlerContext context;
	private boolean isConnected;
	private OnSocketListener listener;
	private CMDFactory cmdFactory = CMDFactory.getInstance();
	public void setOnSocketListener(OnSocketListener listener) {
		this.listener = listener;
	}

	public boolean isConnected() {
		return isConnected;
	}

	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		context = ctx;
		isConnected = true;
		startTimerIdle();
		if (listener != null) {
			listener.onSocketConnected();
		}
		super.channelActive(ctx);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		cancelIdleTimer();
		startTimerIdle();
		ByteBuf m = (ByteBuf) msg;
		byte[] req = new byte[m.readableBytes()];
		m.readBytes(req);
		boolean check = checkHeader(req);
		System.out.println("receive:"+CMDFactory.bytesToHex(req));
		if (!check) {
			System.out.println("头部验证错误");
			ReferenceCountUtil.release(m);
			return;
		}
		if (req.length < 11) {
			ReferenceCountUtil.release(m);
			return;
		}
		LanServerCmd serverCmd = checkCmd(req);
		if (listener != null) {
			listener.onSocketReceive(serverCmd);
		}
		ReferenceCountUtil.release(m);
		super.channelRead(ctx, msg);
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		isConnected = false;
		cancelIdleTimer();
		if (listener != null) {
			listener.onSocketClosed();
		}
		System.out.println("closed");
		super.channelUnregistered(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
		cause.printStackTrace();
		super.exceptionCaught(ctx, cause);
	}

	public void send(int cmd,byte[] bytes) {
		byte datas[] = cmdFactory.getBytes(cmd, bytes);
		ByteBuf message = Unpooled.buffer(datas.length);
		message.writeBytes(datas);
		System.out.println("send:"+CMDFactory.bytesToHex(datas));
		if (context != null) {
			context.writeAndFlush(message);
		}
	}

	public void close() {
		if (context != null) {
			context.close();
		}
	}

	public interface OnSocketListener {
		//socket 连接建立
		void onSocketConnected();
          //socket连接断开
		void onSocketClosed();
          //接收到返回命令
		void onSocketReceive(LanServerCmd serverCmd);
	}

	private boolean checkHeader(byte[] data) {
		if (data[0] != (byte) 0xFF || data[1] != (byte) 0xFF) {
			return false;
		}
		return true;
	}
	//连接成功接收到00号指令，接收到00号指令才能进行后续的操作
	private LanServerCmd checkCmd(byte[] data) {
		byte macs[] = new byte[7];
		LanServerCmd serverCmd= new LanServerCmd();
		serverCmd.cmd = data[10];
		switch (data[10]) {
		case 0x00:
			cmdFactory.key[0] = data[11];
			cmdFactory.key[1] = data[12];
			cmdFactory.key[2] = data[13];
			cmdFactory.key[3] = data[14];
			return new LanServerCmd00();
		case 0x0A: //接收到设备返回的控制0x0a指令
			System.arraycopy(data, 11, macs, 0, 7);
			serverCmd.mac = CMDFactory.bytesToHex(macs);
			byte devdatas[] = new byte[data[19]];
			System.arraycopy(data, 20, devdatas, 0, data[19]);
			serverCmd.datas = devdatas;
            return serverCmd;	
		case 0x0B: //设备状态改变返回0x0b指令
			System.arraycopy(data, 11, macs, 0, 7);
			serverCmd.mac = CMDFactory.bytesToHex(macs);
			byte devtas[] = new byte[data[19]];
			System.arraycopy(data, 20, devtas, 0, data[19]);
			serverCmd.datas = devtas;
            return serverCmd;	
		case 0x10: //配置成功
			serverCmd.datas =  new byte[]{data[11]};
			return serverCmd;
		case 0x12:  //返回遥控器匹配结果
			serverCmd.datas = new byte[]{data[11]};
			return serverCmd;
		default:
			break;
		}
		return new LanServerCmd();
	}
	
	//心跳 每隔35s发送一次心跳包
	private Timer idleTimer;
	private void startTimerIdle() {
		if (idleTimer == null) {
			idleTimer = new Timer();
			TimerTask task = new TimerTask() {
					
					@Override
				public void run() {
					sendIdleData();
				}
			};
			idleTimer.schedule(task,35*1000, 35*1000);
		}
		
	}
	
	private void cancelIdleTimer() {
		if (idleTimer != null) {	
			idleTimer.cancel();
			idleTimer = null;
		}
	}
	
	private void sendIdleData() {
		send(0xff, new byte[]{});
	}
}
