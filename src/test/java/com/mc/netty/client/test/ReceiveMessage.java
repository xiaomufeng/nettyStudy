package com.mc.netty.client.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import com.mc.netty.constant.ImConstants;
import com.mc.netty.message.Message;
import com.mc.netty.message.MessageFactory;
import com.mc.netty.message.OutMessage;
import com.mc.netty.util.Bytes;

public abstract class ReceiveMessage {

    private String              tcpUrl;
    protected String            memberId;
    private String              token;
    private final SocketChannel socketChannel;
    private Selector            selector;

    public ReceiveMessage(String tcpUrl, String memberId, String token) throws IOException{
        this.tcpUrl = tcpUrl;
        this.memberId = memberId;
        this.token = token;
        this.socketChannel = SocketChannel.open();
        selector = Selector.open();
    }

    public void startSub() {
        try {
            String[] urlArray = tcpUrl.split(":");
            if (urlArray.length < 2) {
                throw new RuntimeException("tcpUrl地址不正确");
            }
            socketChannel.socket().connect(new InetSocketAddress(urlArray[0], Integer.parseInt(urlArray[1])));
            System.out.println("connected ........");
            final ByteBuffer buffer = ByteBuffer.allocate(1024); // 创建Buffer
            final int keepAlive = 600;// 心跳
            // socketChannel.configureBlocking(false);
            // socketChannel.register(selector, SelectionKey.OP_READ);
            // ByteBuffer bb=ByteBuffer.allocate(1024);
            // while(true){
            // int count = 0;
            // while ((count = socketChannel.read(buffer)) > 0) { // 读取接收到的数据
            // buffer.flip();
            // byte[] dst = new byte[buffer.limit()];
            // buffer.get(dst);
            // buffer.clear();
            // }
            // if (count < 0) {
            // System.out.println("关闭同道");
            // try {
            // socketChannel.close();// 关闭同道 最可行的方案 解决注释上的问题
            // } catch (Exception e) {
            // e.printStackTrace();
            // }
            // }
            // }
            // 认证
            OutMessage loginMessage = MessageFactory.getLoginMessage(memberId, token);
            buffer.put(loginMessage.toBytes());
            buffer.flip();
            socketChannel.write(buffer);
            System.out.println("send bytes="+buffer);
            Thread.sleep(1000l);
            // 获取用户信息
            // for(int i=0;i<1;i++){
            // OutMessage getUserRequest = MessageFactory
            // .getRequestUserInfoMessage("1");
            // buffer.clear();
            // buffer.put(getUserRequest.toBytes());
            // buffer.flip();
            // socketChannel.write(buffer);
            // }
            // 获取会话列表
            // OutMessage getUserRequest = MessageFactory
            // .getRequestCollectionList();
            // buffer.clear();
            // buffer.put(getUserRequest.toBytes());
            // buffer.flip();
            // socketChannel.write(buffer);
            new Thread() {

                public void run() {
                    try {
                        ByteBuffer buffer = ByteBuffer.allocate(1024 * 100);
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ);
                        init();
                        long begin = System.currentTimeMillis() / 1000l;
                        while (true) {
                            int selects = selector.select(keepAlive * 1000);
                            long end = System.currentTimeMillis() / 1000l;
                            if ((end - begin) >= keepAlive) {
                                // 简单心跳
                                OutMessage heartbeat = MessageFactory.getHeartbeatMessage();
                                buffer.clear();
                                buffer.put(heartbeat.toBytes());
                                buffer.flip();
                                System.out.println("发送心跳");
                                socketChannel.write(buffer);
                                begin = end;
                            }
                            if (selects > 0) {
                                Set<SelectionKey> selectedKeys = selector.selectedKeys(); // 获取发生的事件
                                Iterator<SelectionKey> it = selectedKeys.iterator(); // 依次进行处理
                                while (it.hasNext()) {
                                    SelectionKey selectedKey = it.next();
                                    it.remove();
                                    if (selectedKey.isReadable()) {
                                        selectedKey.interestOps(selectedKey.interestOps() & (~SelectionKey.OP_READ));
                                        buffer.clear();
                                        int count = 0;
                                        try {
                                            while ((count = socketChannel.read(buffer)) > 0) { // 读取接收到的数据
                                                buffer.flip();
                                                while (buffer.hasRemaining()) {
                                                    byte[] dst = new byte[ImConstants.HEADER_LENGTH];
                                                    buffer.mark();
                                                    buffer.get(dst);
                                                    // 验证magic
                                                    if (dst[0] != ImConstants.MAGIC_HIGH
                                                        || dst[1] != ImConstants.MAGIC_LOW) {
                                                        return;
                                                    }
                                                    Message.Header requestHeader = new Message.Header();
                                                    // 版本
                                                    byte version = dst[2];
                                                    requestHeader.setVersion(version);
                                                    // 标志位
                                                    requestHeader.setAck((dst[3] & 1) > 0);
                                                    requestHeader.setCompress((dst[3] & 2) > 0);
                                                    // 类型
                                                    int type = Bytes.bytes2short(new byte[] { dst[4], dst[5] });
                                                    Message.Type messageType = Message.Type.valueOf(type);
//                                                    if (messageType == null) {
//                                                        throw new SteelImException(String.format("消息的类型【%d】不存在", type));
//                                                    }
                                                    requestHeader.setType(messageType);
                                                    int length = Bytes.bytes2int(new byte[] { dst[6], dst[7], dst[8],
                                                                                              dst[9] });
                                                    int rpc = Bytes.bytes2short(new byte[] { dst[10], dst[11] });
                                                    System.out.println("rpc:" + rpc);
                                                    if (length <= buffer.remaining()) {
                                                        byte[] bodyBytes = new byte[length];
                                                        buffer.get(bodyBytes);
                                                        messageArrived(messageType, bodyBytes, requestHeader);
                                                    } else {
                                                        buffer.reset();
                                                        buffer.compact();
                                                        break;
                                                    }

                                                }
                                                // buffer.clear();
                                            }
                                            if (count < 0) {
                                                try {
                                                    socketChannel.close();// 关闭同道
                                                                          // 最可行的方案
                                                                          // 解决注释上的问题
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            // 没有可用字节,继续监听OP_READ
                                            selectedKey.interestOps(SelectionKey.OP_READ);
                                            selectedKey.selector().wakeup();
                                        } catch (IOException e1) {
                                            e1.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    } catch (IOException e2) {
                        throw new RuntimeException("监听出错");
                    }
                };
            }.start();
        } catch (Exception e) {
            throw new RuntimeException("启动失败", e);
        }
    }

    public void close() {
        try {
            selector.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            socketChannel.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 子类初始化
     */
    protected void init() {

    }

    protected abstract void messageArrived(Message.Type messageType, byte[] bodyBytes, Message.Header requestHeader);
}
