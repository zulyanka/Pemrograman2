// ============================================
// FILE    : Main.java
// ============================================

import config.DatabaseConfig;
import view.MainFrame;

import javax.swing.*;

/**
 * Entry point aplikasi POS.
 * Cek koneksi DB dulu sebelum membuka jendela utama.
 */
public class Main {
    public static void main(String[] args) {
        // Set Look and Feel ke sistem (lebih native di Windows/macOS)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Jika gagal, lanjut dengan default Swing
        }

        // Jalankan di Event Dispatch Thread (thread-safe untuk Swing)
        SwingUtilities.invokeLater(() -> {
            // Cek koneksi database saat startup
            if (DatabaseConfig.getConnection() == null) {
                JOptionPane.showMessageDialog(null,
                    "❌ Gagal terhubung ke database!\n\n" +
                    "Pastikan:\n" +
                    "1. MySQL sudah berjalan\n" +
                    "2. Database 'db_pos_app' sudah dibuat\n" +
                    "3. Username/password di DatabaseConfig.java sudah benar",
                    "Koneksi Gagal", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }

            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
