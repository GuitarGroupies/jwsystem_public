package com.students.jwsystem_public.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class CourseSelection {
    private Long id;
    private Long studentId;       // 数据库 student_id
    private Long courseId;        // 数据库 course_id
    private Date selectTime;      // 数据库 select_time
    private String studentUsername;  // 学号
    private String studentName;      // 学生姓名
    private BigDecimal score;        // 成绩（可能为 null）
    private String department;
    private String className;
}