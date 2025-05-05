package EC.service;

import java.util.HashMap;

/*
 *管理一个客户端的多个线程
 */
public class ManageClientConnectServer {
    public static HashMap<String, ClientConnectServerThread> hm = new HashMap<>();
    public static void addThread(String userId, ClientConnectServerThread thread) {
        hm.put(userId, thread);
    }
    public static ClientConnectServerThread getThread(String userId) {
        return hm.get(userId);
    }

}
