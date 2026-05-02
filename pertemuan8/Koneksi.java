// NAMA    : Zulyan Widyaka Krisna
// NIM     : 231011403446
// KELAS   : 06TPLE016

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Koneksi {
    private static Connection koneksi;

    public static Connection getKoneksi() {
        if (koneksi == null) {
            try {
                String url = "jdbc:mysql://127.0.0.1:3306/dbaplikasigajikaryawan";
                String user = "root";
                String password = "123456";
                
                Class.forName("com.mysql.cj.jdbc.Driver");
                koneksi = DriverManager.getConnection(url, user, password);
                System.out.println("Koneksi Database Berhasil");
            } catch (ClassNotFoundException | SQLException e) {
                System.out.println("Koneksi Database Gagal: " + e.getMessage());
            }
        }
        return koneksi;
    }
}