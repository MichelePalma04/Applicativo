package gui;

import javax.swing.*;

public class Login {
    private JPanel Login;
    private JPanel PanneloMail;
    private JLabel mailLabel;
    private JLabel passwordLabel;
    private JTextField mailField;
    private JPasswordField passwordField;
    private JButton button1;
    private JButton button2;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Login");
        frame.setContentPane(new Login().Login);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
