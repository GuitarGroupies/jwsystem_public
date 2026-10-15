package com.students.jwsystem_public.service;

import com.students.jwsystem_public.entity.Course;   // ✅ 新增
import java.util.List;                                // ✅ 新增

public interface CourseSelectionService {
    String selectCourse(Long courseId, String username);
    String dropCourse(Long courseId, String username);

    void dropCourse(Long studentId, Long courseId);

    // ✅ 新增：查询当前学生已选课程列表
    List<Course> getMyCourses(String username);
}