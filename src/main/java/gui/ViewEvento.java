package gui;

import controller.Controller;
import model.Evento;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ViewEvento {
    private JPanel ViewEvento;
    private JPanel panelEventi;
    private Controller controller;

    public static JFrame frameEventi;

    public ViewEvento() {
        ArrayList<Evento> eventi = Controller.getEventiDisponibili();

        panelEventi.setLayout(new BoxLayout(panelEventi, BoxLayout.Y_AXIS));

        for (Evento e : eventi) {
            JPanel eventoPanel = new JPanel(new GridLayout(0,1));
            eventoPanel.setBorder(BorderFactory.createTitledBorder(e.getTitolo()));

            eventoPanel.add(new JLabel("Sede: " + e.getSede()));
            eventoPanel.add(new JLabel("Data Inizio: " + e.getDataInizio()));
            eventoPanel.add(new JLabel("Data Fine: " + e.getDataFine()));
            eventoPanel.add(new JLabel("Data apertura registrazione: " +e.getInizio_registrazioni()));
            eventoPanel.add(new JLabel("Data fine registrazione: " +e.getFine_registrazioni()));
            eventoPanel.add(new JLabel("Organizzatore: " + e.getOrganizzatore()));

            panelEventi.add(eventoPanel);
        }
    }

    public static void main(String[] args) {
        frameEventi = new JFrame("ViewEvento");
        ViewEvento eventiForm = new ViewEvento();
        frameEventi.setContentPane(eventiForm.ViewEvento);
        frameEventi.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frameEventi.pack();
        frameEventi.setVisible(true);
    }
}
