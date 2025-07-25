package gui;
import controller.Controller;
import model.*;

import javax.swing.*;
import java.awt.*;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

import java.util.List;

/**
 * GUI per l'area riservata ai giudici all'interno dell'applicazione Hackaton.
 * <p>
 * Permette ai giudici di:
 * <ul>
 *   <li>Visualizzare il benvenuto personalizzato</li>
 *   <li>Gestire la descrizione del problema dell'evento (se responsabili)</li>
 *   <li>Visualizzare i documenti caricati dai team</li>
 *   <li>Votare i team alla fine dell'evento</li>
 *   <li>Visualizzare dettagli documenti e commentare</li>
 * </ul>
 * <p>
 * L'interfaccia si adatta dinamicamente in base al ruolo del giudice e allo stato dell'evento.
 * Tutti gli stili e le interazioni sono gestiti localmente per un'esperienza utente coerente.
 */
public class AreaGiudice {

    /** Pannello principale della GUI. */
    private JPanel mainPanel;

    /** Label di benvenuto per il giudice. */
    private JLabel benvenuto;

    /** ScrollPane per visualizzare l'elenco dei team e documenti. */
    private JScrollPane scroll;

    /** Pannello interno dove vengono aggiunti i componenti dinamici. */
    private JPanel panel;

    /** Bottone per tornare indietro. */
    private JButton back;

    /** Finestra della GUI per il giudice. */
    private JFrame frameGiudice;

    /** Finestra della GUI degli eventi (per tornare indietro). */
    private JFrame frameEventi;

    /** Login del giudice attualmente loggato. */
    private String loginGiudice;

    /** Identificativo dell'evento visualizzato. */
    private int eventoId;

    /** Nome del font da utilizzare per le componenti grafiche. */
    private static final String FONT_FAMILY = "SansSerif";

    /** Stringa di chiusura per i contenuti HTML, utile per formattare testi in JLabel/JEditorPane. */
    private static final String HTML_END = "</html>";

    /** Controller logico dell'applicazione. */
    private Controller controller;

    /** Colore di sfondo principale (azzurrino chiaro). */
    private final Color bgColor = new Color(240, 248, 255);

    /** Colore dei bottoni. */
    private final Color btnColor = new Color(30, 144, 255);

    /** Colore dei bottoni in hover. */
    private final Color btnHoverColor = new Color(65, 105, 225);

    /** Font per le label. */
    private final Font labelFont = new Font(FONT_FAMILY, Font.BOLD, 16);

    /** Font per i campi di testo. */
    private final Font fieldFont = new Font(FONT_FAMILY, Font.PLAIN, 15);

    /**
     * Costruisce l'area giudice, inizializzando la GUI e i dati relativi al giudice e all'evento.
     *
     * @param controller Controller dell'applicazione per la gestione logica
     * @param giudiceLogin login del giudice attualmente loggato
     * @param idEvento identificativo dell'evento associato
     * @param frameAreaEventi frame della GUI eventi, per gestire il ritorno indietro
     */
    public AreaGiudice(Controller controller, String giudiceLogin, int idEvento, JFrame frameAreaEventi) {
        this.controller = controller;
        this.loginGiudice = giudiceLogin;
        this.eventoId = idEvento;
        frameEventi = frameAreaEventi;

        benvenuto.setText("Benvenuto " + loginGiudice);
        benvenuto.setFont(labelFont);
        benvenuto.setHorizontalAlignment(SwingConstants.CENTER);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        frameGiudice = new JFrame();
        frameGiudice.setTitle("Area giudice " + loginGiudice);
        frameGiudice.setContentPane(mainPanel);
        frameGiudice.pack();
        frameGiudice.setSize(700, 700);
        frameGiudice.setLocationRelativeTo(null);
        frameGiudice.setDefaultCloseOperation(EXIT_ON_CLOSE);
        setupStile();
        setupProblema();
        setupDocumenti();
        setupListeners();
    }

    /**
     * Applica gli stili grafici a pannelli, bottoni e scrollbar.
     */
    private void setupStile() {
        if (mainPanel != null) mainPanel.setBackground(bgColor);
        if (panel != null) panel.setBackground(bgColor);

        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(bgColor);

        JButton[] allButtons = {back};
        for (JButton btn : allButtons) {
            if (btn == null) continue;
            btn.setBackground(btnColor);
            btn.setForeground(Color.WHITE);
            btn.setFont(labelFont);
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
            setButtonHover(btn, btnColor, btnHoverColor);
        }
    }

