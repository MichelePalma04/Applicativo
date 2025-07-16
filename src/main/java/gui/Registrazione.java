package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Registrazione {
    private JPanel panel;
    private JLabel nome;
    private JTextField nomeUtenteField;
    private JLabel password;
    private JButton registrati;
    private JPasswordField passwordField;
    public JFrame frameRegistrazione;
    public JFrame frameAccedi;
    private Controller controller;

    public Registrazione(Controller controller, JFrame frame) {
        this.frameAccedi = frame;
        this.controller = controller;

        frameRegistrazione = new JFrame("Registrazione");
        frameRegistrazione.setContentPane(panel);
        frameRegistrazione.pack();
        frameRegistrazione.setSize(500, 500);
        frameRegistrazione.setLocationRelativeTo(null);

        frameRegistrazione.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                frameAccedi.setVisible(true);
                frameRegistrazione.setVisible(false);
                frameRegistrazione.dispose();
            }
        });
        registrati.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = new String (nomeUtenteField.getText());
                String password = new String (passwordField.getPassword());

                if (nome.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Inserisci sia il nome utente che la password!");
                    return;
                }

                boolean successo = controller.registraUtente(nome, password);
                if(successo) {
                    JOptionPane.showMessageDialog(frameRegistrazione, "Registrazione completata.");
                    frameRegistrazione.dispose();
                    frameAccedi.setVisible(true);
                }else{
                    JOptionPane.showMessageDialog(frameRegistrazione, "Utente già esistente");
                    frameRegistrazione.dispose();
                    frameAccedi.setVisible(true);
                }
            }
        });
    }


}
