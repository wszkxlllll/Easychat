package Ecommon;

public interface type {
    String text_login_succeed = "1"; //登录成功
    String text_login_fail = "0"; //登录失败
    String text_com_tex = "2"; //普通信息包
    String text_get_online_friend = "3"; //要求返回在线用户列表
    String text_ret_online_friend = "4"; //返回在线用户列表
    String text_exit = "5"; //客户端请求退出
    String text_com_text_all = "6"; //群发消息
    String text_heartbeat = "7"; //心跳消息
    String text_heartbeat_reply = "8"; //心跳回复
}
