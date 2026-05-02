// ============================================
// FILE    : view/FormTransaksi.java
// ============================================
package view;

import controller.BarangController;
import controller.CustomerController;
import controller.TransaksiController;
import model.Barang;
import model.Customer;
import model.DetailTransaksi;
import model.Transaksi;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Form Transaksi Penjualan (POS).
 * Pilih customer → pilih barang → input jumlah → hitung total → simpan.
 */
public class FormTransaksi extends JPanel {

    private TransaksiController trxCtrl  = new TransaksiController();
    private BarangController    barangCtrl = new BarangController();
    private CustomerController  custCtrl  = new CustomerController();

    // Keranjang belanja (cart)
    private List<DetailTransaksi> cart = new ArrayList<>();

    // Komponen UI
    private JComboBox<Customer> cmbCustomer;
    private JComboBox<Barang>   cmbBarang;
    private JTextField txtJumlah, txtHargaSatuan, txtNoNota;
    private JTextField txtTotal, txtBayar, txtKembalian;
    private JTable cartTable;
    private DefaultTableModel cartModel;
    private JButton btnTambahItem, btnHapusItem, btnBayar, btnBatal;
    private JLabel lblNoNota;

    public FormTransaksi() {
        setLayout(new BorderLayout());
        setBackground(UIHelper.BG_LIGHT);
        buildUI();
        generateNota();
    }

