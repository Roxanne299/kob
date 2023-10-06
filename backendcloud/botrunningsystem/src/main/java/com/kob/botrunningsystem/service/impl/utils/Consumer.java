package com.kob.botrunningsystem.service.impl.utils;

import com.kob.botrunningsystem.utils.BotInterface;
import org.joor.Reflect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Component
public class Consumer extends Thread{
    private Bot bot;
    private static RestTemplate restTemplate;
    private final static String receiveBotMoveUrl = "http://127.0.0.1:8081/pk/receive/bot/move/";
    @Autowired
    private void setRestTemplate(RestTemplate restTemplate){Consumer.restTemplate = restTemplate;}
    public void startTimeout(long timeout,Bot bot){
        this.bot = bot;
        //开一个新的线程
        this.start();

        try {
            //当我们线程执行完或者timeout到点了就会不阻塞
            this.join(timeout);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            //最多等待timeout中断当前进程
            this.interrupt();
        }
    }

    private String addUid(String code,String uId){ //在bot类名后面加上uid
        int k = code.indexOf(" implements BotInterface");
        return code.substring(0,k) + uId + code.substring(k);
    }
    @Override
    public void run() {
        UUID uuid = UUID.randomUUID();
        String uId = uuid.toString().substring(0,8);

//        动态编译代码 注意里面如果类重名了那么只能编译一次 所以我们需要加上uuid来区分我们类名
        BotInterface botInterface = Reflect.compile(
                "com.kob.botrunningsystem.utils.Bot"+uId,
                addUid(bot.getBotCode(),uId)
               ).create().get();

        Integer direction = botInterface.nextMove(bot.getInput());
        MultiValueMap<String,String> data = new LinkedMultiValueMap<>();
        data.add("user_id",bot.getUserId().toString());
        data.add("direction",direction.toString());
        restTemplate.postForObject(receiveBotMoveUrl,data,String.class);

        System.out.println("move: " + bot.getUserId() + " " + direction);
    }
}
