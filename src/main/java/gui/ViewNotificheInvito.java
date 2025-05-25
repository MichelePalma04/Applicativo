package gui;

import controller.Controller;
import model.Evento;
import model.Utente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ViewNotificheInvito {
    private Controller controller;
    public static JFrame frameNotifiche, frameEventi;
    private JPanel panel;
    private JButton backButton;
    private Utente utente;

    public ViewNotificheInvito(Controller controller, Utente utente, JFrame frame) {
        this.controller = controller;
        this.utente = utente;
        frameEventi = frame;
        JPanel panelEventi = new JPanel();

        frameNotifiche = new JFrame("Inviti ricevuti");
        frameNotifiche.setContentPane(panelEventi);
        frameNotifiche.pack();
        frameNotifiche.setSize(500, 500);
        frameNotifiche.setLocationRelativeTo(null);

        aggiornaListaInviti();

        panelEventi.setLayout(new BoxLayout(panelEventi, BoxLayout.Y_AXIS));

        private void aggiornaListaInviti(){
            panelEventi.removeAll();
            ArrayList<Evento> inviti = controller.getInvitiUtente(utente);
            if (inviti.isEmpty()) {
                panelEventi.add(new JLabel("Nessun invito ricevuto."));
            } else{
                    for (Evento evento : inviti) {
                        JPanel riga = new JPanel(new FlowLayout(FlowLayout.LEFT));
                        riga.add(new JLabel("Sei stato invitato da "+ evento.getOrganizzatore().getLogin()+" per l'evento "+ evento.getTitolo()));
                        JButton accettabutton = new JButton("Accetta");
                        JButton rifiutabutton = new JButton("Rifiuta");
                        riga.add(accettabutton);
                        riga.add(rifiutabutton);

                        accettabutton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                if(Controller.accettaInvitoGiudice(evento, utente)){
                                    JOptionPane.showMessageDialog(frameNotifiche, "Ora sei giudice dell'evento: "+ evento.getTitolo());
                                    accettabutton.setEnabled(false);
                                    rifiutabutton.setEnabled(false);
                                }
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



                accettabutton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(Controller.accettaInvitoGiudice(evento, utente)){
                        JOptionPane.showMessageDialog(frameNotifiche, "Ora sei giudice dell'evento: "+ evento.getTitolo());
                        accettabutton.setEnabled(false);
                        rifiutabutton.setEnabled(false);
                        }
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

        backButton = new JButton ("<- back");
        JPanel panelBack = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBack.add(backButton);
        panelEventi.add(panelBack, BorderLayout.SOUTH);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameNotifiche.setVisible(false);
                frameEventi.setVisible(true);
            }
        });


    }
}
