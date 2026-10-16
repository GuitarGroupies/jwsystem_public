package com.students.jwsystem_public.controller;

import com.students.jwsystem_public.entity.Course;
import com.students.jwsystem_public.entity.Score;
import com.students.jwsystem_public.entity.User;
import com.students.jwsystem_public.mapper.UserMapper;
import com.students.jwsystem_public.service.CourseSelectionService;
import com.students.jwsystem_public.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    // 注入这两个 Service 用于统计
    @Autowired
    private CourseSelectionService courseSelectionService;

    @Autowired
    private ScoreService scoreService;

    @GetMapping("/info")
    public Map<String, Object> getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userMapper.findByUsername(username);

        Map<String, Object> result = new HashMap<>();
        result.put("username", user.getUsername());
        result.put("realName", user.getRealName());
        result.put("role", user.getRole());
        return result;
    }

    // 获取完整个人信息（含学习统计）
    @GetMapping("/profile")
    public Map<String, Object> getProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userMapper.findByUsername(username);

        Map<String, Object> result = new HashMap<>();
        result.put("username", user.getUsername());
        result.put("realName", user.getRealName());
        result.put("role", user.getRole());

        // ===== 统计：已选课程数 + 总学分 =====
        List<Course> myCourses = courseSelectionService.getMyCourses(username);
        result.put("courseCount", myCourses.size());

        int totalCredit = 0;
        for (Course c : myCourses) {
            totalCredit += (c.getCredit() == null ? 0 : c.getCredit());
        }
        result.put("totalCredit", totalCredit);

        // ===== 统计：及格课程数 + 平均分 =====
        List<Score> myScores = scoreService.getMyScores(username);
        int passedCount = 0;
        double totalScore = 0;
        for (Score s : myScores) {
            double sc = (s.getScore() == null ? 0 : s.getScore().doubleValue());
            totalScore += sc;
            if (sc >= 60) passedCount++;
        }
        result.put("passedCount", passedCount);
        double avg = myScores.isEmpty() ? 0 : Math.round(totalScore / myScores.size() * 10.0) / 10.0;
        result.put("avgScore", avg);

        return result;
    }
}