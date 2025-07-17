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
    private JButton back;
    public JFrame frameGiudice;
    public JFrame frameAccesso;
    public JFrame frameEventi;

    private Controller controller;

    public AreaGiudice(Controller controller, Giudice giudice, JFrame frame, JFrame eventi, Evento eventoCorrente ) {
        this.controller = controller;
        frameAccesso = frame;
        frameEventi = eventi;

        benvenuto.setText("Benvenuto "+ giudice.getLogin());
        frameGiudice = new JFrame();
        frameGiudice.setTitle("Area giudice "+ giudice.getLogin());
        frameGiudice.setContentPane(mainPanel);
        frameGiudice.pack();
        frameGiudice.setSize(500,500);
        frameGiudice.setLocationRelativeTo(null);

        scroll.getVerticalScrollBar().setUnitIncrement(20);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        if(eventoCorrente.getGiudiceDescrizione() != null && eventoCorrente.getGiudiceDescrizione().equals(giudice)) {
            JLabel messaggio = new JLabel("Sei il giudice responsabile della descrizione del problema.");
            if (eventoCorrente.getProblema() == null || eventoCorrente.getProblema().isEmpty()) {
                JTextArea descrizioneProblema = new JTextArea(5, 20);
                JScrollPane scrollProblema = new JScrollPane(descrizioneProblema);
                JButton carica = new JButton("Carica problema");

                panel.add(messaggio);
                panel.add(scrollProblema);
                panel.add(carica);

                carica.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String testo = descrizioneProblema.getText();
                        if (testo.isEmpty()) {
                            JOptionPane.showMessageDialog(frameGiudice, "Inserisci la descrizione del problema prima di effettuare il caricamento!", "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            eventoCorrente.setProblema(testo);
                            JOptionPane.showMessageDialog(frameGiudice, "Descrizione del problema caricata con successo!");
                            panel.removeAll();
                            JLabel giaCaricata = new JLabel("Problema:\n " + eventoCorrente.getProblema());
                            panel.add(messaggio);
                            panel.add(giaCaricata);
                            panel.revalidate();
                            panel.repaint();
                        }
                    }
                });
            } else {
                JLabel giaCaricata = new JLabel("Problema:\n " + eventoCorrente.getProblema());
                panel.add(giaCaricata);
            }
        }

        if(eventoCorrente.getProblema() != null && !eventoCorrente.getProblema().isEmpty() && !giudice.equals(eventoCorrente.getGiudiceDescrizione())) {
            JLabel message = new JLabel("Problema assegnato per l'evento: \n" + eventoCorrente.getProblema());
            JScrollPane scrollProblema = new JScrollPane(message);

            panel.add(message);
            panel.add(scrollProblema);
        }


        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameGiudice.dispose();
                frameAccesso.setVisible(true);
            }
        });
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameGiudice.dispose();
                frameEventi.setVisible(true);
            }
        });
    }
}
