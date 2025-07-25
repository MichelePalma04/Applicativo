package gui;
import controller.Controller;
import model.Evento;
import model.Giudice;
import model.Utente;

import javax.swing.*;
import java.awt.*;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

import java.util.List;

/**
 * GUI per la gestione degli inviti ai giudici e dell'assegnazione del responsabile del problema
 * nell'applicazione Hackaton, dedicata all'organizzatore di un evento.
 * <p>
 * Permette all'organizzatore di:
 * <ul>
 *   <li>Visualizzare l'elenco degli utenti invitabili come giudici</li>
 *   <li>Invitare un utente come giudice tramite apposito bottone</li>
 *   <li>Visualizzare la lista dei giudici già invitati</li>
 *   <li>Assegnare il ruolo di responsabile della descrizione del problema ad un giudice</li>
 *   <li>Tornare alla schermata precedente</li>
 * </ul>
 * <p>
 * Tutti gli stili e le interazioni sono gestiti localmente per coerenza con il resto dell'applicazione.
 */
public class Invito {

    /** Pannello principale della GUI inviti. */
    private JPanel panel;

    /** ComboBox per la selezione dell'utente da invitare come giudice. */
    private JComboBox <Utente> comboBox1;

    /** Label per l'invito. */
    private JLabel invitoLabel;

    /** Bottone per invitare un utente come giudice. */
    private JButton invitaComeGiudiceButton;

    /** Pannello secondario della GUI. */
    private JPanel panel2;

    /** Label per il nome dell'evento. */
    private JLabel nomeEvento;

    /** Bottone per tornare alla schermata organizzatore. */
    private JButton homeButton;

    /** ScrollPane per la lista dei giudici. */
    private JScrollPane scrollgiudici;

    /** Pannello che contiene la lista dei giudici. */
    private JPanel panelGiudici;

    /** Frame principale della GUI invito. */
    private JFrame frameInvito;

    /** Frame dell'area organizzatore per il ritorno. */
    private JFrame frameOrganizzatore;

    /** Controller logico dell'applicazione. */
    private Controller controller;

    /** Font di default per la GUI. */
    private static final String FONT_FAMILY = "SansSerif";

    /** Identificativo dell'evento. */
    private int eventoID;

    /**
     * Costruisce la GUI per la gestione degli inviti dei giudici e dell'assegnazione del responsabile del problema.
     *
     * @param eventoID identificativo dell'evento gestito
     * @param frameAreaOrganizzatore frame dell'area organizzatore per il ritorno
     * @param controller controller logico dell'applicazione
     */
    public Invito(int eventoID, JFrame frameAreaOrganizzatore, Controller controller) {
        this.controller = controller;
        this.eventoID = eventoID;
        frameOrganizzatore = frameAreaOrganizzatore;
        frameInvito = new JFrame("Operazioni organizzatore");
        frameInvito.setContentPane(panel);
        frameInvito.pack();
        frameInvito.setSize(600, 600);
        frameInvito.setLocationRelativeTo(null);
        frameInvito.setDefaultCloseOperation(EXIT_ON_CLOSE);
        applicaStili();
        preparaEvento();
        preparaPanelGiudici();
        setupListeners();

        aggiornaListaInvitabili();
        frameInvito.setVisible(true);
    }

    /**
     * Aggiorna la lista degli utenti invitabili nella ComboBox.
     */
    private void aggiornaListaInvitabili() {
        comboBox1.removeAllItems();
        for (Utente u : controller.getUtentiInvitabili(eventoID)) {
            comboBox1.addItem(u);
        }
    }

    /**
     * Restituisce il frame della GUI invito.
     * @return frame della GUI invito
     */
    public JFrame getFrameInvito() {
        return frameInvito;
    }

