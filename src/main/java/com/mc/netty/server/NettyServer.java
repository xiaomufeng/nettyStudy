/*
 * Copyright 2015-2020 wuage.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Wuage.com.
 */
package com.mc.netty.server;

import com.mc.netty.handler.TcpChannelInitializer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 类NettyServer.java的实现描述：TODO 类实现描述
 * 
 * @author macun 2017年3月5日 下午8:26:57
 */
public class NettyServer {

    private final NioEventLoopGroup bossGroup   = new NioEventLoopGroup(1);
    private final NioEventLoopGroup workerGroup = new NioEventLoopGroup();
    private int                     port        = 8081;
    private String                  localHost   = "127.0.0.1";
    private String                  serverUrl;                             // host:port
    private Channel                 channle;

    private ServerBootstrap getDefaultServerBootstrap() {
        ServerBootstrap bootStrap = new ServerBootstrap();
        bootStrap.group(bossGroup, workerGroup).option(ChannelOption.SO_BACKLOG, 1000)
              // 接收和发送缓冲区大小
              .option(ChannelOption.SO_SNDBUF, 32 * 1024).option(ChannelOption.SO_RCVBUF, 32 * 1024).option(
                                                                                                            ChannelOption.TCP_NODELAY,
                                                                                                            true)
              // 容量动态调整的接收缓冲区分配器
              .option(ChannelOption.RCVBUF_ALLOCATOR,
                      AdaptiveRecvByteBufAllocator.DEFAULT).channel(NioServerSocketChannel.class).childOption(ChannelOption.SO_KEEPALIVE,
                                                                                                              true);

        return bootStrap;
    }
    
    public ChannelFuture start() throws InterruptedException {
        // 此处可以考虑只绑定外网ip
        ChannelFuture future = getDefaultServerBootstrap().childHandler(new TcpChannelInitializer()).bind(port).sync();
        // ServerManager.pubServer(serverUrl);
        System.out.println("steelIm服务启动成功,端口号：" + port + '.');
        channle = future.channel();
        return future;
    }
    
    
    public void destroy() {
        System.out.println("steelIm服务开始关闭");
        if (channle != null) {
            channle.close();
        }
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        System.out.println("steelIm服务成功关闭");
    }

}
