package gui;

import controller.Controller;
import model.Evento;

import model.InvitoGiudice;

import javax.swing.*;
import java.awt.*;

import java.util.List;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/**
 * GUI per la visualizzazione e gestione degli inviti ricevuti dall'organizzatore come giudice di un evento Hackaton.
 * <p>
 * Permette all'utente di:
 * <ul>
 *   <li>Visualizzare l'elenco degli inviti pendenti ricevuti dagli organizzatori per eventi specifici</li>
 *   <li>Accettare o rifiutare ogni invito tramite appositi bottoni</li>
 *   <li>Ricevere feedback tramite messaggi di dialogo</li>
 *   <li>Tornare all'area eventi tramite il bottone "Indietro"</li>
 * </ul>
 * <p>
 * Gli stili grafici e le interazioni sono definiti per coerenza con il resto dell'applicazione.
 */
public class VediNotifica {
    /** Pannello principale della GUI notifiche. */
    private JPanel mainPanel;

    /** Pannello che contiene la lista degli inviti. */
    private JPanel panelInviti;

    /** Pannello per il bottone "Indietro". */
    private JPanel panelbottone;

    /** Bottone per tornare all'area eventi. */
    private JButton backButton;

    /** Login dell'utente attualmente loggato. */
    private String loginUtente;

    /** ScrollPane per la lista degli inviti. */
    private JScrollPane scroll;

    /** Controller logico dell'applicazione. */
    private Controller controller;

    /** Frame principale della schermata notifiche. */
    private JFrame frameNotifiche;

    /** Frame dell'area eventi per il ritorno. */
    private JFrame frameEventi;

    /** Frame dell'area giudice. */
    private JFrame frameGiudice;

    /** Frame dell'area accesso. */
    private JFrame frameAccesso;

    /** Frame dell'area partecipante. */
    private JFrame framePartecipante;

    /** Font di default per la GUI. */
    private static final String FONT_FAMILY = "SansSerif";

