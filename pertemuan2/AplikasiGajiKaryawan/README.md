# Aplikasi Gaji Karyawan
Project Java Swing — Pemrograman 2, BAB III

## Struktur Folder
```
AplikasiGajiKaryawan/
├── src/
│   └── view/
│       ├── FormUtama.java          ← Main window (JFrame + MDI)
│       ├── FormPekerjaan.java      ← Form input pekerjaan (JInternalFrame)
│       ├── FormLihatPekerjaan.java ← Dialog tabel data pekerjaan (JDialog)
│       └── FormKaryawan.java       ← Form input karyawan (JInternalFrame)
├── out/                            ← Hasil kompilasi (.class)
├── .vscode/
│   ├── settings.json
│   └── launch.json
├── compile.sh   ← Jalankan di Linux/Mac
├── compile.bat  ← Jalankan di Windows
└── README.md
```

## Cara Menjalankan

### Opsi 1 — VSCode (Direkomendasikan)
1. Install extension **"Extension Pack for Java"** dari Microsoft
   (ID: `vscjava.vscode-java-pack`)
2. Buka folder `AplikasiGajiKaryawan` di VSCode
3. Tunggu VSCode mendeteksi project Java
4. Tekan **F5** atau klik tombol ▶ Run di sebelah `main()`

### Opsi 2 — Terminal (tanpa extension)
**Windows:**
```
compile.bat
```
**Linux / Mac:**
```bash
chmod +x compile.sh
./compile.sh
```

### Opsi 3 — Manual via terminal
```bash
# Kompilasi
javac -d out -sourcepath src src/view/FormUtama.java src/view/FormPekerjaan.java src/view/FormLihatPekerjaan.java src/view/FormKaryawan.java

# Jalankan
java -cp out view.FormUtama
```

## Fitur yang Sudah Ada
- ✅ Form Utama dengan menu bar (Aplikasi, Master Data, Transaksi, Laporan)
- ✅ Form Pekerjaan (JInternalFrame) dengan:
  - Input Kode Pekerjaan, Nama Pekerjaan, Jumlah Tugas
  - Tombol Lihat, Simpan, Hapus, Tutup
  - ComboBox Jumlah Tugas (1–6, kecuali 5)
- ✅ Form Lihat Pekerjaan (JDialog) dengan:
  - Tabel 2 kolom (Kode + Nama Pekerjaan)
  - Tombol Pilih dan Tutup
  - Klik Pilih → kode otomatis masuk ke FormPekerjaan
- ✅ Form Karyawan (JInternalFrame) placeholder
- ✅ MDI Desktop (bisa buka banyak form sekaligus)

## Syarat
- Java JDK 8 atau lebih baru
- VSCode dengan Extension Pack for Java (opsional)
