package com.unpam.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MahasiswaDAO {
    public List<Mahasiswa> tampilSemua() {
        List<Mahasiswa> list = new ArrayList<>();
        String query = "SELECT * FROM mahasiswa";
        
        Connection conn = Koneksi.getKoneksi();
        if (conn == null) return list;

        try (PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Mahasiswa m = new Mahasiswa();
                m.setNim(rs.getString("nim"));
                m.setNama(rs.getString("nama"));
                m.setSemester(rs.getInt("semester"));
                m.setKelas(rs.getString("kelas"));
                list.add(m);
            }
        } catch (Exception e) {
            System.out.println("Error tampilSemua Mahasiswa: " + e.getMessage());
        }
        return list;
    }
}