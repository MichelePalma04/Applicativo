package gui;

import controller.Controller;
import model.*;

import javax.swing.*;
import java.awt.*;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

import java.io.File;
import java.io.IOException;

public class VisualizzaDocumenti {
    private JLabel documentiTeam;
    private JPanel mainpanel;
    private JScrollPane scroll;
    private JPanel panelDocumenti;
    private JButton backButton;
    private JFrame frameDocumenti;
    private Controller controller;
    private JFrame frameGiudice;
    private String loginGiudice;

    private static final String FONT_FAMILY = "SansSerif";

    public VisualizzaDocumenti(Controller controller, String nomeTeam, int eventoId, JFrame frameAreaGiudice, String giudiceLogin) {
        this.controller = controller;
        this.loginGiudice = giudiceLogin;
        frameGiudice = frameAreaGiudice;

        Color bgColor = new Color(240, 248, 255);      // chiaro azzurrino
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

        // Scroll solo verticale per il panelDocumenti
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

    public JFrame getFrameDocumenti () {
        return frameDocumenti;
    }
}