    private void buildUI() {
        add(UIHelper.createHeaderPanel("🛒 Transaksi Penjualan", "Input penjualan baru"), BorderLayout.NORTH);

        // ===== PANEL KIRI: Pilih barang & cart =====
        JPanel leftPanel = new JPanel(new BorderLayout(0, 10));
        leftPanel.setOpaque(false);
        leftPanel.setBorder(new EmptyBorder(10, 10, 10, 5));

        // Panel pilih barang
        JPanel pilihPanel = new JPanel(new GridBagLayout());
        pilihPanel.setBackground(Color.WHITE);
        pilihPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(226,232,240)),
                "Pilih Barang", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12), UIHelper.PRIMARY),
            new EmptyBorder(10, 15, 15, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        cmbBarang      = UIHelper.createComboBox();
        txtHargaSatuan = UIHelper.createTextField();
        txtHargaSatuan.setEditable(false);
        txtHargaSatuan.setBackground(new Color(241, 245, 249));
        txtJumlah = UIHelper.createTextField();
        txtJumlah.setText("1");

        loadBarangCombo();

        addRow(pilihPanel, gbc, 0, "Barang", cmbBarang);
        addRow(pilihPanel, gbc, 1, "Harga Satuan", txtHargaSatuan);
        addRow(pilihPanel, gbc, 2, "Jumlah", txtJumlah);

        btnTambahItem = UIHelper.createButton("➕ Tambah ke Keranjang", UIHelper.SUCCESS);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 5, 5, 5);
        pilihPanel.add(btnTambahItem, gbc);

        // Tabel cart
        String[] cartCols = {"#", "Nama Barang", "Harga", "Qty", "Subtotal"};
        cartModel = new DefaultTableModel(cartCols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        cartTable = new JTable(cartModel);
        UIHelper.styleTable(cartTable);
        cartTable.getColumnModel().getColumn(0).setMaxWidth(35);
        cartTable.getColumnModel().getColumn(3).setMaxWidth(50);

        JPanel cartWrapper = new JPanel(new BorderLayout());
        cartWrapper.setBackground(Color.WHITE);
        cartWrapper.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(226,232,240)),
                "Keranjang Belanja", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12), UIHelper.PRIMARY),
            new EmptyBorder(5, 5, 5, 5)
        ));
        cartWrapper.add(new JScrollPane(cartTable), BorderLayout.CENTER);

        btnHapusItem = UIHelper.createButton("🗑 Hapus Item Terpilih", UIHelper.DANGER);
        JPanel hapusPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        hapusPanel.setOpaque(false);
        hapusPanel.add(btnHapusItem);
        cartWrapper.add(hapusPanel, BorderLayout.SOUTH);

        leftPanel.add(pilihPanel, BorderLayout.NORTH);
        leftPanel.add(cartWrapper, BorderLayout.CENTER);

        // ===== PANEL KANAN: Info transaksi & bayar =====
        JPanel rightPanel = new JPanel(new BorderLayout(0, 10));
        rightPanel.setOpaque(false);
        rightPanel.setBorder(new EmptyBorder(10, 5, 10, 10));
        rightPanel.setPreferredSize(new Dimension(280, 0));

        // Info transaksi
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(226,232,240)),
                "Info Transaksi", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12), UIHelper.PRIMARY),
            new EmptyBorder(10, 15, 15, 15)
        ));

        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.fill = GridBagConstraints.HORIZONTAL;
        gbc2.insets = new Insets(6, 5, 6, 5);

        txtNoNota = UIHelper.createTextField();
        txtNoNota.setEditable(false);
        txtNoNota.setBackground(new Color(241, 245, 249));
        txtNoNota.setFont(new Font("Segoe UI", Font.BOLD, 13));

        cmbCustomer = UIHelper.createComboBox();
        loadCustomerCombo();

        addRow(infoPanel, gbc2, 0, "No. Nota", txtNoNota);
        addRow(infoPanel, gbc2, 1, "Customer", cmbCustomer);

        // Total, Bayar, Kembalian
        txtTotal = UIHelper.createTextField();
        txtTotal.setEditable(false);
        txtTotal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        txtTotal.setForeground(UIHelper.SUCCESS);
        txtTotal.setBackground(new Color(240, 253, 244));
        txtTotal.setText("Rp 0");

        txtBayar = UIHelper.createTextField();
        txtBayar.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        txtKembalian = UIHelper.createTextField();
        txtKembalian.setEditable(false);
        txtKembalian.setFont(new Font("Segoe UI", Font.BOLD, 14));
        txtKembalian.setForeground(UIHelper.PRIMARY);
        txtKembalian.setBackground(new Color(239, 246, 255));

        addRow(infoPanel, gbc2, 2, "TOTAL", txtTotal);
        addRow(infoPanel, gbc2, 3, "Uang Bayar", txtBayar);
        addRow(infoPanel, gbc2, 4, "Kembalian", txtKembalian);

        // Tombol Bayar & Batal
        btnBayar = UIHelper.createButton("💳 BAYAR / SIMPAN", UIHelper.SUCCESS);
        btnBayar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnBayar.setPreferredSize(new Dimension(0, 48));

        btnBatal = UIHelper.createButton("❌ Batal / Reset", UIHelper.DANGER);

        JPanel actionPanel = new JPanel(new GridLayout(2, 1, 0, 8));
        actionPanel.setOpaque(false);
        actionPanel.add(btnBayar);
        actionPanel.add(btnBatal);

        gbc2.gridx = 0; gbc2.gridy = 5; gbc2.gridwidth = 2;
        gbc2.insets = new Insets(20, 5, 5, 5);
        infoPanel.add(actionPanel, gbc2);

        rightPanel.add(infoPanel, BorderLayout.NORTH);

        // ===== Main layout =====
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);
        mainPanel.add(leftPanel,  BorderLayout.CENTER);
        mainPanel.add(rightPanel, BorderLayout.EAST);
        add(mainPanel, BorderLayout.CENTER);

        // ===== Event Listeners =====
        cmbBarang.addActionListener(e -> updateHargaSatuan());
        btnTambahItem.addActionListener(e -> tambahKeCart());
        btnHapusItem.addActionListener(e  -> hapusDariCart());
        btnBayar.addActionListener(e      -> prosesTransaksi());
        btnBatal.addActionListener(e      -> resetForm());
        txtBayar.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override public void keyReleased(java.awt.event.KeyEvent e) { hitungKembalian(); }
        });
    }

    private void addRow(JPanel p, GridBagConstraints g, int row, String label, JComponent field) {
        g.gridx = 0; g.gridy = row; g.gridwidth = 1; g.weightx = 0.35;
        JLabel lbl = UIHelper.createLabel(label, Font.BOLD, 12);
        lbl.setForeground(new Color(71, 85, 105));
        p.add(lbl, g);
        g.gridx = 1; g.weightx = 0.65;
        p.add(field, g);
    }

    private void loadBarangCombo() {
        cmbBarang.removeAllItems();
        barangCtrl.getAll().forEach(cmbBarang::addItem);
        updateHargaSatuan();
    }

    private void loadCustomerCombo() {
        cmbCustomer.removeAllItems();
        custCtrl.getAll().forEach(cmbCustomer::addItem);
    }

    private void updateHargaSatuan() {
        Barang b = (Barang) cmbBarang.getSelectedItem();
        if (b != null) {
            txtHargaSatuan.setText(UIHelper.formatRupiah(b.getHargaJual())
                + "  [Stok: " + b.getStok() + "]");
        }
    }

    private void tambahKeCart() {
        Barang b = (Barang) cmbBarang.getSelectedItem();
        if (b == null) return;

        int jumlah;
        try {
            jumlah = Integer.parseInt(txtJumlah.getText().trim());
            if (jumlah <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Jumlah harus angka positif!", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (jumlah > b.getStok()) {
            JOptionPane.showMessageDialog(this, "Stok tidak cukup! Stok tersedia: " + b.getStok(), "Stok Kurang", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Cek apakah barang sudah ada di cart → update jumlah
        for (DetailTransaksi d : cart) {
            if (d.getIdBarang() == b.getIdBarang()) {
                int totalQty = d.getJumlah() + jumlah;
                if (totalQty > b.getStok()) {
                    JOptionPane.showMessageDialog(this, "Total qty melebihi stok!", "Stok Kurang", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                d.setJumlah(totalQty);
                refreshCartTable();
                hitungTotal();
                return;
            }
        }

        // Barang baru di cart
        cart.add(new DetailTransaksi(b.getIdBarang(), b.getNamaBarang(), b.getHargaJual(), jumlah));
        refreshCartTable();
        hitungTotal();
        txtJumlah.setText("1");
    }

    private void hapusDariCart() {
        int row = cartTable.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Pilih item yang ingin dihapus!"); return; }
        cart.remove(row);
        refreshCartTable();
        hitungTotal();
    }

    private void refreshCartTable() {
        cartModel.setRowCount(0);
        int no = 1;
        for (DetailTransaksi d : cart) {
            cartModel.addRow(new Object[]{
                no++, d.getNamaBarang(),
                UIHelper.formatRupiah(d.getHargaJual()),
                d.getJumlah(),
                UIHelper.formatRupiah(d.getSubtotal())
            });
        }
    }

    private void hitungTotal() {
        double total = cart.stream().mapToDouble(DetailTransaksi::getSubtotal).sum();
        txtTotal.setText(UIHelper.formatRupiah(total));
        hitungKembalian();
    }

    private void hitungKembalian() {
        try {
            double total  = cart.stream().mapToDouble(DetailTransaksi::getSubtotal).sum();
            double bayar  = Double.parseDouble(txtBayar.getText().replace(".", "").replace(",", ".").trim());
            double kembal = bayar - total;
            txtKembalian.setText(UIHelper.formatRupiah(Math.max(0, kembal)));
            txtKembalian.setForeground(kembal < 0 ? UIHelper.DANGER : UIHelper.PRIMARY);
        } catch (NumberFormatException e) {
            txtKembalian.setText("Rp 0");
        }
    }

    private void prosesTransaksi() {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Keranjang belanja kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double total;
        double bayar;
        try {
            total = cart.stream().mapToDouble(DetailTransaksi::getSubtotal).sum();
            bayar = Double.parseDouble(txtBayar.getText().replace(".", "").replace(",", ".").trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Isi jumlah uang bayar terlebih dahulu!", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (bayar < total) {
            JOptionPane.showMessageDialog(this, "Uang bayar kurang dari total!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Customer cust = (Customer) cmbCustomer.getSelectedItem();
        Transaksi trx = new Transaksi();
        trx.setNoNota(txtNoNota.getText());
        trx.setIdCustomer(cust != null ? cust.getIdCustomer() : 0);
        trx.setTotalHarga(total);
        trx.setBayar(bayar);
        trx.setKembalian(bayar - total);
        trx.setDetails(cart);

        if (trxCtrl.simpanTransaksi(trx)) {
            String pesan = String.format(
                "✅ Transaksi BERHASIL!\n\nNo. Nota : %s\nTotal    : %s\nBayar    : %s\nKembalian: %s",
                trx.getNoNota(),
                UIHelper.formatRupiah(total),
                UIHelper.formatRupiah(bayar),
                UIHelper.formatRupiah(bayar - total)
            );
            JOptionPane.showMessageDialog(this, pesan, "Transaksi Berhasil", JOptionPane.INFORMATION_MESSAGE);
            resetForm();
        } else {
            JOptionPane.showMessageDialog(this, "Transaksi GAGAL! Cek stok barang.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void generateNota() {
        txtNoNota.setText(trxCtrl.generateNoNota());
    }

    public void resetForm() {
        cart.clear();
        cartModel.setRowCount(0);
        txtBayar.setText("");
        txtTotal.setText("Rp 0");
        txtKembalian.setText("Rp 0");
        txtJumlah.setText("1");
        generateNota();
        loadBarangCombo(); // refresh stok
    }
}
