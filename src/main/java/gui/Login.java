package gui;

import dao.EventoDAO;
import dao.OrganizzatoreDAO;
import dao.PartecipanteDAO;
import dao.UtenteDAO;
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
        IGiudiceDAO giudiceDAO = new IGiudiceDAO();
        IPartecipanteDAO partecipanteDAO = new IPartecipanteDAO();
        IOrganizzatoreDAO organizzatoreDAO = new IOrganizzatoreDAO();
        IEventoDAO eventoDAO = new IEventoDAO(organizzatoreDAO, giudiceDAO, partecipanteDAO);
        ITeamDAO teamDAO = new ITeamDAO();
        controller = new Controller(utenteDAO, organizzatoreDAO, partecipanteDAO, eventoDAO, teamDAO);
        accediButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = new String(nomeUtenteField.getText());
                String password = new String(passwordField.getText());

                if (nome.isEmpty() || password.isEmpty()) {
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
                        ViewEvento terzaGUI = new ViewEvento(controller, frame, null, null, null);
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