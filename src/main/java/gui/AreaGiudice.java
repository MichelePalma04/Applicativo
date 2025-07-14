package gui;

import controller.Controller;
import model.Evento;
import model.Giudice;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AreaGiudice {
    private JPanel mainPanel;
    private JButton logOutButton;
    private JLabel benvenuto;
    private JScrollPane scroll;
    private JPanel panel;
    public JFrame frameGiudice;
    public JFrame frameAccesso;

    private Controller controller;

    public AreaGiudice(Controller controller, Giudice giudice, JFrame frame) {
        this.controller = controller;
        frameAccesso = frame;

        benvenuto.setText("Benvenuto giudice "+ giudice.getLogin());
        frameGiudice = new JFrame();
        frameGiudice.setTitle("Area giudice "+ giudice.getLogin());
        frameGiudice.setContentPane(mainPanel);
        frameGiudice.pack();
        frameGiudice.setSize(500,500);
        frameGiudice.setLocationRelativeTo(null);

        scroll.getVerticalScrollBar().setUnitIncrement(20);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        for (Evento evento: giudice.getEventi()){
            JPanel eventoPanel = new JPanel(new BorderLayout());
            JLabel eventoLabel = new JLabel(evento.getTitolo());
            JButton dettagliButton = new JButton("Dettagli");

            eventoPanel.add(eventoLabel);
            eventoPanel.add(dettagliButton, BorderLayout.SOUTH);
            panel.add(eventoPanel);
        }


        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameGiudice.dispose();
                frameAccesso.setVisible(true);
            }
        });
    }
}
