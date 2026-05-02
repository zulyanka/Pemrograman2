// ============================================
// FILE    : view/MainFrame.java
// ============================================
package view;

import config.DatabaseConfig;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Frame utama aplikasi POS.
 * Menggunakan sidebar navigasi + CardLayout untuk mengganti panel.
 */
public class MainFrame extends JFrame {

    private JPanel contentPanel;
    private CardLayout cardLayout;

    // Nama panel untuk CardLayout
    private static final String CARD_TRANSAKSI  = "transaksi";
    private static final String CARD_BARANG      = "barang";
    private static final String CARD_CUSTOMER    = "customer";
    private static final String CARD_SUPPLIER    = "supplier";
    private static final String CARD_LAP_TRX     = "laporan_trx";
    private static final String CARD_LAP_STOK    = "laporan_stok";

    private FormTransaksi formTransaksi; // disimpan agar bisa di-refresh

    public MainFrame() {
        setTitle("🏪 POS App - Sistem Penjualan | I Gede Yoga Setiawan");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 600));

        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout());

        // ===== SIDEBAR =====
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(15, 23, 42)); // dark navy
        sidebar.setPreferredSize(new Dimension(210, 0));

        // Logo / App name
        JPanel logoPanel = new JPanel(new BorderLayout());
        logoPanel.setBackground(new Color(10, 15, 30));
        logoPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        logoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JLabel lblLogo = new JLabel("🏪 POS App");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblLogo.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Sistem Penjualan");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblSub.setForeground(new Color(100, 116, 139));

        JPanel logoText = new JPanel(new GridLayout(2, 1));
        logoText.setOpaque(false);
        logoText.add(lblLogo);
        logoText.add(lblSub);
        logoPanel.add(logoText, BorderLayout.CENTER);

        sidebar.add(logoPanel);
        sidebar.add(sidebarSeparator("TRANSAKSI"));
        sidebar.add(createNavBtn("🛒  Penjualan",      CARD_TRANSAKSI));
        sidebar.add(sidebarSeparator("MASTER DATA"));
        sidebar.add(createNavBtn("📦  Data Barang",    CARD_BARANG));
        sidebar.add(createNavBtn("👤  Data Customer",  CARD_CUSTOMER));
        sidebar.add(createNavBtn("🏭  Data Supplier",  CARD_SUPPLIER));
        sidebar.add(sidebarSeparator("LAPORAN"));
        sidebar.add(createNavBtn("📊  Lap. Transaksi", CARD_LAP_TRX));
        sidebar.add(createNavBtn("📋  Lap. Stok",      CARD_LAP_STOK));
        sidebar.add(Box.createVerticalGlue()); // push ke atas

        // Info user bawah sidebar
        JPanel userPanel = new JPanel(new BorderLayout());
        userPanel.setBackground(new Color(10, 15, 30));
        userPanel.setBorder(new EmptyBorder(12, 15, 12, 15));
        userPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JLabel lblUser = new JLabel("👨‍💼 Admin");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblUser.setForeground(Color.WHITE);

        JButton btnLogout = new JButton("Keluar");
        btnLogout.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnLogout.setForeground(new Color(248, 113, 113));
        btnLogout.setBackground(new Color(10, 15, 30));
        btnLogout.setBorderPainted(false);
        btnLogout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLogout.addActionListener(e -> {
            DatabaseConfig.closeConnection();
            System.exit(0);
        });
        userPanel.add(lblUser,    BorderLayout.WEST);
        userPanel.add(btnLogout,  BorderLayout.EAST);
        sidebar.add(userPanel);

        // ===== CONTENT PANEL =====
        cardLayout  = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(UIHelper.BG_LIGHT);

        formTransaksi = new FormTransaksi();
        contentPanel.add(formTransaksi,        CARD_TRANSAKSI);
        contentPanel.add(new FormBarang(),      CARD_BARANG);
        contentPanel.add(new FormCustomer(),    CARD_CUSTOMER);
        contentPanel.add(new FormSupplier(),    CARD_SUPPLIER);
        contentPanel.add(new LaporanTransaksi(), CARD_LAP_TRX);
        contentPanel.add(new LaporanStok(),     CARD_LAP_STOK);

        add(sidebar,       BorderLayout.WEST);
        add(contentPanel,  BorderLayout.CENTER);

        // Default tampilkan halaman transaksi
        cardLayout.show(contentPanel, CARD_TRANSAKSI);
    }

    /** Buat tombol navigasi sidebar */
    private JButton createNavBtn(String text, String card) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setForeground(new Color(203, 213, 225));
        btn.setBackground(new Color(15, 23, 42));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(10, 20, 10, 10));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(30, 41, 59));
                btn.setForeground(Color.WHITE);
            }
            @Override public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(15, 23, 42));
                btn.setForeground(new Color(203, 213, 225));
            }
        });

        btn.addActionListener(e -> {
            cardLayout.show(contentPanel, card);
            // Refresh stok saat kembali ke transaksi
            if (card.equals(CARD_TRANSAKSI)) formTransaksi.resetForm();
        });
        return btn;
    }

    /** Separator label kategori di sidebar */
    private JLabel sidebarSeparator(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lbl.setForeground(new Color(71, 85, 105));
        lbl.setBorder(new EmptyBorder(14, 20, 4, 10));
        lbl.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        return lbl;
    }
}
