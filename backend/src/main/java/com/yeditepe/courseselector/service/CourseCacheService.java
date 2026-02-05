package com.yeditepe.courseselector.service;

import com.yeditepe.courseselector.dto.AcademicSeason;
import com.yeditepe.courseselector.dto.CalendarEvent;
import com.yeditepe.courseselector.dto.Course;
import com.yeditepe.courseselector.dto.Department;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
public class CourseCacheService {

    private static final Logger log = LoggerFactory.getLogger(CourseCacheService.class);

    private final YeditepeApiService yeditepeApiService;

    // Cache storage
    private List<AcademicSeason> cachedSeasons = Collections.emptyList();
    private List<Department> cachedDepartments = Collections.emptyList();
    private List<CalendarEvent> cachedCalendar = Collections.emptyList();
    
    // Season ID -> All courses for that season
    private final Map<Long, List<Course>> cachedCoursesBySeason = new ConcurrentHashMap<>();
    
    // Season ID -> Department ID -> Courses
    private final Map<Long, Map<Long, List<Course>>> cachedCoursesBySeasonAndDept = new ConcurrentHashMap<>();

    private LocalDateTime lastRefreshTime;
    private final AtomicBoolean isRefreshing = new AtomicBoolean(false);
    private boolean initialLoadComplete = false;

    public CourseCacheService(YeditepeApiService yeditepeApiService) {
        this.yeditepeApiService = yeditepeApiService;
    }

    /**
     * Initial data load when application starts
     */
    @PostConstruct
    public void init() {
        log.info("Starting initial cache load...");
        refreshAllData();
    }

    /**
     * Scheduled job - runs every day at 06:00 AM
     * Cron format: second minute hour day-of-month month day-of-week
     */
    @Scheduled(cron = "${cache.refresh.cron:0 0 6 * * *}")
    public void scheduledRefresh() {
        log.info("Scheduled cache refresh triggered at {}", LocalDateTime.now());
        refreshAllData();
    }

    /**
     * Refresh all cached data from Yeditepe API
     */
    public synchronized void refreshAllData() {
        if (isRefreshing.get()) {
            log.warn("Cache refresh already in progress, skipping...");
            return;
        }

        isRefreshing.set(true);
        long startTime = System.currentTimeMillis();

        try {
            log.info("Starting full cache refresh...");

            // 1. Fetch seasons
            log.info("Fetching academic seasons...");
            List<AcademicSeason> seasons = yeditepeApiService.getAcademicSeasons();
            this.cachedSeasons = Collections.unmodifiableList(new ArrayList<>(seasons));
            log.info("Cached {} academic seasons", seasons.size());

            // 2. Fetch departments
            log.info("Fetching departments...");
            List<Department> departments = yeditepeApiService.getDepartments();
            this.cachedDepartments = Collections.unmodifiableList(new ArrayList<>(departments));
            log.info("Cached {} departments", departments.size());

            // 3. Fetch calendar
            log.info("Fetching calendar...");
            List<CalendarEvent> calendar = yeditepeApiService.getCalendar();
            this.cachedCalendar = Collections.unmodifiableList(new ArrayList<>(calendar));
            log.info("Cached {} calendar events", calendar.size());

            // 4. Fetch courses for active season(s)
            // Usually we only need the current/active season
            if (!seasons.isEmpty()) {
                // Get the most recent season (usually the first one)
                AcademicSeason activeSeason = seasons.get(0);
                log.info("Fetching all courses for active season: {} (ID: {})", 
                        activeSeason.getName(), activeSeason.getId());
                
                fetchAndCacheCoursesForSeason(activeSeason.getId(), departments);
            }

            this.lastRefreshTime = LocalDateTime.now();
            this.initialLoadComplete = true;
            
            long duration = System.currentTimeMillis() - startTime;
            log.info("Cache refresh completed successfully in {} ms", duration);

        } catch (Exception e) {
            log.error("Failed to refresh cache: {}", e.getMessage(), e);
        } finally {
            isRefreshing.set(false);
        }
    }

