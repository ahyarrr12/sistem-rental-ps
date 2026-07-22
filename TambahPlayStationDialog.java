import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class TambahPlayStationDialog extends JDialog {

    private JComboBox<String> cmbNamaKonsol;
    private JComboBox<String> cmbSeriKonsol;
    private JTextField txtHargaPerJam;
    private JTextField txtKeterangan;

    private JComboBox<String> cmbStatus;

    private JButton btnSimpan;
    private JButton btnBatal;

    private boolean disimpan = false;

    private final PlayStationDAO playStationDAO = new PlayStationDAO();


    public TambahPlayStationDialog(Frame parent) {

        super(parent, "Tambah Data PlayStation", true);

        setSize(500, 400);
        setResizable(false);
        setLocationRelativeTo(parent);

        buatKomponen();
    }


    private void buatKomponen() {


        JPanel panelUtama = new JPanel(new BorderLayout(10,10));

        panelUtama.setBorder(
            BorderFactory.createEmptyBorder(20,28,18,28)
        );


        // ================= HEADER =================

        JPanel panelHeader = new JPanel(new BorderLayout());

        panelHeader.setBackground(
            new Color(160,90,0)
        );


        JLabel lblHeader = new JLabel(
            "TAMBAH DATA PLAYSTATION",
            SwingConstants.CENTER
        );


        lblHeader.setFont(
            new Font("SansSerif",Font.BOLD,15)
        );

        lblHeader.setForeground(Color.WHITE);


        lblHeader.setBorder(
            BorderFactory.createEmptyBorder(8,0,8,0)
        );


        panelHeader.add(lblHeader);



        // ================= FORM =================


        JPanel panelForm = new JPanel(
            new GridBagLayout()
        );


        panelForm.setBorder(
            BorderFactory.createEmptyBorder(12,0,8,0)
        );


        GridBagConstraints g = new GridBagConstraints();

        g.fill = GridBagConstraints.HORIZONTAL;

        g.insets = new Insets(6,4,6,4);



        cmbNamaKonsol = new JComboBox<>(new String[]{"PlayStation"});
        cmbNamaKonsol.setSelectedItem("PlayStation");

        cmbSeriKonsol = new JComboBox<>(new String[]{"PS3", "PS4", "PS5"});

        txtHargaPerJam = buatTextField();
        txtKeterangan = buatTextField();



        cmbStatus = new JComboBox<>();

        cmbStatus.addItem("Tersedia");
        cmbStatus.addItem("Dipinjam");



        // Nama Konsol

        g.gridx = 0;
        g.gridy = 0;
        g.weightx = 0.35;

        panelForm.add(
            new JLabel("Nama Konsol *:"),g
        );


        g.gridx = 1;
        g.weightx = 0.65;

        panelForm.add(
            cmbNamaKonsol,g
        );



        // Seri

        g.gridx = 0;
        g.gridy = 1;

        panelForm.add(
            new JLabel("Seri Konsol *:"),g
        );


        g.gridx = 1;

        panelForm.add(
            cmbSeriKonsol,g
        );



        // Harga

        g.gridx = 0;
        g.gridy = 2;

        panelForm.add(
            new JLabel("Harga Per Jam:"),g
        );


        g.gridx = 1;

        panelForm.add(
            txtHargaPerJam,g
        );



        // Status

        g.gridx = 0;
        g.gridy = 3;


        panelForm.add(
            new JLabel("Status:"),g
        );


        g.gridx = 1;


        panelForm.add(
            cmbStatus,g
        );



        // Keterangan

        g.gridx = 0;
        g.gridy = 4;


        panelForm.add(
            new JLabel("Keterangan:"),g
        );


        g.gridx = 1;


        panelForm.add(
            txtKeterangan,g
        );



        // ================= BUTTON =================


        JPanel panelTombol = new JPanel(
            new FlowLayout(
                FlowLayout.CENTER,
                14,
                6
            )
        );



        btnSimpan = buatTombol(
            "Simpan",
            new Color(160,90,0)
        );


        btnBatal = buatTombol(
            "Batal",
            new Color(120,120,120)
        );



        panelTombol.add(btnSimpan);
        panelTombol.add(btnBatal);



        panelUtama.add(
            panelHeader,
            BorderLayout.NORTH
        );


        panelUtama.add(
            panelForm,
            BorderLayout.CENTER
        );


        panelUtama.add(
            panelTombol,
            BorderLayout.SOUTH
        );


        add(panelUtama);



        btnSimpan.addActionListener(
            e -> aksiSimpan()
        );


        btnBatal.addActionListener(
            e -> dispose()
        );



        SwingUtilities.invokeLater(
            () -> cmbNamaKonsol.requestFocus()
        );

    }




    private JTextField buatTextField(){

        JTextField tf = new JTextField(28);


        tf.setFont(
            new Font(
                "SansSerif",
                Font.PLAIN,
                13
            )
        );


        return tf;
    }




    private JButton buatTombol(
        String teks,
        Color warna
    ){

        JButton btn = new JButton(teks);


        btn.setBackground(warna);

        btn.setForeground(Color.WHITE);

        btn.setFont(
            new Font(
                "SansSerif",
                Font.BOLD,
                13
            )
        );


        btn.setPreferredSize(
            new Dimension(120,36)
        );


        btn.setFocusPainted(false);

        btn.setBorderPainted(false);

        btn.setOpaque(true);


        return btn;
    }





    private void aksiSimpan(){


        String namaKonsol =
            cmbNamaKonsol.getSelectedItem().toString();


        String seriKonsol =
            cmbSeriKonsol.getSelectedItem().toString();


        String hargaStr =
            txtHargaPerJam.getText().trim();



        String status =
            cmbStatus.getSelectedItem().toString();



        String keterangan =
            txtKeterangan.getText().trim();



        if(namaKonsol.isEmpty()){

            tampilPeringatan(
                "Nama Konsol wajib diisi!"
            );

            return;
        }



        if(seriKonsol.isEmpty()){

            tampilPeringatan(
                "Seri Konsol wajib diisi!"
            );

            return;
        }




        double hargaPerJam;


        try{


            hargaPerJam =
                Double.parseDouble(hargaStr);



            if(hargaPerJam <= 0){

                throw new NumberFormatException();

            }


        }
        catch(NumberFormatException ex){


            tampilPeringatan(
                "Harga per jam harus berupa angka!"
            );


            return;
        }





        try{


            PlayStation psBaru =
                new PlayStation(
                    0,
                    namaKonsol,
                    seriKonsol,
                    hargaPerJam,
                    status,
                    keterangan
                );



            playStationDAO.insertPlayStation(
                psBaru
            );



            disimpan = true;



            JOptionPane.showMessageDialog(
                this,
                "Data PlayStation berhasil ditambahkan",
                "Berhasil",
                JOptionPane.INFORMATION_MESSAGE
            );



            dispose();



        }
        catch(SQLException ex){


            JOptionPane.showMessageDialog(
                this,
                "Gagal menyimpan data:\n"
                + ex.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );

        }

    }




    private void tampilPeringatan(String pesan){

        JOptionPane.showMessageDialog(
            this,
            pesan,
            "Validasi Input",
            JOptionPane.WARNING_MESSAGE
        );

    }



    public boolean isDisimpan(){

        return disimpan;

    }

}
