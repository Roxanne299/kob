package com.kob.backend;

import com.kob.backend.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.SQLOutput;

@SpringBootTest
class BackendApplicationTests {

    @Test
    void contextLoads() {
        SecurityConfig securityConfig = new SecurityConfig();
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        System.out.println(passwordEncoder.encode("123"));
    }

}
