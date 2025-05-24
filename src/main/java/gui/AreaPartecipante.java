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
    private JLabel team;
    private JPanel panel;
    private JLabel messaggio;
    private Controller controller;
    public static JFrame frameAreaPartecipante, frameEventi;

    public AreaPartecipante(Partecipante partecipante, Evento evento, JFrame frame) {
        frameEventi = frame;
        frameAreaPartecipante = new JFrame("Area Personale " + partecipante.getLogin());
        frameAreaPartecipante.setContentPane(panel);
        frameAreaPartecipante.pack();
        frameAreaPartecipante.setSize(500, 500);
        frameAreaPartecipante.setLocationRelativeTo(null);

        messaggio.setVisible(false);

        avviso.setText("Iscrizione avvenuta con successo!");
        benvenuto.setText("Benvenuto, " + partecipante.getLogin());

        ArrayList<Team> teams = evento.getTeams();
        for (Team team : teams) {
            comboBox1.addItem(team);
        }

        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameAreaPartecipante.setVisible(false);
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
                team.setVisible(false);
                uniscitiButton.setVisible(false);
                messaggio.setVisible(true);
                messaggio.setText("Ora sei un membro del " + teamSelected.getNomeTeam());

            }
        });
    }

}
