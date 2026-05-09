package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FormUtama extends JFrame {

    public static FormKaryawan formKaryawan;
    public static FormPekerjaan formPekerjaan;

    private JDesktopPane mdiDesktopPane;
    private JMenuBar menuBar;

    // Menu Master Data
    private JMenu menuMasterData;
    private JMenuItem karyawanMenuItem;
    private JMenuItem pekerjaanMenuItem;

    // Menu Transaksi & Laporan (placeholder)
    private JMenu menuTransaksi;
    private JMenu menuLaporan;
    private JMenu menuAplikasi;
    private JMenuItem keluarMenuItem;

    public FormUtama() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Aplikasi Gaji Karyawan");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);

        // Desktop Pane (MDI)
        mdiDesktopPane = new JDesktopPane();
        mdiDesktopPane.setBackground(new Color(200, 210, 220));
        setContentPane(mdiDesktopPane);

        // Menu Bar
        menuBar = new JMenuBar();

        // Menu Aplikasi
        menuAplikasi = new JMenu("Aplikasi");
        keluarMenuItem = new JMenuItem("Keluar");
        keluarMenuItem.addActionListener(e -> System.exit(0));
        menuAplikasi.add(keluarMenuItem);

        // Menu Master Data
        menuMasterData = new JMenu("Master Data");

        karyawanMenuItem = new JMenuItem("Karyawan");
        karyawanMenuItem.addActionListener(e -> karyawanMenuItemActionPerformed());

        pekerjaanMenuItem = new JMenuItem("Pekerjaan");
        pekerjaanMenuItem.addActionListener(e -> pekerjaanMenuItemActionPerformed());

        menuMasterData.add(karyawanMenuItem);
        menuMasterData.add(pekerjaanMenuItem);

        // Menu Transaksi
        menuTransaksi = new JMenu("Transaksi");

        // Menu Laporan
        menuLaporan = new JMenu("Laporan");

        menuBar.add(menuAplikasi);
        menuBar.add(menuMasterData);
        menuBar.add(menuTransaksi);
        menuBar.add(menuLaporan);

        setJMenuBar(menuBar);
    }

    private void karyawanMenuItemActionPerformed() {
        if ((formKaryawan != null) && formKaryawan.isVisible()) {
            try {
                formKaryawan.setSelected(true);
            } catch (java.beans.PropertyVetoException ex) {}
        } else {
            formKaryawan = new FormKaryawan();
            mdiDesktopPane.add(formKaryawan);
            formKaryawan.setVisible(true);
        }
    }

    private void pekerjaanMenuItemActionPerformed() {
        if ((formPekerjaan != null) && formPekerjaan.isVisible()) {
            try {
                formPekerjaan.setSelected(true);
            } catch (java.beans.PropertyVetoException ex) {}
        } else {
            formPekerjaan = new FormPekerjaan();
            mdiDesktopPane.add(formPekerjaan);
            formPekerjaan.setVisible(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FormUtama().setVisible(true);
        });
    }
}
