<template>
  <div class="weekly-schedule" :class="isDarkMode ? 'dark' : 'light'">
    <!-- Grid -->
    <div class="grid" :style="{ gridTemplateColumns: `80px repeat(${days.length}, 1fr)` }">
      <!-- Header -->
      <div class="schedule-header-corner"></div>
      <div 
        v-for="day in days" 
        :key="day" 
        class="schedule-header-cell"
      >
        {{ day }}
      </div>

      <!-- Time Rows -->
      <!-- eslint-disable-next-line vue/no-v-for-template-key -->
      <template v-for="hour in hours" :key="hour">
        <div class="schedule-time-cell">
          {{ formatHour(hour) }}
        </div>
        <div 
          v-for="(day, dayIdx) in days" 
          :key="`${hour}-${day}`"
          class="schedule-cell"
        >
          <div 
            v-for="course in getCoursesAt(dayIdx, hour)" 
            :key="`${course.courseCode}-${course.startHour}-${course.type}`"
            class="course-card"
            :class="[getCourseColor(course), { 'has-conflict': hasConflict(dayIdx, course) }]"
            :style="{ height: `${getHeight(course)}px` }"
          >
            <div class="course-content">
              <!-- Course Code -->
              <div class="course-code">{{ course.courseCode }}</div>
              
              <!-- Type Badge -->
              <div class="course-type-badge" :class="getTypeBadgeClass(course.type)">
                {{ getTypeTurkish(course.type) }}
              </div>
              
              <!-- Time -->
              <div class="course-time">
                {{ course.startHour }} - {{ course.endHour }}
              </div>
              
              <!-- Instructor -->
              <div class="course-instructor" v-if="course.instructor && getHeight(course) > 70">
                {{ formatInstructor(course.instructor) }}
              </div>
            </div>
            
            <!-- Conflict indicator -->
            <div v-if="hasConflict(dayIdx, course)" class="conflict-badge" title="Çakışma var!">
              <span>!</span>
            </div>
          </div>
        </div>
      </template>
    </div>

    <!-- Legend -->
    <div class="legend">
      <div class="legend-item">
        <span class="legend-dot course-blue"></span>
        <span>Teorik</span>
      </div>
      <div class="legend-item">
        <span class="legend-dot course-emerald"></span>
        <span>Lab</span>
      </div>
      <div class="legend-item">
        <span class="legend-dot course-purple"></span>
        <span>PS</span>
      </div>
      <div class="legend-item">
        <span class="legend-dot course-orange"></span>
        <span>Diğer</span>
      </div>
      <div class="legend-item conflict">
        <span class="legend-dot bg-red-500"></span>
        <span>Çakışma</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  schedule: {
    type: Object,
    required: true
  },
  isDarkMode: {
    type: Boolean,
    default: false
  }
})

const days = ['Pzt', 'Sal', 'Çar', 'Per', 'Cum']
const hours = computed(() => {
  let min = 24, max = 0
  Object.values(props.schedule || {}).forEach(dayCourses => {
    dayCourses.forEach(course => {
      const start = parseInt(course.startHour?.split(':')[0] || 9)
      const end = parseInt(course.endHour?.split(':')[0] || 17)
      if (start < min) min = start
      if (end > max) max = end
    })
  })
  const startHour = Math.max(8, min)
  const endHour = Math.min(22, max + 1)
  return Array.from({ length: endHour - startHour }, (_, i) => startHour + i)
})

const dayMap = { 'MON': 0, 'TUE': 1, 'WED': 2, 'THU': 3, 'FRI': 4, 'SAT': 5, 'SUN': 6 }

const formatHour = (hour) => {
  return `${hour.toString().padStart(2, '0')}:00`
}

const formatInstructor = (name) => {
  if (!name) return ''
  const parts = name.split(' ')
  if (parts.length > 2) {
    return parts[0].charAt(0) + '. ' + parts[parts.length - 1]
  }
  if (parts.length === 2) {
    return parts[0].charAt(0) + '. ' + parts[1]
  }
  return name
}

