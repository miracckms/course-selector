export default {
  // Header
  appTitle: 'Ders Planlayıcı',
  university: 'Yeditepe Üniversitesi',
  activeUsers: '{count} aktif',

  // Sidebar
  semester: 'Dönem',
  allDepartments: 'Tüm Bölümler',
  loadCourses: 'Dersleri Getir',
  loading: 'Yükleniyor...',
  auto: 'Otomatik',
  manual: 'Manuel',
  autoModeDesc: 'Dersleri seç, sistem en uygun grupları otomatik belirlesin.',
  manualModeDesc: 'Her dersin grubunu kendin seç, tam kontrol sende.',
  courses: 'Dersler',
  searchPlaceholder: 'Ders kodu, adı veya hoca ara...',
  group: 'Grup',
  selected: 'Seçilen',
  clearAll: 'Temizle',
  conflictWarning: 'Çakışma var!',
  generateSchedule: 'Program Oluştur',
  generating: 'Oluşturuluyor...',
  selectDepartmentFirst: 'Önce bölüm seçip dersleri getirin',
  noCourses: 'Bu bölümde ders bulunamadı',
  noCoursesThisDay: 'Bu gün ders yok',
  scrollHint: 'Kaydırın',

  // Days
  days: {
    MON: 'Pazartesi',
    TUE: 'Salı',
    WED: 'Çarşamba',
    THU: 'Perşembe',
    FRI: 'Cuma',
    SAT: 'Cumartesi',
    SUN: 'Pazar'
  },
  daysShort: {
    MON: 'Pzt',
    TUE: 'Sal',
    WED: 'Çar',
    THU: 'Per',
    FRI: 'Cum',
    SAT: 'Cmt',
    SUN: 'Paz'
  },

  // Course types
  types: {
    lecture: 'Teorik Ders',
    lab: 'Laboratuvar',
    ps: 'Problem Çözümü',
    other: 'Diğer'
  },
  typesShort: {
    lecture: 'Teorik',
    lab: 'Lab',
    ps: 'PS',
    other: 'Diğer'
  },

  // Schedule results
  totalCourses: 'Ders Sayısı',
  totalCredits: 'Toplam Kredi',
  totalEcts: 'Toplam AKTS',
  daysWithClasses: 'Ders Günü',
  gaps: 'Boşluk',
  overlap: 'Çakışma',
  minutes: 'dk',

  // Warnings
  overlapWarning: 'Çakışma Uyarısı',
  overlapMessage: 'Programda {minutes} dakikalık çakışma var.',
  overlapTip: 'İpucu: Çakışan derslerin bir kısmına katılamayabilirsiniz.',
  partialSchedule: 'Kısmi Program Oluşturuldu',
  partialScheduleMessage: 'Tüm dersler sığmadı. Çakışma nedeniyle şu dersler çıkarıldı:',

  // Schedule
  weeklySchedule: 'Haftalık Program',
  scheduledCourses: 'Oluşturulan Programdaki Dersler',
  credit: 'Kredi',

  // Empty state
  noScheduleYet: 'Henüz Program Yok',
  noScheduleMessage: 'Sol panelden bölüm seçin, dersleri yükleyin ve program oluşturun',
  selectCourse: 'Ders Seç',
  generatingSchedule: 'Program Oluşturuluyor...',
  calculatingSchedule: 'En uygun ders programı hesaplanıyor, lütfen bekleyin',

  // Errors
  errorLoadingSeasons: 'Dönemler yüklenirken hata: {message}',
  errorLoadingDepartments: 'Bölümler yüklenirken hata: {message}',
  errorLoadingCourses: 'Dersler yüklenirken hata: {message}',
  errorGenerating: 'Program oluşturulurken hata: {message}',

  // Legend
  legend: {
    lecture: 'Teorik',
    lab: 'Lab',
    ps: 'PS',
    other: 'Diğer',
    conflict: 'Çakışma'
  }
}
