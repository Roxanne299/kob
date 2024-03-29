package com.kob.backend.controller.pk;

import com.kob.backend.service.pk.ReceiveBotMoveService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReceiveBotMoveController {
    @Autowired
    ReceiveBotMoveService receiveBotMoveService;

    @PostMapping("/pk/receive/bot/move/")
    public String receiveBotMove(@RequestParam MultiValueMap<String,String> data){
        Integer userId = Integer.parseInt(data.getFirst("user_id"));
        Integer direction = Integer.parseInt(data.getFirst("direction"));
        return receiveBotMoveService.receiveBotMove(userId,direction);
    }
}
