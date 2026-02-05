<template>
  <div :class="isDarkMode ? 'dark-theme' : 'light-theme'" class="h-screen overflow-hidden">
    <div class="h-full app-bg flex flex-col">
      
      <!-- Top Bar -->
      <header class="header-bg border-b border-color flex-shrink-0 px-4 py-3">
        <div class="flex items-center justify-between">
          <div class="flex items-center gap-3">
            <div class="w-10 h-10 bg-gradient-to-br from-indigo-500 to-purple-600 rounded-xl flex items-center justify-center shadow-lg">
              <svg class="w-5 h-5 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"/>
              </svg>
            </div>
            <div>
              <h1 class="font-bold text-base text-primary">Ders Planlayıcı</h1>
              <p class="text-xs text-secondary hidden sm:block">Yeditepe Üniversitesi</p>
            </div>
          </div>
          
          <div class="flex items-center gap-3">
            <!-- Active Users Badge -->
            <div v-if="activeUserCount > 0" class="hidden sm:flex items-center gap-2 px-3 py-2 rounded-xl active-users-badge">
              <span class="relative flex h-2.5 w-2.5">
                <span class="animate-ping absolute inline-flex h-full w-full rounded-full bg-green-400 opacity-75"></span>
                <span class="relative inline-flex rounded-full h-2.5 w-2.5 bg-green-500"></span>
              </span>
              <span class="text-sm font-medium">{{ activeUserCount }} aktif</span>
            </div>
            
            <div v-if="activeSeason" class="hidden md:flex items-center gap-2 px-4 py-2 rounded-xl season-badge">
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"/>
              </svg>
              <span class="text-sm font-semibold">{{ activeSeason.year }} {{ activeSeason.name }}</span>
            </div>
            
            <button @click="toggleTheme" class="p-2.5 rounded-xl toggle-btn transition-all duration-300 hover:scale-105">
              <svg v-if="isDarkMode" class="w-5 h-5 text-yellow-400" fill="currentColor" viewBox="0 0 20 20">
                <path fill-rule="evenodd" d="M10 2a1 1 0 011 1v1a1 1 0 11-2 0V3a1 1 0 011-1zm4 8a4 4 0 11-8 0 4 4 0 018 0zm-.464 4.95l.707.707a1 1 0 001.414-1.414l-.707-.707a1 1 0 00-1.414 1.414zm2.12-10.607a1 1 0 010 1.414l-.706.707a1 1 0 11-1.414-1.414l.707-.707a1 1 0 011.414 0zM17 11a1 1 0 100-2h-1a1 1 0 100 2h1zm-7 4a1 1 0 011 1v1a1 1 0 11-2 0v-1a1 1 0 011-1zM5.05 6.464A1 1 0 106.465 5.05l-.708-.707a1 1 0 00-1.414 1.414l.707.707zm1.414 8.486l-.707.707a1 1 0 01-1.414-1.414l.707-.707a1 1 0 011.414 1.414zM4 11a1 1 0 100-2H3a1 1 0 000 2h1z" clip-rule="evenodd"/>
              </svg>
              <svg v-else class="w-5 h-5 text-indigo-600" fill="currentColor" viewBox="0 0 20 20">
                <path d="M17.293 13.293A8 8 0 016.707 2.707a8.001 8.001 0 1010.586 10.586z"/>
              </svg>
            </button>
            
            <button @click="mobileMenuOpen = !mobileMenuOpen" class="p-2.5 rounded-xl toggle-btn lg:hidden transition-transform duration-300 hover:scale-105">
              <svg class="w-5 h-5 text-primary" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16"/>
              </svg>
            </button>
          </div>
        </div>
      </header>

      <div class="flex flex-1 overflow-hidden">
        <!-- Sidebar -->
        <aside 
          :class="[
            'fixed lg:relative inset-y-0 right-0 lg:right-auto lg:left-0 z-40 w-80 sidebar-bg border-l lg:border-l-0 lg:border-r border-color transform transition-transform duration-300 lg:transform-none flex flex-col flex-shrink-0 shadow-xl lg:shadow-none',
            mobileMenuOpen ? 'translate-x-0' : 'translate-x-full lg:translate-x-0'
          ]"
        >
          <!-- ===== TOP FIXED SECTION ===== -->
          <div class="flex-shrink-0 p-3 space-y-2 border-b border-color">
            <!-- Aktif Dönem Card (Compact) -->
            <div class="season-card px-3 py-2 rounded-xl animate-fade-in flex items-center justify-between">
              <div class="flex items-center gap-2">
                <svg class="w-4 h-4 opacity-80" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"/>
                </svg>
                <span class="text-xs opacity-80">Dönem:</span>
              </div>
              <span class="text-sm font-bold" v-if="activeSeason">{{ activeSeason.year }} {{ activeSeason.name }}</span>
            </div>
            
            <!-- Bölüm Seçimi -->
            <div class="space-y-1">
              <select 
                v-model="departmentId" 
                @change="onDepartmentChange"
                class="w-full px-3 py-2 input-bg rounded-xl text-primary text-sm focus:ring-2 focus:ring-indigo-500 outline-none cursor-pointer transition-all duration-200"
              >
                <option value="all">Tüm Bölümler</option>
                <option v-for="dept in filteredDepartments" :key="dept.id" :value="dept.id">
                  {{ dept.name }}
                </option>
              </select>
            </div>

            <!-- Dersleri Getir -->
            <button 
              @click="loadCourses"
              :disabled="!selectedSeason || !departmentId || loadingCourses"
              class="w-full py-2 btn-primary text-white text-sm font-bold rounded-xl disabled:opacity-50 disabled:cursor-not-allowed transition-all duration-300 flex items-center justify-center gap-2 hover:shadow-lg"
            >
              <svg v-if="loadingCourses" class="animate-spin h-4 w-4" fill="none" viewBox="0 0 24 24">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"/>
                <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"/>
              </svg>
              <svg v-else class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-4l-4 4m0 0l-4-4m4 4V4"/>
              </svg>
              {{ loadingCourses ? 'Yükleniyor...' : 'Dersleri Getir' }}
            </button>

            <!-- Mod Seçimi -->
            <div v-if="availableCourses.length > 0" class="animate-fade-in">
              <div class="mode-toggle-container">
                <div 
                  class="mode-toggle-slider"
                  :class="scheduleMode === 'MANUAL' ? 'slide-right' : 'slide-left'"
                ></div>
                <button 
                  @click="scheduleMode = 'AUTO'"
                  class="mode-toggle-btn"
                  :class="scheduleMode === 'AUTO' ? 'active' : ''"
                >
                  <span class="mode-icon">🤖</span>
                  <span>Otomatik</span>
                </button>
                <button 
                  @click="scheduleMode = 'MANUAL'"
                  class="mode-toggle-btn"
                  :class="scheduleMode === 'MANUAL' ? 'active' : ''"
                >
                  <span class="mode-icon">✋</span>
                  <span>Manuel</span>
                </button>
              </div>
            </div>
          </div>

          <!-- ===== MIDDLE SCROLLABLE SECTION ===== -->
          <div class="flex-1 overflow-y-auto p-3 space-y-2">
            <!-- Ders Listesi -->
            <div v-if="availableCourses.length > 0" class="space-y-1.5 animate-fade-in">
              <div class="flex items-center justify-between">
                <label class="text-xs font-semibold text-secondary uppercase tracking-wide">Dersler</label>
                <span class="text-xs badge-count px-2 py-0.5 rounded-full">{{ groupedCourses.length }}</span>
              </div>
              <input 
                v-model="courseSearch"
                type="text"
                placeholder="Ders ara..."
                class="w-full px-3 py-2 input-bg rounded-xl text-primary placeholder-secondary text-sm focus:ring-2 focus:ring-indigo-500 outline-none transition-all duration-200"
              />
              <div class="space-y-1 pr-1">
                <div 
                  v-for="course in filteredCourses" 
                  :key="course.code"
                  class="rounded-xl overflow-hidden"
                >
                  <!-- AUTO Mode: Click to select entire course -->
                  <div 
                    v-if="scheduleMode === 'AUTO'"
                    @click="toggleCourseAuto(course.code)"
                    class="px-3 py-2 cursor-pointer flex items-center gap-2 course-item transition-all duration-200"
                    :class="isCourseSelectedAuto(course.code) ? 'selected' : ''"
                  >
                    <div 
                      class="w-4 h-4 rounded border-2 flex items-center justify-center flex-shrink-0 transition-all duration-200 checkbox"
                      :class="isCourseSelectedAuto(course.code) ? 'checked' : ''"
                    >
                      <svg v-if="isCourseSelectedAuto(course.code)" class="w-2.5 h-2.5 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="3" d="M5 13l4 4L19 7"/>
                      </svg>
                    </div>
                    <div class="flex-1 min-w-0">
                      <div class="flex items-center gap-1.5">
                        <span class="font-mono text-xs font-bold text-primary">{{ course.code }}</span>
                        <span class="text-[10px] px-1.5 py-0.5 rounded-full badge-section font-semibold">{{ course.sections.length }}</span>
                      </div>
                      <p class="text-[10px] text-secondary truncate">{{ course.name }}</p>
                    </div>
                  </div>

                  <!-- MANUAL Mode: Expand to select sections -->
                  <template v-else>
                    <div 
                      @click="toggleCourseExpand(course.code)"
                      class="px-3 py-2 cursor-pointer flex items-center gap-2 course-item transition-all duration-200"
                    >
                      <svg 
                        class="w-3 h-3 text-secondary transition-transform duration-200 flex-shrink-0" 
                        :class="expandedCourses.includes(course.code) ? 'rotate-90' : ''"
                        fill="none" stroke="currentColor" viewBox="0 0 24 24"
                      >
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"/>
                      </svg>
                      <div class="flex-1 min-w-0">
                        <div class="flex items-center gap-1.5">
                          <span class="font-mono text-xs font-bold text-primary">{{ course.code }}</span>
                          <span class="text-[10px] px-1.5 py-0.5 rounded-full badge-section font-semibold">{{ course.sections.length }}</span>
                        </div>
                        <p class="text-[10px] text-secondary truncate">{{ course.name }}</p>
                      </div>
                    </div>
                    
                    <!-- Section'lar -->
                    <div v-if="expandedCourses.includes(course.code)" class="section-list border-t border-color animate-expand">
                      <div 
                        v-for="section in course.sections" 
                        :key="`${course.code}-${section.section}`"
                        @click="toggleSection(course.code, section.section)"
                        class="px-3 py-2 cursor-pointer flex items-center gap-2 section-item transition-all duration-200 border-b border-color last:border-b-0"
                        :class="isSelected(course.code, section.section) ? 'selected' : ''"
                      >
                        <div 
                          class="w-4 h-4 rounded border-2 flex items-center justify-center flex-shrink-0 transition-all duration-200 checkbox"
                          :class="isSelected(course.code, section.section) ? 'checked' : ''"
                        >
                          <svg v-if="isSelected(course.code, section.section)" class="w-2.5 h-2.5 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="3" d="M5 13l4 4L19 7"/>
                          </svg>
                        </div>
                        <div class="flex-1 min-w-0">
                          <span class="text-xs font-semibold text-primary">Grup {{ section.section }}</span>
                          <div class="text-[10px] text-secondary">
                            <span v-for="(detail, idx) in section.details" :key="idx">
                              {{ getDayShort(detail.day) }} {{ detail.startHour }}-{{ detail.endHour }}{{ idx < section.details.length - 1 ? ', ' : '' }}
                            </span>
                          </div>
                        </div>
                      </div>
                    </div>
                  </template>
                </div>
              </div>
            </div>

            <!-- Boş Durum -->
            <div v-else class="text-center py-8 text-secondary text-sm">
              <p>Önce bölüm seçip dersleri getirin</p>
            </div>
          </div>

          <!-- ===== BOTTOM FIXED SECTION ===== -->
          <div class="flex-shrink-0 p-3 border-t border-color space-y-2">
            <!-- Seçilen Dersler - AUTO Mode -->
            <div v-if="scheduleMode === 'AUTO' && selectedCoursesAuto.length > 0" class="animate-fade-in">
              <label class="text-[10px] font-semibold text-secondary uppercase tracking-wide mb-1 block">Seçilen ({{ selectedCoursesAuto.length }})</label>
              <div class="flex flex-wrap gap-1 max-h-16 overflow-y-auto">
                <span 
                  v-for="code in selectedCoursesAuto" 
                  :key="code"
                  class="inline-flex items-center gap-1 px-2 py-1 text-xs font-bold rounded-full tag-selected"
                >
                  {{ code }}
                  <button @click.stop="removeCourseAuto(code)" class="hover:opacity-70 transition-opacity">
                    <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
                    </svg>
                  </button>
                </span>
              </div>
            </div>

            <!-- Seçilen Dersler - MANUAL Mode -->
            <div v-if="scheduleMode === 'MANUAL' && selectedSections.length > 0" class="animate-fade-in">
              <label class="text-[10px] font-semibold text-secondary uppercase tracking-wide mb-1 block">Seçilen ({{ selectedSections.length }})</label>
              
              <!-- Çakışma Uyarısı -->
              <div v-if="conflicts.length > 0" class="p-2 conflict-warning rounded-lg mb-1 text-xs">
                ⚠️ Çakışma var!
              </div>

              <div class="flex flex-wrap gap-1 max-h-16 overflow-y-auto">
                <span 
                  v-for="sel in selectedSections" 
                  :key="`${sel.code}-${sel.section}`"
                  class="inline-flex items-center gap-1 px-2 py-1 text-xs font-bold rounded-full"
                  :class="isInConflict(sel.code) ? 'tag-conflict' : 'tag-selected'"
                >
                  {{ sel.code }}
                  <button @click.stop="removeSection(sel.code, sel.section)" class="hover:opacity-70 transition-opacity">
                    <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
                    </svg>
                  </button>
                </span>
              </div>
            </div>

            <!-- Program Oluştur -->
            <button 
              @click="generateSchedule"
              :disabled="!canGenerate || generating"
              class="w-full py-2.5 btn-success text-white text-sm font-bold rounded-xl disabled:opacity-50 disabled:cursor-not-allowed transition-all duration-300 flex items-center justify-center gap-2 hover:shadow-lg"
            >
              <svg v-if="generating" class="animate-spin h-4 w-4" fill="none" viewBox="0 0 24 24">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"/>
                <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"/>
              </svg>
              <svg v-else class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z"/>
              </svg>
              {{ generating ? 'Oluşturuluyor...' : 'Program Oluştur' }}
            </button>
          </div>
        </aside>

        <!-- Mobile Overlay -->
        <div 
          v-if="mobileMenuOpen" 
          @click="mobileMenuOpen = false"
          class="fixed inset-0 bg-black/50 z-30 lg:hidden backdrop-blur-sm animate-fade-in"
        ></div>

        <!-- Main Content -->
        <main class="flex-1 overflow-hidden flex flex-col">
          
          <!-- Hata -->
          <div v-if="error" class="mx-4 mt-4 p-4 error-box rounded-xl flex items-center gap-3 animate-shake">
            <svg class="w-6 h-6 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"/>
            </svg>
            <p class="text-sm font-medium">{{ error }}</p>
          </div>

          <!-- Sonuç -->
          <div v-if="scheduleResult && scheduleResult.success" class="flex-1 overflow-auto p-4 space-y-4">
            
            <!-- Kısmi Çözüm Uyarısı -->
            <div v-if="scheduleResult.excludedCourses && scheduleResult.excludedCourses.length > 0" 
                 class="p-4 rounded-xl bg-gradient-to-r from-amber-500/20 to-orange-500/20 border border-amber-500/50 animate-fade-in">
              <div class="flex items-start gap-3">
                <div class="w-10 h-10 rounded-xl bg-gradient-to-br from-amber-500 to-orange-600 flex items-center justify-center flex-shrink-0">
                  <svg class="w-5 h-5 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"/>
                  </svg>
                </div>
                <div class="flex-1">
                  <h3 class="font-bold text-amber-400 mb-1">Kısmi Program Oluşturuldu</h3>
                  <p class="text-sm text-secondary mb-2">Tüm dersler sığmadı. Çakışma nedeniyle şu dersler çıkarıldı:</p>
                  <div class="flex flex-wrap gap-2">
                    <span v-for="code in scheduleResult.excludedCourses" :key="code" 
                          class="px-3 py-1 bg-red-500/30 text-red-300 text-sm font-bold rounded-full border border-red-500/50">
                      ❌ {{ code }}
                    </span>
                  </div>
                </div>
              </div>
            </div>
            
            <!-- İstatistikler ve Başlık -->
            <div class="flex items-center gap-3 flex-wrap animate-slide-down">
              <div class="stat-card stat-blue">
                <span class="text-2xl font-bold">{{ scheduleResult.totalCredits }}</span>
                <span class="text-xs opacity-90">Toplam Kredi</span>
              </div>
              <div class="stat-card stat-purple">
                <span class="text-2xl font-bold">{{ scheduleResult.totalEcts }}</span>
                <span class="text-xs opacity-90">Toplam AKTS</span>
              </div>
              <div class="stat-card stat-green">
                <span class="text-2xl font-bold">{{ scheduleResult.metrics?.daysWithClasses }}</span>
                <span class="text-xs opacity-90">Ders Günü</span>
              </div>
              <div class="stat-card stat-orange">
                <span class="text-2xl font-bold">{{ scheduleResult.metrics?.totalGaps }}<span class="text-sm">dk</span></span>
                <span class="text-xs opacity-90">Boşluk</span>
              </div>
              
              <!-- Program Başlığı -->
              <div class="hidden sm:block border-l border-color pl-4 ml-2">
                <h2 class="text-base font-bold text-primary">{{ activeSeason?.year }} {{ activeSeason?.name }}</h2>
                <p class="text-xs text-secondary">{{ new Date().toLocaleDateString('tr-TR') }}</p>
              </div>
            </div>

            <!-- Haftalık Program -->
            <div class="card-bg rounded-2xl p-4">
              <h2 class="text-lg font-bold text-primary mb-4 flex items-center gap-2">
                <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"/>
                </svg>
                Haftalık Program
              </h2>
              <WeeklySchedule :schedule="scheduleResult.weeklySchedule" :isDarkMode="isDarkMode" />
            </div>
            
            <!-- Oluşturulan Programdaki Dersler -->
            <div class="card-bg rounded-2xl p-5 animate-fade-in">
              <h2 class="text-xl font-bold text-primary mb-5 flex items-center gap-3">
                <div class="w-10 h-10 rounded-xl bg-gradient-to-br from-emerald-500 to-teal-600 flex items-center justify-center shadow-lg">
                  <svg class="w-5 h-5 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-3 7h3m-3 4h3m-6-4h.01M9 16h.01"/>
                  </svg>
                </div>
                Oluşturulan Programdaki Dersler
              </h2>
              
              <div class="grid gap-5 md:grid-cols-2 xl:grid-cols-3">
                <div 
                  v-for="(course, idx) in scheduleResult.selectedCourses" 
                  :key="course.code"
                  class="course-detail-card rounded-2xl overflow-hidden animate-slide-up"
                  :style="{ animationDelay: `${idx * 0.1}s` }"
                >
                  <!-- Course Header with gradient -->
                  <div class="course-detail-header p-4">
                    <div class="flex items-start justify-between">
                      <div>
                        <h3 class="font-black text-white text-lg tracking-wide">{{ course.code }}</h3>
                        <p class="text-sm text-white/80 mt-1">{{ course.name || course.nameEn }}</p>
                      </div>
                      <span class="px-3 py-1.5 text-xs font-black rounded-lg bg-white/20 text-white backdrop-blur-sm">
                        Grup {{ course.section }}
                      </span>
                    </div>
                    
                    <!-- Credits in header -->
                    <div class="flex gap-3 mt-3">
                      <span class="px-3 py-1 rounded-lg bg-white/15 text-white text-sm font-semibold">
                        {{ course.credit }} Kredi
                      </span>
                      <span class="px-3 py-1 rounded-lg bg-white/15 text-white text-sm font-semibold">
                        {{ course.ects }} AKTS
                      </span>
                    </div>
                  </div>
                  
                  <!-- Course Details (Lectures, Labs, etc.) -->
                  <div class="p-4 space-y-3">
                    <div 
                      v-for="(detail, didx) in course.details" 
                      :key="didx"
                      class="detail-row rounded-xl p-3"
                      :class="getDetailTypeClass(detail.typeShort)"
                    >
                      <div class="flex items-center justify-between mb-2">
                        <span class="font-bold text-sm flex items-center gap-2">
                          <span class="text-lg">{{ getTypeIcon(detail.typeShort) }}</span>
                          {{ getTypeTurkishFull(detail.typeShort) }}
                        </span>
                        <span class="text-xs font-bold px-2 py-1 rounded-md bg-black/10">
                          {{ getDayTurkish(detail.day) }}
                        </span>
                      </div>
                      <div class="flex items-center justify-between text-sm">
                        <span class="flex items-center gap-2 font-semibold">
                          <svg class="w-4 h-4 opacity-70" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"/>
                          </svg>
                          {{ detail.startHour }} - {{ detail.endHour }}
                        </span>
                        <span v-if="detail.fullName" class="text-xs flex items-center gap-1.5 font-medium opacity-80 truncate max-w-[140px]" :title="detail.fullName">
                          <svg class="w-3.5 h-3.5 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"/>
                          </svg>
                          {{ formatInstructorShort(detail.fullName) }}
                        </span>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- Oluşturuluyor Durumu -->
          <div v-else-if="generating" class="flex-1 flex flex-col items-center justify-center text-center px-6">
            <div class="generating-animation mb-8">
              <!-- Animated Calendar Icon -->
              <div class="calendar-container">
                <div class="calendar-icon">
                  <svg class="w-20 h-20" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"/>
                  </svg>
                </div>
                <!-- Floating Elements -->
                <div class="floating-element el-1">📚</div>
                <div class="floating-element el-2">🔬</div>
                <div class="floating-element el-3">✏️</div>
                <div class="floating-element el-4">📅</div>
              </div>
              
              <!-- Progress Bar -->
              <div class="progress-container mt-8">
                <div class="progress-bar">
                  <div class="progress-fill"></div>
                </div>
              </div>
            </div>
            
            <h3 class="text-2xl font-bold text-primary mb-2 animate-pulse">Program Oluşturuluyor...</h3>
            <p class="text-secondary text-base max-w-sm">
              En uygun ders programı hesaplanıyor, lütfen bekleyin
            </p>
            
            <!-- Loading dots -->
            <div class="flex gap-2 mt-6">
              <span class="loading-dot"></span>
              <span class="loading-dot" style="animation-delay: 0.2s"></span>
              <span class="loading-dot" style="animation-delay: 0.4s"></span>
            </div>
          </div>

          <!-- Boş Durum -->
          <div v-else class="flex-1 flex flex-col items-center justify-center text-center px-6">
            <div class="w-24 h-24 mb-6 rounded-3xl empty-icon flex items-center justify-center animate-float">
              <svg class="w-12 h-12" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"/>
              </svg>
            </div>
            <h3 class="text-2xl font-bold text-primary mb-2">Henüz Program Yok</h3>
            <p class="text-secondary text-base max-w-sm">
              Sol panelden bölüm seçin, dersleri yükleyin ve program oluşturun
            </p>
            <button 
              @click="mobileMenuOpen = true"
              class="mt-6 px-6 py-3 btn-primary text-white text-sm font-bold rounded-xl lg:hidden hover:shadow-lg transition-all duration-300"
            >
              Ders Seç
            </button>
          </div>
        </main>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { courseApi } from './api/courseApi'
