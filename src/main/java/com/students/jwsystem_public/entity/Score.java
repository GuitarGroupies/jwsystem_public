package com.students.jwsystem_public.entity;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class Score {
    private Long id;
    private Long studentId;
    private Long courseId;
    private BigDecimal score;      // 数据库 score 是 decimal(5,2)

    // 联表查询时用的字段
    private String courseName;
    private Integer credit;
    private String teacherName;
}