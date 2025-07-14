package gui;

import controller.Controller;
import model.Evento;
import model.Giudice;
import model.Partecipante;
import model.Utente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ViewEvento {
    private JPanel mainPanel;
    private JPanel panelEventi;
    private JScrollPane scroll;
    private JButton logOutButton1;
    private JButton visualizzaButton;
    private JLabel notifiche;
    private Controller controller;


    public JFrame frameEventi;
    public JFrame frameAccedi;
    public JFrame frameAreaPartecipante;
    public JFrame frameNotifiche;
    public JFrame frameGiudice;

    public ViewEvento(Controller controller,JFrame frame, JFrame frame2, JFrame frame3, JFrame frame4) {
        scroll.getVerticalScrollBar().setUnitIncrement(20); //abbiamo aumentato la sensibilità dello scroll
        frameAccedi = frame;
        frameGiudice = frame4;
        this.controller = controller;
        frameAreaPartecipante = frame2;
        frameNotifiche = frame3;
        controller.initEventi();
        ArrayList<Evento> eventi = controller.getEventiDisponibili();

        frameEventi = new JFrame("Eventi");
        frameEventi.setContentPane(mainPanel);
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
            visualizzaArea.setVisible(false);
            eventoPanel.add(visualizzaArea);

            Utente u = controller.getUtenteCorrente();
            Partecipante partecipante = controller.getPartecipantCorrente();
            Giudice giudiceCorrente = controller.getGiudiceCorrente(ev);

            if(controller.isUtenteGiudice(ev, controller.getUtenteCorrente())) {
                iscrivitiButton.setVisible(false);
                visualizzaArea.setVisible(false);
            }else if(partecipante != null && partecipante.getEventi().contains(ev)) {
                iscrivitiButton.setVisible(false);
                visualizzaArea.setVisible(true);
                visualizzaButton.setVisible(true);
                notifiche.setVisible(true);
            }else {
                iscrivitiButton.setVisible(true);
                visualizzaArea.setVisible(false);
            }

            iscrivitiButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Partecipante p = controller.getPartecipantCorrente();
                    if(p!= null && p.getEventi().contains(ev)) {
                        iscrivitiButton.setVisible(false);
                        visualizzaArea.setVisible(true);
                    }
                    if (p == null) {
                        p=new Partecipante(u.getLogin(), u.getPassword(), null, new ArrayList<>());
                        controller.setPartecipantCorrente(p);
                    }

                    //Aggiunge evento alla sua liste e viceversa
                    p.getEventi().add(ev);
                    if(!ev.getPartecipanti().contains(p)) {
                        ev.getPartecipanti().add(p);
                    }

                    controller.stampaPartecipantiEvento(ev);

                    System.out.println("Utenti attualmente nel sistema");
                    for(Utente u: controller.getUtentiRegistrati()){
                        System.out.println(u.getLogin());
                    }


                    JOptionPane.showMessageDialog(frameEventi,"Iscrizione completata con successo!");
                    iscrivitiButton.setVisible(false);
                    visualizzaArea.setVisible(true);
                    AreaPartecipante quintaGUI = new AreaPartecipante(p, ev, frameEventi, frameAccedi, frameNotifiche, frameAreaPartecipante, controller);
                    quintaGUI.frameAreaPartecipante.setVisible(true);
                    frameEventi.setVisible(false);
                }

            });
            panelEventi.add(eventoPanel);
            visualizzaArea.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Partecipante p = controller.getPartecipantCorrente();
                    AreaPartecipante areaGUI = new AreaPartecipante(p, ev, frameEventi, frameAccedi, frameNotifiche, frameAreaPartecipante, controller);
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
                controller.setPartecipantCorrente(null);
                controller.setUtenteCorrente(null);
                frameEventi.setVisible(false);
                frameAccedi.setVisible(true);
            }
        });
        visualizzaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Utente utente = controller.getUtenteCorrente();
                System.out.println("Utente corrente: " + utente.getLogin());
                System.out.println("Inviti trovati: " + controller.getInvitiUtente(utente).size());
                VediNotifica notifica = new VediNotifica(controller, utente, frameEventi, frameGiudice, frameAccedi, frameAreaPartecipante );
                notifica.frameNotifiche.setVisible(true);
                frameEventi.setVisible(false);
            }
        });
    }

}
