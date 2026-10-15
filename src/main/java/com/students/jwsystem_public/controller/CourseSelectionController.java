package com.students.jwsystem_public.controller;

import com.students.jwsystem_public.entity.Course;
import com.students.jwsystem_public.entity.CourseSchedule;                        // ✅ 新增
import com.students.jwsystem_public.service.CourseScheduleService;                // ✅ 新增
import com.students.jwsystem_public.service.CourseSelectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/course/select")
public class CourseSelectionController {

    @Autowired
    private CourseSelectionService courseSelectionService;

    @Autowired
    private CourseScheduleService courseScheduleService;   // ✅ 新增

    /**
     * 选课接口
     */
    @PostMapping("/{courseId}")
    public Map<String, Object> select(@PathVariable Long courseId) {
        Map<String, Object> result = new HashMap<>();
        try {
            String username = getCurrentUsername();
            String msg = courseSelectionService.selectCourse(courseId, username);
            result.put("success", true);
            result.put("message", msg);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 退课接口
     */
    @PostMapping("/drop/{courseId}")
    public Map<String, Object> drop(@PathVariable Long courseId) {
        Map<String, Object> result = new HashMap<>();
        try {
            String username = getCurrentUsername();
            String msg = courseSelectionService.dropCourse(courseId, username);
            result.put("success", true);
            result.put("message", msg);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 获取当前学生已选课程列表（"我选的课"Tab）
     */
    @GetMapping("/my")
    public List<Course> getMyCourses() {
        String username = getCurrentUsername();
        return courseSelectionService.getMyCourses(username);
    }

    // ✅ 新增：获取当前学生的课表
    @GetMapping("/schedule")
    public List<CourseSchedule> getSchedule() {
        String username = getCurrentUsername();
        return courseScheduleService.getMySchedule(username);
    }

    private String getCurrentUsername() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            throw new RuntimeException("用户未登录，请先登录");
        }
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}