<div align="center">

# FitTrack
**Intelligent Fitness Management System & Backend API**

*Penilaian Sumatif Akhir Semester (PSAS) | SMK Telkom Purwokerto | 2026/2027*

![Laravel](https://img.shields.io/badge/Laravel-FF2D20?style=for-the-badge&logo=laravel&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![MongoDB](https://img.shields.io/badge/MongoDB-47A248?style=for-the-badge&logo=mongodb&logoColor=white)
![Android](https://img.shields.io/badge/Android_Native-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![REST API](https://img.shields.io/badge/RESTful_API-005571?style=for-the-badge&logo=json&logoColor=white)

</div>

<br>

FitTrack adalah ekosistem platform manajemen kebugaran dan pelacakan aktivitas olahraga berbasis RESTful API. Sistem ini dirancang sebagai solusi teknologi untuk mendigitalisasi program kesehatan personal melalui penjadwalan latihan, analisis komposisi tubuh secara berkala, hingga integrasi asisten pintar berbasis kecerdasan buatan (AI Chat Assistant). 

Aplikasi ini mengadopsi arsitektur penanganan data hibrida (Hybrid Database) dengan memanfaatkan stabilitas relasional MySQL untuk data transaksional utama dan skalabilitas NoSQL MongoDB untuk manajemen log aktivitas serta histori obrolan asisten virtual secara real-time.

---

## 👥 1. TIM PENGEMBANG & DISTRIBUSI PERAN (ROLE MATCHING)

**Kelas** : XI PPLG 6 (Kelompok 4)  
**Mata Pelajaran** : MK 1 - B (Pengembangan Perangkat Lunak dan Gim)  

| No | Nama Tim & Role | Jobdesk |
|:--:|:---|:---|
| **03** | **Alfa Dhani Fahrezan**<br>*(Core Backend Developer)* | Membangun arsitektur server-side menggunakan framework Laravel. Mengonfigurasi core business logic, mengimplementasikan sistem keamanan autentikasi Token via Laravel Sanctum, serta menerapkan Role-Based Access Control (RBAC). |
| **04** | **Aufa Sendy Anggiansyah**<br>*(Lead Frontend Developer)* | Bertanggung jawab atas desain arsitektur antarmuka (UI/UX) sisi client. Menyusun tata letak dasbor aktivitas, kartu katalog program latihan (workout), serta visualisasi bagan grafik pelacakan komposisi tubuh pengguna. |
| **05** | **Axel Azhar Putra Ananca**<br>*(API Specialist & QA Engineer)* | Merancang spesifikasi teknis endpoints API, mendesain skema format respons JSON, menguji performa integrasi backend cerdas melalui `AiController` untuk fitur AI Chat Assistant dan personalisasi gizi harian, serta menyusun dokumentasi pengujian unit API menggunakan Postman. |
| **17** | **Jonathan Christian Boangmanalu**<br>*(Frontend Developer & UI Integrator)* | Mengembangkan komponen interaktif pada aplikasi client, mengelola manajemen state aplikasi, melakukan penanganan navigasi utama, dan memastikan responsivitas layout sesuai dengan aset desain. |
| **30** | **Noah Rey**<br>*(Database Administrator / DBA)* | Merancang pemodelan ERD, memigrasi serta mengoptimalkan skema basis data relasional MySQL untuk data transaksional utama, serta mengonfigurasi database NoSQL MongoDB Compass untuk kebutuhan penyimpanan data semi-terstruktur. |

---

## 🏗️ 2. ARSITEKTUR PLATFORM & MODEL BISNIS

Sistem FitTrack terbagi ke dalam dua segmentasi platform yang saling terhubung secara real-time melalui jalur komunikasi REST API hulu ke hilir:

* 📱 **Sisi Klien (Mobile / Web End-User):**
  Platform interaktif yang digunakan oleh pelanggan reguler untuk mengakses program latihan berdasarkan kategori, menyusun jadwal latihan mandiri, memantau chart tren berat badan/lemak tubuh, melacak daily streak, serta melakukan sesi konsultasi pemenuhan target nutrisi harian bersama AI.

* 💻 **Sisi Manajemen (Dashboard Admin):**
  Sistem kontrol internal khusus bagi administrator untuk melakukan manajemen katalog CRUD (Create, Read, Update, Delete) menu olahraga, kontrol data user terdaftar, regulasi tantangan lencana (achievement), serta memantau ringkasan analitik kebugaran pengguna secara makro.

---

## ✨ 3. SPESIFIKASI FITUR UTAMA SISTEM

* 🔐 **[Autentikasi Aman & RBAC]**
  Registrasi dan login terenkripsi menggunakan token Laravel Sanctum. Pembatasan hak akses ketat antara admin dan user via middleware `IsAdmin`.
* 🏋️ **[Katalog Olahraga Dinamis]**
  Katalog workout terklasifikasi berdasarkan kategori tertentu (Kardio, Angkat Beban) lengkap dengan data durasi dan estimasi pembakaran kalori harian.
* 📅 **[Penjadwal & Pengingat Latihan]**
  Manajemen tabel data `workout_schedules` untuk menyusun agenda olahraga mingguan yang terintegrasi dengan push notification.
* 📊 **[Analitik Komposisi Fisik]**
  Modul pelacakan metrik kebugaran berkala (berat badan, tinggi badan, kadar lemak) yang dirangkum otomatis pada endpoint `GET /analytics/summary`.
* 🤖 **[AI Personalization Hub]**
  Konsultasi berbasis NLP (Natural Language Processing) melalui asisten pintar untuk memformulasikan rekomendasi menu olahraga dan target pemenuhan nutrisi makro (makronutrien).
* 🎯 **[Sistem Gamifikasi]**
  Retensi pengguna didukung dengan fitur pemantauan Daily Streak berturut-turut, pencapaian lencana (badges), serta akumulasi poin loyalitas yang tercatat pada log riwayat poin.

---

## 🛠️ 4. SPESIFIKASI TECH STACK

### A. BACKEND ENGINE & CORE SERVER
- **Framework Utama** : Laravel (PHP 13)
- **RDBMS** : MySQL (Data Relasional Transaksional)
- **Protokol API** : RESTful API Architecture (JSON Data Interchange)
- **Driver Keamanan** : Laravel Sanctum API Token Guard

### B. CLIENT-SIDE ARCHITECTURE
- **Lingkungan Code** : Java / Kotlin / JavaScript
- **UI Engine** : Android Native (Jetpack Compose)
- **HTTP Client** : Retrofit Client
- **Session Storage** : -

### C. STRUKTUR ENTITAS BASIS DATA (MYSQL SCHEMA RELATIONAL)
- `users` : Kredensial, level role, tier akun, dan saldo poin.
- `workouts` : Master data tipe latihan dan nilai kalori.
- `categories` : Entitas klasifikasi rumpun latihan fisik.
- `workout_schedules` : Tabel relasional penjadwalan aktivitas user.
- `workout_histories` : Log rekam jejak aktivitas olahraga yang selesai.
- `progress_entries` : Pelacakan histori metrik komposisi tubuh.
- `ai_personalizations` : Blueprint target nutrisi harian hasil kalkulasi AI.
- `achievements` : Master data tantangan, ikon lencana, & reward.
- `user_achievements` : Tabel pivot klaim pencapaian tantangan user.
- `reviews`, `favorites`, `notifications`, `point_histories` : Sub-tabel penunjang.

---

## 🚀 5. PETUNJUK DEPLOYMENT & INSTALASI BACKEND (RINGKAS)

1. Clone repositori backend:
   ```bash
   git clone [https://github.com/ALFADHANI284/FitTrack_Laravel](https://github.com/ALFADHANI284/FitTrack_Laravel)
2. Instalasi dependensi composer:
   composer install
3. Salin berkas lingkungan (.env) & atur konfigurasi database MySQL
   cp .env.example .env
4. Generate application key:
   php artisan key:generate
5. Jalankan migrasi basis data beserta seeder:
   php artisan migrate --seed
6. php artisan serve

## 6. DIREKTORI REPOSITORI & ARTIFAK PRODUK (SOURCE CODE & BUILDS)
A. LINK REPOSITORY (SOURCE CODE)
🔗 Backend API (Laravel) : https://github.com/ALFADHANI284/FitTrack_Laravel

B. LINK PRODUCTION ARTIFACTS (HASIL BUILD)
🔗 Mobile App : https://fittracklaravel-production.up.railway.app/api/

© 2026 Kelompok 4 - XI PPLG 6 - FitTrack
