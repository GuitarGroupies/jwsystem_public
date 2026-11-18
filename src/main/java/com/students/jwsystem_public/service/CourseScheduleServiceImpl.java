package com.students.jwsystem_public.service;

import cn.hutool.json.JSONUtil;
import com.students.jwsystem_public.entity.CourseSchedule;
import com.students.jwsystem_public.entity.User;
import com.students.jwsystem_public.mapper.CourseScheduleMapper;
import com.students.jwsystem_public.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CourseScheduleServiceImpl implements CourseScheduleService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private CourseScheduleMapper courseScheduleMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<CourseSchedule> getMySchedule(String username) {
        User user = userMapper.findByUsername(username);
        if (user == null) throw new RuntimeException("用户不存在");

        String redisKey = "schedule:my:" + user.getId();

        // 1. 查 Redis
        Object cachedData = redisTemplate.opsForValue().get(redisKey);
        if (cachedData != null) {
            System.out.println("=== 课表命中缓存，直接返回 ===");
            // ✅ 手动把 JSON 字符串转回 List
            return JSONUtil.toList(cachedData.toString(), CourseSchedule.class);
        }

        // 2. 查 MySQL
        List<CourseSchedule> schedules = courseScheduleMapper.selectByStudentId(user.getId());
        System.out.println("=== 课表走数据库查询 ===");

        // 3. 写缓存
        redisTemplate.opsForValue().set(redisKey, JSONUtil.toJsonStr(schedules), 30, TimeUnit.MINUTES);

        return schedules;
    }

    @Override
    public List<CourseSchedule> getTeacherSchedule(String username) {
        User user = userMapper.findByUsername(username);
        if (user == null) throw new RuntimeException("用户不存在");
        return courseScheduleMapper.selectByTeacherId(user.getId());
    }
}