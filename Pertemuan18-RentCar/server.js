/* =========================================================
   SERVER.JS — ZW Rent Car (Backend)
   Port: 3001 (sengaja beda dengan aplikasi showroom yang
   memakai 3000, supaya keduanya bisa jalan bersamaan).
   Jalankan dengan:  node server.js
   ========================================================= */

const express = require("express");
const mysql = require("mysql2/promise");

const app = express();
const PORT = 3001;

// Koneksi database — sesuaikan jika MySQL-mu pakai password
const db = mysql.createPool({
  host: "localhost",
  user: "root",
  password: "",
  database: "rentcar"
});

app.use(express.json());
app.use(express.static("public"));

/* ================== API MOBIL ================== */

app.get("/api/mobil", async (req, res) => {
  try {
    const [rows] = await db.query("SELECT * FROM mobil ORDER BY id");
    res.json(rows);
  } catch (err) {
    res.status(500).json({ pesan: "Gagal mengambil data mobil: " + err.message });
  }
});

app.post("/api/mobil", async (req, res) => {
  try {
    const { merk, model, tahun, plat_nomor, harga_sewa } = req.body;
    await db.query(
      "INSERT INTO mobil (merk, model, tahun, plat_nomor, harga_sewa) VALUES (?, ?, ?, ?, ?)",
      [merk, model, tahun, plat_nomor, harga_sewa]
    );
    res.json({ pesan: "Mobil tersimpan" });
  } catch (err) {
    res.status(500).json({ pesan: "Gagal menyimpan mobil: " + err.message });
  }
});

