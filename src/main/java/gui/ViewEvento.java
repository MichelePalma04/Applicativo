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
import java.time.LocalDate;
import java.util.List;

public class ViewEvento {
    private JPanel mainPanel;
    private JPanel panelEventi;
    private JScrollPane scroll;
    private JButton logOutButton1;
    private JButton visualizzaButton;
    private JLabel notifiche;
    private JPanel panelBottoni;
    private Controller controller;
    private String loginUtente;
    public JFrame frameEventi;
    public JFrame frameAccedi;
    public JFrame frameAreaPartecipante;
    public JFrame frameNotifiche;
    public JFrame frameGiudice;

    public ViewEvento(Controller controller, String loginUtente, JFrame frame, JFrame frame2, JFrame frame3, JFrame frame4) {
        scroll.getVerticalScrollBar().setUnitIncrement(20); //abbiamo aumentato la sensibilità dello scroll
        frameAccedi = frame;
        frameGiudice = frame4;
        this.controller = controller;
        this.loginUtente = loginUtente;

        frameAreaPartecipante = frame2;
        frameNotifiche = frame3;
        controller.initEventi();
        List<Evento> eventi = controller.getTuttiEventi();

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
            eventoPanel.add(visualizzaArea);
            visualizzaArea.setVisible(false);
            JButton areaGiudice = new JButton("Area personale giudice");
            eventoPanel.add(areaGiudice);
            areaGiudice.setVisible(false);
            JButton classificaButton = new JButton("Vedi classifica");
            eventoPanel.add(classificaButton);
            classificaButton.setVisible(false);

            Utente u = controller.getUtenteDaDB(loginUtente);
            Partecipante partecipante = controller.getPartecipanteDaDB(loginUtente, ev.getId());
            Giudice giudice = controller.getGiudiceEvento(loginUtente, ev.getId());

            if(giudice != null) {
                iscrivitiButton.setVisible(false);
                visualizzaArea.setVisible(false);
                areaGiudice.setVisible(true);
            }else if(partecipante != null) {
                iscrivitiButton.setVisible(false);
                visualizzaArea.setVisible(true);
            }

            if(ev.getDataFine().isBefore(LocalDate.now())) {
                classificaButton.setVisible(true);
            }

            classificaButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Classifica classificaGUI = new Classifica(ev.getId(), controller, frameEventi);
                    classificaGUI.frameClassifica.setVisible(true);
                    frameEventi.setVisible(false);
                }
            });

            areaGiudice.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Giudice g =  controller.getGiudiceEvento(loginUtente, ev.getId());
                    if(g != null) {
                        AreaGiudice gui = new AreaGiudice(controller, loginUtente, ev.getId(), frame, frameEventi);
                        gui.frameGiudice.setVisible(true);
                        frameEventi.setVisible(false);
                    }
                }
            });

            iscrivitiButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Utente u = controller.getUtenteDaDB(loginUtente);

                    // Verifica se l'utente è già partecipante per questo evento (controllo su DB)
                    Partecipante p = controller.getPartecipanteDaDB(loginUtente, ev.getId());
                    if (p != null) {
                        // Già iscritto: mostra solo l'area partecipante
                        iscrivitiButton.setVisible(false);
                        visualizzaArea.setVisible(true);
                        AreaPartecipante areaGUI = new AreaPartecipante(loginUtente, ev.getId(), frameEventi, frameAccedi, frameNotifiche, frameAreaPartecipante, controller);
                        areaGUI.frameAreaPartecipante.setVisible(true);
                        frameEventi.setVisible(false);
                        return;
                    }

                    // Tenta iscrizione su DB
                    boolean successo = controller.iscriviPartecipante(loginUtente, ev.getId());
                    if (successo) {
                        // Recupera ora il nuovo partecipante dal DB
                       // Partecipante nuovoPartecipante = controller.getPartecipanteDaDB(loginUtente, ev.getId());
                        //controller.setPartecipantCorrente(nuovoPartecipante);

                        JOptionPane.showMessageDialog(frameEventi, "Iscrizione completata con successo!");
                        iscrivitiButton.setVisible(false);
                        visualizzaArea.setVisible(true);

                        AreaPartecipante areaGUI = new AreaPartecipante(loginUtente, ev.getId(), frameEventi, frameAccedi, frameNotifiche, frameAreaPartecipante, controller);
                        areaGUI.frameAreaPartecipante.setVisible(true);
                        frameEventi.setVisible(false);
                    } else {
                        JOptionPane.showMessageDialog(frameEventi, "Errore durante l'iscrizione!");
                    }
                }
            });
/*
            iscrivitiButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Partecipante p = controller.getPartecipantCorrente();
                    
                    if(p!= null && p.getEventi().contains(ev)) {
                        iscrivitiButton.setVisible(false);
                        visualizzaArea.setVisible(true);
                    }
                    if (p == null) {
                        p = new Partecipante(u.getLogin(), u.getPassword(), null, new ArrayList<>());
                        controller.getUtentiRegistrati().add(p);
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

 */
            panelEventi.add(eventoPanel);

            visualizzaArea.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //Partecipante p = controller.getPartecipanteDaDB(loginUtente, ev.getId());
                    AreaPartecipante areaGUI = new AreaPartecipante(loginUtente, ev.getId(), frameEventi, frameAccedi, frameNotifiche, frameAreaPartecipante, controller);
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
                //controller.setPartecipantCorrente(null);
                //controller.setUtenteCorrente(null);
                frameEventi.setVisible(false);
                frameAccedi.setVisible(true);
            }
        });
        visualizzaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Utente utente = controller.getUtenteDaDB(loginUtente);
                VediNotifica notifica = new VediNotifica(controller, loginUtente, frameEventi, frameGiudice, frameAccedi, frameAreaPartecipante );
                notifica.frameNotifiche.setVisible(true);
                frameEventi.setVisible(false);

            }
        });
    }

}
