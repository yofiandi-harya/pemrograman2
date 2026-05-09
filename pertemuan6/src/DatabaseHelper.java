import java.sql.*;

public class DatabaseHelper {
    private static final String URL = "jdbc:sqlite:datanilai.db";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    
    public static void initDB() {
        String sql = "CREATE TABLE IF NOT EXISTS datanilai (" +
                     "nim TEXT PRIMARY KEY," +
                     "nama TEXT," +
                     "nil1 REAL," +
                     "nil2 REAL," +
                     "rata REAL)";
        try (Connection conn = connect();
             Statement st = conn.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            System.out.println("Init DB gagal: " + e.getMessage());
        }
    }

    // INSERT
    public static void tambahData(String nim, String nama, double nil1, double nil2) {
        double rata = (nil1 + nil2) / 2;
        String sql = "INSERT INTO datanilai VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nim);
            ps.setString(2, nama);
            ps.setDouble(3, nil1);
            ps.setDouble(4, nil2);
            ps.setDouble(5, rata);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Tambah gagal: " + e.getMessage());
        }
    }

    // SELECT by nama (LIKE)
    public static ResultSet cariData(Connection conn, String nama) throws SQLException {
        String sql = "SELECT * FROM datanilai WHERE nama LIKE ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, "%" + nama + "%");
        return ps.executeQuery();
    }

    // UPDATE
    public static void updateData(String nim, String nama, double nil1, double nil2) {
        double rata = (nil1 + nil2) / 2;
        String sql = "UPDATE datanilai SET nama=?, nil1=?, nil2=?, rata=? WHERE nim=?";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nama);
            ps.setDouble(2, nil1);
            ps.setDouble(3, nil2);
            ps.setDouble(4, rata);
            ps.setString(5, nim);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Update gagal: " + e.getMessage());
        }
    }

    // DELETE
    public static void hapusData(String nim) {
        String sql = "DELETE FROM datanilai WHERE nim=?";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nim);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Hapus gagal: " + e.getMessage());
        }
    }
}