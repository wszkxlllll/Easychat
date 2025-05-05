package EC.service;

import Ecommon.Message;
import Ecommon.type;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;

/*
 *和消息相关的服务方法
 */
public class MessageClientService {
    public void sendToALl(String text,String FromId){
        Message message = new Message();
        message.setFrom(FromId);
        message.setText(text);
        message.setSendtime(new Date().toString());
        message.setType(type.text_com_text_all);
        System.out.println(FromId + "对所有人说" + text);
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(ManageClientConnectServer.getThread(FromId).getSocket().getOutputStream());
            objectOutputStream.writeObject(message);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(String text, String FromId, String Toid) {
        Message message = new Message();
        message.setFrom(FromId);
        message.setTo(Toid);
        message.setText(text);
        message.setSendtime(new Date().toString());
        message.setType(type.text_com_tex);
        System.out.println(FromId + "对" + Toid + "说" + text);
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(ManageClientConnectServer.getThread(FromId).getSocket().getOutputStream());
            objectOutputStream.writeObject(message);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
