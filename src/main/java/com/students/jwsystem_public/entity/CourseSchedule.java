package com.students.jwsystem_public.entity;

import lombok.Data;

@Data
public class CourseSchedule {
    private Long id;
    private Long courseId;
    private Integer weekDay;       // 1=周一 ... 7=周日
    private Integer startSection;  // 开始节次
    private Integer endSection;    // 结束节次
    private String location;

    private String courseName;
    private String teacherName;
    private Integer credit;
}
