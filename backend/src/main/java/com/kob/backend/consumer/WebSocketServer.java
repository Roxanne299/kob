package com.kob.backend.consumer;

import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {

    private Session session = null;
    private User user;
    //使用静态变量对所有实例可见  不是静态变量那就是每一个实例有独有一份 并且完成一个线程安全的hashmap
    private static ConcurrentHashMap<Integer,WebSocketServer> users = new ConcurrentHashMap<>();
    //WebSocket不是单例模式的所以需要设置一个独一份的静态变量 用set函数来Autowire
    private static UserMapper userMapper;

    @Autowired
    public void setUserMapper(UserMapper userMapper){
        WebSocketServer.userMapper = userMapper;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        // 建立连接
        this.session = session;
        System.out.println("websocket on connected!");
        Integer userId = Integer.parseInt(token);
        this.user = userMapper.selectById(userId);
        users.put(userId,this);
        sendMessage("你好");
    }
    @OnClose
    public void onClose() {
        // 关闭链接
        System.out.println("websocket on closed!");
        if(this.user!=null){
            users.remove(this.user.getId());
        }
    }
    @OnMessage
    public void onMessage(String message, Session session) {
        // 从Client接收消息
    }
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    public void sendMessage(String message){
        //实现向前端发送信息
        synchronized (this.session){
            try {
                this.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
