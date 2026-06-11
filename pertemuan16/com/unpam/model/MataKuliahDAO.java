package com.unpam.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MataKuliahDAO {
    public List<MataKuliah> tampilSemua() {
        List<MataKuliah> list = new ArrayList<>();
        String query = "SELECT * FROM mata_kuliah";
        
        Connection conn = Koneksi.getKoneksi();
        if (conn == null) return list;

        try (PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                MataKuliah mk = new MataKuliah();
                mk.setKodeMk(rs.getString("kode_mk"));
                mk.setNamaMk(rs.getString("nama_mk"));
                mk.setSks(rs.getInt("sks"));
                list.add(mk);
            }
        } catch (Exception e) {
            System.out.println("Error tampilSemua MataKuliah: " + e.getMessage());
        }
        return list;
    }
}