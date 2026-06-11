package com.unpam.model;

public class Nilai {
    // Properti sesuai dengan kolom di tabel database 'nilai'
    private int idNilai;
    private String nim;
    private String kodeMk;
    private float nilaiTugas;
    private float nilaiUts;
    private float nilaiUas;

    // Objek relasi untuk mempermudah pengambilan data nama mahasiswa & mata kuliah
    private Mahasiswa mahasiswa;
    private MataKuliah mataKuliah;

    // Konstruktor kosong
    public Nilai() {
        // Inisialisasi objek agar tidak NullPointerException saat dipanggil
        this.mahasiswa = new Mahasiswa();
        this.mataKuliah = new MataKuliah();
    }

    // Konstruktor dengan parameter
    public Nilai(int idNilai, String nim, String kodeMk, float nilaiTugas, float nilaiUts, float nilaiUas) {
        this.idNilai = idNilai;
        this.nim = nim;
        this.kodeMk = kodeMk;
        this.nilaiTugas = nilaiTugas;
        this.nilaiUts = nilaiUts;
        this.nilaiUas = nilaiUas;
        this.mahasiswa = new Mahasiswa();
        this.mataKuliah = new MataKuliah();
    }

    // --- GETTER DAN SETTER ---

    public int getIdNilai() {
        return idNilai;
    }

    public void setIdNilai(int idNilai) {
        this.idNilai = idNilai;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getKodeMk() {
        return kodeMk;
    }

    public void setKodeMk(String kodeMk) {
        this.kodeMk = kodeMk;
    }

    public float getNilaiTugas() {
        return nilaiTugas;
    }

    public void setNilaiTugas(float nilaiTugas) {
        this.nilaiTugas = nilaiTugas;
    }

    public float getNilaiUts() {
        return nilaiUts;
    }

    public void setNilaiUts(float nilaiUts) {
        this.nilaiUts = nilaiUts;
    }

    public float getNilaiUas() {
        return nilaiUas;
    }

    public void setNilaiUas(float nilaiUas) {
        this.nilaiUas = nilaiUas;
    }

    public Mahasiswa getMahasiswa() {
        return mahasiswa;
    }

    public void setMahasiswa(Mahasiswa mahasiswa) {
        this.mahasiswa = mahasiswa;
    }

    public MataKuliah getMataKuliah() {
        return mataKuliah;
    }

    public void setMataKuliah(MataKuliah mataKuliah) {
        this.mataKuliah = mataKuliah;
    }
    
    // Metode tambahan untuk menghitung Nilai Akhir (opsional, sangat berguna untuk improvisasi)
    public float hitungNilaiAkhir() {
        // Asumsi bobot: Tugas 20%, UTS 30%, UAS 50%
        return (float) ((nilaiTugas * 0.2) + (nilaiUts * 0.3) + (nilaiUas * 0.5));
    }
}