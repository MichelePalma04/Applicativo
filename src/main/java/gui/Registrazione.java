package gui;
import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * GUI per la schermata di registrazione utente nell'applicazione Hackaton.
 * <p>
 * Permette all'utente di:
 * <ul>
 *   <li>Inserire nome utente e password per registrarsi</li>
 *   <li>Ricevere feedback in caso di campi vuoti, utente già esistente o registrazione riuscita</li>
 *   <li>Tornare automaticamente alla schermata di accesso dopo la registrazione o la chiusura della finestra</li>
 * </ul>
 * <p>
 * Gli stili grafici e le interazioni sono definiti per coerenza con il resto dell'applicazione.
 */
public class Registrazione {

    /** Pannello principale della GUI registrazione. */
    private JPanel panel;

    /** Label per il nome utente. */
    private JLabel nomeLabel;

    /** Campo di testo per il nome utente. */
    private JTextField nomeUtenteField;

    /** Label per la password. */
    private JLabel passwordLabel;

    /** Bottone per effettuare la registrazione. */
    private JButton registrati;

    /** Campo di testo per la password. */
    private JPasswordField passwordField;

    /** Frame principale della schermata di registrazione. */
    private JFrame frameRegistrazione;

    /** Frame della schermata di accesso (per tornare indietro). */
    private JFrame frameAccedi;

    /** Controller logico dell'applicazione. */
    private Controller controller;

    /**
     * Costruisce la schermata di registrazione, inizializzando i componenti grafici, lo stile e il controller.
     * Gestisce la registrazione dell'utente e il ritorno alla schermata di accesso.
     *
     * @param controller Controller logico dell'applicazione
     * @param frameAreaAccesso frame della schermata di accesso per tornare indietro
     */
    public Registrazione(Controller controller, JFrame frameAreaAccesso) {
        this.frameAccedi = frameAreaAccesso;
        this.controller = controller;

        Color bgColor = new Color(240, 248, 255);       // Azzurrino chiaro
        Color btnColor = new Color(30, 144, 255);       // Blu moderno
        Color btnHoverColor = new Color(65, 105, 225);  // Blu scuro

        Font labelFont = new Font("SansSerif", Font.BOLD, 16);
        Font fieldFont = new Font("SansSerif", Font.PLAIN, 15);

        panel.setBackground(bgColor);
        nomeLabel.setFont(labelFont);
        passwordLabel.setFont(labelFont);
        nomeUtenteField.setFont(fieldFont);
        passwordField.setFont(fieldFont);

        registrati.setBackground(btnColor);
        registrati.setForeground(Color.WHITE);
        registrati.setFocusPainted(false);
        registrati.setFont(labelFont);
        registrati.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));

        registrati.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                registrati.setBackground(btnHoverColor);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                registrati.setBackground(btnColor);
            }
        });

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

        registrati.addActionListener(e -> {
            String nome = nomeUtenteField.getText();
            String password = new String(passwordField.getPassword());
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
        });
    }

    /**
     * Restituisce il frame della registrazione.
     *
     * @return frame della registrazione
     */
    public JFrame getFrameRegistrazione() {
        return frameRegistrazione;
    }

}
