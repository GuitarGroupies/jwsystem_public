package com.students.jwsystem_public.mapper;


import com.students.jwsystem_public.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    User findByUsername(@Param("username")String username);
    int insertUser(User user);

    int deleteById(@Param("id") Long id);

    int countByRole(@Param("role")String role);

    List<User> findAll();
    int resetPassword(@Param("id") Long id, @Param("password") String password);
    List<User> findStudentsByClassId(@Param("classId") Long classId);
    List<User> findTeachersByDeptId(@Param("deptId") Long deptId);

    List<User> findAllTeachers();
}
