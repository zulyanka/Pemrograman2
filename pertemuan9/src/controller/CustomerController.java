// ============================================
// FILE    : controller/CustomerController.java
// ============================================
package controller;

import config.DatabaseConfig;
import model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerController {

    private Connection conn;

    public CustomerController() {
        this.conn = DatabaseConfig.getConnection();
    }

    public List<Customer> getAll() {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM customer ORDER BY id_customer";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("[Customer] getAll error: " + e.getMessage());
        }
        return list;
    }

    public boolean insert(Customer c) {
        String sql = "INSERT INTO customer (nama_customer, telepon, alamat) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getNamaCustomer());
            ps.setString(2, c.getTelepon());
            ps.setString(3, c.getAlamat());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[Customer] insert error: " + e.getMessage());
            return false;
        }
    }

    public boolean update(Customer c) {
        String sql = "UPDATE customer SET nama_customer=?, telepon=?, alamat=? WHERE id_customer=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getNamaCustomer());
            ps.setString(2, c.getTelepon());
            ps.setString(3, c.getAlamat());
            ps.setInt(4, c.getIdCustomer());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[Customer] update error: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM customer WHERE id_customer = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[Customer] delete error: " + e.getMessage());
            return false;
        }
    }

    private Customer mapRow(ResultSet rs) throws SQLException {
        return new Customer(
            rs.getInt("id_customer"),
            rs.getString("nama_customer"),
            rs.getString("telepon"),
            rs.getString("alamat")
        );
    }
}