const getCoursesAt = (dayIdx, hour) => {
  const dayKey = Object.keys(dayMap).find(k => dayMap[k] === dayIdx)
  const courses = props.schedule?.[dayKey] || []
  return courses.filter(c => {
    const startH = parseInt(c.startHour?.split(':')[0] || 0)
    return startH === hour
  })
}

const getAllCoursesForDay = (dayIdx) => {
  const dayKey = Object.keys(dayMap).find(k => dayMap[k] === dayIdx)
  return props.schedule?.[dayKey] || []
}

const parseTime = (timeStr) => {
  if (!timeStr) return 0
  const parts = timeStr.split(':')
  return parseInt(parts[0]) * 60 + parseInt(parts[1] || 0)
}

const hasConflict = (dayIdx, course) => {
  const allCourses = getAllCoursesForDay(dayIdx)
  const courseStart = parseTime(course.startHour)
  const courseEnd = parseTime(course.endHour)
  
  for (const other of allCourses) {
    if (other.courseCode === course.courseCode && other.startHour === course.startHour && other.type === course.type) continue
    
    const otherStart = parseTime(other.startHour)
    const otherEnd = parseTime(other.endHour)
    
    if (courseStart < otherEnd && otherStart < courseEnd) {
      return true
    }
  }
  return false
}

const getHeight = (course) => {
  const start = parseInt(course.startHour?.split(':')[0] || 0) * 60 + parseInt(course.startHour?.split(':')[1] || 0)
  const end = parseInt(course.endHour?.split(':')[0] || 0) * 60 + parseInt(course.endHour?.split(':')[1] || 0)
  const duration = end - start
  return Math.max(70, (duration / 60) * 70 - 4)
}

const getCourseColor = (course) => {
  const type = (course.type || '').toUpperCase()
  if (type.includes('LEC') || type.includes('LECTURE') || type.includes('TEO')) return 'course-blue'
  if (type.includes('LAB') || type.includes('LABOR')) return 'course-emerald'
  if (type.includes('PS') || type.includes('PROBLEM')) return 'course-purple'
  return 'course-orange'
}

const getTypeBadgeClass = (type) => {
  const t = (type || '').toUpperCase()
  if (t.includes('LEC') || t.includes('LECTURE') || t.includes('TEO')) return 'badge-blue'
  if (t.includes('LAB') || t.includes('LABOR')) return 'badge-emerald'
  if (t.includes('PS') || t.includes('PROBLEM')) return 'badge-purple'
  return 'badge-orange'
}

const getTypeTurkish = (type) => {
  const t = (type || '').toUpperCase()
  if (t.includes('LEC') || t.includes('LECTURE') || t.includes('TEO')) return 'Teorik'
  if (t.includes('LAB') || t.includes('LABOR')) return 'Lab'
  if (t.includes('PS') || t.includes('PROBLEM')) return 'PS'
  return type || 'Diğer'
}
</script>

<style scoped>
.weekly-schedule.dark {
  --bg-secondary: #1e293b;
  --bg-tertiary: #334155;
  --text-primary: #f8fafc;
  --text-secondary: #94a3b8;
  --border-color: #475569;
  --header-bg: #334155;
  --cell-hover: rgba(99, 102, 241, 0.1);
}

.weekly-schedule.light {
  --bg-secondary: #ffffff;
  --bg-tertiary: #f8fafc;
  --text-primary: #1e293b;
  --text-secondary: #64748b;
  --border-color: #e2e8f0;
  --header-bg: #f1f5f9;
  --cell-hover: rgba(99, 102, 241, 0.05);
}

.grid {
  display: grid;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0,0,0,0.15);
}

/* Header */
.schedule-header-corner {
  background: var(--header-bg);
  border-bottom: 2px solid var(--border-color);
}

.schedule-header-cell {
  padding: 16px 8px;
  text-align: center;
  font-weight: 800;
  font-size: 13px;
  color: var(--text-primary);
  background: var(--header-bg);
  border-bottom: 2px solid var(--border-color);
  border-left: 1px solid var(--border-color);
  text-transform: uppercase;
  letter-spacing: 1px;
}

