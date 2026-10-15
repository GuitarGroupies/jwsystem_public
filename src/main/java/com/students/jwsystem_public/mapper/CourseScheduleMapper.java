package com.students.jwsystem_public.mapper;

import com.students.jwsystem_public.entity.CourseSchedule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CourseScheduleMapper {
    // 查询某学生已选课程的排课信息（用于课表展示）
    List<CourseSchedule> selectByStudentId(@Param("studentId") Long studentId);
}