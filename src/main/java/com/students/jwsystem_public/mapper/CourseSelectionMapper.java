package com.students.jwsystem_public.mapper;

import com.students.jwsystem_public.entity.Course;
import com.students.jwsystem_public.entity.CourseSelection;  // ✅ 确保导入
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CourseSelectionMapper {
 int countByStudentAndCourse(@Param("studentId") Long studentId,
                             @Param("courseId") Long courseId);

 int insertSelection(CourseSelection selection);

 int incrementSelectedCount(@Param("courseId") Long courseId);

 int decrementSelectedCount(@Param("courseId") Long courseId);

 int deleteSelection(@Param("studentId") Long studentId,
                     @Param("courseId") Long courseId);

 List<Long> selectCourseIdsByStudentId(@Param("studentId") Long studentId);

 List<Course> selectMyCourses(@Param("studentId") Long studentId);

 // 返回类型改成 List<CourseSelection>
 List<CourseSelection> selectStudentsByCourseId(@Param("courseId") Long courseId);

 List<CourseSelection> selectAllStudentsByTeacherId(@Param("teacherId") Long teacherId);
 int deleteByStudentId(@Param("studentId") Long studentId);

 int deleteByCourseId(@Param("courseId") Long courseId);
}