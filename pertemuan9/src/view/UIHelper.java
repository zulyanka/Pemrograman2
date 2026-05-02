// ============================================
// FILE    : view/UIHelper.java
// ============================================
package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Utility class untuk konsistensi warna dan style UI di seluruh aplikasi.
 */
public class UIHelper {

    // Palet warna tema POS
    public static final Color PRIMARY      = new Color(37, 99, 235);   // Biru utama
    public static final Color PRIMARY_DARK = new Color(29, 78, 216);
    public static final Color SUCCESS      = new Color(22, 163, 74);   // Hijau
    public static final Color DANGER       = new Color(220, 38, 38);   // Merah
    public static final Color WARNING      = new Color(217, 119, 6);   // Oranye
    public static final Color BG_LIGHT     = new Color(248, 250, 252); // Background
    public static final Color HEADER_BG    = new Color(30, 41, 59);    // Header gelap
    public static final Color TABLE_STRIPE = new Color(241, 245, 249);

    private static final DecimalFormat RUPIAH_FMT;
    static {
        DecimalFormatSymbols sym = new DecimalFormatSymbols(new Locale("id", "ID"));
        sym.setGroupingSeparator('.');
        sym.setDecimalSeparator(',');
        RUPIAH_FMT = new DecimalFormat("Rp #,##0", sym);
    }

    /** Format angka ke Rupiah: Rp 15.000 */
    public static String formatRupiah(double amount) {
        return RUPIAH_FMT.format(amount);
    }

    /** Buat tombol dengan warna kustom */
    public static JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBorder(new EmptyBorder(8, 16, 8, 16));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    /** Styling JTable agar tampil rapi */
    public static void styleTable(JTable table) {
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setGridColor(new Color(226, 232, 240));
        table.setShowGrid(true);
        table.setSelectionBackground(new Color(219, 234, 254));
        table.setSelectionForeground(Color.BLACK);
        table.setFillsViewportHeight(true);

        // Header styling
        JTableHeader header = table.getTableHeader();
        header.setBackground(HEADER_BG);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setPreferredSize(new Dimension(0, 35));
        header.setReorderingAllowed(false);

        // Alternating row colors
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {
                super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, col);
                setBorder(new EmptyBorder(0, 8, 0, 8));
                if (!isSelected) {
                    setBackground(row % 2 == 0 ? Color.WHITE : TABLE_STRIPE);
                }
                return this;
            }
        });
    }

    /** Buat label dengan font tertentu */
    public static JLabel createLabel(String text, int style, int size) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", style, size));
        return lbl;
    }

    /** Buat panel header berwarna gelap */
    public static JPanel createHeaderPanel(String title, String subtitle) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(HEADER_BG);
        panel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel(subtitle);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(new Color(148, 163, 184));

        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 2));
        textPanel.setOpaque(false);
        textPanel.add(lblTitle);
        textPanel.add(lblSub);
        panel.add(textPanel, BorderLayout.WEST);
        return panel;
    }

    /** Style JTextField agar terlihat modern */
    public static JTextField createTextField() {
        JTextField tf = new JTextField();
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(203, 213, 225)),
            new EmptyBorder(6, 10, 6, 10)
        ));
        return tf;
    }

    /** Style JComboBox */
    public static <T> JComboBox<T> createComboBox() {
        JComboBox<T> cb = new JComboBox<>();
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cb.setBackground(Color.WHITE);
        return cb;
    }
}
