/*
 * Copyright 2015-2020 wuage.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Wuage.com.
 */
package com.mc.netty.server;

/**
 * 类MainStart.java的实现描述：TODO 类实现描述 
 * @author macun 2017年3月5日 下午9:27:28
 */
public class MainStart {

    public static void main(String[] args){
        NettyServer server = new NettyServer();
        try {
            server.start();
        } catch (InterruptedException e) {
            System.out.println("exception: "+e);
            e.printStackTrace();
        }
    }
}
