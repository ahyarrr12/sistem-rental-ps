import java.sql.*;

public class DatabaseHelper {

    // Database SQLite
    private static final String URL = "jdbc:sqlite:rental_ps.db";

    private static Connection conn;

    /**
     * Mengembalikan koneksi database.
     */
    public static Connection getConnection() throws SQLException {

        if (conn == null || conn.isClosed()) {

            conn = DriverManager.getConnection(URL);

            try (Statement st = conn.createStatement()) {
                st.execute("PRAGMA foreign_keys = ON");
            }
        }

        return conn;
    }

    /**
     * Membuat tabel playstation jika belum ada.
     */
    public static void inisialisasi() throws SQLException {

        String sql =
            "CREATE TABLE IF NOT EXISTS playstation (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "nama_konsol TEXT NOT NULL," +
            "seri_konsol TEXT NOT NULL," +
            "durasi INTEGER NOT NULL," +
            "harga_per_jam REAL NOT NULL," +
            "status TEXT NOT NULL," +
            "keterangan TEXT" +
            ")";

        try (Statement st = getConnection().createStatement()) {
            st.execute(sql);
        }
    }

    /**
     * Menutup koneksi database.
     */
    public static void tutupKoneksi() {

        try {

            if (conn != null && !conn.isClosed()) {
                conn.close();
            }

        } catch (SQLException e) {

            System.err.println("Gagal menutup koneksi : " + e.getMessage());

        }

    }

}