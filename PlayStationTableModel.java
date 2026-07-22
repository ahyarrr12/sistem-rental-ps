// ================================================================
// File  : PlayStationTableModel.java
// Desc  : Custom TableModel untuk JTable daftar PlayStation
// ================================================================

import javax.swing.table.AbstractTableModel;
import java.util.*;

public class PlayStationTableModel extends AbstractTableModel {

    // Nama kolom tabel
    private static final String[] KOLOM = {
        "No",
        "Nama Konsol",
        "Seri Konsol",
        "Harga/Jam",
        "Status",
        "Keterangan"
    };

    private List<PlayStation> data = new ArrayList<>();
    private int startNo = 1;

    /**
     * Memperbarui data tabel dan merefresh tampilan.
     *
     * @param data Daftar PlayStation
     * @param startNo Nomor awal pada halaman
     */
    public void setData(List<PlayStation> data, int startNo) {
        this.data = data;
        this.startNo = startNo;
        fireTableDataChanged();
    }

    /**
     * Mengembalikan objek PlayStation pada baris tertentu.
     */
    public PlayStation getPlayStationAt(int row) {
        return data.get(row);
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return KOLOM.length;
    }

    @Override
    public String getColumnName(int column) {
        return KOLOM[column];
    }

    @Override
    public Object getValueAt(int row, int column) {

        PlayStation ps = data.get(row);

        switch (column) {
            case 0:
                return startNo + row;

            case 1:
                return ps.getNamaKonsol();

            case 2:
                return ps.getSeriKonsol();

            case 3:
                return "Rp " + String.format("%,.0f", ps.getHargaPerJam());

            case 4:
                return ps.getStatus();

            case 5:
                return (ps.getKeterangan() != null &&
                        !ps.getKeterangan().isEmpty())
                        ? ps.getKeterangan()
                        : "-";

            default:
                return "";
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    @Override
    public Class<?> getColumnClass(int column) {
        return String.class;
    }
}