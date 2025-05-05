package ES.service;

import java.util.HashMap;
import java.util.Iterator;

/*
 *管理和客户端通信的线程
 */
public class ManageClientThreads {
    private static HashMap<String, ServerConnectClientThread> hm = new HashMap<>();
    public static void add(String userId, ServerConnectClientThread serverConnectClientThread) {
        hm.put(userId, serverConnectClientThread);
    }
    public static ServerConnectClientThread get(String userId) {
        return hm.get(userId);
    }
    public static void remove(String userId) {
        hm.remove(userId);
    }

    public static HashMap<String, ServerConnectClientThread> getHm() {
        return hm;
    }

    //返回在线用户列表
    public static String getOnlineUsers() {
        Iterator<String> iterator = hm.keySet().iterator();
        String onlineUserList = "";
        while (iterator.hasNext()) {
            onlineUserList += iterator.next().toString() + " ";
        }
        return onlineUserList;
    }
}