import WeeklySchedule from './components/WeeklySchedule.vue'

// Theme & Mobile
const isDarkMode = ref(true)
const mobileMenuOpen = ref(false)

// Active Users WebSocket
const activeUserCount = ref(0)
let websocket = null

const connectWebSocket = () => {
  const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:'
  const wsUrl = `${protocol}//${window.location.host}/ws/active-users`
  
  websocket = new WebSocket(wsUrl)
  
  websocket.onopen = () => {
    console.log('WebSocket connected')
    // Send ping every 30 seconds to keep connection alive
    setInterval(() => {
      if (websocket && websocket.readyState === WebSocket.OPEN) {
        websocket.send('ping')
      }
    }, 30000)
  }
  
  websocket.onmessage = (event) => {
    try {
      const data = JSON.parse(event.data)
      if (data.type === 'activeUsers') {
        activeUserCount.value = data.count
      }
    } catch (e) {
      // Ignore pong messages
    }
  }
  
  websocket.onclose = () => {
    console.log('WebSocket disconnected, reconnecting...')
    setTimeout(connectWebSocket, 3000)
  }
  
  websocket.onerror = (error) => {
    console.error('WebSocket error:', error)
  }
}

const toggleTheme = () => {
  isDarkMode.value = !isDarkMode.value
  localStorage.setItem('theme', isDarkMode.value ? 'dark' : 'light')
}

