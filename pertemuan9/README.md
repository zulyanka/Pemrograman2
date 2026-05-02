# 🏪 POS App - Sistem Penjualan Sederhana

**Java Swing + MySQL | MVC Pattern**

```
NAMA    : ZULYAN WIDYAKA KRISNA
NIM     : 231011403446
KELAS   : 06TPLE016
```

\---

## 📁 Struktur Folder

```
pos-app/
├── .vscode/
│   ├── settings.json       # Konfigurasi Java project VS Code
│   └── tasks.json          # Task compile \& run (Ctrl+Shift+B)
├── src/
│   ├── Main.java                           # Entry point
│   ├── config/
│   │   └── DatabaseConfig.java            # Singleton JDBC connection
│   ├── model/
│   │   ├── Barang.java
│   │   ├── Customer.java
│   │   ├── Supplier.java
│   │   ├── Transaksi.java
│   │   └── DetailTransaksi.java
│   ├── controller/
│   │   ├── BarangController.java          # CRUD barang + stok
│   │   ├── CustomerController.java        # CRUD customer
│   │   ├── SupplierController.java        # CRUD supplier
│   │   └── TransaksiController.java       # Transaksi + laporan
│   └── view/
│       ├── UIHelper.java                  # Utility style \& format
│       ├── MainFrame.java                 # Frame utama (sidebar nav)
│       ├── FormBarang.java                # CRUD barang
│       ├── FormCustomer.java              # CRUD customer
│       ├── FormSupplier.java              # CRUD supplier
│       ├── FormTransaksi.java             # POS / kasir
│       ├── LaporanTransaksi.java          # Laporan penjualan
│       └── LaporanStok.java               # Laporan stok
├── lib/
│   └── mysql-connector-j-9.6.0.jar       # Driver MySQL JDBC
├── out/                                   # Hasil kompilasi (.class)
├── database.sql                           # Script DDL + dummy data
├── run.bat                                # Jalankan di Windows
├── run.sh                                 # Jalankan di Linux/macOS
└── build-jar.bat                          # Build file .jar
```

\---

## ⚙️ Persiapan Awal

### 1\. Install Dependencies

* JDK 11+ → https://adoptium.net
* MySQL Server → https://dev.mysql.com/downloads/mysql/
* VS Code + Extension: **Extension Pack for Java** (Microsoft)

### 2\. Setup Database

Buka MySQL Workbench / phpMyAdmin / terminal MySQL, lalu jalankan:

```sql
source /path/ke/pos-app/database.sql
```

Atau copy-paste isi `database.sql` ke query editor.

### 3\. Konfigurasi Koneksi

Edit file `src/config/DatabaseConfig.java`:

```java
private static final String USER     = "root";      // username MySQL kamu
private static final String PASSWORD = "";           // password MySQL kamu
```

\---

## ▶️ Cara Menjalankan di VS Code

### Cara 1 — Menggunakan Tasks (Recommended)

1. Buka folder `pos-app` di VS Code
2. Tekan **`Ctrl + Shift + B`** → pilih **"1. Compile POS App"**
3. Tekan **`Ctrl + Shift + B`** lagi → pilih **"2. Run POS App"**

### Cara 2 — Menggunakan Script

**Windows:**

```
Double-click run.bat
```

**Linux/macOS:**

```bash
chmod +x run.sh \&\& ./run.sh
```

### Cara 3 — Manual di Terminal

```bash
# Buat folder output
mkdir out

# Compile (Windows pakai ; sebagai separator, Linux/macOS pakai :)
javac -cp ".;lib/mysql-connector-j-9.6.0.jar" -d out src/config/\*.java src/model/\*.java src/controller/\*.java src/view/\*.java src/Main.java

# Jalankan
java -cp ".;out;lib/mysql-connector-j-9.6.0.jar" Main
```

\---

## 📦 Build JAR

```bash
# Windows
build-jar.bat

# Setelah selesai, jalankan dengan:
java -jar pos-app.jar
```

\---

## 🔧 Fitur Aplikasi

|Fitur|Keterangan|
|-|-|
|🛒 Transaksi Penjualan|Pilih barang, qty, hitung total, bayar, simpan|
|📦 Manajemen Barang|CRUD barang + search + filter supplier|
|👤 Manajemen Customer|CRUD data pelanggan|
|🏭 Manajemen Supplier|CRUD data pemasok|
|📊 Laporan Transaksi|Filter by tanggal, lihat detail per nota|
|📋 Laporan Stok|Color-coded status stok (aman/kritis/habis)|
|🔒 PreparedStatement|Anti SQL injection di semua query|
|💾 DB Transaction|Rollback otomatis jika stok tidak cukup|

\---

## 🐛 Troubleshooting

**Error: Communications link failure**
→ MySQL belum berjalan. Start MySQL service.

**Error: Access denied for user 'root'**
→ Password salah di `DatabaseConfig.java`

**Error: Unknown database 'db\_pos\_app'**
→ Jalankan `database.sql` terlebih dahulu

**Error: ClassNotFoundException: com.mysql.cj.jdbc.Driver**
→ JAR connector tidak ada di folder `lib/` atau classpath salah

