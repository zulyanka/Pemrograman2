# LoginApp — JSP Session \& Cookies

Pemrograman 2 | Pertemuan 10

```
NAMA    : ZULYAN WIDYAKA KRISNA
NIM     : 231011403446
KELAS   : 06TPLE016
```

## Struktur Project

```
LoginApp/
├── pom.xml                                     ← Build config (Maven)
└── src/
    └── main/
        ├── java/
        │   └── com/example/servlet/
        │       └── LoginServlet.java            ← (OPSIONAL) Servlet alternatif
        └── webapp/
            ├── WEB-INF/
            │   └── web.xml                     ← Konfigurasi aplikasi
            ├── index.jsp                       ← Halaman form login
            ├── validasi.jsp                    ← Halaman proses \\\& hasil login
            └── logout.jsp                      ← Proses hapus session \\\& cookie
```

## Cara Menjalankan

### Menggunakan NetBeans + Apache Tomcat

1. **File → New Project → Java with Ant → Web Application** (atau Maven Web App)
2. Salin semua file sesuai struktur di atas
3. Klik kanan project → **Run**
4. Buka browser: `http://localhost:8080/LoginApp/`

### Menggunakan IntelliJ IDEA

1. **New Project → Jakarta EE → Web Application**
2. Salin semua file sesuai struktur di ataszulyan
3. Konfigurasi Tomcat di Run Configuration
4. Klik **Run**

## Kredensial Login

|Field|Value|
|-|-|
|Username|`ADMIN`|
|Password|`ADMIN`|

## Alur Aplikasi

```
index.jsp  ──(POST form)──► validasi.jsp
                               ├── Login OK  → Buat Session + Cookie → Tampil sukses
                               └── Login Gagal → Tampil pesan error

validasi.jsp ──(klik Logout)──► logout.jsp
                                    └── Hapus Session + Cookie → Redirect index.jsp

index.jsp (refresh saat sudah login) → Auto redirect ke validasi.jsp
```

## Konsep Utama

### Session

```java
// Simpan data ke session
session.setAttribute("userLogin", username);

// Set masa aktif (detik) — 86400 = 1 hari
session.setMaxInactiveInterval(60 \\\* 60 \\\* 24);

// Baca data dari session
String user = (String) session.getAttribute("userLogin");

// Hapus session
session.invalidate();
```

### Cookie

```java
// Buat cookie
Cookie myCookie = new Cookie("userLogin", username);

// Set masa aktif (detik) — 86400 = 1 hari
myCookie.setMaxAge(60 \\\* 60 \\\* 24);

// Berlaku di seluruh path aplikasi
myCookie.setPath("/");

// Kirim ke browser
response.addCookie(myCookie);

// Hapus cookie (set MaxAge = 0)
myCookie.setMaxAge(0);
response.addCookie(myCookie);
```

## Perbedaan Session vs Cookie

|Aspek|Session|Cookie|
|-|-|-|
|Disimpan di|Server (memori)|Client (browser/file)|
|Keamanan|Lebih aman|Bisa diakses browser/JS|
|Ukuran data|Tidak terbatas|Maks \~4KB per cookie|
|Masa aktif|Selama server berjalan / timeout|Bisa diset manual (detik)|
|Contoh pakai|Data login, keranjang belanja|"Ingat saya", preferensi tema|



