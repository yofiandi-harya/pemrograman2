package Pertemuan9;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;

public class FormInventory extends JInternalFrame {

    DefaultTableModel tableModel;
    JTable table;

    public FormInventory() {
        super("Inventory Barang", true, true, true, true);
        setSize(550, 400);
        setLayout(new BorderLayout(5,5));

        String[] kolom = {"Kode Barang","Nama Barang","Harga","Stok"};
        tableModel = new DefaultTableModel(kolom, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10,10,10,10),
            BorderFactory.createTitledBorder("Inventory (Urut Stok Terkecil)")));

        JButton refreshBtn = new JButton("Refresh Inventory");
        refreshBtn.addActionListener(e -> refreshTabel());

        add(refreshBtn, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        refreshTabel();
        setVisible(true);
    }

    void refreshTabel() {
        tableModel.setRowCount(0);
        try (Connection c = DatabaseHelper.connect();
             ResultSet rs = DatabaseHelper.getInventory(c)) {
            while (rs.next()) {
                String stokStatus = rs.getInt("stok") < 5 ? rs.getInt("stok")+" ⚠️" : String.valueOf(rs.getInt("stok"));
                tableModel.addRow(new Object[]{rs.getString("kode_barang"),rs.getString("nama_barang"),rs.getDouble("harga"),stokStatus});
            }
        } catch (SQLException ex) { System.out.println(ex.getMessage()); }
    }
}
