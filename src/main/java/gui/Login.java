package gui;

import implementazione_postgres_dao.*;
import model .*;
import javax.swing.*;
import java.awt.*;
import controller.Controller;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/**
 * GUI per la schermata di login dell'applicazione Hackaton.
 * <p>
 * Consente all'utente di:
 * <ul>
 *   <li>Effettuare l'accesso tramite nome utente e password</li>
 *   <li>Accedere alla schermata di registrazione</li>
 *   <li>Ricevere feedback in caso di errori di autenticazione o campi vuoti</li>
 *   <li>Viene indirizzato in base al ruolo: organizzatore nell'area organizzatore, altrimenti nell'area eventi</li>
 * </ul>
 * <p>
 * La classe si occupa anche di inizializzare tutti i DAO e il Controller per la gestione logica dell'applicazione.
 * Gli stili e le interazioni sono definiti per coerenza con il resto dell'applicazione.
 */
public class Login {
    /** Pannello principale per la schermata di login. */
    private JPanel loginPanel;

    /** Label per il nome utente. */
    private JLabel nomeUtenteLabel;

    /** Label per la password. */
    private JLabel passwordLabel;

    /** Campo di testo per il nome utente. */
    private JTextField nomeUtenteField;

    /** Campo di testo per la password. */
    private JPasswordField passwordField;

    /** Bottone per accedere all'applicazione. */
    private JButton accediButton;

    /** Bottone per accedere alla schermata di registrazione. */
    private JButton registratiButton;

    /** Pannello secondario. */
    private JPanel panel;

    /** Frame principale della schermata di login. */
    private static JFrame frame;

    /** Controller logico dell'applicazione. */
    private Controller controller;

    /**
     * Costruisce la schermata di login, inizializzando componenti grafici, stili, DAO e Controller.
     * Gestisce le azioni di login e di accesso alla registrazione.
     */
    public Login() {
        Color bgColor = new Color(240, 248, 255); // Azzurrino molto chiaro
        Color btnColor = new Color(30, 144, 255); // Blu moderno
        Color btnHoverColor = new Color(65, 105, 225); // Blu scuro

        loginPanel.setBackground(bgColor);
        panel.setBackground(bgColor);

        Font labelFont = new Font("SansSerif", Font.BOLD, 16);
        Font fieldFont = new Font("SansSerif", Font.PLAIN, 15);

        nomeUtenteLabel.setFont(labelFont);
        passwordLabel.setFont(labelFont);
        nomeUtenteField.setFont(fieldFont);
        passwordField.setFont(fieldFont);

        accediButton.setBackground(btnColor);
        accediButton.setForeground(Color.WHITE);
        accediButton.setFocusPainted(false);
        accediButton.setFont(labelFont);
        accediButton.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));

        registratiButton.setBackground(btnColor);
        registratiButton.setForeground(Color.WHITE);
        registratiButton.setFocusPainted(false);
        registratiButton.setFont(labelFont);
        registratiButton.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));

        accediButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                accediButton.setBackground(btnHoverColor);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                accediButton.setBackground(btnColor);
            }
        });
        registratiButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                registratiButton.setBackground(btnHoverColor);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                registratiButton.setBackground(btnColor);
            }
        });

        loginPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        IUtenteDAO utenteDAO = new IUtenteDAO();
        IOrganizzatoreDAO organizzatoreDAO = new IOrganizzatoreDAO();
        IPartecipanteDAO partecipanteDAO = new IPartecipanteDAO();
        IEventoDAO eventoDAO = new IEventoDAO();
        ITeamDAO teamDAO = new ITeamDAO();
        IGiudiceDAO giudiceDAO = new IGiudiceDAO();
        IVotoDAO votoDAO = new IVotoDAO();
        IDocumentoDAO documentoDAO = new IDocumentoDAO();
        IInvitoGiudiceDAO invitoGiudiceDAO = new IInvitoGiudiceDAO();
        controller = new Controller(utenteDAO, organizzatoreDAO, partecipanteDAO, giudiceDAO, eventoDAO, teamDAO, invitoGiudiceDAO, documentoDAO, votoDAO);

        accediButton.addActionListener(e -> {
            String nome = nomeUtenteField.getText();
            String password = new String (passwordField.getPassword());

            if (nome.trim().isEmpty() || password.trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Inserisci sia il nome utente che la password!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Utente u = controller.loginUtente(nome, password);
            if (u != null) {
                if (u instanceof Organizzatore organizzatore) {
                    AreaOrganizzatore quartaGUI = new AreaOrganizzatore(controller, organizzatore, null, frame);
                    quartaGUI.getFrameOrganizzatore().setVisible(true);
                    frame.setVisible(false);
                } else {
                    ViewEvento terzaGUI = new ViewEvento(controller, nome, frame, null, null, null);
                    terzaGUI.getFrameEventi().setVisible(true);
                    frame.setVisible(false);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Nessun utente trovato, effettua prima la registrazione se non ancora lo hai fatto.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        registratiButton.addActionListener(e -> {
            Registrazione secondGUI = new Registrazione(controller, frame);
            secondGUI.getFrameRegistrazione().setVisible(true);
            frame.setVisible(false);
        });

    }

    /**
     * Metodo main: avvia la schermata di login.
     * @param args argomenti della riga di comando (non usati)
     */
    public static void main (String[]args){
        frame = new JFrame("Login");
        frame.setContentPane(new Login().loginPanel);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(600, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}