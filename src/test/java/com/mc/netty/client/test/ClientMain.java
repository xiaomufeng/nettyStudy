package com.mc.netty.client.test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.protobuf.InvalidProtocolBufferException;
import com.mc.netty.message.Message.Header;
import com.mc.netty.message.Message.Type;
import com.mc.netty.probuffer.CommonProtos.Common;
import com.mc.netty.probuffer.ConversationProtos.Conversation;
import com.mc.netty.probuffer.ConversationProtos.ConversationChangeNotification;
import com.mc.netty.probuffer.ConversationProtos.ConversationNotification;
import com.mc.netty.probuffer.ConversationProtos.ListConversationResponse;
import com.mc.netty.probuffer.IMUserProtos;
import com.mc.netty.probuffer.IMUserProtos.UserInfoResponse;
import com.mc.netty.probuffer.MessageProtos.Message;
import com.mc.netty.probuffer.MessageProtos.MessageNotification;

/**
 * 类ClientMain.java的实现描述：测试im服务的客户端
 * 
 * @author ziliang.wu 2017年2月28日 下午4:05:44
 */
public class ClientMain {

    private static final AtomicInteger count  = new AtomicInteger();

    public static void main(String[] args) throws IOException {
        String tcpUrl = "127.0.0.1:8081";
        String memberId = "1";
        String token = "1-886a455a356d460f56163822e66544ae";
        new TestServerSubscribeMessage(tcpUrl, memberId, token).startSub();
    }

    private static class TestServerSubscribeMessage extends ReceiveMessage {

        public TestServerSubscribeMessage(String tcpUrl, String memberId, String token) throws IOException{
            super(tcpUrl, memberId, token);
        }

        @Override
        protected void init() {
            super.init();
        }

        @Override
        protected void messageArrived(Type messageType, byte[] bodyBytes, Header requestHeader) {

            try {
                System.out.println(messageType);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                System.out.println(sdf.format(new Date()));
                boolean unZip = requestHeader.isCompress();
                if (unZip) {
//                    bodyBytes = ZipUtils.unGZip(bodyBytes);
                }
                switch (messageType) {
                    case COMMON_RESPONSE:
                        Common common = Common.parseFrom(bodyBytes);
                        outJson(common.getErrDesc());
                        break;
                    case MESSAGE_NOTIFICATION:
                        MessageNotification messageNotification = MessageNotification.parseFrom(bodyBytes);
                        for (Message message : messageNotification.getAddedListList()) {
                            System.out.println("发送人" + message.getSenderId());
                            System.out.println("消息id" + message.getMessageid());
                            System.out.println(message.getMessageContent());
                        }
                        break;
                    case GET_USER_INFO_RESPONSE:
                        UserInfoResponse userInfoResponse = UserInfoResponse.parseFrom(bodyBytes);
                        for (IMUserProtos.UserInfo user : userInfoResponse.getListUserInfosList()) {
                            System.out.println(user.getNick());
                            // System.out.println(user.getAvatar());
                            // System.out.println(user.getExtension());
                        }
                        System.out.println("总数" + count.getAndIncrement());
                        break;
                    case CONVERSATION_NOTIFICATION:
                        ConversationNotification conversationNotification = ConversationNotification.parseFrom(bodyBytes);
                        for (Conversation converstaion : conversationNotification.getAddedListList()) {
                            parseConversation(converstaion);
                        }
                        break;
                    case GET_CONVERSATION_LIST_REPONSE:
                        ListConversationResponse listConversationResponse = ListConversationResponse.parseFrom(bodyBytes);
                        System.out.println("会话总素:" + listConversationResponse.getListConversationsCount());
                        for (Conversation converstaion : listConversationResponse.getListConversationsList()) {
                            parseConversation(converstaion);
                        }
                        break;
                    case CONVERSATION_CHANGE_NOTIFICATION:
                        ConversationChangeNotification conversationChangeNotification = ConversationChangeNotification.parseFrom(bodyBytes);
                        for (Conversation converstaion : conversationChangeNotification.getChangedListList()) {
                            parseConversation(converstaion);
                        }
                        break;
                    default:
                        break;
                }
            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
            }
        }

    }

    public static void parseConversation(Conversation converstaion) {
        System.out.println("会话id:" + converstaion.getConversaionId());
        System.out.println("未读消息树" + converstaion.getUnreadMessageCount());
    }

    public static void outJson(Object obj) {
//        System.out.println(JSON.toJSON(obj));
    }
}
