package Pertemuan9;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;

public class FormTransaksi extends JInternalFrame {

    JTextField idTF, customerTF, tanggalTF, totalTF, kodeBarangTF, jumlahTF;
    DefaultTableModel tableModel, detailModel;
    JTable tableTrans, tableDetail;
    private double totalTransaksi = 0;

    public FormTransaksi() {
        super("Transaksi Penjualan", true, true, true, true);
        setSize(750, 580);
        setLayout(new BorderLayout(5,5));

        // Panel atas - header transaksi
        JPanel formPanel = new JPanel(new GridLayout(4,4,5,5));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10,10,5,10),
            BorderFactory.createTitledBorder("Input Transaksi")));

        formPanel.add(new JLabel("ID Transaksi:")); idTF       = new JTextField(); formPanel.add(idTF);
        formPanel.add(new JLabel("Kode Customer:")); customerTF = new JTextField(); formPanel.add(customerTF);
        formPanel.add(new JLabel("Tanggal (YYYY-MM-DD):")); tanggalTF = new JTextField(LocalDate.now().toString()); formPanel.add(tanggalTF);
        formPanel.add(new JLabel("Total:")); totalTF = new JTextField("0"); totalTF.setEditable(false); formPanel.add(totalTF);

        formPanel.add(new JLabel("Kode Barang:")); kodeBarangTF = new JTextField(); formPanel.add(kodeBarangTF);
        formPanel.add(new JLabel("Jumlah:"));      jumlahTF     = new JTextField(); formPanel.add(jumlahTF);

        JButton addItemBtn   = new JButton("Tambah Item");
        JButton simpanBtn    = new JButton("Simpan Transaksi");
        JButton tampilBtn    = new JButton("Tampil Semua");
        JButton resetBtn     = new JButton("Reset");
        formPanel.add(addItemBtn); formPanel.add(simpanBtn);
        formPanel.add(tampilBtn);  formPanel.add(resetBtn);

        // Panel tengah - detail item
        String[] detKolom = {"Kode Barang","Jumlah","Subtotal"};
        detailModel = new DefaultTableModel(detKolom, 0);
        tableDetail = new JTable(detailModel);
        JScrollPane scrollDetail = new JScrollPane(tableDetail);
        scrollDetail.setBorder(BorderFactory.createTitledBorder("Detail Item"));
        scrollDetail.setPreferredSize(new Dimension(700,150));

        // Panel bawah - tabel semua transaksi
        String[] kolom = {"ID Transaksi","Customer","Tanggal","Total"};
        tableModel = new DefaultTableModel(kolom, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tableTrans = new JTable(tableModel);
        JScrollPane scrollTrans = new JScrollPane(tableTrans);
        scrollTrans.setBorder(BorderFactory.createTitledBorder("Tabel Transaksi"));

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(scrollDetail, BorderLayout.NORTH);
        centerPanel.add(scrollTrans, BorderLayout.CENTER);

        add(formPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        // Tambah item ke detail
        addItemBtn.addActionListener(e -> {
            String kodeBarang = kodeBarangTF.getText().trim();
            String jmlStr     = jumlahTF.getText().trim();
            if (kodeBarang.isEmpty() || jmlStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Isi kode barang dan jumlah!"); return;
            }
            try {
                int jumlah = Integer.parseInt(jmlStr);
                // Cek harga barang
                try (Connection c = DatabaseHelper.connect();
                     PreparedStatement ps = c.prepareStatement("SELECT harga, stok, nama_barang FROM barang WHERE kode_barang=?")) {
                    ps.setString(1, kodeBarang);
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next()) { JOptionPane.showMessageDialog(this,"Kode barang tidak ditemukan!"); return; }
                    int stok = rs.getInt("stok");
                    if (jumlah > stok) { JOptionPane.showMessageDialog(this,"Stok tidak cukup! Stok: "+stok); return; }
                    double harga    = rs.getDouble("harga");
                    double subtotal = harga * jumlah;
                    totalTransaksi += subtotal;
                    detailModel.addRow(new Object[]{kodeBarang, jumlah, subtotal});
                    totalTF.setText(String.valueOf(totalTransaksi));
                    kodeBarangTF.setText(""); jumlahTF.setText("");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,"Jumlah harus angka!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,"Error: "+ex.getMessage());
            }
        });

        // Simpan transaksi
        simpanBtn.addActionListener(e -> {
            String id   = idTF.getText().trim();
            String cust = customerTF.getText().trim();
            String tgl  = tanggalTF.getText().trim();
            if (id.isEmpty() || cust.isEmpty() || detailModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this,"Lengkapi data transaksi dan tambahkan item!"); return;
            }
            boolean ok = DatabaseHelper.simpanTransaksi(id, cust, tgl, totalTransaksi);
            if (!ok) { JOptionPane.showMessageDialog(this,"Gagal simpan! ID transaksi mungkin sudah ada."); return; }
            for (int i = 0; i < detailModel.getRowCount(); i++) {
                String kb  = detailModel.getValueAt(i,0).toString();
                int jml    = Integer.parseInt(detailModel.getValueAt(i,1).toString());
                double sub = Double.parseDouble(detailModel.getValueAt(i,2).toString());
                DatabaseHelper.simpanDetailTransaksi(id, kb, jml, sub);
            }
            JOptionPane.showMessageDialog(this,"Transaksi berhasil disimpan!");
            resetForm(); refreshTabel();
        });

        tampilBtn.addActionListener(e -> refreshTabel());
        resetBtn.addActionListener(e -> resetForm());
        setVisible(true);
    }

    void refreshTabel() {
        tableModel.setRowCount(0);
        try (Connection c = DatabaseHelper.connect();
             ResultSet rs = DatabaseHelper.getAllTransaksi(c)) {
            while (rs.next())
                tableModel.addRow(new Object[]{rs.getString("id_transaksi"),rs.getString("nama_customer"),rs.getString("tanggal"),rs.getDouble("total")});
        } catch (SQLException ex) { System.out.println(ex.getMessage()); }
    }

    void resetForm() {
        idTF.setText(""); customerTF.setText("");
        tanggalTF.setText(LocalDate.now().toString());
        totalTF.setText("0"); totalTransaksi = 0;
        detailModel.setRowCount(0);
    }
}
