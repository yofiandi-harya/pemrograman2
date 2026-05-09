@echo off
REM ============================================================
REM  compile.bat  –  Kompilasi & jalankan AplikasiGajiKaryawan
REM ============================================================

REM Buat folder output
if not exist out mkdir out

REM Kompilasi semua file .java
javac -d out -sourcepath src src\view\FormUtama.java ^
                              src\view\FormPekerjaan.java ^
                              src\view\FormLihatPekerjaan.java ^
                              src\view\FormKaryawan.java

IF %ERRORLEVEL% EQU 0 (
    echo === Kompilasi berhasil ===
    java -cp out view.FormUtama
) ELSE (
    echo === Kompilasi gagal, periksa error di atas ===
)
