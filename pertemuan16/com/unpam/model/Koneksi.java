package com.unpam.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Koneksi {
    private static Connection koneksi;
    
    private static final String URL = "jdbc:mysql://localhost:3306/db_administrasi_nilai";
    private static final String USER = "root";
    private static final String PASSWORD = ""; 

    public static Connection getKoneksi() {
        try {
            // Cek jika koneksi belum ada ATAU koneksi sudah terputus/ditutup
            if (koneksi == null || koneksi.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                koneksi = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Koneksi Database Aktif & Stabil!");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Driver tidak ditemukan: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Koneksi ke database gagal: " + e.getMessage());
        }
        return koneksi;
    }
}