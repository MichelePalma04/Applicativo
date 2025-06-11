package gui;

import controller.Controller;
import model.Evento;
import model.Organizzatore;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AreaOrganizzatore {
    private JPanel panel;
    private JLabel benvenuto;
    private JPanel panelEventi;
    private JScrollPane scroll;
    private JButton logOutButton;
    protected JFrame frameOrganizzatore;
    private JFrame frameAccessi;
    protected JFrame frameInviti;
    private Controller controller;


    public AreaOrganizzatore(Controller controller, Organizzatore organizzatore, JFrame frame, JFrame frame2) {
        this.controller = controller;
        frameInviti = frame;
        frameAccessi = frame2;
        benvenuto.setText("Benvenuto organizzatore "+ organizzatore.getLogin());
        frameOrganizzatore = new JFrame("Area Organizzatore " + organizzatore.getLogin());
        frameOrganizzatore.setContentPane(panel);
        frameOrganizzatore.pack();
        frameOrganizzatore.setSize(500, 500);
        frameOrganizzatore.setLocationRelativeTo(null);
        frameOrganizzatore.setVisible(true);

        scroll.getVerticalScrollBar().setUnitIncrement(20);
        panelEventi.setLayout(new BoxLayout(panelEventi, BoxLayout.Y_AXIS));

        for (Evento evento: organizzatore.getEventi()){
            JPanel eventoBox = new JPanel(new BorderLayout());
            JLabel eventoLabel = new JLabel(evento.getTitolo() + " " + evento.getSede());
            JButton infoButton = new JButton("Dettagli");

            infoButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Invito nuovaGUI = new Invito (evento, frameOrganizzatore, controller);
                    nuovaGUI.frameInvito.setVisible(true);
                    frameOrganizzatore.setVisible(false);
                }
            });

            eventoBox.add(eventoLabel);
            eventoBox.add(infoButton, BorderLayout.SOUTH);
            eventoBox.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            panelEventi.add(eventoBox);
        }

        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameOrganizzatore.setVisible(false);
                frameAccessi.setVisible(true);
            }
        });
    }
}
