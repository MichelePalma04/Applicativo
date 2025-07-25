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

    private static final String FONT_FAMILY = "SansSerif";

    public ViewEvento(Controller controller, String loginUser, JFrame frameAreaAccesso, JFrame framePartecipante, JFrame frameAreaNotifiche, JFrame frameAreaGiudice) {
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        frameAccedi = frameAreaAccesso;
        frameAreaPartecipante = framePartecipante;
        frameNotifiche = frameAreaNotifiche;
        frameGiudice = frameAreaGiudice;
        this.controller = controller;
        this.loginUtente = loginUser;

        // --- Stili coerenti ---
        Color bgColor = new Color(240, 248, 255);
        Color cardColor = new Color(225, 235, 245);
        Color btnColor = new Color(30, 144, 255);
        Color btnHoverColor = new Color(65, 105, 225);
        Color borderColor = new Color(210, 210, 210);
        Font labelFont = new Font(FONT_FAMILY, Font.BOLD, 16);
        Font fieldFont = new Font(FONT_FAMILY, Font.PLAIN, 15);

        if (mainPanel != null) mainPanel.setBackground(bgColor);
        if (panelEventi != null) panelEventi.setBackground(bgColor);
        if (panelBottoni != null) panelBottoni.setBackground(bgColor);

        JButton[] allButtons = {logOutButton1, visualizzaButton};
        for (JButton btn : allButtons) {
            if (btn == null) continue;
            btn.setBackground(btnColor);
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setFont(labelFont);
            btn.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        }
        setButtonHover(logOutButton1, btnColor, btnHoverColor);
        setButtonHover(visualizzaButton, btnColor, btnHoverColor);

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
            panelEventi.add(creaEventoCard(ev, btnColor, btnHoverColor, cardColor, borderColor, fieldFont));
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

    private void setButtonHover(JButton button, Color btnColor, Color btnHoverColor) {
        if (button != null) {
            button.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    button.setBackground(btnHoverColor);
                }
                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    button.setBackground(btnColor);
                }
            });
        }
    }

    private JPanel creaEventoCard(Evento ev, Color btnColor, Color btnHoverColor, Color cardColor, Color borderColor, Font fieldFont) {
        JPanel eventoCard = new JPanel(new BorderLayout());
        eventoCard.setBackground(cardColor);
        eventoCard.setBorder(BorderFactory.createLineBorder(borderColor, 1));
        eventoCard.setPreferredSize(new Dimension(220, 240));

        JLabel titoloLabel = new JLabel("<html><b>" + ev.getTitolo() + "</b></html>", SwingConstants.CENTER);
        titoloLabel.setFont(new Font(FONT_FAMILY, Font.BOLD, 17));
        titoloLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        infoPanel.add(creaLabel("Sede: " + ev.getSede(), fieldFont));
        infoPanel.add(creaLabel("Data inizio: " + ev.getDataInizio(), fieldFont));
        infoPanel.add(creaLabel("Data fine: " + ev.getDataFine(), fieldFont));
        infoPanel.add(creaLabel("Apertura iscrizioni: " + ev.getInizioReg(), fieldFont));
        infoPanel.add(creaLabel("Chiusura iscrizioni: " + ev.getFineReg(), fieldFont));
        infoPanel.add(creaLabel("Organizzatore: " + ev.getOrganizzatore().getLogin(), fieldFont));

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
            b.setFont(new Font(FONT_FAMILY, Font.BOLD, 13));
            b.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
            b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
            setButtonHover(b, btnColor, btnHoverColor);
        }

        visualizzaArea.setVisible(false);
        areaGiudice.setVisible(false);
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
        } else {
            iscrivitiButton.setVisible(true);
            visualizzaArea.setVisible(false);
            areaGiudice.setVisible(false);
        }
        if (eventoFinito) {
            classificaButton.setVisible(true);
            visualizzaArea.setVisible(false);
        }

        classificaButton.addActionListener(e -> {
            Classifica classificaGUI = new Classifica(ev.getId(), controller, frameEventi);
            classificaGUI.getFrameClassifica().setVisible(true);
            frameEventi.setVisible(false);
        });

        areaGiudice.addActionListener(e -> {
            Giudice g =  controller.getGiudiceEvento(loginUtente, ev.getId());
            if(g != null) {
                AreaGiudice gui = new AreaGiudice(controller, loginUtente, ev.getId(), frameEventi);
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

        eventoCard.add(titoloLabel, BorderLayout.NORTH);
        eventoCard.add(infoPanel, BorderLayout.CENTER);

        JPanel southPanel = new JPanel();
        southPanel.setOpaque(false);
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
        southPanel.add(Box.createVerticalStrut(6));
        for (JButton b : cardButtons) southPanel.add(b);
        eventoCard.add(southPanel, BorderLayout.SOUTH);

        return eventoCard;
    }

    private JLabel creaLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    public JFrame getFrameEventi() {
        return frameEventi;
    }

    public JFrame getFramePartecipante() {
        return frameAreaPartecipante;
    }
}