app.delete("/api/mobil/:id", async (req, res) => {
  try {
    await db.query("DELETE FROM mobil WHERE id = ?", [req.params.id]);
    res.json({ pesan: "Mobil dihapus" });
  } catch (err) {
    res.status(400).json({ pesan: "Mobil tidak bisa dihapus karena punya riwayat sewa." });
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
    const { nama, no_ktp, hp, alamat } = req.body;
    await db.query(
      "INSERT INTO customer (nama, no_ktp, hp, alamat) VALUES (?, ?, ?, ?)",
      [nama, no_ktp, hp, alamat]
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
    res.status(400).json({ pesan: "Customer tidak bisa dihapus karena punya riwayat sewa." });
  }
});

/* ================== API PENYEWAAN ================== */

// Ambil semua transaksi sewa, lengkap dengan nama customer & mobil
app.get("/api/sewa", async (req, res) => {
  try {
    const [rows] = await db.query(`
      SELECT s.id, s.lama_hari, s.total_biaya, s.denda, s.status,
             DATE_FORMAT(s.tgl_sewa, '%Y-%m-%d')            AS tgl_sewa,
             DATE_FORMAT(s.tgl_kembali_rencana, '%Y-%m-%d') AS tgl_kembali_rencana,
             DATE_FORMAT(s.tgl_kembali_aktual, '%Y-%m-%d')  AS tgl_kembali_aktual,
             c.nama AS namaCustomer,
             CONCAT(m.merk, ' ', m.model, ' (', m.plat_nomor, ')') AS namaMobil
      FROM sewa s
      LEFT JOIN customer c ON s.customer_id = c.id
      LEFT JOIN mobil m    ON s.mobil_id = m.id
      ORDER BY s.id
    `);
    res.json(rows);
  } catch (err) {
    res.status(500).json({ pesan: "Gagal mengambil data sewa: " + err.message });
  }
});

// PROSES PENYEWAAN:
// 1. Cek mobil masih tersedia
// 2. Hitung total biaya = lama_hari x harga_sewa
// 3. Hitung tanggal kembali rencana = tgl_sewa + lama_hari
// 4. Simpan transaksi, lalu ubah status mobil jadi "Disewa"
app.post("/api/sewa", async (req, res) => {
  try {
    const { mobil_id, customer_id, tgl_sewa, lama_hari } = req.body;

    const [hasil] = await db.query("SELECT harga_sewa, status FROM mobil WHERE id = ?", [mobil_id]);
    if (hasil.length === 0)            return res.status(404).json({ pesan: "Mobil tidak ditemukan." });
    if (hasil[0].status === "Disewa")  return res.status(400).json({ pesan: "Mobil ini sedang disewa." });

    const total = Number(lama_hari) * Number(hasil[0].harga_sewa);

    await db.query(
      `INSERT INTO sewa (mobil_id, customer_id, tgl_sewa, lama_hari, tgl_kembali_rencana, total_biaya)
       VALUES (?, ?, ?, ?, DATE_ADD(?, INTERVAL ? DAY), ?)`,
      [mobil_id, customer_id, tgl_sewa, lama_hari, tgl_sewa, lama_hari, total]
    );
    await db.query("UPDATE mobil SET status = 'Disewa' WHERE id = ?", [mobil_id]);

    res.json({ pesan: "Penyewaan tersimpan", total: total });
  } catch (err) {
    res.status(500).json({ pesan: "Gagal menyimpan penyewaan: " + err.message });
  }
});

// PROSES PENGEMBALIAN:
// 1. Ambil data sewa + tarif mobilnya
// 2. Hitung keterlambatan = tgl kembali aktual - tgl kembali rencana
// 3. Denda = hari telat x harga sewa per hari (0 kalau tepat waktu)
// 4. Update transaksi jadi "Selesai", mobil kembali "Tersedia"
app.post("/api/kembali", async (req, res) => {
  try {
    const { sewa_id, tgl_kembali } = req.body;

    const [rows] = await db.query(`
      SELECT s.status, s.mobil_id, m.harga_sewa,
             DATE_FORMAT(s.tgl_kembali_rencana, '%Y-%m-%d') AS rencana
      FROM sewa s JOIN mobil m ON s.mobil_id = m.id
      WHERE s.id = ?`, [sewa_id]);

    if (rows.length === 0)              return res.status(404).json({ pesan: "Transaksi tidak ditemukan." });
    if (rows[0].status === "Selesai")   return res.status(400).json({ pesan: "Mobil ini sudah dikembalikan." });

    // Hitung selisih hari (dibulatkan), minimal 0
    const selisihMs = new Date(tgl_kembali) - new Date(rows[0].rencana);
    const hariTelat = Math.max(0, Math.round(selisihMs / (1000 * 60 * 60 * 24)));
    const denda = hariTelat * Number(rows[0].harga_sewa);

    await db.query(
      "UPDATE sewa SET tgl_kembali_aktual = ?, denda = ?, status = 'Selesai' WHERE id = ?",
      [tgl_kembali, denda, sewa_id]
    );
    await db.query("UPDATE mobil SET status = 'Tersedia' WHERE id = ?", [rows[0].mobil_id]);

    res.json({ pesan: "Pengembalian tercatat", hariTelat: hariTelat, denda: denda });
  } catch (err) {
    res.status(500).json({ pesan: "Gagal memproses pengembalian: " + err.message });
  }
});

// Hapus transaksi sewa (kalau masih "Disewa", mobilnya dibebaskan lagi)
app.delete("/api/sewa/:id", async (req, res) => {
  try {
    const [rows] = await db.query("SELECT mobil_id, status FROM sewa WHERE id = ?", [req.params.id]);
    await db.query("DELETE FROM sewa WHERE id = ?", [req.params.id]);
    if (rows.length > 0 && rows[0].status === "Disewa") {
      await db.query("UPDATE mobil SET status = 'Tersedia' WHERE id = ?", [rows[0].mobil_id]);
    }
    res.json({ pesan: "Transaksi dihapus" });
  } catch (err) {
    res.status(500).json({ pesan: "Gagal menghapus transaksi: " + err.message });
  }
});

/* ================== JALANKAN SERVER ================== */
app.listen(PORT, () => {
  console.log("✔ Server Rent Car jalan! Buka di browser: http://localhost:" + PORT);
});
