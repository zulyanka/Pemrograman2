-- =========================================================
-- DATABASE.SQL — Showroom Master
-- Jalankan file ini sekali saja untuk membuat database.
-- Cara: buka phpMyAdmin / MySQL Workbench → menu Import /
-- copy-paste isi file ini ke tab SQL → jalankan.
-- =========================================================

CREATE DATABASE IF NOT EXISTS showroom;
USE showroom;

-- Tabel mobil
CREATE TABLE IF NOT EXISTS mobil (
  id      INT AUTO_INCREMENT PRIMARY KEY,
  merk    VARCHAR(50)  NOT NULL,
  model   VARCHAR(100) NOT NULL,
  tahun   INT          NOT NULL,
  warna   VARCHAR(30)  NOT NULL,
  harga   BIGINT       NOT NULL,
  terjual BOOLEAN      NOT NULL DEFAULT FALSE
);

-- Tabel customer
CREATE TABLE IF NOT EXISTS customer (
  id     INT AUTO_INCREMENT PRIMARY KEY,
  nama   VARCHAR(100) NOT NULL,
  hp     VARCHAR(20)  NOT NULL,
  email  VARCHAR(100) DEFAULT '-',
  alamat VARCHAR(255) NOT NULL
);

-- Tabel transaksi (terhubung ke mobil & customer lewat foreign key)
CREATE TABLE IF NOT EXISTS transaksi (
  id          INT AUTO_INCREMENT PRIMARY KEY,
  tanggal     DATE        NOT NULL,
  mobil_id    INT         NOT NULL,
  customer_id INT         NOT NULL,
  metode      VARCHAR(30) NOT NULL,
  harga       BIGINT      NOT NULL,
  FOREIGN KEY (mobil_id)    REFERENCES mobil(id),
  FOREIGN KEY (customer_id) REFERENCES customer(id)
);

-- Contoh data awal (boleh dihapus kalau tidak perlu)
INSERT INTO mobil (merk, model, tahun, warna, harga) VALUES
  ('Toyota', 'Avanza 1.5 G', 2024, 'Hitam', 255000000),
  ('Honda',  'Brio RS',      2023, 'Putih', 243000000);

INSERT INTO customer (nama, hp, email, alamat) VALUES
  ('Budi Santoso', '081234567890', 'budi@email.com', 'Jl. Merdeka No. 1, Jakarta');
