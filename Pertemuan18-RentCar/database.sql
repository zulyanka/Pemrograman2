-- =========================================================
-- DATABASE.SQL — ZW Rent Car (Aplikasi Penyewaan Mobil)
-- Cara pakai: buka phpMyAdmin → klik tab SQL →
-- paste seluruh isi file ini → klik Go.
-- =========================================================

CREATE DATABASE IF NOT EXISTS rentcar;
USE rentcar;

-- Tabel mobil: mobil rental punya harga sewa PER HARI dan status
CREATE TABLE IF NOT EXISTS mobil (
  id         INT AUTO_INCREMENT PRIMARY KEY,
  merk       VARCHAR(50)  NOT NULL,
  model      VARCHAR(100) NOT NULL,
  tahun      INT          NOT NULL,
  plat_nomor VARCHAR(15)  NOT NULL,
  harga_sewa BIGINT       NOT NULL,                -- tarif per hari
  status     ENUM('Tersedia','Disewa') NOT NULL DEFAULT 'Tersedia'
);

-- Tabel customer: penyewa wajib tercatat identitasnya
CREATE TABLE IF NOT EXISTS customer (
  id     INT AUTO_INCREMENT PRIMARY KEY,
  nama   VARCHAR(100) NOT NULL,
  no_ktp VARCHAR(20)  NOT NULL,
  hp     VARCHAR(20)  NOT NULL,
  alamat VARCHAR(255) NOT NULL
);

-- Tabel sewa: satu baris = satu transaksi penyewaan.
-- Kolom pengembalian (tgl_kembali_aktual & denda) diisi
-- belakangan saat mobil dikembalikan.
CREATE TABLE IF NOT EXISTS sewa (
  id                  INT AUTO_INCREMENT PRIMARY KEY,
  mobil_id            INT  NOT NULL,
  customer_id         INT  NOT NULL,
  tgl_sewa            DATE NOT NULL,
  lama_hari           INT  NOT NULL,
  tgl_kembali_rencana DATE NOT NULL,               -- tgl_sewa + lama_hari
  total_biaya         BIGINT NOT NULL,             -- lama_hari x harga_sewa
  tgl_kembali_aktual  DATE NULL,                   -- diisi saat pengembalian
  denda               BIGINT NOT NULL DEFAULT 0,   -- hari telat x harga_sewa
  status              ENUM('Disewa','Selesai') NOT NULL DEFAULT 'Disewa',
  FOREIGN KEY (mobil_id)    REFERENCES mobil(id),
  FOREIGN KEY (customer_id) REFERENCES customer(id)
);

-- Contoh data awal
INSERT INTO mobil (merk, model, tahun, plat_nomor, harga_sewa) VALUES
  ('Toyota',  'Avanza 1.5 G',   2024, 'B 1234 ZWK', 350000),
  ('Honda',   'Brio RS',        2023, 'B 5678 ZWK', 300000),
  ('Daihatsu','Terios X',       2022, 'B 9012 ZWK', 400000);

INSERT INTO customer (nama, no_ktp, hp, alamat) VALUES
  ('Budi Santoso', '3171234567890001', '081234567890', 'Jl. Merdeka No. 1, Jakarta');
