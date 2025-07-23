package gui;

import controller.Controller;
import model.Evento;
import model.Giudice;
import model.InvitoGiudice;
import model.Utente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class VediNotifica {

    private JPanel mainPanel;
    private JPanel panelInviti;
    private JPanel panelbottone;
    private JButton backButton;
    private String loginUtente;
    private JScrollPane scroll;
    private Controller controller;
    public JFrame frameNotifiche;
    public JFrame frameEventi;
    public JFrame frameGiudice;
    public JFrame frameAccesso;
    public JFrame frameAreaPartecipante;
    private Evento evento;

    public VediNotifica(Controller controller, String loginUtente, JFrame frame, JFrame frame2, JFrame frame3, JFrame frame4) {
        this.controller = controller;
        this.loginUtente = loginUtente;
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
                frameNotifiche.setVisible(false);
                frameEventi.dispose();
                ViewEvento nuovo = new ViewEvento(controller, loginUtente, frameAccesso, frameAreaPartecipante, frameNotifiche, frameGiudice);
                nuovo.frameEventi.setVisible(true);
            }
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


                accettabutton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(controller.accettaInvitoGiudice(invito, utente)){
                            JOptionPane.showMessageDialog(frameNotifiche, "Ora sei giudice dell'evento: " + evento.getTitolo());
                            aggiornaInviti();
                        }
                        aggiornaInviti();
                    }
                });

                rifiutabutton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        controller.rifiutaInvitoGiudice(invito, utente);
                        aggiornaInviti();
                    }
                });

            }
        }
        panelInviti.revalidate();
        panelInviti.repaint();
    }
}
