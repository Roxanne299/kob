package com.kob.backend.controller.user.account;

import com.kob.backend.service.user.account.RegisterService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RestController
public class RegisterController {
    @Autowired
    RegisterService registerService;

    @PostMapping("/user/account/register/")
    public Map<String,String> register(@RequestParam Map<String,String> map){
        String username = map.get("username");
        String password = map.get("password");
        String confirmPassword = map.get("confirmPassword");
        String imgUrl = map.get("imgUrl");
        return registerService.register(username,password,confirmPassword,imgUrl);

    }

}
