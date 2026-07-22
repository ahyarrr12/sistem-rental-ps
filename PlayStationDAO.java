// ================================================================
// File  : PlayStationDAO.java
// Desc  : Data Access Object - semua operasi CRUD untuk tabel playstation
// ================================================================

import java.sql.*;
import java.util.*;

public class PlayStationDAO {

    /**
     * Menghitung total data PlayStation.
     */
    public int countSemua() throws SQLException {
        String sql = "SELECT COUNT(*) FROM playstation";

        try (Statement st = DatabaseHelper.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    /**
     * Mengambil daftar PlayStation dengan paginasi.
     */
    public List<PlayStation> getSemuaPlayStation(int offset, int limit)
            throws SQLException {

        List<PlayStation> list = new ArrayList<>();

        String sql =
                "SELECT * FROM playstation " +
                "ORDER BY seri_konsol COLLATE NOCASE " +
                "LIMIT ? OFFSET ?";

        try (PreparedStatement ps =
                DatabaseHelper.getConnection().prepareStatement(sql)) {

            ps.setInt(1, limit);
            ps.setInt(2, offset);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(petakanResultSet(rs));
            }
        }

        return list;
    }

    /**
     * Menambahkan data PlayStation baru.
     */
    public void insertPlayStation(PlayStation psData)
            throws SQLException {

        String sql =
            "INSERT INTO playstation " +
            "(nama_konsol, seri_konsol, harga_per_jam, status, keterangan) " +
            "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps =
                DatabaseHelper.getConnection().prepareStatement(sql)) {

            ps.setString(1, psData.getNamaKonsol());
            ps.setString(2, psData.getSeriKonsol());
            ps.setDouble(3, psData.getHargaPerJam());
            ps.setString(4, psData.getStatus());
            ps.setString(5, psData.getKeterangan());

            ps.executeUpdate();
        }
    }

    /**
     * Mengubah data PlayStation berdasarkan ID.
     */
    public void updatePlayStation(PlayStation psData)
            throws SQLException {

        String sql =
            "UPDATE playstation SET " +
            "nama_konsol=?, " +
            "seri_konsol=?, " +
            "harga_per_jam=?, " +
            "status=?, " +
            "keterangan=? " +
            "WHERE id=?";

        try (PreparedStatement ps =
                DatabaseHelper.getConnection().prepareStatement(sql)) {

            ps.setString(1, psData.getNamaKonsol());
            ps.setString(2, psData.getSeriKonsol());
            ps.setDouble(3, psData.getHargaPerJam());
            ps.setString(4, psData.getStatus());
            ps.setString(5, psData.getKeterangan());
            ps.setInt(6, psData.getId());

            ps.executeUpdate();
        }
    }

    /**
     * Menghapus data PlayStation berdasarkan ID.
     */
    public void hapusPlayStation(int id)
            throws SQLException {

        String sql = "DELETE FROM playstation WHERE id=?";

        try (PreparedStatement ps =
                DatabaseHelper.getConnection().prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    /**
     * Mengubah ResultSet menjadi objek PlayStation.
     */
    private PlayStation petakanResultSet(ResultSet rs)
            throws SQLException {

        return new PlayStation(
                rs.getInt("id"),
                rs.getString("nama_konsol"),
                rs.getString("seri_konsol"),
                rs.getDouble("harga_per_jam"),
                rs.getString("status"),
                rs.getString("keterangan")
        );
    }
}