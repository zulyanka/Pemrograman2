// ============================================
// FILE    : view/FormBarang.java
// ============================================
package view;

import controller.BarangController;
import controller.SupplierController;
import model.Barang;
import model.Supplier;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Form CRUD untuk manajemen data barang.
 */
public class FormBarang extends JPanel {

    private BarangController barangCtrl   = new BarangController();
    private SupplierController supplierCtrl = new SupplierController();

    // Komponen tabel
    private JTable table;
    private DefaultTableModel tableModel;

    // Komponen form input
    private JTextField txtKode, txtNama, txtKategori, txtHargaBeli, txtHargaJual, txtStok, txtCari;
    private JComboBox<Supplier> cmbSupplier;
    private JButton btnTambah, btnUpdate, btnHapus, btnBersihkan;

    private int selectedId = -1; // ID barang yang sedang dipilih di tabel

    public FormBarang() {
        setLayout(new BorderLayout());
        setBackground(UIHelper.BG_LIGHT);
        buildUI();
        loadTable();
    }

    private void buildUI() {
        // === Header ===
        add(UIHelper.createHeaderPanel("📦 Manajemen Barang", "Kelola data produk dan stok"), BorderLayout.NORTH);

        // === Form Input (kiri) ===
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240)),
            new EmptyBorder(20, 20, 20, 20)
        ));
        formPanel.setPreferredSize(new Dimension(320, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weightx = 1.0;

        // Search bar
        txtCari = UIHelper.createTextField();
        txtCari.setToolTipText("Cari nama atau kode barang...");
        JButton btnCari = UIHelper.createButton("🔍 Cari", UIHelper.PRIMARY);

        addRow(formPanel, gbc, 0, "Kode Barang", txtKode = UIHelper.createTextField());
        addRow(formPanel, gbc, 1, "Nama Barang", txtNama = UIHelper.createTextField());
        addRow(formPanel, gbc, 2, "Kategori", txtKategori = UIHelper.createTextField());
        addRow(formPanel, gbc, 3, "Harga Beli (Rp)", txtHargaBeli = UIHelper.createTextField());
        addRow(formPanel, gbc, 4, "Harga Jual (Rp)", txtHargaJual = UIHelper.createTextField());
        addRow(formPanel, gbc, 5, "Stok", txtStok = UIHelper.createTextField());

        // Supplier dropdown
        cmbSupplier = UIHelper.createComboBox();
        loadSupplierCombo();
        addRow(formPanel, gbc, 6, "Supplier", cmbSupplier);

        // Tombol aksi
        JPanel btnPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        btnPanel.setOpaque(false);
        btnTambah    = UIHelper.createButton("➕ Tambah",  UIHelper.SUCCESS);
        btnUpdate    = UIHelper.createButton("✏ Update",   UIHelper.WARNING);
        btnHapus     = UIHelper.createButton("🗑 Hapus",   UIHelper.DANGER);
        btnBersihkan = UIHelper.createButton("🔄 Bersih",  UIHelper.PRIMARY);
        btnPanel.add(btnTambah); btnPanel.add(btnUpdate);
        btnPanel.add(btnHapus);  btnPanel.add(btnBersihkan);

        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2; gbc.insets = new Insets(15, 5, 5, 5);
        formPanel.add(btnPanel, gbc);

        // === Tabel (kanan) ===
        String[] cols = {"ID", "Kode", "Nama Barang", "Kategori", "Harga Jual", "Stok", "Supplier"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        UIHelper.styleTable(table);
        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(4).setPreferredWidth(110);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));

        // Search panel atas tabel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        searchPanel.setBackground(UIHelper.BG_LIGHT);
        searchPanel.add(UIHelper.createLabel("Cari:", Font.BOLD, 13));
        txtCari.setPreferredSize(new Dimension(200, 32));
        searchPanel.add(txtCari);
        searchPanel.add(btnCari);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(UIHelper.BG_LIGHT);
        rightPanel.add(searchPanel, BorderLayout.NORTH);
        rightPanel.add(scroll, BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, formPanel, rightPanel);
        split.setDividerLocation(330);
        split.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(split, BorderLayout.CENTER);

        // === Event Listeners ===
        btnTambah.addActionListener(e -> tambahBarang());
        btnUpdate.addActionListener(e -> updateBarang());
        btnHapus.addActionListener(e  -> hapusBarang());
        btnBersihkan.addActionListener(e -> bersihkanForm());
        btnCari.addActionListener(e -> cariBarang());
        txtCari.addActionListener(e -> cariBarang());

        // Klik baris tabel → isi form
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                isiFormDariTabel(table.getSelectedRow());
            }
        });
    }

    // Helper: tambah baris label + field ke GridBag
    private void addRow(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field) {
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1; gbc.weightx = 0.3;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel.add(UIHelper.createLabel(label, Font.PLAIN, 12), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        panel.add(field, gbc);
    }

    private void loadSupplierCombo() {
        cmbSupplier.removeAllItems();
        cmbSupplier.addItem(new Supplier(0, "-- Pilih Supplier --", "", ""));
        supplierCtrl.getAll().forEach(cmbSupplier::addItem);
    }

    private void loadTable() {
        tableModel.setRowCount(0);
        for (Barang b : barangCtrl.getAll()) {
            tableModel.addRow(new Object[]{
                b.getIdBarang(), b.getKodeBarang(), b.getNamaBarang(),
                b.getKategori(), UIHelper.formatRupiah(b.getHargaJual()),
                b.getStok(), b.getNamaSupplier()
            });
        }
    }

    private void cariBarang() {
        String keyword = txtCari.getText().trim();
        List<Barang> hasil = keyword.isEmpty() ? barangCtrl.getAll() : barangCtrl.search(keyword);
        tableModel.setRowCount(0);
        for (Barang b : hasil) {
            tableModel.addRow(new Object[]{
                b.getIdBarang(), b.getKodeBarang(), b.getNamaBarang(),
                b.getKategori(), UIHelper.formatRupiah(b.getHargaJual()),
                b.getStok(), b.getNamaSupplier()
            });
        }
    }

    private void isiFormDariTabel(int row) {
        selectedId = (int) tableModel.getValueAt(row, 0);
        List<Barang> all = barangCtrl.getAll();
        Barang b = all.stream().filter(x -> x.getIdBarang() == selectedId).findFirst().orElse(null);
        if (b == null) return;

        txtKode.setText(b.getKodeBarang());
        txtNama.setText(b.getNamaBarang());
        txtKategori.setText(b.getKategori());
        txtHargaBeli.setText(String.valueOf((long) b.getHargaBeli()));
        txtHargaJual.setText(String.valueOf((long) b.getHargaJual()));
        txtStok.setText(String.valueOf(b.getStok()));

        // Set supplier combobox
        for (int i = 0; i < cmbSupplier.getItemCount(); i++) {
            if (cmbSupplier.getItemAt(i).getIdSupplier() == b.getIdSupplier()) {
                cmbSupplier.setSelectedIndex(i);
                break;
            }
        }
    }

    private Barang getBarangDariForm() {
        try {
            Barang b = new Barang();
            b.setIdBarang(selectedId);
            b.setKodeBarang(txtKode.getText().trim());
            b.setNamaBarang(txtNama.getText().trim());
            b.setKategori(txtKategori.getText().trim());
            b.setHargaBeli(Double.parseDouble(txtHargaBeli.getText().trim()));
            b.setHargaJual(Double.parseDouble(txtHargaJual.getText().trim()));
            b.setStok(Integer.parseInt(txtStok.getText().trim()));
            Supplier sup = (Supplier) cmbSupplier.getSelectedItem();
            b.setIdSupplier(sup != null ? sup.getIdSupplier() : 0);
            return b;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Harga dan Stok harus berupa angka!", "Input Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private void tambahBarang() {
        Barang b = getBarangDariForm();
        if (b == null || b.getNamaBarang().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama barang tidak boleh kosong!", "Validasi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (barangCtrl.insert(b)) {
            JOptionPane.showMessageDialog(this, "Barang berhasil ditambahkan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            loadTable(); bersihkanForm();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal menambahkan barang!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateBarang() {
        if (selectedId == -1) { JOptionPane.showMessageDialog(this, "Pilih barang di tabel terlebih dahulu!"); return; }
        Barang b = getBarangDariForm();
        if (b == null) return;
        if (barangCtrl.update(b)) {
            JOptionPane.showMessageDialog(this, "Barang berhasil diupdate!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            loadTable(); bersihkanForm();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal mengupdate barang!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hapusBarang() {
        if (selectedId == -1) { JOptionPane.showMessageDialog(this, "Pilih barang di tabel terlebih dahulu!"); return; }
        int konfirmasi = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus barang ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (konfirmasi == JOptionPane.YES_OPTION) {
            if (barangCtrl.delete(selectedId)) {
                JOptionPane.showMessageDialog(this, "Barang berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                loadTable(); bersihkanForm();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus! Mungkin barang sudah ada di transaksi.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void bersihkanForm() {
        selectedId = -1;
        txtKode.setText(""); txtNama.setText(""); txtKategori.setText("");
        txtHargaBeli.setText(""); txtHargaJual.setText(""); txtStok.setText("");
        cmbSupplier.setSelectedIndex(0);
        table.clearSelection();
    }
}
