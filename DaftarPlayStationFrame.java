// ================================================================
// File  : DaftarBukuFrame.java
// Desc  : Form utama - menampilkan daftar buku dengan paginasi
// ================================================================

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;
import java.util.*;
import java.util.List;

public class DaftarPlayStationFrame extends JFrame {

    // Komponen UI
    private JTable          tabelPlayStation;
    private PlayStationTableModel  tableModel;
    private JButton         btnTambah, btnEdit, btnHapus;
    private JButton         btnSebelumnya, btnSelanjutnya;
    private JLabel          lblInfoHalaman;
    private JComboBox<Integer> cmbPageSize;

    // State paginasi
    private final PlayStationDAO PlayStationDAO     = new PlayStationDAO();
    private int           currentPage = 1;
    private int           pageSize    = 10;
    private int           totalData   = 0;

    public DaftarPlayStationFrame() {
        setTitle("Sistem Manajemen - Rental PlayStation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(980, 620);
        setMinimumSize(new Dimension(800, 500));
        setLocationRelativeTo(null);
        buatKomponen();
        muatData();
    }

    // ----------------------------------------------------------------
    // Membangun semua komponen UI
    // ----------------------------------------------------------------
    private void buatKomponen() {

        // === HEADER ===
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(new Color(25, 85, 155));
        JLabel lblJudul = new JLabel("Sistem Manajemen - Rental PlayStation",
                                     SwingConstants.CENTER);
        lblJudul.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblJudul.setForeground(Color.BLACK);
        lblJudul.setBorder(BorderFactory.createEmptyBorder(14, 0, 14, 0));
        panelHeader.add(lblJudul, BorderLayout.CENTER);
        add(panelHeader, BorderLayout.NORTH);

        // === TABEL ===
        tableModel = new PlayStationTableModel();
        tabelPlayStation  = new JTable(tableModel);
        tabelPlayStation.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelPlayStation.setRowHeight(28);
        tabelPlayStation.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tabelPlayStation.setGridColor(new Color(210, 220, 230));
        tabelPlayStation.setShowGrid(true);
        tabelPlayStation.setIntercellSpacing(new Dimension(1, 1));

        // Lebar kolom
        TableColumnModel tcm = tabelPlayStation.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(50);  tcm.getColumn(0).setMaxWidth(60);
        tcm.getColumn(1).setPreferredWidth(270); // Judul
        tcm.getColumn(2).setPreferredWidth(160); // Penulis
        tcm.getColumn(3).setPreferredWidth(120); // ISBN
        tcm.getColumn(4).setPreferredWidth(150); // Penerbit
        tcm.getColumn(5).setPreferredWidth(65);  tcm.getColumn(5).setMaxWidth(75);

        // Style header tabel
        JTableHeader header = tabelPlayStation.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 13));
        header.setBackground(new Color(50, 120, 200));
        header.setForeground(Color.BLACK);
        header.setPreferredSize(new Dimension(header.getWidth(), 32));
        header.setReorderingAllowed(false);

