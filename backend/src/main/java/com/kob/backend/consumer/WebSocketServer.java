package com.kob.backend.consumer;

import com.alibaba.fastjson2.JSONObject;
import com.kob.backend.consumer.utils.JwtAuthentication;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import com.kob.backend.utils.JwtUtil;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.*;
import java.util.Iterator;

@Component
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {

    private Session session = null;
    private User user;
    //使用静态变量对所有实例可见  不是静态变量那就是每一个实例有独有一份 并且完成一个线程安全的hashmap
    final private static ConcurrentHashMap<Integer,WebSocketServer> users = new ConcurrentHashMap<>();

     private static final CopyOnWriteArraySet<User> matchpool = new CopyOnWriteArraySet<>();
    //WebSocket不是spring的标准的组件，不是单例模式的所以需要设置一个独一份的静态变量 用set函数来Autowire
//    单例模式就是每个类每个事件都只有一个实例，但是我们这个类不是的，有多个实例
    private static UserMapper userMapper;

    @Autowired
    public void setUserMapper(UserMapper userMapper){
        WebSocketServer.userMapper = userMapper;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        try{
            // 建立连接
            this.session = session;
            System.out.println("websocket server on connected!");
            Integer userId = JwtAuthentication.getUserId(token);
            this.user = userMapper.selectById(userId);
            if(this.user != null)
                users.put(userId,this);
            else
                session.close();
        }catch (IOException e){
            e.printStackTrace();
        }

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
        System.out.println("server receive message....");
        JSONObject object = JSONObject.parseObject(message);
        String msg = String.valueOf(object.get("msg"));
        System.out.println(msg);
        if("matching".equals(msg)){
            startMatching();
        }
        else if("cancel".equals(msg)){

        }
    }
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    public void startMatching(){
        System.out.println("server start matching....");
        matchpool.add(this.user);

        while(matchpool.size() >= 2){
            Iterator<User> it = matchpool.iterator();
            User a = it.next(), b = it.next();
            matchpool.remove(a);
            matchpool.remove(b);
            System.out.println("matching success!" + a.getUsername() + " " + b.getUsername());

            JSONObject jsonObject_a = new JSONObject();
            jsonObject_a.put("msg","macthing");
            jsonObject_a.put("opponent_username",b.getUsername());
            jsonObject_a.put("opponent_photo",b.getPhoto());
            users.get(a.getId()).sendMessage(String.valueOf(jsonObject_a));

            JSONObject jsonObject_b = new JSONObject();
            jsonObject_b.put("msg","macthing");
            jsonObject_b.put("opponent_username",a.getUsername());
            jsonObject_b.put("opponent_photo",a.getPhoto());
            users.get(b.getId()).sendMessage(String.valueOf(jsonObject_b));
            break;
        }
    }

    public void stopMatching(){
        System.out.println("server stop matching......");
        matchpool.remove(this.user);
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
