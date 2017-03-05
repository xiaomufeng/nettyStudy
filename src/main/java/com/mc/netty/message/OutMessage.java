package com.mc.netty.message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.mc.netty.constant.ImConstants;
import com.mc.netty.util.Bytes;


public class OutMessage extends AckMessage {

	public OutMessage(Header header, int requestId, byte[] data) {
		super(header, requestId, data);
	}

	public final byte[] toBytes() {
		int length = data != null ? data.length : 0;
		ByteArrayOutputStream baos = new ByteArrayOutputStream(
				ImConstants.HEADER_LENGTH + length);
		try {
			// 写入header
			baos.write(Bytes.short2bytes(ImConstants.STEEL_IM_MAGIC));
			// 写入版本
			baos.write(header.getVersion());
			// 标志位
			baos.write(header.encode());
			// 类型
			baos.write(Bytes.short2bytes((short) header.getType().getVal()));
			// 获取消息体长度
			baos.write(Bytes.int2bytes(length));
			// reqid
			baos.write(Bytes.short2bytes((short) requestId));
			// 写入消息
			if (length > 0) {
				baos.write(data);
			}
		} catch (IOException e) {
		}
		return baos.toByteArray();
	}

}
