import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;

public class Main extends JFrame {

    JTextField nimTF, namaTF, nil1TF, nil2TF, rataTF;
    DefaultTableModel tableModel;
    JTable table;

    public Main() {
        DatabaseHelper.initDB();

        setTitle("Data Nilai Mahasiswa");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(5, 5));

        // Panel form input (atas)
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        formPanel.add(new JLabel("NIM:"));       nimTF  = new JTextField(); formPanel.add(nimTF);
        formPanel.add(new JLabel("Nama:"));      namaTF = new JTextField(); formPanel.add(namaTF);
        formPanel.add(new JLabel("Nilai 1:"));   nil1TF = new JTextField(); formPanel.add(nil1TF);
        formPanel.add(new JLabel("Nilai 2:"));   nil2TF = new JTextField(); formPanel.add(nil2TF);
        formPanel.add(new JLabel("Rata-rata:")); rataTF = new JTextField(); rataTF.setEditable(false); formPanel.add(rataTF);

        // Panel tombol (tengah)
        JPanel btnPanel = new JPanel(new FlowLayout());
        JButton tambahBtn = new JButton("Tambah");
        JButton cariBtn   = new JButton("Cari");
        JButton updateBtn = new JButton("Update");
        JButton hapusBtn  = new JButton("Hapus");
        JButton tampilBtn = new JButton("Tampil Semua");
        btnPanel.add(tambahBtn);
        btnPanel.add(cariBtn);
        btnPanel.add(updateBtn);
        btnPanel.add(hapusBtn);
        btnPanel.add(tampilBtn);

        // Tabel hasil (bawah)
        String[] kolom = {"NIM", "Nama", "Nilai 1", "Nilai 2", "Rata-rata"};
        tableModel = new DefaultTableModel(kolom, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        // Klik baris tabel -> isi form otomatis
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = table.getSelectedRow();
                nimTF.setText(tableModel.getValueAt(row, 0).toString());
                namaTF.setText(tableModel.getValueAt(row, 1).toString());
                nil1TF.setText(tableModel.getValueAt(row, 2).toString());
                nil2TF.setText(tableModel.getValueAt(row, 3).toString());
                rataTF.setText(tableModel.getValueAt(row, 4).toString());
            }
        });

        add(formPanel, BorderLayout.NORTH);
        add(btnPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        // Aksi Tambah
        tambahBtn.addActionListener(e -> {
            try {
                DatabaseHelper.tambahData(
                    nimTF.getText(), namaTF.getText(),
                    Double.parseDouble(nil1TF.getText()),
                    Double.parseDouble(nil2TF.getText())
                );
                JOptionPane.showMessageDialog(this, "Data berhasil ditambahkan!");
                clearFields();
                loadSemuaData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Input tidak valid atau NIM sudah ada!");
            }
        });

        // Aksi Cari (by nama, tampil di tabel)
        cariBtn.addActionListener(e -> {
            tableModel.setRowCount(0);
            try (Connection conn = DatabaseHelper.connect();
                 ResultSet rs = DatabaseHelper.cariData(conn, namaTF.getText())) {
                boolean ada = false;
                while (rs.next()) {
                    ada = true;
                    tableModel.addRow(new Object[]{
                        rs.getString("nim"),
                        rs.getString("nama"),
                        rs.getString("nil1"),
                        rs.getString("nil2"),
                        rs.getString("rata")
                    });
                }
                if (!ada) {
                    JOptionPane.showMessageDialog(this, "Data tidak ada/Salah",
                        "Informasi", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException ex) {
                System.out.println("Koneksi gagal: " + ex.toString());
            }
        });

        // Aksi Update
        updateBtn.addActionListener(e -> {
            try {
                DatabaseHelper.updateData(
                    nimTF.getText(), namaTF.getText(),
                    Double.parseDouble(nil1TF.getText()),
                    Double.parseDouble(nil2TF.getText())
                );
                JOptionPane.showMessageDialog(this, "Data berhasil diupdate!");
                loadSemuaData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Input tidak valid!");
            }
        });

        // Aksi Hapus
        hapusBtn.addActionListener(e -> {
            if (nimTF.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Pilih data dari tabel dulu!");
                return;
            }
            int konfirmasi = JOptionPane.showConfirmDialog(this,
                "Yakin hapus data NIM: " + nimTF.getText() + "?",
                "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (konfirmasi == JOptionPane.YES_OPTION) {
                DatabaseHelper.hapusData(nimTF.getText());
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
                clearFields();
                loadSemuaData();
            }
        });

        // Aksi Tampil Semua
        tampilBtn.addActionListener(e -> loadSemuaData());

        // Load data saat pertama buka
        loadSemuaData();

        setVisible(true);
    }

    void loadSemuaData() {
        tableModel.setRowCount(0);
        try (Connection conn = DatabaseHelper.connect();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM datanilai")) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getString("nim"),
                    rs.getString("nama"),
                    rs.getString("nil1"),
                    rs.getString("nil2"),
                    rs.getString("rata")
                });
            }
        } catch (SQLException e) {
            System.out.println("Load data gagal: " + e.getMessage());
        }
    }

    void clearFields() {
        nimTF.setText(""); namaTF.setText("");
        nil1TF.setText(""); nil2TF.setText(""); rataTF.setText("");
    }

    public static void main(String[] args) {
        new Main();
    }
}