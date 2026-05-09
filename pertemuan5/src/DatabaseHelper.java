import java.sql.*;

public class DatabaseHelper {
    private static final String URL = "jdbc:sqlite:datamhs.db";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initDB() {
        String sql = "CREATE TABLE IF NOT EXISTS datamhs (" +
                     "nim VARCHAR(15) PRIMARY KEY NOT NULL," +
                     "nama VARCHAR(30)," +
                     "semester INT," +
                     "kelas VARCHAR(1))";
        try (Connection conn = connect();
             Statement st = conn.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            System.out.println("Init DB gagal: " + e.getMessage());
        }
    }

    // INSERT
    public static boolean simpanData(String nim, String nama, int semester, String kelas) {
        String sql = "INSERT INTO datamhs (nim, nama, semester, kelas) VALUES (?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pStat = conn.prepareStatement(sql)) {
            pStat.setString(1, nim);
            pStat.setString(2, nama);
            pStat.setInt(3, semester);
            pStat.setString(4, kelas);
            pStat.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Simpan gagal: " + e.getMessage());
            return false;
        }
    }

    // SELECT semua data
    public static ResultSet getAllData(Connection conn) throws SQLException {
        String sql = "SELECT * FROM datamhs";
        Statement st = conn.createStatement();
        return st.executeQuery(sql);
    }
}