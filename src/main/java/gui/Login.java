package gui;

import implementazione_postgres_dao.*;
import model .*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import controller.Controller;

public class Login {
    private JPanel Login;
    private JLabel nomeUtenteLabel;
    private JLabel passwordLabel;
    private JTextField nomeUtenteField;
    private JPasswordField passwordField;
    private JButton accediButton;
    private JButton registratiButton;
    private JPanel panel;
    public static JFrame frame;
    public JFrame frame2;
    public JFrame frameEventi;
    private Controller controller;

    public Login() {
        Color bgColor = new Color(240, 248, 255); // Azzurrino molto chiaro
        Color btnColor = new Color(30, 144, 255); // Blu moderno
        Color btnHoverColor = new Color(65, 105, 225); // Blu scuro

        Login.setBackground(bgColor);
        panel.setBackground(bgColor);

        // Font moderni
        Font labelFont = new Font("SansSerif", Font.BOLD, 16);
        Font fieldFont = new Font("SansSerif", Font.PLAIN, 15);

        nomeUtenteLabel.setFont(labelFont);
        passwordLabel.setFont(labelFont);
        nomeUtenteField.setFont(fieldFont);
        passwordField.setFont(fieldFont);

        // Pulsanti stilizzati
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

        // Effetto hover
        accediButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                accediButton.setBackground(btnHoverColor);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                accediButton.setBackground(btnColor);
            }
        });
        registratiButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                registratiButton.setBackground(btnHoverColor);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                registratiButton.setBackground(btnColor);
            }
        });

        // Migliora layout con padding
        Login.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        IUtenteDAO utenteDAO = new IUtenteDAO();
        IOrganizzatoreDAO organizzatoreDAO = new IOrganizzatoreDAO();
        IPartecipanteDAO partecipanteDAO = new IPartecipanteDAO();
        IEventoDAO eventoDAO = new IEventoDAO();
        ITeamDAO teamDAO = new ITeamDAO();
        IGiudiceDAO giudiceDAO = new IGiudiceDAO();
        IVotoDAO votoDAO = new IVotoDAO();
        IDocumentoDAO documentoDAO = new IDocumentoDAO();
        IInvitoGiudiceDAO invitoGiudiceDAO = new IInvitoGiudiceDAO();
        organizzatoreDAO.setUtenteDAO(utenteDAO);
        partecipanteDAO.setUtenteDAO(utenteDAO);
        eventoDAO.setOrganizzatoreDAO(organizzatoreDAO);
        teamDAO.setPartecipanteDAO(partecipanteDAO);
        teamDAO.setVotoDAO(votoDAO);
        giudiceDAO.setUtenteDAO(utenteDAO);
        votoDAO.setGiudiceDAO(giudiceDAO);
        invitoGiudiceDAO.setUtenteDAO(utenteDAO);
        invitoGiudiceDAO.setEventoDAO(eventoDAO);
        documentoDAO.setTeamDAO(teamDAO);
        documentoDAO.setDocumentoDAO(documentoDAO);
        documentoDAO.setGiudiceDAO(giudiceDAO);
        controller = new Controller(utenteDAO, organizzatoreDAO, partecipanteDAO, giudiceDAO, eventoDAO, teamDAO, invitoGiudiceDAO, documentoDAO, votoDAO);

        accediButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = nomeUtenteField.getText();
                String password = passwordField.getText();

                if (nome.trim().isEmpty() || password.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Inserisci sia il nome utente che la password!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Utente u = controller.loginUtente(nome, password);
                if (u != null) {
                    if (u instanceof Organizzatore) {
                        AreaOrganizzatore QuartaGUI = new AreaOrganizzatore(controller, (Organizzatore) u, null, frame);
                        QuartaGUI.frameOrganizzatore.setVisible(true);
                        frame.setVisible(false);
                    }else {
                        ViewEvento terzaGUI = new ViewEvento(controller, nome, frame, null, null, null);
                        terzaGUI.frameEventi.setVisible(true);
                        frame.setVisible(false);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Nessun utente trovato, effettua prima la registrazione se non ancora lo hai fatto.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        registratiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Registrazione secondGUI = new Registrazione(controller, frame);
                secondGUI.frameRegistrazione.setVisible(true);
                frame.setVisible(false);
            }
        });

    }
    public static void main (String[]args){
        frame = new JFrame("Login");
        frame.setContentPane(new Login().Login);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(600, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}