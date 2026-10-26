package com.students.jwsystem_public.mapper;

import com.students.jwsystem_public.entity.Course;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CourseMapper {
    List<Course> getAllCourses();
    Course getCourseById(@Param("id") Long id);

    int insertCourse(Course course);
    int deleteCourse(@Param("id") Long id);
    int countAllCourses();
    List<Course> selectByTeacherId(@Param("teacherId") Long teacherId);

    // ==========================================
    // ✅ 新增：必修课批量分配相关方法
    // ==========================================

    // 批量给某个班级的所有学生插入选课记录
    int batchInsertSelection(@Param("classId") Long classId, @Param("courseId") Long courseId);

    // 重新计算并更新课程的已选人数
    int updateCourseSelectedCount(@Param("courseId") Long courseId);
}