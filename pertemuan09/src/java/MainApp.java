package Pertemuan9;

import javax.swing.*;
import java.awt.*;

public class MainApp extends JFrame {

    JDesktopPane desktop;

    public MainApp() {
        DatabaseHelper.initDB();

        setTitle("Aplikasi Penjualan Barang - Pertemuan 9");
        setSize(900, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        desktop = new JDesktopPane();
        desktop.setBackground(new Color(240, 240, 240));
        setContentPane(desktop);

        // ===== Menu Bar =====
        JMenuBar menuBar = new JMenuBar();

        JMenu menuData = new JMenu("Data Master");
        JMenuItem mBarang    = new JMenuItem("Data Barang");
        JMenuItem mCustomer  = new JMenuItem("Data Customer");
        JMenuItem mSupplier  = new JMenuItem("Data Supplier");
        menuData.add(mBarang); menuData.add(mCustomer); menuData.add(mSupplier);

        JMenu menuTrans = new JMenu("Transaksi");
        JMenuItem mTransaksi = new JMenuItem("Transaksi Penjualan");
        JMenuItem mInventory = new JMenuItem("Inventory Barang");
        menuTrans.add(mTransaksi); menuTrans.add(mInventory);

        JMenu menuLaporan = new JMenu("Laporan");
        JMenuItem mLaporan = new JMenuItem("Cetak Laporan");
        menuLaporan.add(mLaporan);

        JMenu menuKeluar = new JMenu("Keluar");
        JMenuItem mKeluar = new JMenuItem("Exit");
        menuKeluar.add(mKeluar);

        menuBar.add(menuData); menuBar.add(menuTrans);
        menuBar.add(menuLaporan); menuBar.add(menuKeluar);
        setJMenuBar(menuBar);

        // ===== Aksi Menu =====
        mBarang.addActionListener(e   -> { FormBarang f = new FormBarang(); desktop.add(f); f.setVisible(true); });
        mCustomer.addActionListener(e -> { FormCustomer f = new FormCustomer(); desktop.add(f); f.setVisible(true); });
        mSupplier.addActionListener(e -> { FormSupplier f = new FormSupplier(); desktop.add(f); f.setVisible(true); });
        mTransaksi.addActionListener(e-> { FormTransaksi f = new FormTransaksi(); desktop.add(f); f.setVisible(true); });
        mInventory.addActionListener(e-> { FormInventory f = new FormInventory(); desktop.add(f); f.setVisible(true); });
        mLaporan.addActionListener(e  -> { FormLaporan f = new FormLaporan(); desktop.add(f); f.setVisible(true); });
        mKeluar.addActionListener(e   -> System.exit(0));

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainApp());
    }
}
