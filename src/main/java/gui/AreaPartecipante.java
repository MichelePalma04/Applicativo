package gui;

import controller.Controller;
import model.Evento;
import model.Partecipante;
import model.Team;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AreaPartecipante {
    private JLabel avviso;
    private JButton homeButton;
    private JComboBox <Team> comboBox1;
    private JButton uniscitiButton;
    private JLabel benvenuto;
    private JLabel teamLabel;
    private JPanel panel;
    private JLabel messaggio;
    private JButton caricaDocumento;
    private JLabel inserisciDocumento;
    private JTextField nomedocFied;
    private JPanel panel2;
    private JLabel problema;
    private Controller controller;
    public JFrame frameAreaPartecipante;
    public JFrame frameEventi;
    public JFrame frameAccedi;
    public JFrame frameNotifica;
    public JFrame frameGiudice;

    public AreaPartecipante(Partecipante partecipante, Evento evento, JFrame frame, JFrame frame2, JFrame frame3, JFrame frame4, Controller controller) {
        this.controller = controller;
        frameEventi = frame;
        frameAccedi = frame2;
        frameNotifica = frame3;
        frameGiudice = frame4;
        frameAreaPartecipante = new JFrame("Area Personale " + partecipante.getLogin());
        frameAreaPartecipante.setContentPane(panel);
        frameAreaPartecipante.pack();
        frameAreaPartecipante.setSize(600, 600);
        frameAreaPartecipante.setLocationRelativeTo(null);

        messaggio.setVisible(false);

        avviso.setText("Iscrizione avvenuta con successo!");
        benvenuto.setText("Benvenuto, " + partecipante.getLogin());

        ArrayList<Team> teams = evento.getTeams();
        for (Team team : teams) {
            comboBox1.addItem(team);
        }

        boolean inTeam = false;
        Team teamUtente = null;
        for (Team team : evento.getTeams()) {
            if (team.getPartecipanti().contains(partecipante)) {
                inTeam = true;
                teamUtente = team;
                break;
            }
        }
        if(!inTeam){
            String[] opzioni = {"Singolo", "Team"};
            int scelta = JOptionPane.showOptionDialog(frameAreaPartecipante, "Vuoi partecipare come singolo o in team? La scelta è definitiva", "Modalità partecipazione", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opzioni, opzioni[0]);
            if (scelta == 0) { //singolo
                uniscitiButton.setVisible(false);
                comboBox1.setVisible(false);
                teamLabel.setVisible(false);
                avviso.setVisible(false);
                messaggio.setText("Hai scelto di partecipare come singolo");
                messaggio.setVisible(true);
                problema.setVisible(true);
                caricaDocumento.setVisible(true);
                inserisciDocumento.setVisible(true);
                nomedocFied.setVisible(true);
            } else if (scelta == 1){ // team
                uniscitiButton.setVisible(true);
                comboBox1.setVisible(true);
                teamLabel.setVisible(true);
                avviso.setVisible(true);
                messaggio.setVisible(false);
                problema.setVisible(false);
                caricaDocumento.setVisible(false);
                inserisciDocumento.setVisible(false);
                nomedocFied.setVisible(false);
            }
        } else{ //già in un team
            uniscitiButton.setVisible(false);
            comboBox1.setVisible(false);
            teamLabel.setVisible(false);
            avviso.setVisible(false);
            messaggio.setText("Ora sei un membro del "+ teamUtente.getNomeTeam());
            messaggio.setVisible(true);
            problema.setVisible(true);
            caricaDocumento.setVisible(true);
            inserisciDocumento.setVisible(true);
            nomedocFied.setVisible(true);
        }

        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewEvento gui = new ViewEvento(controller, frameAccedi, frameAreaPartecipante, frameNotifica, frameGiudice);
                gui.frameAreaPartecipante.setVisible(false);
                frameEventi.setVisible(true);

            }
        });
        uniscitiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Team teamSelected = (Team) comboBox1.getSelectedItem();

                for (Team team : teams) {
                    if (team.getPartecipanti().contains(partecipante)) {
                        JOptionPane.showMessageDialog(frameAreaPartecipante, "Sei già in un team.");
                        return;
                    }
                }
                if (teamSelected.getPartecipanti().size() >= evento.getDim_max_team()) {
                    JOptionPane.showMessageDialog(frameAreaPartecipante, "Il team "+ teamSelected.getNomeTeam() + " è pieno.");
                    return;
                }
                teamSelected.getPartecipanti().add(partecipante);
                JOptionPane.showMessageDialog(frameAreaPartecipante, "Ti sei unito al team "+ teamSelected.getNomeTeam());

                comboBox1.setVisible(false);
                teamLabel.setVisible(false);
                uniscitiButton.setVisible(false);
                messaggio.setVisible(true);
                messaggio.setText("Ora sei un membro del " + teamSelected.getNomeTeam());
                problema.setVisible(true);
                caricaDocumento.setVisible(true);
                inserisciDocumento.setVisible(true);
                nomedocFied.setVisible(true);
            }
        });
        controller.stampaUtentiRegistrati();
    }

}
