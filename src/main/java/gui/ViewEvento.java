package gui;

import controller.Controller;
import model.Evento;
import model.Partecipante;
import model.Utente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ViewEvento {
    private JPanel ViewEvento;
    private JPanel panelEventi;
    private JScrollPane scroll;
    private JButton logOutButton1;
    private JButton visualizzaButton;
    private Controller controller;


    public static JFrame frameEventi, frameAccedi, frameAreaPartecipante, frameNotifiche;

    public ViewEvento(Controller controller,JFrame frame, JFrame frame2, JFrame frame3) {
        scroll.getVerticalScrollBar().setUnitIncrement(20); //abbiamo aumentato la sensibilità dello scroll
        frameAccedi = frame;
        this.controller = controller;
        frameAreaPartecipante = frame2;
        frameNotifiche = frame3;
        Controller.initEventi();
        ArrayList<Evento> eventi = Controller.getEventiDisponibili();

        frameEventi = new JFrame("Eventi");
        frameEventi.setContentPane(ViewEvento);
        frameEventi.pack();
        frameEventi.setSize(500,500);
        frameEventi.setLocationRelativeTo(null);

        panelEventi.setLayout(new BoxLayout(panelEventi, BoxLayout.Y_AXIS));

        for (Evento ev : eventi) {
            JPanel eventoPanel = new JPanel(new GridLayout(0,1));
            eventoPanel.setBorder(BorderFactory.createTitledBorder(ev.getTitolo()));

            eventoPanel.add(new JLabel("Sede: " + ev.getSede()));
            eventoPanel.add(new JLabel("Data Inizio: " + ev.getDataInizio()));
            eventoPanel.add(new JLabel("Data Fine: " + ev.getDataFine()));
            eventoPanel.add(new JLabel("Data apertura registrazione: " +ev.getInizio_registrazioni()));
            eventoPanel.add(new JLabel("Data fine registrazione: " +ev.getFine_registrazioni()));
            eventoPanel.add(new JLabel("Organizzatore: " + ev.getOrganizzatore().getLogin()));
            JButton iscrivitiButton = new JButton("Iscriviti");
            eventoPanel.add(iscrivitiButton);
            JButton visualizzaArea = new JButton("Visualizza info");
            eventoPanel.add(visualizzaArea);
            visualizzaArea.setVisible(false);
            eventoPanel.add(visualizzaArea);

            Utente u = Controller.getUtenteCorrente();
            Partecipante partecipante = Controller.getPartecipantCorrente();

            if(partecipante!= null && partecipante.getEventi().contains(ev)) {
                //JOptionPane.showMessageDialog(frameEventi, "Sei già iscritto a questo evento.");
                //return;
                iscrivitiButton.setVisible(true);
                visualizzaArea.setVisible(false);
            } else{
                iscrivitiButton.setVisible(true);
                visualizzaArea.setVisible(false);
            }

            iscrivitiButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Partecipante p = Controller.getPartecipantCorrente();
                    if(p!= null && p.getEventi().contains(ev)) {
                        JOptionPane.showMessageDialog(frameEventi, "Sei già iscritto a questo evento.");
                        return;
                    }
                    if (p == null) {
                        p=new Partecipante(u.getLogin(), u.getPassword(), null, new ArrayList<>());
                        Controller.setPartecipantCorrente(p);
                    }

                    //Aggiunge evento alla sua liste e viceversa
                    p.getEventi().add(ev);
                    if(!ev.getPartecipanti().contains(p)) {
                        ev.getPartecipanti().add(p);
                    }

                    Controller.stampaPartecipantiEvento(ev);

                    JOptionPane.showMessageDialog(frameEventi,"Iscrizione completata con successo!");
                    iscrivitiButton.setVisible(false);
                    visualizzaArea.setVisible(true);
                    AreaPartecipante quintaGUI = new AreaPartecipante(p, ev, frameEventi);
                    quintaGUI.frameAreaPartecipante.setVisible(true);
                    frameEventi.setVisible(false);
                }

            });
            panelEventi.add(eventoPanel);
            visualizzaArea.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Partecipante p = Controller.getPartecipantCorrente();
                    AreaPartecipante areaGUI = new AreaPartecipante(p, ev, frameEventi);
                    areaGUI.frameAreaPartecipante.setVisible(true);
                    frameEventi.setVisible(false);
                }
            });

        }

        panelEventi.revalidate();
        panelEventi.repaint();



        logOutButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Controller.setPartecipantCorrente(null);
                Controller.setUtenteCorrente(null);
                frameEventi.setVisible(false);
                frameAccedi.setVisible(true);
            }
        });
        visualizzaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Utente utente = Controller.getUtenteCorrente();
                ViewNotificheInviti notifiche = new ViewNotificheInviti(controller, utente);
                notifiche.frameNotifiche.setVisible(true);
                frameEventi.setVisible(false);
            }
        });
    }

}
