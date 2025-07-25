package gui;

import controller.Controller;
import model.Evento;
import model.Giudice;
import model.Partecipante;


import javax.swing.*;
import java.awt.*;

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
    private JFrame frameEventi;
    private JFrame frameAccedi;
    private JFrame frameAreaPartecipante;
    private JFrame frameNotifiche;
    private JFrame frameGiudice;

    public ViewEvento(Controller controller, String loginUser, JFrame frameAreaAccesso, JFrame framePartecipante, JFrame frameAreaNotifiche, JFrame frameAreaGiudice) {
        scroll.getVerticalScrollBar().setUnitIncrement(20); //abbiamo aumentato la sensibilità dello scroll
        frameAccedi = frameAreaAccesso;
        frameAreaPartecipante = framePartecipante;
        frameNotifiche = frameAreaNotifiche;
        frameGiudice = frameAreaGiudice;
        this.controller = controller;
        this.loginUtente = loginUser;


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
            eventoPanel.add(new JLabel("Data apertura registrazione: " +ev.getInizioReg()));
            eventoPanel.add(new JLabel("Data fine registrazione: " +ev.getFineReg()));
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

            classificaButton.addActionListener(e -> {
                Classifica classificaGUI = new Classifica(ev.getId(), controller, frameEventi);
                classificaGUI.getFrameClassifica().setVisible(true);
                frameEventi.setVisible(false);
            });

            areaGiudice.addActionListener(e -> {
                Giudice g =  controller.getGiudiceEvento(loginUtente, ev.getId());
                if(g != null) {
                    AreaGiudice gui = new AreaGiudice(controller, loginUtente, ev.getId(), frameAreaAccesso, frameEventi, loginUtente);
                    gui.getFrameGiudice().setVisible(true);
                    frameEventi.setVisible(false);
                }
            });

            iscrivitiButton.addActionListener(e ->{

                    // Verifica se l'utente è già partecipante per questo evento (controllo su DB)
                    Partecipante p = controller.getPartecipanteDaDB(loginUtente, ev.getId());
                    if (p != null) {
                        // Già iscritto: mostra solo l'area partecipante
                        iscrivitiButton.setVisible(false);
                        visualizzaArea.setVisible(true);
                        AreaPartecipante areaGUI = new AreaPartecipante(loginUtente, ev.getId(), frameEventi, frameAccedi, frameNotifiche, frameAreaPartecipante, controller);
                        areaGUI.getFramePartecipante().setVisible(true);
                        frameEventi.setVisible(false);
                        return;
                    }

                    // Tenta iscrizione su DB
                    boolean successo = controller.iscriviPartecipante(loginUtente, ev.getId());
                    if (successo) {
                        JOptionPane.showMessageDialog(frameEventi, "Iscrizione completata con successo!");
                        iscrivitiButton.setVisible(false);
                        visualizzaArea.setVisible(true);

                        AreaPartecipante areaGUI = new AreaPartecipante(loginUtente, ev.getId(), frameEventi, frameAccedi, frameNotifiche, frameAreaPartecipante, controller);
                        areaGUI.getFramePartecipante().setVisible(true);
                        frameEventi.setVisible(false);
                    } else {
                        JOptionPane.showMessageDialog(frameEventi, "Errore durante l'iscrizione!");
                    }
            });

            panelEventi.add(eventoPanel);

            visualizzaArea.addActionListener(e -> {
                AreaPartecipante areaGUI = new AreaPartecipante(loginUtente, ev.getId(), frameEventi, frameAccedi, frameNotifiche, frameAreaPartecipante, controller);
                areaGUI.getFramePartecipante().setVisible(true);
                frameEventi.setVisible(false);
            });

        }

        panelEventi.revalidate();
        panelEventi.repaint();



        logOutButton1.addActionListener(e -> {
            frameEventi.setVisible(false);
            frameAccedi.setVisible(true);
        });

        visualizzaButton.addActionListener(e ->{
            VediNotifica notifica = new VediNotifica(controller, loginUtente, frameEventi, frameGiudice, frameAccedi, frameAreaPartecipante );
            notifica.getFrameNotifiche().setVisible(true);
            frameEventi.setVisible(false);
        });
    }

    public JFrame getFrameEventi() {
        return frameEventi;
    }

    public JFrame getFramePartecipante() {
        return frameAreaPartecipante;
    }
}
