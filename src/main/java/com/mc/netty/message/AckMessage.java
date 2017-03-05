package com.mc.netty.message;

/**
 * 
 * 类AckMessage.java的实现描述：需要确认的消息
 * @author ziliang.wu 2017年2月27日 上午9:54:48
 */
public class AckMessage extends Message {
	protected final int requestId;
	protected final byte[] data;

	public AckMessage(Header header, int requestId, byte[] data) {
		super(header);
		this.requestId = requestId;
		this.data = data;
	}

	public AckMessage(Header header) {
		super(header);
		this.requestId = 0;
		this.data = null;
	}

	public int getRequestId() {
		return requestId;
	}

	public byte[] getData() {
		return data;
	}

}
