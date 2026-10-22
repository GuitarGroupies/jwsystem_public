package com.students.jwsystem_public.entity;

import lombok.Data;

// 用户表
@Data
public class User {
    private Long id;
    private String username;
    private String password;
    private String realName;
    private String role;

    private String className;
}