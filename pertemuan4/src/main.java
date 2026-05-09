import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class Main extends JFrame {

    JTextField nimTF, namaTF, nilaiTF;
    DefaultTableModel tableModel;

    public Main() {
        setTitle("Data Mahasiswa");
        setSize(600, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(5, 5));

        // ===== Panel Form (Kiri) =====
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 5));

        formPanel.add(new JLabel("NIM:"));
        nimTF = new JTextField(); formPanel.add(nimTF);

        formPanel.add(new JLabel("Nama Mahasiswa:"));
        namaTF = new JTextField(); formPanel.add(namaTF);

        formPanel.add(new JLabel("Nilai:"));
        nilaiTF = new JTextField(); formPanel.add(nilaiTF);

        // Tombol TABEL
        JButton tabelBtn = new JButton("TABEL");
        formPanel.add(new JLabel()); // spacer
        formPanel.add(tabelBtn);

        // ===== Tabel (Bawah) =====
        String[] kolom = {"NIM", "Nama Mahasiswa", "Nilai"};
        tableModel = new DefaultTableModel(kolom, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // ===== Aksi Tombol TABEL =====
        tabelBtn.addActionListener(e -> {
            String nim   = nimTF.getText().trim();
            String nama  = namaTF.getText().trim();
            String nilai = nilaiTF.getText().trim();

            if (nim.isEmpty() || nama.isEmpty() || nilai.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Semua field harus diisi!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            tableModel.addRow(new Object[]{nim, nama, nilai});

            // Kosongkan form setelah input
            nimTF.setText("");
            namaTF.setText("");
            nilaiTF.setText("");
            nimTF.requestFocus();
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new Main();
    }
}