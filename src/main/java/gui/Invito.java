package gui;

import controller.Controller;
import model.Evento;
import model.Giudice;
import model.Utente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Invito {
    private JPanel panel;
    private JComboBox <Utente> comboBox1;
    private JLabel invitoLabel;
    private JButton invitaComeGiudiceButton;
    private JPanel panel2;
    private JLabel nomeEvento;
    private JButton homeButton;
    private JScrollPane scrollgiudici;
    private JPanel panelGiudici;
    private JFrame frameInvito;
    private JFrame frameOrganizzatore;
    private Controller controller;

    private int eventoID;

    public Invito(int eventoID, JFrame frameAreaOrganizzatore, Controller controller) {
        this.controller = controller;
        this.eventoID = eventoID;

        frameOrganizzatore = frameAreaOrganizzatore;

        frameInvito = new JFrame("Operazioni organizzatore");
        frameInvito.setContentPane(panel);
        frameInvito.pack();
        frameInvito.setSize(600, 600);
        frameInvito.setLocationRelativeTo(null);
        // --- Stili coerenti con AreaOrganizzatore/CreazioneEventi ---
        Color bgColor = new Color(240, 248, 255);      // chiaro azzurrino
        Color btnColor = new Color(30, 144, 255);
        Color btnHoverColor = new Color(65, 105, 225);
        Color fieldBg = Color.WHITE;
        Color fieldBorder = new Color(210, 210, 210);
        Font labelFont = new Font("SansSerif", Font.BOLD, 16);
        Font fieldFont = new Font("SansSerif", Font.PLAIN, 15);

        // Pannelli
        if (panel != null) panel.setBackground(bgColor);
        if (panel2 != null) panel2.setBackground(bgColor);
        if (panelGiudici != null) panelGiudici.setBackground(bgColor);

        // Label
        if (invitoLabel != null) invitoLabel.setFont(labelFont);
        if (nomeEvento != null) nomeEvento.setFont(labelFont);

        // Bottoni stile
        JButton[] allButtons = {invitaComeGiudiceButton, homeButton};
        for (JButton btn : allButtons) {
            if (btn == null) continue;
            btn.setBackground(btnColor);
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setFont(labelFont);
            btn.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        }
        // Effetto hover bottoni
        if (invitaComeGiudiceButton != null) {
            invitaComeGiudiceButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) { invitaComeGiudiceButton.setBackground(btnHoverColor);}
                public void mouseExited(java.awt.event.MouseEvent evt) { invitaComeGiudiceButton.setBackground(btnColor);}
            });
        }
        if (homeButton != null) {
            homeButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) { homeButton.setBackground(btnHoverColor);}
                public void mouseExited(java.awt.event.MouseEvent evt) { homeButton.setBackground(btnColor);}
            });
        }

        // ComboBox stile
        if (comboBox1 != null) {
            comboBox1.setFont(fieldFont);
            comboBox1.setBackground(fieldBg);
            comboBox1.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(fieldBorder, 1),
                    BorderFactory.createEmptyBorder(5, 8, 5, 8)
            ));
        }

        // ScrollPane stile (no bordo nero)
        if (scrollgiudici != null) {
            scrollgiudici.setBorder(BorderFactory.createEmptyBorder());
            scrollgiudici.getViewport().setBackground(bgColor);
        }

        frameInvito = new JFrame("Operazioni organizzatore");
        frameInvito.setContentPane(panel);
        frameInvito.setSize(600, 600);
        frameInvito.setLocationRelativeTo(null);

        Evento evento = controller.getEventoById(eventoID);

        nomeEvento.setText("Evento " + evento.getTitolo());

        panelGiudici.setLayout(new BoxLayout(panelGiudici, BoxLayout.Y_AXIS));
        for(Giudice g : controller.getGiudiciEvento(eventoID)) {
            JPanel riga = new JPanel();
            riga.setBackground(bgColor);
            riga.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 2));
            riga.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32)); // altezza compatta
            riga.setAlignmentX(Component.LEFT_ALIGNMENT);


            JLabel nomiGiudici = new JLabel(g.getLogin());
            nomiGiudici.setFont(fieldFont);

            JButton assegna = new JButton("Assegna come responsabile.");
            assegna.setBackground(btnColor);
            assegna.setForeground(Color.WHITE);
            assegna.setFocusPainted(false);
            assegna.setFont(new Font("SansSerif", Font.BOLD, 13));
            assegna.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
            assegna.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) { assegna.setBackground(btnHoverColor);}
                public void mouseExited(java.awt.event.MouseEvent evt) { assegna.setBackground(btnColor);}
            });

            JLabel ruolo = new JLabel("Ruolo: responsabile del problema");
            ruolo.setFont(fieldFont);
            ruolo.setForeground(new Color(30, 120, 30));
            ruolo.setVisible(false);

            Giudice responsabile = controller.getGiudiceDescrizione(eventoID);
            if (responsabile != null && responsabile.getLogin().equals(g.getLogin())) {
                assegna.setVisible(false);
                ruolo.setVisible(true);
            } else if (responsabile != null) {
                assegna.setVisible(false);
            } else {
                assegna.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        boolean ok = controller.assegnaGiudiceDescrizione(eventoID, g.getLogin());
                        if (ok) {
                            for (Component comp : panelGiudici.getComponents()) {
                                if (comp instanceof JPanel rigaPanel) {
                                    for (Component c : rigaPanel.getComponents()) {
                                        if (c instanceof JButton) c.setVisible(false);
                                        if (c instanceof JLabel ruoloLbl && ((JLabel) c).getText().equals("Ruolo: responsabile del problema"))
                                            ruoloLbl.setVisible(false);
                                    }
                                }
                            }
                            ruolo.setVisible(true);
                            JOptionPane.showMessageDialog(frameInvito, g.getLogin() + " è stato assegnato come giudice responsabile della descrizione del problema.");
                        } else {
                            JOptionPane.showMessageDialog(frameInvito, "Errore nell'assegnazione del responsabile.");
                        }
                    }
                });
            }
        /*for(Giudice g : controller.getGiudiciEvento(eventoID)) {
            JPanel riga = new JPanel();
            riga.setLayout(new FlowLayout(FlowLayout.LEFT));
            JLabel nomiGiudici = new JLabel(g.getLogin());
            JButton assegna = new JButton("Assegna come responsabile.");
            JLabel ruolo = new JLabel("Ruolo: responsabile del problema");
            ruolo.setVisible(false);

            Giudice responsabile = controller.getGiudiceDescrizione(eventoID);
            if (responsabile != null && responsabile.getLogin().equals(g.getLogin())) {
                assegna.setVisible(false);
                ruolo.setVisible(true);
            } else if (responsabile != null) {
                assegna.setVisible(false);
            } else {
                assegna.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        boolean ok = controller.assegnaGiudiceDescrizione(eventoID, g.getLogin());
                        if (ok) {
                            for (Component comp : panelGiudici.getComponents()) {
                                if (comp instanceof JPanel rigaPanel) {
                                    for (Component c : rigaPanel.getComponents()) {
                                        if (c instanceof JButton) c.setVisible(false);
                                        if (c instanceof JLabel ruoloLbl && ((JLabel) c).getText().equals("Ruolo: responsabile del problema"))
                                            ruoloLbl.setVisible(false);
                                    }
                                }
                            }
                            ruolo.setVisible(true);
                            JOptionPane.showMessageDialog(frameInvito, g.getLogin() + " è stato assegnato come giudice responsabile della descrizione del problema.");
                        } else {
                            JOptionPane.showMessageDialog(frameInvito, "Errore nell'assegnazione del responsabile.");
                        }
                    }
                });
            }*/

            /*
            if (evento.getGiudiceDescrizione() != null) {
                if (evento.getGiudiceDescrizione().equals(g)) {
                    assegna.setVisible(false);
                    ruolo.setVisible(true);
                }
                assegna.setVisible(false);
            }else {
                assegna.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        controller.assegnaGiudiceDescrizione(evento, g);
                        for (Component comp : panelGiudici.getComponents()) {
                            if (comp instanceof JPanel) {
                                JPanel riga = (JPanel) comp;
                                for (Component c : riga.getComponents()) {
                                    if (c instanceof JButton) {
                                        c.setVisible(false); //non sono più visibili tutti i bottoni
                                    }
                                    if (c instanceof JLabel) {
                                        JLabel ruolo = (JLabel) c;
                                        if (ruolo.getText().equals("Ruolo: responsabile del problema")) {
                                            ruolo.setVisible(false);
                                        }
                                    }
                                }
                            }
                        }
                        ruolo.setVisible(true);
                        JOptionPane.showMessageDialog(frameInvito, g.getLogin() + " è stato assegnato come giudice responsabile della descrizione del problema.");
                    }
                });
            }

             */

            riga.add(nomiGiudici);
            riga.add(ruolo);
            riga.add(assegna);
            panelGiudici.add(riga);
            panelGiudici.add(Box.createVerticalStrut(2));
        }

        homeButton.addActionListener(e -> {
            frameInvito.setVisible(false);
            frameOrganizzatore.setVisible(true);
        });

        invitaComeGiudiceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Utente daInvitare = (Utente) comboBox1.getSelectedItem();
                if (daInvitare != null) {
                    if (controller.invitaGiudicePendente(eventoID, daInvitare.getLogin())) {
                        JOptionPane.showMessageDialog(frameInvito, "Giudice invitato con sucesso!");
                        aggiornaListaInvitabili();
                    } else {
                        JOptionPane.showMessageDialog(frameInvito, "Errore, impossibile invitare l'utente");
                    }
                }
            }
        });
        aggiornaListaInvitabili();
        frameInvito.setVisible(true);
    }

    private void aggiornaListaInvitabili() {
        comboBox1.removeAllItems();
        for (Utente u : controller.getUtentiInvitabili(eventoID)) {
            comboBox1.addItem(u);
        }
    }

    public JFrame getFrameInvito() {
        return frameInvito;
    }

}
