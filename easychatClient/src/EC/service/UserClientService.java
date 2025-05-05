package EC.service;

import Ecommon.Message;
import Ecommon.User;
import Ecommon.type;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/*
 * 完成用户登录验证和用户注册功能
 */
public class UserClientService {
    private User u = new User();
    private Socket socket;
    private ClientHeartbeatThread heartbeatThread;
    public boolean checkUser(String userId, String passwd) {
        boolean flag = false;
        u.setUserId(userId);
        u.setPasswd(passwd);

        //连接到服务端，发送对象
        try {
            socket = new Socket(InetAddress.getByName("127.0.0.1"),9999);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(u);
            
            //读取服务器会送的Message对象
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            try {
                Message ms = (Message) in.readObject();
                //登录成功
                if(ms.getType().equals(type.text_login_succeed)) {
                    flag = true;
                    //创建和客户端保持连接的线程
                    //现在是单线程，如果需要扩展可以用hashmap管理多个线程
                    ClientConnectServerThread clientConnectServerThread = new ClientConnectServerThread(socket);
                    clientConnectServerThread.start();
                    //创建心跳线程并启动
                    heartbeatThread = new ClientHeartbeatThread(socket, userId);
                    heartbeatThread.start();
                    ManageClientConnectServer.addThread(userId, clientConnectServerThread);
                } else {
                    socket.close();
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return flag;
    }

    public void onlineFriendList(){
        Message ms = new Message();
        ms.setType(type.text_get_online_friend);
        ms.setFrom(u.getUserId());
        ms.setTo("Server");
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(ManageClientConnectServer.getThread(u.getUserId()).getSocket().getOutputStream());
            objectOutputStream.writeObject(ms);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public void logout(){
        Message ms = new Message();
        ms.setType(type.text_exit);
        ms.setFrom(u.getUserId());

        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(ManageClientConnectServer.getThread(u.getUserId()).getSocket().getOutputStream());
            objectOutputStream.writeObject(ms);
            System.out.println(u.getUserId() + "退出系统");
            if(heartbeatThread != null) {
                heartbeatThread.setLoop(false);
            }
            System.exit(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
