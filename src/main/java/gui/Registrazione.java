package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
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
    private JFrame frameRegistrazione;
    private JFrame frameAccedi;
    private Controller controller;

    public Registrazione(Controller controller, JFrame frameAreaAccesso) {
        this.frameAccedi = frameAreaAccesso;
        this.controller = controller;

        // Colori e font personalizzati
        Color bgColor = new Color(240, 248, 255);       // Azzurrino chiaro
        Color btnColor = new Color(30, 144, 255);       // Blu moderno
        Color btnHoverColor = new Color(65, 105, 225);  // Blu scuro

        Font labelFont = new Font("SansSerif", Font.BOLD, 16);
        Font fieldFont = new Font("SansSerif", Font.PLAIN, 15);

        // Applica i font e colore
        panel.setBackground(bgColor);
        nome.setFont(labelFont);
        password.setFont(labelFont);
        nomeUtenteField.setFont(fieldFont);
        passwordField.setFont(fieldFont);

        // Padding ai campi (non bordo arrotondato)


        // Stile pulsante
        registrati.setBackground(btnColor);
        registrati.setForeground(Color.WHITE);
        registrati.setFocusPainted(false);
        registrati.setFont(labelFont);
        registrati.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));

        // Effetto hover sul bottone
        registrati.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                registrati.setBackground(btnHoverColor);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                registrati.setBackground(btnColor);
            }
        });

        // Padding del pannello principale
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));


        frameRegistrazione = new JFrame("Registrazione");
        frameRegistrazione.setContentPane(panel);
        frameRegistrazione.pack();
        frameRegistrazione.setSize(600, 600);
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
                String nome = nomeUtenteField.getText();
                String password = passwordField.getText();

                if (nome.trim().isEmpty() || password.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(frameRegistrazione, "Inserisci sia il nome utente che la password!", "Error", JOptionPane.ERROR_MESSAGE);
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
    public JFrame getFrameRegistrazione() {
        return frameRegistrazione;
    }

}
