package com.students.jwsystem_public.service;

import com.students.jwsystem_public.entity.Course;
import com.students.jwsystem_public.entity.CourseSelection;

import java.util.List;

public interface CourseSelectionService {
    String selectCourse(Long courseId, String username);
    String dropCourse(Long courseId, String username);

    void dropCourse(Long studentId, Long courseId);

    // ：查询当前学生已选课程列表
    List<Course> getMyCourses(String username);
    List<CourseSelection> getStudentsByCourseId(Long courseId);
}