    /**
     * Imposta l'effetto hover per i bottoni.
     *
     * @param button bottone su cui applicare l'effetto
     * @param color colore normale del bottone
     * @param hoverColor colore del bottone durante l'hover
     */
    private void setButtonHover(JButton button, Color color, Color hoverColor) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
    }

    /**
     * Imposta la sezione della descrizione del problema dell'evento.
     * Se il giudice è responsabile, permette di caricare la descrizione;
     * altrimenti mostra la descrizione se già presente.
     */
    private void setupProblema() {
        String problema = controller.getProblemaEvento(eventoId);
        Giudice responsabile = controller.getGiudiceDescrizione(eventoId);

        if (responsabile != null && loginGiudice.equals(responsabile.getLogin())) {
            JLabel messaggio = new JLabel("Sei il giudice responsabile della descrizione del problema.");
            messaggio.setFont(labelFont);

            if (problema == null || problema.trim().isEmpty()) {
                JTextArea descrizioneProblema = new JTextArea(5, 20);
                descrizioneProblema.setFont(fieldFont);
                descrizioneProblema.setLineWrap(true);
                descrizioneProblema.setWrapStyleWord(true);

                JScrollPane scrollProblema = new JScrollPane(descrizioneProblema,
                        ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                scrollProblema.getVerticalScrollBar().setUnitIncrement(20);

                JButton carica = creaStyledButton("Carica problema");

                panel.add(Box.createVerticalStrut(10));
                panel.add(messaggio);
                panel.add(Box.createVerticalStrut(10));
                panel.add(scrollProblema);
                panel.add(Box.createVerticalStrut(8));
                panel.add(carica);

                carica.addActionListener(e -> {
                    String testo = descrizioneProblema.getText();
                    if (testo.isEmpty()) {
                        JOptionPane.showMessageDialog(frameGiudice, "Inserisci la descrizione del problema prima di effettuare il caricamento!", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        controller.setProblemaEvento(eventoId, testo);
                        JOptionPane.showMessageDialog(frameGiudice, "Descrizione del problema caricata con successo!");
                        panel.removeAll();
                        JLabel giaCaricata = new JLabel("<html><b>Problema:</b><br>" + testo + HTML_END);
                        panel.add(messaggio);
                        panel.add(giaCaricata);
                        panel.revalidate();
                        panel.repaint();
                    }
                });
            } else {
                JLabel giaCaricata = new JLabel("<html><b>Problema:</b><br>" + problema + HTML_END);
                giaCaricata.setFont(fieldFont);
                panel.add(Box.createVerticalStrut(10));
                panel.add(messaggio);
                panel.add(Box.createVerticalStrut(10));
                panel.add(giaCaricata);
            }
        } else if (problema != null && !problema.isEmpty()) {
            JLabel message = new JLabel("<html><b>Problema assegnato per l'evento:</b><br>" + problema + HTML_END);
            message.setFont(fieldFont);
            panel.add(Box.createVerticalStrut(10));
            panel.add(message);
        } else {
            JLabel noProblem = new JLabel("Nessun problema inserito per questo evento.");
            noProblem.setFont(fieldFont);
            panel.add(Box.createVerticalStrut(10));
            panel.add(noProblem);
        }
    }

    /**
     * Imposta la sezione documenti, mostrando i documenti caricati dai team e permettendo
     * la visualizzazione o la votazione in base allo stato dell'evento.
     */
    private void setupDocumenti() {
        List<Team> teams = controller.getTeamsEvento(eventoId);
        boolean almenoUno = false;
        if (teams != null && !teams.isEmpty()) {
            for (Team t : teams) {
                boolean haDocumenti = controller.teamHaDocumenti(t.getNomeTeam(), eventoId);
                if (haDocumenti) {
                    if (!almenoUno) {
                        JLabel documentiLabel = new JLabel("Documenti caricati dai team: ");
                        documentiLabel.setFont(labelFont);
                        panel.add(Box.createVerticalStrut(16));
                        panel.add(documentiLabel);
                        almenoUno = true;
                    }
                    aggiungiRigaTeam(t);
                }
            }
            if (!almenoUno) {
                JLabel noDocumenti = new JLabel("Nessun documento caricato dai team.");
                noDocumenti.setFont(fieldFont);
                panel.add(Box.createVerticalStrut(10));
                panel.add(noDocumenti);
            }
        }
    }

    /**
     * Aggiunge una riga per un team, permettendo la visualizzazione dei documenti
     * o la votazione in base allo stato dell'evento.
     *
     * @param t team da visualizzare o votare
     */
    private void aggiungiRigaTeam(Team t) {
        JLabel nomeTeam = new JLabel(t.getNomeTeam());
        nomeTeam.setFont(fieldFont);

        JButton visualizzaDocumenti = creaStyledButton("Visualizza documenti");

        JPanel rigaTeam = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rigaTeam.setOpaque(false);
        rigaTeam.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        rigaTeam.setAlignmentX(Component.LEFT_ALIGNMENT);

        Evento evento = controller.getEventoById(eventoId); // assicurati di avere questo metodo in Controller
        boolean eventoFinito = evento.getDataFine().isBefore(java.time.LocalDate.now());

        boolean haVotato = controller.giudiceHaVotatoTeam(loginGiudice, t.getNomeTeam(), eventoId);

        if (eventoFinito) {
            if (haVotato) {
                int votoAssegnato = controller.getVotoDiGiudiceTeam(loginGiudice, t.getNomeTeam(), eventoId);
                rigaTeam.add(Box.createHorizontalStrut(10));
                rigaTeam.add(nomeTeam);
                rigaTeam.add(Box.createHorizontalStrut(8));
                rigaTeam.add(new JLabel("Voto al team: " + votoAssegnato));
                panel.add(Box.createVerticalStrut(10));
                panel.add(rigaTeam);
            } else {
                JComboBox<Integer> voti = new JComboBox<>();
                for (int i = 0; i <= 10; i++) {
                    voti.addItem(i);
                }
                voti.setFont(fieldFont);
                voti.setBackground(Color.WHITE);
                voti.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(210, 210, 210), 1),
                        BorderFactory.createEmptyBorder(5, 8, 5, 8)
                ));
                voti.setPreferredSize(new Dimension(100, 32));
                voti.setMaximumSize(new Dimension(100, 32));
                voti.setFocusable(false);

                JButton confermaVoto = creaStyledButton("Conferma");

                rigaTeam.add(Box.createHorizontalStrut(10));
                rigaTeam.add(nomeTeam);
                rigaTeam.add(Box.createHorizontalStrut(8));
                rigaTeam.add(new JLabel("Voto: "));
                rigaTeam.add(Box.createHorizontalStrut(4));
                rigaTeam.add(voti);
                rigaTeam.add(Box.createHorizontalStrut(8));
                rigaTeam.add(confermaVoto);
                panel.add(Box.createVerticalStrut(10));
                panel.add(rigaTeam);

                confermaVoto.addActionListener(e -> {
                    int votoSelezionato = (int) voti.getSelectedItem();
                    controller.votaTeam(loginGiudice, t.getNomeTeam(), eventoId, votoSelezionato);
                    JOptionPane.showMessageDialog(frameGiudice, "Voto " + votoSelezionato + " assegnato con successo al team " + t.getNomeTeam());
                    voti.setVisible(false);
                    confermaVoto.setVisible(false);
                    JLabel votoAssegnato = new JLabel("" + votoSelezionato);
                    rigaTeam.add(votoAssegnato);
                    rigaTeam.revalidate();
                    rigaTeam.repaint();
                });
            }
        } else {
            rigaTeam.add(Box.createHorizontalStrut(10));
            rigaTeam.add(nomeTeam);
            rigaTeam.add(Box.createHorizontalStrut(8));
            rigaTeam.add(visualizzaDocumenti);
            panel.add(Box.createVerticalStrut(10));
            panel.add(rigaTeam);

            visualizzaDocumenti.addActionListener(e -> {
                VisualizzaDocumenti nuovaGUI = new VisualizzaDocumenti(controller, t.getNomeTeam(), eventoId, frameGiudice, loginGiudice);
                nuovaGUI.getFrameDocumenti().setVisible(true);
                frameGiudice.setVisible(false);
            });
        }
    }

    /**
     * Crea un bottone con stile personalizzato per la GUI.
     *
     * @param text testo da mostrare sul bottone
     * @return bottone creato con stile applicato
     */
    private JButton creaStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(btnColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font(FONT_FAMILY, Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        setButtonHover(btn, btnColor, btnHoverColor);
        return btn;
    }

    /**
     * Imposta i listener sui bottoni della GUI (al momento solo "back").
     */
    private void setupListeners() {
        back.addActionListener(e -> {
            frameGiudice.dispose();
            frameEventi.setVisible(true);
        });
    }

    /**
     * Restituisce il frame della GUI dell'area giudice.
     *
     * @return frame dell'area giudice
     */
    public JFrame getFrameGiudice() {
        return frameGiudice;
    }
}