package gui;

import controller.Controller;
import model.Evento;
import model.Organizzatore;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Area;

public class AreaOrganizzatore {
    private JPanel panel;
    private JLabel benvenuto;
    private JPanel panelEventi;
    private JScrollPane scroll;
    public static JFrame frameOrganizzatore, frameAccessi, frameInviti;
    private Controller controller;


    public AreaOrganizzatore(Organizzatore organizzatore, JFrame frame){
        frameInviti = frame;
        benvenuto.setText("Benvenuto organizzatore "+ organizzatore.getLogin());
        frameOrganizzatore = new JFrame("Area Organizzatore " + organizzatore.getLogin());
        frameOrganizzatore.setContentPane(panel);
        frameOrganizzatore.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
                    Invito nuovaGUI = new Invito (evento);
                    nuovaGUI.frameInvito.setVisible(true);
                    frameOrganizzatore.setVisible(false);
                }
            });

            eventoBox.add(eventoLabel);
            eventoBox.add(infoButton, BorderLayout.SOUTH);
            eventoBox.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            panelEventi.add(eventoBox);
        }

    }


}
