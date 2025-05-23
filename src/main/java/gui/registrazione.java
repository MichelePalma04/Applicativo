package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class registrazione {
    private JPanel panel;
    private JLabel email;
    private JTextField emailField;
    private JLabel password;
    private JButton registrazione;
    private JPasswordField passwordField;
    private JFrame frameRegistrazione;


    public registrazione() {
        registrazione.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String (passwordField.getPassword());

                boolean successo = Controller.registraUtente(email, password);
                if(successo) {
                    JOptionPane.showMessageDialog(frameRegistrazione, "Registrazione completata.");
                    frameRegistrazione.dispose();
                }else{
                    JOptionPane.showMessageDialog(frameRegistrazione, "Utente già esistente");
                }
            }
        });
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("Registrazione");
        frame.setContentPane(new registrazione().panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(500,500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
