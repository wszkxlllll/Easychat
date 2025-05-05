package ES.service;

import Ecommon.Message;
import Ecommon.User;
import Ecommon.type;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class easychatServer {
    private ServerSocket serverSocket = null;
    private HeartbeatDetectionService heartbeatDetectionService;
    //可以处理并发的集合，没有线程安全
    private static ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();
    static {
        users.put("jokersun", new User("jokersun", "123456"));
        users.put("LYR", new User("LYR", "123456"));
        users.put("GLY", new User("GLY", "123456"));
        users.put("ZXL", new User("ZXL", "123456"));
        users.put("MGX", new User("MGX", "123456"));
    }
    private boolean checkUser(String userId, String passwd) {
        User user = users.get(userId);
        if (user == null) {
            return false;
        } else if (!passwd.equals(user.getPasswd())) {
            return false;
        } else {
            return true;
        }
    }
    public easychatServer(){
        System.out.println("服务端在9999端口监听");
        try {
            serverSocket = new ServerSocket(9999);
            // 启动心跳检测服务
            heartbeatDetectionService = new HeartbeatDetectionService();
            heartbeatDetectionService.start();
            System.out.println("心跳检测服务已启动");
            while (true) {
                Socket socket = serverSocket.accept();
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                User user = (User) objectInputStream.readObject();
                Message message = new Message();
                //验证
                if(checkUser(user.getUserId(), user.getPasswd())){
                    message.setType(type.text_login_succeed);
                    objectOutputStream.writeObject(message);
                    ServerConnectClientThread serverConnectClientThread = new ServerConnectClientThread(socket, user.getUserId());
                    serverConnectClientThread.start();
                    ManageClientThreads.add(user.getUserId(), serverConnectClientThread);
                } else {
                    message.setType(type.text_login_fail);
                    objectOutputStream.writeObject(message);
                    socket.close();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                serverSocket.close();
                if(heartbeatDetectionService != null){
                    heartbeatDetectionService.setRunning(false);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) {
        easychatServer server = new easychatServer();
    }
}
