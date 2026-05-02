-- ============================================
-- POS APP - DATABASE SCHEMA
-- NAMA: ZULYAN WIDYAKA KRISNA
-- NIM : 231011403446
-- ============================================

CREATE DATABASE IF NOT EXISTS db_pos_app;
USE db_pos_app;

-- ============================================
-- TABLE: supplier
-- ============================================
CREATE TABLE IF NOT EXISTS supplier (
    id_supplier INT AUTO_INCREMENT PRIMARY KEY,
    nama_supplier VARCHAR(100) NOT NULL,
    telepon VARCHAR(20),
    alamat TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- TABLE: customer
-- ============================================
CREATE TABLE IF NOT EXISTS customer (
    id_customer INT AUTO_INCREMENT PRIMARY KEY,
    nama_customer VARCHAR(100) NOT NULL,
    telepon VARCHAR(20),
    alamat TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- TABLE: barang
-- ============================================
CREATE TABLE IF NOT EXISTS barang (
    id_barang INT AUTO_INCREMENT PRIMARY KEY,
    kode_barang VARCHAR(20) UNIQUE NOT NULL,
    nama_barang VARCHAR(100) NOT NULL,
    kategori VARCHAR(50),
    harga_beli DECIMAL(15, 2) NOT NULL DEFAULT 0,
    harga_jual DECIMAL(15, 2) NOT NULL DEFAULT 0,
    stok INT NOT NULL DEFAULT 0,
    id_supplier INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_supplier) REFERENCES supplier(id_supplier) ON DELETE SET NULL
);

-- ============================================
-- TABLE: transaksi (header)
-- ============================================
CREATE TABLE IF NOT EXISTS transaksi (
    id_transaksi INT AUTO_INCREMENT PRIMARY KEY,
    no_nota VARCHAR(30) UNIQUE NOT NULL,
    id_customer INT,
    total_harga DECIMAL(15, 2) NOT NULL DEFAULT 0,
    bayar DECIMAL(15, 2) NOT NULL DEFAULT 0,
    kembalian DECIMAL(15, 2) NOT NULL DEFAULT 0,
    tanggal TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_customer) REFERENCES customer(id_customer) ON DELETE SET NULL
);

-- ============================================
-- TABLE: detail_transaksi (line items)
-- ============================================
CREATE TABLE IF NOT EXISTS detail_transaksi (
    id_detail INT AUTO_INCREMENT PRIMARY KEY,
    id_transaksi INT NOT NULL,
    id_barang INT NOT NULL,
    nama_barang VARCHAR(100) NOT NULL,
    harga_jual DECIMAL(15, 2) NOT NULL,
    jumlah INT NOT NULL,
    subtotal DECIMAL(15, 2) NOT NULL,
    FOREIGN KEY (id_transaksi) REFERENCES transaksi(id_transaksi) ON DELETE CASCADE,
    FOREIGN KEY (id_barang) REFERENCES barang(id_barang)
);

-- ============================================
-- DUMMY DATA
-- ============================================

INSERT INTO supplier (nama_supplier, telepon, alamat) VALUES
('PT Sejahtera Mandiri', '021-5551234', 'Jl. Sudirman No. 10, Jakarta'),
('CV Maju Bersama', '022-5559876', 'Jl. Asia Afrika No. 5, Bandung'),
('UD Karya Tani', '031-5554567', 'Jl. Pemuda No. 20, Surabaya');

INSERT INTO customer (nama_customer, telepon, alamat) VALUES
('Budi Santoso', '081234567890', 'Jl. Melati No. 3, Tangerang'),
('Siti Rahayu', '082345678901', 'Jl. Mawar No. 7, Bekasi'),
('Ahmad Fauzi', '083456789012', 'Jl. Kenanga No. 12, Depok'),
('Umum / Walk-in', '-', '-');

INSERT INTO barang (kode_barang, nama_barang, kategori, harga_beli, harga_jual, stok, id_supplier) VALUES
('BRG001', 'Beras Premium 5kg', 'Sembako', 55000, 68000, 100, 1),
('BRG002', 'Minyak Goreng 2L', 'Sembako', 28000, 35000, 80, 1),
('BRG003', 'Gula Pasir 1kg', 'Sembako', 14000, 18000, 150, 2),
('BRG004', 'Indomie Goreng', 'Mie Instan', 2800, 3500, 500, 2),
('BRG005', 'Susu UHT Full Cream 1L', 'Minuman', 15000, 19000, 60, 3),
('BRG006', 'Sabun Mandi Lifebuoy', 'Kebersihan', 3500, 5000, 200, 3),
('BRG007', 'Shampo Pantene 180ml', 'Kebersihan', 18000, 24000, 75, 1),
('BRG008', 'Kopi Kapal Api 165gr', 'Minuman', 12000, 16000, 120, 2);
