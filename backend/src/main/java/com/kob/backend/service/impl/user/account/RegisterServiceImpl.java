package com.kob.backend.service.impl.user.account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import com.kob.backend.service.user.account.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public Map<String, String> register(String username, String password, String confirmPassword) {
        Map<String,String> map = new HashMap<>();
        if(username == null){
            map.put("error_message","用户名不能为空");
            return map;
        }
        username = username.trim();
        if(username.length() == 0){
            map.put("error_message","用户名不能为空");
            return map;
        }
        if(password == null || confirmPassword == null){
            map.put("error_message","密码不能为空");
            return map;
        }
        if(password.length() > 100){
            map.put("error_message","密码长度不能大于100");
            return map;
        }
        if(username.length() > 100){
            map.put("error_message","用户名长度不能大于100");
            return map;
        }
        if(!password.equals(confirmPassword)){
            map.put("error_message","两次输入密码不一致");
            return map;
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        List<User> list = userMapper.selectList(queryWrapper);

        if(!list.isEmpty()){
            map.put("error_message","用户名已存在");
            return map;
        }
        //因为id是自增的，所以传一个null就好了
        userMapper.insert(new User(null,username,passwordEncoder.encode(password),"https://cdn.acwing.com/media/user/profile/photo/80738_lg_05b87e238e.jpeg"));
        map.put("error_message","success");

        return map;
    }
}
