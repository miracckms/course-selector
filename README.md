# Ders Programı Planlayıcı - Yeditepe Üniversitesi

Yeditepe Üniversitesi öğrencileri için çakışmasız ders programı oluşturucu. Bu uygulama, seçtiğiniz dersler için en optimal haftalık programı otomatik olarak oluşturur.

## Özellikler

- **Otomatik Yetkilendirme**: Backend otomatik olarak Yeditepe API'sine login olur ve token yönetimini yapar
- **Token Yenileme**: Token süresi dolduğunda otomatik olarak yeni token alır
- **Dönem Seçimi**: Aktif ve geçmiş dönemler arasından seçim yapabilirsiniz
- **Ders Arama**: Bölüm derslerini listeler ve arama yapmanızı sağlar
- **Çakışma Kontrolü**: Seçilen dersler arasında çakışma olup olmadığını kontrol eder
- **Optimal Program**: Birden fazla section varsa en az boşluklu programı seçer
- **Haftalık Görünüm**: Oluşturulan programı görsel olarak gösterir

## Teknolojiler

### Backend
- Java 11
- Spring Boot 2.7
- Maven

### Frontend
- Vue.js 3
- Vite
- Tailwind CSS
- Axios

## Kurulum

### Backend

```bash
cd backend
./mvnw spring-boot:run
```

Backend `http://localhost:8080` adresinde çalışacaktır.

**Not:** Yetkilendirme bilgileri `application.properties` dosyasında saklanır:
```properties
yeditepe.auth.username=YOUR_USERNAME
yeditepe.auth.password=YOUR_PASSWORD
```

### Frontend

```bash
cd frontend
npm install
npm run dev
```

Frontend `http://localhost:5173` adresinde çalışacaktır.

## Kullanım

1. Uygulamayı açın (`http://localhost:5173`)
2. Dönem seçin (aktif dönem otomatik seçilir)
3. Bölüm ID girin (Bilgisayar Mühendisliği: 11172)
4. "Dersleri Getir" butonuna tıklayın
5. Almak istediğiniz dersleri seçin veya manuel olarak ders kodlarını girin
6. "Program Oluştur" butonuna tıklayın
7. Sistem çakışmasız en optimal programı oluşturacaktır

## API Endpoints

### Backend API

| Endpoint | Method | Açıklama |
|----------|--------|----------|
| `/api/seasons` | GET | Akademik dönemleri listeler |
| `/api/courses/{seasonId}/{departmentId}` | GET | Döneme ait dersleri listeler |
| `/api/schedule/generate` | POST | Ders programı oluşturur |

### Schedule Generate Request Body

```json
{
  "seasonId": 139,
  "departmentId": 11172,
  "courseCodes": ["CSE 311", "CSE 344", "CSE 348", "CSE 471"]
}
```

## Optimal Program Algoritması

Sistem şu kriterlere göre en iyi programı seçer:
1. **Çakışma yok**: Seçilen dersler arasında zaman çakışması olmamalı
2. **Az boşluk**: Günler arası ve gün içi boşlukları minimize eder
3. **Az gün**: Mümkünse dersleri daha az güne sıkıştırır
4. **Geç başlangıç**: Sabah erken dersleri penalize eder

## Bölüm ID'leri

Bazı bölüm ID'leri:
- Bilgisayar Mühendisliği: 11172

## Notlar

- Bu uygulama resmi bir Yeditepe uygulaması değildir
- Veriler Yeditepe Üniversitesi API'lerinden çekilmektedir
- Veritabanı kullanılmamaktadır, tüm veriler API'den anlık olarak alınır
- Token süresi dolduğunda backend otomatik olarak yeniden login olur

## Ücretsiz Canlıya Alma (Deploy)

Bu projeyi **Render.com** üzerinde ücretsiz olarak canlıya alabilirsiniz. Frontend ve Backend tek sunucuda çalışır.

### Adım 1: GitHub'a Yükle

```bash
# Proje klasöründe
git init
git add .
git commit -m "Initial commit"

# GitHub'da yeni repo oluşturun, sonra:
git remote add origin https://github.com/KULLANICI_ADINIZ/course-selector.git
git branch -M main
git push -u origin main
```

### Adım 2: Render.com'da Deploy

1. [render.com](https://render.com) adresine gidin ve GitHub ile giriş yapın
2. Dashboard'da **"New +"** → **"Web Service"** seçin
3. GitHub reponuzu bağlayın ve `course-selector` reposunu seçin
4. Ayarlar:
   - **Name**: `course-selector` (veya istediğiniz isim)
   - **Runtime**: `Docker`
   - **Plan**: `Free`
5. **Environment Variables** bölümünde ekleyin:
   - `YEDITEPE_USERNAME` = Yeditepe öğrenci numaranız (U ile başlayan)
   - `YEDITEPE_PASSWORD` = Şifreniz
6. **"Create Web Service"** butonuna tıklayın

### Adım 3: Bekleyin

İlk deploy 5-10 dakika sürebilir. Tamamlandığında size bir URL verilecek:
`https://course-selector-xxxx.onrender.com`

### Önemli Notlar

- **Ücretsiz plan sınırlaması**: 15 dakika kullanılmazsa uygulama "uyku" moduna geçer. İlk isteğinizde ~30 saniye uyanma süresi olabilir.
- **Aylık limit**: Ücretsiz planda aylık 750 saat çalışma süresi var (yeterli).
- **URL paylaşımı**: Verilen URL'yi istediğiniz kişiyle paylaşabilirsiniz.

### Alternatif: Railway.app

Railway.com da ücretsiz deploy seçeneği sunar:

1. [railway.app](https://railway.app) adresine gidin
2. GitHub ile giriş yapın
3. **"New Project"** → **"Deploy from GitHub repo"**
4. Reponuzu seçin ve environment variable'ları ekleyin
5. Deploy otomatik başlayacaktır

## Lisans

MIT
