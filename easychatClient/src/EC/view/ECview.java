package EC.view;

import EC.service.ManageClientConnectServer;
import EC.service.MessageClientService;
import EC.service.UserClientService;
import EC.utils.Utility;

public class ECview {
    private boolean loop = true;
    private String key;
    private UserClientService userClientService = new UserClientService(); //用于登录服务器
    private MessageClientService messageClientService = new MessageClientService();
    private void mainMenu(){
        while (loop) {
            System.out.println("\t\t 1 登录系统");
            System.out.println("\t\t 0 退出系统");
            System.out.println("请输入你的选择");
            key = Utility.readString(1);

            switch (key){
                case "1":
                    System.out.println("请输入userid");
                    String userid = Utility.readString(20);
                    System.out.println("请输入密码");
                    String passwd = Utility.readString(20);
                    if(userClientService.checkUser(userid,passwd)){
                        System.out.println(userid + "登录成功");
                        while(loop){
                            System.out.println("\t\t 欢迎进入二级菜单(用户 " + userid + " )");
                            System.out.println("\t\t 1 显示在线用户列表");
                            System.out.println("\t\t 2 群发消息");
                            System.out.println("\t\t 3 私发消息");
                            System.out.println("\t\t 0 退出系统");
                            key = Utility.readString(1);
                            switch (key){
                                case "1":
                                    userClientService.onlineFriendList();
                                    break;
                                case "2":
                                    System.out.println("请输入想对大家说的话：");
                                    String s = Utility.readString(100);
                                    messageClientService.sendToALl(s, userid);
                                    break;
                                case "3":
                                    System.out.print("请输入想聊天的用户名：");
                                    String Toid = Utility.readString(20);
                                    System.out.print("请输入想说的话：");
                                    String text = Utility.readString(100);
                                    //将消息发送给服务器端
                                    messageClientService.send(text,userid,Toid);
                                    break;
                                case "0":
                                    userClientService.logout();
                                    loop = false;
                                    break;
                            }
                        }
                    } else {
                        System.out.println("登录失败");
                        loop = false;
                    }
                    break;
                case "0":
                    loop = false;
                    break;
            }
        }
    }

    public static void main(String[] args) {
        new ECview().mainMenu();
        System.out.println("已退出系统");
    }
}