        // Warna baris bergantian (zebra striping)
        tabelPlayStation.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            private final Color WARNA_GENAP = Color.WHITE;
            private final Color WARNA_GANJIL = new Color(238, 245, 255);
            private final Color WARNA_PILIH  = new Color(173, 216, 230);

            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                if (isSelected) {
                    c.setBackground(WARNA_PILIH);
                    c.setForeground(Color.DARK_GRAY);
                } else {
                    c.setBackground(row % 2 == 0 ? WARNA_GENAP : WARNA_GANJIL);
                    c.setForeground(Color.DARK_GRAY);
                }
                // Kolom "No" dan "Tahun" rata tengah
                if (column == 0 || column == 5) {
                    ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                } else {
                    ((JLabel) c).setHorizontalAlignment(SwingConstants.LEFT);
                }
                setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4));
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(tabelPlayStation);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(8, 12, 0, 12),
            BorderFactory.createLineBorder(new Color(180, 200, 220))));
        add(scrollPane, BorderLayout.CENTER);

        // === PANEL BAWAH ===
        JPanel panelBawah = new JPanel(new BorderLayout(0, 6));
        panelBawah.setBorder(BorderFactory.createEmptyBorder(8, 12, 12, 12));
        panelBawah.setBackground(new Color(245, 248, 252));

        // Paginasi
        JPanel panelPaging = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 4));
        panelPaging.setOpaque(false);
        btnSebelumnya = buatTombol("<< Sebelumnya", new Color(50, 120, 200), false);
        btnSelanjutnya = buatTombol("Selanjutnya >>", new Color(50, 120, 200), false);
        lblInfoHalaman = new JLabel("Halaman 1 dari 1   |   Total: 0 ps");
        lblInfoHalaman.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblInfoHalaman.setForeground(new Color(60, 80, 100));

        JLabel lblPer = new JLabel("  Tampilkan:");
        lblPer.setFont(new Font("SansSerif", Font.PLAIN, 12));
        cmbPageSize = new JComboBox<>(new Integer[]{5, 10, 20, 50});
        cmbPageSize.setSelectedItem(10);
        cmbPageSize.setFont(new Font("SansSerif", Font.PLAIN, 12));
        cmbPageSize.setPreferredSize(new Dimension(65, 28));

        panelPaging.add(btnSebelumnya);
        panelPaging.add(Box.createHorizontalStrut(10));
        panelPaging.add(lblInfoHalaman);
        panelPaging.add(Box.createHorizontalStrut(10));
        panelPaging.add(btnSelanjutnya);
        panelPaging.add(Box.createHorizontalStrut(20));
        panelPaging.add(lblPer);
        panelPaging.add(cmbPageSize);

        // Tombol aksi
        JPanel panelAksi = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 4));
        panelAksi.setOpaque(false);
        btnTambah = buatTombol("  Tambah", new Color(28, 120, 60), true);
        btnEdit   = buatTombol("  Edit  ",      new Color(180, 100, 0), true);
        btnHapus  = buatTombol("  Hapus  ",     new Color(180, 40,  40), true);
        panelAksi.add(btnTambah);
        panelAksi.add(btnEdit);
        panelAksi.add(btnHapus);

        panelBawah.add(panelPaging, BorderLayout.CENTER);
        panelBawah.add(panelAksi,   BorderLayout.SOUTH);
        add(panelBawah, BorderLayout.SOUTH);

        // === EVENT HANDLER ===
        btnSebelumnya.addActionListener(e -> {
            if (currentPage > 1) { currentPage--; muatData(); }
        });
        btnSelanjutnya.addActionListener(e -> {
            if (currentPage < totalHalaman()) { currentPage++; muatData(); }
        });
        cmbPageSize.addActionListener(e -> {
            pageSize    = (Integer) cmbPageSize.getSelectedItem();
            currentPage = 1;
            muatData();
        });
        btnTambah.addActionListener(e -> aksiTambah());
        btnEdit.addActionListener(e   -> aksiEdit());
        btnHapus.addActionListener(e  -> aksiHapus());
    }

    private JButton buatTombol(String teks, Color bg, boolean bold) {
        JButton btn = new JButton(teks);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", bold ? Font.BOLD : Font.PLAIN, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setPreferredSize(new Dimension(140, 32));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

// ----------------------------------------------------------------
// Menghitung total jumlah halaman
// ----------------------------------------------------------------
        private int totalHalaman() {
            return (int) Math.max(1, Math.ceil((double) totalData / pageSize));
        }


// ----------------------------------------------------------------
// Memuat / merefresh data dari database
// ----------------------------------------------------------------
        public void muatData() {

            try {

                totalData = PlayStationDAO.countSemua();

                int offset = (currentPage - 1) * pageSize;

                List<PlayStation> list =
                        PlayStationDAO.getSemuaPlayStation(offset, pageSize);


                tableModel.setData(list, offset + 1);


                lblInfoHalaman.setText(String.format(
                    "Halaman %d dari %d   |   Total: %d PlayStation",
                    currentPage,
                    totalHalaman(),
                    totalData
                ));


                btnSebelumnya.setEnabled(currentPage > 1);

                btnSelanjutnya.setEnabled(
                    currentPage < totalHalaman()
                );


            } catch (SQLException ex) {

                tampilError(
                    "Gagal memuat data PlayStation:\n"
                    + ex.getMessage()
                );

            }
        }


// ----------------------------------------------------------------
// Aksi tombol Tambah PlayStation
// ----------------------------------------------------------------
            private void aksiTambah() {

            TambahPlayStationDialog dialog = new TambahPlayStationDialog(this);
            dialog.setVisible(true);


            if (dialog.isDisimpan()) {
                muatData();
            }
        }


        // ----------------------------------------------------------------
        // Aksi tombol Edit PlayStation
        // ----------------------------------------------------------------
        private void aksiEdit() {

            int baris = tabelPlayStation.getSelectedRow();


            if (baris < 0) {

                JOptionPane.showMessageDialog(
                    this,
                    "Silakan pilih PlayStation yang ingin diedit.",
                    "Perhatian",
                    JOptionPane.WARNING_MESSAGE
                );

                return;
            }


            PlayStation playStation =
                    tableModel.getPlayStationAt(baris);


            EditPlayStationDialog dialog =
                    new EditPlayStationDialog(
                            this,
                            playStation
                    );


            dialog.setVisible(true);


            if (dialog.isDisimpan()) {
                muatData();
            }

        }


// ----------------------------------------------------------------
// Aksi tombol Hapus PlayStation
// ----------------------------------------------------------------
private void aksiHapus() {

    int baris = tabelPlayStation.getSelectedRow();


    if (baris < 0) {

        JOptionPane.showMessageDialog(
            this,
            "Silakan pilih PlayStation yang ingin dihapus.",
            "Perhatian",
            JOptionPane.WARNING_MESSAGE
        );

        return;
    }


    PlayStation playStation =
            tableModel.getPlayStationAt(baris);



    int pilihan = JOptionPane.showConfirmDialog(
        this,
        "Apakah Anda yakin ingin menghapus PlayStation:\n\""
        + playStation.getNamaKonsol()
        + "\" seri "
        + playStation.getSeriKonsol()
        + "?",
        "Konfirmasi Hapus",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.WARNING_MESSAGE
    );


    if (pilihan == JOptionPane.YES_OPTION) {


        try {

            PlayStationDAO.hapusPlayStation(
                    playStation.getId()
            );


            int perkiraanTotalBaru = totalData - 1;


            int halamanMaks = (int) Math.max(
                1,
                Math.ceil(
                    (double) perkiraanTotalBaru / pageSize
                )
            );


            if (currentPage > halamanMaks) {

                currentPage = halamanMaks;

            }


            muatData();


            JOptionPane.showMessageDialog(
                this,
                "PlayStation \""
                + playStation.getNamaKonsol()
                + "\" berhasil dihapus.",
                "Berhasil",
                JOptionPane.INFORMATION_MESSAGE
            );


        } catch (SQLException ex) {


            tampilError(
                "Gagal menghapus PlayStation:\n"
                + ex.getMessage()
            );

        }

    }

}


private void tampilError(String pesan) {

    JOptionPane.showMessageDialog(
        this,
        pesan,
        "Error",
        JOptionPane.ERROR_MESSAGE
    );

 }
}