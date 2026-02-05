export default {
  // Header
  appTitle: 'Course Planner',
  university: 'Yeditepe University',
  activeUsers: '{count} active',

  // Sidebar
  semester: 'Semester',
  allDepartments: 'All Departments',
  loadCourses: 'Load Courses',
  loading: 'Loading...',
  auto: 'Auto',
  manual: 'Manual',
  autoModeDesc: 'Select courses, system finds the best sections automatically.',
  manualModeDesc: 'Choose each section yourself, full control is yours.',
  courses: 'Courses',
  searchPlaceholder: 'Search course code, name or instructor...',
  group: 'Section',
  selected: 'Selected',
  clearAll: 'Clear All',
  conflictWarning: 'Conflict detected!',
  generateSchedule: 'Generate Schedule',
  generating: 'Generating...',
  selectDepartmentFirst: 'Select a department and load courses first',
  noCourses: 'No courses found for this department',
  noCoursesThisDay: 'No courses this day',
  scrollHint: 'Scroll',

  // Days
  days: {
    MON: 'Monday',
    TUE: 'Tuesday',
    WED: 'Wednesday',
    THU: 'Thursday',
    FRI: 'Friday',
    SAT: 'Saturday',
    SUN: 'Sunday'
  },
  daysShort: {
    MON: 'Mon',
    TUE: 'Tue',
    WED: 'Wed',
    THU: 'Thu',
    FRI: 'Fri',
    SAT: 'Sat',
    SUN: 'Sun'
  },

  // Course types
  types: {
    lecture: 'Lecture',
    lab: 'Laboratory',
    ps: 'Problem Session',
    other: 'Other'
  },
  typesShort: {
    lecture: 'Lecture',
    lab: 'Lab',
    ps: 'PS',
    other: 'Other'
  },

  // Schedule results
  totalCourses: 'Courses',
  totalCredits: 'Total Credits',
  totalEcts: 'Total ECTS',
  daysWithClasses: 'Class Days',
  gaps: 'Gaps',
  overlap: 'Overlap',
  minutes: 'min',

  // Warnings
  overlapWarning: 'Overlap Warning',
  overlapMessage: 'Schedule has {minutes} minutes of overlap.',
  overlapTip: 'Tip: You may not be able to attend parts of overlapping courses.',
  partialSchedule: 'Partial Schedule Created',
  partialScheduleMessage: 'Not all courses fit. The following courses were excluded due to conflicts:',

  // Schedule
  weeklySchedule: 'Weekly Schedule',
  scheduledCourses: 'Courses in Generated Schedule',
  credit: 'Credit',

  // Empty state
  noScheduleYet: 'No Schedule Yet',
  noScheduleMessage: 'Select a department from the side panel, load courses and generate a schedule',
  selectCourse: 'Select Course',
  generatingSchedule: 'Generating Schedule...',
  calculatingSchedule: 'Calculating the optimal course schedule, please wait',

  // Errors
  errorLoadingSeasons: 'Error loading semesters: {message}',
  errorLoadingDepartments: 'Error loading departments: {message}',
  errorLoadingCourses: 'Error loading courses: {message}',
  errorGenerating: 'Error generating schedule: {message}',

  // Legend
  legend: {
    lecture: 'Lecture',
    lab: 'Lab',
    ps: 'PS',
    other: 'Other',
    conflict: 'Conflict'
  }
}