// State
const seasons = ref([])
const selectedSeason = ref('')
const departments = ref([])
const departmentId = ref('all')
const departmentSearch = ref('')
const availableCourses = ref([])
const selectedSections = ref([]) // For MANUAL mode: [{code, section}]
const selectedCoursesAuto = ref([]) // For AUTO mode: [courseCode, ...]
const expandedCourses = ref([])
const courseSearch = ref('')
const scheduleResult = ref(null)
const error = ref('')

// Schedule Mode: 'AUTO' or 'MANUAL'
const scheduleMode = ref('AUTO')

// Loading states
const loadingSeasons = ref(false)
const loadingDepartments = ref(false)
const loadingCourses = ref(false)
const generating = ref(false)

// Computed
const filteredDepartments = computed(() => {
  if (!departmentSearch.value) return departments.value
  const search = departmentSearch.value.toLowerCase()
  return departments.value.filter(d => 
    d.name.toLowerCase().includes(search) || 
    d.nameEn?.toLowerCase().includes(search)
  )
})

const activeSeason = computed(() => {
  return seasons.value.find(s => s.active === 1)
})

const groupedCourses = computed(() => {
  const courseMap = new Map()
  
  availableCourses.value.forEach(course => {
    if (!courseMap.has(course.code)) {
      courseMap.set(course.code, {
        code: course.code,
        name: course.name || course.nameEn,
        nameEn: course.nameEn,
        credit: course.credit,
        ects: course.ects,
        sections: []
      })
    }
    
    const instructor = course.details && course.details.length > 0 ? course.details[0].fullName : null
    
    courseMap.get(course.code).sections.push({
      section: course.section,
      instructor: instructor,
      details: course.details || []
    })
  })
  
  courseMap.forEach(course => {
    course.sections.sort((a, b) => a.section - b.section)
  })
  
  return Array.from(courseMap.values()).sort((a, b) => a.code.localeCompare(b.code))
})

