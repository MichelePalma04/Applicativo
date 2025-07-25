package gui;

import controller.Controller;
import model.Evento;
import model.Giudice;
import model.Partecipante;


import javax.swing.*;
import java.awt.*;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

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
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        scroll.setBorder(BorderFactory.createEmptyBorder());//abbiamo aumentato la sensibilità dello scroll
        frameAccedi = frameAreaAccesso;
        frameAreaPartecipante = framePartecipante;
        frameNotifiche = frameAreaNotifiche;
        frameGiudice = frameAreaGiudice;
        this.controller = controller;
        this.loginUtente = loginUser;

        // --- Stili coerenti ---
        Color bgColor = new Color(240, 248, 255);      // chiaro azzurrino
        Color cardColor = new Color(225, 235, 245);    // più scuro per le card
        Color btnColor = new Color(30, 144, 255);
        Color btnHoverColor = new Color(65, 105, 225);
        Color borderColor = new Color(210, 210, 210);
        Font labelFont = new Font("SansSerif", Font.BOLD, 16);
        Font fieldFont = new Font("SansSerif", Font.PLAIN, 15);

        // Stile pannelli
        if (mainPanel != null) mainPanel.setBackground(bgColor);
        if (panelEventi != null) panelEventi.setBackground(bgColor);
        if (panelBottoni != null) panelBottoni.setBackground(bgColor);

        // Stile bottoni
        JButton[] allButtons = {logOutButton1, visualizzaButton};
        for (JButton btn : allButtons) {
            if (btn == null) continue;
            btn.setBackground(btnColor);
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setFont(labelFont);
            btn.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        }
        // Hover
        if (logOutButton1 != null) {
            logOutButton1.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) { logOutButton1.setBackground(btnHoverColor);}
                public void mouseExited(java.awt.event.MouseEvent evt) { logOutButton1.setBackground(btnColor);}
            });
        }
        if (visualizzaButton != null) {
            visualizzaButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) { visualizzaButton.setBackground(btnHoverColor);}
                public void mouseExited(java.awt.event.MouseEvent evt) { visualizzaButton.setBackground(btnColor);}
            });
        }

        // Stile notifiche label
        if (notifiche != null) notifiche.setFont(labelFont);



        List<Evento> eventi = controller.getTuttiEventi();

        frameEventi = new JFrame("Eventi");
        frameEventi.setContentPane(mainPanel);
        frameEventi.pack();
        frameEventi.setSize(600,600);
        frameEventi.setLocationRelativeTo(null);
        frameEventi.setDefaultCloseOperation(EXIT_ON_CLOSE);

        panelEventi.setLayout(new GridLayout(0, 2, 20, 20));
        panelEventi.setBackground(bgColor);


        for (Evento ev : eventi) {
            JPanel eventoCard = new JPanel(new BorderLayout());
            eventoCard.setBackground(cardColor);
            eventoCard.setBorder(BorderFactory.createLineBorder(borderColor, 1));
            eventoCard.setPreferredSize(new Dimension(220, 210));

            // Titolo in alto
            JLabel titoloLabel = new JLabel("<html><b>" + ev.getTitolo() + "</b></html>", SwingConstants.CENTER);
            titoloLabel.setFont(new Font("SansSerif", Font.BOLD, 17));
            titoloLabel.setHorizontalAlignment(SwingConstants.CENTER);

            // Info evento (sede, date, organizzatore)
            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
            infoPanel.setOpaque(false);

            JLabel sedeLabel = new JLabel("Sede: " + ev.getSede());
            sedeLabel.setFont(fieldFont);
            sedeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel dataInizioLabel = new JLabel("Data inizio: " + ev.getDataInizio());
            dataInizioLabel.setFont(fieldFont);
            dataInizioLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel dataFineLabel = new JLabel("Data fine: " + ev.getDataFine());
            dataFineLabel.setFont(fieldFont);
            dataFineLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel inizioRegLabel = new JLabel("Apertura iscrizioni: " + ev.getInizioReg());
            inizioRegLabel.setFont(fieldFont);
            inizioRegLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel fineRegLabel = new JLabel("Chiusura iscrizioni: " + ev.getFineReg());
            fineRegLabel.setFont(fieldFont);
            fineRegLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel orgLabel = new JLabel("Organizzatore: " + ev.getOrganizzatore().getLogin());
            orgLabel.setFont(fieldFont);
            orgLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            infoPanel.add(sedeLabel);
            infoPanel.add(dataInizioLabel);
            infoPanel.add(dataFineLabel);
            infoPanel.add(inizioRegLabel);
            infoPanel.add(fineRegLabel);
            infoPanel.add(orgLabel);

            // Bottoni in basso
            JPanel btnPanel = new JPanel();
            btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.Y_AXIS));
            btnPanel.setOpaque(false);

            JButton iscrivitiButton = new JButton("Iscriviti");
            JButton visualizzaArea = new JButton("Visualizza info");
            JButton areaGiudice = new JButton("Area personale giudice");
            JButton classificaButton = new JButton("Vedi classifica");

            JButton[] cardButtons = { iscrivitiButton, visualizzaArea, areaGiudice, classificaButton };
            for (JButton b : cardButtons) {
                b.setBackground(btnColor);
                b.setForeground(Color.WHITE);
                b.setFocusPainted(false);
                b.setFont(new Font("SansSerif", Font.BOLD, 13));
                b.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
                b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
                b.setAlignmentX(Component.CENTER_ALIGNMENT);
                b.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent evt) { b.setBackground(btnHoverColor);}
                    public void mouseExited(java.awt.event.MouseEvent evt) { b.setBackground(btnColor);}
                });
            }

            visualizzaArea.setVisible(false);
            areaGiudice.setVisible(false);
            classificaButton.setVisible(false);

            // Logica visibilità bottoni
            Partecipante partecipante = controller.getPartecipanteDaDB(loginUtente, ev.getId());
            Giudice giudice = controller.getGiudiceEvento(loginUtente, ev.getId());
            boolean isGiudice = giudice != null;
            boolean isPartecipante = partecipante != null;
            boolean eventoFinito = ev.getDataFine().isBefore(LocalDate.now());

            if (isGiudice) {
                iscrivitiButton.setVisible(false);
                visualizzaArea.setVisible(false);
                areaGiudice.setVisible(true);
            } else if (isPartecipante) {
                iscrivitiButton.setVisible(false);
                visualizzaArea.setVisible(true);
            } else {
                iscrivitiButton.setVisible(true);
                visualizzaArea.setVisible(false);
                areaGiudice.setVisible(false);
            }
            if (eventoFinito) {
                classificaButton.setVisible(true);
            }

            // Listener bottoni
            classificaButton.addActionListener(e -> {
                Classifica classificaGUI = new Classifica(ev.getId(), controller, frameEventi);
                classificaGUI.getFrameClassifica().setVisible(true);
                frameEventi.setVisible(false);
            });

            areaGiudice.addActionListener(e -> {
                Giudice g =  controller.getGiudiceEvento(loginUtente, ev.getId());
                if(g != null) {
                    AreaGiudice gui = new AreaGiudice(controller, loginUtente, ev.getId(), frameAccedi, frameEventi);
                    gui.getFrameGiudice().setVisible(true);
                    frameEventi.setVisible(false);
                }
            });

            iscrivitiButton.addActionListener(e -> {
                Partecipante p = controller.getPartecipanteDaDB(loginUtente, ev.getId());
                if (p != null) {
                    iscrivitiButton.setVisible(false);
                    visualizzaArea.setVisible(true);
                    AreaPartecipante areaGUI = new AreaPartecipante(loginUtente, ev.getId(), frameEventi, frameAccedi, frameNotifiche, frameAreaPartecipante, controller);
                    areaGUI.getFramePartecipante().setVisible(true);
                    frameEventi.setVisible(false);
                    return;
                }
                boolean successo = controller.iscriviPartecipante(loginUtente, ev.getId());
                if (!successo) {
                    JOptionPane.showMessageDialog(frameEventi, "Errore durante l'iscrizione!");
                    return;
                }
                JOptionPane.showMessageDialog(frameEventi, "Iscrizione completata con successo!");
                iscrivitiButton.setVisible(false);
                visualizzaArea.setVisible(true);
                AreaPartecipante areaGUI = new AreaPartecipante(loginUtente, ev.getId(), frameEventi, frameAccedi, frameNotifiche, frameAreaPartecipante, controller);
                areaGUI.getFramePartecipante().setVisible(true);
                frameEventi.setVisible(false);
            });

            visualizzaArea.addActionListener(e -> {
                AreaPartecipante areaGUI = new AreaPartecipante(loginUtente, ev.getId(), frameEventi, frameAccedi, frameNotifiche, frameAreaPartecipante, controller);
                areaGUI.getFramePartecipante().setVisible(true);
                frameEventi.setVisible(false);
            });

            // Layout della card
            eventoCard.add(titoloLabel, BorderLayout.NORTH);
            eventoCard.add(infoPanel, BorderLayout.CENTER);

            JPanel southPanel = new JPanel();
            southPanel.setOpaque(false);
            southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
            // Spaziatura
            southPanel.add(Box.createVerticalStrut(6));
            for (JButton b : cardButtons) southPanel.add(b);
            eventoCard.add(southPanel, BorderLayout.SOUTH);

            panelEventi.add(eventoCard);
        }
        /*
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


            boolean isGiudice = giudice != null;
            boolean isPartecipante = partecipante != null;
            boolean eventoFinito = ev.getDataFine().isBefore(LocalDate.now());

            if (isGiudice) {
                iscrivitiButton.setVisible(false);
                visualizzaArea.setVisible(false);
                areaGiudice.setVisible(true);
            } else if (isPartecipante) {
                iscrivitiButton.setVisible(false);
                visualizzaArea.setVisible(true);
            }

            if (eventoFinito) {
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

                boolean successo = controller.iscriviPartecipante(loginUtente, ev.getId());
                if (!successo) {
                    JOptionPane.showMessageDialog(frameEventi, "Errore durante l'iscrizione!");
                    return;
                }
                JOptionPane.showMessageDialog(frameEventi, "Iscrizione completata con successo!");
                iscrivitiButton.setVisible(false);
                visualizzaArea.setVisible(true);
                AreaPartecipante areaGUI = new AreaPartecipante(loginUtente, ev.getId(), frameEventi, frameAccedi, frameNotifiche, frameAreaPartecipante, controller);
                areaGUI.getFramePartecipante().setVisible(true);
                frameEventi.setVisible(false);
            });

            panelEventi.add(eventoPanel);

            visualizzaArea.addActionListener(e -> {
                AreaPartecipante areaGUI = new AreaPartecipante(loginUtente, ev.getId(), frameEventi, frameAccedi, frameNotifiche, frameAreaPartecipante, controller);
                areaGUI.getFramePartecipante().setVisible(true);
                frameEventi.setVisible(false);
            });

        }*/

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
