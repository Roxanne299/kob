package com.kob.backend.controller.pk;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/pk/")
public class    IndexController {

    @RequestMapping("getbotinfo/")
    public Map<String,String> getBot()
    {
        Map<String,String> map = new HashMap<String, String>();
        map.put("Bot_Name","zgy");
        map.put("Bot_age","18");
        return map;
    }
}