    /**
     * Fetch and cache courses for a specific season
     */
    private void fetchAndCacheCoursesForSeason(Long seasonId, List<Department> departments) {
        Map<Long, List<Course>> coursesByDept = new ConcurrentHashMap<>();
        List<Course> allCoursesForSeason = Collections.synchronizedList(new ArrayList<>());

        // Fetch courses for each department
        for (Department dept : departments) {
            try {
                List<Course> deptCourses = yeditepeApiService.getCourses(seasonId, dept.getId());
                if (!deptCourses.isEmpty()) {
                    coursesByDept.put(dept.getId(), Collections.unmodifiableList(new ArrayList<>(deptCourses)));
                    allCoursesForSeason.addAll(deptCourses);
                    log.debug("Cached {} courses for department: {}", deptCourses.size(), dept.getName());
                }
            } catch (Exception e) {
                log.warn("Failed to fetch courses for department {}: {}", dept.getName(), e.getMessage());
            }
        }

        // Store in cache
        cachedCoursesBySeasonAndDept.put(seasonId, coursesByDept);
        cachedCoursesBySeason.put(seasonId, Collections.unmodifiableList(new ArrayList<>(allCoursesForSeason)));
        
        log.info("Cached {} total courses for season {} across {} departments", 
                allCoursesForSeason.size(), seasonId, coursesByDept.size());
    }

    // ============ GETTERS FOR CACHED DATA ============

    public List<AcademicSeason> getSeasons() {
        return cachedSeasons;
    }

    public List<Department> getDepartments() {
        return cachedDepartments;
    }

    public List<CalendarEvent> getCalendar() {
        return cachedCalendar;
    }

    /**
     * Get all courses for a season from cache
     * If not in cache, fetch and cache it
     */
    public List<Course> getAllCourses(Long seasonId) {
        List<Course> cached = cachedCoursesBySeason.get(seasonId);
        
        if (cached != null) {
            log.debug("Returning {} cached courses for season {}", cached.size(), seasonId);
            return cached;
        }

        // Not in cache - fetch it
        log.info("Season {} not in cache, fetching courses...", seasonId);
        fetchAndCacheCoursesForSeason(seasonId, cachedDepartments);
        
        return cachedCoursesBySeason.getOrDefault(seasonId, Collections.emptyList());
    }

    /**
     * Get courses for a specific department from cache
     * If not in cache, fetch and cache it
     */
    public List<Course> getCourses(Long seasonId, Long departmentId) {
        Map<Long, List<Course>> seasonCache = cachedCoursesBySeasonAndDept.get(seasonId);
        
        if (seasonCache != null) {
            List<Course> deptCourses = seasonCache.get(departmentId);
            if (deptCourses != null) {
                log.debug("Returning {} cached courses for season {} dept {}", 
                        deptCourses.size(), seasonId, departmentId);
                return deptCourses;
            }
        }

        // Not in cache - fetch it
        log.info("Courses for season {} dept {} not in cache, fetching...", seasonId, departmentId);
        try {
            List<Course> courses = yeditepeApiService.getCourses(seasonId, departmentId);
            
            // Add to cache
            cachedCoursesBySeasonAndDept
                .computeIfAbsent(seasonId, k -> new ConcurrentHashMap<>())
                .put(departmentId, Collections.unmodifiableList(new ArrayList<>(courses)));
            
            return courses;
        } catch (Exception e) {
            log.error("Failed to fetch courses: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    // ============ CACHE STATUS ============

    public Map<String, Object> getCacheStatus() {
        Map<String, Object> status = new LinkedHashMap<>();
        status.put("lastRefreshTime", lastRefreshTime != null 
                ? lastRefreshTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) 
                : "Never");
        status.put("isRefreshing", isRefreshing.get());
        status.put("initialLoadComplete", initialLoadComplete);
        status.put("cachedSeasons", cachedSeasons.size());
        status.put("cachedDepartments", cachedDepartments.size());
        status.put("cachedCalendarEvents", cachedCalendar.size());
        status.put("cachedSeasonCourses", cachedCoursesBySeason.entrySet().stream()
                .collect(Collectors.toMap(
                        e -> "season_" + e.getKey(),
                        e -> e.getValue().size()
                )));
        return status;
    }

    public boolean isReady() {
        return initialLoadComplete && !isRefreshing.get();
    }

    public LocalDateTime getLastRefreshTime() {
        return lastRefreshTime;
    }
}
