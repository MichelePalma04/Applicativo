package gui;

import controller.Controller;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class VisualizzaDocumenti {
    private JLabel documentiTeam;
    private JPanel mainpanel;
    private JScrollPane scroll;
    private JPanel panelDocumenti;
    private JButton backButton;
    public JFrame frameDocumenti;
    private Controller controller;
    public JFrame frameGiudice;
    private String loginGiudice;
    private String loginPartecipante;

    public VisualizzaDocumenti(Controller controller, String nomeTeam, int eventoId, JFrame frame, String loginGiudice, String loginPartecipante) {
        this.controller = controller;
        this.loginGiudice = loginGiudice;
        this.loginPartecipante = loginPartecipante;
        frameGiudice = frame;
        frameDocumenti = new JFrame("Documenti");
        frameDocumenti.setContentPane(mainpanel);
        frameDocumenti.pack();
        frameDocumenti.setSize(600, 600);
        frameDocumenti.setLocationRelativeTo(null);
        documentiTeam.setText("Documenti del team: " + nomeTeam);
        scroll.getVerticalScrollBar().setUnitIncrement(20);

        panelDocumenti.setLayout(new BoxLayout(panelDocumenti, BoxLayout.Y_AXIS));
        for(Documento doc: controller.getDocumentiTeamEvento(nomeTeam, eventoId)) {
            JPanel rigaDocumenti = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel nomeDoc = new JLabel(doc.getFile().getName());
            JButton visualizzaDoc = new JButton("Visualizza");
            JButton commentaDoc = new JButton("Commenta");
            JTextArea commentaDocText = new JTextArea(2,20);
            JScrollPane commentaDocScrollPane = new JScrollPane(commentaDocText);
            commentaDocScrollPane.getVerticalScrollBar().setUnitIncrement(20);


            visualizzaDoc.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed (ActionEvent e){
                    apriFile (doc.getFile());
                }
            });

            commentaDoc.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed (ActionEvent e){
                    String commento = commentaDocText.getText();
                    if(!commento.trim().isEmpty()) {
                        boolean ok = controller.aggiungiCommentoGiudice(doc.getId(),loginGiudice, eventoId, commento);
                        if(ok){
                            JOptionPane.showMessageDialog(frameDocumenti, "Commento aggiunto con successo.");
                        }else{
                            JOptionPane.showMessageDialog(frameDocumenti, "Errore nel salvataggio del commento.", "error", JOptionPane.ERROR_MESSAGE);
                        }
                    }else{
                        JOptionPane.showMessageDialog(frameDocumenti, "Il commento non può essere vuoto!", "error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            rigaDocumenti.add(nomeDoc);
            rigaDocumenti.add(visualizzaDoc);
            rigaDocumenti.add(commentaDocText);
            rigaDocumenti.add(commentaDoc);
            rigaDocumenti.add(commentaDocScrollPane);
            panelDocumenti.add(rigaDocumenti);
        }

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameDocumenti.setVisible(false);
                frameGiudice.setVisible(true);
            }
        });
    }

    public void apriFile (File f){
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try{
                desktop.open(f);
            }catch(IOException ex){
                JOptionPane.showMessageDialog(frameDocumenti,"Impossibile aprire il file " + f.getName(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }else{
            JOptionPane.showMessageDialog(frameDocumenti, "Funzionalità non supportata dal sistema operativo.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}


