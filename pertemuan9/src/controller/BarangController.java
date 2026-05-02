// ============================================
// FILE    : controller/BarangController.java
// ============================================
package controller;

import config.DatabaseConfig;
import model.Barang;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller untuk operasi CRUD tabel barang.
 * Semua query menggunakan PreparedStatement (aman dari SQL Injection).
 */
public class BarangController {

    private Connection conn;

    public BarangController() {
        this.conn = DatabaseConfig.getConnection();
    }

    /** Ambil semua barang + nama supplier via JOIN */
    public List<Barang> getAll() {
        List<Barang> list = new ArrayList<>();
        String sql = "SELECT b.*, s.nama_supplier FROM barang b "
                   + "LEFT JOIN supplier s ON b.id_supplier = s.id_supplier "
                   + "ORDER BY b.id_barang";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Barang b = mapRow(rs);
                b.setNamaSupplier(rs.getString("nama_supplier"));
                list.add(b);
            }
        } catch (SQLException e) {
            System.err.println("[Barang] getAll error: " + e.getMessage());
        }
        return list;
    }

    /** Cari barang berdasarkan nama atau kode (untuk fitur search) */
    public List<Barang> search(String keyword) {
        List<Barang> list = new ArrayList<>();
        String sql = "SELECT b.*, s.nama_supplier FROM barang b "
                   + "LEFT JOIN supplier s ON b.id_supplier = s.id_supplier "
                   + "WHERE b.nama_barang LIKE ? OR b.kode_barang LIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Barang b = mapRow(rs);
                b.setNamaSupplier(rs.getString("nama_supplier"));
                list.add(b);
            }
        } catch (SQLException e) {
            System.err.println("[Barang] search error: " + e.getMessage());
        }
        return list;
    }

    /** Tambah barang baru */
    public boolean insert(Barang b) {
        String sql = "INSERT INTO barang (kode_barang, nama_barang, kategori, harga_beli, harga_jual, stok, id_supplier) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, b.getKodeBarang());
            ps.setString(2, b.getNamaBarang());
            ps.setString(3, b.getKategori());
            ps.setDouble(4, b.getHargaBeli());
            ps.setDouble(5, b.getHargaJual());
            ps.setInt(6, b.getStok());
            ps.setInt(7, b.getIdSupplier());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[Barang] insert error: " + e.getMessage());
            return false;
        }
    }

    /** Update data barang */
    public boolean update(Barang b) {
        String sql = "UPDATE barang SET kode_barang=?, nama_barang=?, kategori=?, "
                   + "harga_beli=?, harga_jual=?, stok=?, id_supplier=? WHERE id_barang=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, b.getKodeBarang());
            ps.setString(2, b.getNamaBarang());
            ps.setString(3, b.getKategori());
            ps.setDouble(4, b.getHargaBeli());
            ps.setDouble(5, b.getHargaJual());
            ps.setInt(6, b.getStok());
            ps.setInt(7, b.getIdSupplier());
            ps.setInt(8, b.getIdBarang());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[Barang] update error: " + e.getMessage());
            return false;
        }
    }

    /** Hapus barang berdasarkan id */
    public boolean delete(int idBarang) {
        String sql = "DELETE FROM barang WHERE id_barang = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idBarang);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[Barang] delete error: " + e.getMessage());
            return false;
        }
    }

    /** Kurangi stok saat transaksi (dipanggil dari TransaksiController) */
    public boolean kurangiStok(int idBarang, int jumlah) {
        String sql = "UPDATE barang SET stok = stok - ? WHERE id_barang = ? AND stok >= ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, jumlah);
            ps.setInt(2, idBarang);
            ps.setInt(3, jumlah);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[Barang] kurangiStok error: " + e.getMessage());
            return false;
        }
    }

    // Helper: mapping ResultSet ke object Barang
    private Barang mapRow(ResultSet rs) throws SQLException {
        Barang b = new Barang();
        b.setIdBarang(rs.getInt("id_barang"));
        b.setKodeBarang(rs.getString("kode_barang"));
        b.setNamaBarang(rs.getString("nama_barang"));
        b.setKategori(rs.getString("kategori"));
        b.setHargaBeli(rs.getDouble("harga_beli"));
        b.setHargaJual(rs.getDouble("harga_jual"));
        b.setStok(rs.getInt("stok"));
        b.setIdSupplier(rs.getInt("id_supplier"));
        return b;
    }
}
