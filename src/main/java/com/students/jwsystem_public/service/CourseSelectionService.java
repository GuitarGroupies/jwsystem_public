package com.students.jwsystem_public.service;

import com.students.jwsystem_public.entity.Course;
import com.students.jwsystem_public.entity.CourseSelection;
import java.util.List;

public interface CourseSelectionService {
    String selectCourse(Long courseId, String username);
    String dropCourse(Long courseId, String username);
    void dropCourse(Long studentId, Long courseId);
    List<Course> getMyCourses(String username);
    List<CourseSelection> getStudentsByCourseId(Long courseId);
    List<CourseSelection> getAllStudentsByTeacher(String username);

    // ✅ 替换为这个：将学生从老师所有的课中移出
    void removeStudentFromTeacherCourses(Long studentId, String teacherUsername);
}