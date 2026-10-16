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
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private CourseService courseService;

    @GetMapping("/courses")
    public List<Course> getMyCourses() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return courseService.getTeacherCourses(username);
    }
}