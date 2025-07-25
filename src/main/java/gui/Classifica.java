package gui;

import controller.Controller;
import model.*;

import javax.swing.*;
import java.awt.*;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

import java.util.List;


public class Classifica {
    private JPanel mainPanel;
    private JLabel ingresso;
    private JScrollPane scroll;
    private JPanel panelVoti;
    private JButton backButton;
    private Controller controller;
    private JFrame frameClassifica;
    private JFrame frameEventi;
    private int eventoId;

    private static final String FONT_FAMILY = "SansSerif";
    private static final Color BG_COLOR = new Color(240, 248, 255);      // chiaro azzurrino
    private static final Color BTN_COLOR = new Color(30, 144, 255);
    private static final Color BTN_HOVER_COLOR = new Color(65, 105, 225);
    private static final Color RIGA_COLOR = new Color(225, 235, 245);    // leggermente diverso per le righe
    private static final Color BORDER_COLOR = new Color(210, 210, 210);

    public Classifica(int idEvento, Controller controller, JFrame frameAreaEventi) {
        this.controller = controller;
        this.frameEventi = frameAreaEventi;
        this.eventoId = idEvento;

        // Stili
        Font titleFont = new Font(FONT_FAMILY, Font.BOLD, 16);
        Font teamFont = new Font(FONT_FAMILY, Font.PLAIN, 15);
        Font btnFont = new Font(FONT_FAMILY, Font.BOLD, 14);

        frameClassifica = new JFrame("Classifica");
        frameClassifica.setContentPane(mainPanel);
        frameClassifica.pack();
        frameClassifica.setSize(500, 500);
        frameClassifica.setLocationRelativeTo(null);
        frameClassifica.setDefaultCloseOperation(EXIT_ON_CLOSE);
        if (mainPanel != null) mainPanel.setBackground(BG_COLOR);
        if (panelVoti != null) {
            panelVoti.setLayout(new BoxLayout(panelVoti, BoxLayout.Y_AXIS));
            panelVoti.setBackground(BG_COLOR);
        }

        // Scroll stile coerente
        if (scroll != null) {
            scroll.getVerticalScrollBar().setUnitIncrement(20);
            scroll.setBorder(BorderFactory.createEmptyBorder());
            scroll.getViewport().setBackground(BG_COLOR);
        }

        // Titolo ingresso
        Evento evento = controller.getEventoById(eventoId);
        if (ingresso != null) {
            ingresso.setText("Classifica finale di " + evento.getTitolo());
            ingresso.setFont(titleFont);
            ingresso.setHorizontalAlignment(SwingConstants.CENTER);
        }

        // --- Popola classifica ---
        List<Team> teams = controller.getTeamsEvento(eventoId);
        teams.sort((t1, t2) -> Double.compare(t2.mediaVoti(), t1.mediaVoti()));
        int posizione = 1;
        for (Team t : teams) {
            double media = t.mediaVoti();
            JPanel riga = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 6));
            riga.setBackground(RIGA_COLOR);
            riga.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_COLOR, 1),
                    BorderFactory.createEmptyBorder(7, 15, 7, 15)
            ));
            riga.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

            JLabel posizioneTeam = new JLabel(posizione++ + ") ");
            posizioneTeam.setFont(teamFont);

            JLabel nomeTeam = new JLabel(t.getNomeTeam() + ":");
            nomeTeam.setFont(teamFont);

            JLabel mediaTeam = new JLabel(String.format("%.2f", media));
            mediaTeam.setFont(teamFont);

            riga.add(posizioneTeam);
            riga.add(nomeTeam);
            riga.add(mediaTeam);

            panelVoti.add(Box.createVerticalStrut(7));
            panelVoti.add(riga);
        }

        // --- Bottone back stile coerente ---
        if (backButton != null) {
            backButton.setBackground(BTN_COLOR);
            backButton.setForeground(Color.WHITE);
            backButton.setFocusPainted(false);
            backButton.setFont(btnFont);
            backButton.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
            setButtonHover(backButton, BTN_COLOR, BTN_HOVER_COLOR);

            backButton.addActionListener(e -> {
                frameClassifica.dispose();
                frameEventi.setVisible(true);
            });
        }
    }

    private void setButtonHover(JButton btn, Color normal, Color hover) {
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) { btn.setBackground(hover);}
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) { btn.setBackground(normal);}
        });
    }

    public JFrame getFrameClassifica() {
        return frameClassifica;
    }
}
