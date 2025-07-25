package gui;

import controller.Controller;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;

import java.util.List;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/**
        * GUI dedicata all'area partecipante dell'applicazione Hackaton.
        * <p>
 * Permette al partecipante di:
        * <ul>
 *   <li>Visualizzare il benvenuto e lo stato dell'iscrizione</li>
        *   <li>Unirsi a un team esistente oppure crearne uno nuovo</li>
        *   <li>Visualizzare il problema da risolvere per l'evento</li>
        *   <li>Caricare documenti e visualizzare quelli da lui già caricati/li>
        *   <li>Visualizzare eventuali commenti dei giudici sui documenti caricati</li>
        *   <li>Tornare alla schermata principale o di accesso</li>
        * </ul>
        * <p>
 * Gli stili e le interazioni sono gestiti localmente, con effetti hover, layout responsivo e messaggi di feedback per garantire una buona esperienza utente.
 */
public class AreaPartecipante {
    /** Label di avviso per l'iscrizione. */
    private JLabel avviso;

    /** Bottone per tornare alla home. */
    private JButton homeButton;

    /** ComboBox per la selezione di un team a cui unirsi. */
    private JComboBox <Team> comboBox1;

    /** Bottone per unirsi al team selezionato. */
    private JButton uniscitiButton;

    /** Label di benvenuto per il partecipante. */
    private JLabel benvenuto;

    /** Label per la sezione team. */
    private JLabel teamLabel;

    /** Pannello principale della GUI. */
    private JPanel panel;

    /** Label di messaggio per lo stato del team. */
    private JLabel messaggio;

    /** Bottone per sfogliare i documenti da caricare. */
    private JButton sfogliaDocumenti;

    /** Label per la sezione di caricamento documento. */
    private JLabel inserisciDocumento;

    /** Pannello secondario per la gestione dei documenti. */
    private JPanel panel2;

    /** Label del problema da risolvere. */
    private JLabel problema;

    /** Bottone per la creazione di un nuovo team. */
    private JButton creaTeamButton;

    /** Campo di testo per il nome del team da creare. */
    private JTextField nomeField;

    /** Label per il nome del team. */
    private JLabel nomeTeam;

    /** Controller logico dell'applicazione. */
    private Controller controller;

    /** Frame principale dell'area partecipante. */
    private JFrame frameAreaPartecipante;

    /** Frame della schermata eventi. */
    private JFrame frameEventi;

    /** Frame della schermata di accesso. */
    private JFrame frameAccedi;

    /** Frame della schermata notifiche. */
    private JFrame frameNotifica;

    /** Frame della schermata giudice. */
    private JFrame frameGiudice;

    /** Login del partecipante attualmente loggato. */
    private String loginPartecipante;

    /** Identificativo dell'evento. */
    private int eventoId;

    /** Font usato per tutti i testi e label della GUI. */
    private static final String FONT_FAMILY = "SansSerif";

    /** Label per la sezione problema da risolvere. */
    private static final String PROBLEMA_LABEL = "Problema da risolvere: ";

    /** Label per la visualizzazione di un documento caricato. */
    private static final String DOCUMENTO_CARICATO_LABEL = "Documento caricato: ";

    /** Messaggio mostrato se il team non ha caricato documenti. */
    private static final String NESSUN_DOCUMENTO_LABEL = "Nessun documento caricato dal tuo team.";

    /** Messaggio mostrato se non ci sono commenti dei giudici. */
    private static final String COMMENTO_GIUDICE_LABEL = "Nessun commento dai giudici.";

    /** Colore di sfondo principale della GUI (azzurrino chiaro). */
    private Color bgColor = new Color(240, 248, 255);

    /** Colore base dei bottoni. */
    private Color btnColor = new Color(30, 144, 255);

    /** Colore dei bottoni in hover (quando il mouse passa sopra). */
    private Color btnHoverColor = new Color(65, 105, 225);

