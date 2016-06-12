package com.vanhitech.vanhitech.socket;

import com.vanhitech.vanhitech.bean.CMDFactory;

import java.net.InetSocketAddress;
import java.nio.ByteOrder;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;


/**
 * socket 连接类
 * @author ytz 2016/3/25
 *
 */
public class LanSocketClient {
	private LanSocketClientHandler nettyClientHandler;
	private Bootstrap bootstrap;
	private EventLoopGroup group;
	private LanSocketClientHandler.OnSocketListener listener;

	public LanSocketClient() {
	}

	public void setOnSocketListener(LanSocketClientHandler.OnSocketListener listener) {
		this.listener = listener;
		if (nettyClientHandler != null) {
			nettyClientHandler.setOnSocketListener(listener);
		}
	}

	public void connect(String host, int port) {
		try {
			nettyClientHandler = new LanSocketClientHandler();
			nettyClientHandler.setOnSocketListener(listener);
			bootstrap = new Bootstrap();
			group = new NioEventLoopGroup();
			bootstrap.group(group).channel(NioSocketChannel.class);
			bootstrap.option(ChannelOption.TCP_NODELAY, true).option(ChannelOption.SO_KEEPALIVE, true)
					.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
					.option(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT)
					.handler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel arg0) throws Exception {
							arg0.pipeline().addLast(new LengthFieldBasedFrameDecoder(ByteOrder.LITTLE_ENDIAN,
									256 * 1024, 3, 1, -4, 0, true));
							arg0.pipeline().addLast(nettyClientHandler);
						}
					});
			// 连接到port端口的服务端
			final ChannelFuture f = bootstrap.connect(new InetSocketAddress(host, port));
			f.addListener(new ChannelFutureListener() {

				@Override
				public void operationComplete(ChannelFuture arg0) throws Exception {
					if (!arg0.isSuccess()) {
						if (!arg0.isCancelled()) {
							if (listener != null) {
								listener.onSocketClosed();
							}
							close();
						}
					}
				}
			});
			f.sync();
			// 等待客户端连裤关闭
			f.channel().closeFuture().sync();

		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			if (group != null) {
				group.shutdownGracefully();
			}
		}

	}

	public boolean isConnected() {
		return (nettyClientHandler == null) ? false : nettyClientHandler.isConnected();
	}

	public void close() {
		if (group == null) {
			return;
		}
		group.shutdownGracefully();
	}
	/**
	 * 发送服务器信息给设备
	 * @param host  设备连接的服务器连接地址
	 * @param port  设备连接的服务器端口
	 */
    public void sendConfigData(String host,int port) {
    	String[] hosts = host.split("\\.");
		byte hostsBytes[] = { (byte) Integer.parseInt(hosts[0]),
				(byte) Integer.parseInt(hosts[1]),
				(byte) Integer.parseInt(hosts[2]),
				(byte) Integer.parseInt(hosts[3]) };
		int len = hostsBytes.length + 3; // ip长度为4
		byte bytes[] = new byte[len];
		bytes[0] = (byte) (len - 3);
		for (int i = 0; i < hostsBytes.length; i++) {
			bytes[i + 1] = hostsBytes[i];
		}
		bytes[len - 2] = (byte)port;
		bytes[len - 1] = (byte) (port >> 8);
		if (nettyClientHandler.isConnected()) {
			nettyClientHandler.send(0x0F, bytes);
		}
    }
	/**
	 * 发送控制命令
	 * @param sn 设备的mac地址
	 * @param data  协议中mac地址后面的数据
	 */
	public void sendControlData(String sn, byte[] data) {
		if (nettyClientHandler == null) {
			return;
		}
		byte macs[] = CMDFactory.hexToBytes(sn);
		byte datas[] = new byte[macs.length + data.length ];
		System.arraycopy(macs, 0, datas, 0, 7);
		System.arraycopy(data, 0, datas,7, data.length);
		if (nettyClientHandler.isConnected()) {
			nettyClientHandler.send(0x09, datas);
		}
	}

	/**
	 * 发送控制命令
	 * @param sn 设备的mac地址
	 * @param data  协议中mac地址后面的数据
	 */
	public void sendMatchData(String sn, byte[] data) {
		if (nettyClientHandler == null) {
			return;
		}
		byte macs[] = CMDFactory.hexToBytes(sn);
		byte datas[] = new byte[macs.length + data.length];
		System.arraycopy(macs, 0, datas, 0, 7);
		System.arraycopy(data, 0, datas, 7, data.length);
		if (nettyClientHandler.isConnected()) {
			nettyClientHandler.send(0x11, data);
		}
		
	}
}
