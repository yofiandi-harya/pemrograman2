package Pertemuan9;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;

public class FormBarang extends JInternalFrame {

    JTextField kodeTF, namaTF, hargaTF, stokTF;
    DefaultTableModel tableModel;
    JTable table;
    private String kodeDipilih = null;

    public FormBarang() {
        super("Data Barang", true, true, true, true);
        setSize(650, 500);
        setLayout(new BorderLayout(5,5));

        JPanel form = new JPanel(new GridLayout(6,2,5,5));
        form.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10,10,5,10),
            BorderFactory.createTitledBorder("Input Data Barang")));

        form.add(new JLabel("Kode Barang:")); kodeTF = new JTextField(); form.add(kodeTF);
        form.add(new JLabel("Nama Barang:")); namaTF = new JTextField(); form.add(namaTF);
        form.add(new JLabel("Harga:"));       hargaTF = new JTextField(); form.add(hargaTF);
        form.add(new JLabel("Stok:"));        stokTF  = new JTextField(); form.add(stokTF);

        JButton simpanBtn = new JButton("Simpan");
        JButton tampilBtn = new JButton("Tampil");
        JButton updateBtn = new JButton("Update");
        JButton hapusBtn  = new JButton("Hapus");
        form.add(simpanBtn); form.add(tampilBtn);
        form.add(updateBtn); form.add(hapusBtn);

        String[] kolom = {"Kode", "Nama Barang", "Harga", "Stok"};
        tableModel = new DefaultTableModel(kolom, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(0,10,10,10),
            BorderFactory.createTitledBorder("Tabel Barang")));

        add(form, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                int r = table.getSelectedRow();
                kodeDipilih = tableModel.getValueAt(r,0).toString();
                kodeTF.setText(kodeDipilih); kodeTF.setEditable(false);
                namaTF.setText(tableModel.getValueAt(r,1).toString());
                hargaTF.setText(tableModel.getValueAt(r,2).toString());
                stokTF.setText(tableModel.getValueAt(r,3).toString());
            }
        });

        simpanBtn.addActionListener(e -> {
            try {
                boolean ok = DatabaseHelper.simpanBarang(
                    kodeTF.getText().trim(), namaTF.getText().trim(),
                    Double.parseDouble(hargaTF.getText().trim()),
                    Integer.parseInt(stokTF.getText().trim()));
                JOptionPane.showMessageDialog(this, ok ? "Berhasil disimpan!" : "Gagal! Kode sudah ada.");
                if (ok) { clearFields(); refreshTabel(); }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Harga dan Stok harus angka!");
            }
        });

        tampilBtn.addActionListener(e -> refreshTabel());

        updateBtn.addActionListener(e -> {
            if (kodeDipilih == null) { JOptionPane.showMessageDialog(this,"Pilih data dulu!"); return; }
            try {
                boolean ok = DatabaseHelper.updateBarang(
                    kodeDipilih, namaTF.getText().trim(),
                    Double.parseDouble(hargaTF.getText().trim()),
                    Integer.parseInt(stokTF.getText().trim()));
                JOptionPane.showMessageDialog(this, ok ? "Berhasil diupdate!" : "Gagal update.");
                if (ok) { clearFields(); refreshTabel(); }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Harga dan Stok harus angka!");
            }
        });

        hapusBtn.addActionListener(e -> {
            if (kodeDipilih == null) { JOptionPane.showMessageDialog(this,"Pilih data dulu!"); return; }
            int conf = JOptionPane.showConfirmDialog(this,"Yakin hapus "+kodeDipilih+"?","Hapus",JOptionPane.YES_NO_OPTION);
            if (conf == JOptionPane.YES_OPTION) {
                boolean ok = DatabaseHelper.hapusBarang(kodeDipilih);
                JOptionPane.showMessageDialog(this, ok ? "Berhasil dihapus!" : "Gagal hapus.");
                if (ok) { clearFields(); refreshTabel(); }
            }
        });

        setVisible(true);
    }

    void refreshTabel() {
        tableModel.setRowCount(0);
        try (Connection c = DatabaseHelper.connect();
             ResultSet rs = DatabaseHelper.getAllBarang(c)) {
            while (rs.next())
                tableModel.addRow(new Object[]{rs.getString("kode_barang"),rs.getString("nama_barang"),rs.getDouble("harga"),rs.getInt("stok")});
        } catch (SQLException ex) { System.out.println(ex.getMessage()); }
    }

    void clearFields() {
        kodeTF.setText(""); namaTF.setText(""); hargaTF.setText(""); stokTF.setText("");
        kodeTF.setEditable(true); kodeDipilih = null;
    }
}
