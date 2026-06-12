/* =========================================================
   SERVER.JS — Showroom Master (Backend)
   Tugas file ini:
   1. Menyajikan halaman web (folder "public")
   2. Menyediakan API: alamat-alamat yang dipanggil oleh
      script.js untuk mengambil & menyimpan data ke MySQL
   Jalankan dengan perintah:  node server.js
   ========================================================= */

const express = require("express");
const mysql = require("mysql2/promise");

const app = express();
const PORT = 3000;

/* ---------- KONEKSI DATABASE ----------
   Sesuaikan dengan pengaturan MySQL di komputermu.
   Default XAMPP: user "root", password kosong.        */
const db = mysql.createPool({
  host: "localhost",
  user: "root",
  password: "",          // isi kalau MySQL-mu pakai password
  database: "showroom"
});

app.use(express.json());          // agar bisa membaca data JSON dari frontend
app.use(express.static("public")); // menyajikan index.html, style.css, script.js

/* ================== API MOBIL ================== */

// Ambil semua mobil
app.get("/api/mobil", async (req, res) => {
  try {
    const [rows] = await db.query("SELECT * FROM mobil ORDER BY id");
    res.json(rows);
  } catch (err) {
    res.status(500).json({ pesan: "Gagal mengambil data mobil: " + err.message });
  }
});

// Tambah mobil baru
app.post("/api/mobil", async (req, res) => {
  try {
    const { merk, model, tahun, warna, harga } = req.body;
    await db.query(
      "INSERT INTO mobil (merk, model, tahun, warna, harga) VALUES (?, ?, ?, ?, ?)",
      [merk, model, tahun, warna, harga]
    );
    res.json({ pesan: "Mobil tersimpan" });
  } catch (err) {
    res.status(500).json({ pesan: "Gagal menyimpan mobil: " + err.message });
  }
});

// Hapus mobil
app.delete("/api/mobil/:id", async (req, res) => {
  try {
    await db.query("DELETE FROM mobil WHERE id = ?", [req.params.id]);
    res.json({ pesan: "Mobil dihapus" });
  } catch (err) {
    // Error foreign key = mobil sudah dipakai di transaksi
    res.status(400).json({ pesan: "Mobil tidak bisa dihapus karena sudah tercatat di transaksi." });
  }
});

/* ================== API CUSTOMER ================== */

app.get("/api/customer", async (req, res) => {
  try {
    const [rows] = await db.query("SELECT * FROM customer ORDER BY id");
    res.json(rows);
  } catch (err) {
    res.status(500).json({ pesan: "Gagal mengambil data customer: " + err.message });
  }
});

app.post("/api/customer", async (req, res) => {
  try {
    const { nama, hp, email, alamat } = req.body;
    await db.query(
      "INSERT INTO customer (nama, hp, email, alamat) VALUES (?, ?, ?, ?)",
      [nama, hp, email || "-", alamat]
    );
    res.json({ pesan: "Customer tersimpan" });
  } catch (err) {
    res.status(500).json({ pesan: "Gagal menyimpan customer: " + err.message });
  }
});

app.delete("/api/customer/:id", async (req, res) => {
  try {
    await db.query("DELETE FROM customer WHERE id = ?", [req.params.id]);
    res.json({ pesan: "Customer dihapus" });
  } catch (err) {
    res.status(400).json({ pesan: "Customer tidak bisa dihapus karena sudah tercatat di transaksi." });
  }
});

/* ================== API TRANSAKSI ================== */

// Ambil semua transaksi, digabung (JOIN) dengan nama customer & mobil
app.get("/api/transaksi", async (req, res) => {
  try {
    const [rows] = await db.query(`
      SELECT t.id,
             DATE_FORMAT(t.tanggal, '%Y-%m-%d') AS tanggal,
             t.metode,
             t.harga,
             c.nama AS namaCustomer,
             CONCAT(m.merk, ' ', m.model) AS namaMobil
      FROM transaksi t
      LEFT JOIN customer c ON t.customer_id = c.id
      LEFT JOIN mobil m    ON t.mobil_id = m.id
      ORDER BY t.id
    `);
    res.json(rows);
  } catch (err) {
    res.status(500).json({ pesan: "Gagal mengambil transaksi: " + err.message });
  }
});

// Tambah transaksi: simpan + tandai mobil sebagai terjual
app.post("/api/transaksi", async (req, res) => {
  try {
    const { mobil_id, customer_id, tanggal, metode } = req.body;

    // Ambil harga mobil dari database (lebih aman daripada dikirim dari browser)
    const [hasil] = await db.query("SELECT harga, terjual FROM mobil WHERE id = ?", [mobil_id]);
    if (hasil.length === 0)  return res.status(404).json({ pesan: "Mobil tidak ditemukan." });
    if (hasil[0].terjual)    return res.status(400).json({ pesan: "Mobil ini sudah terjual." });

    await db.query(
      "INSERT INTO transaksi (tanggal, mobil_id, customer_id, metode, harga) VALUES (?, ?, ?, ?, ?)",
      [tanggal, mobil_id, customer_id, metode, hasil[0].harga]
    );
    await db.query("UPDATE mobil SET terjual = TRUE WHERE id = ?", [mobil_id]);

    res.json({ pesan: "Transaksi tersimpan" });
  } catch (err) {
    res.status(500).json({ pesan: "Gagal menyimpan transaksi: " + err.message });
  }
});

// Hapus transaksi: mobil kembali berstatus tersedia
app.delete("/api/transaksi/:id", async (req, res) => {
  try {
    const [hasil] = await db.query("SELECT mobil_id FROM transaksi WHERE id = ?", [req.params.id]);
    await db.query("DELETE FROM transaksi WHERE id = ?", [req.params.id]);
    if (hasil.length > 0) {
      await db.query("UPDATE mobil SET terjual = FALSE WHERE id = ?", [hasil[0].mobil_id]);
    }
    res.json({ pesan: "Transaksi dihapus" });
  } catch (err) {
    res.status(500).json({ pesan: "Gagal menghapus transaksi: " + err.message });
  }
});

/* ================== JALANKAN SERVER ================== */
app.listen(PORT, () => {
  console.log("✔ Server jalan! Buka di browser: http://localhost:" + PORT);
});
