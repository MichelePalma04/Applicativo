package gui;

import controller.Controller;
import model.Evento;
import model.Giudice;
import model.Utente;

import javax.swing.*;
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
    protected JFrame frameNotifiche;
    private JFrame frameEventi;
    private JFrame frameGiudice;
    private JFrame frameAccesso;
    protected JFrame frameAreaPartecipante;
    private Evento evento;

    public VediNotifica(Controller controller, Utente utente, JFrame frame, JFrame frame2, JFrame frame3, JFrame frame4) {
        this.controller = controller;
        this.utente = utente;
        frameEventi = frame;
        frameGiudice = frame2;
        frameAccesso = frame3;
        frameAreaPartecipante = frame4;

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
                Utente u = controller.getUtenteCorrente();
                for(Evento ev : controller.getEventiDisponibili()) {
                    if (controller.isUtenteGiudice(ev, u) && u instanceof Giudice) {
                        AreaGiudice GUI = new AreaGiudice(controller, (Giudice) u, frameAccesso);
                        GUI.frameGiudice.setVisible(true);
                        frameNotifiche.setVisible(false);
                        return;
                    }
                }
                frameNotifiche.dispose();
                ViewEvento nuovo = new ViewEvento(controller, frameAccesso, frameAreaPartecipante, frameNotifiche, frameGiudice);
                frameEventi.setVisible(true);
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
