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

    public VisualizzaDocumenti(Controller controller, String nomeTeam, int eventoId, JFrame frame) {
        this.controller = controller;
        frameGiudice = frame;
        frameDocumenti = new JFrame("Documenti");
        frameDocumenti.setContentPane(mainpanel);
        frameDocumenti.pack();
        frameDocumenti.setSize(500, 500);
        frameDocumenti.setLocationRelativeTo(null);
        documentiTeam.setText("Documenti del team: " + nomeTeam);
        scroll.getVerticalScrollBar().setUnitIncrement(20);

        panelDocumenti.setLayout(new BoxLayout(panelDocumenti, BoxLayout.Y_AXIS));
        for(Documento doc: controller.getDocumentiTeamEvento(nomeTeam, eventoId)) {
            JPanel rigaDocumenti = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel nomeDoc = new JLabel(doc.getFile().getName());
            JButton visualizzaDoc = new JButton("Visualizza");


            visualizzaDoc.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed (ActionEvent e){
                    apriFile (doc.getFile());
                }
            });
            rigaDocumenti.add(nomeDoc);
            rigaDocumenti.add(visualizzaDoc);
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


