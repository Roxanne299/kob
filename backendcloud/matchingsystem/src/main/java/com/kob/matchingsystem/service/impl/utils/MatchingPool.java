package com.kob.matchingsystem.service.impl.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class MatchingPool extends  Thread{

    private static List<Player> players = new ArrayList<>();
    private ReentrantLock lock = new ReentrantLock();
    private static RestTemplate restTemplate;
    private final static String startGameUrl = "http://127.0.0.1:8081/pk/start/game/";

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate){MatchingPool.restTemplate = restTemplate;}

    public void addplayer(Integer userId,Integer botId,Integer rating){
        lock.lock();
        try{
            players.add(new Player(userId,rating,botId,0));
        }finally {
            lock.unlock();
        }
    }

    private void updateWatingTime(){
        lock.lock();
        try {
            for(Player player:players){
                player.setWatingTime(player.getWatingTime()+1);
            }
        }finally {
            lock.unlock();
        }
    }


//    等待的时间越长，所匹配的两者分数可以会越多 我们需要判断两者的分数差满足两者分别的等待时间
    private boolean checkMatched(Player a,Player b){
        int ratingDelta = Math.abs(a.getRating() - b.getRating());
        int minWatingTime = Math.min(a.getWatingTime(),b.getWatingTime());
        return ratingDelta <= minWatingTime * 10;
    }

    private void sendResult(Player a,Player b){
        MultiValueMap<String,String> data = new LinkedMultiValueMap<>();
        data.add("a_id",a.getUserId().toString());
        data.add("a_bot_id",a.getBotId().toString());
        data.add("b_id",b.getUserId().toString());
        data.add("b_bot_id",b.getBotId().toString());
        restTemplate.postForObject(startGameUrl,data,String.class);

    }
    private void matchPlayers(){
            boolean[] used = new boolean[players.size()];
//          我们加入players数组里面都是从前往后加的，所以前面的waitingTime绝对是最大的，所以我们需要从前往后来匹配
            for(int i = 0;i < players.size();i++){
                if(used[i]) continue;
                for(int j = i + 1; j < players.size();j++){
                    if(used[j]) continue;
                    Player a = players.get(i),b = players.get(j);
                    if(checkMatched(a,b)){
                        used[i] = used[j] = true;
                        sendResult(a,b);
                        break;
                    }
                }
            }
//            删除已经匹配的玩家
            List<Player> newPlayers = new ArrayList<>();
            for(int i = 0;i < players.size();i++){
                if(used[i]) continue;
                newPlayers.add(players.get(i));
            }
            players = newPlayers;

    }
    public void removePlayer(Integer userId){
        lock.lock();
        try{
            ArrayList<Player> newPlayers = new ArrayList<>();
            for(Player player:players){
                if(player.getUserId() != userId) newPlayers.add(player);
            }
            players = newPlayers;
        }finally {
            lock.unlock();
        }


    }
    @Override
    public void run() {
        while(true){
            try {
                Thread.sleep(1000);
                System.out.println("目前匹配池里面用户的数量：" + players.size());
                lock.lock();
                try{
                    updateWatingTime();
                    matchPlayers();
                }finally {
                    lock.unlock();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
        super.run();
    }
}
