package gui;

import controller.Controller;
import model.Evento;
import model.Organizzatore;

import javax.swing.*;
import java.awt.*;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

import java.util.List;

/**
 * GUI dedicata all'organizzatore all'interno dell'applicazione Hackaton.
 * <p>
 * Consente all'organizzatore di:
 * <ul>
 *   <li>Visualizzare il benvenuto personalizzato con il proprio login</li>
 *   <li>Visualizzare la lista degli eventi di cui è responsabile tramite card interattive</li>
 *   <li>Accedere ai dettagli di ciascun evento e gestire gli inviti ai giudici</li>
 *   <li>Creare nuovi eventi</li>
 *   <li>Effettuare il logout e tornare all'area di accesso</li>
 * </ul>
 * <p>
 * Gli stili grafici e le interazioni sono gestiti localmente,
 * con effetti hover e layout responsivi per una migliore esperienza utente.
 */
public class AreaOrganizzatore {
    /** Pannello principale della GUI organizzatore. */
    private JPanel panel;

    /** Label di benvenuto con login dell'organizzatore. */
    private JLabel benvenuto;

    /** Pannello che contiene le card degli eventi. */
    private JPanel panelEventi;

    /** ScrollPane per scorrere tra gli eventi. */
    private JScrollPane scroll;

    /** Bottone per effettuare il logout. */
    private JButton logOutButton;

    /** Bottone per accedere alla creazione di nuovi eventi. */
    private JButton creaEventiButton;

    /** Frame principale dell'area organizzatore. */
    private JFrame frameOrganizzatore;

    /** Frame dell'area accessi (per tornare indietro). */
    private JFrame frameAccessi;

    /** Frame dell'area inviti (per gestire inviti ai giudici). */
    private JFrame frameInviti;

    /** Controller logico dell'applicazione. */
    private Controller controller;

    /** Nome del font da utilizzare per le componenti grafiche. */
    private static final String FONT_FAMILY = "SansSerif";

    /**
     * Costruisce la GUI dell'area organizzatore, inizializzando tutti i componenti grafici e logici.
     *
     * @param controller Controller dell'applicazione per la gestione logica
     * @param organizzatore Organizzatore attualmente loggato
     * @param frameAreaInviti Frame dell'area inviti (per la gestione inviti ai giudici)
     * @param frameAreaAccesso Frame dell'area accessi (per tornare al login)
     */
    public AreaOrganizzatore(Controller controller, Organizzatore organizzatore, JFrame frameAreaInviti, JFrame frameAreaAccesso) {
        this.controller = controller;
        frameInviti = frameAreaInviti;
        frameAccessi = frameAreaAccesso;

        Color bgColor = new Color(240, 248, 255);
        Color cardColor = new Color(225, 235, 245);
        Color btnColor = new Color(30, 144, 255);
        Color borderColor = new Color(210, 210, 210);
        Color btnHoverColor = new Color(65, 105, 225);
        Font labelFont = new Font(FONT_FAMILY, Font.BOLD, 16);
        Font fieldFont = new Font(FONT_FAMILY, Font.PLAIN, 15);

        panel.setBackground(bgColor);
        benvenuto.setFont(labelFont);

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

        List<Evento> eventi = controller.getEventiOrganizzatore(organizzatore.getLogin());
        for (Evento evento : eventi) {
            JPanel eventoCard = new JPanel(new BorderLayout());
            eventoCard.setBackground(cardColor);
            eventoCard.setBorder(BorderFactory.createLineBorder(borderColor, 1));
            eventoCard.setPreferredSize(new Dimension(220, 150));

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
            eventoCard.add(Box.createVerticalGlue(), BorderLayout.CENTER);
            eventoCard.add(infoButton, BorderLayout.SOUTH);

            panelEventi.add(eventoCard);
        }

        logOutButton.addActionListener(e -> {
            frameOrganizzatore.setVisible(false);
            frameAccessi.setVisible(true);

        });

        creaEventiButton.addActionListener(e -> {
            CreazioneEventi nuovaGUI = new CreazioneEventi(controller, organizzatore, frameAccessi, frameInviti);
            nuovaGUI.getFrameCreazioneEventi().setVisible(true);
            frameOrganizzatore.setVisible(false);
        });
    }

    /**
     * Restituisce il frame dell'area organizzatore.
     *
     * @return frame dell'area organizzatore
     */
    public JFrame getFrameOrganizzatore() {
        return frameOrganizzatore;
    }
}
