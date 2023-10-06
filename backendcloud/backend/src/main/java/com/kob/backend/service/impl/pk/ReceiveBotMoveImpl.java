package com.kob.backend.service.impl.pk;

import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.consumer.utils.GameMap;
import com.kob.backend.service.pk.ReceiveBotMoveService;
import org.springframework.stereotype.Service;

@Service
public class ReceiveBotMoveImpl implements ReceiveBotMoveService {
    @Override
    public String receiveBotMove(Integer userId, Integer direction) {
        System.out.println("receive bot move: " + userId+ " " + direction);
        if(WebSocketServer.users.get(userId) != null){
            GameMap game = WebSocketServer.users.get(userId).game;
            if(game.getPlayerA().getId() == userId){
                // 这个判断就是为了屏蔽前端操作的
                if(game.getPlayerA().getBotId() != -1)
                    game.setNextStepA(direction);
            }else if(game.getPlayerB().getId() == userId){
                if(game.getPlayerB().getBotId() != -1)
                    game.setNextStepB(direction);
            }
        }

        return "receive bot move success";
    }
}
