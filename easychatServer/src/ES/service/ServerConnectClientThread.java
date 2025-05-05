package ES.service;

import Ecommon.Message;
import Ecommon.type;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

public class ServerConnectClientThread extends Thread {
    private Socket socket;
    private String userId;
    public ServerConnectClientThread(Socket socket, String userId) {
        this.socket = socket;
        this.userId = userId;
        HeartbeatDetectionService.addUserHeartbeat(userId);
    }

    public Socket getSocket() {
        return socket;
    }

    public void run() {
        while(true){
            System.out.println("服务器和客户端" +userId+"保持通讯，读取数据");
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                Message ms = (Message) objectInputStream.readObject();
                if (ms.getType().equals(type.text_heartbeat)) {
                    System.out.println("收到用户 " + userId + " 的心跳包");
                    // 更新用户的最后心跳时间
                    HeartbeatDetectionService.updateHeartbeat(userId);
                    Message heartbeatReply = new Message();
                    heartbeatReply.setType(type.text_heartbeat_reply);
                    heartbeatReply.setFrom("Server");
                    heartbeatReply.setTo(userId);
                    heartbeatReply.setSendtime(new Date().toString());

                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(heartbeatReply);
                }else if (ms.getType().equals(type.text_get_online_friend)) {
                    System.out.println("用户"+userId+"要得到在线用户列表");
                    String onlineUsers = ManageClientThreads.getOnlineUsers() ;
                    Message message2 = new Message();
                    message2.setType(type.text_ret_online_friend);
                    message2.setText(onlineUsers);
                    message2.setFrom(ms.getTo());
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    objectOutputStream.writeObject(message2);

                } else if(ms.getType().equals(type.text_exit)){
                    System.out.println(ms.getFrom() + "退出系统");
                    sleep(1);
                    ManageClientThreads.remove(userId);
                    socket.close();
                    break;
                } else if (ms.getType().equals(type.text_com_tex)) {
                    //获取toid对应的线程
                    ServerConnectClientThread serverConnectClientThread = ManageClientThreads.get(ms.getTo());
                    //获取其socket并转发
                    System.out.println("在" + ms.getSendtime()+ms.getFrom() + "对" + ms.getTo() + "说" + ms.getText());
                    new ObjectOutputStream(serverConnectClientThread.getSocket().getOutputStream()).writeObject(ms);
                } else if (ms.getType().equals(type.text_com_text_all)){
                    HashMap<String, ServerConnectClientThread> hm = ManageClientThreads.getHm();
                    Iterator<String> iterator = hm.keySet().iterator();
                    while (iterator.hasNext()) {
                        String onlineUserId = iterator.next().toString();
                        if(!onlineUserId.equals(ms.getFrom())){ //除自己之外的所有用户
                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(hm.get(onlineUserId).getSocket().getOutputStream());
                            objectOutputStream.writeObject(ms);
                        }
                    }
                }
            }catch (Exception e) {
                System.out.println("与客户端 " + userId + " 通信异常: " + e.getMessage());
                // 发生异常时，移除用户的心跳记录
                HeartbeatDetectionService.removeUserHeartbeat(userId);
                ManageClientThreads.remove(userId);
                try {
                    socket.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;
            }
        }
    }
}
