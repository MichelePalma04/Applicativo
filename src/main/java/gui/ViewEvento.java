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

/**
 * GUI per la visualizzazione e gestione degli eventi accessibili dall'utente nell'applicazione Hackaton.
 * <p>
 * Permette all'utente di:
 * <ul>
 *   <li>Visualizzare la lista degli eventi disponibili tramite card interattive</li>
 *   <li>Iscriversi come partecipante ad un evento</li>
 *   <li>Accedere alla propria area personale di partecipante o di giudice</li>
 *   <li>Visualizzare la classifica finale dell'evento (se concluso)</li>
 *   <li>Visualizzare le notifiche ricevute</li>
 *   <li>Effettuare il logout</li>
 * </ul>
 * <p>
 * Gli stili grafici e le interazioni sono definiti per coerenza con il resto dell'applicazione.
 */
public class ViewEvento {

    /** Pannello principale della GUI eventi. */
    private JPanel mainPanel;

    /** Pannello che contiene le card degli eventi. */
    private JPanel panelEventi;

    /** ScrollPane per scorrere tra gli eventi. */
    private JScrollPane scroll;

    /** Bottone per il logout. */
    private JButton logOutButton1;

    /** Bottone per visualizzare le notifiche. */
    private JButton visualizzaButton;

    /** Label per le notifiche. */
    private JLabel notifiche;

    /** Pannello per i bottoni in alto. */
    private JPanel panelBottoni;

    /** Controller logico dell'applicazione. */
    private Controller controller;

    /** Login dell'utente attualmente loggato. */
    private String loginUtente;

    /** Frame principale della schermata eventi. */
    private JFrame frameEventi;

    /** Frame della schermata di accesso. */
    private JFrame frameAccedi;

    /** Frame dell'area partecipante. */
    private JFrame frameAreaPartecipante;

    /** Frame della schermata notifiche. */
    private JFrame frameNotifiche;

    /** Frame dell'area giudice. */
    private JFrame frameGiudice;

    /** Font di default per la GUI. */
    private static final String FONT_FAMILY = "SansSerif";

    /**
     * Costruisce la GUI per la visualizzazione degli eventi, inizializza gli stili, listeners e popola la schermata.
     *
     * @param controller Controller logico dell'applicazione
     * @param loginUser login dell'utente che consulta gli eventi
     * @param frameAreaAccesso frame della schermata di accesso
     * @param framePartecipante frame dell'area partecipante
     * @param frameAreaNotifiche frame notifiche
     * @param frameAreaGiudice frame giudice
     */
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

    /**
     * Imposta l'effetto hover sui bottoni.
     * @param button bottone da modificare
     * @param btnColor colore normale del bottone
     * @param btnHoverColor colore del bottone in hover
     */
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

    /**
     * Crea una card per la visualizzazione di un singolo evento.
     * <p>
     * La card mostra informazioni sull'evento e offre bottoni per iscrizione, area personale,
     * classifica e visualizzazione info, in base allo stato dell'utente e dell'evento.
     *
     * @param ev evento da visualizzare
     * @param btnColor colore base dei bottoni
     * @param btnHoverColor colore dei bottoni in hover
     * @param cardColor colore di sfondo della card
     * @param borderColor colore bordo della card
     * @param fieldFont font per le informazioni
     * @return JPanel rappresentante la card dell'evento
     */
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

    /**
     * Crea una JLabel con font personalizzato.
     * @param text testo della label
     * @param font font da applicare
     * @return JLabel personalizzata
     */
    private JLabel creaLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    /**
     * Restituisce il frame della schermata eventi.
     * @return frame della schermata eventi
     */
    public JFrame getFrameEventi() {
        return frameEventi;
    }

    /**
     * Restituisce il frame dell'area partecipante.
     * @return frame dell'area partecipante
     */
    public JFrame getFramePartecipante() {
        return frameAreaPartecipante;
    }
}
