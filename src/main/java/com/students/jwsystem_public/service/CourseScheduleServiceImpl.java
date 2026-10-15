package com.students.jwsystem_public.service;

import com.students.jwsystem_public.entity.CourseSchedule;
import com.students.jwsystem_public.entity.User;
import com.students.jwsystem_public.mapper.CourseScheduleMapper;
import com.students.jwsystem_public.mapper.UserMapper;
import com.students.jwsystem_public.service.CourseScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service   //关键：必须加这个注解
public class CourseScheduleServiceImpl implements CourseScheduleService {

    @Autowired
    private CourseScheduleMapper courseScheduleMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<CourseSchedule> getMySchedule(String username) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        return courseScheduleMapper.selectByStudentId(user.getId());
    }
}