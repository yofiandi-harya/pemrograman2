-- db_penjualan.sql
-- Import ke MySQL sebelum menjalankan aplikasi

CREATE DATABASE IF NOT EXISTS db_penjualan;
USE db_penjualan;

CREATE TABLE IF NOT EXISTS barang (
    kode_barang  VARCHAR(10) PRIMARY KEY,
    nama_barang  VARCHAR(50),
    harga        DOUBLE,
    stok         INT
);

CREATE TABLE IF NOT EXISTS customer (
    kode_customer VARCHAR(10) PRIMARY KEY,
    nama_customer VARCHAR(50),
    alamat        VARCHAR(100),
    telepon       VARCHAR(15)
);

CREATE TABLE IF NOT EXISTS supplier (
    kode_supplier VARCHAR(10) PRIMARY KEY,
    nama_supplier VARCHAR(50),
    alamat        VARCHAR(100),
    telepon       VARCHAR(15)
);

CREATE TABLE IF NOT EXISTS transaksi (
    id_transaksi  VARCHAR(15) PRIMARY KEY,
    kode_customer VARCHAR(10),
    tanggal       DATE,
    total         DOUBLE,
    FOREIGN KEY (kode_customer) REFERENCES customer(kode_customer)
);

CREATE TABLE IF NOT EXISTS detail_transaksi (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    id_transaksi  VARCHAR(15),
    kode_barang   VARCHAR(10),
    jumlah        INT,
    subtotal      DOUBLE,
    FOREIGN KEY (id_transaksi) REFERENCES transaksi(id_transaksi),
    FOREIGN KEY (kode_barang)  REFERENCES barang(kode_barang)
);

-- Data contoh
INSERT INTO barang VALUES ('B001','Baju Kaos',75000,50);
INSERT INTO barang VALUES ('B002','Celana Jeans',150000,30);
INSERT INTO barang VALUES ('B003','Sepatu Sneakers',250000,20);
INSERT INTO customer VALUES ('C001','Budi Santoso','Jl. Merdeka No.1','081234567890');
INSERT INTO customer VALUES ('C002','Siti Rahayu','Jl. Pahlawan No.5','089876543210');
INSERT INTO supplier VALUES ('S001','PT Tekstil Jaya','Jl. Industri No.10','021-5551234');
