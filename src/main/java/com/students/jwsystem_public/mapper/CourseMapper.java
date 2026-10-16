package com.students.jwsystem_public.mapper;

import com.students.jwsystem_public.entity.Course;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CourseMapper {
    List<Course> getAllCourses();
    Course getCourseById(@Param("id") Long id);

    List<Course> selectByTeacherId(@Param("teacherId") Long teacherId);
}