    /** Colore di sfondo dei campi input. */
    private Color fieldBg = Color.WHITE;

    /** Colore del bordo dei campi input. */
    private Color fieldBorder = new Color(210, 210, 210);

    /** Font delle label principali. */
    private Font labelFont = new Font(FONT_FAMILY, Font.BOLD, 16);

    /** Font dei campi di input e dei messaggi. */
    private Font fieldFont = new Font(FONT_FAMILY, Font.PLAIN, 15);


    /**
     * Costruisce la GUI dell'area partecipante, inizializzando tutti i componenti grafici e logici.
     *
     * @param partecipanteLogin login del partecipante
     * @param idEvento id dell'evento associato
     * @param frameAreaEventi frame della schermata degli eventi
     * @param frameAreaAccesso frame della schermata di accesso
     * @param frameAreaNotifiche frame della schermata notifiche
     * @param frameAreaGiudice frame della schermata giudice
     * @param controller Controller logico dell'applicazione
     */
    public AreaPartecipante(String partecipanteLogin, int idEvento, JFrame frameAreaEventi, JFrame frameAreaAccesso, JFrame frameAreaNotifiche, JFrame frameAreaGiudice, Controller controller) {
        this.controller = controller;
        this.loginPartecipante = partecipanteLogin;
        this.eventoId = idEvento;
        frameEventi = frameAreaEventi;
        frameAccedi = frameAreaAccesso;
        frameNotifica = frameAreaNotifiche;
        frameGiudice = frameAreaGiudice;

        setupStileComponenti();
        setupFrame();

        List<Team> teams = controller.getTeamsEvento(eventoId);
        caricaTeamComboBox(teams);

        Team teamUtente = trovaTeamUtente(teams);
        boolean inTeam = teamUtente != null;

        JPanel documentiPanel = creaDocumentiPanel();
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
        panel2.add(documentiPanel);

        aggiornaVisibilitaCampi(inTeam, teamUtente);

        if (inTeam) {
            mostraDocumenti(documentiPanel, teamUtente);
        }

        creaTeamButton.addActionListener(e -> azioneCreaTeam(documentiPanel));
        homeButton.addActionListener(e -> tornaHome());
        uniscitiButton.addActionListener(e -> azioneUniscitiTeam(documentiPanel));
        sfogliaDocumenti.addActionListener(e -> azioneCaricaDocumento(documentiPanel));
    }

    /**
     * Applica gli stili ai componenti della GUI.
     */
    private void setupStileComponenti() {
        setPanelBg(panel, bgColor);
        setPanelBg(panel2, bgColor);

        setLabelFont(benvenuto, labelFont);
        setLabelFont(teamLabel, labelFont);
        setLabelFont(avviso, labelFont);
        setLabelFont(messaggio, fieldFont);
        setLabelFont(problema, labelFont);
        setLabelFont(nomeTeam, labelFont);
        setLabelFont(inserisciDocumento, fieldFont);

        JButton[] allButtons = {homeButton, uniscitiButton, sfogliaDocumenti, creaTeamButton};
        for (JButton btn : allButtons) {
            if (btn != null) styleButton(btn);
        }

        styleField(comboBox1, fieldFont, fieldBg, fieldBorder);
        styleField(nomeField, fieldFont, fieldBg, fieldBorder);
    }

    /**
     * Imposta il colore di sfondo di un pannello.
     * @param p pannello da modificare
     * @param color colore di sfondo
     */
    private void setPanelBg(JPanel p, Color color) {
        if (p != null) p.setBackground(color);
    }

    /**
     * Imposta il font di una label.
     * @param l label da modificare
     * @param font font da applicare
     */
    private void setLabelFont(JLabel l, Font font) {
        if (l != null) l.setFont(font);
    }

