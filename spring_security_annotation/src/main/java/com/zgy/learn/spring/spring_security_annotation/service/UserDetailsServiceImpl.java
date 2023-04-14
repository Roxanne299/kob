package com.zgy.learn.spring.spring_security_annotation.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zgy.learn.spring.spring_security_annotation.mapper.UserMapper;
import com.zgy.learn.spring.spring_security_annotation.pojo.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserMapper userMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        Users users = userMapper.selectOne(queryWrapper);
        if(users != null){
            List<GrantedAuthority> authentication = AuthorityUtils.commaSeparatedStringToAuthorityList("admins");
            return new User(username,new BCryptPasswordEncoder().encode(users.getPassword()),authentication);

        }
        else {
            throw new UsernameNotFoundException("没有该用户");
        }
    }
}
