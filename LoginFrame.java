import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;


    public LoginFrame() {

        setTitle("Login Admin Rental PS");
        setSize(350,200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        JPanel panel = new JPanel(new GridLayout(3,2,10,10));

        txtUsername = new JTextField();
        txtPassword = new JPasswordField();
        btnLogin = new JButton("Login");


        panel.add(new JLabel("Username"));
        panel.add(txtUsername);

        panel.add(new JLabel("Password"));
        panel.add(txtPassword);

        panel.add(new JLabel(""));
        panel.add(btnLogin);


        add(panel);


        btnLogin.addActionListener(e -> login());

    }


    private void login(){

        String username = txtUsername.getText();
        String password = new String(
                txtPassword.getPassword()
        );


        if(username.equals("admin") &&
           password.equals("admin123")){


            JOptionPane.showMessageDialog(
                this,
                "Login berhasil"
            );


            new DaftarPlayStationFrame()
                    .setVisible(true);


            dispose();


        }else{

            JOptionPane.showMessageDialog(
                this,
                "Username atau password salah"
            );

        }

    }

}