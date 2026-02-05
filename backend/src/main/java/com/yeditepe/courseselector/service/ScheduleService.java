package com.yeditepe.courseselector.service;

import com.yeditepe.courseselector.dto.*;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    private static final List<String> DAYS_ORDER = Arrays.asList("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("H:mm");

    /**
     * Main entry point that handles both AUTO and MANUAL modes
     */
    public ScheduleResult generateSchedule(List<Course> allCourses, ScheduleRequest request) {
        if ("MANUAL".equalsIgnoreCase(request.getMode())) {
            return generateManualSchedule(allCourses, request.getSelectedSections());
        } else {
            return generateOptimalSchedule(allCourses, request.getCourseCodes());
        }
    }

    /**
     * MANUAL mode: User specifies exact sections
     */
    private ScheduleResult generateManualSchedule(List<Course> allCourses, List<ScheduleRequest.SelectedSection> selectedSections) {
        if (selectedSections == null || selectedSections.isEmpty()) {
            return createErrorResult("Manuel modda en az bir section seçilmelidir.");
        }

        List<Course> selectedCourses = new ArrayList<>();
        List<String> notFound = new ArrayList<>();

        for (ScheduleRequest.SelectedSection sel : selectedSections) {
            Course found = allCourses.stream()
                .filter(c -> c.getCode().equals(sel.getCode()) && 
                            String.valueOf(c.getSection()).equals(sel.getSection()))
                .findFirst()
                .orElse(null);

            if (found != null) {
                selectedCourses.add(found);
            } else {
                notFound.add(sel.getCode() + " (Grup " + sel.getSection() + ")");
            }
        }

        if (!notFound.isEmpty()) {
            return createErrorResult("Şu dersler bulunamadı: " + String.join(", ", notFound));
        }

        // Check for conflicts
        if (hasConflict(selectedCourses)) {
            return createErrorResult("Seçilen derslerde çakışma var! Lütfen farklı section'lar seçin.");
        }

        ScheduleMetrics metrics = calculateMetrics(selectedCourses);
        return createSuccessResult(selectedCourses, metrics);
    }

    /**
     * AUTO mode: System finds best non-conflicting sections
     * Uses backtracking with pruning for efficiency
     */
    public ScheduleResult generateOptimalSchedule(List<Course> allCourses, List<String> requestedCourseCodes) {
        // Group courses by code (each code can have multiple sections)
        Map<String, List<Course>> coursesByCode = allCourses.stream()
                .filter(c -> requestedCourseCodes.contains(c.getCode()))
                .collect(Collectors.groupingBy(Course::getCode));

        // Check if all requested courses are available
        List<String> missingCourses = requestedCourseCodes.stream()
                .filter(code -> !coursesByCode.containsKey(code))
                .collect(Collectors.toList());

        if (!missingCourses.isEmpty()) {
            return createErrorResult("Şu ders kodları bulunamadı: " + String.join(", ", missingCourses));
        }

        List<String> availableCodes = new ArrayList<>(requestedCourseCodes);
        
        // Use backtracking to find valid schedules efficiently
        List<ScoredSchedule> validSchedules = new ArrayList<>();
        backtrackFindSchedules(coursesByCode, availableCodes, 0, new ArrayList<>(), new ArrayList<>(), validSchedules, 10);
        
        if (!validSchedules.isEmpty()) {
            // Sort by score and return best
            validSchedules.sort(Comparator.comparingInt(s -> s.metrics.getScore()));
            return createSuccessResult(validSchedules.get(0).courses, validSchedules.get(0).metrics);
        }

        // No complete solution - try greedy approach for partial solution
        return findPartialScheduleGreedy(coursesByCode, availableCodes);
    }
    
    /**
     * Backtracking algorithm with pruning - stops early when conflict detected
     */
    private void backtrackFindSchedules(Map<String, List<Course>> coursesByCode, 
                                        List<String> courseCodes, 
                                        int index,
                                        List<Course> currentSchedule,
                                        List<TimeSlot> currentSlots,
                                        List<ScoredSchedule> validSchedules,
                                        int maxResults) {
        // Found enough solutions
        if (validSchedules.size() >= maxResults) {
            return;
        }
        
        // Successfully scheduled all courses
        if (index == courseCodes.size()) {
            ScheduleMetrics metrics = calculateMetrics(currentSchedule);
            validSchedules.add(new ScoredSchedule(new ArrayList<>(currentSchedule), metrics));
            return;
        }
        
        String code = courseCodes.get(index);
        List<Course> sections = coursesByCode.get(code);
        
        if (sections == null || sections.isEmpty()) {
            return;
        }
        
        // Try each section for this course
        for (Course section : sections) {
            List<TimeSlot> sectionSlots = getTimeSlots(section);
            
            // Check if this section conflicts with current schedule
            if (!hasConflictWithSlots(sectionSlots, currentSlots)) {
                // No conflict - add and recurse
                currentSchedule.add(section);
                currentSlots.addAll(sectionSlots);
                
                backtrackFindSchedules(coursesByCode, courseCodes, index + 1, 
                                       currentSchedule, currentSlots, validSchedules, maxResults);
                
                // Backtrack
                currentSchedule.remove(currentSchedule.size() - 1);
                currentSlots.subList(currentSlots.size() - sectionSlots.size(), currentSlots.size()).clear();
            }
        }
    }
    
    /**
     * Greedy approach for partial solution when complete solution doesn't exist
     */
    private ScheduleResult findPartialScheduleGreedy(Map<String, List<Course>> coursesByCode, List<String> courseCodes) {
        List<Course> selectedCourses = new ArrayList<>();
        List<TimeSlot> usedSlots = new ArrayList<>();
        List<String> includedCodes = new ArrayList<>();
        List<String> excludedCodes = new ArrayList<>();
        
        // Sort courses by number of sections (fewer sections = harder to schedule, do first)
        List<String> sortedCodes = new ArrayList<>(courseCodes);
        sortedCodes.sort(Comparator.comparingInt(code -> 
            coursesByCode.getOrDefault(code, Collections.emptyList()).size()));
        
        for (String code : sortedCodes) {
            List<Course> sections = coursesByCode.get(code);
            if (sections == null) continue;
            
            // Find best fitting section for this course
            Course bestSection = null;
            List<TimeSlot> bestSlots = null;
            int bestScore = Integer.MAX_VALUE;
            
            for (Course section : sections) {
                List<TimeSlot> sectionSlots = getTimeSlots(section);
                
                if (!hasConflictWithSlots(sectionSlots, usedSlots)) {
                    // Calculate how well this fits (prefer sections that leave more room)
                    int score = calculateFitScore(sectionSlots, usedSlots);
                    if (score < bestScore) {
                        bestScore = score;
                        bestSection = section;
                        bestSlots = sectionSlots;
                    }
                }
            }
            
            if (bestSection != null) {
                selectedCourses.add(bestSection);
                usedSlots.addAll(bestSlots);
                includedCodes.add(code);
            } else {
                excludedCodes.add(code);
            }
        }
        
        if (selectedCourses.isEmpty()) {
            return createErrorResult("Hiçbir ders kombinasyonu oluşturulamadı.");
        }
        
        ScheduleMetrics metrics = calculateMetrics(selectedCourses);
        ScheduleResult result = createSuccessResult(selectedCourses, metrics);
        
        if (!excludedCodes.isEmpty()) {
            result.setMessage("⚠️ Tüm dersler sığmadı! " + excludedCodes.size() + " ders çıkarıldı: " + 
                String.join(", ", excludedCodes) + ". " + selectedCourses.size() + " derslik program oluşturuldu.");
            result.setExcludedCourses(excludedCodes);
        }
        
        return result;
    }
    
    private List<TimeSlot> getTimeSlots(Course course) {
        List<TimeSlot> slots = new ArrayList<>();
        if (course.getDetails() == null) return slots;
        
        for (CourseDetail detail : course.getDetails()) {
            if (detail.getDay() != null && detail.getStartHour() != null && detail.getEndHour() != null) {
                slots.add(new TimeSlot(
                    detail.getDay(),
                    parseTime(detail.getStartHour()),
                    parseTime(detail.getEndHour())
                ));
            }
        }
        return slots;
    }
    
    private boolean hasConflictWithSlots(List<TimeSlot> newSlots, List<TimeSlot> existingSlots) {
        for (TimeSlot newSlot : newSlots) {
            for (TimeSlot existing : existingSlots) {
                if (newSlot.overlapsWith(existing)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private int calculateFitScore(List<TimeSlot> sectionSlots, List<TimeSlot> usedSlots) {
        // Lower score is better
        int score = 0;
        for (TimeSlot slot : sectionSlots) {
            // Prefer afternoon slots
            if (slot.start.isBefore(LocalTime.of(10, 0))) {
                score += 10;
            }
            // Prefer continuous blocks (check proximity to existing slots)
            for (TimeSlot used : usedSlots) {
                if (slot.day.equals(used.day)) {
                    long gap = Math.abs(java.time.Duration.between(slot.end, used.start).toMinutes());
                    if (gap < 60) score -= 5; // Small gap is good
                }
            }
        }
        return score;
    }

    private boolean hasConflict(List<Course> courses) {
        List<TimeSlot> allSlots = new ArrayList<>();

        for (Course course : courses) {
            if (course.getDetails() == null) continue;
            
            for (CourseDetail detail : course.getDetails()) {
                if (detail.getDay() == null || detail.getStartHour() == null || detail.getEndHour() == null) {
                    continue;
                }
                allSlots.add(new TimeSlot(
                    detail.getDay(),
                    parseTime(detail.getStartHour()),
                    parseTime(detail.getEndHour())
                ));
            }
        }

        // Check for overlaps
        for (int i = 0; i < allSlots.size(); i++) {
            for (int j = i + 1; j < allSlots.size(); j++) {
                if (allSlots.get(i).overlapsWith(allSlots.get(j))) {
                    return true;
                }
            }
        }
        return false;
    }

    private ScheduleMetrics calculateMetrics(List<Course> courses) {
        Map<String, List<TimeSlot>> slotsByDay = new HashMap<>();

        for (Course course : courses) {
            if (course.getDetails() == null) continue;
            
            for (CourseDetail detail : course.getDetails()) {
                if (detail.getDay() == null || detail.getStartHour() == null || detail.getEndHour() == null) {
                    continue;
                }
                slotsByDay.computeIfAbsent(detail.getDay(), k -> new ArrayList<>())
                    .add(new TimeSlot(
                        detail.getDay(),
                        parseTime(detail.getStartHour()),
                        parseTime(detail.getEndHour())
                    ));
            }
        }

        int totalGaps = 0;
        LocalTime earliestStart = LocalTime.of(23, 59);
        LocalTime latestEnd = LocalTime.of(0, 0);

        for (List<TimeSlot> daySlots : slotsByDay.values()) {
            daySlots.sort(Comparator.comparing(s -> s.start));
            
            for (int i = 0; i < daySlots.size() - 1; i++) {
                LocalTime gapStart = daySlots.get(i).end;
                LocalTime gapEnd = daySlots.get(i + 1).start;
                if (gapEnd.isAfter(gapStart)) {
                    totalGaps += java.time.Duration.between(gapStart, gapEnd).toMinutes();
                }
            }

            for (TimeSlot slot : daySlots) {
                if (slot.start.isBefore(earliestStart)) {
                    earliestStart = slot.start;
                }
                if (slot.end.isAfter(latestEnd)) {
                    latestEnd = slot.end;
                }
            }
        }

        int daysWithClasses = slotsByDay.size();
        
        // Calculate score: prefer fewer gaps, fewer days, later starts
        int score = totalGaps + (daysWithClasses * 100);
        if (earliestStart.isBefore(LocalTime.of(10, 0))) {
            score += 50; // Penalize early classes
        }

        return new ScheduleMetrics(
            totalGaps,
            daysWithClasses,
            earliestStart.format(TIME_FORMATTER),
            latestEnd.format(TIME_FORMATTER),
            score
        );
    }

    private LocalTime parseTime(String time) {
        if (time.contains(":")) {
            String[] parts = time.split(":");
            return LocalTime.of(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        }
        return LocalTime.of(Integer.parseInt(time), 0);
    }

    private ScheduleResult createErrorResult(String message) {
        ScheduleResult result = new ScheduleResult();
        result.setSuccess(false);
        result.setMessage(message);
        return result;
    }

    private ScheduleResult createSuccessResult(List<Course> courses, ScheduleMetrics metrics) {
        ScheduleResult result = new ScheduleResult();
        result.setSuccess(true);
        result.setMessage("Optimal ders programı oluşturuldu!");
        result.setSelectedCourses(courses);
        result.setMetrics(metrics);

        // Calculate totals
        int totalCredits = courses.stream().mapToInt(c -> c.getCredit() != null ? c.getCredit() : 0).sum();
        int totalEcts = courses.stream().mapToInt(c -> c.getEcts() != null ? c.getEcts() : 0).sum();
        result.setTotalCredits(totalCredits);
        result.setTotalEcts(totalEcts);

        // Build weekly schedule map
        Map<String, List<ScheduleSlot>> weeklySchedule = new LinkedHashMap<>();
        for (String day : DAYS_ORDER) {
            weeklySchedule.put(day, new ArrayList<>());
        }

        for (Course course : courses) {
            if (course.getDetails() == null) continue;
            
            for (CourseDetail detail : course.getDetails()) {
                if (detail.getDay() == null) continue;
                
                ScheduleSlot slot = new ScheduleSlot(
                    course.getCode(),
                    course.getNameEn(),
                    course.getSection(),
                    detail.getTypeShort(),
                    detail.getStartHour(),
                    detail.getEndHour(),
                    detail.getFullName(),
                    detail.getRoomName()
                );
                weeklySchedule.get(detail.getDay()).add(slot);
            }
        }

        // Sort each day's slots by start time
        for (List<ScheduleSlot> daySlots : weeklySchedule.values()) {
            daySlots.sort(Comparator.comparing(s -> parseTime(s.getStartHour())));
        }

        result.setWeeklySchedule(weeklySchedule);
        return result;
    }

    private static class TimeSlot {
        String day;
        LocalTime start;
        LocalTime end;

        TimeSlot(String day, LocalTime start, LocalTime end) {
            this.day = day;
            this.start = start;
            this.end = end;
        }

        boolean overlapsWith(TimeSlot other) {
            if (!this.day.equals(other.day)) {
                return false;
            }
            return this.start.isBefore(other.end) && other.start.isBefore(this.end);
        }
    }

    private static class ScoredSchedule {
        List<Course> courses;
        ScheduleMetrics metrics;
        int adjustedScore;

        ScoredSchedule(List<Course> courses, ScheduleMetrics metrics) {
            this.courses = courses;
            this.metrics = metrics;
            this.adjustedScore = metrics.getScore();
        }
    }
}
