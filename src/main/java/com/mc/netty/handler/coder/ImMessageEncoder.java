/*
 * Copyright 2015-2020 wuage.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Wuage.com.
 */
package com.mc.netty.handler.coder;

import java.util.List;

import com.mc.netty.message.OutMessage;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

/**
 * 类ImMessageEncoder.java的实现描述：TODO 类实现描述 
 * @author macun 2017年3月5日 下午8:58:28
 */
public class ImMessageEncoder extends MessageToMessageEncoder<Object>{

    
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
        byte[] data = ((OutMessage) msg).toBytes();
        System.out.println("get messge, begin encodes......");
        out.add(Unpooled.wrappedBuffer(data));
    }

}
