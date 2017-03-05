package com.mc.netty.message;

import java.util.HashMap;
import java.util.Map;

import com.mc.netty.constant.ImConstants;

/**
 * 类Message.java的实现描述：消息
 * 
 * @author ziliang.wu 2017年2月27日 上午9:54:23
 */
public abstract class Message {

    protected final Header header;

    public Message(Header header){
        this.header = header;
    }

    public enum Type {
                      HEARTBEAT(80), COMMON_RESPONSE(100), LOGIN_REQUEST(101), LOGIN_RESPONSE(102),
                      GET_USER_INFO_REQUEST(201), GET_USER_INFO_RESPONSE(202), GET_CONVERSATION_LIST_REQUEST(301),
                      GET_CONVERSATION_LIST_REPONSE(302), GET_CONVERSATION_REQUEST(303), GET_CONVERSATION_REPONSE(304),
                      REMOVE_CONVERSATION_REQUEST(305), REMOVE_CONVERSATION_REPONSE(306), GET_MESSAGE_LIST_REQUEST(307),
                      GET_MESSAGE_LIST_REPONSE(308), CONVERSATION_NOTIFICATION(309),
                      CONVERSATION_CHANGE_NOTIFICATION(310), RESET_CONVERSATION_UNREAD_REQUEST(311),
                      RESET_CONVERSATION_UNREAD_RESPONSE(312), MESSAGE_NOTIFICATION(401), DISCONNECTION(90);

        final private int                 val;
        private static Map<Integer, Type> codeLookup = new HashMap<Integer, Type>();

        Type(int val){
            this.val = val;
        }

        static {
            for (Type t : Type.values()) {
                codeLookup.put(t.val, t);
            }
        }

        public int getVal() {
            return val;
        }

        public static Type valueOf(int i) {
            return codeLookup.get(i);
        }
    }

    public static class Header {

        private byte    version;
        private boolean ack;
        private boolean compress;
        private Type    type;

        public byte getVersion() {
            return version;
        }

        public void setVersion(byte version) {
            this.version = version;
        }

        public boolean isAck() {
            return ack;
        }

        public void setAck(boolean ack) {
            this.ack = ack;
        }

        public boolean isCompress() {
            return compress;
        }

        public void setCompress(boolean compress) {
            this.compress = compress;
        }

        public Type getType() {
            return type;
        }

        public void setType(Type type) {
            this.type = type;
        }

        public byte encode() {
            byte b = 0;
            b |= ack ? 1 : 0;
            b |= compress ? 2 : 0;
            return b;
        }

        public static Header getHeader(Type type) {
            Header header = new Header();
            header.setVersion(ImConstants.STEEL_IM_MAGIC_NOW_VERSION);
            header.setAck(false);
            header.setCompress(false);
            header.setType(type);
            return header;
        }
    }

    public Type getType() {
        return header.getType();
    }

    public Header getHeader() {
        return header;
    }

}
