package com.students.jwsystem_public.mapper;

import com.students.jwsystem_public.entity.Score;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ScoreMapper {
    List<Score> selectByStudentId(@Param("studentId") Long studentId);
}