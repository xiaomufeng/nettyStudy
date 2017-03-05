/*
 * Copyright 2015-2020 wuage.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Wuage.com.
 */
package com.mc.netty.constant;

import com.mc.netty.util.Bytes;

/**
 * 类ImConstants.java的实现描述：TODO 类实现描述 
 * @author macun 2017年3月5日 下午9:17:04
 */
public class ImConstants {

    public static final String SYSTEM_NAME                = "steelIm";
    public static final int    DEFAULT_PORT               = 10010;
    public static final int    HEADER_LENGTH              = 12;                                  // 固定头
    public static final short  STEEL_IM_MAGIC             = (short) 0x5A9E;
    public static final byte   MAGIC_HIGH                 = Bytes.short2bytes(STEEL_IM_MAGIC)[0];

    public static final byte   MAGIC_LOW                  = Bytes.short2bytes(STEEL_IM_MAGIC)[1];

    public static final byte   STEEL_IM_MAGIC_NOW_VERSION = 1;

    public static final int    TIME_OUT                   = 600;                                 // 秒
}
