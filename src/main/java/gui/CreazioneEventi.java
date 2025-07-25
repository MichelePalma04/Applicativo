package gui;

import controller.Controller;
import model.Evento;
import model.Organizzatore;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private JLabel titolo;
    private JLabel sede;
    private JLabel dataInizio;
    private JLabel dataFine;
    private JLabel maxIscritti;
    private JLabel dimMax;
    private JLabel inizioReg;
    private JLabel fineReg;
    private JButton backButton;
    private Controller controller;
    private JFrame frameCreazioneEventi;
    private JFrame frameOrganizzatore;
    private JFrame frameAccesso;
    private JFrame frameInviti;
    private Organizzatore organizzatore;

    public CreazioneEventi(Controller controller, JFrame frameAreaOrganizzatore, Organizzatore organizzatore, JFrame frameAreaAccesso, JFrame frameAreaInvito) {
        this.controller = controller;
        this.organizzatore = organizzatore;
        frameOrganizzatore = frameAreaOrganizzatore;
        frameAccesso = frameAreaAccesso;
        frameInviti = frameAreaInvito;
        frameCreazioneEventi = new JFrame("Creazione Eventi");
        frameCreazioneEventi.pack();
        frameCreazioneEventi.setSize(600, 600);
        frameCreazioneEventi.setContentPane(mainPanel);
        frameCreazioneEventi.setLocationRelativeTo(null);
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
        titolo.setFont(labelFont);
        sede.setFont(labelFont);
        dataInizio.setFont(labelFont);
        dataFine.setFont(labelFont);
        maxIscritti.setFont(labelFont);
        dimMax.setFont(labelFont);
        inizioReg.setFont(labelFont);
        fineReg.setFont(labelFont);


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
            public void mouseEntered(java.awt.event.MouseEvent evt) { creaEventoButton.setBackground(btnHoverColor);}
            public void mouseExited(java.awt.event.MouseEvent evt) { creaEventoButton.setBackground(btnColor);}
        });
        backButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { backButton.setBackground(btnHoverColor);}
            public void mouseExited(java.awt.event.MouseEvent evt) { backButton.setBackground(btnColor);}
        });

        benvenuto.setText("Benvenuto!");

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameCreazioneEventi.dispose();
                AreaOrganizzatore nuovaGUI = new AreaOrganizzatore(controller, organizzatore, frameInviti, frameAccesso);
                nuovaGUI.getFrameOrganizzatore().setVisible(true);
            }
        });

       /* creaEventoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String titolo = titoloField.getText();
                String sede = sedeField.getText();
                LocalDate dataInizio = LocalDate.parse(dataInizioField.getText());
                LocalDate dataFine = LocalDate.parse(dataFineField.getText());
                int maxIscritti = Integer.parseInt(maxIscrittiField.getText());
                int dimensione = Integer.parseInt(dimensioneField.getText());
                LocalDate inizioReg = LocalDate.parse(inizoRegField.getText());
                LocalDate fineReg = LocalDate.parse(fineRegField.getText());

                Evento nuovoEvento = controller.creaEvento(titolo, sede, dataInizio, dataFine, maxIscritti, dimensione, inizioReg, fineReg, organizzatore);
                if(nuovoEvento != null) {
                    JOptionPane.showMessageDialog(frameCreazioneEventi , "Evento creato con successo");
                    frameCreazioneEventi.dispose();
                    AreaOrganizzatore nuovaGUI = new AreaOrganizzatore(controller, organizzatore, frameI, frameA);
                    nuovaGUI.frameOrganizzatore.setVisible(true);
                }else{
                    JOptionPane.showMessageDialog(frameCreazioneEventi, "Errore nella creazione evento", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        */

        creaEventoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String titolo = titoloField.getText();
                String sede = sedeField.getText();

                // Controlli di validità e parsing sicuro!
                LocalDate dataInizio, dataFine, inizioReg, fineReg;
                int maxIscritti, dimensione;
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
            }
        });
    }
    public JFrame getFrameCreazioneEventi() {
        return frameCreazioneEventi;
    }
}
