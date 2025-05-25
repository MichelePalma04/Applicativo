package gui;

import controller.Controller;
import model.Evento;
import model.Utente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ViewNotificheInviti {
    private Controller controller;
    public static JFrame frameNotifiche;

    public ViewNotificheInviti(Controller controller, Utente utente) {
        this.controller = controller;
        ArrayList<Evento> inviti = controller.getInvitiUtente(utente);
        JPanel panel = new JPanel();

        frameNotifiche = new JFrame("Inviti ricevuti");
        frameNotifiche.setContentPane(panel);
        frameNotifiche.pack();
        frameNotifiche.setSize(500, 500);
        frameNotifiche.setLocationRelativeTo(null);

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        if (inviti.isEmpty()) {
            panel.add(new JLabel("Nessun invito ricevuto."));
        } else{
            for (Evento evento : inviti) {
                //JPanel panelInviti = new JPanel();
                //panelInviti.setLayout(new BoxLayout(panelInviti, BoxLayout.Y_AXIS));
                JPanel riga = new JPanel(new FlowLayout(FlowLayout.LEFT));
                riga.add(new JLabel("Sei stato invitato da "+ evento.getOrganizzatore().getLogin()+"per l'evento "+ evento.getTitolo()));
                JButton accettabutton = new JButton("Accetta");
                JButton rifiutabutton = new JButton("Rifiuta");
                riga.add(accettabutton);
                riga.add(rifiutabutton);

                panel.add(riga);

                accettabutton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Controller.accettaInvitoGiudice(evento, utente);
                        JOptionPane.showMessageDialog(frameNotifiche, "Ora sei giudice dell'evento: "+ evento.getTitolo());
                        accettabutton.setEnabled(false);
                        rifiutabutton.setEnabled(false);
                    }
                });
                rifiutabutton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Controller.rifiutaInvitoGiudice(evento, utente);
                        accettabutton.setEnabled(false);
                        rifiutabutton.setEnabled(false);
                    }
                });


            }
        }

    }
}
