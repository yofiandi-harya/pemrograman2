-- phpMyAdmin SQL Dump
-- version 5.2.2
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Waktu pembuatan: 21 Bulan Mei 2026 pada 19.58
-- Versi server: 10.11.16-MariaDB-cll-lve
-- Versi PHP: 8.4.21

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `mtffmlyt_AplikasiAdministrasiNilaiWeb`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `dosen`
--

CREATE TABLE `dosen` (
  `nidn` varchar(20) NOT NULL,
  `nama_dosen` varchar(100) NOT NULL,
  `no_telp` varchar(15) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data untuk tabel `dosen`
--

INSERT INTO `dosen` (`nidn`, `nama_dosen`, `no_telp`) VALUES
('0419058801', 'Sonasa Rinusantoro S.Kom., M.A.', '081234567890');

-- --------------------------------------------------------

--
-- Struktur dari tabel `mahasiswa`
--

CREATE TABLE `mahasiswa` (
  `nim` varchar(20) NOT NULL,
  `nama_mahasiswa` varchar(100) NOT NULL,
  `program_studi` varchar(50) DEFAULT NULL,
  `jenis_kelamin` enum('L','P') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data untuk tabel `mahasiswa`
--

INSERT INTO `mahasiswa` (`nim`, `nama_mahasiswa`, `program_studi`, `jenis_kelamin`) VALUES
('231011401899', 'Yofiandi Harya', 'Teknik Informatika', 'L'),
('231011403030', 'Budi Santoso', 'Teknik Informatika', 'L');

-- --------------------------------------------------------

--
-- Struktur dari tabel `matkul`
--

CREATE TABLE `matkul` (
  `kode_mk` varchar(15) NOT NULL,
  `nama_mk` varchar(100) NOT NULL,
  `sks` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data untuk tabel `matkul`
--

INSERT INTO `matkul` (`kode_mk`, `nama_mk`, `sks`) VALUES
('MK001', 'Pemrograman 2', 3),
('MK002', 'Basis Data', 3);

-- --------------------------------------------------------

--
-- Struktur dari tabel `nilai`
--

CREATE TABLE `nilai` (
  `id_nilai` int(11) NOT NULL,
  `nim` varchar(20) DEFAULT NULL,
  `kode_mk` varchar(15) DEFAULT NULL,
  `nidn` varchar(20) DEFAULT NULL,
  `nilai_tugas` decimal(5,2) DEFAULT 0.00,
  `nilai_uts` decimal(5,2) DEFAULT 0.00,
  `nilai_uas` decimal(5,2) DEFAULT 0.00,
  `nilai_akhir` decimal(5,2) DEFAULT 0.00,
  `grade` char(2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data untuk tabel `nilai`
--

INSERT INTO `nilai` (`id_nilai`, `nim`, `kode_mk`, `nidn`, `nilai_tugas`, `nilai_uts`, `nilai_uas`, `nilai_akhir`, `grade`) VALUES
(1, '231011403029', 'MK001', '0419058801', 85.00, 90.00, 88.00, 87.60, 'A');

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `dosen`
--
ALTER TABLE `dosen`
  ADD PRIMARY KEY (`nidn`);

--
-- Indeks untuk tabel `mahasiswa`
--
ALTER TABLE `mahasiswa`
  ADD PRIMARY KEY (`nim`);

--
-- Indeks untuk tabel `matkul`
--
ALTER TABLE `matkul`
  ADD PRIMARY KEY (`kode_mk`);

--
-- Indeks untuk tabel `nilai`
--
ALTER TABLE `nilai`
  ADD PRIMARY KEY (`id_nilai`),
  ADD KEY `nim` (`nim`),
  ADD KEY `kode_mk` (`kode_mk`),
  ADD KEY `nidn` (`nidn`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `nilai`
--
ALTER TABLE `nilai`
  MODIFY `id_nilai` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `nilai`
--
ALTER TABLE `nilai`
  ADD CONSTRAINT `nilai_ibfk_1` FOREIGN KEY (`nim`) REFERENCES `mahasiswa` (`nim`) ON DELETE CASCADE,
  ADD CONSTRAINT `nilai_ibfk_2` FOREIGN KEY (`kode_mk`) REFERENCES `matkul` (`kode_mk`) ON DELETE CASCADE,
  ADD CONSTRAINT `nilai_ibfk_3` FOREIGN KEY (`nidn`) REFERENCES `dosen` (`nidn`) ON DELETE SET NULL;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
