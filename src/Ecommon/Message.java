package Ecommon;

import java.io.Serializable;

/*
 *表示客户端和服务器端的消息对象
 */
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    private String from; //发送者
    private String to; //接收者
    private String text; // 消息内容
    private String sendtime; // 发送时间
    private String type; //消息类型

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSendtime() {
        return sendtime;
    }

    public void setSendtime(String sendtime) {
        this.sendtime = sendtime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}