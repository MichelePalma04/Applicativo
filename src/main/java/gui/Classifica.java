package gui;

import controller.Controller;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;


public class Classifica {
    private JPanel mainPanel;
    private JLabel ingresso;
    private JScrollPane scroll;
    private JPanel panelVoti;
    private JButton backButton;
    private Controller controller;
    public JFrame frameClassifica;
    private JFrame frameEventi;
    private int eventoID;

    public Classifica(int eventoId, Controller controller, JFrame frameE) {
        this.controller = controller;
        this.frameEventi = frameE;
        frameClassifica = new JFrame("Classifica");
        frameClassifica.setContentPane(mainPanel);
        frameClassifica.pack();
        frameClassifica.setSize(500, 500);
        frameClassifica.setLocationRelativeTo(null);
        Evento evento = controller.getEventoById(eventoId);
        ingresso.setText("Classifica finale di "+ evento.getTitolo());
        scroll.getVerticalScrollBar().setUnitIncrement(20);

        panelVoti.setLayout(new BoxLayout(panelVoti, BoxLayout.Y_AXIS));

        List <Team> teams = controller.getTeamsEvento(eventoId);
        teams.sort((t1, t2) -> Double.compare(t2.mediaVoti(), t1.mediaVoti()));
        int posizione = 1;
        for(Team t: teams) {
            double media = t.mediaVoti();
            JPanel riga = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel posizioneTeam = new JLabel(posizione++ +") ");
            JLabel nomeTeam = new JLabel(" "+ t.getNomeTeam() +":");
            JLabel mediaTeam = new JLabel(" "+ String.format("%.2f", media));
            riga.add(posizioneTeam);
            riga.add(nomeTeam);
            riga.add(mediaTeam);
            panelVoti.add(riga);
        }

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameClassifica.dispose();
                frameE.setVisible(true);
            }
        });
    }
}
