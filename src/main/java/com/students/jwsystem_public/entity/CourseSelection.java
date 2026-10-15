package com.students.jwsystem_public.entity;

import lombok.Data;
import java.util.Date;

@Data
public class CourseSelection {
    private Long id;
    private Long studentId;       // 数据库 student_id
    private Long courseId;        // 数据库 course_id
    private Date selectTime;      // 数据库 select_time
}