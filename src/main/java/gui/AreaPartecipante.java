package gui;

import controller.Controller;
import model.Documento;
import model.Evento;
import model.Partecipante;
import model.Team;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AreaPartecipante {
    private JLabel avviso;
    private JButton homeButton;
    private JComboBox <Team> comboBox1;
    private JButton uniscitiButton;
    private JLabel benvenuto;
    private String loginPartecipante;
    private int eventoId;
    private JLabel teamLabel;
    private JPanel panel;
    private JLabel messaggio;
    private JButton sfogliaDocumenti;
    private JLabel inserisciDocumento;
    private JPanel panel2;
    private JLabel problema;
    private JButton creaTeamButton;
    private JTextField nomeField;
    private JLabel nomeTeam;
    private Controller controller;
    public JFrame frameAreaPartecipante;
    public JFrame frameEventi;
    public JFrame frameAccedi;
    public JFrame frameNotifica;
    public JFrame frameGiudice;


    public AreaPartecipante(String loginPartecipante, int eventoId, JFrame frame, JFrame frame2, JFrame frame3, JFrame frame4, Controller controller) {
        this.controller = controller;
        this.loginPartecipante = loginPartecipante;
        this.eventoId = eventoId;
        frameEventi = frame;
        frameAccedi = frame2;
        frameNotifica = frame3;
        frameGiudice = frame4;

        Partecipante partecipante = controller.getPartecipanteDaDB(loginPartecipante, eventoId);
        Evento evento = controller.geteventoById(eventoId);
        frameAreaPartecipante = new JFrame("Area Personale " + loginPartecipante);
        frameAreaPartecipante.setContentPane(panel);
        frameAreaPartecipante.pack();
        frameAreaPartecipante.setSize(600, 600);
        frameAreaPartecipante.setLocationRelativeTo(null);

        messaggio.setVisible(false);

        avviso.setText("Iscrizione avvenuta con successo!");
        benvenuto.setText("Benvenuto, " + loginPartecipante);



        List<Team> teams = controller.getTeamsEvento(eventoId);
        if (teams.isEmpty()) {
            uniscitiButton.setEnabled(false);
            comboBox1.setEnabled(false);
            teamLabel.setText("Nessun team disponibile a cui unirsi.");
        } else {
            for (Team team : teams) {
                comboBox1.addItem(team);
            }
        }

       // boolean inTeam = false;
        // Verifica se il partecipante è già in un team
        Team teamUtente = null;
        for (Team team : teams) {
            if (controller.isPartecipanteInTeam(loginPartecipante, team.getNomeTeam(), eventoId)) {
                //inTeam = true;
                teamUtente = team;
                break;
            }
        }

        boolean inTeam = (teamUtente != null);
        creaTeamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = nomeField.getText();
                if(nome.isEmpty()) {
                    JOptionPane.showMessageDialog(frameAreaPartecipante, "Inserire un nome valido.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // Crea il team nel DB e unisci subito il partecipante
                Team nuovoTeam = controller.creaTeam(nome, loginPartecipante, eventoId);
               // evento.getTeams().add(nuovoTeam);
                comboBox1.addItem(nuovoTeam);

                nomeField.setVisible(false);
                nomeTeam.setVisible(false);
                creaTeamButton.setVisible(false);
                teamLabel.setVisible(false);
                comboBox1.setVisible(false);
                uniscitiButton.setVisible(false);
                avviso.setVisible(false);
                messaggio.setVisible(true);
                messaggio.setText("Ora sei un membro del team "+ nuovoTeam.getNomeTeam());
                if(evento.getProblema()!= null && !evento.getProblema().isEmpty()){
                    problema.setText("Problema da risolvere: " + evento.getProblema());
                    problema.setVisible(true);
                    sfogliaDocumenti.setVisible(true);
                    inserisciDocumento.setVisible(true);
                }else{
                    problema.setVisible(false);
                    sfogliaDocumenti.setVisible(false);
                    inserisciDocumento.setVisible(false);
                }
            }
        });

        if(inTeam) {
            creaTeamButton.setVisible(false);
            nomeField.setVisible(false);
            nomeTeam.setVisible(false);
            uniscitiButton.setVisible(false);
            comboBox1.setVisible(false);
            teamLabel.setVisible(false);
            avviso.setVisible(false);
            messaggio.setVisible(true);
            messaggio.setText("Ora sei membro del " + teamUtente.getNomeTeam());
            if(evento.getProblema()!= null && !evento.getProblema().isEmpty()){
                problema.setText("Problema da risolvere: " + evento.getProblema());
                problema.setVisible(true);
                sfogliaDocumenti.setVisible(true);
                inserisciDocumento.setVisible(true);
            }else{
                problema.setVisible(false);
                sfogliaDocumenti.setVisible(false);
                inserisciDocumento.setVisible(false);
            }
        }else{
            uniscitiButton.setVisible(true);
            comboBox1.setVisible(true);
            teamLabel.setVisible(true);
            avviso.setVisible(true);
            messaggio.setVisible(false);
            problema.setVisible(false);
            sfogliaDocumenti.setVisible(false);
            inserisciDocumento.setVisible(false);
        }


        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewEvento gui = new ViewEvento(controller, loginPartecipante, frameAccedi, frameAreaPartecipante, frameNotifica, frameGiudice);
                gui.frameAreaPartecipante.setVisible(false);
                frameEventi.setVisible(true);

            }
        });
        uniscitiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Team teamSelected = (Team) comboBox1.getSelectedItem();
