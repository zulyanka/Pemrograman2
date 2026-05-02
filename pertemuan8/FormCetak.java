// NAMA    : Zulyan Widyaka Krisna
// NIM     : 231011403446
// KELAS   : 06TPLE016

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.Connection;
import java.util.HashMap;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

public class FormCetak extends JFrame {
    
    public FormCetak() {
        setTitle("Pusat Laporan Aplikasi");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        
        getContentPane().setBackground(new Color(240, 240, 245));

        JLabel infoLabel = new JLabel("Silakan Pilih Laporan yang Ingin Dicetak:", SwingConstants.CENTER);
        infoLabel.setBounds(20, 20, 340, 25);
        infoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(infoLabel);

        // Tombol 1: Laporan Karyawan
        JButton btnKaryawan = new JButton("1. Cetak Laporan Karyawan");
        btnKaryawan.setBounds(70, 70, 240, 40);
        add(btnKaryawan);

        // Tombol 2: Laporan Pekerjaan
        JButton btnPekerjaan = new JButton("2. Cetak Laporan Pekerjaan");
        btnPekerjaan.setBounds(70, 130, 240, 40);
        add(btnPekerjaan);

        // Tombol 3: Laporan Gaji (Transaksi)
        JButton btnGaji = new JButton("3. Cetak Laporan Transaksi Gaji");
        btnGaji.setBounds(70, 190, 240, 40);
        add(btnGaji);

        // --- EVENT LISTENER (Memanggil fungsi dinamis) ---
        btnKaryawan.addActionListener(e -> prosesCetak("reportKaryawan.jrxml"));
        btnPekerjaan.addActionListener(e -> prosesCetak("reportPekerjaan.jrxml"));
        btnGaji.addActionListener(e -> prosesCetak("reportGaji.jrxml"));
    }

    // FUNGSI DINAMIS: Bisa dipakai untuk nge-print file JRXML apa saja!
    private void prosesCetak(String namaFileJrxml) {
        try {
            Connection con = Koneksi.getKoneksi();
            
            File reportFile = new File(namaFileJrxml);
            if(!reportFile.exists()) {
                JOptionPane.showMessageDialog(this, "File " + namaFileJrxml + " tidak ditemukan!");
                return;
            }

            // Minta Java meng-compile dan mengisi data
            JasperReport jr = JasperCompileManager.compileReport(reportFile.getPath());
            JasperPrint jp = JasperFillManager.fillReport(jr, new HashMap<>(), con);
            
            // Tampilkan laporannya!
            JasperViewer.viewReport(jp, false);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal mencetak laporan:\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FormCetak().setVisible(true);
        });
    }
}