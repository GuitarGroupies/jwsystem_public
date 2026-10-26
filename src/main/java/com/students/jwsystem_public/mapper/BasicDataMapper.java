package com.students.jwsystem_public.mapper;

import com.students.jwsystem_public.entity.Department;
import com.students.jwsystem_public.entity.Major;
import com.students.jwsystem_public.entity.SysClass;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BasicDataMapper {
    List<Department> findAllDepts();
    int insertDept(@Param("deptName") String deptName);

    List<Major> findMajorsByDeptId(@Param("deptId") Long deptId);
    int insertMajor(@Param("majorName") String majorName, @Param("deptId") Long deptId);
    List<SysClass> findClassesByDeptId(@Param("deptId") Long deptId);
    List<SysClass> findClassesByMajorId(@Param("majorId") Long majorId);
    int updateDept(@Param("id") Long id, @Param("deptName") String deptName);
    int updateMajor(@Param("id") Long id, @Param("majorName") String majorName);
    int updateClass(@Param("id") Long id, @Param("className") String className);
    int insertClass(@Param("className") String className, @Param("majorId") Long majorId);
}