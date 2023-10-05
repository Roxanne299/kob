package com.kob.backend.consumer;

import com.alibaba.fastjson2.JSONObject;
import com.kob.backend.consumer.utils.GameMap;
import com.kob.backend.consumer.utils.JwtAuthentication;
import com.kob.backend.mapper.RecordMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.*;

@Component
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {

    private Session session = null;
    private User user;
    //使用静态变量对所有实例可见  不是静态变量那就是每一个实例有独有一份 并且完成一个线程安全的hashmap
    final public static ConcurrentHashMap<Integer,WebSocketServer> users = new ConcurrentHashMap<>();

    private static RestTemplate restTemplate;
    private final static String addPlayerUrl = "http://127.0.0.1:8082/player/add/";
    private final static String removePlayerUrl = "http://127.0.0.1:8082/player/remove/";
    public static RecordMapper recordMapper;
    //WebSocket不是spring的标准的组件，不是单例模式的所以需要设置一个独一份的静态变量 用set函数来Autowire
//    单例模式就是每个类每个事件都只有一个实例，但是我们这个类不是的，有多个实例
    private static UserMapper userMapper;
    private GameMap game = null;

    @Autowired
    public void setUserMapper(UserMapper userMapper){
        WebSocketServer.userMapper = userMapper;
    }
    @Autowired
    public void setRecordMapper(RecordMapper recordMapper) {
        WebSocketServer.recordMapper = recordMapper;
    }
    @Autowired
    public void setRestTemplate(RestTemplate restTemplate){WebSocketServer.restTemplate = restTemplate;}


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
        else if("cancel_matching".equals(msg)){
            stopMatching();
        }else if("move".equals(msg)){
            System.out.println(object);
            move(object.getInteger("direction"));
        }
    }
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }
    public  static void startGame(Integer aId, Integer bId){
        User a = userMapper.selectById(aId),b = userMapper.selectById(bId);
        GameMap gameMap = new GameMap(14,13,30,a.getId(),b.getId());
        gameMap.createGameMap();
        gameMap.start();
        if(users.get(a.getId()) != null)
            users.get(a.getId()).game = gameMap;
        if(users.get(b.getId()) != null)
            users.get(b.getId()).game = gameMap;
        int[][] g = gameMap.getGameMap();
        System.out.println("matching success!" + a.getUsername() + " " + b.getUsername());

        JSONObject respGame = new JSONObject();
        respGame.put("a_id", gameMap.getPlayerA().getId());
        respGame.put("a_sx", gameMap.getPlayerA().getSx());
        respGame.put("a_sy", gameMap.getPlayerA().getSy());
        respGame.put("b_id", gameMap.getPlayerB().getId());
        respGame.put("b_sx", gameMap.getPlayerB().getSx());
        respGame.put("b_sy", gameMap.getPlayerB().getSy());
        respGame.put("map", gameMap.getGameMap());

        JSONObject jsonObject_a = new JSONObject();
        jsonObject_a.put("msg","macthing");
        jsonObject_a.put("opponent_username",b.getUsername());
        jsonObject_a.put("opponent_photo",b.getPhoto());
        jsonObject_a.put("game_map",respGame);
        if(users.get(a.getId()) != null)
            users.get(a.getId()).sendMessage(String.valueOf(jsonObject_a));

        JSONObject jsonObject_b = new JSONObject();
        jsonObject_b.put("msg","macthing");
        jsonObject_b.put("opponent_username",a.getUsername());
        jsonObject_b.put("opponent_photo",a.getPhoto());
        jsonObject_b.put("game_map",respGame);
        if(users.get(b.getId()) != null)
            users.get(b.getId()).sendMessage(String.valueOf(jsonObject_b));
    }
    public void startMatching(){
        System.out.println("server start matching....");
        MultiValueMap<String,String> data = new LinkedMultiValueMap<>();
        data.add("user_id",user.getId().toString());
        data.add("rating",user.getRating().toString());
//        向matchingsystem通信
        restTemplate.postForObject(addPlayerUrl,data,String.class);

    }

    public void move(int direction){
        if(game.getPlayerA().getId() == user.getId()){
            game.setNextStepA(direction);
        }else if(game.getPlayerB().getId() == user.getId()){
            game.setNextStepB(direction);
        }
    }
    public void stopMatching(){
        System.out.println("server stop matching......");
        MultiValueMap<String,String> data = new LinkedMultiValueMap<>();
        data.add("user_id",user.getId().toString());
//        向matchingsystem通信
        restTemplate.postForObject(removePlayerUrl,data,String.class);
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