const filteredCourses = computed(() => {
  if (!courseSearch.value) return groupedCourses.value
  const search = courseSearch.value.toLowerCase().replace(/\s+/g, '')
  return groupedCourses.value.filter(c => {
    const codeNoSpace = c.code.toLowerCase().replace(/\s+/g, '')
    return codeNoSpace.includes(search) || 
           c.code.toLowerCase().includes(courseSearch.value.toLowerCase()) ||
           c.name?.toLowerCase().includes(courseSearch.value.toLowerCase())
  })
})

const canGenerate = computed(() => {
  if (!selectedSeason.value || !departmentId.value) return false
  if (scheduleMode.value === 'AUTO') {
    return selectedCoursesAuto.value.length > 0
  } else {
    return selectedSections.value.length > 0
  }
})

// Helpers
const parseTime = (timeStr) => {
  if (!timeStr) return 0
  const parts = timeStr.split(':')
  return parseInt(parts[0]) * 60 + parseInt(parts[1] || 0)
}

const dayNames = {
  'MON': 'Pazartesi', 'TUE': 'Salı', 'WED': 'Çarşamba',
  'THU': 'Perşembe', 'FRI': 'Cuma', 'SAT': 'Cumartesi', 'SUN': 'Pazar'
}

const dayShort = {
  'MON': 'Pzt', 'TUE': 'Sal', 'WED': 'Çar',
  'THU': 'Per', 'FRI': 'Cum', 'SAT': 'Cmt', 'SUN': 'Paz'
}

