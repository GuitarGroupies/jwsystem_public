package com.students.jwsystem_public.mapper;

import com.students.jwsystem_public.entity.Score;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ScoreMapper {
    List<Score> selectByStudentId(@Param("studentId") Long studentId);

    Score selectByStudentAndCourse(@Param("studentId") Long studentId, @Param("courseId") Long courseId);

    int insertScore(Score score);
    int updateScore(Score score);

    //  保留全删（防止报错，虽然暂时不用）
    int deleteByStudentId(@Param("studentId") Long studentId);

    int deleteByCourseId(@Param("courseId") Long courseId);
    // 按学生和课程精准删除成绩
    int deleteByStudentAndCourse(@Param("studentId") Long studentId, @Param("courseId") Long courseId);
}