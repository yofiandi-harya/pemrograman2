import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.File;
import java.io.InputStream;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class MainFrame extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnCetak;
    private JButton btnExportPDF;
    private JButton btnRefresh;
    private JLabel lblTotal;

    public MainFrame() {
        initDatabase();
        initUI();
        loadData();
    }

    // ─── Inisialisasi database ────────────────────────────────────────────────
    private void initDatabase() {
        DatabaseHelper.initDatabase();
    }

    // ─── Bangun tampilan UI ───────────────────────────────────────────────────
    private void initUI() {
        setTitle("Data Mahasiswa - Pertemuan 7");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 520);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 0));

        // ── Panel Header ──────────────────────────────────────────────────────
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(new Color(26, 58, 92));
        panelHeader.setPreferredSize(new Dimension(800, 60));

        JLabel lblJudul = new JLabel("DATA MAHASISWA", SwingConstants.CENTER);
        lblJudul.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblJudul.setForeground(Color.WHITE);
        panelHeader.add(lblJudul, BorderLayout.CENTER);

        // ── Panel Tabel ───────────────────────────────────────────────────────
        String[] kolom = {"No", "NIM", "Nama", "Jurusan", "IPK"};
        tableModel = new DefaultTableModel(kolom, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // tabel tidak bisa diedit langsung
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setSelectionBackground(new Color(46, 117, 182));
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(new Color(210, 210, 210));
        table.setShowGrid(true);

        // Lebar kolom
        table.getColumnModel().getColumn(0).setPreferredWidth(40);   // No
        table.getColumnModel().getColumn(1).setPreferredWidth(100);  // NIM
        table.getColumnModel().getColumn(2).setPreferredWidth(200);  // Nama
        table.getColumnModel().getColumn(3).setPreferredWidth(200);  // Jurusan
        table.getColumnModel().getColumn(4).setPreferredWidth(80);   // IPK

        // Center alignment untuk No, NIM, IPK
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

        // Style header tabel
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 13));
        header.setBackground(new Color(46, 117, 182));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 32));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 15, 5, 15));

        // ── Panel Footer (tombol + info) ──────────────────────────────────────
        JPanel panelFooter = new JPanel(new BorderLayout());
        panelFooter.setBackground(new Color(245, 245, 245));
        panelFooter.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        // Label total data
        lblTotal = new JLabel("Total: 0 mahasiswa");
        lblTotal.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblTotal.setForeground(new Color(80, 80, 80));

        // Panel tombol kanan
        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panelTombol.setBackground(new Color(245, 245, 245));

        btnRefresh = buatTombol("↺ Refresh", new Color(100, 100, 100));
        btnCetak   = buatTombol("🖨 Cetak Laporan", new Color(46, 117, 182));
        btnExportPDF = buatTombol("⬇ Export PDF", new Color(192, 57, 43));

        panelTombol.add(btnRefresh);
        panelTombol.add(btnCetak);
        panelTombol.add(btnExportPDF);

        panelFooter.add(lblTotal, BorderLayout.WEST);
        panelFooter.add(panelTombol, BorderLayout.EAST);

        // ── Susun layout utama ────────────────────────────────────────────────
        add(panelHeader, BorderLayout.NORTH);
        add(scrollPane,  BorderLayout.CENTER);
        add(panelFooter, BorderLayout.SOUTH);

        // ── Event tombol ──────────────────────────────────────────────────────
        btnRefresh.addActionListener(e -> loadData());
        btnCetak.addActionListener(e -> cetakLaporan(false));
        btnExportPDF.addActionListener(e -> cetakLaporan(true));
    }

    // ─── Helper buat tombol seragam ───────────────────────────────────────────
    private JButton buatTombol(String teks, Color warna) {
        JButton btn = new JButton(teks);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setBackground(warna);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(160, 36));
        return btn;
    }

    // ─── Load data dari SQLite ke JTable ─────────────────────────────────────
    private void loadData() {
        tableModel.setRowCount(0); // kosongkan tabel dulu

        try (Connection conn = DatabaseHelper.getConnection();
             ResultSet rs = DatabaseHelper.getAllMahasiswa(conn)) {

            int no = 1;
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        no++,
                        rs.getString("nim"),
                        rs.getString("nama"),
                        rs.getString("jurusan"),
                        String.format("%.2f", rs.getDouble("ipk"))
                });
            }

            lblTotal.setText("Total: " + (no - 1) + " mahasiswa");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Gagal memuat data:\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ─── Cetak laporan / export PDF ───────────────────────────────────────────
    private void cetakLaporan(boolean exportPDF) {
        try {
            // 1. Lokasi file .jrxml
            String jrxmlPath = "src/laporan/LaporanMahasiswa.jrxml";
            File jrxmlFile = new File(jrxmlPath);

            if (!jrxmlFile.exists()) {
                JOptionPane.showMessageDialog(this,
                        "File template tidak ditemukan:\n" + jrxmlFile.getAbsolutePath(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 2. Compile .jrxml → JasperReport
            JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getAbsolutePath());

            // 3. Koneksi database untuk JasperReports
            Connection conn = DatabaseHelper.getConnection();

            // 4. Parameter tambahan (opsional, bisa diisi data dinamis)
            Map<String, Object> params = new HashMap<>();

            // 5. Fill data ke report
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, conn);
            conn.close();

            if (exportPDF) {
                // ── Export ke PDF ─────────────────────────────────────────────
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Simpan PDF");
                fileChooser.setSelectedFile(new File("LaporanMahasiswa.pdf"));

                int result = fileChooser.showSaveDialog(this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    String outputPath = fileChooser.getSelectedFile().getAbsolutePath();
                    if (!outputPath.endsWith(".pdf")) outputPath += ".pdf";

                    JasperExportManager.exportReportToPdfFile(jasperPrint, outputPath);

                    JOptionPane.showMessageDialog(this,
                            "PDF berhasil disimpan:\n" + outputPath,
                            "Sukses", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                // ── Tampilkan preview ─────────────────────────────────────────
                JasperViewer viewer = new JasperViewer(jasperPrint, false);
                viewer.setTitle("Preview Laporan Mahasiswa");
                viewer.setVisible(true);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Gagal membuat laporan:\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // ─── Entry point ──────────────────────────────────────────────────────────
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}

            new MainFrame().setVisible(true);
        });
    }
}