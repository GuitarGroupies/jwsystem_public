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

    // 新增：标识当前登录学生是否已选这门课
    private Boolean isSelected;
}