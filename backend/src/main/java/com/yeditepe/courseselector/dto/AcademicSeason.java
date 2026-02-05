package com.yeditepe.courseselector.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AcademicSeason {
    private Long id;
    private Integer year;
    private String name;
    private String nameEn;
    private Integer active;
}
