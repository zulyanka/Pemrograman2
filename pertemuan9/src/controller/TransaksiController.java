// ============================================
// FILE    : controller/TransaksiController.java
// ============================================
package controller;

import config.DatabaseConfig;
import model.DetailTransaksi;
import model.Transaksi;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Controller untuk transaksi penjualan.
 * Menggunakan database Transaction (commit/rollback) agar data konsisten.
 */
public class TransaksiController {

    private Connection conn;
    private BarangController barangCtrl;

    public TransaksiController() {
        this.conn       = DatabaseConfig.getConnection();
        this.barangCtrl = new BarangController();
    }

    /** Generate nomor nota otomatis: NOT-YYYYMMDD-XXXXX */
    public String generateNoNota() {
        String tanggal = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String sql = "SELECT COUNT(*) FROM transaksi WHERE DATE(tanggal) = CURDATE()";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                int urut = rs.getInt(1) + 1;
                return String.format("NOT-%s-%05d", tanggal, urut);
            }
        } catch (SQLException e) {
            System.err.println("[Transaksi] generateNoNota error: " + e.getMessage());
        }
        return "NOT-" + tanggal + "-00001";
    }

    /**
     * Simpan transaksi lengkap dengan detail dan pengurangan stok.
     * Menggunakan DB Transaction untuk menjaga konsistensi data.
     */
    public boolean simpanTransaksi(Transaksi trx) {
        String sqlHeader = "INSERT INTO transaksi (no_nota, id_customer, total_harga, bayar, kembalian) "
                         + "VALUES (?, ?, ?, ?, ?)";
        String sqlDetail = "INSERT INTO detail_transaksi (id_transaksi, id_barang, nama_barang, harga_jual, jumlah, subtotal) "
                         + "VALUES (?, ?, ?, ?, ?, ?)";
        try {
            conn.setAutoCommit(false); // Mulai transaction

            // 1. Insert header transaksi
            int idTransaksi;
            try (PreparedStatement ps = conn.prepareStatement(sqlHeader, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, trx.getNoNota());
                ps.setInt(2, trx.getIdCustomer());
                ps.setDouble(3, trx.getTotalHarga());
                ps.setDouble(4, trx.getBayar());
                ps.setDouble(5, trx.getKembalian());
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                idTransaksi = rs.getInt(1);
            }

            // 2. Insert detail + kurangi stok per item
            try (PreparedStatement ps = conn.prepareStatement(sqlDetail)) {
                for (DetailTransaksi d : trx.getDetails()) {
                    // Cek dan kurangi stok dulu
                    boolean stokOk = barangCtrl.kurangiStok(d.getIdBarang(), d.getJumlah());
                    if (!stokOk) {
                        conn.rollback(); // Batalkan semua jika stok tidak cukup
                        System.err.println("[Transaksi] Stok tidak cukup untuk: " + d.getNamaBarang());
                        return false;
                    }
                    ps.setInt(1, idTransaksi);
                    ps.setInt(2, d.getIdBarang());
                    ps.setString(3, d.getNamaBarang());
                    ps.setDouble(4, d.getHargaJual());
                    ps.setInt(5, d.getJumlah());
                    ps.setDouble(6, d.getSubtotal());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            conn.commit(); // Semua berhasil → commit
            return true;

        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ex) { /* ignore */ }
            System.err.println("[Transaksi] simpanTransaksi error: " + e.getMessage());
            return false;
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException e) { /* ignore */ }
        }
    }

    /** Laporan semua transaksi dengan filter tanggal opsional */
    public List<Transaksi> getLaporan(String tglMulai, String tglSelesai) {
        List<Transaksi> list = new ArrayList<>();
        String sql = "SELECT t.*, c.nama_customer FROM transaksi t "
                   + "LEFT JOIN customer c ON t.id_customer = c.id_customer "
                   + "WHERE DATE(t.tanggal) BETWEEN ? AND ? "
                   + "ORDER BY t.tanggal DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tglMulai);
            ps.setString(2, tglSelesai);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Transaksi t = new Transaksi();
                t.setIdTransaksi(rs.getInt("id_transaksi"));
                t.setNoNota(rs.getString("no_nota"));
                t.setNamaCustomer(rs.getString("nama_customer"));
                t.setTotalHarga(rs.getDouble("total_harga"));
                t.setBayar(rs.getDouble("bayar"));
                t.setKembalian(rs.getDouble("kembalian"));
                t.setTanggal(rs.getTimestamp("tanggal"));
                list.add(t);
            }
        } catch (SQLException e) {
            System.err.println("[Transaksi] getLaporan error: " + e.getMessage());
        }
        return list;
    }

    /** Ambil detail per transaksi */
    public List<DetailTransaksi> getDetailByTransaksi(int idTransaksi) {
        List<DetailTransaksi> list = new ArrayList<>();
        String sql = "SELECT * FROM detail_transaksi WHERE id_transaksi = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idTransaksi);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                DetailTransaksi d = new DetailTransaksi();
                d.setIdDetail(rs.getInt("id_detail"));
                d.setIdTransaksi(rs.getInt("id_transaksi"));
                d.setIdBarang(rs.getInt("id_barang"));
                d.setNamaBarang(rs.getString("nama_barang"));
                d.setHargaJual(rs.getDouble("harga_jual"));
                d.setJumlah(rs.getInt("jumlah"));
                d.setSubtotal(rs.getDouble("subtotal"));
                list.add(d);
            }
        } catch (SQLException e) {
            System.err.println("[Transaksi] getDetail error: " + e.getMessage());
        }
        return list;
    }
}
