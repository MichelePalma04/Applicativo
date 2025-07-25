package gui;

import controller.Controller;
import model.Evento;
import model.Organizzatore;

import javax.swing.*;
import java.awt.*;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * GUI per la creazione di un nuovo evento da parte di un organizzatore nell'applicazione Hackaton.
 * <p>
 * Permette all'organizzatore di:
 * <ul>
 *   <li>Inserire titolo, sede, date e parametri principali dell'evento tramite form</li>
 *   <li>Effettuare controlli di validità sui dati inseriti</li>
 *   <li>Creare l'evento, con feedback di successo o errore</li>
 *   <li>Tornare all'area organizzatore tramite pulsante dedicato</li>
 * </ul>
 * <p>
 * Gli stili e le interazioni sono coerenti con il resto dell'applicazione per garantire una user experience uniforme.
 */
public class CreazioneEventi {
    /** Pannello principale della GUI creazione eventi. */
    private JPanel mainPanel;

    /** Label di benvenuto. */
    private JLabel benvenuto;

    /** Campo di testo per il titolo dell'evento. */
    private JTextField titoloField;

    /** Campo di testo per la sede dell'evento. */
    private JTextField sedeField;

    /** Bottone per creare l'evento. */
    private JButton creaEventoButton;

    /** Campo di testo per la data di inizio evento. */
    private JTextField dataInizioField;

    /** Campo di testo per la data di fine evento. */
    private JTextField dataFineField;

    /** Campo di testo per il massimo numero di iscritti. */
    private JTextField maxIscrittiField;

    /** Campo di testo per la dimensione massima del team. */
    private JTextField dimensioneField;

    /** Campo di testo per la data di inizio iscrizioni. */
    private JTextField inizoRegField;

    /** Campo di testo per la data di fine iscrizioni. */
    private JTextField fineRegField;

    /** Label per il titolo. */
    private JLabel titoloLabel;

    /** Label per la sede. */
    private JLabel sedeLabel;

    /** Label per la data di inizio. */
    private JLabel dataInizioLabel;

    /** Label per la data di fine. */
    private JLabel dataFineLabel;

    /** Label per max iscritti. */
    private JLabel maxIscrittiLabel;

    /** Label per dimensione massima team. */
    private JLabel dimMaxLabel;

    /** Label per inizio iscrizioni. */
    private JLabel inizioRegLabel;

    /** Label per fine iscrizioni. */
    private JLabel fineRegLabel;

    /** Bottone per tornare indietro all'area organizzatore. */
    private JButton backButton;

    /** Controller logico dell'applicazione. */
    private Controller controller;

    /** Frame principale della creazione eventi. */
    private JFrame frameCreazioneEventi;

    /** Frame dell'area accesso (per tornare indietro). */
    private JFrame frameAccesso;

    /** Frame dell'area inviti. */
    private JFrame frameInviti;

    /** Organizzatore che sta creando l'evento. */
    private Organizzatore organizzatore;

    /**
     * Costruisce la GUI per la creazione di eventi, inizializzando tutti i componenti grafici e logici.
     * @param controller Controller logico dell'applicazione
     * @param organizzatoreEvento Organizzatore che crea l'evento
     * @param frameAreaAccesso frame della schermata di accesso
     * @param frameAreaInvito frame della schermata di inviti
     */
    public CreazioneEventi(Controller controller, Organizzatore organizzatoreEvento, JFrame frameAreaAccesso, JFrame frameAreaInvito) {
        this.controller = controller;
        this.organizzatore = organizzatoreEvento;
        frameAccesso = frameAreaAccesso;
        frameInviti = frameAreaInvito;
        frameCreazioneEventi = new JFrame("Creazione Eventi");
        frameCreazioneEventi.pack();
        frameCreazioneEventi.setSize(600, 600);
        frameCreazioneEventi.setContentPane(mainPanel);
        frameCreazioneEventi.setLocationRelativeTo(null);
        frameCreazioneEventi.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frameCreazioneEventi.setVisible(true);

        Color bgColor = new Color(240, 248, 255);
        Color btnColor = new Color(30, 144, 255);
        Color btnHoverColor = new Color(65, 105, 225);
        Color fieldBg = Color.WHITE;
        Color fieldBorder = new Color(210, 210, 210);
        Font labelFont = new Font("SansSerif", Font.BOLD, 16);
        Font fieldFont = new Font("SansSerif", Font.PLAIN, 15);

        mainPanel.setBackground(bgColor);

        benvenuto.setFont(labelFont);
        titoloLabel.setFont(labelFont);
        sedeLabel.setFont(labelFont);
        dataInizioLabel.setFont(labelFont);
        dataFineLabel.setFont(labelFont);
        maxIscrittiLabel.setFont(labelFont);
        dimMaxLabel.setFont(labelFont);
        inizioRegLabel.setFont(labelFont);
        fineRegLabel.setFont(labelFont);

        JTextField[] allFields = {titoloField, sedeField, dataInizioField, dataFineField, maxIscrittiField, dimensioneField, inizoRegField, fineRegField};
        for (JTextField field : allFields) {
            field.setFont(fieldFont);
            field.setBackground(fieldBg);
            field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(fieldBorder, 1),
                    BorderFactory.createEmptyBorder(8, 8, 8, 8)
            ));
        }

        JButton[] allButtons = {creaEventoButton, backButton};
        for (JButton btn : allButtons) {
            btn.setBackground(btnColor);
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setFont(labelFont);
            btn.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        }

        creaEventoButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                creaEventoButton.setBackground(btnHoverColor);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                creaEventoButton.setBackground(btnColor);
            }
        });
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

        benvenuto.setText("Benvenuto!");

        backButton.addActionListener(e -> {
            frameCreazioneEventi.dispose();
            AreaOrganizzatore nuovaGUI = new AreaOrganizzatore(controller, organizzatore, frameInviti, frameAccesso);
            nuovaGUI.getFrameOrganizzatore().setVisible(true);
        });

        creaEventoButton.addActionListener(e -> {
            String titolo = titoloField.getText();
            String sede = sedeField.getText();

            LocalDate dataInizio;
            LocalDate dataFine;
            LocalDate inizioReg;
            LocalDate fineReg;
            int maxIscritti;
            int dimensione;
            try {
                dataInizio = LocalDate.parse(dataInizioField.getText());
                dataFine = LocalDate.parse(dataFineField.getText());
                inizioReg = LocalDate.parse(inizoRegField.getText());
                fineReg = LocalDate.parse(fineRegField.getText());
                maxIscritti = Integer.parseInt(maxIscrittiField.getText());
                dimensione = Integer.parseInt(dimensioneField.getText());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frameCreazioneEventi, "Controlla i campi: errore di formato.", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Evento nuovoEvento = new Evento(titolo, sede, dataInizio, dataFine, maxIscritti, dimensione, inizioReg, fineReg, organizzatore, new ArrayList<>(), new ArrayList<>());
            Evento eventoCreato = controller.creaEvento(nuovoEvento);
            if(eventoCreato != null) {
                JOptionPane.showMessageDialog(frameCreazioneEventi , "Evento creato con successo");
                frameCreazioneEventi.dispose();
                AreaOrganizzatore nuovaGUI = new AreaOrganizzatore(controller, organizzatore, frameAreaInvito , frameAreaAccesso);
                nuovaGUI.getFrameOrganizzatore().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(frameCreazioneEventi, "Errore nella creazione evento", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    /**
     * Restituisce il frame per la creazione eventi.
     *
     * @return frame della creazione eventi
     */
    public JFrame getFrameCreazioneEventi() {
        return frameCreazioneEventi;
    }
}
