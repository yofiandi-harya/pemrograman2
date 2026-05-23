package Pertemuan9;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;
import javax.swing.*;
import java.io.File;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class FormLaporan extends JInternalFrame {

    public FormLaporan() {
        super("Laporan", true, true, true, true);
        setSize(350, 220);
        setLayout(null);

        JLabel lbl = new JLabel("Pilih Laporan yang Ingin Dicetak:");
        lbl.setBounds(20, 20, 300, 25);
        add(lbl);

        JButton btnTransaksi  = new JButton("Laporan Transaksi");
        JButton btnInventory  = new JButton("Laporan Inventory");
        btnTransaksi.setBounds(50, 60, 240, 40);
        btnInventory.setBounds(50, 115, 240, 40);
        add(btnTransaksi);
        add(btnInventory);

        btnTransaksi.addActionListener(e -> cetakLaporan("LaporanTransaksi.jrxml"));
        btnInventory.addActionListener(e -> cetakLaporan("LaporanInventory.jrxml"));

        setVisible(true);
    }

    void cetakLaporan(String namaFile) {
        File dir1 = new File(".");
        String reportPath = dir1.getAbsolutePath() + File.separator + "src"
                + File.separator + "laporan" + File.separator + namaFile;
        try (Connection koneksi = DatabaseHelper.connect()) {
            JasperReport jasperReport = JasperCompileManager.compileReport(reportPath);
            Map<String, Object> params = new HashMap<>();
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, koneksi);
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException e) {
            JOptionPane.showMessageDialog(this, "Gagal cetak laporan!\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal koneksi DB!\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
