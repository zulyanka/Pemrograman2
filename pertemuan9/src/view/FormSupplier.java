// ============================================
// FILE    : view/FormSupplier.java
// ============================================
package view;

import controller.SupplierController;
import model.Supplier;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class FormSupplier extends JPanel {

    private SupplierController ctrl = new SupplierController();
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtNama, txtTelepon, txtAlamat;
    private JButton btnTambah, btnUpdate, btnHapus, btnBersih;
    private int selectedId = -1;

    public FormSupplier() {
        setLayout(new BorderLayout());
        setBackground(UIHelper.BG_LIGHT);
        buildUI();
        loadTable();
    }

    private void buildUI() {
        add(UIHelper.createHeaderPanel("🏭 Manajemen Supplier", "Kelola data pemasok barang"), BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240)),
            new EmptyBorder(20, 20, 20, 20)
        ));
        formPanel.setPreferredSize(new Dimension(300, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 5, 6, 5);

        txtNama    = UIHelper.createTextField();
        txtTelepon = UIHelper.createTextField();
        txtAlamat  = UIHelper.createTextField();

        addRow(formPanel, gbc, 0, "Nama Supplier", txtNama);
        addRow(formPanel, gbc, 1, "Telepon", txtTelepon);
        addRow(formPanel, gbc, 2, "Alamat", txtAlamat);

        JPanel btnPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        btnPanel.setOpaque(false);
        btnTambah = UIHelper.createButton("➕ Tambah", UIHelper.SUCCESS);
        btnUpdate = UIHelper.createButton("✏ Update",  UIHelper.WARNING);
        btnHapus  = UIHelper.createButton("🗑 Hapus",  UIHelper.DANGER);
        btnBersih = UIHelper.createButton("🔄 Bersih", UIHelper.PRIMARY);
        btnPanel.add(btnTambah); btnPanel.add(btnUpdate);
        btnPanel.add(btnHapus);  btnPanel.add(btnBersih);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 5, 5);
        formPanel.add(btnPanel, gbc);

        tableModel = new DefaultTableModel(new String[]{"ID", "Nama Supplier", "Telepon", "Alamat"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        UIHelper.styleTable(table);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new EmptyBorder(10, 0, 0, 0));

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, formPanel, scroll);
        split.setDividerLocation(310);
        split.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(split, BorderLayout.CENTER);

        btnTambah.addActionListener(e -> tambah());
        btnUpdate.addActionListener(e -> update());
        btnHapus.addActionListener(e  -> hapus());
        btnBersih.addActionListener(e -> bersih());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                int row = table.getSelectedRow();
                selectedId = (int) tableModel.getValueAt(row, 0);
                txtNama.setText((String) tableModel.getValueAt(row, 1));
                txtTelepon.setText((String) tableModel.getValueAt(row, 2));
                txtAlamat.setText((String) tableModel.getValueAt(row, 3));
            }
        });
    }

    private void addRow(JPanel p, GridBagConstraints g, int row, String label, JComponent field) {
        g.gridx = 0; g.gridy = row; g.gridwidth = 1; g.weightx = 0.35;
        p.add(UIHelper.createLabel(label, Font.PLAIN, 12), g);
        g.gridx = 1; g.weightx = 0.65;
        p.add(field, g);
    }

    private void loadTable() {
        tableModel.setRowCount(0);
        ctrl.getAll().forEach(s -> tableModel.addRow(new Object[]{
            s.getIdSupplier(), s.getNamaSupplier(), s.getTelepon(), s.getAlamat()
        }));
    }

    private void tambah() {
        if (txtNama.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this, "Nama supplier tidak boleh kosong!"); return; }
        Supplier s = new Supplier(0, txtNama.getText().trim(), txtTelepon.getText().trim(), txtAlamat.getText().trim());
        if (ctrl.insert(s)) { JOptionPane.showMessageDialog(this, "Supplier berhasil ditambahkan!"); loadTable(); bersih(); }
        else JOptionPane.showMessageDialog(this, "Gagal menambahkan supplier!", "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void update() {
        if (selectedId == -1) { JOptionPane.showMessageDialog(this, "Pilih supplier terlebih dahulu!"); return; }
        Supplier s = new Supplier(selectedId, txtNama.getText().trim(), txtTelepon.getText().trim(), txtAlamat.getText().trim());
        if (ctrl.update(s)) { JOptionPane.showMessageDialog(this, "Supplier berhasil diupdate!"); loadTable(); bersih(); }
        else JOptionPane.showMessageDialog(this, "Gagal update!", "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void hapus() {
        if (selectedId == -1) { JOptionPane.showMessageDialog(this, "Pilih supplier terlebih dahulu!"); return; }
        if (JOptionPane.showConfirmDialog(this, "Yakin hapus supplier ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if (ctrl.delete(selectedId)) { JOptionPane.showMessageDialog(this, "Supplier berhasil dihapus!"); loadTable(); bersih(); }
            else JOptionPane.showMessageDialog(this, "Gagal menghapus!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void bersih() {
        selectedId = -1; txtNama.setText(""); txtTelepon.setText(""); txtAlamat.setText(""); table.clearSelection();
    }
}
