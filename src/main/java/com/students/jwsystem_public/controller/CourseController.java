package com.students.jwsystem_public.controller;

import com.students.jwsystem_public.entity.Course;
import com.students.jwsystem_public.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping("/list")
    public List<Course> getAllCourses() {
        // 从 SecurityContext 获取当前登录用户名
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        // 修改：把 username 传给 Service（原来是无参调用）
        return courseService.getAllCourses(username);
    }
}