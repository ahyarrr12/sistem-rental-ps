import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class EditPlayStationDialog extends JDialog {

    private JTextField txtHargaPerJam, txtKeterangan;
    private JComboBox<String> cmbNamaKonsol, cmbSeriKonsol, cmbStatus;
    private JButton    btnSimpan, btnBatal;
    private boolean    disimpan = false;

    private final PlayStation    playStationAsal;
    private final PlayStationDAO playStationDAO = new PlayStationDAO();

    /**
     * @param parent   Frame pemanggil (DaftarPlayStationFrame)
     * @param playStationAsal Objek PlayStation yang akan diedit (sudah ada di database)
     */
    public EditPlayStationDialog(Frame parent, PlayStation playStation) {
        super(parent, "Edit PlayStation", true);  // true = modal
        this.playStationAsal = playStation;
        setSize(500, 400);
        setResizable(false);
        setLocationRelativeTo(parent);
        buatKomponen();
        isiDataAwal();
    }

    private void buatKomponen() {
        JPanel panelUtama = new JPanel(new BorderLayout(10, 10));
        panelUtama.setBorder(BorderFactory.createEmptyBorder(20, 28, 18, 28));

        // --- Header dialog ---
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(new Color(160, 90, 0));
        JLabel lblHeader = new JLabel("EDIT DATA PLAYSTATION", SwingConstants.CENTER);
        lblHeader.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblHeader.setForeground(Color.WHITE);
        lblHeader.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        panelHeader.add(lblHeader);

        // --- Form isi data ---
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createEmptyBorder(12, 0, 8, 0));
        GridBagConstraints g = new GridBagConstraints();
        g.fill   = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(6, 4, 6, 4);
        g.anchor = GridBagConstraints.WEST;

        cmbNamaKonsol  = new JComboBox<>(new String[]{"PlayStation"});
        cmbSeriKonsol  = new JComboBox<>(new String[]{"PS3", "PS4", "PS5"});
        txtHargaPerJam     = buatTextField();
        txtKeterangan = buatTextField();

        cmbStatus = new JComboBox<>();

        cmbStatus.addItem("Tersedia");
        cmbStatus.addItem("Disewa");
        cmbStatus.addItem("Maintenance");

        String[]    labels     = {"Nama Konsol *", "Seri Konsol *", "Harga Per Jam",
                               "Keterangan"};
        JComponent[] fields = {cmbNamaKonsol, cmbSeriKonsol, txtHargaPerJam, txtKeterangan};

        for (int i = 0; i < labels.length; i++) {
            g.gridx = 0; g.gridy = i; g.weightx = 0.35;
            JLabel lbl = new JLabel(labels[i] + ":");
            lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
            panelForm.add(lbl, g);

            g.gridx = 1; g.weightx = 0.65;
            panelForm.add(fields[i], g);
        }

        g.gridx = 0; g.gridy = labels.length; g.gridwidth = 2; g.weightx = 1.0;
        JLabel lblWajib = new JLabel("  * Kolom wajib diisi");
        lblWajib.setFont(new Font("SansSerif", Font.ITALIC, 11));
        lblWajib.setForeground(new Color(150, 50, 50));
        panelForm.add(lblWajib, g);

        // --- Tombol aksi ---
        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 6));
        btnSimpan = buatTombol("  Simpan  ", new Color(160, 90, 0));
        btnBatal  = buatTombol("  Batal   ", new Color(120, 120, 120));
        panelTombol.add(btnSimpan);
        panelTombol.add(btnBatal);

        panelUtama.add(panelHeader,  BorderLayout.NORTH);
        panelUtama.add(panelForm,    BorderLayout.CENTER);
        panelUtama.add(panelTombol,  BorderLayout.SOUTH);
        add(panelUtama);

        btnSimpan.addActionListener(e -> aksiSimpan());
        btnBatal.addActionListener(e  -> dispose());
        SwingUtilities.invokeLater(() -> cmbNamaKonsol.requestFocus());
    }
 
    /** Mengisi form dengan data playstation yang sudah ada */
private void isiDataAwal() {
    cmbNamaKonsol.setSelectedItem(playStationAsal.getNamaKonsol());
    cmbSeriKonsol.setSelectedItem(playStationAsal.getSeriKonsol());
    txtHargaPerJam.setText(String.valueOf(playStationAsal.getHargaPerJam()));
    txtKeterangan.setText(playStationAsal.getKeterangan());
  
    cmbStatus.setSelectedItem(playStationAsal.getStatus());
}
 
    private JTextField buatTextField() {
        JTextField tf = new JTextField(28);
        tf.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 200, 220)),
            BorderFactory.createEmptyBorder(4, 6, 4, 6)));
        return tf;
    }

    private JButton buatTombol(String teks, Color bg) {
        JButton btn = new JButton(teks);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setPreferredSize(new Dimension(120, 36));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        return btn;
    }

    // ----------------------------------------------------------------
    // Validasi input, simpan perubahan ke DB, dan tutup dialog
    // ----------------------------------------------------------------
    private void aksiSimpan() {
            String namaKonsol = cmbNamaKonsol.getSelectedItem().toString();
            String seriKonsol = cmbSeriKonsol.getSelectedItem().toString();
            String hargaStr = txtHargaPerJam.getText().trim();
            String status = cmbStatus.getSelectedItem().toString();
            String keterangan = txtKeterangan.getText().trim();

            double hargaPerJam;

            try {
                hargaPerJam = Integer.parseInt(hargaStr);

                if (hargaPerJam <= 0) {
                    throw new NumberFormatException();
                }

            } catch (NumberFormatException ex) {
                tampilPeringatan("Harga per jam harus berupa angka");
                txtHargaPerJam.requestFocus();
                return;
            }

            try {
            PlayStation psBaru = new PlayStation(
                playStationAsal.getId(), 
                namaKonsol, 
                seriKonsol,
                hargaPerJam,
                status,
                keterangan
            );

            playStationDAO.updatePlayStation(psBaru);
            disimpan = true;

            JOptionPane.showMessageDialog(
                this,
                "Data PlayStation berhasil diperbarui",
                "Berhasil", 
                JOptionPane.INFORMATION_MESSAGE
            );

            dispose();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Gagal menyimpan perubahan:\n" + ex.getMessage(),
                "Error Database", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void tampilPeringatan(String pesan) {
        JOptionPane.showMessageDialog(this, pesan, "Validasi Input",
            JOptionPane.WARNING_MESSAGE);
    }

    /** @return true jika user menekan Simpan dan perubahan berhasil disimpan */
    public boolean isDisimpan() { return disimpan; }
}
