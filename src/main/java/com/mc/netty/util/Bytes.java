package com.mc.netty.util;
/**
 * 
 * 类Bytes.java的实现描述：TODO 类实现描述 
 * @author ziliang.wu 2017年2月24日 下午5:05:40
 */
public class Bytes {

    /**
     * to byte array. 把一个short类型转换成一个byte数组
     * 
     * @param v
     * @return
     */
    public static byte[] short2bytes(short v) {
        byte[] ret = { 0, 0 };
        short2bytes(v, ret);
        return ret;
    }

    public static void short2bytes(short v, byte[] b) {
        short2bytes(v, b, 0);
    }

    public static void short2bytes(short v, byte[] b, int off) {
        b[off + 1] = (byte) v;
        b[off + 0] = (byte) (v >>> 8);
    }

    public static short bytes2short(byte[] b) {
        return bytes2short(b, 0);
    }

    public static short bytes2short(byte[] b, int off) {
        return (short) (((b[off + 1] & 0xFF) << 0) + ((b[off + 0]) << 8));
    }

    public static byte[] int2bytes(int v) {
        byte[] ret = { 0, 0, 0, 0 };
        int2bytes(v, ret);
        return ret;
    }

    public static void int2bytes(int v, byte[] b) {
        int2bytes(v, b, 0);
    }

    public static void int2bytes(int v, byte[] b, int off) {
        b[off + 3] = (byte) v;
        b[off + 2] = (byte) (v >>> 8);
        b[off + 1] = (byte) (v >>> 16);
        b[off + 0] = (byte) (v >>> 24);
    }

    public static int bytes2int(byte[] b) {
        return bytes2int(b, 0);
    }

    public static int bytes2int(byte[] b, int off) {
        return ((b[off + 3] & 0xFF) << 0) + ((b[off + 2] & 0xFF) << 8) + ((b[off + 1] & 0xFF) << 16)
               + ((b[off + 0]) << 24);
    }
}
