// ============================================
// FILE    : controller/SupplierController.java
// ============================================
package controller;

import config.DatabaseConfig;
import model.Supplier;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierController {

    private Connection conn;

    public SupplierController() {
        this.conn = DatabaseConfig.getConnection();
    }

    public List<Supplier> getAll() {
        List<Supplier> list = new ArrayList<>();
        String sql = "SELECT * FROM supplier ORDER BY id_supplier";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("[Supplier] getAll error: " + e.getMessage());
        }
        return list;
    }

    public boolean insert(Supplier s) {
        String sql = "INSERT INTO supplier (nama_supplier, telepon, alamat) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getNamaSupplier());
            ps.setString(2, s.getTelepon());
            ps.setString(3, s.getAlamat());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[Supplier] insert error: " + e.getMessage());
            return false;
        }
    }

    public boolean update(Supplier s) {
        String sql = "UPDATE supplier SET nama_supplier=?, telepon=?, alamat=? WHERE id_supplier=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getNamaSupplier());
            ps.setString(2, s.getTelepon());
            ps.setString(3, s.getAlamat());
            ps.setInt(4, s.getIdSupplier());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[Supplier] update error: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM supplier WHERE id_supplier = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[Supplier] delete error: " + e.getMessage());
            return false;
        }
    }

    private Supplier mapRow(ResultSet rs) throws SQLException {
        return new Supplier(
            rs.getInt("id_supplier"),
            rs.getString("nama_supplier"),
            rs.getString("telepon"),
            rs.getString("alamat")
        );
    }
}
