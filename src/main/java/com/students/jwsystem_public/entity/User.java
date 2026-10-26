package com.students.jwsystem_public.entity;

import lombok.Data;

@Data
public class User {
    private Long id;
    private String username;
    private String password;
    private String realName;
    private String role;

    // ✅ 外键字段
    private Long deptId;
    private Long classId;

    // ✅ 联表查询展示字段（前端直接用这些）
    private String department;
    private String className;
}