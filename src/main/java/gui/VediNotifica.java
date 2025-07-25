package gui;

import controller.Controller;
import model.Evento;

import model.InvitoGiudice;
import model.Utente;

import javax.swing.*;
import java.awt.*;

import java.util.List;

public class VediNotifica {

    private JPanel mainPanel;
    private JPanel panelInviti;
    private JPanel panelbottone;
    private JButton backButton;
    private String loginUtente;
    private JScrollPane scroll;
    private Controller controller;
    private JFrame frameNotifiche;
    private JFrame frameEventi;
    private JFrame frameGiudice;
    private JFrame frameAccesso;
    private JFrame framePartecipante;

    public VediNotifica(Controller controller, String loginUtente, JFrame frameAreaEvento, JFrame frameAreaGiudice, JFrame frameAreaAccesso, JFrame frameAreaPartecipante) {
        this.controller = controller;
        this.loginUtente = loginUtente;
        frameEventi = frameAreaEvento;
        frameGiudice = frameAreaGiudice;
        frameAccesso = frameAreaAccesso;
        framePartecipante = frameAreaPartecipante;

        scroll.getVerticalScrollBar().setUnitIncrement(20);

        frameNotifiche = new JFrame("Inviti ricevuti");
        frameNotifiche.setContentPane(mainPanel);
        frameNotifiche.pack();
        frameNotifiche.setSize(500, 500);
        frameNotifiche.setLocationRelativeTo(null);

        panelInviti.setLayout(new BoxLayout(panelInviti, BoxLayout.Y_AXIS));
        aggiornaInviti();

        backButton.addActionListener(e -> {
            frameNotifiche.setVisible(false);
            frameEventi.dispose();
            ViewEvento nuovo = new ViewEvento(controller, loginUtente, frameAccesso, framePartecipante, frameNotifiche, frameGiudice);
            nuovo.getFrameEventi().setVisible(true);
        });
    }
    private void aggiornaInviti() {
        Utente utente = controller.getUtenteDaDB(loginUtente);
        panelInviti.removeAll();

        List<InvitoGiudice> inviti = controller.getInvitiPendentiUtente(loginUtente);

        if (inviti.isEmpty()) {
            panelInviti.add(new JLabel("Nessun invito ricevuto"));
        } else {
            for (InvitoGiudice invito : inviti) {
                Evento evento = invito.getEvento();
                JPanel riga = new JPanel(new FlowLayout(FlowLayout.LEFT));
                riga.add(new JLabel("Sei stato invitato da " + evento.getOrganizzatore().getLogin() + " per l'evento " + evento.getTitolo()));
                JButton accettabutton = new JButton("Accetta");
                JButton rifiutabutton = new JButton("Rifiuta");
                riga.add(accettabutton);
                riga.add(rifiutabutton);
                panelInviti.add(riga);


                accettabutton.addActionListener(e -> {
                    if(controller.accettaInvitoGiudice(invito.getId(), loginUtente)) {
                        JOptionPane.showMessageDialog(frameNotifiche, "Ora sei giudice dell'evento: " + evento.getTitolo());
                        aggiornaInviti();
                    }
                    aggiornaInviti();
                });

                rifiutabutton.addActionListener(e ->{
                    controller.rifiutaInvitoGiudice(invito.getId());
                    aggiornaInviti();
                });

            }
        }
        panelInviti.revalidate();
        panelInviti.repaint();
    }
    public JFrame getFrameNotifiche() {
        return frameNotifiche;
    }
}
