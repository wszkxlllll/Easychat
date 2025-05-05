package EC.service;

import Ecommon.Message;
import Ecommon.type;

import java.io.ObjectInputStream;
import java.net.Socket;

public class ClientConnectServerThread extends Thread {
     private Socket socket;
     public ClientConnectServerThread(Socket socket) {
         this.socket = socket;
     }
     public void run() {
         while(true) {
             System.out.println("客户端线程等待服务器端发送的消息");
             try {
                 ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                 //若无Message对象，线程会阻塞，考虑心跳机制检查
                 Message ms = (Message)objectInputStream.readObject();
                 if (ms.getType().equals(type.text_heartbeat_reply)) {
                     System.out.println("收到服务器的心跳回复");
                     continue;
                 }
                 if(ms.getType().equals(type.text_ret_online_friend)){
                     String[] onlineUsers = ms.getText().split(" ");
                     System.out.println("\t\t在线用户列表");
                     for (String onlineUser : onlineUsers) {
                         System.out.println("用户：" + onlineUser);
                     }
                 } else if(ms.getType().equals(type.text_com_tex)){
                     System.out.println("在" + ms.getSendtime()+ms.getFrom() + "对" + ms.getTo() + "说" + ms.getText());
                 } else if (ms.getType().equals(type.text_com_text_all)) {
                     System.out.println("在" + ms.getSendtime()+ms.getFrom() + "对所有人说" + ms.getText());
                 }
             } catch (Exception e) {
                 throw new RuntimeException(e);
             }

         }
     }
     public Socket getSocket() {
         return socket;
     }
}
