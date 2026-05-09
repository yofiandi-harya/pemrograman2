package view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class FormKaryawan extends JInternalFrame {

    private JPanel panel1;
    private JPanel panel2;

    private JLabel nimLabel;
    private JLabel namaLabel;
    private JLabel jenisKelaminLabel;
    private JLabel jurusanLabel;
    private JLabel semesterLabel;
    private JLabel biayaLabel;

    private JTextField nimTextField;
    private JTextField namaTextField;
    private JTextField semesterTextField;
    private JTextField biayaTextField;

    private JRadioButton lakiRadioButton;
    private JRadioButton perempuanRadioButton;
    private ButtonGroup jenisKelaminGroup;

    private JComboBox<String> jurusanComboBox;

    private JButton hitungButton;
    private JButton hapusButton;
    private JButton keTabelButton;

    public FormKaryawan() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Master Data Karyawan");
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setSize(500, 350);
        setLocation(60, 60);

        // ── Panel 1 (atas) – kuning ───────────────────────────────────
        panel1 = new JPanel(null);
        panel1.setBackground(new Color(255, 255, 153));
        panel1.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                "Data Karyawan",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Tahoma", Font.PLAIN, 11)));

        // Labels
        nimLabel            = new JLabel("Nim");
        namaLabel           = new JLabel("Nama");
        jenisKelaminLabel   = new JLabel("J.Kelamin");
        jurusanLabel        = new JLabel("Jurusan");
        semesterLabel       = new JLabel("Semester");
        biayaLabel          = new JLabel("Biaya/bln");

        nimLabel.setBounds(10, 28, 80, 22);
        namaLabel.setBounds(10, 58, 80, 22);
        jenisKelaminLabel.setBounds(10, 88, 80, 22);
        jurusanLabel.setBounds(10, 140, 80, 22);
        semesterLabel.setBounds(10, 170, 80, 22);
        biayaLabel.setBounds(210, 140, 70, 22);

        // TextFields
        nimTextField      = new JTextField();
        namaTextField     = new JTextField();
        semesterTextField = new JTextField();
        biayaTextField    = new JTextField();

        nimTextField.setBounds(100, 28, 150, 22);
        namaTextField.setBounds(100, 58, 250, 22);
        semesterTextField.setBounds(100, 170, 100, 22);
        biayaTextField.setBounds(285, 140, 100, 22);

        // Radio buttons
        lakiRadioButton     = new JRadioButton("Laki-laki");
        perempuanRadioButton = new JRadioButton("Perempuan");
        jenisKelaminGroup   = new ButtonGroup();
        jenisKelaminGroup.add(lakiRadioButton);
        jenisKelaminGroup.add(perempuanRadioButton);
        lakiRadioButton.setBounds(100, 88, 100, 22);
        perempuanRadioButton.setBounds(100, 112, 100, 22);
        lakiRadioButton.setOpaque(false);
        perempuanRadioButton.setOpaque(false);

        // ComboBox Jurusan
        String[] jurusanList = {"Teknik Informatika", "Sistem Informasi", "Manajemen"};
        jurusanComboBox = new JComboBox<>(jurusanList);
        jurusanComboBox.setBounds(100, 140, 150, 22);

        panel1.add(nimLabel);           panel1.add(nimTextField);
        panel1.add(namaLabel);          panel1.add(namaTextField);
        panel1.add(jenisKelaminLabel);  panel1.add(lakiRadioButton);
        panel1.add(perempuanRadioButton);
        panel1.add(jurusanLabel);       panel1.add(jurusanComboBox);
        panel1.add(semesterLabel);      panel1.add(semesterTextField);
        panel1.add(biayaLabel);         panel1.add(biayaTextField);

        // ── Panel 2 (bawah) – ungu ───────────────────────────────────
        panel2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 12));
        panel2.setBackground(new Color(180, 100, 220));

        hitungButton  = new JButton("HITUNG");
        hapusButton   = new JButton("HAPUS");
        keTabelButton = new JButton("Ke Tabel");

        hapusButton.addActionListener(e -> {
            nimTextField.setText("");
            namaTextField.setText("");
            semesterTextField.setText("");
            biayaTextField.setText("");
            jenisKelaminGroup.clearSelection();
        });

        tutupSetup();

        panel2.add(hitungButton);
        panel2.add(hapusButton);
        panel2.add(keTabelButton);

        getContentPane().setLayout(new BorderLayout(5, 5));
        getContentPane().add(panel1, BorderLayout.CENTER);
        getContentPane().add(panel2, BorderLayout.SOUTH);
        panel2.setPreferredSize(new Dimension(0, 65));
    }

    private void tutupSetup() {
        // Tombol Tutup dipanggil via close button di title bar
    }
}
