package gui;

import implementazionePostgresDAO.*;
import model .*;
import javax.swing.*;
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
        partecipanteDAO.setTeamDAO(teamDAO);
        eventoDAO.setPartecipanteDAO(partecipanteDAO);
        eventoDAO.setTeamDAO(teamDAO);
        eventoDAO.setGiudiceDAO(giudiceDAO);
        eventoDAO.setOrganizzatoreDAO(organizzatoreDAO);
        teamDAO.setPartecipanteDAO(partecipanteDAO);
        teamDAO.setVotoDAO(votoDAO);
        giudiceDAO.setUtenteDAO(utenteDAO);
        giudiceDAO.setOrganizzatoreDAO(organizzatoreDAO);
        giudiceDAO.setVotoDAO(votoDAO);
        giudiceDAO.setEventoDAO(eventoDAO);
        votoDAO.setGiudiceDAO(giudiceDAO);
        votoDAO.setTeamDAO(teamDAO);
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
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}