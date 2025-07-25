package gui;
import controller.Controller;
import model.*;

import javax.swing.*;
import java.awt.*;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

import java.util.List;

/**
 * GUI per la visualizzazione della classifica finale di un evento nell'applicazione Hackaton.
 * <p>
 * Mostra la lista dei team partecipanti ordinati per media dei voti ricevuti,
 * visualizzando posizione, nome del team e media dei voti.
 * <p>
 * L'organizzazione della classifica è dinamica: la lista è ordinata in tempo reale
 * in base alla media dei voti calcolata per ciascun team.
 * <p>
 * Include un bottone per tornare all'area eventi.
 *
 * <ul>
 *   <li>Visualizza titolo dell'evento e classifica finale</li>
 *   <li>Ordina i team per media voti decrescente</li>
 *   <li>Gestisce lo stile grafico e l'effetto hover sui bottoni</li>
 *   <li>Permette di tornare indietro alla schermata eventi</li>
 * </ul>
 */
public class Classifica {

    /** Pannello principale della GUI classifica. */
    private JPanel mainPanel;

    /** Label di ingresso/titolo. */
    private JLabel ingresso;

    /** ScrollPane per scorrere tra i team. */
    private JScrollPane scroll;

    /** Pannello che contiene le righe dei voti e dei team. */
    private JPanel panelVoti;

    /** Bottone per tornare all'area eventi. */
    private JButton backButton;

    /** Controller logico dell'applicazione. */
    private Controller controller;

    /** Frame principale della classifica. */
    private JFrame frameClassifica;

    /** Frame dell'area eventi per il ritorno. */
    private JFrame frameEventi;

    /** Identificativo dell'evento visualizzato. */
    private int eventoId;

    /** Nome del font da utilizzare per le componenti grafiche. */
    private static final String FONT_FAMILY = "SansSerif";

    /** Colore di sfondo principale dell'interfaccia (azzurro molto chiaro). */
    private static final Color BG_COLOR = new Color(240, 248, 255);

    /** Colore di sfondo dei pulsanti. */
    private static final Color BTN_COLOR = new Color(30, 144, 255);

    /** Colore di sfondo dei pulsanti quando il mouse è sopra (hover). */
    private static final Color BTN_HOVER_COLOR = new Color(65, 105, 225);

    /** Colore di sfondo alternativo per le righe (leggermente diverso per evidenziare le righe). */
    private static final Color RIGA_COLOR = new Color(225, 235, 245);

    /** Colore dei bordi utilizzati nei campi e nei pannelli. */
    private static final Color BORDER_COLOR = new Color(210, 210, 210);

    /**
     * Costruisce la GUI della classifica per uno specifico evento, popolando la lista dei team ordinata per media voti.
     *
     * @param idEvento identificativo dell'evento
     * @param controller Controller logico dell'applicazione
     * @param frameAreaEventi frame dell'area eventi per il ritorno
     */
    public Classifica(int idEvento, Controller controller, JFrame frameAreaEventi) {
        this.controller = controller;
        this.frameEventi = frameAreaEventi;
        this.eventoId = idEvento;

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

        if (scroll != null) {
            scroll.getVerticalScrollBar().setUnitIncrement(20);
            scroll.setBorder(BorderFactory.createEmptyBorder());
            scroll.getViewport().setBackground(BG_COLOR);
        }

        Evento evento = controller.getEventoById(eventoId);
        if (ingresso != null) {
            ingresso.setText("Classifica finale di " + evento.getTitolo());
            ingresso.setFont(titleFont);
            ingresso.setHorizontalAlignment(SwingConstants.CENTER);
        }

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

    /**
     * Applica l'effetto hover al bottone.
     *
     * @param btn bottone da modificare
     * @param normal colore normale
     * @param hover colore in hover
     */
    private void setButtonHover(JButton btn, Color normal, Color hover) {
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) { btn.setBackground(hover);}
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) { btn.setBackground(normal);}
        });
    }

    /**
     * Restituisce il frame della classifica.
     *
     * @return frame della classifica
     */
    public JFrame getFrameClassifica() {
        return frameClassifica;
    }
}
