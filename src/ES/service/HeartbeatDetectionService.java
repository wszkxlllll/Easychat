package ES.service;

import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务端心跳检测服务，检测客户端是否存活
 */
public class HeartbeatDetectionService extends Thread {
    // 存储用户最后一次心跳时间
    private static ConcurrentHashMap<String, Long> userLastHeartbeatTime = new ConcurrentHashMap<>();
    // 心跳超时时间，单位毫秒，这里设置为30秒
    private static final long HEARTBEAT_TIMEOUT = 30000;
    private boolean running = true;

    public void setRunning(boolean running) {
        this.running = running;
    }

    /**
     * 更新用户的最后心跳时间
     * @param userId 用户ID
     */
    public static void updateHeartbeat(String userId) {
        userLastHeartbeatTime.put(userId, System.currentTimeMillis());
        System.out.println("更新用户 " + userId + " 的心跳时间");
    }

    /**
     * 用户登录时，添加其心跳记录
     * @param userId 用户ID
     */
    public static void addUserHeartbeat(String userId) {
        updateHeartbeat(userId);
    }

    /**
     * 用户登出时，移除其心跳记录
     * @param userId 用户ID
     */
    public static void removeUserHeartbeat(String userId) {
        userLastHeartbeatTime.remove(userId);
        System.out.println("移除用户 " + userId + " 的心跳记录");
    }

    @Override
    public void run() {
        while (running) {
            try {
                // 当前时间
                long currentTime = System.currentTimeMillis();

                // 遍历所有用户的心跳记录
                Iterator<String> iterator = userLastHeartbeatTime.keySet().iterator();
                while (iterator.hasNext()) {
                    String userId = iterator.next();
                    Long lastHeartbeatTime = userLastHeartbeatTime.get(userId);

                    // 检查心跳是否超时
                    if (currentTime - lastHeartbeatTime > HEARTBEAT_TIMEOUT) {
                        System.out.println("用户 " + userId + " 心跳超时，断开连接");

                        // 从心跳记录中移除
                        iterator.remove();

                        // 从线程管理中移除并关闭连接
                        ServerConnectClientThread clientThread = ManageClientThreads.get(userId);
                        if (clientThread != null) {
                            try {
                                clientThread.getSocket().close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            ManageClientThreads.remove(userId);
                        }
                    }
                }

                // 每5秒检测一次
                Thread.sleep(5000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
