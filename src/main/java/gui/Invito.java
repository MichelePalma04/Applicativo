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
        frameInvito.setSize(500, 500);
        frameInvito.setLocationRelativeTo(null);

        Evento evento = controller.getEventoById(eventoID);

        nomeEvento.setText("Evento " + evento.getTitolo());

        panelGiudici.setLayout(new BoxLayout(panelGiudici, BoxLayout.Y_AXIS));
        for(Giudice g : controller.getGiudiciEvento(eventoID)) {
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
            }

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
