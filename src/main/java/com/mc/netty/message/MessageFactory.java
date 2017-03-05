package com.mc.netty.message;

import com.mc.netty.message.Message.Header;
import com.mc.netty.probuffer.AuthProtos;
import com.mc.netty.probuffer.AuthProtos.LoginRequest.Builder;
import com.mc.netty.probuffer.ConversationProtos.Conversation.ConversationType;
import com.mc.netty.probuffer.ConversationProtos.ListConversationRequest;
import com.mc.netty.probuffer.IMUserProtos.UserInfoRequest;

public class MessageFactory {

    private final static OutMessage DISCONNECTION_MESSAGE = new OutMessage(Message.Header.getHeader(Message.Type.DISCONNECTION),
                                                                           0, new byte[0]);
    private final static OutMessage HEARTBEAT_MESSAGE     = new OutMessage(Message.Header.getHeader(Message.Type.HEARTBEAT),
                                                                           0, new byte[0]);

    public static OutMessage getDisconnnectionMessage() {
        return DISCONNECTION_MESSAGE;
    }

    public static OutMessage getHeartbeatMessage() {
        return HEARTBEAT_MESSAGE;
    }

    public static OutMessage getLoginMessage(String memberId, String token) {
        Header header = Message.Header.getHeader(Message.Type.LOGIN_REQUEST);
        Builder builer = AuthProtos.LoginRequest.newBuilder();
        builer.setToken(token);
        builer.setMemberId(memberId);
        OutMessage outMessage = new OutMessage(header, 0, builer.build().toByteArray());
        return outMessage;
    }

    public static OutMessage getRequestUserInfoMessage(String memberId) {
        Header header = Message.Header.getHeader(Message.Type.GET_USER_INFO_REQUEST);
        UserInfoRequest.Builder builer = UserInfoRequest.newBuilder();
        builer.addMemberId(memberId);
        OutMessage outMessage = new OutMessage(header, 0, builer.build().toByteArray());
        return outMessage;
    }

    public static OutMessage getRequestCollectionList() {
        Header header = Message.Header.getHeader(Message.Type.GET_CONVERSATION_LIST_REQUEST);
        ListConversationRequest.Builder builer = ListConversationRequest.newBuilder();
        builer.setType(ConversationType.CHAT);
        builer.setLastUpdateTime(0l);
        builer.setSize(50);
        OutMessage outMessage = new OutMessage(header, 0, builer.build().toByteArray());
        return outMessage;
    }
}
