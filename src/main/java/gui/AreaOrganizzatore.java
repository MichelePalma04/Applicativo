package gui;

import controller.Controller;
import model.Evento;
import model.Organizzatore;

import javax.swing.*;
import java.awt.*;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

import java.util.List;

public class AreaOrganizzatore {
    private JPanel panel;
    private JLabel benvenuto;
    private JPanel panelEventi;
    private JScrollPane scroll;
    private JButton logOutButton;
    private JButton creaEventiButton;
    private JFrame frameOrganizzatore;
    private JFrame frameAccessi;
    private JFrame frameInviti;
    private Controller controller;

    private static final String FONT_FAMILY = "SansSerif";


    public AreaOrganizzatore(Controller controller, Organizzatore organizzatore, JFrame frameAreaInviti, JFrame frameAreaAccesso) {
        this.controller = controller;
        frameInviti = frameAreaInviti;
        frameAccessi = frameAreaAccesso;

        // Color and Font settings
        Color bgColor = new Color(240, 248, 255);      // chiaro azzurrino
        Color cardColor = new Color(225, 235, 245);    // più scuro per le card
        Color btnColor = new Color(30, 144, 255);
        Color borderColor = new Color(210, 210, 210);
        Color btnHoverColor = new Color(65, 105, 225);
        Font labelFont = new Font(FONT_FAMILY, Font.BOLD, 16);
        Font fieldFont = new Font(FONT_FAMILY, Font.PLAIN, 15);


        // Main panel style
        panel.setBackground(bgColor);
        benvenuto.setFont(labelFont);

        // Pulsanti stile
        logOutButton.setBackground(btnColor);
        logOutButton.setForeground(Color.WHITE);
        logOutButton.setFocusPainted(false);
        logOutButton.setFont(labelFont);
        logOutButton.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));

        creaEventiButton.setBackground(btnColor);
        creaEventiButton.setForeground(Color.WHITE);
        creaEventiButton.setFocusPainted(false);
        creaEventiButton.setFont(labelFont);
        creaEventiButton.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));

        // Effetto hover pulsanti
        logOutButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                logOutButton.setBackground(btnHoverColor);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                logOutButton.setBackground(btnColor);
            }
        });

        creaEventiButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                creaEventiButton.setBackground(btnHoverColor);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                creaEventiButton.setBackground(btnColor);
            }
        });

        benvenuto.setText("Benvenuto organizzatore " + organizzatore.getLogin());

        frameOrganizzatore = new JFrame("Area Organizzatore " + organizzatore.getLogin());
        frameOrganizzatore.setContentPane(panel);
        frameOrganizzatore.pack();
        frameOrganizzatore.setSize(600, 600);
        frameOrganizzatore.setLocationRelativeTo(null);
        frameOrganizzatore.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frameOrganizzatore.setVisible(true);

        scroll.getVerticalScrollBar().setUnitIncrement(20);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        panelEventi.setLayout(new GridLayout(0, 2, 20, 20));
        panelEventi.setBackground(bgColor);

        // Popola eventi come card
        List<Evento> eventi = controller.getEventiOrganizzatore(organizzatore.getLogin());
        for (Evento evento : eventi) {
            JPanel eventoCard = new JPanel(new BorderLayout());
            eventoCard.setBackground(cardColor);
            eventoCard.setBorder(BorderFactory.createLineBorder(borderColor, 1));
            eventoCard.setPreferredSize(new Dimension(220, 150));

            // Titolo e sede in alto
            JPanel topPanel = new JPanel();
            topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
            topPanel.setOpaque(false);

            JLabel titoloLabel = new JLabel("<html><b>" + evento.getTitolo() + "</b></html>");
            titoloLabel.setFont(fieldFont);
            titoloLabel.setHorizontalAlignment(SwingConstants.CENTER); // sinistra

            JLabel sedeLabel = new JLabel("Sede: " + evento.getSede());
            sedeLabel.setFont(new Font(FONT_FAMILY, Font.PLAIN, 13));
            sedeLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // sinistra

            topPanel.add(titoloLabel);
            topPanel.add(sedeLabel);

            // Bottone Dettagli in basso, largo quanto la card
            JButton infoButton = new JButton("Dettagli");
            infoButton.setBackground(btnColor);
            infoButton.setForeground(Color.WHITE);
            infoButton.setFocusPainted(false);
            infoButton.setFont(new Font(FONT_FAMILY, Font.BOLD, 13));
            infoButton.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0));
            infoButton.setPreferredSize(new Dimension(220, 40));
            infoButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

            infoButton.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    infoButton.setBackground(btnHoverColor);
                }
                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    infoButton.setBackground(btnColor);
                }
            });

            infoButton.addActionListener(e->{
                Invito nuovaGUI = new Invito(evento.getId(), frameOrganizzatore, controller);
                nuovaGUI.getFrameInvito().setVisible(true);
                frameOrganizzatore.setVisible(false);
            });

            eventoCard.add(topPanel, BorderLayout.NORTH);
            eventoCard.add(Box.createVerticalGlue(), BorderLayout.CENTER); // Spinge il bottone in basso
            eventoCard.add(infoButton, BorderLayout.SOUTH);

            panelEventi.add(eventoCard);
        }

        logOutButton.addActionListener(e -> {
            frameOrganizzatore.setVisible(false);
            frameAccessi.setVisible(true);

        });

        creaEventiButton.addActionListener(e -> {
            CreazioneEventi nuovaGUI = new CreazioneEventi(controller, frameOrganizzatore, organizzatore, frameAccessi, frameInviti);
            nuovaGUI.getFrameCreazioneEventi().setVisible(true);
            frameOrganizzatore.setVisible(false);
        });

        /*benvenuto.setText("Benvenuto organizzatore "+ organizzatore.getLogin());
        frameOrganizzatore = new JFrame("Area Organizzatore " + organizzatore.getLogin());
        frameOrganizzatore.setContentPane(panel);
        frameOrganizzatore.pack();
        frameOrganizzatore.setSize(500, 500);
        frameOrganizzatore.setLocationRelativeTo(null);
        frameOrganizzatore.setVisible(true);

        scroll.getVerticalScrollBar().setUnitIncrement(20);
        panelEventi.setLayout(new BoxLayout(panelEventi, BoxLayout.Y_AXIS));

        List <Evento> eventi = controller.getEventiOrganizzatore(organizzatore.getLogin());
        for (Evento evento: eventi){
            JPanel eventoBox = new JPanel(new BorderLayout());
            JLabel eventoLabel = new JLabel(evento.getTitolo() + " " + evento.getSede());
            JButton infoButton = new JButton("Dettagli");

            infoButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Invito nuovaGUI = new Invito (evento.getId(), frameOrganizzatore, controller);
                    nuovaGUI.frameInvito.setVisible(true);
                    frameOrganizzatore.setVisible(false);
                }
            });

            eventoBox.add(eventoLabel);
            eventoBox.add(infoButton, BorderLayout.SOUTH);
            eventoBox.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            panelEventi.add(eventoBox);
        }

        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameOrganizzatore.setVisible(false);
                frameAccessi.setVisible(true);
            }
        });

        creaEventiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreazioneEventi nuovaGUI = new CreazioneEventi(controller, frameOrganizzatore, organizzatore, frameAccessi, frameInviti);
                nuovaGUI.frameCreazioneEventi.setVisible(true);
                frameOrganizzatore.setVisible(false);
            }
        });*/
    }
    public JFrame getFrameOrganizzatore() {
        return frameOrganizzatore;
    }
}
