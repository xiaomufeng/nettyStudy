/*
 * Copyright 2015-2020 wuage.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Wuage.com.
 */
package com.mc.netty.handler;

import com.mc.netty.handler.coder.ImMessageDecoder;
import com.mc.netty.handler.coder.ImMessageEncoder;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;


/**
 * 类TcpChannelInitializer.java的实现描述：TODO 类实现描述 
 * @author macun 2017年3月5日 下午8:56:52
 */
public class TcpChannelInitializer extends ChannelInitializer<SocketChannel> {

    /* (non-Javadoc)
     * @see io.netty.channel.ChannelInitializer#initChannel(io.netty.channel.Channel)
     */
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("encoder", new ImMessageEncoder());
        pipeline.addLast("decoder", new ImMessageDecoder());
//        pipeline.addLast("steelImHandler", new ImMessageHandler());
        
    }

}
