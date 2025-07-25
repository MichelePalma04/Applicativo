package gui;

import controller.Controller;
import model.*;

import javax.swing.*;
import java.awt.*;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

import java.io.File;
import java.io.IOException;

/**
 * GUI per la visualizzazione e gestione dei documenti di un team da parte di un giudice nell'applicazione Hackaton.
 * <p>
 * Permette al giudice di:
 * <ul>
 *   <li>Visualizzare la lista dei documenti caricati dal team per un evento</li>
 *   <li>Aprire i documenti direttamente tramite il pulsante "Visualizza"</li>
 *   <li>Aggiungere commenti ai documenti tramite il pulsante "Commenta"</li>
 *   <li>Ricevere feedback tramite messaggi di dialogo</li>
 *   <li>Tornare all'area personale del giudice</li>
 * </ul>
 * <p>
 * Gli stili grafici e le interazioni sono definiti per garantire coerenza con il resto dell'applicazione.
 */
public class VisualizzaDocumenti {
    /** Label per il titolo della sezione documenti del team. */
    private JLabel documentiTeam;

    /** Pannello principale della GUI documenti. */
    private JPanel mainpanel;

    /** ScrollPane per la lista dei documenti. */
    private JScrollPane scroll;

    /** Pannello che contiene le righe dei documenti. */
    private JPanel panelDocumenti;

    /** Bottone per tornare all'area giudice. */
    private JButton backButton;

    /** Frame principale della schermata documenti. */
    private JFrame frameDocumenti;

    /** Controller logico dell'applicazione. */
    private Controller controller;

    /** Frame dell'area giudice per il ritorno. */
    private JFrame frameGiudice;

    /** Login del giudice attualmente loggato. */
    private String loginGiudice;

    /** Font di default per la GUI. */
    private static final String FONT_FAMILY = "SansSerif";

    /**
     * Costruisce la GUI per la visualizzazione e commento dei documenti di un team, inizializza gli stili, listeners e popola la schermata.
     * @param controller Controller logico dell'applicazione
     * @param nomeTeam nome del team di cui visualizzare i documenti
     * @param eventoId identificativo dell'evento
     * @param frameAreaGiudice frame dell'area giudice per il ritorno
     * @param giudiceLogin login del giudice attualmente loggato
     */
    public VisualizzaDocumenti(Controller controller, String nomeTeam, int eventoId, JFrame frameAreaGiudice, String giudiceLogin) {
        this.controller = controller;
        this.loginGiudice = giudiceLogin;
        frameGiudice = frameAreaGiudice;

        Color bgColor = new Color(240, 248, 255);
        Color btnColor = new Color(30, 144, 255);
        Color btnHoverColor = new Color(65, 105, 225);
        Font labelFont = new Font(FONT_FAMILY, Font.BOLD, 16);
        Font fieldFont = new Font(FONT_FAMILY, Font.PLAIN, 15);

        frameDocumenti = new JFrame("Documenti");
        frameDocumenti.setContentPane(mainpanel);
        frameDocumenti.pack();
        frameDocumenti.setSize(700, 700);
        frameDocumenti.setLocationRelativeTo(null);
        frameDocumenti.setDefaultCloseOperation(EXIT_ON_CLOSE);


        if (mainpanel != null) mainpanel.setBackground(bgColor);
        if (panelDocumenti != null) panelDocumenti.setBackground(bgColor);


        documentiTeam.setText("Documenti del team: " + nomeTeam);
        documentiTeam.setFont(labelFont);
        documentiTeam.setHorizontalAlignment(SwingConstants.CENTER);

        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(bgColor);


        panelDocumenti.setLayout(new BoxLayout(panelDocumenti, BoxLayout.Y_AXIS));
        for(Documento doc: controller.getDocumentiTeamEvento(nomeTeam, eventoId)) {
            JPanel rigaDocumenti = new JPanel();
            rigaDocumenti.setLayout(new BoxLayout(rigaDocumenti, BoxLayout.X_AXIS));
            rigaDocumenti.setOpaque(false);
            rigaDocumenti.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
            rigaDocumenti.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel nomeDoc = new JLabel(doc.getFile().getName());
            nomeDoc.setFont(fieldFont);


            JButton visualizzaDoc = new JButton("Visualizza");
            JButton commentaDoc = new JButton("Commenta");
            JTextArea commentaDocText = new JTextArea(2, 20);
            commentaDocText.setFont(fieldFont);
            JScrollPane commentaDocScrollPane = new JScrollPane(commentaDocText, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            commentaDocScrollPane.getVerticalScrollBar().setUnitIncrement(20);
            commentaDocScrollPane.setPreferredSize(new Dimension(220, 36));
            commentaDocScrollPane.setMaximumSize(new Dimension(220, 36));
            commentaDocScrollPane.setMinimumSize(new Dimension(220, 36));

            JButton[] buttons = {visualizzaDoc, commentaDoc};
            for (JButton btn : buttons) {
                btn.setBackground(btnColor);
                btn.setForeground(Color.WHITE);
                btn.setFont(new Font(FONT_FAMILY, Font.BOLD, 13));
                btn.setFocusPainted(false);
                btn.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
                btn.setMaximumSize(new Dimension(120, 36));
                btn.setMinimumSize(new Dimension(120, 36));
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

            visualizzaDoc.addActionListener(e -> apriFile(doc.getFile()));

            commentaDoc.addActionListener(e -> {
                String commento = commentaDocText.getText();
                if (!commento.trim().isEmpty()) {
                    boolean ok = controller.aggiungiCommentoGiudice(doc.getId(), loginGiudice, eventoId, commento);
                    if (ok) {
                        JOptionPane.showMessageDialog(frameDocumenti, "Commento aggiunto con successo.");
                        commentaDocText.setText("");
                    } else {
                        JOptionPane.showMessageDialog(frameDocumenti, "Errore nel salvataggio del commento.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(frameDocumenti, "Il commento non può essere vuoto!", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            });

            rigaDocumenti.add(Box.createHorizontalStrut(10));
            rigaDocumenti.add(nomeDoc);
            rigaDocumenti.add(Box.createHorizontalStrut(8));
            rigaDocumenti.add(visualizzaDoc);
            rigaDocumenti.add(Box.createHorizontalStrut(8));
            rigaDocumenti.add(commentaDocScrollPane);
            rigaDocumenti.add(Box.createHorizontalStrut(8));
            rigaDocumenti.add(commentaDoc);
            rigaDocumenti.add(Box.createHorizontalStrut(10));

            panelDocumenti.add(Box.createVerticalStrut(8));
            panelDocumenti.add(rigaDocumenti);
        }

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

        backButton.addActionListener(e -> {
            frameDocumenti.setVisible(false);
            frameGiudice.setVisible(true);
        });
    }

    /**
     * Prova ad aprire il file specificato tramite il sistema operativo.
     * Mostra un messaggio di errore se il file non può essere aperto.
     *
     * @param f file da aprire
     */
    public void apriFile (File f){
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try{
                desktop.open(f);
            }catch(IOException ex){
                JOptionPane.showMessageDialog(frameDocumenti,"Impossibile aprire il file " + f.getName(), "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }else{
            JOptionPane.showMessageDialog(frameDocumenti, "Funzionalità non supportata dal sistema operativo.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Restituisce il frame della schermata documenti.
     * @return frame della schermata documenti
     */
    public JFrame getFrameDocumenti () {
        return frameDocumenti;
    }
}


