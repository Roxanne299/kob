package com.zgy.learn.spring.spring_security_annotation.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    public UserDetailsService userDetailsServiceImpl;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceImpl).passwordEncoder(password());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()    //自定义自己的登录页面
                .loginPage("/login.html")   //登录页面设置
                .loginProcessingUrl("/login")  //代替系统默认的登录访问路径
                .permitAll()  //登录成功之后的跳转路径
                .and().authorizeRequests()  //设置允许的访问路径
                .antMatchers("/","/login").permitAll()  //访问里面这些路径可以直接访问不需要认证
//                .antMatchers("/test").hasRole("admins")
                .anyRequest().authenticated()   //其他的所有都需要认证
                .and().csrf().disable();    //关闭crsf防护
    }

    @Bean
    public PasswordEncoder password(){
        return new BCryptPasswordEncoder();
    }
}
