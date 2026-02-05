package com.yeditepe.courseselector.controller;

import com.yeditepe.courseselector.dto.*;
import com.yeditepe.courseselector.service.CourseCacheService;
import com.yeditepe.courseselector.service.ScheduleService;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CourseController {

    private final CourseCacheService cacheService;
    private final ScheduleService scheduleService;

    public CourseController(CourseCacheService cacheService, ScheduleService scheduleService) {
        this.cacheService = cacheService;
        this.scheduleService = scheduleService;
    }

    @GetMapping("/seasons")
    public ResponseEntity<List<AcademicSeason>> getSeasons() {
        return ResponseEntity.ok(cacheService.getSeasons());
    }

    @GetMapping("/departments")
    public ResponseEntity<List<Department>> getDepartments() {
        return ResponseEntity.ok(cacheService.getDepartments());
    }

    @GetMapping("/calendar")
    public ResponseEntity<List<CalendarEvent>> getCalendar() {
        return ResponseEntity.ok(cacheService.getCalendar());
    }

    @GetMapping("/courses/{seasonId}/all")
    public ResponseEntity<List<Course>> getAllCourses(@PathVariable Long seasonId) {
        return ResponseEntity.ok(cacheService.getAllCourses(seasonId));
    }

    @GetMapping("/courses/{seasonId}/{departmentId}")
    public ResponseEntity<List<Course>> getCourses(
            @PathVariable Long seasonId,
            @PathVariable Long departmentId) {
        return ResponseEntity.ok(cacheService.getCourses(seasonId, departmentId));
    }

    @PostMapping("/schedule/generate")
    public ResponseEntity<ScheduleResult> generateSchedule(@Valid @RequestBody ScheduleRequest request) {
        List<Course> allCourses;
        
        // Check if "all departments" mode
        if (request.getDepartmentId() == null || request.getDepartmentId() == 0) {
            // Get from cache - all departments
            allCourses = new java.util.ArrayList<>(
                cacheService.getAllCourses(request.getSeasonId())
            );
        } else {
            // Get courses from primary department (from cache)
            allCourses = new java.util.ArrayList<>(
                cacheService.getCourses(request.getSeasonId(), request.getDepartmentId())
            );
            
            // Get courses from additional departments if specified
            if (request.getAdditionalDepartmentIds() != null && !request.getAdditionalDepartmentIds().isEmpty()) {
                for (Long deptId : request.getAdditionalDepartmentIds()) {
                    if (!deptId.equals(request.getDepartmentId())) { // Avoid duplicate
                        List<Course> deptCourses = cacheService.getCourses(request.getSeasonId(), deptId);
                        allCourses.addAll(deptCourses);
                    }
                }
            }
        }
        
        // Use the new method that handles both AUTO and MANUAL modes
        ScheduleResult result = scheduleService.generateSchedule(allCourses, request);
        return ResponseEntity.ok(result);
    }

    // ============ CACHE MANAGEMENT ENDPOINTS ============

    /**
     * Get cache status information
     */
    @GetMapping("/cache/status")
    public ResponseEntity<Map<String, Object>> getCacheStatus() {
        return ResponseEntity.ok(cacheService.getCacheStatus());
    }

    /**
     * Manually trigger cache refresh
     */
    @PostMapping("/cache/refresh")
    public ResponseEntity<Map<String, String>> refreshCache() {
        // Run refresh in background thread
        new Thread(() -> cacheService.refreshAllData()).start();
        return ResponseEntity.ok(Map.of(
            "message", "Cache refresh started",
            "note", "Check /api/cache/status for progress"
        ));
    }

    /**
     * Check if cache is ready (initial load complete)
     */
    @GetMapping("/cache/ready")
    public ResponseEntity<Map<String, Object>> isCacheReady() {
        return ResponseEntity.ok(Map.of(
            "ready", cacheService.isReady(),
            "lastRefreshTime", cacheService.getLastRefreshTime() != null 
                ? cacheService.getLastRefreshTime().toString() 
                : "Never"
        ));
    }
}