const getDayTurkish = (day) => dayNames[day] || day
const getDayShort = (day) => dayShort[day] || day

// Type helpers for detailed view - flexible matching
const getTypeTurkishFull = (type) => {
  const t = (type || '').toUpperCase()
  if (t.includes('LEC') || t.includes('LECTURE') || t.includes('TEO') || t.includes('THEORY')) return 'Teorik Ders'
  if (t.includes('LAB') || t.includes('LABOR')) return 'Laboratuvar'
  if (t.includes('PS') || t.includes('PROBLEM') || t.includes('RECIT')) return 'Problem Çözümü'
  return type || 'Diğer'
}

const getTypeIcon = (type) => {
  const t = (type || '').toUpperCase()
  if (t.includes('LEC') || t.includes('LECTURE') || t.includes('TEO') || t.includes('THEORY')) return '📚'
  if (t.includes('LAB') || t.includes('LABOR')) return '🔬'
  if (t.includes('PS') || t.includes('PROBLEM') || t.includes('RECIT')) return '✏️'
  return '📋'
}

const getDetailTypeClass = (type) => {
  const t = (type || '').toUpperCase()
  if (t.includes('LEC') || t.includes('LECTURE') || t.includes('TEO') || t.includes('THEORY')) return 'type-lecture'
  if (t.includes('LAB') || t.includes('LABOR')) return 'type-lab'
  if (t.includes('PS') || t.includes('PROBLEM') || t.includes('RECIT')) return 'type-ps'
  return 'type-other'
}