    /**
     * Costruisce la GUI per la visualizzazione degli inviti ricevuti, inizializza gli stili, listeners e aggiorna la lista degli inviti.
     * @param controller Controller logico dell'applicazione
     * @param loginUtente login dell'utente che consulta le notifiche
     * @param frameAreaEvento frame dell'area eventi per il ritorno
     * @param frameAreaGiudice frame dell'area giudice
     * @param frameAreaAccesso frame dell'area accesso
     * @param frameAreaPartecipante frame dell'area partecipante
     */
    public VediNotifica(Controller controller, String loginUtente, JFrame frameAreaEvento, JFrame frameAreaGiudice, JFrame frameAreaAccesso, JFrame frameAreaPartecipante) {
        this.controller = controller;
        this.loginUtente = loginUtente;
        frameEventi = frameAreaEvento;
        frameGiudice = frameAreaGiudice;
        frameAccesso = frameAreaAccesso;
        framePartecipante = frameAreaPartecipante;

        scroll.getVerticalScrollBar().setUnitIncrement(20);

        Color bgColor = new Color(240, 248, 255);      // chiaro azzurrino
        Color btnColor = new Color(30, 144, 255);
        Color btnHoverColor = new Color(65, 105, 225);
        Font labelFont = new Font(FONT_FAMILY, Font.BOLD, 16);

        if (mainPanel != null) mainPanel.setBackground(bgColor);
        if (panelInviti != null) panelInviti.setBackground(bgColor);
        if (panelbottone != null) panelbottone.setBackground(bgColor);

        scroll.getVerticalScrollBar().setUnitIncrement(20);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(bgColor);

        // Back button stile
        if (backButton != null) {
            backButton.setBackground(btnColor);
            backButton.setForeground(Color.WHITE);
            backButton.setFont(labelFont);
            backButton.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
            backButton.setFocusPainted(false);
            backButton.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    backButton.setBackground(btnHoverColor);
                }
                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    backButton.setBackground(btnColor);
                }
            });
        }

        frameNotifiche = new JFrame("Inviti ricevuti");
        frameNotifiche.setContentPane(mainPanel);
        frameNotifiche.pack();
        frameNotifiche.setSize(600, 600);
        frameNotifiche.setLocationRelativeTo(null);
        frameNotifiche.setDefaultCloseOperation(EXIT_ON_CLOSE);

        if(panelInviti != null) {
            panelInviti.setLayout(new BoxLayout(panelInviti, BoxLayout.Y_AXIS));
        }
        aggiornaInviti();

        backButton.addActionListener(e -> {
            frameNotifiche.setVisible(false);
            frameEventi.dispose();
            ViewEvento nuovo = new ViewEvento(controller, loginUtente, frameAccesso, framePartecipante, frameNotifiche, frameGiudice);
            nuovo.getFrameEventi().setVisible(true);
        });
    }

    /**
     * Aggiorna la lista degli inviti ricevuti dall'utente, mostrando i dettagli e i bottoni per accettare o rifiutare.
     */
    private void aggiornaInviti() {
        panelInviti.removeAll();
        Color btnColor = new Color(30, 144, 255);
        Color btnHoverColor = new Color(65, 105, 225);
        Font labelFont = new Font(FONT_FAMILY, Font.BOLD, 16);
        Font fieldFont = new Font(FONT_FAMILY, Font.PLAIN, 15);

        List<InvitoGiudice> inviti = controller.getInvitiPendentiUtente(loginUtente);

        if (inviti.isEmpty()) {
            JLabel nessunInvito = new JLabel("Nessun invito ricevuto");
            nessunInvito.setFont(labelFont);
            nessunInvito.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelInviti.add(Box.createVerticalStrut(15));
            panelInviti.add(nessunInvito);
        } else {
            for (InvitoGiudice invito : inviti) {
                Evento evento = invito.getEvento();
                JPanel riga = new JPanel(new FlowLayout(FlowLayout.LEFT,  10, 4));
                riga.setOpaque(false);
                riga.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
                riga.setAlignmentX(Component.LEFT_ALIGNMENT);
                JLabel infoLabel = new JLabel(
                        "<html>Sei stato invitato da <b>" + evento.getOrganizzatore().getLogin() + "</b> per l'evento <b>" + evento.getTitolo() + "</b></html>"
                );
                infoLabel.setFont(fieldFont);
                JButton accettabutton = new JButton("Accetta");
                JButton rifiutabutton = new JButton("Rifiuta");
                JButton[] buttons = {accettabutton, rifiutabutton};
                for (JButton btn : buttons) {
                    btn.setBackground(btnColor);
                    btn.setForeground(Color.WHITE);
                    btn.setFont(new Font(FONT_FAMILY, Font.BOLD, 13));
                    btn.setFocusPainted(false);
                    btn.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
                    btn.addMouseListener(new java.awt.event.MouseAdapter() {
                        @Override
                        public void mouseEntered(java.awt.event.MouseEvent evt) {
                            btn.setBackground(btnHoverColor);
                        }
                        @Override
                        public void mouseExited(java.awt.event.MouseEvent evt) {
                            btn.setBackground(btnColor);
                        }
                    });
                }
                riga.add(infoLabel);
                riga.add(accettabutton);
                riga.add(rifiutabutton);
                panelInviti.add(riga);

                panelInviti.add(Box.createVerticalStrut(5));



                accettabutton.addActionListener(e -> {
                    if(controller.accettaInvitoGiudice(invito.getId(), loginUtente)) {
                        JOptionPane.showMessageDialog(frameNotifiche, "Ora sei giudice dell'evento: " + evento.getTitolo());
                        aggiornaInviti();
                    }
                    aggiornaInviti();
                });

                rifiutabutton.addActionListener(e ->{
                    controller.rifiutaInvitoGiudice(invito.getId());
                    aggiornaInviti();
                });

            }
        }
        panelInviti.revalidate();
        panelInviti.repaint();
    }

    /**
     * Restituisce il frame della schermata notifiche.
     * @return frame della schermata notifiche
     */
    public JFrame getFrameNotifiche() {
        return frameNotifiche;
    }
}