/*
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

 */
                // Verifica se già in un team tramite Controller
                if (controller.isPartecipanteInTeam(loginPartecipante, teamSelected.getNomeTeam(), eventoId)) {
                    JOptionPane.showMessageDialog(frameAreaPartecipante, "Sei già in un team.");
                    return;
                }

                // Verifica la dimensione massima tramite Controller
                if (controller.getDimTeam(teamSelected.getNomeTeam(), eventoId) >= evento.getDim_max_team()) {
                    JOptionPane.showMessageDialog(frameAreaPartecipante, "Il team " + teamSelected.getNomeTeam() + " è pieno.");
                    return;
                }

                // Unisci il partecipante al team tramite Controller
                controller.unisciPartecipanteATeam(loginPartecipante, teamSelected.getNomeTeam(), eventoId);
                creaTeamButton.setVisible(false);
                nomeField.setVisible(false);
                nomeTeam.setVisible(false);
                comboBox1.setVisible(false);
                teamLabel.setVisible(false);
                uniscitiButton.setVisible(false);
                avviso.setVisible(false);
                messaggio.setVisible(true);
                messaggio.setText("Ora sei un membro del " + teamSelected.getNomeTeam());
                if(evento.getProblema()!= null && !evento.getProblema().isEmpty()){
                    problema.setText("Problema da risolvere: " + evento.getProblema());
                    problema.setVisible(true);
                    sfogliaDocumenti.setVisible(true);
                    inserisciDocumento.setVisible(true);
                }else{
                    problema.setVisible(false);
                    sfogliaDocumenti.setVisible(false);
                    inserisciDocumento.setVisible(false);
                }
            }
        });
        controller.stampaUtentiRegistrati();


        sfogliaDocumenti.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int risultato = fileChooser.showOpenDialog(frameAreaPartecipante);
                if (risultato == JFileChooser.APPROVE_OPTION) {
                    java.io.File file = fileChooser.getSelectedFile();

                    //Trova il team dell'utente tramite controller
                    Team teamUser = null;
                    List<Team> teams = controller.getTeamsEvento(eventoId);
                    for (Team t: teams) {
                        if (controller.isPartecipanteInTeam(loginPartecipante, t.getNomeTeam(), eventoId)) {
                            teamUser = t;
                            break;
                        }
                    }
                    Documento documento = new Documento(LocalDate.now(), file, teamUser);
                    controller.caricaDocumento (documento, teamUser.getNomeTeam(), eventoId);
                    JOptionPane.showMessageDialog(frameAreaPartecipante, "Documento caricato con successo!");
                }
            }
        });
    }

}
