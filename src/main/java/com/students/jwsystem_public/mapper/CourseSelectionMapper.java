package com.students.jwsystem_public.mapper;

import com.students.jwsystem_public.entity.Course;           // ✅ 新增
import com.students.jwsystem_public.entity.CourseSelection;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface CourseSelectionMapper {
 // 查询某学生是否已选某课程
 int countByStudentAndCourse(@Param("studentId") Long studentId,
                             @Param("courseId") Long courseId);

 // 插入选课记录
 int insertSelection(CourseSelection selection);

 // 已选人数 +1
 int incrementSelectedCount(@Param("courseId") Long courseId);

 // 已选人数 -1
 int decrementSelectedCount(@Param("courseId") Long courseId);

 // 删除选课记录
 int deleteSelection(@Param("studentId") Long studentId,
                     @Param("courseId") Long courseId);

 // 根据学生ID查询已选课程ID列表
 List<Long> selectCourseIdsByStudentId(@Param("studentId") Long studentId);

 // 查询某学生已选的课程详情列表（用于“我选的课”页面）
 List<Course> selectMyCourses(@Param("studentId") Long studentId);
}