    /**
     * Applica lo stile ai bottoni.
     * @param btn bottone da stilizzare
     */
    private void styleButton(JButton btn) {
        btn.setBackground(btnColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(labelFont);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) { btn.setBackground(btnHoverColor);}
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) { btn.setBackground(btnColor);}
        });
    }

    /**
     * Applica lo stile ai campi di testo e combo box.
     * @param f componente da stilizzare
     * @param font font da applicare
     * @param bg colore di sfondo
     * @param borderColor colore bordo
     */
    private void styleField(JComponent f, Font font, Color bg, Color borderColor) {
        if (f != null) {
            f.setFont(font);
            f.setBackground(bg);
            f.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(borderColor, 1),
                    BorderFactory.createEmptyBorder(5, 8, 5, 8)
            ));
        }
    }


    /**
     * Inizializza il frame dell'area partecipante.
     */
    private void setupFrame() {
        frameAreaPartecipante = new JFrame("Area Personale " + loginPartecipante);
        frameAreaPartecipante.setContentPane(panel);
        frameAreaPartecipante.pack();
        frameAreaPartecipante.setSize(600, 600);
        frameAreaPartecipante.setLocationRelativeTo(null);
        frameAreaPartecipante.setDefaultCloseOperation(EXIT_ON_CLOSE);

        messaggio.setVisible(false);
        avviso.setText("Iscrizione avvenuta con successo!");
        benvenuto.setText("Benvenuto, " + loginPartecipante);
    }

    /**
     * Carica i team nella combo box.
     * @param teams lista dei team disponibili
     */
    private void caricaTeamComboBox(List<Team> teams) {
        if (teams.isEmpty()) {
            uniscitiButton.setEnabled(false);
            comboBox1.setEnabled(false);
            teamLabel.setText("Nessun team disponibile a cui unirsi.");
        } else {
            for (Team team : teams) {
                comboBox1.addItem(team);
            }
        }
    }

    /**
     * Trova il team del partecipante, se presente.
     * @param teams lista dei team
     * @return il team del partecipante, oppure null se non è in nessun team
     */
    private Team trovaTeamUtente(List<Team> teams) {
        for (Team team : teams) {
            if (controller.isPartecipanteInTeam(loginPartecipante, team.getNomeTeam(), eventoId)) {
                return team;
            }
        }
        return null;
    }

    /**
     * Crea il pannello per la visualizzazione dei documenti.
     * @return pannello documenti
     */
    private JPanel creaDocumentiPanel() {
        JPanel documentiPanel = new JPanel();
        documentiPanel.setBackground(bgColor);
        documentiPanel.setLayout(new BoxLayout(documentiPanel, BoxLayout.Y_AXIS));
        return documentiPanel;
    }

    /**
     * Aggiorna la visibilità dei campi in base alla presenza o meno di un team.
     * @param inTeam true se il partecipante è in un team
     * @param teamUtente team del partecipante
     */
    private void aggiornaVisibilitaCampi(boolean inTeam, Team teamUtente) {
        if (inTeam) {
            setVisibilitaCampiTeam(true, teamUtente);
        } else {
            setVisibilitaCampiTeam(false, null);
        }
    }

    /**
     * Imposta la visibilità dei campi collegati al team.
     * @param inTeam true se il partecipante è in un team
     * @param teamUtente team del partecipante
     */
    private void setVisibilitaCampiTeam(boolean inTeam, Team teamUtente) {
        creaTeamButton.setVisible(!inTeam);
        nomeField.setVisible(!inTeam);
        nomeTeam.setVisible(!inTeam);
        uniscitiButton.setVisible(!inTeam);
        comboBox1.setVisible(!inTeam);
        teamLabel.setVisible(!inTeam);
        avviso.setVisible(!inTeam);
        messaggio.setVisible(inTeam);
        problema.setVisible(false);
        sfogliaDocumenti.setVisible(false);
        inserisciDocumento.setVisible(false);

        if (inTeam && teamUtente != null) {
            messaggio.setText("Ora sei membro del " + teamUtente.getNomeTeam());
            String problemaEventoInTeam = controller.getProblemaEvento(eventoId);
            if (problemaEventoInTeam != null && !problemaEventoInTeam.isEmpty()) {
                problema.setText(PROBLEMA_LABEL + problemaEventoInTeam);
                problema.setVisible(true);
                sfogliaDocumenti.setVisible(true);
                inserisciDocumento.setVisible(true);
            }
        }
    }

    /**
     * Mostra i documenti caricati dal partecipante.
     * @param documentiPanel pannello documenti su cui mostrare i documenti
     * @param teamUtente team dell'utente
     */
    private void mostraDocumenti(JPanel documentiPanel, Team teamUtente) {
        documentiPanel.removeAll();
        List<Documento> documenti = controller.getDocumentiTeamEventoPartecipante(eventoId, teamUtente.getNomeTeam(), loginPartecipante);
        if (documenti.isEmpty()) {
            aggiungiLabel(documentiPanel, NESSUN_DOCUMENTO_LABEL, fieldFont);
        } else {
            for (Documento doc : documenti) {
                documentiPanel.add(creaDocPanel(doc));
                documentiPanel.add(Box.createVerticalStrut(8));
            }
        }
        documentiPanel.revalidate();
        documentiPanel.repaint();
    }


    /**
     * Crea un pannello per un singolo documento, con eventuali commenti dei giudici.
     * @param doc documento da visualizzare
     * @return pannello documento
     */
    private JPanel creaDocPanel(Documento doc) {
        JPanel docPanel = new JPanel();
        docPanel.setLayout(new BoxLayout(docPanel, BoxLayout.Y_AXIS));
        docPanel.setBackground(bgColor);
        docPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel rigaPanel = new JPanel();
        rigaPanel.setLayout(new BoxLayout(rigaPanel, BoxLayout.X_AXIS));
        rigaPanel.setBackground(bgColor);
        rigaPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel nomeDocLabel = new JLabel(DOCUMENTO_CARICATO_LABEL + doc.getFile().getName());
        nomeDocLabel.setFont(fieldFont);

        JButton visualizzaDocButton = creaVisualizzaDocButton(doc);

        rigaPanel.add(nomeDocLabel);
        rigaPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        rigaPanel.add(visualizzaDocButton);

        docPanel.add(rigaPanel);
        docPanel.add(creaCommentiPanel(doc.getCommentiGiudici()));
        return docPanel;
    }

    /**
     * Crea il bottone per visualizzare un documento.
     * @param doc documento da visualizzare
     * @return bottone visualizzazione
     */
    private JButton creaVisualizzaDocButton(Documento doc) {
        JButton visualizzaDocButton = new JButton("Visualizza");
        visualizzaDocButton.setBackground(btnColor);
        visualizzaDocButton.setForeground(Color.WHITE);
        visualizzaDocButton.setFont(new Font(FONT_FAMILY, Font.BOLD, 13));
        visualizzaDocButton.setFocusPainted(false);
        visualizzaDocButton.setBorder(BorderFactory.createEmptyBorder(6, 18, 6, 18));
        visualizzaDocButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) { visualizzaDocButton.setBackground(btnHoverColor); }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) { visualizzaDocButton.setBackground(btnColor); }
        });
        visualizzaDocButton.addActionListener(e -> {
            try {
                Desktop.getDesktop().open(doc.getFile());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frameAreaPartecipante, "Impossibile aprire il documento.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });
        return visualizzaDocButton;
    }

    /**
     * Crea il pannello per i commenti dei giudici.
     * @param commenti lista dei commenti dei giudici
     * @return pannello commenti
     */
    private JPanel creaCommentiPanel(List<CommentoGiudice> commenti) {
        JPanel commentiPanel = new JPanel();
        commentiPanel.setLayout(new BoxLayout(commentiPanel, BoxLayout.Y_AXIS));
        commentiPanel.setBackground(bgColor);
        commentiPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        if (commenti == null || commenti.isEmpty()) {
            aggiungiLabel(commentiPanel, COMMENTO_GIUDICE_LABEL, fieldFont);
        } else {
            for (CommentoGiudice c : commenti) {
                aggiungiLabel(commentiPanel, c.getGiudice() + ": " + c.getTesto(), fieldFont);
            }
        }
        return commentiPanel;
    }

    /**
     * Aggiunge una label al pannello specificato.
     * @param panel pannello di destinazione
     * @param text testo della label
     * @param font font da applicare
     */
    private void aggiungiLabel(JPanel panel, String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
    }


    /**
     * Azione per la creazione di un nuovo team.
     * @param documentiPanel pannello documenti da aggiornare dopo la creazione
     */
    private void azioneCreaTeam(JPanel documentiPanel) {
        String nome = nomeField.getText();
        if (nome.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frameAreaPartecipante, "Inserire un nome valido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Team nuovoTeam = controller.creaTeam(nome, loginPartecipante, eventoId);
        controller.unisciPartecipanteATeam(loginPartecipante, nuovoTeam.getNomeTeam(), eventoId);
        comboBox1.addItem(nuovoTeam);

        Team nuovoTeamUtente = trovaTeamUtente(controller.getTeamsEvento(eventoId));
        aggiornaVisibilitaCampi(true, nuovoTeamUtente);
        mostraDocumenti(documentiPanel, nuovoTeamUtente);
    }

    /**
     * Azione per tornare alla schermata principale.
     */
    private void tornaHome() {
        ViewEvento gui = new ViewEvento(controller, loginPartecipante, frameAccedi, frameAreaPartecipante, frameNotifica, frameGiudice);
        gui.getFramePartecipante().setVisible(false);
        frameEventi.setVisible(true);
    }

    /**
     * Azione per unirsi al team selezionato.
     * @param documentiPanel pannello documenti da aggiornare dopo l'unione
     */
    private void azioneUniscitiTeam(JPanel documentiPanel) {
        Team teamSelected = (Team) comboBox1.getSelectedItem();
        if (controller.isPartecipanteInTeam(loginPartecipante, teamSelected.getNomeTeam(), eventoId)) {
            JOptionPane.showMessageDialog(frameAreaPartecipante, "Sei già in un team.");
            return;
        }
        boolean successo = controller.unisciPartecipanteATeam(loginPartecipante, teamSelected.getNomeTeam(), eventoId);
        if (!successo) {
            JOptionPane.showMessageDialog(
                    frameAreaPartecipante, "Il team " + teamSelected.getNomeTeam() + " è pieno!\nPuoi unirti ad un altro team oppure crearne uno nuovo.", "Team pieno", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Team nuovoTeamUtente = trovaTeamUtente(controller.getTeamsEvento(eventoId));
        aggiornaVisibilitaCampi(true, nuovoTeamUtente);
        mostraDocumenti(documentiPanel, nuovoTeamUtente);
    }

    /**
     * Azione per caricare un documento da parte del partecipante.
     * @param documentiPanel pannello documenti da aggiornare dopo il caricamento
     */
    private void azioneCaricaDocumento(JPanel documentiPanel) {
        JFileChooser fileChooser = new JFileChooser();
        int risultato = fileChooser.showOpenDialog(frameAreaPartecipante);
        if (risultato == JFileChooser.APPROVE_OPTION) {
            java.io.File file = fileChooser.getSelectedFile();

            Team teamUser = trovaTeamUtente(controller.getTeamsEvento(eventoId));
            Documento documento = new Documento(LocalDate.now(), file, teamUser);
            controller.caricaDocumento(documento, teamUser.getNomeTeam(), eventoId, loginPartecipante);
            JOptionPane.showMessageDialog(frameAreaPartecipante, "Documento caricato con successo!");

            mostraDocumenti(documentiPanel, teamUser);
        }
    }

    /**
     * Restituisce il frame dell'area partecipante.
     * @return frame dell'area partecipante
     */
    public JFrame getFramePartecipante() {
        return frameAreaPartecipante;
    }
}
