package gui;

import controller.Controller;
import model.Evento;
import model.Organizzatore;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

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
    public JFrame frameCreazioneEventi;
    public JFrame frameOrganizzatore;
    public JFrame frameAccesso;
    public JFrame frameInviti;
    private Organizzatore organizzatore;

    public CreazioneEventi(Controller controller, JFrame frameO, Organizzatore organizzatore, JFrame frameA, JFrame frameI) {
        this.controller = controller;
        this.organizzatore = organizzatore;
        frameOrganizzatore = frameO;
        frameAccesso = frameA;
        frameInviti = frameI;
        frameCreazioneEventi = new JFrame("Creazione Eventi");
        frameCreazioneEventi.setSize(500, 500);
        frameCreazioneEventi.setContentPane(mainPanel);
        frameCreazioneEventi.pack();
        frameCreazioneEventi.setLocationRelativeTo(null);
        benvenuto.setText("Benvenuto!");

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameCreazioneEventi.dispose();
                AreaOrganizzatore nuovaGUI = new AreaOrganizzatore(controller, organizzatore, frameI, frameA);
                nuovaGUI.frameOrganizzatore.setVisible(true);
              // frameOrganizzatore.setVisible(true);
            }
        });

        creaEventoButton.addActionListener(new ActionListener() {
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
    }
}
