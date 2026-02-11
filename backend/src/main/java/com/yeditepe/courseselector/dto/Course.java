package com.yeditepe.courseselector.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    private String code;
    private String section;
    private String name;
    private String nameEn;
    private Integer credit;
    private Integer ects;
    private Integer fullQuota;
    private Integer quota;
    private String info;
    private List<CourseDetail> details;
    private Long departmentId;  // Added for quota checking
}
