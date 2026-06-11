package com.unpam.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NilaiDAO {

    public Mahasiswa cariMahasiswa(String nim) {
        Mahasiswa mhs = null;
        String query = "SELECT * FROM mahasiswa WHERE nim = ?";
        
        // Memisahkan Connection dari blok try otomatis
        Connection conn = Koneksi.getKoneksi();
        if (conn == null) return null;

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, nim);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    mhs = new Mahasiswa();
                    mhs.setNim(rs.getString("nim"));
                    mhs.setNama(rs.getString("nama"));
                    mhs.setSemester(rs.getInt("semester"));
                    mhs.setKelas(rs.getString("kelas"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error cariMahasiswa: " + e.getMessage());
        }
        return mhs;
    }

    public MataKuliah cariMataKuliah(String kodeMk) {
        MataKuliah mk = null;
        String query = "SELECT * FROM mata_kuliah WHERE kode_mk = ?";
        
        Connection conn = Koneksi.getKoneksi();
        if (conn == null) return null;

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, kodeMk);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    mk = new MataKuliah();
                    mk.setKodeMk(rs.getString("kode_mk"));
                    mk.setNamaMk(rs.getString("nama_mk"));
                    mk.setSks(rs.getInt("sks"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error cariMataKuliah: " + e.getMessage());
        }
        return mk;
    }

    public boolean simpan(Nilai nilai) {
        String query = "INSERT INTO nilai (nim, kode_mk, nilai_tugas, nilai_uts, nilai_uas) VALUES (?, ?, ?, ?, ?)";
        Connection conn = Koneksi.getKoneksi();
        
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, nilai.getNim());
            ps.setString(2, nilai.getKodeMk());
            ps.setFloat(3, nilai.getNilaiTugas());
            ps.setFloat(4, nilai.getNilaiUts());
            ps.setFloat(5, nilai.getNilaiUas());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error simpan nilai: " + e.getMessage());
            return false;
        }
    }

    public boolean hapus(String nim, String kodeMk) {
        String query = "DELETE FROM nilai WHERE nim = ? AND kode_mk = ?";
        Connection conn = Koneksi.getKoneksi();
        
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, nim);
            ps.setString(2, kodeMk);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error hapus nilai: " + e.getMessage());
            return false;
        }
    }
    // Method BARU untuk mengambil semua data (Rekap Nilai)
    public java.util.List<Nilai> tampilSemua() {
        java.util.List<Nilai> listNilai = new java.util.ArrayList<>();
        String query = "SELECT n.id_nilai, n.nim, m.nama, n.kode_mk, mk.nama_mk, " +
                       "n.nilai_tugas, n.nilai_uts, n.nilai_uas " +
                       "FROM nilai n " +
                       "JOIN mahasiswa m ON n.nim = m.nim " +
                       "JOIN mata_kuliah mk ON n.kode_mk = mk.kode_mk " +
                       "ORDER BY n.id_nilai DESC";
                       
        Connection conn = Koneksi.getKoneksi();
        if (conn == null) return listNilai;

        try (PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
             
            while (rs.next()) {
                Nilai n = new Nilai();
                n.setIdNilai(rs.getInt("id_nilai"));
                n.setNim(rs.getString("nim"));
                n.setKodeMk(rs.getString("kode_mk"));
                n.setNilaiTugas(rs.getFloat("nilai_tugas"));
                n.setNilaiUts(rs.getFloat("nilai_uts"));
                n.setNilaiUas(rs.getFloat("nilai_uas"));
                
                // Set data relasi
                n.getMahasiswa().setNama(rs.getString("nama"));
                n.getMataKuliah().setNamaMk(rs.getString("nama_mk"));
                
                listNilai.add(n);
            }
        } catch (SQLException e) {
            System.out.println("Error tampilSemua: " + e.getMessage());
        }
        return listNilai;
    }
}