package gui;

import controller.Controller;
import model.Evento;
import model.Utente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class VediNotifica {

    private JPanel VediNotifica;
    private JPanel panelInviti;
    private JPanel panelbottone;
    private JButton backButton;
    private Controller controller;
    private Utente utente;
    public static JFrame frameNotifiche, frameEventi;

    public VediNotifica(Controller controller, Utente utente, JFrame frame) {
        this.controller = controller;
        this.utente = utente;
        frameEventi = frame;

        ArrayList<Evento> inviti = Controller.getInvitiUtente(utente);
        frameNotifiche = new JFrame("Inviti ricevuti");
        System.out.println("vediNotifica: "+ VediNotifica);
        frameNotifiche.setContentPane(VediNotifica);
        frameNotifiche.pack();
        frameNotifiche.setSize(500, 500);
        frameNotifiche.setLocationRelativeTo(null);

        if (inviti.isEmpty()) {
            panelInviti.add(new JLabel("Nessun invito ricevuto"));
        }else{
            for (Evento evento : inviti) {
                JPanel riga = new JPanel(new GridLayout(0,1));
                riga.add(new JLabel("Sei stato invitato da " + evento.getOrganizzatore().getLogin() + " per l'evento " + evento.getTitolo()));
                JButton accettabutton = new JButton("Accetta");
                JButton rifiutabutton = new JButton("Rifiuta");
                riga.add(accettabutton);
                riga.add(rifiutabutton);


                accettabutton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Controller.accettaInvitoGiudice(evento, utente);
                        JOptionPane.showMessageDialog(frameNotifiche, "Ora sei giudice dell'evento: " + evento.getTitolo());
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
                panelInviti.add(riga);
            }
        }
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameNotifiche.setVisible(false);
                frameEventi.setVisible(true);
            }
        });
    }

}
