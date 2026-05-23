package Pertemuan9;

import java.sql.*;

public class DatabaseHelper {

    private static final String HOST = "localhost";
    private static final String PORT = "3306";
    private static final String DB   = "db_penjualan";
    private static final String USER = "root";
    private static final String PASS = "";
    private static final String URL  =
        "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB + "?useSSL=false&serverTimezone=UTC";

    public static Connection connect() throws SQLException {
        try { Class.forName("com.mysql.cj.jdbc.Driver"); }
        catch (ClassNotFoundException e) { throw new SQLException("Driver tidak ditemukan!"); }
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public static void initDB() {
        String[] sqls = {
            "CREATE DATABASE IF NOT EXISTS db_penjualan",
            "USE db_penjualan",
            "CREATE TABLE IF NOT EXISTS barang (" +
                "kode_barang VARCHAR(10) PRIMARY KEY," +
                "nama_barang VARCHAR(50)," +
                "harga DOUBLE," +
                "stok INT)",
            "CREATE TABLE IF NOT EXISTS customer (" +
                "kode_customer VARCHAR(10) PRIMARY KEY," +
                "nama_customer VARCHAR(50)," +
                "alamat VARCHAR(100)," +
                "telepon VARCHAR(15))",
            "CREATE TABLE IF NOT EXISTS supplier (" +
                "kode_supplier VARCHAR(10) PRIMARY KEY," +
                "nama_supplier VARCHAR(50)," +
                "alamat VARCHAR(100)," +
                "telepon VARCHAR(15))",
            "CREATE TABLE IF NOT EXISTS transaksi (" +
                "id_transaksi VARCHAR(15) PRIMARY KEY," +
                "kode_customer VARCHAR(10)," +
                "tanggal DATE," +
                "total DOUBLE," +
                "FOREIGN KEY (kode_customer) REFERENCES customer(kode_customer))",
            "CREATE TABLE IF NOT EXISTS detail_transaksi (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "id_transaksi VARCHAR(15)," +
                "kode_barang VARCHAR(10)," +
                "jumlah INT," +
                "subtotal DOUBLE," +
                "FOREIGN KEY (id_transaksi) REFERENCES transaksi(id_transaksi)," +
                "FOREIGN KEY (kode_barang) REFERENCES barang(kode_barang))"
        };
        try (Connection conn = connect();
             Statement st = conn.createStatement()) {
            for (String sql : sqls) st.execute(sql);
            System.out.println("Database siap.");
        } catch (SQLException e) {
            System.out.println("Init DB gagal: " + e.getMessage());
        }
    }

    // ===== BARANG =====
    public static boolean simpanBarang(String kode, String nama, double harga, int stok) {
        String sql = "INSERT INTO barang VALUES (?,?,?,?)";
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1,kode); ps.setString(2,nama);
            ps.setDouble(3,harga); ps.setInt(4,stok);
            ps.executeUpdate(); return true;
        } catch (SQLException e) { System.out.println(e.getMessage()); return false; }
    }
    public static boolean updateBarang(String kode, String nama, double harga, int stok) {
        String sql = "UPDATE barang SET nama_barang=?,harga=?,stok=? WHERE kode_barang=?";
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1,nama); ps.setDouble(2,harga);
            ps.setInt(3,stok); ps.setString(4,kode);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }
    public static boolean hapusBarang(String kode) {
        String sql = "DELETE FROM barang WHERE kode_barang=?";
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1,kode); return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }
    public static ResultSet getAllBarang(Connection c) throws SQLException {
        return c.createStatement().executeQuery("SELECT * FROM barang");
    }

    // ===== CUSTOMER =====
    public static boolean simpanCustomer(String kode, String nama, String alamat, String telp) {
        String sql = "INSERT INTO customer VALUES (?,?,?,?)";
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1,kode); ps.setString(2,nama);
            ps.setString(3,alamat); ps.setString(4,telp);
            ps.executeUpdate(); return true;
        } catch (SQLException e) { return false; }
    }
    public static boolean updateCustomer(String kode, String nama, String alamat, String telp) {
        String sql = "UPDATE customer SET nama_customer=?,alamat=?,telepon=? WHERE kode_customer=?";
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1,nama); ps.setString(2,alamat);
            ps.setString(3,telp); ps.setString(4,kode);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }
    public static boolean hapusCustomer(String kode) {
        String sql = "DELETE FROM customer WHERE kode_customer=?";
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1,kode); return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }
    public static ResultSet getAllCustomer(Connection c) throws SQLException {
        return c.createStatement().executeQuery("SELECT * FROM customer");
    }

    // ===== SUPPLIER =====
    public static boolean simpanSupplier(String kode, String nama, String alamat, String telp) {
        String sql = "INSERT INTO supplier VALUES (?,?,?,?)";
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1,kode); ps.setString(2,nama);
            ps.setString(3,alamat); ps.setString(4,telp);
            ps.executeUpdate(); return true;
        } catch (SQLException e) { return false; }
    }
    public static boolean updateSupplier(String kode, String nama, String alamat, String telp) {
        String sql = "UPDATE supplier SET nama_supplier=?,alamat=?,telepon=? WHERE kode_supplier=?";
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1,nama); ps.setString(2,alamat);
            ps.setString(3,telp); ps.setString(4,kode);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }
    public static boolean hapusSupplier(String kode) {
        String sql = "DELETE FROM supplier WHERE kode_supplier=?";
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1,kode); return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }
    public static ResultSet getAllSupplier(Connection c) throws SQLException {
        return c.createStatement().executeQuery("SELECT * FROM supplier");
    }

    // ===== TRANSAKSI =====
    public static boolean simpanTransaksi(String id, String kodeCustomer, String tanggal, double total) {
        String sql = "INSERT INTO transaksi VALUES (?,?,?,?)";
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1,id); ps.setString(2,kodeCustomer);
            ps.setString(3,tanggal); ps.setDouble(4,total);
            ps.executeUpdate(); return true;
        } catch (SQLException e) { return false; }
    }
    public static boolean simpanDetailTransaksi(String idTransaksi, String kodeBarang, int jumlah, double subtotal) {
        String sql = "INSERT INTO detail_transaksi (id_transaksi,kode_barang,jumlah,subtotal) VALUES (?,?,?,?)";
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1,idTransaksi); ps.setString(2,kodeBarang);
            ps.setInt(3,jumlah); ps.setDouble(4,subtotal);
            ps.executeUpdate();
            // Kurangi stok
            c.createStatement().execute("UPDATE barang SET stok=stok-"+jumlah+" WHERE kode_barang='"+kodeBarang+"'");
            return true;
        } catch (SQLException e) { return false; }
    }
    public static ResultSet getAllTransaksi(Connection c) throws SQLException {
        String sql = "SELECT t.id_transaksi, c.nama_customer, t.tanggal, t.total " +
                     "FROM transaksi t JOIN customer c ON t.kode_customer=c.kode_customer";
        return c.createStatement().executeQuery(sql);
    }
    public static ResultSet getInventory(Connection c) throws SQLException {
        return c.createStatement().executeQuery("SELECT * FROM barang ORDER BY stok ASC");
    }
}
