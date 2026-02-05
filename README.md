<div align="center">

# 📚 Ders Programı Planlayıcı

### Yeditepe Üniversitesi için Akıllı Ders Çizelgesi Oluşturucu

[![Java](https://img.shields.io/badge/Java-11-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-2.7-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Vue.js](https://img.shields.io/badge/Vue.js-3-4FC08D?style=for-the-badge&logo=vue.js&logoColor=white)](https://vuejs.org/)
[![Tailwind CSS](https://img.shields.io/badge/Tailwind_CSS-3-38B2AC?style=for-the-badge&logo=tailwind-css&logoColor=white)](https://tailwindcss.com/)
[![Docker](https://img.shields.io/badge/Docker-Ready-2496ED?style=for-the-badge&logo=docker&logoColor=white)](https://www.docker.com/)

<br>

<img src="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Objects/Calendar.png" alt="Calendar" width="150" />

**Çakışmasız, optimal ders programını saniyeler içinde oluşturun!**

[🚀 Demo](#demo) • [✨ Özellikler](#-özellikler) • [🛠️ Kurulum](#️-kurulum) • [☁️ Deploy](#️-ücretsiz-deploy)

</div>

---

## 🎯 Ne İşe Yarar?

Bu uygulama, Yeditepe Üniversitesi öğrencilerinin ders programı oluşturma sürecini **otomatikleştirir**. Manuel olarak saatlerce uğraşmak yerine:

```
1️⃣ Derslerini seç
2️⃣ "Program Oluştur" tıkla  
3️⃣ En optimal program hazır! ✅
```

---

## ✨ Özellikler

<table>
<tr>
<td width="50%">

### 🔐 Otomatik Yetkilendirme
Backend otomatik olarak Yeditepe API'sine bağlanır, token yönetimini yapar

### 📅 Dönem Seçimi
Aktif ve geçmiş dönemler arasından kolayca seçim

### 🔍 Akıllı Arama
Bölüm derslerini listele, filtrele ve ara

</td>
<td width="50%">

### ⚡ Çakışma Kontrolü
Seçilen dersler arasında çakışma analizi

### 🎯 Optimal Algoritma
En az boşluklu, en verimli programı otomatik seç

### 📊 Görsel Program
Haftalık görünümde şık tasarım

</td>
</tr>
</table>

---

## 🧠 Akıllı Algoritma

Program oluşturulurken şu kriterlere göre **en iyi kombinasyon** seçilir:

| Öncelik | Kriter | Açıklama |
|:-------:|--------|----------|
| 🥇 | **Çakışma Yok** | Dersler arasında zaman çakışması olmamalı |
| 🥈 | **Az Boşluk** | Dersler arası boş saatleri minimize et |
| 🥉 | **Az Gün** | Dersleri mümkün olduğunca az güne sığdır |
| 🏅 | **Geç Başla** | Sabah 08:00 derslerinden kaçın 😴 |

---

## 🛠️ Kurulum

### Gereksinimler

```
📦 Java 11+
📦 Node.js 18+
📦 Maven 3.8+
```

### ⚡ Hızlı Başlangıç

<details>
<summary><b>🖥️ Backend</b></summary>

```bash
cd backend
./mvnw spring-boot:run
```

> 🌐 Backend: `http://localhost:8080`

</details>

<details>
<summary><b>🎨 Frontend</b></summary>

```bash
cd frontend
npm install
npm run dev
```

> 🌐 Frontend: `http://localhost:5173`

</details>

---

## ☁️ Ücretsiz Deploy

<div align="center">

### 🚀 Render.com ile Tek Tıkla Deploy

[![Deploy to Render](https://render.com/images/deploy-to-render-button.svg)](https://render.com/deploy)

</div>

### 📝 Adım Adım

<details>
<summary><b>1️⃣ GitHub'a Yükle</b></summary>

```bash
git init
git add .
git commit -m "Initial commit"
git remote add origin https://github.com/KULLANICI/course-selector.git
git push -u origin main
```

</details>

<details>
<summary><b>2️⃣ Render.com'da Deploy</b></summary>

1. [render.com](https://render.com) → GitHub ile giriş
2. **"New +"** → **"Web Service"**
3. Repoyu seç: `course-selector`
4. Ayarlar:
   - **Runtime**: `Docker`
   - **Plan**: `Free`
5. **Environment Variables**:
   ```
   YEDITEPE_USERNAME = U20xxxxxxxxx
   YEDITEPE_PASSWORD = ************
   ```
6. **"Create Web Service"** 🎉

</details>

<details>
<summary><b>3️⃣ Bekle & Paylaş</b></summary>

⏱️ İlk deploy **5-10 dakika** sürer

✅ Tamamlandığında URL'in hazır:
```
https://course-selector-xxxx.onrender.com
```

📤 Bu linki istediğin kişiyle paylaş!

</details>

### ⚠️ Ücretsiz Plan Hakkında

| | |
|---|---|
| 💤 | 15 dk kullanılmazsa uyku moduna geçer |
| ⏰ | İlk istekte ~30 saniye uyanma süresi |
| ✅ | Aylık 750 saat ücretsiz (yeterli!) |

---

## 🔌 API Endpoints

```http
GET  /api/seasons                           # Dönemleri listele
GET  /api/departments                       # Bölümleri listele  
GET  /api/courses/{seasonId}/{departmentId} # Dersleri getir
POST /api/schedule/generate                 # Program oluştur
```

<details>
<summary><b>📤 Örnek Request</b></summary>

```json
POST /api/schedule/generate

{
  "seasonId": 139,
  "departmentId": 11172,
  "courseCodes": ["CSE 311", "CSE 344", "CSE 348"]
}
```

</details>

---

## 🏫 Bazı Bölüm ID'leri

| Bölüm | ID |
|-------|---:|
| Bilgisayar Mühendisliği | `11172` |
| Elektrik-Elektronik Müh. | `11171` |
| Endüstri Mühendisliği | `11173` |

---

## 📸 Ekran Görüntüleri

<div align="center">

| Ders Seçimi | Haftalık Program |
|:-----------:|:----------------:|
| ![Ders Seçimi](https://via.placeholder.com/400x250/4FC08D/FFFFFF?text=Ders+Secimi) | ![Program](https://via.placeholder.com/400x250/6DB33F/FFFFFF?text=Haftalik+Program) |

</div>

---

## ⚠️ Notlar

> [!NOTE]
> Bu uygulama resmi bir Yeditepe uygulaması değildir.

> [!IMPORTANT]
> Veriler Yeditepe Üniversitesi API'lerinden anlık olarak çekilir.

> [!TIP]
> Token süresi dolduğunda backend otomatik olarak yeniden login olur.

---

<div align="center">

## 🤝 Katkıda Bulun

Pull request'ler memnuniyetle karşılanır!

---

### ⭐ Beğendiysen yıldız atmayı unutma!

<br>

[![GitHub stars](https://img.shields.io/github/stars/miracckms/course-selector?style=social)](https://github.com/miracckms/course-selector)

---

**MIT License** • Made with ❤️ for Yeditepe Students

<img src="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Hand%20gestures/Waving%20Hand.png" alt="Wave" width="30" />

</div>
