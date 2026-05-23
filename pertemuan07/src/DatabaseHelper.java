import java.sql.*;

public class DatabaseHelper {

    // Sesuaikan path ini dengan lokasi folder project pertemuan7 kamu
    private static final String DB_PATH = "database/datamhs.db";
    private static final String URL = "jdbc:sqlite:" + DB_PATH;

    // Membuka koneksi ke database
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    // Membuat tabel dan mengisi data dummy jika belum ada
    public static void initDatabase() {
        String createTable = """
                CREATE TABLE IF NOT EXISTS mahasiswa (
                    id      INTEGER PRIMARY KEY AUTOINCREMENT,
                    nim     TEXT NOT NULL,
                    nama    TEXT NOT NULL,
                    jurusan TEXT NOT NULL,
                    ipk     REAL NOT NULL
                );
                """;

        String[] dummyData = {
            "INSERT OR IGNORE INTO mahasiswa (nim, nama, jurusan, ipk) VALUES ('2021001', 'Andi Pratama',    'Teknik Informatika', 3.75)",
            "INSERT OR IGNORE INTO mahasiswa (nim, nama, jurusan, ipk) VALUES ('2021002', 'Budi Santoso',    'Sistem Informasi',   3.50)",
            "INSERT OR IGNORE INTO mahasiswa (nim, nama, jurusan, ipk) VALUES ('2021003', 'Citra Dewi',      'Teknik Informatika', 3.90)",
            "INSERT OR IGNORE INTO mahasiswa (nim, nama, jurusan, ipk) VALUES ('2021004', 'Dina Rahayu',     'Manajemen Informatika', 3.20)",
            "INSERT OR IGNORE INTO mahasiswa (nim, nama, jurusan, ipk) VALUES ('2021005', 'Eko Wahyudi',     'Sistem Informasi',   3.65)",
            "INSERT OR IGNORE INTO mahasiswa (nim, nama, jurusan, ipk) VALUES ('2021006', 'Fani Kurniawan',  'Teknik Informatika', 3.80)",
            "INSERT OR IGNORE INTO mahasiswa (nim, nama, jurusan, ipk) VALUES ('2021007', 'Gilang Saputra',  'Manajemen Informatika', 2.95)",
            "INSERT OR IGNORE INTO mahasiswa (nim, nama, jurusan, ipk) VALUES ('2021008', 'Hana Fitriani',   'Sistem Informasi',   3.40)",
        };

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(createTable);

            for (String sql : dummyData) {
                stmt.execute(sql);
            }

            System.out.println("Database siap.");

        } catch (SQLException e) {
            System.err.println("Error inisialisasi database: " + e.getMessage());
        }
    }

    // Mengambil semua data mahasiswa
    public static ResultSet getAllMahasiswa(Connection conn) throws SQLException {
        String sql = "SELECT id, nim, nama, jurusan, ipk FROM mahasiswa ORDER BY id";
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(sql);
    }
}