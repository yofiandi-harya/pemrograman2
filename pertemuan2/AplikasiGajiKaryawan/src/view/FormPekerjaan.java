package view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class FormPekerjaan extends JInternalFrame {

    // Panel
    private JPanel panel1; // Data Pekerjaan (atas) - hijau
    private JPanel panel2; // Tombol (bawah) - merah muda

    // Labels
    private JLabel kodePekerjaanLabel;
    private JLabel namaPekerjaanLabel;
    private JLabel jumlahTugasLabel;

    // Input fields
    private JTextField kodePekerjaanTextField;
    private JTextField namaPekerjaanTextField;
    private JComboBox<String> jumlahTugasComboBox;

    // Buttons
    private JButton lihatButton;
    private JButton simpanButton;
    private JButton hapusButton;
    private JButton tutupButton;

    public FormPekerjaan() {
        initComponents();
        setItemComboBox();
    }

    private void initComponents() {
        setTitle("Master Data Pekerjaan");
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setSize(480, 320);
        setLocation(30, 30);

        // ── Panel 1 (atas) – hijau muda ──────────────────────────────
        panel1 = new JPanel(null); // absolute layout
        panel1.setBackground(new Color(153, 255, 204));
        panel1.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                "Data Pekerjaan",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Tahoma", Font.PLAIN, 11)));

        // Labels
        kodePekerjaanLabel  = new JLabel("Kode Pekerjaan");
        namaPekerjaanLabel  = new JLabel("Nama Pekerjaan");
        jumlahTugasLabel    = new JLabel("Jumlah Tugas");

        kodePekerjaanLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        namaPekerjaanLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        jumlahTugasLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        kodePekerjaanLabel.setBounds(10, 30, 110, 25);
        namaPekerjaanLabel.setBounds(10, 65, 110, 25);
        jumlahTugasLabel.setBounds(10, 100, 110, 25);

        // Text Fields
        kodePekerjaanTextField  = new JTextField();
        namaPekerjaanTextField  = new JTextField();

        kodePekerjaanTextField.setBounds(130, 30, 120, 25);
        namaPekerjaanTextField.setBounds(130, 65, 250, 25);

        // ComboBox
        jumlahTugasComboBox = new JComboBox<>();
        jumlahTugasComboBox.setBounds(130, 100, 80, 25);

        // Lihat Button (sejajar kode pekerjaan)
        lihatButton = new JButton("Lihat");
        lihatButton.setBounds(260, 30, 80, 25);
        lihatButton.addActionListener(e -> lihatButtonActionPerformed());

        panel1.add(kodePekerjaanLabel);
        panel1.add(namaPekerjaanLabel);
        panel1.add(jumlahTugasLabel);
        panel1.add(kodePekerjaanTextField);
        panel1.add(namaPekerjaanTextField);
        panel1.add(jumlahTugasComboBox);
        panel1.add(lihatButton);

        // ── Panel 2 (bawah) – merah muda ─────────────────────────────
        panel2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panel2.setBackground(new Color(255, 200, 200));

        simpanButton = new JButton("Simpan");
        hapusButton  = new JButton("Hapus");
        tutupButton  = new JButton("Tutup");

        simpanButton.addActionListener(e -> simpanButtonActionPerformed());
        hapusButton.addActionListener(e  -> hapusButtonActionPerformed());
        tutupButton.addActionListener(e  -> tutupButtonActionPerformed());

        panel2.add(simpanButton);
        panel2.add(hapusButton);
        panel2.add(tutupButton);

        // ── Layout utama ──────────────────────────────────────────────
        getContentPane().setLayout(new BorderLayout(5, 5));
        getContentPane().add(panel1, BorderLayout.CENTER);
        getContentPane().add(panel2, BorderLayout.SOUTH);
        panel2.setPreferredSize(new Dimension(0, 70));
    }

    // ── Business Methods ─────────────────────────────────────────────

    private void setItemComboBox() {
        jumlahTugasComboBox.removeAllItems();
        for (int i = 1; i <= 6; i++) {
            if (i != 5) {
                jumlahTugasComboBox.addItem(Integer.toString(i));
            }
        }
    }

    public void setKodePekerjaan(String kodePekerjaan) {
        kodePekerjaanTextField.setText(kodePekerjaan);
    }

    public String getKodePekerjaan() {
        return kodePekerjaanTextField.getText();
    }

    public void setNamaPekerjaan(String namaPekerjaan) {
        namaPekerjaanTextField.setText(namaPekerjaan);
    }

    public String getNamaPekerjaan() {
        return namaPekerjaanTextField.getText();
    }

    public void setJumlahTugas(int jumlahTugas) {
        jumlahTugasComboBox.setSelectedItem(jumlahTugas);
    }

    public int getJumlahTugas() {
        return Integer.parseInt(
                jumlahTugasComboBox.getSelectedItem().toString());
    }

    // ── Event Handlers ────────────────────────────────────────────────

    private void lihatButtonActionPerformed() {
        // Contoh data dummy untuk demo
        Object[][] dummyData = {
            {"1415", "admin"},
            {"2231", "sales"},
            {"4444", "direktur"},
            {"4545", "KULI"}
        };

        FormLihatPekerjaan formLihat = new FormLihatPekerjaan(
                SwingUtilities.getWindowAncestor(this), true);
        formLihat.tampilkanData(dummyData);
        formLihat.setVisible(true);

        String kodedipilih = formLihat.getKodePekerjaanDipilih();
        if (kodedipilih != null && !kodedipilih.isEmpty()) {
            kodePekerjaanTextField.setText(kodedipilih);
            // Cari nama dari dummy data
            for (Object[] row : dummyData) {
                if (row[0].toString().equals(kodedipilih)) {
                    namaPekerjaanTextField.setText(row[1].toString());
                    break;
                }
            }
        }
    }

    private void simpanButtonActionPerformed() {
        String kode = getKodePekerjaan().trim();
        String nama = getNamaPekerjaan().trim();
        if (kode.isEmpty() || nama.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Kode dan Nama Pekerjaan tidak boleh kosong!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(this,
                "Data berhasil disimpan!\nKode: " + kode + "\nNama: " + nama,
                "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    private void hapusButtonActionPerformed() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Yakin ingin menghapus data ini?",
                "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            kodePekerjaanTextField.setText("");
            namaPekerjaanTextField.setText("");
            jumlahTugasComboBox.setSelectedIndex(0);
            JOptionPane.showMessageDialog(this, "Data berhasil dihapus.");
        }
    }

    private void tutupButtonActionPerformed() {
        dispose();
    }
}
