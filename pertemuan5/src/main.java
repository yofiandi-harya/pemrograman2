import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;

public class Main extends JFrame {

    JTextField nimTF, namaTF, semesterTF, kelasTF;
    DefaultTableModel tableModel;

    public Main() {
        DatabaseHelper.initDB();

        setTitle("Data Mahasiswa - Pertemuan 5");
        setSize(620, 480);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(5, 5));

        // ===== Panel Form Input =====
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Input Data Mahasiswa"));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 10, 5, 10),
            BorderFactory.createTitledBorder("Input Data Mahasiswa")));

        formPanel.add(new JLabel("NIM:"));
        nimTF = new JTextField(); formPanel.add(nimTF);

        formPanel.add(new JLabel("Nama:"));
        namaTF = new JTextField(); formPanel.add(namaTF);

        formPanel.add(new JLabel("Semester:"));
        semesterTF = new JTextField(); formPanel.add(semesterTF);

        formPanel.add(new JLabel("Kelas:"));
        kelasTF = new JTextField(); formPanel.add(kelasTF);

        // Tombol
        JButton simpanBtn = new JButton("Simpan");
        JButton tampilBtn = new JButton("Tampil Data");
        formPanel.add(simpanBtn);
        formPanel.add(tampilBtn);

        // ===== Tabel =====
        String[] kolom = {"NIM", "Nama", "Semester", "Kelas"};
        tableModel = new DefaultTableModel(kolom, 0);
        JTable table = new JTable(tableModel);
        table.setRowHeight(22);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(0, 10, 10, 10),
            BorderFactory.createTitledBorder("Tabel Data Mahasiswa")));

        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // ===== Aksi Simpan =====
        simpanBtn.addActionListener(e -> {
            String nim      = nimTF.getText().trim();
            String nama     = namaTF.getText().trim();
            String semStr   = semesterTF.getText().trim();
            String kelas    = kelasTF.getText().trim();

            if (nim.isEmpty() || nama.isEmpty() || semStr.isEmpty() || kelas.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Semua field harus diisi!", "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                int semester = Integer.parseInt(semStr);
                boolean berhasil = DatabaseHelper.simpanData(nim, nama, semester, kelas);
                if (berhasil) {
                    JOptionPane.showMessageDialog(this, "Data berhasil disimpan!");
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Gagal menyimpan! NIM mungkin sudah ada.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                    "Semester harus berupa angka!", "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            }
        });

        // ===== Aksi Tampil Data =====
        tampilBtn.addActionListener(e -> {
            tableModel.setRowCount(0);
            try (Connection conn = DatabaseHelper.connect();
                 ResultSet rs = DatabaseHelper.getAllData(conn)) {
                while (rs.next()) {
                    tableModel.addRow(new Object[]{
                        rs.getString("nim"),
                        rs.getString("nama"),
                        rs.getInt("semester"),
                        rs.getString("kelas")
                    });
                }
            } catch (SQLException ex) {
                System.out.println("Gagal load data: " + ex.getMessage());
            }
        });

        setVisible(true);
    }

    void clearFields() {
        nimTF.setText(""); namaTF.setText("");
        semesterTF.setText(""); kelasTF.setText("");
        nimTF.requestFocus();
    }

    public static void main(String[] args) {
        new Main();
    }
}