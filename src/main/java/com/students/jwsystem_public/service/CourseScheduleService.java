package com.students.jwsystem_public.service;

import com.students.jwsystem_public.entity.CourseSchedule;
import java.util.List;

public interface CourseScheduleService {
    // 查询当前学生的课表
    List<CourseSchedule> getMySchedule(String username);
}