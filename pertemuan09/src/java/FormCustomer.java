package Pertemuan9;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;

public class FormCustomer extends JInternalFrame {

    JTextField kodeTF, namaTF, alamatTF, telpTF;
    DefaultTableModel tableModel;
    JTable table;
    private String kodeDipilih = null;

    public FormCustomer() {
        super("Data Customer", true, true, true, true);
        setSize(650, 500);
        setLayout(new BorderLayout(5,5));

        JPanel form = new JPanel(new GridLayout(6,2,5,5));
        form.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10,10,5,10),
            BorderFactory.createTitledBorder("Input Data Customer")));

        form.add(new JLabel("Kode Customer:")); kodeTF   = new JTextField(); form.add(kodeTF);
        form.add(new JLabel("Nama Customer:")); namaTF   = new JTextField(); form.add(namaTF);
        form.add(new JLabel("Alamat:"));        alamatTF = new JTextField(); form.add(alamatTF);
        form.add(new JLabel("Telepon:"));       telpTF   = new JTextField(); form.add(telpTF);

        JButton simpanBtn = new JButton("Simpan");
        JButton tampilBtn = new JButton("Tampil");
        JButton updateBtn = new JButton("Update");
        JButton hapusBtn  = new JButton("Hapus");
        form.add(simpanBtn); form.add(tampilBtn);
        form.add(updateBtn); form.add(hapusBtn);

        String[] kolom = {"Kode", "Nama Customer", "Alamat", "Telepon"};
        tableModel = new DefaultTableModel(kolom, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(0,10,10,10),
            BorderFactory.createTitledBorder("Tabel Customer")));

        add(form, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                int r = table.getSelectedRow();
                kodeDipilih = tableModel.getValueAt(r,0).toString();
                kodeTF.setText(kodeDipilih); kodeTF.setEditable(false);
                namaTF.setText(tableModel.getValueAt(r,1).toString());
                alamatTF.setText(tableModel.getValueAt(r,2).toString());
                telpTF.setText(tableModel.getValueAt(r,3).toString());
            }
        });

        simpanBtn.addActionListener(e -> {
            boolean ok = DatabaseHelper.simpanCustomer(kodeTF.getText().trim(),namaTF.getText().trim(),alamatTF.getText().trim(),telpTF.getText().trim());
            JOptionPane.showMessageDialog(this, ok ? "Berhasil disimpan!" : "Gagal! Kode sudah ada.");
            if (ok) { clearFields(); refreshTabel(); }
        });
        tampilBtn.addActionListener(e -> refreshTabel());
        updateBtn.addActionListener(e -> {
            if (kodeDipilih == null) { JOptionPane.showMessageDialog(this,"Pilih data dulu!"); return; }
            boolean ok = DatabaseHelper.updateCustomer(kodeDipilih,namaTF.getText().trim(),alamatTF.getText().trim(),telpTF.getText().trim());
            JOptionPane.showMessageDialog(this, ok ? "Berhasil diupdate!" : "Gagal.");
            if (ok) { clearFields(); refreshTabel(); }
        });
        hapusBtn.addActionListener(e -> {
            if (kodeDipilih == null) { JOptionPane.showMessageDialog(this,"Pilih data dulu!"); return; }
            int conf = JOptionPane.showConfirmDialog(this,"Yakin hapus?","Hapus",JOptionPane.YES_NO_OPTION);
            if (conf == JOptionPane.YES_OPTION) {
                boolean ok = DatabaseHelper.hapusCustomer(kodeDipilih);
                JOptionPane.showMessageDialog(this, ok ? "Berhasil dihapus!" : "Gagal.");
                if (ok) { clearFields(); refreshTabel(); }
            }
        });
        setVisible(true);
    }

    void refreshTabel() {
        tableModel.setRowCount(0);
        try (Connection c = DatabaseHelper.connect();
             ResultSet rs = DatabaseHelper.getAllCustomer(c)) {
            while (rs.next())
                tableModel.addRow(new Object[]{rs.getString("kode_customer"),rs.getString("nama_customer"),rs.getString("alamat"),rs.getString("telepon")});
        } catch (SQLException ex) { System.out.println(ex.getMessage()); }
    }

    void clearFields() {
        kodeTF.setText(""); namaTF.setText(""); alamatTF.setText(""); telpTF.setText("");
        kodeTF.setEditable(true); kodeDipilih = null;
    }
}
