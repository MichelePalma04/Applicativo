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

    public static JFrame frameEventi, frameAccedi;

    public ViewEvento(Controller controller, JFrame frame) {

        frameAccedi = frame;
        this.controller = controller;
        Controller.initEventi();
        ArrayList<Evento> eventi = Controller.getEventiDisponibili();
        System.out.println(eventi.size());

        frameEventi = new JFrame("Eventi");
        ViewEvento eventiForm = new ViewEvento(controller, frame);
        frameEventi.setContentPane(eventiForm.ViewEvento);
        frameEventi.pack();

        panelEventi.setLayout(new BoxLayout(panelEventi, BoxLayout.Y_AXIS));

        for (Evento e : eventi) {
            JPanel eventoPanel = new JPanel(new GridLayout(0,1));
            eventoPanel.setBorder(BorderFactory.createTitledBorder(e.getTitolo()));

            eventoPanel.add(new JLabel("Sede: " + e.getSede()));
            eventoPanel.add(new JLabel("Data Inizio: " + e.getDataInizio()));
            eventoPanel.add(new JLabel("Data Fine: " + e.getDataFine()));
            eventoPanel.add(new JLabel("Data apertura registrazione: " +e.getInizio_registrazioni()));
            eventoPanel.add(new JLabel("Data fine registrazione: " +e.getFine_registrazioni()));
            eventoPanel.add(new JLabel("Organizzatore: " + e.getOrganizzatore().getLogin()));

            panelEventi.add(eventoPanel);
        }

        panelEventi.revalidate();
        panelEventi.repaint();
    }

}
