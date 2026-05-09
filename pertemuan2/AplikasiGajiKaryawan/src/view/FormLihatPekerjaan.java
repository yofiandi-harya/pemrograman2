package view;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class FormLihatPekerjaan extends JDialog {

    private final DefaultTableModel pekerjaanTableModel;
    private String kodePekerjaanDipilih;

    // UI Components
    private JPanel panel1;   // tabel (atas) - hijau muda
    private JPanel panel2;   // tombol (bawah) - merah muda
    private JScrollPane jScrollPane1;
    private JTable pekerjaanTable;
    private JButton pilihButton;
    private JButton tutupButton;

    public FormLihatPekerjaan(Window parent, boolean modal) {
        super(parent, "Data Pekerjaan",
              modal ? ModalityType.APPLICATION_MODAL : ModalityType.MODELESS);
        initComponents();
        pekerjaanTableModel = (DefaultTableModel) pekerjaanTable.getModel();

        // Saat window aktif, reset pilihan
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                kodePekerjaanDipilih = "";
            }
        });
    }

    private void initComponents() {
        setSize(531, 616);
        setResizable(true);
        setLocationRelativeTo(getOwner()); // tampil di tengah layar

        // ── Panel 1 (atas) – hijau muda ──────────────────────────────
        panel1 = new JPanel(new BorderLayout(5, 5));
        panel1.setBackground(new Color(153, 255, 204));
        panel1.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                "Data Pekerjaan",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Tahoma", Font.PLAIN, 11)));

        // Tabel dengan 2 kolom: Kode (70px) dan Nama Pekerjaan
        String[] columnNames = {"Kode", "Nama Pekerjaan"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false; // tidak bisa diedit
            }
        };

        pekerjaanTable = new JTable(model);
        pekerjaanTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pekerjaanTable.getColumnModel().getColumn(0).setPreferredWidth(70);
        pekerjaanTable.getColumnModel().getColumn(0).setMinWidth(70);
        pekerjaanTable.getColumnModel().getColumn(0).setMaxWidth(70);

        jScrollPane1 = new JScrollPane(pekerjaanTable);
        panel1.add(jScrollPane1, BorderLayout.CENTER);

        // ── Panel 2 (bawah) – merah muda ─────────────────────────────
        panel2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        panel2.setBackground(new Color(255, 200, 200));
        panel2.setPreferredSize(new Dimension(0, 60));

        pilihButton = new JButton("Pilih");
        tutupButton = new JButton("Tutup");

        pilihButton.addActionListener(e -> pilihButtonActionPerformed());
        tutupButton.addActionListener(e -> dispose());

        panel2.add(pilihButton);
        panel2.add(tutupButton);

        // ── Layout utama ──────────────────────────────────────────────
        getContentPane().setLayout(new BorderLayout(5, 5));
        getContentPane().add(panel1, BorderLayout.CENTER);
        getContentPane().add(panel2, BorderLayout.SOUTH);
    }

    // ── Public Methods ────────────────────────────────────────────────

    /**
     * Mengisi tabel dengan data dari array 2D.
     * Format: { {kode, nama}, ... }
     */
    public void tampilkanData(Object[][] list) {
        DefaultTableModel dtm = (DefaultTableModel) pekerjaanTable.getModel();
        dtm.setRowCount(0);
        if (list != null && list.length > 0) {
            for (Object[] row : list) {
                dtm.addRow(row);
            }
        }
    }

    public String getKodePekerjaanDipilih() {
        return kodePekerjaanDipilih;
    }

    // ── Event Handlers ────────────────────────────────────────────────

    private void pilihButtonActionPerformed() {
        if (pekerjaanTable.getSelectedRowCount() > 0) {
            kodePekerjaanDipilih = pekerjaanTable
                    .getValueAt(pekerjaanTable.getSelectedRow(), 0)
                    .toString();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Belum ada yang dipilih");
        }
    }
}
