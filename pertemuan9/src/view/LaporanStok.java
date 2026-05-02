// ============================================
// FILE    : view/LaporanStok.java
// ============================================
package view;

import controller.BarangController;
import model.Barang;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class LaporanStok extends JPanel {

    private BarangController ctrl = new BarangController();
    private JTable table;
    private DefaultTableModel tableModel;

    public LaporanStok() {
        setLayout(new BorderLayout());
        setBackground(UIHelper.BG_LIGHT);
        buildUI();
        loadData();
    }

    private void buildUI() {
        add(UIHelper.createHeaderPanel("📋 Laporan Stok Barang", "Status stok seluruh produk"), BorderLayout.NORTH);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        actionPanel.setBackground(Color.WHITE);
        actionPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226,232,240)));

        JButton btnRefresh = UIHelper.createButton("🔄 Refresh", UIHelper.PRIMARY);
        JLabel  lblInfo    = UIHelper.createLabel("🔴 Merah = stok ≤ 10  |  🟡 Kuning = stok ≤ 20", Font.PLAIN, 12);
        lblInfo.setForeground(new Color(100, 116, 139));

        actionPanel.add(btnRefresh);
        actionPanel.add(Box.createHorizontalStrut(15));
        actionPanel.add(lblInfo);

        String[] cols = {"Kode", "Nama Barang", "Kategori", "Harga Beli", "Harga Jual", "Stok", "Supplier", "Status"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        UIHelper.styleTable(table);

        // Custom renderer: warnai baris berdasarkan stok
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {
                super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, col);
                setBorder(new EmptyBorder(0, 8, 0, 8));
                if (!isSelected) {
                    Object stokVal = tableModel.getValueAt(row, 5);
                    int stok = stokVal != null ? (int) stokVal : 0;
                    if (stok <= 10)      setBackground(new Color(254, 226, 226)); // merah muda
                    else if (stok <= 20) setBackground(new Color(254, 249, 195)); // kuning muda
                    else                 setBackground(row % 2 == 0 ? Color.WHITE : UIHelper.TABLE_STRIPE);
                }
                return this;
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new EmptyBorder(10, 10, 10, 10));

        add(actionPanel, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        btnRefresh.addActionListener(e -> loadData());
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<Barang> list = ctrl.getAll();
        for (Barang b : list) {
            String status;
            if (b.getStok() == 0)      status = "❌ Habis";
            else if (b.getStok() <= 10) status = "⚠ Kritis";
            else if (b.getStok() <= 20) status = "🟡 Hampir Habis";
            else                        status = "✅ Aman";

            tableModel.addRow(new Object[]{
                b.getKodeBarang(), b.getNamaBarang(), b.getKategori(),
                UIHelper.formatRupiah(b.getHargaBeli()),
                UIHelper.formatRupiah(b.getHargaJual()),
                b.getStok(),
                b.getNamaSupplier() != null ? b.getNamaSupplier() : "-",
                status
            });
        }
    }
}
