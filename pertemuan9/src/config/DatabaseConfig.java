// ============================================
// NAMA    : ZULYAN WIDYAKA KRISNA
// NIM     : 231011403446
// KELAS   : 06TPLE016
// FILE    : DatabaseConfig.java
// ============================================
package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton class untuk mengelola koneksi database.
 * Hanya satu koneksi yang dibuat selama aplikasi berjalan.
 */
public class DatabaseConfig {

    private static final String HOST     = "127.0.0.1";
    private static final String PORT     = "3306";
    private static final String DATABASE = "db_pos_app";
    private static final String USER     = "root";
    private static final String PASSWORD = "123456";

    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE
            + "?useSSL=false&serverTimezone=Asia/Jakarta&allowPublicKeyRetrieval=true";

    private static Connection connection = null;

    // Private constructor → tidak bisa diinstansiasi dari luar
    private DatabaseConfig() {}

    /**
     * Mengembalikan koneksi database (buat baru jika belum ada atau sudah closed).
     */
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("[DB] Koneksi database berhasil!");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("[DB] Driver MySQL tidak ditemukan: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("[DB] Gagal koneksi database: " + e.getMessage());
        }
        return connection;
    }

    /**
     * Menutup koneksi database.
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("[DB] Koneksi database ditutup.");
            }
        } catch (SQLException e) {
            System.err.println("[DB] Gagal menutup koneksi: " + e.getMessage());
        }
    }
}
