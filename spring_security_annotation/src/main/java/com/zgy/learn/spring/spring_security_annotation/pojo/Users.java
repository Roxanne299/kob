package com.zgy.learn.spring.spring_security_annotation.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Users {
    Integer id;
    String username;
    String password;
}
