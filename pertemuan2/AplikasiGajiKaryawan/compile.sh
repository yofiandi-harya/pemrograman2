#!/bin/bash
# ============================================================
#  compile.sh  –  Kompilasi & jalankan AplikasiGajiKaryawan
# ============================================================

# Buat folder output
mkdir -p out

# Kompilasi semua file .java
javac -d out -sourcepath src src/view/FormUtama.java \
                             src/view/FormPekerjaan.java \
                             src/view/FormLihatPekerjaan.java \
                             src/view/FormKaryawan.java

if [ $? -eq 0 ]; then
    echo "=== Kompilasi berhasil ==="
    java -cp out view.FormUtama
else
    echo "=== Kompilasi gagal, periksa error di atas ==="
fi
