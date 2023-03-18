package com.kob.backend.service.impl.user.bot;

import com.kob.backend.mapper.BotMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import com.kob.backend.service.user.bot.AddService;
import com.kob.backend.utils.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AddServiceImpl implements AddService {

    @Autowired
    BotMapper botMapper;
    @Override
    public Map<String, String> add(Map<String, String> data) {
        //用户登录成功之后可以从上下文获取
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();

        User user = loginUser.getUser();
        String title = data.get("title");
        String descrption = data.get("description");
        String content = data.get("content");

        Map<String,String> map = new HashMap<>();

        if(title == null || title.length() == 0){
            map.put("error_message","标题长度不能为空");
            return map;
        }
        if(title.length() > 100){
            map.put("error_message","标题长度不能超100");
            return map;
        }


        if(descrption == null || descrption.length() == 0){
            descrption = "这个用户咩有写描述";
        }
        if(descrption.length() > 300){
            map.put("error_message","Bot描述不能超300");
            return map;
        }

        if(content == null || content.length() == 0){
            map.put("error_message","代码不能为空");
            return map;
        }
        if(content.length() > 10000){
            map.put("error_message","代码长度不能超10000");
            return map;
        }

        Date date = new Date();
        Bot bot = new Bot(null, user.getId(),title,descrption,content,1500,date,date);
        botMapper.insert(bot);
        map.put("error_message","success");
        return map;
    }
}
