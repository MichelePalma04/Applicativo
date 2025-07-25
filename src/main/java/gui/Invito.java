package gui;

import controller.Controller;
import model.Evento;
import model.Giudice;
import model.Utente;

import javax.swing.*;
import java.awt.*;

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
    private static final String FONT_FAMILY = "SansSerif";
    private int eventoID;

  /*  public Invito(int eventoID, JFrame frameAreaOrganizzatore, Controller controller) {
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
        Font labelFont = new Font(FONT_FAMILY, Font.BOLD, 16);
        Font fieldFont = new Font(FONT_FAMILY, Font.PLAIN, 15);

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
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    invitaComeGiudiceButton.setBackground(btnHoverColor);
                }
                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    invitaComeGiudiceButton.setBackground(btnColor);
                }
            });
        }
        if (homeButton != null) {
            homeButton.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    homeButton.setBackground(btnHoverColor);
                }
                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    homeButton.setBackground(btnColor);
                }
            });
        }

        // ComboBox stile
        if (comboBox1 != null) {
            comboBox1.setFont(fieldFont);
            comboBox1.setBackground(fieldBg);
            comboBox1.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(fieldBorder, 1), BorderFactory.createEmptyBorder(5, 8, 5, 8)));
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
            assegna.setFont(new Font(FONT_FAMILY, Font.BOLD, 13));
            assegna.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
            assegna.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    assegna.setBackground(btnHoverColor);
                }
                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    assegna.setBackground(btnColor);
                }
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
                assegna.addActionListener(e -> {
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
                });
            }

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

        invitaComeGiudiceButton.addActionListener(e -> {
            Utente daInvitare = (Utente) comboBox1.getSelectedItem();
            if (daInvitare != null) {
                if (controller.invitaGiudicePendente(eventoID, daInvitare.getLogin())) {
                    JOptionPane.showMessageDialog(frameInvito, "Giudice invitato con sucesso!");
                    aggiornaListaInvitabili();
                } else {
                    JOptionPane.showMessageDialog(frameInvito, "Errore, impossibile invitare l'utente");
                }
            }
        });
        aggiornaListaInvitabili();
        frameInvito.setVisible(true);
    }
    */

    private void aggiornaListaInvitabili() {
        comboBox1.removeAllItems();
        for (Utente u : controller.getUtentiInvitabili(eventoID)) {
            comboBox1.addItem(u);
        }
    }


    public JFrame getFrameInvito() {
        return frameInvito;
    }

    public Invito(int eventoID, JFrame frameAreaOrganizzatore, Controller controller) {
        this.controller = controller;
        this.eventoID = eventoID;
        frameOrganizzatore = frameAreaOrganizzatore;
        frameInvito = new JFrame("Operazioni organizzatore");
        frameInvito.setContentPane(panel);
        frameInvito.pack();
        frameInvito.setSize(600, 600);
        frameInvito.setLocationRelativeTo(null);
        applicaStili();
        preparaEvento();
        preparaPanelGiudici();
        setupListeners();

        aggiornaListaInvitabili();
        frameInvito.setVisible(true);
    }


    // --- METODO: Stili coerenti con AreaOrganizzatore/CreazioneEventi ---
    private void applicaStili() {
        Color bgColor = new Color(240, 248, 255);
        Color btnColor = new Color(30, 144, 255);
        Color btnHoverColor = new Color(65, 105, 225);
        Color fieldBg = Color.WHITE;
        Color fieldBorder = new Color(210, 210, 210);
        Font labelFont = new Font(FONT_FAMILY, Font.BOLD, 16);
        Font fieldFont = new Font(FONT_FAMILY, Font.PLAIN, 15);

        if (panel != null) panel.setBackground(bgColor);
        if (panel2 != null) panel2.setBackground(bgColor);
        if (panelGiudici != null) panelGiudici.setBackground(bgColor);

        if (invitoLabel != null) invitoLabel.setFont(labelFont);
        if (nomeEvento != null) nomeEvento.setFont(labelFont);

        JButton[] allButtons = {invitaComeGiudiceButton, homeButton};
        for (JButton btn : allButtons) {
            if (btn == null) continue;
            btn.setBackground(btnColor);
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setFont(labelFont);
            btn.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        }
        if (invitaComeGiudiceButton != null) setButtonHover(invitaComeGiudiceButton, btnColor, btnHoverColor);
        if (homeButton != null) setButtonHover(homeButton, btnColor, btnHoverColor);

        if (comboBox1 != null) {
            comboBox1.setFont(fieldFont);
            comboBox1.setBackground(fieldBg);
            comboBox1.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(fieldBorder, 1),
                    BorderFactory.createEmptyBorder(5, 8, 5, 8)
            ));
        }

        if (scrollgiudici != null) {
            scrollgiudici.setBorder(BorderFactory.createEmptyBorder());
            scrollgiudici.getViewport().setBackground(bgColor);
        }
    }

    private void setButtonHover(JButton button, Color normal, Color hover) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hover);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(normal);
            }
        });
    }

    // --- METODO: Prepara dati evento ---
    private void preparaEvento() {
        Evento evento = controller.getEventoById(eventoID);
        if (nomeEvento != null && evento != null) {
            nomeEvento.setText("Evento " + evento.getTitolo());
        }
    }

    // --- METODO: Prepara panelGiudici ---
    private void preparaPanelGiudici() {
        Color bgColor = new Color(240, 248, 255);
        Color btnColor = new Color(30, 144, 255);
        Color btnHoverColor = new Color(65, 105, 225);
        Font fieldFont = new Font(FONT_FAMILY, Font.PLAIN, 15);

        if (panelGiudici == null) return;
        panelGiudici.setLayout(new BoxLayout(panelGiudici, BoxLayout.Y_AXIS));
        List<Giudice> giudici = controller.getGiudiciEvento(eventoID);
        Giudice responsabile = controller.getGiudiceDescrizione(eventoID);

        for (Giudice g : giudici) {
            JPanel riga = creaRigaGiudice(g, bgColor, btnColor, btnHoverColor, fieldFont, responsabile);
            panelGiudici.add(riga);
            panelGiudici.add(Box.createVerticalStrut(2));
        }
    }

    private JPanel creaRigaGiudice(Giudice g, Color bgColor, Color btnColor, Color btnHoverColor, Font fieldFont, Giudice responsabile) {
        JPanel riga = new JPanel();
        riga.setBackground(bgColor);
        riga.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 2));
        riga.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        riga.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel nomiGiudici = new JLabel(g.getLogin());
        nomiGiudici.setFont(fieldFont);

        JButton assegna = new JButton("Assegna come responsabile.");
        assegna.setBackground(btnColor);
        assegna.setForeground(Color.WHITE);
        assegna.setFocusPainted(false);
        assegna.setFont(new Font(FONT_FAMILY, Font.BOLD, 13));
        assegna.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        setButtonHover(assegna, btnColor, btnHoverColor);

        JLabel ruolo = new JLabel("Ruolo: responsabile del problema");
        ruolo.setFont(fieldFont);
        ruolo.setForeground(new Color(30, 120, 30));
        ruolo.setVisible(false);

        if (responsabile != null && responsabile.getLogin().equals(g.getLogin())) {
            assegna.setVisible(false);
            ruolo.setVisible(true);
        } else if (responsabile != null) {
            assegna.setVisible(false);
        } else {
            assegna.addActionListener(e -> assegnaResponsabile(g, ruolo));
        }

        riga.add(nomiGiudici);
        riga.add(ruolo);
        riga.add(assegna);
        return riga;
    }

    private void assegnaResponsabile(Giudice g, JLabel ruolo) {
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

    // --- METODO: Setup listeners principali ---
    private void setupListeners() {
        if (homeButton != null) {
            homeButton.addActionListener(e -> {
                frameInvito.setVisible(false);
                frameOrganizzatore.setVisible(true);
            });
        }

        if (invitaComeGiudiceButton != null) {
            invitaComeGiudiceButton.addActionListener(e -> invitaGiudice());
        }
    }

    private void invitaGiudice() {
        Utente daInvitare = (Utente) comboBox1.getSelectedItem();
        if (daInvitare != null) {
            if (controller.invitaGiudicePendente(eventoID, daInvitare.getLogin())) {
                JOptionPane.showMessageDialog(frameInvito, "Giudice invitato con successo!");
                aggiornaListaInvitabili();
            } else {
                JOptionPane.showMessageDialog(frameInvito, "Errore, impossibile invitare l'utente");
            }
        }
    }

}
