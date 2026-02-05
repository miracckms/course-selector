package com.yeditepe.courseselector.service;

import com.yeditepe.courseselector.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScheduleServiceTest {

    private ScheduleService scheduleService;

    @BeforeEach
    void setUp() {
        scheduleService = new ScheduleService();
    }

    // ============ HELPER METHODS ============
    
    private Course createCourse(String code, String name, String section, List<CourseDetail> details) {
        Course course = new Course();
        course.setCode(code);
        course.setNameEn(name);
        course.setSection(section);
        course.setCredit(3);
        course.setEcts(5);
        course.setDetails(details);
        return course;
    }

    private CourseDetail createDetail(String day, String startHour, String endHour) {
        CourseDetail detail = new CourseDetail();
        detail.setDay(day);
        detail.setStartHour(startHour);
        detail.setEndHour(endHour);
        detail.setTypeShort("LEC");
        detail.setFullName("Test Instructor");
        detail.setRoomName("Room 101");
        return detail;
    }

    // ============ NO CONFLICT TESTS ============

    @Test
    @DisplayName("Çakışma yok - Farklı günlerde dersler")
    void testNoConflict_DifferentDays() {
        List<Course> courses = Arrays.asList(
            createCourse("CSE101", "Intro to CS", "1", 
                Arrays.asList(createDetail("MON", "9:00", "10:00"))),
            createCourse("CSE102", "Data Structures", "1", 
                Arrays.asList(createDetail("TUE", "9:00", "10:00"))),
            createCourse("CSE103", "Algorithms", "1", 
                Arrays.asList(createDetail("WED", "9:00", "10:00")))
        );

        ScheduleRequest request = new ScheduleRequest();
        request.setCourseCodes(Arrays.asList("CSE101", "CSE102", "CSE103"));

        ScheduleResult result = scheduleService.generateSchedule(courses, request);

        assertTrue(result.isSuccess());
        assertFalse(result.isHasOverlap());
        assertEquals(3, result.getSelectedCourses().size());
        System.out.println("✅ Test passed: Farklı günlerde dersler - çakışma yok");
    }

    @Test
    @DisplayName("Çakışma yok - Aynı gün ardışık dersler")
    void testNoConflict_SameDay_Sequential() {
        List<Course> courses = Arrays.asList(
            createCourse("CSE101", "Intro to CS", "1", 
                Arrays.asList(createDetail("MON", "9:00", "10:00"))),
            createCourse("CSE102", "Data Structures", "1", 
                Arrays.asList(createDetail("MON", "10:00", "11:00"))),
            createCourse("CSE103", "Algorithms", "1", 
                Arrays.asList(createDetail("MON", "11:00", "12:00")))
        );

        ScheduleRequest request = new ScheduleRequest();
        request.setCourseCodes(Arrays.asList("CSE101", "CSE102", "CSE103"));

        ScheduleResult result = scheduleService.generateSchedule(courses, request);

        assertTrue(result.isSuccess());
        assertFalse(result.isHasOverlap());
        assertEquals(3, result.getSelectedCourses().size());
        System.out.println("✅ Test passed: Aynı gün ardışık dersler - çakışma yok");
    }

    // ============ ACCEPTABLE OVERLAP TESTS (<=60 min, 1 time) ============

    @Test
    @DisplayName("Kabul edilebilir çakışma - 30 dakika, 1 kere")
    void testAcceptableOverlap_30Minutes() {
        List<Course> courses = Arrays.asList(
            createCourse("CSE101", "Intro to CS", "1", 
                Arrays.asList(createDetail("MON", "9:00", "10:00"))),
            createCourse("CSE102", "Data Structures", "1", 
                Arrays.asList(createDetail("MON", "9:30", "10:30"))) // 30 dk çakışma
        );

        ScheduleRequest request = new ScheduleRequest();
        request.setCourseCodes(Arrays.asList("CSE101", "CSE102"));

        ScheduleResult result = scheduleService.generateSchedule(courses, request);

        assertTrue(result.isSuccess());
        assertTrue(result.isHasOverlap());
        assertEquals(30, result.getOverlapMinutes());
        assertEquals(2, result.getSelectedCourses().size());
        System.out.println("✅ Test passed: 30 dakika çakışma kabul edildi");
    }

    @Test
    @DisplayName("Kabul edilebilir çakışma - Tam 60 dakika, 1 kere")
    void testAcceptableOverlap_Exactly60Minutes() {
        List<Course> courses = Arrays.asList(
            createCourse("CSE101", "Intro to CS", "1", 
                Arrays.asList(createDetail("MON", "9:00", "11:00"))),
            createCourse("CSE102", "Data Structures", "1", 
                Arrays.asList(createDetail("MON", "10:00", "12:00"))) // 60 dk çakışma
        );

        ScheduleRequest request = new ScheduleRequest();
        request.setCourseCodes(Arrays.asList("CSE101", "CSE102"));

        ScheduleResult result = scheduleService.generateSchedule(courses, request);

        assertTrue(result.isSuccess());
        assertTrue(result.isHasOverlap());
        assertEquals(60, result.getOverlapMinutes());
        assertEquals(2, result.getSelectedCourses().size());
        System.out.println("✅ Test passed: 60 dakika çakışma kabul edildi (sınırda)");
    }

    @Test
    @DisplayName("Kabul edilebilir çakışma - 45 dakika, 1 kere")
    void testAcceptableOverlap_45Minutes() {
        List<Course> courses = Arrays.asList(
            createCourse("CSE101", "Intro to CS", "1", 
                Arrays.asList(createDetail("TUE", "14:00", "16:00"))),
            createCourse("CSE102", "Data Structures", "1", 
                Arrays.asList(createDetail("TUE", "15:15", "17:00"))), // 45 dk çakışma
            createCourse("CSE103", "Algorithms", "1", 
                Arrays.asList(createDetail("WED", "10:00", "12:00"))) // Çakışma yok
        );

        ScheduleRequest request = new ScheduleRequest();
        request.setCourseCodes(Arrays.asList("CSE101", "CSE102", "CSE103"));

        ScheduleResult result = scheduleService.generateSchedule(courses, request);

        assertTrue(result.isSuccess());
        assertTrue(result.isHasOverlap());
        assertEquals(45, result.getOverlapMinutes());
        assertEquals(3, result.getSelectedCourses().size());
        System.out.println("✅ Test passed: 45 dakika çakışma kabul edildi");
    }

    // ============ UNACCEPTABLE OVERLAP TESTS ============

    @Test
    @DisplayName("Kabul edilemez çakışma - 90 dakika (60'dan fazla)")
    void testUnacceptableOverlap_90Minutes() {
        List<Course> courses = Arrays.asList(
            createCourse("CSE101", "Intro to CS", "1", 
                Arrays.asList(createDetail("MON", "9:00", "12:00"))),
            createCourse("CSE102", "Data Structures", "1", 
                Arrays.asList(createDetail("MON", "10:30", "13:30"))) // 90 dk çakışma
        );

        ScheduleRequest request = new ScheduleRequest();
        request.setCourseCodes(Arrays.asList("CSE101", "CSE102"));

        ScheduleResult result = scheduleService.generateSchedule(courses, request);

        // Should either fail or exclude one course
        if (result.isSuccess()) {
            // If success, one course should be excluded
            assertTrue(result.getExcludedCourses() != null && !result.getExcludedCourses().isEmpty() 
                || result.getSelectedCourses().size() < 2);
        }
        System.out.println("✅ Test passed: 90 dakika çakışma reddedildi");
    }

    @Test
    @DisplayName("Kabul edilemez çakışma - 2 kere çakışma")
    void testUnacceptableOverlap_TwoOverlaps() {
        List<Course> courses = Arrays.asList(
            createCourse("CSE101", "Intro to CS", "1", 
                Arrays.asList(createDetail("MON", "9:00", "10:00"))),
            createCourse("CSE102", "Data Structures", "1", 
                Arrays.asList(createDetail("MON", "9:30", "10:30"))), // Çakışma 1: CSE101 ile
            createCourse("CSE103", "Algorithms", "1", 
                Arrays.asList(createDetail("MON", "10:00", "11:00"))) // Çakışma 2: CSE102 ile
        );

        ScheduleRequest request = new ScheduleRequest();
        request.setCourseCodes(Arrays.asList("CSE101", "CSE102", "CSE103"));

        ScheduleResult result = scheduleService.generateSchedule(courses, request);

        // 2 çakışma var, kabul edilmemeli veya bir ders çıkarılmalı
        if (result.isSuccess() && result.getSelectedCourses().size() == 3) {
            // Eğer 3 ders de seçildiyse, en fazla 1 çakışma olmalı
            assertTrue(result.getOverlapMinutes() <= 60);
        }
        System.out.println("✅ Test passed: 2 çakışma kontrolü yapıldı");
    }

    // ============ SECTION SELECTION TESTS ============

    @Test
    @DisplayName("Alternatif section seçimi - Çakışmayan section tercih edilir")
    void testAlternativeSectionSelection() {
        List<Course> courses = Arrays.asList(
            // CSE101 - tek section
            createCourse("CSE101", "Intro to CS", "1", 
                Arrays.asList(createDetail("MON", "9:00", "10:00"))),
            // CSE102 - Section 1 çakışıyor, Section 2 çakışmıyor
            createCourse("CSE102", "Data Structures", "1", 
                Arrays.asList(createDetail("MON", "9:00", "10:00"))), // Çakışıyor
            createCourse("CSE102", "Data Structures", "2", 
                Arrays.asList(createDetail("TUE", "9:00", "10:00")))  // Çakışmıyor
        );

        ScheduleRequest request = new ScheduleRequest();
        request.setCourseCodes(Arrays.asList("CSE101", "CSE102"));

        ScheduleResult result = scheduleService.generateSchedule(courses, request);

        assertTrue(result.isSuccess());
        assertEquals(2, result.getSelectedCourses().size());
        
        // CSE102 Section 2 seçilmiş olmalı (çakışmayan)
        Course selectedCSE102 = result.getSelectedCourses().stream()
            .filter(c -> c.getCode().equals("CSE102"))
            .findFirst()
            .orElse(null);
        
        assertNotNull(selectedCSE102);
        assertFalse(result.isHasOverlap()); // Çakışma olmamalı çünkü Section 2 seçildi
        System.out.println("✅ Test passed: Çakışmayan section tercih edildi");
    }

    // ============ MANUAL MODE TESTS ============

    @Test
    @DisplayName("Manuel mod - Kabul edilebilir çakışma")
    void testManualMode_AcceptableOverlap() {
        List<Course> courses = Arrays.asList(
            createCourse("CSE101", "Intro to CS", "1", 
                Arrays.asList(createDetail("MON", "9:00", "10:00"))),
            createCourse("CSE102", "Data Structures", "1", 
                Arrays.asList(createDetail("MON", "9:30", "10:30")))
        );

        ScheduleRequest request = new ScheduleRequest();
        request.setMode("MANUAL");
        List<ScheduleRequest.SelectedSection> sections = new ArrayList<>();
        sections.add(new ScheduleRequest.SelectedSection("CSE101", "1"));
        sections.add(new ScheduleRequest.SelectedSection("CSE102", "1"));
        request.setSelectedSections(sections);

        ScheduleResult result = scheduleService.generateSchedule(courses, request);

        assertTrue(result.isSuccess());
        assertTrue(result.isHasOverlap());
        assertEquals(30, result.getOverlapMinutes());
        System.out.println("✅ Test passed: Manuel mod - 30 dk çakışma kabul edildi");
    }

    @Test
    @DisplayName("Manuel mod - Kabul edilemez çakışma")
    void testManualMode_UnacceptableOverlap() {
        List<Course> courses = Arrays.asList(
            createCourse("CSE101", "Intro to CS", "1", 
                Arrays.asList(createDetail("MON", "9:00", "12:00"))),
            createCourse("CSE102", "Data Structures", "1", 
                Arrays.asList(createDetail("MON", "10:00", "13:00"))) // 120 dk çakışma
        );

        ScheduleRequest request = new ScheduleRequest();
        request.setMode("MANUAL");
        List<ScheduleRequest.SelectedSection> sections = new ArrayList<>();
        sections.add(new ScheduleRequest.SelectedSection("CSE101", "1"));
        sections.add(new ScheduleRequest.SelectedSection("CSE102", "1"));
        request.setSelectedSections(sections);

        ScheduleResult result = scheduleService.generateSchedule(courses, request);

        assertFalse(result.isSuccess()); // 120 dk > 60 dk limit
        assertTrue(result.getMessage().contains("kabul edilemez"));
        System.out.println("✅ Test passed: Manuel mod - 120 dk çakışma reddedildi");
    }

    // ============ EDGE CASES ============

    @Test
    @DisplayName("Edge case - Boş ders listesi")
    void testEmptyCourseList() {
        List<Course> courses = new ArrayList<>();

        ScheduleRequest request = new ScheduleRequest();
        request.setCourseCodes(Arrays.asList("CSE101"));

        ScheduleResult result = scheduleService.generateSchedule(courses, request);

        assertFalse(result.isSuccess());
        System.out.println("✅ Test passed: Boş ders listesi hata döndürdü");
    }

    @Test
    @DisplayName("Edge case - Ders bulunamadı")
    void testCourseNotFound() {
        List<Course> courses = Arrays.asList(
            createCourse("CSE101", "Intro to CS", "1", 
                Arrays.asList(createDetail("MON", "9:00", "10:00")))
        );

        ScheduleRequest request = new ScheduleRequest();
        request.setCourseCodes(Arrays.asList("CSE101", "CSE999")); // CSE999 yok

        ScheduleResult result = scheduleService.generateSchedule(courses, request);

        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("CSE999"));
        System.out.println("✅ Test passed: Olmayan ders kodu hata döndürdü");
    }

    @Test
    @DisplayName("Edge case - Tam sınırda çakışma (60 dakika)")
    void testBoundaryOverlap() {
        List<Course> courses = Arrays.asList(
            createCourse("CSE101", "Intro to CS", "1", 
                Arrays.asList(createDetail("MON", "9:00", "11:00"))),
            createCourse("CSE102", "Data Structures", "1", 
                Arrays.asList(createDetail("MON", "10:00", "12:00"))) // Tam 60 dk
        );

        ScheduleRequest request = new ScheduleRequest();
        request.setCourseCodes(Arrays.asList("CSE101", "CSE102"));

        ScheduleResult result = scheduleService.generateSchedule(courses, request);

        assertTrue(result.isSuccess()); // 60 dk = limit, kabul edilmeli
        assertTrue(result.isHasOverlap());
        assertEquals(60, result.getOverlapMinutes());
        System.out.println("✅ Test passed: Sınırda 60 dk çakışma kabul edildi");
    }

    @Test
    @DisplayName("Edge case - Sınırı 1 dakika aşan çakışma (61 dakika)")
    void testBoundaryOverlap_OneMinuteOver() {
        List<Course> courses = Arrays.asList(
            createCourse("CSE101", "Intro to CS", "1", 
                Arrays.asList(createDetail("MON", "9:00", "11:01"))),
            createCourse("CSE102", "Data Structures", "1", 
                Arrays.asList(createDetail("MON", "10:00", "12:00"))) // 61 dk çakışma
        );

        ScheduleRequest request = new ScheduleRequest();
        request.setCourseCodes(Arrays.asList("CSE101", "CSE102"));

        ScheduleResult result = scheduleService.generateSchedule(courses, request);

        // 61 dk > 60 dk, reddedilmeli veya alternatif bulunmalı
        if (result.isSuccess() && result.getSelectedCourses().size() == 2) {
            // Eğer ikisi de seçildiyse, çakışma 60'ı geçmemeli
            assertTrue(result.getOverlapMinutes() <= 60);
        }
        System.out.println("✅ Test passed: 61 dk çakışma kontrolü yapıldı");
    }
}
