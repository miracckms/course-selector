package com.yeditepe.courseselector.controller;

import com.yeditepe.courseselector.dto.*;
import com.yeditepe.courseselector.service.ScheduleService;
import com.yeditepe.courseselector.service.YeditepeApiService;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CourseController {

    private final YeditepeApiService yeditepeApiService;
    private final ScheduleService scheduleService;

    public CourseController(YeditepeApiService yeditepeApiService, ScheduleService scheduleService) {
        this.yeditepeApiService = yeditepeApiService;
        this.scheduleService = scheduleService;
    }

    @GetMapping("/seasons")
    public ResponseEntity<List<AcademicSeason>> getSeasons() {
        return ResponseEntity.ok(yeditepeApiService.getAcademicSeasons());
    }

    @GetMapping("/departments")
    public ResponseEntity<List<Department>> getDepartments() {
        return ResponseEntity.ok(yeditepeApiService.getDepartments());
    }

    @GetMapping("/calendar")
    public ResponseEntity<List<CalendarEvent>> getCalendar() {
        return ResponseEntity.ok(yeditepeApiService.getCalendar());
    }

    @GetMapping("/courses/{seasonId}/all")
    public ResponseEntity<List<Course>> getAllCourses(@PathVariable Long seasonId) {
        return ResponseEntity.ok(yeditepeApiService.getAllCourses(seasonId));
    }

    @GetMapping("/courses/{seasonId}/{departmentId}")
    public ResponseEntity<List<Course>> getCourses(
            @PathVariable Long seasonId,
            @PathVariable Long departmentId) {
        return ResponseEntity.ok(yeditepeApiService.getCourses(seasonId, departmentId));
    }

    @PostMapping("/schedule/generate")
    public ResponseEntity<ScheduleResult> generateSchedule(@Valid @RequestBody ScheduleRequest request) {
        List<Course> allCourses;
        
        // Check if "all departments" mode
        if (request.getDepartmentId() == null || request.getDepartmentId() == 0) {
            // Fetch from all departments
            allCourses = new java.util.ArrayList<>(
                yeditepeApiService.getAllCourses(request.getSeasonId())
            );
        } else {
            // Fetch courses from primary department
            allCourses = new java.util.ArrayList<>(
                yeditepeApiService.getCourses(request.getSeasonId(), request.getDepartmentId())
            );
            
            // Fetch courses from additional departments if specified
            if (request.getAdditionalDepartmentIds() != null && !request.getAdditionalDepartmentIds().isEmpty()) {
                for (Long deptId : request.getAdditionalDepartmentIds()) {
                    if (!deptId.equals(request.getDepartmentId())) { // Avoid duplicate fetch
                        List<Course> deptCourses = yeditepeApiService.getCourses(request.getSeasonId(), deptId);
                        allCourses.addAll(deptCourses);
                    }
                }
            }
        }
        
        // Use the new method that handles both AUTO and MANUAL modes
        ScheduleResult result = scheduleService.generateSchedule(allCourses, request);
        return ResponseEntity.ok(result);
    }
}
