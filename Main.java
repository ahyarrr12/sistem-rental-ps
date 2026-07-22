// ================================================================
// File  : Main.java
// Desc  : Entry Point Sistem Informasi Rental PlayStation
// ================================================================

import javax.swing.*;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            // Menggunakan tampilan sesuai sistem operasi
            try {
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Inisialisasi database SQLite
            try {
                DatabaseHelper.inisialisasi();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(
                        null,
                        "Gagal menginisialisasi database!\n" + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // Tampilkan form Login
            LoginFrame login = new LoginFrame();
            login.setVisible(true);

            // Tutup koneksi database saat aplikasi ditutup
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                DatabaseHelper.tutupKoneksi();
            }));

        });

    }

}