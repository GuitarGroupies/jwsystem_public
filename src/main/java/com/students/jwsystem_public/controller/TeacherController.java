package com.students.jwsystem_public.controller;

import com.students.jwsystem_public.entity.Course;
import com.students.jwsystem_public.entity.CourseSchedule;
import com.students.jwsystem_public.entity.CourseSelection;
import com.students.jwsystem_public.service.CourseScheduleService;
import com.students.jwsystem_public.service.CourseSelectionService;
import com.students.jwsystem_public.service.CourseService;
import com.students.jwsystem_public.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired private CourseService courseService;
    @Autowired private CourseScheduleService courseScheduleService;
    @Autowired private CourseSelectionService courseSelectionService;
    @Autowired private ScoreService scoreService;

    @GetMapping("/courses")
    public List<Course> getMyCourses() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return courseService.getTeacherCourses(username);
    }

    @GetMapping("/schedule")
    public List<CourseSchedule> getSchedule() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return courseScheduleService.getTeacherSchedule(username);
    }

    @GetMapping("/students")
    public List<CourseSelection> getStudents(@RequestParam("courseId") Long courseId) {
        return courseSelectionService.getStudentsByCourseId(courseId);
    }

    @GetMapping("/all-students")
    public List<CourseSelection> getAllStudents() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return courseSelectionService.getAllStudentsByTeacher(username);
    }

    @PostMapping("/grade")
    public Map<String, Object> saveGrade(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            Long studentId = Long.valueOf(params.get("studentId").toString());
            Long courseId = Long.valueOf(params.get("courseId").toString());
            Double scoreValue = Double.valueOf(params.get("score").toString());

            boolean success = scoreService.saveOrUpdateScore(studentId, courseId, scoreValue);
            result.put("success", success);
            result.put("message", success ? "成绩保存成功" : "保存失败");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    // ✅ 修改：调用移出逻辑，绝不删账号
    @PostMapping("/delete-student/{id}")
    @Transactional
    public Map<String, Object> deleteStudent(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            String teacherUsername = SecurityContextHolder.getContext().getAuthentication().getName();
            // 调用 Service 执行移出逻辑
            courseSelectionService.removeStudentFromTeacherCourses(id, teacherUsername);

            result.put("success", true);
            result.put("message", "已成功将该学生移出您的课程");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "操作失败: " + e.getMessage());
        }
        return result;
    }
}