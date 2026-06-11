package com.unpam.model;

public class Mahasiswa {
    private String nim;
    private String nama;
    private int semester;
    private String kelas;

    // Konstruktor kosong
    public Mahasiswa() {
    }

    // Konstruktor dengan parameter
    public Mahasiswa(String nim, String nama, int semester, String kelas) {
        this.nim = nim;
        this.nama = nama;
        this.semester = semester;
        this.kelas = kelas;
    }

    // Getter dan Setter
    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public String getKelas() {
        return kelas;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }
}