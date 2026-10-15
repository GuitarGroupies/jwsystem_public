package com.students.jwsystem_public.service;

import com.students.jwsystem_public.entity.Course;
import java.util.List;

public interface CourseService {
    public List<Course> getAllCourses();// ← 加 public
    Course getCourseById(Long id);
    List<Course> getAllCourses(String username);
}