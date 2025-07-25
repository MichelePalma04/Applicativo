package gui;

import controller.Controller;
import model.Evento;
import model.Organizzatore;

import javax.swing.*;
import java.awt.*;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

import java.time.LocalDate;
import java.util.ArrayList;

public class CreazioneEventi {
    private JPanel mainPanel;
    private JLabel benvenuto;
    private JTextField titoloField;
    private JTextField sedeField;
    private JButton creaEventoButton;
    private JTextField dataInizioField;
    private JTextField dataFineField;
    private JTextField maxIscrittiField;
    private JTextField dimensioneField;
    private JTextField inizoRegField;
    private JTextField fineRegField;
    private JLabel titoloLabel;
    private JLabel sedeLabel;
    private JLabel dataInizioLabel;
    private JLabel dataFineLabel;
    private JLabel maxIscrittiLabel;
    private JLabel dimMaxLabel;
    private JLabel inizioRegLabel;
    private JLabel fineRegLabel;
    private JButton backButton;
    private Controller controller;
    private JFrame frameCreazioneEventi;
    private JFrame frameAccesso;
    private JFrame frameInviti;
    private Organizzatore organizzatore;

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

        // --- Stile coerente con AreaOrganizzatore ---
        Color bgColor = new Color(240, 248, 255);      // chiaro azzurrino
        Color btnColor = new Color(30, 144, 255);
        Color btnHoverColor = new Color(65, 105, 225);
        Color fieldBg = Color.WHITE;
        Color fieldBorder = new Color(210, 210, 210);
        Font labelFont = new Font("SansSerif", Font.BOLD, 16);
        Font fieldFont = new Font("SansSerif", Font.PLAIN, 15);

        mainPanel.setBackground(bgColor);

        // Label styling
        benvenuto.setFont(labelFont);
        titoloLabel.setFont(labelFont);
        sedeLabel.setFont(labelFont);
        dataInizioLabel.setFont(labelFont);
        dataFineLabel.setFont(labelFont);
        maxIscrittiLabel.setFont(labelFont);
        dimMaxLabel.setFont(labelFont);
        inizioRegLabel.setFont(labelFont);
        fineRegLabel.setFont(labelFont);


        // Field styling
        JTextField[] allFields = {titoloField, sedeField, dataInizioField, dataFineField, maxIscrittiField, dimensioneField, inizoRegField, fineRegField};
        for (JTextField field : allFields) {
            field.setFont(fieldFont);
            field.setBackground(fieldBg);
            field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(fieldBorder, 1),
                    BorderFactory.createEmptyBorder(8, 8, 8, 8)
            ));
        }

        // Button styling
        JButton[] allButtons = {creaEventoButton, backButton};
        for (JButton btn : allButtons) {
            btn.setBackground(btnColor);
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setFont(labelFont);
            btn.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        }

        // Effetto hover pulsanti
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

            // Controlli di validità e parsing sicuro!
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
    public JFrame getFrameCreazioneEventi() {
        return frameCreazioneEventi;
    }
}
