package EC.service;

import Ecommon.Message;
import Ecommon.type;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;

/**
 * 客户端心跳发送线程，每10秒发送一次心跳包到服务器
 */
public class ClientHeartbeatThread extends Thread {
    private Socket socket;
    private String userId;
    private boolean loop = true;

    public ClientHeartbeatThread(Socket socket, String userId) {
        this.socket = socket;
        this.userId = userId;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    @Override
    public void run() {
        while (loop) {
            try {
                // 创建心跳消息
                Message heartbeatMsg = new Message();
                heartbeatMsg.setType(type.text_heartbeat);
                heartbeatMsg.setFrom(userId);
                heartbeatMsg.setSendtime(new Date().toString());

                // 发送心跳消息
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(heartbeatMsg);
                System.out.println("客户端 " + userId + " 发送心跳包...");

                // 每10秒发送一次心跳包
                Thread.sleep(10000);
            } catch (Exception e) {
                System.out.println("客户端心跳线程异常: " + e.getMessage());
                break;
            }
        }
    }
}