/* Time Column */
.schedule-time-cell {
  padding: 12px 8px;
  text-align: center;
  font-size: 12px;
  font-weight: 700;
  color: var(--text-secondary);
  background: var(--bg-tertiary);
  border-bottom: 1px solid var(--border-color);
  height: 70px;
  display: flex;
  align-items: flex-start;
  justify-content: center;
}

/* Cells */
.schedule-cell {
  position: relative;
  background: var(--bg-secondary);
  border-bottom: 1px solid var(--border-color);
  border-left: 1px solid var(--border-color);
  min-height: 70px;
  padding: 3px;
  transition: background 0.2s;
}

.schedule-cell:hover {
  background: var(--cell-hover);
}

/* Course Cards */
.course-card {
  position: absolute;
  top: 3px;
  left: 3px;
  right: 3px;
  padding: 8px 10px;
  border-radius: 12px;
  color: white;
  overflow: hidden;
  z-index: 10;
  cursor: pointer;
  box-shadow: 0 4px 12px rgba(0,0,0,0.2);
  transition: all 0.2s ease;
}

.course-card:hover {
  transform: translateY(-2px) scale(1.02);
  box-shadow: 0 8px 24px rgba(0,0,0,0.3);
  z-index: 20;
}

.course-card.has-conflict {
  animation: pulse-conflict 2s ease-in-out infinite;
  box-shadow: 0 0 0 3px rgba(239, 68, 68, 0.6), 0 4px 12px rgba(0,0,0,0.2);
}

@keyframes pulse-conflict {
  0%, 100% { box-shadow: 0 0 0 3px rgba(239, 68, 68, 0.6), 0 4px 12px rgba(0,0,0,0.2); }
  50% { box-shadow: 0 0 0 5px rgba(239, 68, 68, 0.3), 0 4px 12px rgba(0,0,0,0.2); }
}

.course-content {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.course-code {
  font-weight: 900;
  font-size: 13px;
  letter-spacing: 0.5px;
  text-shadow: 0 1px 2px rgba(0,0,0,0.3);
  margin-bottom: 2px;
}

.course-type-badge {
  display: inline-block;
  font-size: 9px;
  font-weight: 800;
  padding: 2px 6px;
  border-radius: 6px;
  background: rgba(255,255,255,0.25);
  width: fit-content;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  margin-bottom: 4px;
}

.course-time {
  font-size: 11px;
  font-weight: 600;
  opacity: 0.95;
}

.course-instructor {
  font-size: 10px;
  opacity: 0.9;
  margin-top: auto;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

/* Conflict Badge */
.conflict-badge {
  position: absolute;
  top: 6px;
  right: 6px;
  width: 20px;
  height: 20px;
  background: #ef4444;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 900;
  font-size: 12px;
  animation: bounce-badge 1s ease-in-out infinite;
  box-shadow: 0 2px 8px rgba(239, 68, 68, 0.5);
}

@keyframes bounce-badge {
  0%, 100% { transform: scale(1); }
  50% { transform: scale(1.15); }
}

/* Course Colors - Vibrant Gradients */
.course-blue { 
  background: linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%);
}
.course-emerald { 
  background: linear-gradient(135deg, #10b981 0%, #047857 100%);
}
.course-purple { 
  background: linear-gradient(135deg, #8b5cf6 0%, #6d28d9 100%);
}
.course-orange { 
  background: linear-gradient(135deg, #f97316 0%, #c2410c 100%);
}

/* Legend */
.legend {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  justify-content: center;
  align-items: center;
  margin-top: 20px;
  padding: 12px 20px;
  background: var(--bg-tertiary);
  border-radius: 12px;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
  font-weight: 600;
  color: var(--text-secondary);
}

.legend-item.conflict {
  color: #ef4444;
  padding-left: 20px;
  border-left: 2px solid var(--border-color);
}

.legend-dot {
  width: 16px;
  height: 16px;
  border-radius: 6px;
}

.bg-red-500 {
  background: #ef4444;
}
</style>
