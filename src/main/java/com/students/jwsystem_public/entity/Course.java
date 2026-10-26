package com.students.jwsystem_public.entity;

import lombok.Data;

@Data
public class Course {
    private Long id;
    private String courseName;
    private Long teacherId;
    private Integer credit;
    private Integer capacity;
    private Integer selectedCount;
    private String teacherName;
    private Boolean isSelected;

    // ✅ 必填：课程类型和开课院系
    private String courseType;
    private Long deptId;
    private String department;

    // ✅ 必填：必修课分配的目标班级
    private Long targetClassId;
}