package com.yeditepe.courseselector.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDetail {
    private String fullName;
    private String type;
    private String typeShort;
    private String day;
    private String startHour;
    private String endHour;
    private String roomFloor;
    private String roomName;
    private String nameShort;
}
