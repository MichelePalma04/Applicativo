package gui;

import controller.Controller;
import model.Evento;

import model.InvitoGiudice;

import javax.swing.*;
import java.awt.*;

import java.util.List;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class VediNotifica {

    private JPanel mainPanel;
    private JPanel panelInviti;
    private JPanel panelbottone;
    private JButton backButton;
    private String loginUtente;
    private JScrollPane scroll;
    private Controller controller;
    private JFrame frameNotifiche;
    private JFrame frameEventi;
    private JFrame frameGiudice;
    private JFrame frameAccesso;
    private JFrame framePartecipante;

    public VediNotifica(Controller controller, String loginUtente, JFrame frameAreaEvento, JFrame frameAreaGiudice, JFrame frameAreaAccesso, JFrame frameAreaPartecipante) {
        this.controller = controller;
        this.loginUtente = loginUtente;
        frameEventi = frameAreaEvento;
        frameGiudice = frameAreaGiudice;
        frameAccesso = frameAreaAccesso;
        framePartecipante = frameAreaPartecipante;

        scroll.getVerticalScrollBar().setUnitIncrement(20);

        Color bgColor = new Color(240, 248, 255);      // chiaro azzurrino
        Color cardColor = new Color(225, 235, 245);    // più scuro per le card/notifiche
        Color btnColor = new Color(30, 144, 255);
        Color btnHoverColor = new Color(65, 105, 225);
        Color borderColor = new Color(210, 210, 210);
        Font labelFont = new Font("SansSerif", Font.BOLD, 16);
        Font fieldFont = new Font("SansSerif", Font.PLAIN, 15);

        if (mainPanel != null) mainPanel.setBackground(bgColor);
        if (panelInviti != null) panelInviti.setBackground(bgColor);
        if (panelbottone != null) panelbottone.setBackground(bgColor);

        // Scroll senza bordo
        if (scroll != null) {
            scroll.getVerticalScrollBar().setUnitIncrement(20);
            scroll.setBorder(BorderFactory.createEmptyBorder());
            scroll.getViewport().setBackground(bgColor);
        }

        // Back button stile
        if (backButton != null) {
            backButton.setBackground(btnColor);
            backButton.setForeground(Color.WHITE);
            backButton.setFont(labelFont);
            backButton.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
            backButton.setFocusPainted(false);
            backButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) { backButton.setBackground(btnHoverColor);}
                public void mouseExited(java.awt.event.MouseEvent evt) { backButton.setBackground(btnColor);}
            });
        }


        frameNotifiche = new JFrame("Inviti ricevuti");
        frameNotifiche.setContentPane(mainPanel);
        frameNotifiche.pack();
        frameNotifiche.setSize(600, 600);
        frameNotifiche.setLocationRelativeTo(null);
        frameNotifiche.setDefaultCloseOperation(EXIT_ON_CLOSE);

        panelInviti.setLayout(new BoxLayout(panelInviti, BoxLayout.Y_AXIS));
        aggiornaInviti();

        backButton.addActionListener(e -> {
            frameNotifiche.setVisible(false);
            frameEventi.dispose();
            ViewEvento nuovo = new ViewEvento(controller, loginUtente, frameAccesso, framePartecipante, frameNotifiche, frameGiudice);
            nuovo.getFrameEventi().setVisible(true);
        });
    }
    private void aggiornaInviti() {
        panelInviti.removeAll();

        Color cardColor = new Color(225, 235, 245);
        Color btnColor = new Color(30, 144, 255);
        Color btnHoverColor = new Color(65, 105, 225);
        Color borderColor = new Color(210, 210, 210);
        Font labelFont = new Font("SansSerif", Font.BOLD, 16);
        Font fieldFont = new Font("SansSerif", Font.PLAIN, 15);

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
                JPanel riga = new JPanel(new FlowLayout(FlowLayout.LEFT,  10, 8));
                riga.setOpaque(false);
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
                    btn.setFont(new Font("SansSerif", Font.BOLD, 13));
                    btn.setFocusPainted(false);
                    btn.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
                    btn.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseEntered(java.awt.event.MouseEvent evt) { btn.setBackground(btnHoverColor);}
                        public void mouseExited(java.awt.event.MouseEvent evt) { btn.setBackground(btnColor);}
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
    public JFrame getFrameNotifiche() {
        return frameNotifiche;
    }
}
