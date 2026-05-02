// ============================================
// FILE    : view/LaporanTransaksi.java
// ============================================
package view;

import controller.TransaksiController;
import model.DetailTransaksi;
import model.Transaksi;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class LaporanTransaksi extends JPanel {

    private TransaksiController ctrl = new TransaksiController();
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtDari, txtSampai;
    private JLabel lblTotal;

    public LaporanTransaksi() {
        setLayout(new BorderLayout());
        setBackground(UIHelper.BG_LIGHT);
        buildUI();
        loadHariIni();
    }

    private void buildUI() {
        add(UIHelper.createHeaderPanel("📊 Laporan Transaksi", "Riwayat penjualan berdasarkan tanggal"), BorderLayout.NORTH);

        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        filterPanel.setBackground(Color.WHITE);
        filterPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226,232,240)),
            new EmptyBorder(8, 10, 8, 10)
        ));

        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        txtDari   = UIHelper.createTextField(); txtDari.setText(today);   txtDari.setPreferredSize(new Dimension(130, 32));
        txtSampai = UIHelper.createTextField(); txtSampai.setText(today); txtSampai.setPreferredSize(new Dimension(130, 32));

        JButton btnFilter = UIHelper.createButton("🔍 Tampilkan", UIHelper.PRIMARY);
        lblTotal = UIHelper.createLabel("Total Penjualan: Rp 0", Font.BOLD, 13);
        lblTotal.setForeground(UIHelper.SUCCESS);

        filterPanel.add(UIHelper.createLabel("Dari:", Font.BOLD, 12));
        filterPanel.add(txtDari);
        filterPanel.add(UIHelper.createLabel("Sampai:", Font.BOLD, 12));
        filterPanel.add(txtSampai);
        filterPanel.add(btnFilter);
        filterPanel.add(Box.createHorizontalStrut(20));
        filterPanel.add(lblTotal);

        // Tabel laporan
        String[] cols = {"No. Nota", "Customer", "Total", "Bayar", "Kembalian", "Tanggal"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        UIHelper.styleTable(table);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Detail panel bawah
        JPanel detailPanel = new JPanel(new BorderLayout());
        detailPanel.setBackground(Color.WHITE);
        detailPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(226,232,240)),
            new EmptyBorder(8, 10, 8, 10)
        ));
        detailPanel.setPreferredSize(new Dimension(0, 130));

        JLabel lblDetailTitle = UIHelper.createLabel("Detail Item (klik baris transaksi):", Font.BOLD, 12);
        String[] detailCols = {"Nama Barang", "Harga Satuan", "Qty", "Subtotal"};
        DefaultTableModel detailModel = new DefaultTableModel(detailCols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable detailTable = new JTable(detailModel);
        UIHelper.styleTable(detailTable);

        detailPanel.add(lblDetailTitle, BorderLayout.NORTH);
        detailPanel.add(new JScrollPane(detailTable), BorderLayout.CENTER);

        add(filterPanel, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(detailPanel, BorderLayout.SOUTH);

        // Events
        btnFilter.addActionListener(e -> loadData(txtDari.getText().trim(), txtSampai.getText().trim()));

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                // Cari id_transaksi dari data yang sudah dimuat
                int row = table.getSelectedRow();
                // Ambil semua transaksi lagi untuk dapat id-nya
                List<Transaksi> list = ctrl.getLaporan(txtDari.getText(), txtSampai.getText());
                if (row < list.size()) {
                    List<DetailTransaksi> details = ctrl.getDetailByTransaksi(list.get(row).getIdTransaksi());
                    detailModel.setRowCount(0);
                    for (DetailTransaksi d : details) {
                        detailModel.addRow(new Object[]{
                            d.getNamaBarang(),
                            UIHelper.formatRupiah(d.getHargaJual()),
                            d.getJumlah(),
                            UIHelper.formatRupiah(d.getSubtotal())
                        });
                    }
                }
            }
        });
    }

    private void loadHariIni() {
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        loadData(today, today);
    }

    private void loadData(String dari, String sampai) {
        tableModel.setRowCount(0);
        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        List<Transaksi> list = ctrl.getLaporan(dari, sampai);
        double grandTotal = 0;
        for (Transaksi t : list) {
            tableModel.addRow(new Object[]{
                t.getNoNota(),
                t.getNamaCustomer() != null ? t.getNamaCustomer() : "Umum",
                UIHelper.formatRupiah(t.getTotalHarga()),
                UIHelper.formatRupiah(t.getBayar()),
                UIHelper.formatRupiah(t.getKembalian()),
                t.getTanggal() != null ? fmt.format(t.getTanggal()) : "-"
            });
            grandTotal += t.getTotalHarga();
        }
        lblTotal.setText("Total Penjualan: " + UIHelper.formatRupiah(grandTotal)
            + "  (" + list.size() + " transaksi)");
    }
}
