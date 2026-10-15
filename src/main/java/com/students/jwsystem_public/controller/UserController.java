package com.students.jwsystem_public.controller;

import com.students.jwsystem_public.entity.User;
import com.students.jwsystem_public.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")

public class UserController {
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/info")
    public Map<String,Object> getCurrentUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userMapper.findByUsername(username);

        Map<String,Object> result = new HashMap<>();
        result.put("username", user.getUsername());
        result.put("realName", user.getRealName());
        result.put("role", user.getRole());
        return result;

    }

}