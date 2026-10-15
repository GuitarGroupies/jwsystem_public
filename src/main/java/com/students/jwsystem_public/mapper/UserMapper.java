package com.students.jwsystem_public.mapper;


import com.students.jwsystem_public.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    User findByUsername(@Param("username")String username);



}
