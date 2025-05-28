package gui;

import controller.Controller;
import model.Evento;
import model.Utente;

import javax.swing.*;
import javax.swing.text.View;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class VediNotifica {

    private JPanel mainPanel;
    private JPanel panelInviti;
    private JPanel panelbottone;
    private JButton backButton;
    private JScrollPane scroll;
    private Controller controller;
    private Utente utente;
    public static JFrame frameNotifiche, frameEventi;

    public VediNotifica(Controller controller, Utente utente, JFrame frame) {
        this.controller = controller;
        this.utente = utente;
        frameEventi = frame;

        scroll.getVerticalScrollBar().setUnitIncrement(20);

        frameNotifiche = new JFrame("Inviti ricevuti");
        frameNotifiche.setContentPane(mainPanel);
        frameNotifiche.pack();
        frameNotifiche.setSize(500, 500);
        frameNotifiche.setLocationRelativeTo(null);

        panelInviti.setLayout(new BoxLayout(panelInviti, BoxLayout.Y_AXIS));
        aggiornaInviti();

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameNotifiche.setVisible(false);
                frameEventi.dispose();
                ViewEvento nuovo = new ViewEvento(controller, ViewEvento.frameAccedi, ViewEvento.frameAreaPartecipante, frameNotifiche);
                ViewEvento.frameEventi.setVisible(true);
            }
        });
    }
    private void aggiornaInviti() {
        panelInviti.removeAll();

        ArrayList<Evento> inviti = controller.getInvitiUtente(utente);

        if (inviti.isEmpty()) {
            panelInviti.add(new JLabel("Nessun invito ricevuto"));
        } else {
            for (Evento evento : inviti) {
                JPanel riga = new JPanel(new FlowLayout(FlowLayout.LEFT));
                riga.add(new JLabel("Sei stato invitato da " + evento.getOrganizzatore().getLogin() + " per l'evento " + evento.getTitolo()));
                JButton accettabutton = new JButton("Accetta");
                JButton rifiutabutton = new JButton("Rifiuta");
                riga.add(accettabutton);
                riga.add(rifiutabutton);
                panelInviti.add(riga);


                accettabutton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        controller.accettaInvitoGiudice(evento, utente);
                        JOptionPane.showMessageDialog(frameNotifiche, "Ora sei giudice dell'evento: " + evento.getTitolo());
                        aggiornaInviti();
                    }
                });

                rifiutabutton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        controller.rifiutaInvitoGiudice(evento, utente);
                        aggiornaInviti();
                    }
                });

            }
        }
        panelInviti.revalidate();
        panelInviti.repaint();
    }
}