const formatInstructorShort = (name) => {
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

// Conflicts
const conflicts = computed(() => {
  const result = []
  const selectedDetails = []
  
  selectedSections.value.forEach(sel => {
    const course = groupedCourses.value.find(c => c.code === sel.code)
    if (course) {
      const section = course.sections.find(s => s.section === sel.section)
      if (section && section.details) {
        section.details.forEach(detail => {
          selectedDetails.push({
            code: sel.code,
            section: sel.section,
            day: detail.day,
            startHour: detail.startHour,
            endHour: detail.endHour
          })
        })
      }
    }
  })
  
  for (let i = 0; i < selectedDetails.length; i++) {
    for (let j = i + 1; j < selectedDetails.length; j++) {
      const d1 = selectedDetails[i]
      const d2 = selectedDetails[j]
      
      if (d1.code !== d2.code && d1.day === d2.day) {
        const start1 = parseTime(d1.startHour)
        const end1 = parseTime(d1.endHour)
        const start2 = parseTime(d2.startHour)
        const end2 = parseTime(d2.endHour)
        
        if (start1 < end2 && start2 < end1) {
          const conflictKey = [d1.code, d2.code].sort().join('-') + '-' + d1.day
          if (!result.find(r => r.key === conflictKey)) {
            result.push({
              key: conflictKey,
              course1: `${d1.code}`,
              course2: `${d2.code}`,
              day: dayNames[d1.day] || d1.day
            })
          }
        }
      }
    }
  }
  
  return result
})

const isInConflict = (code) => {
  return conflicts.value.some(c => c.course1 === code || c.course2 === code)
}

// Methods
const loadSeasons = async () => {
  loadingSeasons.value = true
  try {
    seasons.value = await courseApi.getSeasons()
    const active = seasons.value.find(s => s.active === 1)
    if (active) selectedSeason.value = active.id
  } catch (e) {
    error.value = 'Dönemler yüklenirken hata: ' + e.message
  } finally {
    loadingSeasons.value = false
  }
}

const loadDepartments = async () => {
  loadingDepartments.value = true
  try {
    departments.value = await courseApi.getDepartments()
    departments.value.sort((a, b) => a.name.localeCompare(b.name, 'tr'))
  } catch (e) {
    error.value = 'Bölümler yüklenirken hata: ' + e.message
  } finally {
    loadingDepartments.value = false
  }
}

const onDepartmentChange = () => {
  availableCourses.value = []
  selectedSections.value = []
  expandedCourses.value = []
  scheduleResult.value = null
}

const loadCourses = async () => {
  if (!selectedSeason.value || !departmentId.value) return
  loadingCourses.value = true
  error.value = ''
  try {
    availableCourses.value = await courseApi.getCourses(selectedSeason.value, departmentId.value)
  } catch (e) {
    error.value = 'Dersler yüklenirken hata: ' + e.message
  } finally {
    loadingCourses.value = false
  }
}

const toggleCourseExpand = (code) => {
  const idx = expandedCourses.value.indexOf(code)
  if (idx === -1) expandedCourses.value.push(code)
  else expandedCourses.value.splice(idx, 1)
}

const isSelected = (code, section) => {
  return selectedSections.value.some(s => s.code === code && s.section === section)
}

const toggleSection = (code, section) => {
  const idx = selectedSections.value.findIndex(s => s.code === code && s.section === section)
  if (idx === -1) {
    selectedSections.value = selectedSections.value.filter(s => s.code !== code)
    selectedSections.value.push({ code, section })
  } else {
    selectedSections.value.splice(idx, 1)
  }
}

const removeSection = (code, section) => {
  const idx = selectedSections.value.findIndex(s => s.code === code && s.section === section)
  if (idx !== -1) selectedSections.value.splice(idx, 1)
}

// AUTO mode functions
const isCourseSelectedAuto = (code) => {
  return selectedCoursesAuto.value.includes(code)
}

const toggleCourseAuto = (code) => {
  const idx = selectedCoursesAuto.value.indexOf(code)
  if (idx === -1) {
    selectedCoursesAuto.value.push(code)
  } else {
    selectedCoursesAuto.value.splice(idx, 1)
  }
}

const removeCourseAuto = (code) => {
  const idx = selectedCoursesAuto.value.indexOf(code)
  if (idx !== -1) selectedCoursesAuto.value.splice(idx, 1)
}

const generateSchedule = async () => {
  if (!canGenerate.value) return
  generating.value = true
  error.value = ''
  scheduleResult.value = null
  mobileMenuOpen.value = false
  
  try {
    const deptId = departmentId.value === 'all' ? 0 : Number(departmentId.value)
    
    let requestData = {
      seasonId: selectedSeason.value,
      departmentId: deptId,
      mode: scheduleMode.value
    }
    
    if (scheduleMode.value === 'AUTO') {
      // Auto mode: send course codes, backend finds best sections
      requestData.courseCodes = [...selectedCoursesAuto.value]
    } else {
      // Manual mode: send specific sections
      requestData.courseCodes = [...new Set(selectedSections.value.map(s => s.code))]
      requestData.selectedSections = selectedSections.value.map(s => ({
        code: s.code,
        section: String(s.section)
      }))
    }
    
    const result = await courseApi.generateScheduleAdvanced(requestData)
    
    if (result.success) scheduleResult.value = result
    else error.value = result.message
  } catch (e) {
    error.value = 'Program oluşturulurken hata: ' + e.message
  } finally {
    generating.value = false
  }
}

// Lifecycle
onMounted(() => {
  const savedTheme = localStorage.getItem('theme')
  isDarkMode.value = savedTheme !== 'light'
  loadSeasons()
  loadDepartments()
  connectWebSocket()
})

onUnmounted(() => {
  if (websocket) {
    websocket.close()
  }
})
</script>

<style scoped>
/* ===== DARK THEME ===== */
.dark-theme {
  --bg-primary: #0f172a;
  --bg-secondary: #1e293b;
  --bg-tertiary: #334155;
  --text-primary: #f8fafc;
  --text-secondary: #94a3b8;
  --border-color: #334155;
  
  --season-bg: linear-gradient(135deg, #6366f1, #8b5cf6);
  --season-text: #ffffff;
  --badge-bg: rgba(99, 102, 241, 0.3);
  --badge-text: #a5b4fc;
  
  --btn-primary: linear-gradient(135deg, #6366f1, #8b5cf6);
  --btn-success: linear-gradient(135deg, #10b981, #14b8a6);
  
  --stat-blue: linear-gradient(135deg, #3b82f6, #2563eb);
  --stat-purple: linear-gradient(135deg, #8b5cf6, #7c3aed);
  --stat-green: linear-gradient(135deg, #10b981, #059669);
  --stat-orange: linear-gradient(135deg, #f97316, #ea580c);
  
  --tag-selected: linear-gradient(135deg, #6366f1, #8b5cf6);
  --tag-conflict: linear-gradient(135deg, #ef4444, #dc2626);
  --conflict-bg: rgba(239, 68, 68, 0.15);
  --conflict-border: rgba(239, 68, 68, 0.4);
  --conflict-text: #fca5a5;
  
  --error-bg: rgba(239, 68, 68, 0.15);
  --error-border: rgba(239, 68, 68, 0.4);
  --error-text: #fca5a5;
  
  --empty-icon-bg: linear-gradient(135deg, rgba(99, 102, 241, 0.2), rgba(139, 92, 246, 0.2));
  --empty-icon-color: #818cf8;
  
  --course-item-hover: rgba(99, 102, 241, 0.1);
  --section-selected: rgba(99, 102, 241, 0.15);
  --checkbox-border: #64748b;
  --checkbox-checked: linear-gradient(135deg, #6366f1, #8b5cf6);
}

/* ===== LIGHT THEME ===== */
.light-theme {
  --bg-primary: #f1f5f9;
  --bg-secondary: #ffffff;
  --bg-tertiary: #e2e8f0;
  --text-primary: #1e293b;
  --text-secondary: #64748b;
  --border-color: #cbd5e1;
  
  --season-bg: linear-gradient(135deg, #6366f1, #8b5cf6);
  --season-text: #ffffff;
  --badge-bg: #6366f1;
  --badge-text: #ffffff;
  
  --btn-primary: linear-gradient(135deg, #6366f1, #8b5cf6);
  --btn-success: linear-gradient(135deg, #10b981, #14b8a6);
  
  --stat-blue: linear-gradient(135deg, #3b82f6, #1d4ed8);
  --stat-purple: linear-gradient(135deg, #8b5cf6, #6d28d9);
  --stat-green: linear-gradient(135deg, #10b981, #047857);
  --stat-orange: linear-gradient(135deg, #f97316, #c2410c);
  
  --tag-selected: linear-gradient(135deg, #6366f1, #8b5cf6);
  --tag-conflict: linear-gradient(135deg, #ef4444, #b91c1c);
  --conflict-bg: #fef2f2;
  --conflict-border: #fecaca;
  --conflict-text: #b91c1c;
  
  --error-bg: #fef2f2;
  --error-border: #fecaca;
  --error-text: #b91c1c;
  
  --empty-icon-bg: linear-gradient(135deg, #e0e7ff, #ddd6fe);
  --empty-icon-color: #6366f1;
  
  --course-item-hover: rgba(99, 102, 241, 0.08);
  --section-selected: rgba(99, 102, 241, 0.12);
  --checkbox-border: #94a3b8;
  --checkbox-checked: linear-gradient(135deg, #6366f1, #8b5cf6);
}

/* ===== BASE STYLES ===== */
.app-bg { background-color: var(--bg-primary); }
.sidebar-bg { background-color: var(--bg-secondary); }
.header-bg { background-color: var(--bg-secondary); }
.card-bg { background-color: var(--bg-secondary); border: 1px solid var(--border-color); }
.input-bg { background-color: var(--bg-tertiary); border: 1px solid var(--border-color); }
.text-primary { color: var(--text-primary); }
.text-secondary { color: var(--text-secondary); }
.border-color { border-color: var(--border-color); }
.placeholder-secondary::placeholder { color: var(--text-secondary); opacity: 0.7; }

.toggle-btn { 
  background-color: var(--bg-tertiary); 
  border: 1px solid var(--border-color);
}

/* Active Users Badge */
.active-users-badge {
  background: rgba(34, 197, 94, 0.15);
  color: #22c55e;
  border: 1px solid rgba(34, 197, 94, 0.3);
}

.dark-theme .active-users-badge {
  background: rgba(34, 197, 94, 0.15);
  color: #4ade80;
  border: 1px solid rgba(34, 197, 94, 0.25);
}

.light-theme .active-users-badge {
  background: rgba(34, 197, 94, 0.1);
  color: #16a34a;
  border: 1px solid rgba(34, 197, 94, 0.3);
}

/* Season Badge */
.season-badge {
  background: var(--season-bg);
  color: var(--season-text);
}

.season-card {
  background: var(--season-bg);
  color: var(--season-text);
}

/* Buttons */
.btn-primary {
  background: var(--btn-primary);
  box-shadow: 0 4px 14px rgba(99, 102, 241, 0.4);
}

.btn-success {
  background: var(--btn-success);
  box-shadow: 0 4px 14px rgba(16, 185, 129, 0.4);
}

/* Stats */
.stat-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 0.75rem 1.25rem;
  border-radius: 1rem;
  color: white;
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
}

.stat-blue { background: var(--stat-blue); }
.stat-purple { background: var(--stat-purple); }
.stat-green { background: var(--stat-green); }
.stat-orange { background: var(--stat-orange); }

/* Course Items */
.course-item {
  background-color: var(--bg-secondary);
  border: 1px solid var(--border-color);
}
.course-item:hover {
  background-color: var(--course-item-hover);
}

.section-list {
  background-color: var(--bg-tertiary);
}

.section-item:hover {
  background-color: var(--course-item-hover);
}
.section-item.selected {
  background-color: var(--section-selected);
}

.checkbox {
  border-color: var(--checkbox-border);
}
.checkbox.checked {
  background: var(--checkbox-checked);
  border-color: transparent;
}

/* Mode Toggle */
.mode-toggle-container {
  position: relative;
  display: flex;
  background: var(--bg-tertiary);
  border-radius: 12px;
  padding: 4px;
  border: 1px solid var(--border-color);
}

.mode-toggle-slider {
  position: absolute;
  top: 4px;
  left: 4px;
  width: calc(50% - 4px);
  height: calc(100% - 8px);
  border-radius: 8px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  z-index: 0;
}

.mode-toggle-slider.slide-left {
  transform: translateX(0);
  background: linear-gradient(135deg, #6366f1, #8b5cf6);
  box-shadow: 0 4px 12px rgba(99, 102, 241, 0.4);
}

.mode-toggle-slider.slide-right {
  transform: translateX(100%);
  background: linear-gradient(135deg, #10b981, #14b8a6);
  box-shadow: 0 4px 12px rgba(16, 185, 129, 0.4);
}

.mode-toggle-btn {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  padding: 8px 12px;
  font-size: 12px;
  font-weight: 700;
  color: var(--text-secondary);
  background: transparent;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  position: relative;
  z-index: 1;
  transition: all 0.3s ease;
}

.mode-toggle-btn:hover:not(.active) {
  color: var(--text-primary);
}

.mode-toggle-btn.active {
  color: white;
}

.mode-icon {
  font-size: 14px;
  transition: transform 0.3s ease;
}

.mode-toggle-btn.active .mode-icon {
  animation: bounce-icon 0.4s ease;
}

@keyframes bounce-icon {
  0%, 100% { transform: scale(1); }
  50% { transform: scale(1.3); }
}

/* Badges & Tags */
.badge-count {
  background: var(--badge-bg);
  color: var(--badge-text);
  font-weight: 700;
}

.badge-section {
  background: var(--badge-bg);
  color: var(--badge-text);
}

.tag-selected {
  background: var(--tag-selected);
  color: white;
}

.tag-conflict {
  background: var(--tag-conflict);
  color: white;
}

.course-tag {
  background: var(--bg-tertiary);
  color: var(--text-secondary);
  border: 1px solid var(--border-color);
}

/* Warnings & Errors */
.conflict-warning {
  background: var(--conflict-bg);
  border: 1px solid var(--conflict-border);
  color: var(--conflict-text);
}

.error-box {
  background: var(--error-bg);
  border: 1px solid var(--error-border);
  color: var(--error-text);
}

/* Empty State */
.empty-icon {
  background: var(--empty-icon-bg);
  color: var(--empty-icon-color);
}

/* Generating Animation */
.generating-animation {
  position: relative;
}

.calendar-container {
  position: relative;
  width: 160px;
  height: 160px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.calendar-icon {
  width: 100px;
  height: 100px;
  border-radius: 24px;
  background: linear-gradient(135deg, var(--accent-purple), var(--accent-blue));
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  animation: calendar-pulse 2s ease-in-out infinite;
  box-shadow: 0 10px 40px rgba(99, 102, 241, 0.4);
}

@keyframes calendar-pulse {
  0%, 100% { 
    transform: scale(1) rotate(0deg); 
    box-shadow: 0 10px 40px rgba(99, 102, 241, 0.4);
  }
  50% { 
    transform: scale(1.1) rotate(3deg); 
    box-shadow: 0 20px 60px rgba(99, 102, 241, 0.6);
  }
}

.floating-element {
  position: absolute;
  font-size: 24px;
  animation: float-around 3s ease-in-out infinite;
}

.el-1 { top: 0; left: 20%; animation-delay: 0s; }
.el-2 { top: 20%; right: 0; animation-delay: 0.5s; }
.el-3 { bottom: 0; left: 30%; animation-delay: 1s; }
.el-4 { top: 40%; left: 0; animation-delay: 1.5s; }

@keyframes float-around {
  0%, 100% { 
    transform: translateY(0) scale(1); 
    opacity: 0.7;
  }
  50% { 
    transform: translateY(-15px) scale(1.2); 
    opacity: 1;
  }
}

.progress-container {
  width: 200px;
}

.progress-bar {
  height: 6px;
  background: var(--border-color);
  border-radius: 3px;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, var(--accent-purple), var(--accent-blue), var(--accent-purple));
  background-size: 200% 100%;
  animation: progress-move 1.5s ease-in-out infinite;
  border-radius: 3px;
}

@keyframes progress-move {
  0% { 
    width: 0%; 
    background-position: 0% 0%;
  }
  50% { 
    width: 70%; 
    background-position: 100% 0%;
  }
  100% { 
    width: 100%; 
    background-position: 0% 0%;
  }
}

.loading-dot {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--accent-purple), var(--accent-blue));
  animation: dot-bounce 1.4s ease-in-out infinite both;
}

@keyframes dot-bounce {
  0%, 80%, 100% { 
    transform: scale(0.6);
    opacity: 0.5;
  }
  40% { 
    transform: scale(1);
    opacity: 1;
  }
}

/* Course Detail Cards */
.course-detail-card {
  background: var(--bg-secondary);
  border: 1px solid var(--border-color);
  box-shadow: 0 4px 20px rgba(0,0,0,0.1);
  transition: all 0.3s ease;
}

.course-detail-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 30px rgba(0,0,0,0.15);
}

.course-detail-header {
  background: linear-gradient(135deg, #6366f1 0%, #8b5cf6 50%, #a855f7 100%);
}

.course-detail-card:nth-child(3n+2) .course-detail-header {
  background: linear-gradient(135deg, #3b82f6 0%, #2563eb 50%, #1d4ed8 100%);
}

.course-detail-card:nth-child(3n+3) .course-detail-header {
  background: linear-gradient(135deg, #10b981 0%, #059669 50%, #047857 100%);
}

.credit-badge {
  background: var(--bg-secondary);
  color: var(--text-secondary);
  border: 1px solid var(--border-color);
}

.detail-row {
  border: 2px solid transparent;
  transition: all 0.2s ease;
}

.detail-row:hover {
  transform: scale(1.02);
}

.detail-row.type-lecture {
  background: linear-gradient(135deg, rgba(59, 130, 246, 0.2), rgba(29, 78, 216, 0.15));
  border-color: rgba(59, 130, 246, 0.5);
  color: var(--text-primary);
}

.detail-row.type-lab {
  background: linear-gradient(135deg, rgba(16, 185, 129, 0.2), rgba(4, 120, 87, 0.15));
  border-color: rgba(16, 185, 129, 0.5);
  color: var(--text-primary);
}

.detail-row.type-ps {
  background: linear-gradient(135deg, rgba(139, 92, 246, 0.2), rgba(109, 40, 217, 0.15));
  border-color: rgba(139, 92, 246, 0.5);
  color: var(--text-primary);
}

.detail-row.type-other {
  background: linear-gradient(135deg, rgba(249, 115, 22, 0.2), rgba(194, 65, 12, 0.15));
  border-color: rgba(249, 115, 22, 0.5);
  color: var(--text-primary);
}

/* ===== ANIMATIONS ===== */
@keyframes fade-in {
  from { opacity: 0; }
  to { opacity: 1; }
}

@keyframes slide-up {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

@keyframes slide-down {
  from { opacity: 0; transform: translateY(-10px); }
  to { opacity: 1; transform: translateY(0); }
}

@keyframes expand {
  from { opacity: 0; max-height: 0; }
  to { opacity: 1; max-height: 500px; }
}

@keyframes pop {
  0% { transform: scale(0.8); opacity: 0; }
  50% { transform: scale(1.05); }
  100% { transform: scale(1); opacity: 1; }
}

@keyframes shake {
  0%, 100% { transform: translateX(0); }
  20%, 60% { transform: translateX(-4px); }
  40%, 80% { transform: translateX(4px); }
}

@keyframes check {
  from { transform: scale(0); }
  to { transform: scale(1); }
}

@keyframes float {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-10px); }
}

.animate-fade-in { animation: fade-in 0.3s ease-out forwards; }
.animate-slide-up { animation: slide-up 0.4s ease-out forwards; opacity: 0; }
.animate-slide-down { animation: slide-down 0.4s ease-out forwards; }
.animate-expand { animation: expand 0.3s ease-out forwards; }
.animate-pop { animation: pop 0.3s ease-out forwards; }
.animate-shake { animation: shake 0.4s ease-out; }
.animate-check { animation: check 0.2s ease-out forwards; }
.animate-float { animation: float 3s ease-in-out infinite; }

/* ===== SCROLLBAR ===== */
::-webkit-scrollbar { width: 6px; height: 6px; }
::-webkit-scrollbar-track { background: transparent; }
::-webkit-scrollbar-thumb { background: var(--border-color); border-radius: 3px; }
::-webkit-scrollbar-thumb:hover { background: var(--text-secondary); }
</style>