    /**
     * Applica gli stili ai componenti della GUI per coerenza con il resto dell'applicazione.
     */
    private void applicaStili() {
        Color bgColor = new Color(240, 248, 255);
        Color btnColor = new Color(30, 144, 255);
        Color btnHoverColor = new Color(65, 105, 225);
        Color fieldBg = Color.WHITE;
        Color fieldBorder = new Color(210, 210, 210);
        Font labelFont = new Font(FONT_FAMILY, Font.BOLD, 16);
        Font fieldFont = new Font(FONT_FAMILY, Font.PLAIN, 15);

        if (panel != null) panel.setBackground(bgColor);
        if (panel2 != null) panel2.setBackground(bgColor);
        if (panelGiudici != null) panelGiudici.setBackground(bgColor);

        if (invitoLabel != null) invitoLabel.setFont(labelFont);
        if (nomeEvento != null) nomeEvento.setFont(labelFont);

        JButton[] allButtons = {invitaComeGiudiceButton, homeButton};
        for (JButton btn : allButtons) {
            if (btn == null) continue;
            btn.setBackground(btnColor);
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setFont(labelFont);
            btn.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        }
        if (invitaComeGiudiceButton != null) setButtonHover(invitaComeGiudiceButton, btnColor, btnHoverColor);
        if (homeButton != null) setButtonHover(homeButton, btnColor, btnHoverColor);

        if (comboBox1 != null) {
            comboBox1.setFont(fieldFont);
            comboBox1.setBackground(fieldBg);
            comboBox1.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(fieldBorder, 1),
                    BorderFactory.createEmptyBorder(5, 8, 5, 8)
            ));
        }

        if (scrollgiudici != null) {
            scrollgiudici.setBorder(BorderFactory.createEmptyBorder());
            scrollgiudici.getViewport().setBackground(bgColor);
        }
    }

    /**
     * Imposta l'effetto hover sui bottoni.
     * @param button bottone da modificare
     * @param normal colore normale del bottone
     * @param hover colore del bottone in hover
     */
    private void setButtonHover(JButton button, Color normal, Color hover) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hover);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(normal);
            }
        });
    }

    /**
     * Prepara i dati dell'evento, impostando il nome nella GUI.
     */
    private void preparaEvento() {
        Evento evento = controller.getEventoById(eventoID);
        if (nomeEvento != null && evento != null) {
            nomeEvento.setText("Evento " + evento.getTitolo());
        }
    }

    /**
     * Prepara il pannello dei giudici, visualizzando la lista dei giudici già invitati
     * e permettendo l'assegnazione del responsabile del problema.
     */
    private void preparaPanelGiudici() {
        Color bgColor = new Color(240, 248, 255);
        Color btnColor = new Color(30, 144, 255);
        Color btnHoverColor = new Color(65, 105, 225);
        Font fieldFont = new Font(FONT_FAMILY, Font.PLAIN, 15);

        if (panelGiudici == null) return;
        panelGiudici.setLayout(new BoxLayout(panelGiudici, BoxLayout.Y_AXIS));
        List<Giudice> giudici = controller.getGiudiciEvento(eventoID);
        Giudice responsabile = controller.getGiudiceDescrizione(eventoID);

        for (Giudice g : giudici) {
            JPanel riga = creaRigaGiudice(g, bgColor, btnColor, btnHoverColor, fieldFont, responsabile);
            panelGiudici.add(riga);
            panelGiudici.add(Box.createVerticalStrut(2));
        }
    }

    /**
     * Crea la riga di visualizzazione per un giudice, con bottone di assegnazione o label di ruolo.
     * @param g giudice da visualizzare
     * @param bgColor colore di sfondo
     * @param btnColor colore base bottone
     * @param btnHoverColor colore bottone in hover
     * @param fieldFont font da applicare
     * @param responsabile giudice responsabile del problema
     * @return JPanel rappresentante la riga del giudice
     */
    private JPanel creaRigaGiudice(Giudice g, Color bgColor, Color btnColor, Color btnHoverColor, Font fieldFont, Giudice responsabile) {
        JPanel riga = new JPanel();
        riga.setBackground(bgColor);
        riga.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 2));
        riga.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        riga.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel nomiGiudici = new JLabel(g.getLogin());
        nomiGiudici.setFont(fieldFont);

        JButton assegna = new JButton("Assegna come responsabile.");
        assegna.setBackground(btnColor);
        assegna.setForeground(Color.WHITE);
        assegna.setFocusPainted(false);
        assegna.setFont(new Font(FONT_FAMILY, Font.BOLD, 13));
        assegna.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        setButtonHover(assegna, btnColor, btnHoverColor);

        JLabel ruolo = new JLabel("Ruolo: responsabile del problema");
        ruolo.setFont(fieldFont);
        ruolo.setForeground(new Color(30, 120, 30));
        ruolo.setVisible(false);

        if (responsabile != null && responsabile.getLogin().equals(g.getLogin())) {
            assegna.setVisible(false);
            ruolo.setVisible(true);
        } else if (responsabile != null) {
            assegna.setVisible(false);
        } else {
            assegna.addActionListener(e -> assegnaResponsabile(g, ruolo));
        }

        riga.add(nomiGiudici);
        riga.add(ruolo);
        riga.add(assegna);
        return riga;
    }

    /**
     * Assegna il ruolo di responsabile della descrizione del problema al giudice selezionato,
     * aggiornando la GUI e mostrando un messaggio di feedback.
     * @param g giudice da assegnare
     * @param ruolo label del ruolo da mostrare
     */
    private void assegnaResponsabile(Giudice g, JLabel ruolo) {
        boolean ok = controller.assegnaGiudiceDescrizione(eventoID, g.getLogin());
        if (!ok) {
            JOptionPane.showMessageDialog(frameInvito, "Errore nell'assegnazione del responsabile.");
            return;
        }

        for (Component comp : panelGiudici.getComponents()) {
            if (!(comp instanceof JPanel rigaPanel)) continue;
            for (Component c : rigaPanel.getComponents()) {
                if (c instanceof JButton) {
                    c.setVisible(false);
                } else if (c instanceof JLabel jlabel && "Ruolo: responsabile del problema".equals(jlabel.getText())) {
                    jlabel.setVisible(false);
                }
            }
        }
        ruolo.setVisible(true);
        JOptionPane.showMessageDialog(frameInvito, g.getLogin() + " è stato assegnato come giudice responsabile della descrizione del problema.");
    }

    /**
     * Imposta i listener principali sui bottoni della GUI.
     */
    private void setupListeners() {
        if (homeButton != null) {
            homeButton.addActionListener(e -> {
                frameInvito.setVisible(false);
                frameOrganizzatore.setVisible(true);
            });
        }

        if (invitaComeGiudiceButton != null) {
            invitaComeGiudiceButton.addActionListener(e -> invitaGiudice());
        }
    }

    /**
     * Invita l'utente selezionato come giudice per l'evento.
     * Aggiorna la lista degli invitabili e mostra un messaggio di feedback.
     */
    private void invitaGiudice() {
        Utente daInvitare = (Utente) comboBox1.getSelectedItem();
        if (daInvitare != null) {
            if (controller.invitaGiudicePendente(eventoID, daInvitare.getLogin())) {
                JOptionPane.showMessageDialog(frameInvito, "Giudice invitato con successo!");
                aggiornaListaInvitabili();
            } else {
                JOptionPane.showMessageDialog(frameInvito, "Errore, impossibile invitare l'utente");
            }
        }
    }

}
