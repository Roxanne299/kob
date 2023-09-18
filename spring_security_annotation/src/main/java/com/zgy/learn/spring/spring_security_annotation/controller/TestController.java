package com.zgy.learn.spring.spring_security_annotation.controller;


import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {
    @RequestMapping("/test")
    @Secured("ROLE_admins1")
    public  String test(){
        return "test.html";
    }
}
