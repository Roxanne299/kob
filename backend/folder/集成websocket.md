# 集成websocket

* 集成`WebSocket`：在`pom.xml`加入依赖

  * `spring-boot-starter-websocket`：2.7.2
  * `fastjson2`：2.0.11

* 添加`config.WebSocketConfig`配置类：

  ```java
  import org.springframework.context.annotation.Bean;
  import org.springframework.context.annotation.Configuration;
  import org.springframework.web.socket.server.standard.ServerEndpointExporter;
  @Configuration
  public class WebSocketConfig {
      @Bean
      public ServerEndpointExporter serverEndpointExporter() {
  
          return new ServerEndpointExporter();
      }
  }
  ```

* 添加`consumer.WebSocketServer`类:

  ```java
  import org.springframework.stereotype.Component;
  import javax.websocket.*;
  import javax.websocket.server.PathParam;
  import javax.websocket.server.ServerEndpoint;
  
  @Component
  @ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾 类似controller里面的mapping 映射链接 
  public class WebSocketServer {
      @OnOpen
      public void onOpen(Session session, @PathParam("token") String token) {
          // 建立连接 调用该函数
      }
      @OnClose
      public void onClose() {
          // 关闭链接 调用该函数
      }
      @OnMessage
      public void onMessage(String message, Session session) {
          // 从Client接收消息 
      }
      @OnError
      public void onError(Session session, Throwable error) {
          error.printStackTrace();
      }
  }
  
  
  ```

* 配置`config.SecurityConfig`

  ```java
  @Override
  public void configure(WebSecurity web) throws Exception {
      web.ignoring().antMatchers("/websocket/**");
  }
  ```

---

**websocket的使用**

前端页面使用：

```vue
<template>
  <PlayGround>PK</PlayGround>
</template>

<script>
import PlayGround from "@/components/PlayGround.vue";
import { onMounted,onUnmounted} from "vue";
import { useStore } from "vuex";
export default {
  name: "PkIndexView",
  components: {
    PlayGround,
  },
  setup(){
    const store = new useStore();
    const socketUrl = `ws://127.0.0.1:8081/websocket/${store.state.user.id}/`;
    let socket = null;

    //当组件被加载之后才建立连接
    onMounted(()=>{
      //新建一个websocket连接
      socket = new WebSocket(socketUrl);
      
      //和后端一样对应的函数
      socket.onopen = ()=>{
          console.log("connected!");
      };
      socket.onmessage = msg =>{
        //spring框架返回的消息定义在msg.data里面
        console.log(msg.data);
      };
      socket.onclose = ()=>{
        console.log("closed");
      };
    });

    //当组件关闭之后就关掉连接
    onUnmounted(()=>{
      socket.close();
    });

  }
};
</script>

<style scoped>
</style>
```

后端页面使用

```java
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
        System.out.println("on connected!");
        Integer userId = Integer.parseInt(token);
        this.user = userMapper.selectById(userId);
        users.put(userId,this);
        sendMessage("你好");
    }
    @OnClose
    public void onClose() {
        // 关闭链接
        System.out.println("on closed!");
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

```

---

**配置`jwt`验证**

因为如果仅仅按照以上来操作的话，前端用户可以改变参数，使用其他用户身份进行对战，这样是不安全的。
