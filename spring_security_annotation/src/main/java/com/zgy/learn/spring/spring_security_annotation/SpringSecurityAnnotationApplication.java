package com.zgy.learn.spring.spring_security_annotation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SpringSecurityAnnotationApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityAnnotationApplication.class, args);
    }

}
